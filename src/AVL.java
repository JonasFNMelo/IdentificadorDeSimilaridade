package projeto;

import java.util.ArrayList;
import java.util.List;

public class AVL {

	private No raiz;
	private int qtdRotacoesSimples;
	private int qtdRotacoesDuplas;
	
	public AVL() {
		this.raiz = null;
		qtdRotacoesSimples = 0;
		qtdRotacoesDuplas = 0;
	}
	
	public AVL(No no) {
		this.raiz = no;
		qtdRotacoesSimples = 0;
		qtdRotacoesDuplas = 0;
	}
	
	public int getQtdRotacoesSimples() {
		return qtdRotacoesSimples;
	}
	
	public int getQtdRotacoesDuplas() {
		return qtdRotacoesDuplas;
	}
	
	public int getQtdRotacoesTotal() {
		return qtdRotacoesSimples + (qtdRotacoesDuplas * 2);
	}

	private int obterAltura(No n) {
		if (n == null) return 0;
		return n.getAltura();
	}
	
	private int calcularFatorBalanceamento(No n) {
		if (n == null) return 0;
		return obterAltura(n.getEsq()) - obterAltura(n.getDir());
	}
	
	public void adicionar(ParComparacao res) {
		this.raiz = adicionarRecursivo(this.raiz , res);
	}
	
	private No adicionarRecursivo(No atual, ParComparacao novoRes) {
		if (atual == null) {
			No novoNo = new No(novoRes);
			return novoNo;
		} else if (novoRes.getSimilaridade() == atual.getSimilaridade()) {
			atual.adicionarResultado(novoRes);
			return atual;
		} else {
			if (novoRes.getSimilaridade() < atual.getSimilaridade()) {
			    atual.setEsq(adicionarRecursivo(atual.getEsq(), novoRes)); 
			} else { 
			    atual.setDir(adicionarRecursivo(atual.getDir(), novoRes)); 
			}
		}
		
		atual.setAltura(1 + Math.max(obterAltura(atual.getEsq()), obterAltura(atual.getDir())));
		
		int fb = calcularFatorBalanceamento(atual);
		
		// Rotação Simples Direita
		if (fb > 1 && novoRes.getSimilaridade() < atual.getEsq().getSimilaridade()) {
			qtdRotacoesSimples++;
			return girarDireita(atual);
		}
		
		// Rotação Simples Esquerda
		if (fb < -1 && novoRes.getSimilaridade() > atual.getDir().getSimilaridade()) {
			qtdRotacoesSimples++;
			return girarEsquerda(atual);
		}
		
		// Rotação Dupla Esquerda-Direita
		if (fb > 1 && novoRes.getSimilaridade() > atual.getEsq().getSimilaridade()) {
			qtdRotacoesDuplas++;
			atual.setEsq(girarEsquerda(atual.getEsq()));
			return girarDireita(atual);
		}
		
		// Rotação Dupla Direita-Esquerda
		if (fb < -1 && novoRes.getSimilaridade() < atual.getDir().getSimilaridade()) {
			qtdRotacoesDuplas++;
			atual.setDir(girarDireita(atual.getDir()));
	        return girarEsquerda(atual);
		}
		
		return atual;
	}
	
	private No girarDireita(No y) {
	    No x = y.getEsq();      
	    No temp = x.getDir();     

	    x.setDir(y);            
	    y.setEsq(temp);           

	    y.setAltura(1 + Math.max(obterAltura(y.getEsq()), obterAltura(y.getDir())));
	    x.setAltura(1 + Math.max(obterAltura(x.getEsq()), obterAltura(x.getDir())));

	    return x;
	}
	
	private No girarEsquerda(No x) { 
	    No y = x.getDir();     
	    No temp = y.getEsq();     

	    y.setEsq(x);            
	    x.setDir(temp);           

	    x.setAltura(1 + Math.max(obterAltura(x.getEsq()), obterAltura(x.getDir())));
	    y.setAltura(1 + Math.max(obterAltura(y.getEsq()), obterAltura(y.getDir())));

	    return y; 
	}
	
	public ParComparacao obterMenorSimilaridade() {
        if (this.raiz == null) return null;

        No atual = this.raiz;
        
        while (atual.getEsq() != null) atual = atual.getEsq();
  
        return atual.getResultados().get(0);
    }
	
	public List<ParComparacao> listar(double simMinima) {
		List<ParComparacao> encontrados = new ArrayList<>();
		listarRecursivo(this.raiz, simMinima, encontrados);
		return encontrados;
	}
	
	private void listarRecursivo(No atual, double simMinima, List<ParComparacao> lista) {
		if (atual == null) return;
		if (atual.getSimilaridade() < simMinima) {
			listarRecursivo(atual.getDir(), simMinima, lista);
		} else if (atual.getSimilaridade() >= simMinima) {
			lista.addAll(atual.getResultados());
			listarRecursivo(atual.getEsq(), simMinima, lista);
			listarRecursivo(atual.getDir(), simMinima, lista);
		}
	}
	
	public List<ParComparacao> buscarTopK(int k, double simMinima) {
		if (k <= 0) return new ArrayList<>();
		List<ParComparacao> encontrados = new ArrayList<>();
		buscarTopKRecursivo(this.raiz, k, simMinima, encontrados);
		return encontrados;
	}
	
	private void buscarTopKRecursivo(No atual, int k, double simMinima, List<ParComparacao> lista) {
		if (atual == null) return;
		if (lista.size() >= k) return;
		
		buscarTopKRecursivo(atual.getDir(), k, simMinima, lista);
		
		if (lista.size() >= k) return;
		
		if (atual.getSimilaridade() >= simMinima) {
			List<ParComparacao> auxiliar = atual.getResultados();
			for (ParComparacao res : auxiliar) {
				if (lista.size() < k) lista.add(res);
				else break;
			}
			
			if (lista.size() >= k) return;
			
			buscarTopKRecursivo(atual.getEsq(), k, simMinima, lista);
		}
	}
	
	public int obterAlturArvore() {
		return obterAltura(this.raiz);
	}
	
	public int contarTotalNos() {
		return contarNosRecursivo(this.raiz);
	}
	
	private int contarNosRecursivo(No atual) {
		if (atual == null) return 0;
		return 1 + contarNosRecursivo(atual.getEsq()) + contarNosRecursivo(atual.getDir());
	}
}