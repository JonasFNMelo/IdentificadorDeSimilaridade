package projeto;

import java.util.ArrayList;
import java.util.List;

public class No {

    private double valorSimilaridade;
    private List<ParComparacao> listaResultados;
    private No filhoEsq;
    private No filhoDir;
    private int alturaNo;
    
    public No(ParComparacao resultado) {
        this.valorSimilaridade = resultado.getSimilaridade();
        this.listaResultados = new ArrayList<>();
        this.listaResultados.add(resultado);
        this.filhoEsq = null;
        this.filhoDir = null;
        this.alturaNo = 1;
    }

    public double getSimilaridade() {
        return valorSimilaridade;
    }

    public List<ParComparacao> getResultados() {
        return listaResultados;
    }

    public void adicionarResultado(ParComparacao resultado) {
        this.listaResultados.add(resultado);
    }

    public No getEsq() {
        return filhoEsq;
    }

    public void setEsq(No esq) {
        this.filhoEsq = esq;
    }

    public No getDir() {
        return filhoDir;
    }

    public void setDir(No dir) {
        this.filhoDir = dir;
    }

    public int getAltura() {
        return alturaNo;
    }

    public void setAltura(int altura) {
        this.alturaNo = altura;
    }
    
    @Override
    public String toString() {
        return "No[Similaridade: " + String.format("%.4f", this.valorSimilaridade) + 
               " | Altura: " + this.alturaNo + 
               " | Qtd. Resultados: " + this.listaResultados.size() + "]";
    }
}