package projeto;

import java.util.ArrayList;
import java.util.List;

public class No {

    private double similaridade;
    private List<Resultado> resultados;
    private No esq;
    private No dir;
    private int altura;
    
    public No(Resultado resultado) {
        this.similaridade = resultado.getSimilaridade();
        this.resultados = new ArrayList<>();
        this.resultados.add(resultado);
        this.esq = null;
        this.dir = null;
        this.altura = 1;
    }

    public double getSimilaridade() {
        return similaridade;
    }

    public List<Resultado> getResultados() {
        return resultados;
    }

    public void adicionarResultado(Resultado resultado) {
        this.resultados.add(resultado);
    }

    public No getEsq() {
        return esq;
    }

    public void setEsq(No esq) {
        this.esq = esq;
    }

    public No getDir() {
        return dir;
    }

    public void setDir(No dir) {
        this.dir = dir;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }
    
    @Override
    public String toString() {
        return "No[Similaridade: " + String.format("%.4f", this.similaridade) + 
               " | Altura: " + this.altura + 
               " | Qtd. Resultados: " + this.resultados.size() + "]";
    }
}