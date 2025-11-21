package projeto;

public class ParComparacao {

	private String primeiroArquivo;
	private String segundoArquivo;
	private double grauSimilaridade;
	
	public ParComparacao(String primeiroArquivo, String segundoArquivo, double grauSimilaridade) {
		this.primeiroArquivo = primeiroArquivo;
		this.segundoArquivo = segundoArquivo;
		this.grauSimilaridade = grauSimilaridade;
	}
	
	public String getArquivoA() {
        return primeiroArquivo;
    }

    public String getArquivoB() {
        return segundoArquivo;
    }

    public double getSimilaridade() {
        return grauSimilaridade;
    }
    
    @Override
    public String toString() {
    	return String.format("%s <-> %s = %.2f",
    			this.primeiroArquivo,
    			this.segundoArquivo,
    			this.grauSimilaridade);
    }
}