package index;

import core.Scrabble;
import core.Utils;

import java.io.IOException;
import java.util.List;

public class Indexer {

    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            Scrabble scrabble = new Scrabble();
            System.out.println(String.format("Indexing words in file: '%s'", args[0]));
            List<String> dictionaryWords = Utils.readLinesFromFile(args[0]);
            List<String> indexWords = scrabble.indexDictionary(dictionaryWords);
            Utils.writeIndexWordsToFile(indexWords);
            System.out.println(String.format("Finished writing indexed words to: '%s'", Utils.INDEX_FILE));
        } else {
            System.err.println(String.format("Expected args: <word_file>"));
        }
    }
}
