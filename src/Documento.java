package projeto;

import java.io.*;
import java.util.*;

public class Documento {
	
	private String nomeArquivo;
	private TabelaHash tabela;
	private static final int TAMANHO_HASH = 500;
	
	public Documento(String caminhoArquivo) throws IOException {
	    this.nomeArquivo = new File(caminhoArquivo).getName();
	    this.tabela = new TabelaHash(TAMANHO_HASH);
	    processarArquivo(caminhoArquivo);
	}
	
	private String lerArquivo(String caminhoArquivo) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
	        String linha;
	        while ((linha = br.readLine()) != null) {
	            sb.append(linha).append(" ");
	        }
	    }
	    return sb.toString();
	}
	
	private List<String> preprocessar(String texto) {
        texto = texto.toLowerCase();
        texto = texto.replaceAll("[^a-zà-ú0-9\\s]", " ");
        texto = texto.replaceAll("\\s+", " ");
        String[] separado = texto.trim().split(" ");

        List<String> palavra = new ArrayList<>();
        for (String p : separado) {
            if (!StopWords.eStopWord(p) && !p.isEmpty()) {
                palavra.add(p);
            }
        }
        return palavra;
    }
	
	private void processarArquivo(String caminhoArquivo) throws IOException {
	    String conteudo = lerArquivo(caminhoArquivo);
	    List<String> palavras = preprocessar(conteudo);

	    for (String palavra : palavras) {
	        tabela.inserir(palavra, 1);
	    }
	}

	
	public String getNomeArquivo() {
        return nomeArquivo;
    }

    public TabelaHash getTabela() {
        return tabela;
    }

    public void printTabela() {
        System.out.println("=== " + nomeArquivo + " ===");
        tabela.printDicio();
    }
}
