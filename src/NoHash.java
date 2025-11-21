package projeto;

public class NoHash {
    private String palavra;
    private int frequencia;

    public NoHash(String palavra, int frequencia){
        this.palavra = palavra;
        this.frequencia = frequencia;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public String getPalavra() {
        return palavra;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

}
