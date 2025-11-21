package projeto;

import java.io.*;
import java.util.*;

public class Texto {
	
	private String nome;
	private TabelaHash tabelaFreq;
	private static final int TAM_HASH = 500;
	
	public Texto(String caminho) throws IOException {
	    this.nome = new File(caminho).getName();
	    this.tabelaFreq = new TabelaHash(TAM_HASH);
	    processar(caminho);
	}
	
	private String carregarConteudo(String caminho) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
	        String linha;
	        while ((linha = br.readLine()) != null) {
	            sb.append(linha).append(" ");
	        }
	    }
	    return sb.toString();
	}
	
	private List<String> tratarTexto(String texto) {
        texto = texto.toLowerCase();
        texto = texto.replaceAll("[^a-zà-ú0-9\\s]", " ");
        texto = texto.replaceAll("\\s+", " ");
        String[] tokens = texto.trim().split(" ");

        List<String> palavrasFiltradas = new ArrayList<>();
        for (String p : tokens) {
            if (!PalavrasIgnoradas.verificarStopWord(p) && !p.isEmpty()) {
                palavrasFiltradas.add(p);
            }
        }
        return palavrasFiltradas;
    }
	
	private void processar(String caminho) throws IOException {
	    String conteudo = carregarConteudo(caminho);
	    List<String> palavras = tratarTexto(conteudo);

	    for (String palavra : palavras) {
	        tabelaFreq.inserir(palavra, 1);
	    }
	}

	
	public String getNomeArquivo() {
        return nome;
    }

    public TabelaHash getTabela() {
        return tabelaFreq;
    }

    public void exibirTabela() {
        System.out.println("=== " + nome + " ===");
        tabelaFreq.mostrarConteudo();
    }
}