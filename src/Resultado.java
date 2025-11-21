package projeto;

public class Resultado {

	private String arquivoA;
	private String arquivoB;
	private double similaridade;
	
	public Resultado(String arquivoA, String arquivoB, double similaridade) {
		this.arquivoA = arquivoA;
		this.arquivoB = arquivoB;
		this.similaridade = similaridade;
	}
	
	public String getArquivoA() {
        return arquivoA;
    }

    public String getArquivoB() {
        return arquivoB;
    }

    public double getSimilaridade() {
        return similaridade;
    }
    
    @Override
    public String toString() {
    	return String.format("%s <-> %s = %.2f",
    			this.arquivoA,
    			this.arquivoB,
    			this.similaridade);
    }
}
