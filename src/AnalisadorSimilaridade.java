package projeto;

import java.util.ArrayList;

public class AnalisadorSimilaridade {
    private TabelaHash documento1;
    private TabelaHash documento2;
    private double valorResultado = -1;
    private ArrayList<String> vocab = new ArrayList<String>();
    private int tamanho;

    public AnalisadorSimilaridade(TabelaHash documento1, TabelaHash documento2){
        this.documento1 = documento1;
        this.documento2 = documento2;
        construirVocabulario();
        this.tamanho = vocab.size();
    }

    private void construirVocabulario(){

        for (int i = 0; i < documento1.getTamanho(); i++) {
            NoHash no = documento1.obterPosicao(i);
            if (no != null) {
                vocab.add(no.getPalavra());
            }
        }

        for (int i = 0; i < documento2.getTamanho(); i++){
            NoHash no = documento2.obterPosicao(i);
            if(no != null && documento1.procurar(no.getPalavra()) == -1){
                vocab.add(no.getPalavra());
            }
        }
    }

    private ArrayList<Integer> montarVetorFrequencia(TabelaHash tabela){
        ArrayList<Integer> vetor = new ArrayList<Integer>();

        for (int i = 0; i < this.tamanho; i++){
            int freq = tabela.procurar(vocab.get(i));
            if(freq != -1) vetor.add(freq);
            else vetor.add(0);
        }
        return vetor;
    }

    private double computarProdutoEscalar(ArrayList<Integer> v1, ArrayList<Integer> v2){
        double produto = 0;

        for(int i = 0; i < v1.size(); i++){
            produto += (v1.get(i) * v2.get(i));
        }
        return produto;
    }

    private double computarNorma(ArrayList<Integer> vetor){
        double norma = 0;

        for(int i = 0; i < vetor.size(); i++){
            norma += Math.pow(vetor.get(i), 2);
        }
        norma = Math.sqrt(norma);
        return norma;
    }

    public double calcularSimilaridade(){
        ArrayList<Integer> vetorDoc1 = montarVetorFrequencia(documento1);
        ArrayList<Integer> vetorDoc2 = montarVetorFrequencia(documento2);
        double produto = computarProdutoEscalar(vetorDoc1, vetorDoc2);
        double norma1 = computarNorma(vetorDoc1);
        double norma2 = computarNorma(vetorDoc2);

        if (norma1 == 0 || norma2 == 0) return 0.0;
        valorResultado = produto / (norma1 * norma2);
        return valorResultado;
    }
}