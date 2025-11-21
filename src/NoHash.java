package projeto;

public class NoHash {
    private String termo;
    private int ocorrencias;

    public NoHash(String termo, int ocorrencias){
        this.termo = termo;
        this.ocorrencias = ocorrencias;
    }

    public int getFrequencia() {
        return ocorrencias;
    }

    public String getPalavra() {
        return termo;
    }

    public void setFrequencia(int freq) {
        this.ocorrencias = freq;
    }

    public void setPalavra(String palavra) {
        this.termo = palavra;
    }
}