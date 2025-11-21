package projeto;

public class TabelaHash {
    private int capacidade;
    private NoHash[] elementos;
    private int numColisoes;

    public TabelaHash(int capacidade){
        this.capacidade = capacidade;
        this.elementos = new NoHash[capacidade];
        this.numColisoes = 0;
    }

    private int hashPrimario(String palavra){
        int aux = 0;
        for (int i = 0; i < palavra.length(); i++){
            aux = (31 * aux + palavra.charAt(i)) % this.capacidade;
        }
        if (aux < 0) aux += this.capacidade;
        return aux;
    }

    private int hashSecundario(String palavra) {
        int hash = palavra.hashCode();
        int h2 = (hash & 0x7fffffff) % (this.capacidade - 1) + 1;
        return h2;
    } 

    public void inserir(String palavra, int frequencia) {
        int h1 = hashPrimario(palavra);
        int h2 = hashSecundario(palavra);

        int pos = h1;
        int tentativas = 0;

        while (elementos[pos] != null && !elementos[pos].getPalavra().equals(palavra)) {
            tentativas++;
            numColisoes++;
            pos = (h1 + tentativas * h2) % capacidade;

            if (tentativas >= capacidade) {
                System.err.println("TabelaHash cheia! Não foi possível inserir: " + palavra);
                return;
            }
        }

        if (elementos[pos] != null && elementos[pos].getPalavra().equals(palavra)) {
            elementos[pos].setFrequencia(elementos[pos].getFrequencia() + frequencia);
        } else {
            elementos[pos] = new NoHash(palavra, frequencia);
        }
    }

    public int procurar(String palavra) {
        int h1 = hashPrimario(palavra);
        int h2 = hashSecundario(palavra);

        int pos = h1;
        int tentativas = 0;

        while (this.elementos[pos] != null && tentativas < capacidade) {
            if (this.elementos[pos].getPalavra().equals(palavra)) {
                return this.elementos[pos].getFrequencia();
            }
            tentativas++;
            pos = (h1 + tentativas * h2) % capacidade;
        }

        return -1;
    }

    public int getColisoes(){
        return this.numColisoes;
    }

    public int getTamanho(){
        return this.capacidade;
    }

    public NoHash obterPosicao(int i) {
        return this.elementos[i];
    }

    public void mostrarConteudo(){
        for(int i = 0; i < this.capacidade; i++){
            if(this.elementos[i] != null) {
                System.out.println(this.elementos[i].getPalavra()+ ": " + this.elementos[i].getFrequencia());
            }
        }
    }
}