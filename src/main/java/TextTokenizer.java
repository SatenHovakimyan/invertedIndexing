import java.util.ArrayList;
import java.util.List;

public class TextTokenizer {
    public static List<String> tokenizeText(String text) {
        String[] words = text.split("[\\p{Punct}\\s]+");
        List<String> tokens = new ArrayList<>();
        for (String word : words) {
            tokens.add(word.toLowerCase());
        }
        return tokens;
    }
}
