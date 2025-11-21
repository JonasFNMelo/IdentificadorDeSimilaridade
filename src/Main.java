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

	private static int contadorDocumentos = 0;
	private static int contadorComparacoes = 0;
	private static final String NOME_ARQUIVO_LOG = "resultado.txt";

	// Medidores de tempo (em milissegundos)
	private static long tempoLeituraDocumentos = 0;
	private static long tempoComparacoes = 0;
	private static long tempoInsercaoAVL = 0;
	private static long tempoBuscaAVL = 0;
	private static long tempoTotal = 0;

	// Estatísticas de colisões
	private static int totalColisoes = 0;

	public static void main(String[] args) throws IOException {
		long inicioTotal = System.currentTimeMillis();

		String caminho = args[0];
		double similaridade = Double.parseDouble(args[1]);
		if (similaridade < 0 || similaridade > 1) {
			throw new IllegalArgumentException("O valor de similaridade deve estar entre 0 e 1.");
		}

		Map<String, Documento> tabelaArquivos = Main.getTabelaArquivos(caminho);
		AVL arvore = Main.popularAVL(tabelaArquivos);
		List<Resultado> lista = new ArrayList<Resultado>();

		long inicioBusca = System.currentTimeMillis();

		if (args.length == 3 && args[2].equalsIgnoreCase("lista")) {
			lista = arvore.lista(similaridade);
			tempoBuscaAVL = System.currentTimeMillis() - inicioBusca;
			tempoTotal = System.currentTimeMillis() - inicioTotal;
			Main.printarSaida(similaridade, lista, arvore);
			Main.gerarLog(NOME_ARQUIVO_LOG, similaridade, lista, arvore);

		} else if (args.length == 4 && args[2].equalsIgnoreCase("topK")) {
			int K = Integer.parseInt(args[3]);
			lista = arvore.topK(K, similaridade);
			tempoBuscaAVL = System.currentTimeMillis() - inicioBusca;
			tempoTotal = System.currentTimeMillis() - inicioTotal;
			Main.printarSaida(similaridade, lista, arvore);
			Main.gerarLog(NOME_ARQUIVO_LOG, similaridade, lista, arvore);

		} else if (args.length == 5 && args[2].equalsIgnoreCase("busca")) {
			double resultadoBusca = Main.busca(args[3], args[4], tabelaArquivos);
			tempoBuscaAVL = System.currentTimeMillis() - inicioBusca;
			tempoTotal = System.currentTimeMillis() - inicioTotal;
			Main.printarSaida(args[3], args[4], resultadoBusca);
			Main.gerarLog(NOME_ARQUIVO_LOG, args[3], args[4], resultadoBusca);

		} else {
			System.out.println("Comando inválido! Use:");
			System.out.println("   java projeto/Main docs <similaridade> lista");
			System.out.println("   java projeto/Main docs <similaridade> topK <K>");
			System.out.println("   java projeto/Main docs <similaridade> busca <doc1> <doc2>");
		}
	}

	public static double busca(String arquivoA, String arquivoB, Map<String, Documento> tabelaArquivos) {
		if (arquivoA.equals(arquivoB))
			return 1.0;

		Documento docA = tabelaArquivos.get(arquivoA);
		Documento docB = tabelaArquivos.get(arquivoB);

		if (docA == null || docB == null)
			throw new IllegalArgumentException("Arquivos inválidos.");

		TabelaHash tabA = docA.getTabela();
		TabelaHash tabB = docB.getTabela();

		ComparadorCosseno comparador = new ComparadorCosseno(tabA, tabB);

		return comparador.similaridade();
	}

	public static Map<String, Documento> getTabelaArquivos(String caminhoPasta) throws IOException {
		long inicio = System.currentTimeMillis();

		File pasta = new File(caminhoPasta);
		if (!pasta.isDirectory())
			throw new IllegalStateException("O caminho especificado não é um diretório válido: " + pasta.getPath());

		File[] arquivosPasta = pasta.listFiles();
		if (arquivosPasta == null)
			throw new IOException("Não foi possível listar os arquivos da pasta: " + pasta.getPath());

		Map<String, Documento> tabelaArquivos = new HashMap<String, Documento>();
		for (File f : arquivosPasta) {
			if (f.isFile() && f.getName().endsWith(".txt")) {
				Documento doc = new Documento(f.getPath());
				tabelaArquivos.put(f.getName(), doc);
				totalColisoes += doc.getTabela().getColisoes();
				contadorDocumentos++;
			}
		}

		tempoLeituraDocumentos = System.currentTimeMillis() - inicio;
		return tabelaArquivos;
	}

	// Adicione este debug no método popularAVL para verificar:

	public static AVL popularAVL(Map<String, Documento> tabelaArquivos) {
		List<Documento> lista = new ArrayList<>(tabelaArquivos.values());
		AVL arvore = new AVL();

		for (int i = 0; i < lista.size(); i++) {
			for (int j = i + 1; j < lista.size(); j++) {

				Documento docA = lista.get(i);
				Documento docB = lista.get(j);

				TabelaHash tabA = docA.getTabela();
				TabelaHash tabB = docB.getTabela();

				long inicioComp = System.currentTimeMillis();
				ComparadorCosseno comparador = new ComparadorCosseno(tabA, tabB);
				double similaridade = comparador.similaridade();
				tempoComparacoes += System.currentTimeMillis() - inicioComp;

				// DEBUG: Imprimir cada similaridade calculada
				System.out.println("DEBUG: " + docA.getNomeArquivo() + " <-> "
						+ docB.getNomeArquivo() + " = " + similaridade);

				Resultado res = new Resultado(docA.getNomeArquivo(), docB.getNomeArquivo(), similaridade);

				long inicioInsercao = System.currentTimeMillis();
				arvore.inserir(res);
				tempoInsercaoAVL += System.currentTimeMillis() - inicioInsercao;

				// DEBUG: Imprimir quantidade de nós após cada inserção
				System.out.println("DEBUG: Nós na AVL = " + arvore.contarNos());

				contadorComparacoes++;
			}
		}

		return arvore;
	}

	private static String formatarSaidaLista(double similaridade, List<Resultado> lista, AVL arvore) {
		StringBuilder sb = new StringBuilder();
		String nl = System.lineSeparator();

		sb.append("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===").append(nl);
		sb.append("Total de documentos processados: ").append(contadorDocumentos).append(nl);
		sb.append("Total de pares comparados: ").append(contadorComparacoes).append(nl);
		sb.append("Função hash utilizada: Double Hashing").append(nl);
		sb.append("Métrica de similaridade: Cosseno").append(nl);
		sb.append(nl);

		// Estatísticas da AVL
		sb.append("=== ESTATÍSTICAS DA AVL ===").append(nl);
		sb.append("Altura da árvore: ").append(arvore.getAltura()).append(nl);
		sb.append("Número de nós: ").append(arvore.contarNos()).append(nl);
		sb.append("Rotações simples realizadas: ").append(arvore.getContadorRotacoesSimples()).append(nl);
		sb.append("Rotações duplas realizadas: ").append(arvore.getContadorRotacoesDuplas()).append(nl);
		sb.append("Total de rotações: ").append(arvore.getContadorRotacoesTotal()).append(nl);
		sb.append(nl);

		// Estatísticas da Tabela Hash
		sb.append("=== ESTATÍSTICAS DA TABELA HASH ===").append(nl);
		sb.append("Total de colisões: ").append(totalColisoes).append(nl);
		sb.append(nl);

		// Medições de tempo
		sb.append("=== MEDIÇÕES DE TEMPO ===").append(nl);
		sb.append("Tempo de leitura dos documentos: ").append(tempoLeituraDocumentos).append(" ms").append(nl);
		sb.append("Tempo de comparações (similaridade): ").append(tempoComparacoes).append(" ms").append(nl);
		sb.append("Tempo de inserção na AVL: ").append(tempoInsercaoAVL).append(" ms").append(nl);
		sb.append("Tempo de busca na AVL: ").append(tempoBuscaAVL).append(" ms").append(nl);
		sb.append("Tempo total de execução: ").append(tempoTotal).append(" ms").append(nl);
		sb.append(nl);

		sb.append("Pares com similaridade >= ").append(similaridade).append(":").append(nl);
		sb.append("---------------------------------").append(nl);

		if (lista.isEmpty()) {
			sb.append("Nenhum par encontrado.").append(nl);
		} else {
			for (Resultado r : lista) {
				sb.append(r.getArquivoA()).append(" <-> ").append(r.getArquivoB())
						.append(" = ").append(String.format("%.4f", r.getSimilaridade())).append(nl);
			}
		}

		sb.append(nl);
		sb.append("Pares com menor similaridade:").append(nl);
		sb.append("---------------------------------").append(nl);

		Resultado menor = arvore.getResultadoMenorSimilaridade();
		if (menor != null) {
			sb.append(menor.getArquivoA()).append(" <-> ").append(menor.getArquivoB())
					.append(" = ").append(String.format("%.4f", menor.getSimilaridade())).append(nl);
		} else {
			sb.append("N/A (nenhum par comparado)").append(nl);
		}

		return sb.toString();
	}

	private static String formatarSaidaBusca(String arquivoA, String arquivoB, double similaridade) {
		StringBuilder sb = new StringBuilder();
		String nl = System.lineSeparator();

		sb.append("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===").append(nl);
		sb.append("Comparando: ").append(arquivoA).append(" <-> ").append(arquivoB).append(nl);
		sb.append("Similaridade calculada: ").append(String.format("%.4f", similaridade)).append(nl);
		sb.append("Métrica de similaridade: Cosseno").append(nl);
		sb.append(nl);
		sb.append("=== MEDIÇÕES DE TEMPO ===").append(nl);
		sb.append("Tempo total de execução: ").append(tempoTotal).append(" ms").append(nl);

		return sb.toString();
	}

	public static void printarSaida(double similaridade, List<Resultado> lista, AVL arvore) {
		String saida = formatarSaidaLista(similaridade, lista, arvore);
		System.out.println(saida);
	}

	public static void printarSaida(String arquivoA, String arquivoB, double similaridade) {
		String saida = formatarSaidaBusca(arquivoA, arquivoB, similaridade);
		System.out.println(saida);
	}

	public static void gerarLog(String nomeArquivoLog, double similaridade, List<Resultado> lista, AVL arvore)
			throws IOException {
		String conteudoLog = formatarSaidaLista(similaridade, lista, arvore);
		Path caminhoLog = Path.of(nomeArquivoLog);

		Files.writeString(caminhoLog, conteudoLog, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

		System.out.println("Log gerado em: " + caminhoLog.toAbsolutePath());
	}

	public static void gerarLog(String nomeArquivoLog, String arquivoA, String arquivoB, double similaridade)
			throws IOException {
		String conteudoLog = formatarSaidaBusca(arquivoA, arquivoB, similaridade);
		Path caminhoLog = Path.of(nomeArquivoLog);

		Files.writeString(caminhoLog, conteudoLog, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

		System.out.println("Log gerado em: " + caminhoLog.toAbsolutePath());
	}
}
