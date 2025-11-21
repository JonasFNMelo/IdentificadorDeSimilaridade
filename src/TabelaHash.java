package projeto;

public class TabelaHash {
    private int m;
    private NoHash[] dicio;
    private int qtdColisoes;

    public TabelaHash(int m){
        this.m = m;
        this.dicio = new NoHash[m];
        this.qtdColisoes = 0;
    }

    private int dispersao1(String palavra){
        int aux = 0;
        for (int i = 0; i < palavra.length(); i++){
            aux = (31 * aux + palavra.charAt(i)) % this.m;
        }
        if (aux < 0) aux += this.m; // Garantir valor positivo
        return aux;
    }

    private int dispersao2(String palavra) {
        int hash = palavra.hashCode();
        int h2 = (hash & 0x7fffffff) % (this.m - 1) + 1; // Garante h2 >= 1
        return h2;
    } 

    public void inserir(String palavra, int frequencia) {
        int h1 = dispersao1(palavra);
        int h2 = dispersao2(palavra);

        int index = h1;
        int tentativas = 0;

        while (dicio[index] != null && !dicio[index].getPalavra().equals(palavra)) {
            tentativas++;
            qtdColisoes++;
            index = (h1 + tentativas * h2) % m;

            if (tentativas >= m) {
                System.err.println("TabelaHash cheia! Não foi possível inserir: " + palavra);
                return;
            }
        }

        if (dicio[index] != null && dicio[index].getPalavra().equals(palavra)) {
            dicio[index].setFrequencia(dicio[index].getFrequencia() + frequencia);
        } else {
            dicio[index] = new NoHash(palavra, frequencia);
        }
    }

    public int busca(String palavra) {
        int h1 = dispersao1(palavra);
        int h2 = dispersao2(palavra);

        int index = h1;
        int tentativas = 0;

        while (this.dicio[index] != null && tentativas < m) {
            if (this.dicio[index].getPalavra().equals(palavra)) {
                return this.dicio[index].getFrequencia();
            }
            tentativas++;
            index = (h1 + tentativas * h2) % m;
        }

        return -1;
    }

    public int getColisoes(){
        return this.qtdColisoes;
    }

    public int getM(){
        return this.m;
    }

    public NoHash at(int i) {
        return this.dicio[i];
    }

    public void printDicio(){
        for(int i = 0; i < this.m; i++){
            if(this.dicio[i] != null) {
                System.out.println(this.dicio[i].getPalavra()+ ": " + this.dicio[i].getFrequencia());
            }
        }
    }
}