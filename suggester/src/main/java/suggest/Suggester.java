package suggest;

import core.Scrabble;
import core.ScrabbleWord;
import core.Utils;

import java.io.IOException;
import java.util.List;

public class Suggester {


    public static void main(String[] args) throws IOException {
        if (args.length == 2) {
            List<String> indexWords = Utils.readLinesFromFile(Utils.INDEX_FILE);
            Scrabble scrabble = new Scrabble();
            List<ScrabbleWord> words = scrabble.getSuggestions(args[0], Integer.parseInt(args[1]), indexWords);

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("The top-scoring %s words matching query '%s' are:", args[1], args[0])).append(System.lineSeparator());
            for (ScrabbleWord word: words) {
                sb.append(String.format("%s: (%d)", word.getWord(), word.getScore())).append(System.lineSeparator());
            }
            System.out.println(sb.toString());
        } else {
            System.err.println(String.format("Expected args: <query> <num_matches>"));
        }
    }
}
