package projeto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Main {

	private static int numDocumentos = 0;
	private static int numComparacoes = 0;
	private static final String ARQUIVO_LOG = "resultado.txt";

	private static double tempoLeitura = 0;
	private static double tempoComparacao = 0;
	private static double tempoInsercao = 0;
	private static double tempoBusca = 0;
	private static double tempoExecucao = 0;

	private static int somaColisoes = 0;

	public static void main(String[] args) throws IOException {
		long inicio = System.currentTimeMillis();

		String caminho = args[0];
		double similaridade = Double.parseDouble(args[1]);
		if (similaridade < 0 || similaridade > 1) {
			throw new IllegalArgumentException("O valor de similaridade deve estar entre 0 e 1.");
		}

		Map<String, Texto> mapaArquivos = Main.carregarArquivos(caminho);
		AVL arvore = Main.construirAVL(mapaArquivos);
		List<ParComparacao> lista = new ArrayList<ParComparacao>();

		long inicioBusca = System.currentTimeMillis();

		if (args.length == 3 && args[2].equalsIgnoreCase("lista")) {
			lista = arvore.listar(similaridade);
			tempoBusca = System.currentTimeMillis() - inicioBusca;
			tempoExecucao = System.currentTimeMillis() - inicio;
			Main.exibirResultado(similaridade, lista, arvore);
			Main.salvarLog(ARQUIVO_LOG, similaridade, lista, arvore);

		} else if (args.length == 4 && args[2].equalsIgnoreCase("topK")) {
			int k = Integer.parseInt(args[3]);
			lista = arvore.buscarTopK(k, similaridade);
			tempoBusca = System.currentTimeMillis() - inicioBusca;
			tempoExecucao = System.currentTimeMillis() - inicio;
			Main.exibirResultado(similaridade, lista, arvore);
			Main.salvarLog(ARQUIVO_LOG, similaridade, lista, arvore);

		} else if (args.length == 5 && args[2].equalsIgnoreCase("busca")) {
			double resultadoBusca = Main.compararDocs(args[3], args[4], mapaArquivos);
			tempoBusca = System.currentTimeMillis() - inicioBusca;
			tempoExecucao = System.currentTimeMillis() - inicio;
			Main.exibirResultado(args[3], args[4], resultadoBusca);
			Main.salvarLog(ARQUIVO_LOG, args[3], args[4], resultadoBusca);

		} else {
			System.out.println("Comando inválido! Use:");
			System.out.println("   java projeto/Main docs <similaridade> lista");
			System.out.println("   java projeto/Main docs <similaridade> topK <K>");
			System.out.println("   java projeto/Main docs <similaridade> busca <doc1> <doc2>");
		}
	}

	public static double compararDocs(String arqA, String arqB, Map<String, Texto> mapaArquivos) {
		if (arqA.equals(arqB))
			return 1.0;

		Texto docA = mapaArquivos.get(arqA);
		Texto docB = mapaArquivos.get(arqB);

		if (docA == null || docB == null)
			throw new IllegalArgumentException("Arquivos inválidos.");

		TabelaHash tabA = docA.getTabela();
		TabelaHash tabB = docB.getTabela();

		AnalisadorSimilaridade comparador = new AnalisadorSimilaridade(tabA, tabB);

		return comparador.calcularSimilaridade();
	}

	public static Map<String, Texto> carregarArquivos(String caminhoPasta) throws IOException {
		long inicio = System.currentTimeMillis();

		File pasta = new File(caminhoPasta);
		if (!pasta.isDirectory())
			throw new IllegalStateException("O caminho especificado não é um diretório válido: " + pasta.getPath());

		File[] arquivos = pasta.listFiles();
		if (arquivos == null)
			throw new IOException("Não foi possível listar os arquivos da pasta: " + pasta.getPath());

		Map<String, Texto> mapaArquivos = new HashMap<String, Texto>();
		for (File f : arquivos) {
			if (f.isFile() && f.getName().endsWith(".txt")) {
				Texto doc = new Texto(f.getPath());
				mapaArquivos.put(f.getName(), doc);
				somaColisoes += doc.getTabela().getColisoes();
				numDocumentos++;
			}
		}

		tempoLeitura = System.currentTimeMillis() - inicio;
		return mapaArquivos;
	}

	public static AVL construirAVL(Map<String, Texto> mapaArquivos) {
		List<Texto> lista = new ArrayList<>(mapaArquivos.values());
		AVL arvore = new AVL();

		for (int i = 0; i < lista.size(); i++) {
			for (int j = i + 1; j < lista.size(); j++) {

				Texto docA = lista.get(i);
				Texto docB = lista.get(j);

				TabelaHash tabA = docA.getTabela();
				TabelaHash tabB = docB.getTabela();

				long inicioComp = System.currentTimeMillis();
				AnalisadorSimilaridade comparador = new AnalisadorSimilaridade(tabA, tabB);
				double sim = comparador.calcularSimilaridade();
				tempoComparacao += System.currentTimeMillis() - inicioComp;

				ParComparacao res = new ParComparacao(docA.getNomeArquivo(), docB.getNomeArquivo(), sim);

				long inicioIns = System.currentTimeMillis();
				arvore.adicionar(res);
				tempoInsercao += System.currentTimeMillis() - inicioIns;

				numComparacoes++;
			}
		}

		return arvore;
	}

	private static String montarSaidaLista(double similaridade, List<ParComparacao> lista, AVL arvore) {
		StringBuilder sb = new StringBuilder();
		String nl = System.lineSeparator();

		sb.append("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===").append(nl);
		sb.append("Total de documentos processados: ").append(numDocumentos).append(nl);
		sb.append("Total de pares comparados: ").append(numComparacoes).append(nl);
		sb.append("Função hash utilizada: Double Hashing").append(nl);
		sb.append("Métrica de similaridade: Cosseno").append(nl);
		sb.append(nl);

		sb.append("=== ESTATÍSTICAS DA AVL ===").append(nl);
		sb.append("Altura da árvore: ").append(arvore.obterAlturArvore()).append(nl);
		sb.append("Número de nós: ").append(arvore.contarTotalNos()).append(nl);
		sb.append("Rotações simples realizadas: ").append(arvore.getQtdRotacoesSimples()).append(nl);
		sb.append("Rotações duplas realizadas: ").append(arvore.getQtdRotacoesDuplas()).append(nl);
		sb.append("Total de rotações: ").append(arvore.getQtdRotacoesTotal()).append(nl);
		sb.append(nl);

		sb.append("=== ESTATÍSTICAS DA TABELA HASH ===").append(nl);
		sb.append("Total de colisões: ").append(somaColisoes).append(nl);
		sb.append(nl);

		sb.append("=== MEDIÇÕES DE TEMPO ===").append(nl);
		sb.append("Tempo de leitura dos documentos: ").append(String.format("%.4f", tempoLeitura)).append(" ms").append(nl);
		sb.append("Tempo de comparações (similaridade): ").append(String.format("%.4f", tempoComparacao)).append(" ms").append(nl);
		sb.append("Tempo de inserção na AVL: ").append(String.format("%.4f", tempoInsercao)).append(" ms").append(nl);
		sb.append("Tempo de busca na AVL: ").append(String.format("%.4f", tempoBusca)).append(" ms").append(nl);
		sb.append("Tempo total de execução: ").append(String.format("%.4f", tempoExecucao)).append(" ms").append(nl);
		sb.append(nl);

		sb.append("Pares com similaridade >= ").append(similaridade).append(":").append(nl);
		sb.append("---------------------------------").append(nl);

		if (lista.isEmpty()) {
			sb.append("Nenhum par encontrado.").append(nl);
		} else {
			for (ParComparacao r : lista) {
				sb.append(r.getArquivoA()).append(" <-> ").append(r.getArquivoB())
						.append(" = ").append(String.format("%.4f", r.getSimilaridade())).append(nl);
			}
		}

		sb.append(nl);
		sb.append("Pares com menor similaridade:").append(nl);
		sb.append("---------------------------------").append(nl);

		ParComparacao menor = arvore.obterMenorSimilaridade();
		if (menor != null) {
			sb.append(menor.getArquivoA()).append(" <-> ").append(menor.getArquivoB())
					.append(" = ").append(String.format("%.4f", menor.getSimilaridade())).append(nl);
		} else {
			sb.append("N/A (nenhum par comparado)").append(nl);
		}

		return sb.toString();
	}

	private static String montarSaidaBusca(String arqA, String arqB, double similaridade) {
		StringBuilder sb = new StringBuilder();
		String nl = System.lineSeparator();

		sb.append("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===").append(nl);
		sb.append("Comparando: ").append(arqA).append(" <-> ").append(arqB).append(nl);
		sb.append("Similaridade calculada: ").append(String.format("%.4f", similaridade)).append(nl);
		sb.append("Métrica de similaridade: Cosseno").append(nl);
		sb.append(nl);
		sb.append("=== MEDIÇÕES DE TEMPO ===").append(nl);
		sb.append("Tempo total de execução: ").append(String.format("%.4f", tempoExecucao)).append(" ms").append(nl);

		return sb.toString();
	}

	public static void exibirResultado(double similaridade, List<ParComparacao> lista, AVL arvore) {
		String saida = montarSaidaLista(similaridade, lista, arvore);
		System.out.println(saida);
	}

	public static void exibirResultado(String arqA, String arqB, double similaridade) {
		String saida = montarSaidaBusca(arqA, arqB, similaridade);
		System.out.println(saida);
	}

	public static void salvarLog(String nomeArquivo, double similaridade, List<ParComparacao> lista, AVL arvore)
			throws IOException {
		String conteudo = montarSaidaLista(similaridade, lista, arvore);
		Path caminhoLog = Path.of(nomeArquivo);

		Files.writeString(caminhoLog, conteudo, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

		System.out.println("Log gerado em: " + caminhoLog.toAbsolutePath());
	}

	public static void salvarLog(String nomeArquivo, String arqA, String arqB, double similaridade)
			throws IOException {
		String conteudo = montarSaidaBusca(arqA, arqB, similaridade);
		Path caminhoLog = Path.of(nomeArquivo);

		Files.writeString(caminhoLog, conteudo, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

		System.out.println("Log gerado em: " + caminhoLog.toAbsolutePath());
	}
}