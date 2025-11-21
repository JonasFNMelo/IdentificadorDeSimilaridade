package projeto;

import java.util.HashSet;
import java.util.Set;

class StopWords {
	private static final Set<String> STOPWORDS = new HashSet<>(Set.of(
	        "a", "o", "as", "os", "um", "uma", "uns", "umas",
	        "de", "da", "do", "das", "dos",
	        "em", "no", "na", "nos", "nas",
	        "por", "para", "com", "sem", "sob", "sobre",
	        "ao", "aos", "à", "às",
	        "e", "ou", "mas", "porque", "que", "se", "como",
	        "sua", "seu", "suas", "seus", "essa", "esse", "isso", "isto",
	        "já", "não", "sim", "tão", "muito", "pouco"
	    ));

	    public static boolean eStopWord(String palavra) {
	        return STOPWORDS.contains(palavra);
	    }
}
