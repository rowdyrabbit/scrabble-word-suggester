package core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static final String INDEX_FILE = "./output/INDEX.txt";

    private static Map<Character, Integer> charScoreMap = new HashMap<>();

    static {
        charScoreMap.put('A', 1);
        charScoreMap.put('B', 3);
        charScoreMap.put('C', 3);
        charScoreMap.put('D', 2);
        charScoreMap.put('E', 1);
        charScoreMap.put('F', 4);
        charScoreMap.put('G', 2);
        charScoreMap.put('H', 4);
        charScoreMap.put('I', 1);
        charScoreMap.put('J', 8);
        charScoreMap.put('K', 5);
        charScoreMap.put('L', 1);
        charScoreMap.put('M', 3);
        charScoreMap.put('N', 1);
        charScoreMap.put('O', 1);
        charScoreMap.put('P', 3);
        charScoreMap.put('Q', 10);
        charScoreMap.put('R', 1);
        charScoreMap.put('S', 1);
        charScoreMap.put('T', 1);
        charScoreMap.put('U', 1);
        charScoreMap.put('V', 4);
        charScoreMap.put('W', 4);
        charScoreMap.put('X', 8);
        charScoreMap.put('Y', 4);
        charScoreMap.put('Z', 10);
    }

    /**
     * Given a word, calculates its scrabble score
     *
     * @param word the word
     * @return the scrabble score
     */
    public static int getScore(String word) {
        int score = 0;
        char[] chars = word.toUpperCase().toCharArray();
        for (char c: chars) {
            score += charScoreMap.get(c);
        }
        return score;
    }

    /**
     * Reads a list of all the lines from the specified file path
     *
     * @param filePath the file path to read from
     * @return a list of strings
     * @throws IOException
     */
    public static List<String> readLinesFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        List<String> allWords = new ArrayList<>();
        if (Files.exists(path)) {
            allWords = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        }
        return allWords;
    }

    /**
     * Writes a list of string to the index file.
     *
     * @param index the list of strings
     * @throws IOException
     */
    public static void writeIndexWordsToFile(List<String> index) throws IOException {

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(INDEX_FILE), StandardCharsets.UTF_8))) {
            for (String s: index) {
                writer.write(s);
                writer.write('\n');
            }
        }
    }




}
