package projeto;

import java.util.HashSet;
import java.util.Set;

class PalavrasIgnoradas {
	private static final Set<String> PALAVRAS_IGNORADAS = new HashSet<>(Set.of(
	        "a", "o", "as", "os", "um", "uma", "uns", "umas",
	        "de", "da", "do", "das", "dos",
	        "em", "no", "na", "nos", "nas",
	        "por", "para", "com", "sem", "sob", "sobre",
	        "ao", "aos", "à", "às",
	        "e", "ou", "mas", "porque", "que", "se", "como",
	        "sua", "seu", "suas", "seus", "essa", "esse", "isso", "isto",
	        "já", "não", "sim", "tão", "muito", "pouco"
	    ));

	    public static boolean verificarStopWord(String palavra) {
	        return PALAVRAS_IGNORADAS.contains(palavra);
	    }
}