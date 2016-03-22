package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Scrabble {

    private static final char SEPARATOR_CHAR = '+';


    /**
     * Given a list of strings, converts them into suffix strings
     * and returns a list of all suffix strings for the given input.
     *
     * @param words a list of words to build the index for
     * @return a list of suffix strings for all the input words
     */
    public List<String> indexDictionary(List<String> words) {
        List<String> index = new ArrayList<>();

        for (String word: words) {
            List<String> indexStrings = buildSuffixStrings(word);
            index.addAll(indexStrings);
        }
        Collections.sort(index);
        return index;
    }

    /**
     * Retrieves a list of the highest scoring K words as specified by the parameter {@code numWords}
     * that match the string Q as specified by the {@code query} argument.
     *
     * @param query the query string to search for
     * @param numWords the number of words to return
     * @param indexWords the list of index words to search for high-scoring words in
     * @return a list of high scoring words
     */
    public List<ScrabbleWord> getSuggestions(String query, int numWords, List<String> indexWords) {
        List<String> matchingWords = indexWords.stream().filter(x -> x.startsWith(query.toLowerCase())).collect(Collectors.toList());

        return getTopScoringWords(matchingWords, numWords);
    }

    private List<String> buildSuffixStrings(String word) {
        int score = Utils.getScore(word);
        List<String> suffixStrs = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            suffixStrs.add(word.substring(i) + SEPARATOR_CHAR + word + SEPARATOR_CHAR + score);
        }
        return suffixStrs;
    }

    private List<ScrabbleWord> getTopScoringWords(List<String> matchingWords, int numWords) {
        return matchingWords.stream()
            .map(x -> new ScrabbleWord(
                    x.substring(x.indexOf(SEPARATOR_CHAR) + 1, x.lastIndexOf(SEPARATOR_CHAR)),
                    Integer.valueOf(x.substring(x.lastIndexOf(SEPARATOR_CHAR) + 1, x.length()))))
            .sorted((w1, w2) -> Integer.compare(w2.getScore(), w1.getScore()))
            .distinct()
            .limit(numWords)
            .collect(Collectors.toList());
    }

}

