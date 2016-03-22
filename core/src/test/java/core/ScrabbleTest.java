package core;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;


public class ScrabbleTest {

    private final Scrabble scrabble = new Scrabble();

    private List<String> dictionaryWords = Arrays.asList(
            "abalone", //9
            "abalones", //10
            "abamp", //11
            "abandon", //10
            "abandoned", //13
            "abase", //7
            "abarticulation", //18
            "abashedness", //17
            "abdomen", //12
            "abdominous", //15
            "abbogada"); //14

    @Test
    public void shouldBuildSuffixStringsForWords() {
        List<String> suffixStrings = scrabble.indexDictionary(Arrays.asList("abalone"));
        List<String> expectedOutput = Arrays.asList("abalone+abalone+9",
                                                    "balone+abalone+9",
                                                    "alone+abalone+9",
                                                    "lone+abalone+9",
                                                    "one+abalone+9",
                                                    "ne+abalone+9",
                                                    "e+abalone+9"
                                                    );
        assertTrue(suffixStrings.containsAll(expectedOutput));
    }

    @Test
    public void shouldReturnTopTenScoringWords() {
        List<String> index = scrabble.indexDictionary(dictionaryWords);

        List<ScrabbleWord> topWords = scrabble.getSuggestions("AB", 7, index);
        for (ScrabbleWord word: topWords) {
            System.out.println(String.format("%s: (%d)", word.getWord(), word.getScore()));
        }

        assertTrue(topWords.containsAll(Arrays.asList(new ScrabbleWord("abarticulation", 18),
                                                      new ScrabbleWord("abashedness", 17),
                                                      new ScrabbleWord("abdominous", 15),
                                                      new ScrabbleWord("abbogada", 14),
                                                      new ScrabbleWord("abandoned", 13),
                                                      new ScrabbleWord("abdomen", 12),
                                                      new ScrabbleWord("abamp", 11)

        )));
    }


}
