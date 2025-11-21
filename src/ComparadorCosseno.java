package projeto;

import java.util.ArrayList;

public class ComparadorCosseno {
    private TabelaHash doc1;
    private TabelaHash doc2;
    private double resultado = -1;
    private ArrayList<String> vocabulario = new ArrayList<String>();
    private int tam;

    public ComparadorCosseno(TabelaHash doc1, TabelaHash doc2){
        this.doc1 = doc1;
        this.doc2 = doc2;
        preencherVocab();
        this.tam = vocabulario.size();
    }

    private void preencherVocab(){

        for (int i = 0; i < doc1.getM(); i++) {
            NoHash no = doc1.at(i);
            if (no != null) {
                vocabulario.add(no.getPalavra());
            }
        }

        for (int i = 0; i < doc2.getM(); i++){
            NoHash no = doc2.at(i);
            if(no != null && doc1.busca(no.getPalavra()) == -1){ //Se não tiver em doc1 -> add
                vocabulario.add(no.getPalavra());
            }
        }
    }

    private ArrayList<Integer> preencherVetoresFreq(TabelaHash tabela){
        ArrayList<Integer> vetor = new ArrayList<Integer>();

        for (int i = 0; i<this.tam; i++){
            int freq = tabela.busca(vocabulario.get(i));
            if(freq != -1) vetor.add(freq);
            else vetor.add(0);
        }
        return vetor;
    }

    private double calcularProduto(ArrayList<Integer> v1, ArrayList<Integer> v2){
        double produto = 0;

        for(int i = 0; i<v1.size(); i++){
            produto += (v1.get(i)*v2.get(i));
        }
        return produto;
    }

    private double calcularNorma(ArrayList<Integer> vetor){
        double norma = 0;

        for(int i = 0; i<vetor.size(); i++){
            norma += Math.pow(vetor.get(i), 2);
        }
        norma = Math.sqrt(norma);
        return norma;
    }

    public double similaridade(){
        ArrayList<Integer> vetorDoc1 = preencherVetoresFreq(doc1);
        ArrayList<Integer> vetorDoc2 = preencherVetoresFreq(doc2);
        double produto = calcularProduto(vetorDoc1, vetorDoc2);
        double norma1 = calcularNorma(vetorDoc1);
        double norma2 = calcularNorma(vetorDoc2);

        if (norma1 == 0 || norma2 == 0) return 0.0;
        resultado = produto/(norma1*norma2);
        return resultado;
    }
}
