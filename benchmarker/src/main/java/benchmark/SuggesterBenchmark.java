package benchmark;


import core.Scrabble;
import core.ScrabbleWord;
import core.Utils;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Benchmarking code using JMH for the scrabble-suggester program.
 */
public class SuggesterBenchmark {

    @Benchmark @BenchmarkMode(Mode.SingleShotTime) @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testReadIndexFromFile() throws IOException {
        List<String> indexWords = Utils.readLinesFromFile(Utils.INDEX_FILE);
    }

    @Benchmark @BenchmarkMode(Mode.SingleShotTime)  @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testSearchIndex_Large(IndexState indexState) {
        performInMemoryIndexSearch(indexState.scrabble, indexState.largestQuery, indexState.getLargestQueryCount(), indexState.prebuiltIndexWords);
    }

    @Benchmark @BenchmarkMode(Mode.SingleShotTime)  @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testSearchIndex_Regular(IndexState indexState) {
        performInMemoryIndexSearch(indexState.scrabble, indexState.regularQuery, indexState.getRegularQueryCount(), indexState.prebuiltIndexWords);
    }

    private List<ScrabbleWord> performInMemoryIndexSearch(Scrabble scrabble, String query, String numResults, List<String> indexWords) {
        List<ScrabbleWord> words = scrabble.getSuggestions(query, Integer.valueOf(numResults), indexWords);
        return words;
    }

    @Benchmark @BenchmarkMode(Mode.SingleShotTime)  @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testEndToEnd_Large(IndexState indexState) throws IOException {
        List<String> indexWords = Utils.readLinesFromFile(Utils.INDEX_FILE);
        List<ScrabbleWord> words = performInMemoryIndexSearch(indexState.scrabble, indexState.largestQuery, indexState.getLargestQueryCount(), indexWords);
    }

    @Benchmark @BenchmarkMode(Mode.SingleShotTime)  @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testEndToEnd_Regular(IndexState indexState) throws IOException {
        List<String> indexWords = Utils.readLinesFromFile(Utils.INDEX_FILE);
        List<ScrabbleWord> words = performInMemoryIndexSearch(indexState.scrabble, indexState.regularQuery, indexState.getRegularQueryCount(), indexState.prebuiltIndexWords);
    }

    @Benchmark @BenchmarkMode(Mode.SingleShotTime)  @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testPrintOutput_Large(IndexState indexState) throws IOException {
        List<ScrabbleWord> words = indexState.suggestions;
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("The top-scoring %s words matching query '%s' are:", indexState.largestQueryCount, indexState.largestQuery)).append(System.lineSeparator());
        for (ScrabbleWord word: words) {
            sb.append(String.format("%s: (%d)", word.getWord(), word.getScore())).append(System.lineSeparator());
        }
        System.out.println(sb.toString());
    }

    @Benchmark @BenchmarkMode(Mode.SingleShotTime)  @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public void testPrintOutput_Regular(IndexState indexState) throws IOException {
        List<ScrabbleWord> words = indexState.regularSuggestions;
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("The top-scoring %s words matching query '%s' are:", indexState.regularQueryCount, indexState.regularQuery)).append(System.lineSeparator());
        for (ScrabbleWord word: words) {
            sb.append(String.format("%s: (%d)", word.getWord(), word.getScore())).append(System.lineSeparator());
        }
        System.out.println(sb.toString());
    }



    @State(Scope.Benchmark)
    public static class IndexState {
        private List<String> prebuiltIndexWords;
        private Scrabble scrabble;
        private String largestQuery;
        private long largestQueryCount = 0;
        private String regularQuery = "AT";
        private long regularQueryCount = 30;
        private List<ScrabbleWord> suggestions;
        private List<ScrabbleWord> regularSuggestions;

        @Setup(Level.Trial)
        public void setup() throws IOException {
            prebuiltIndexWords = Utils.readLinesFromFile(Utils.INDEX_FILE);
            Map<Character, Long> charCounts = prebuiltIndexWords.stream()
                    .map(w -> w.charAt(0))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            for (Map.Entry<Character, Long> entry: charCounts.entrySet()) {
                if (entry.getValue() > largestQueryCount) {
                    largestQueryCount = entry.getValue();
                    largestQuery = String.valueOf(entry.getKey());
                }
            }
            scrabble = new Scrabble();
            suggestions = scrabble.getSuggestions(largestQuery, (int) largestQueryCount, prebuiltIndexWords);
            regularSuggestions = scrabble.getSuggestions(regularQuery, (int) regularQueryCount, prebuiltIndexWords);
        }

        public String getLargestQueryCount() {
            return String.valueOf(largestQueryCount);
        }

        public String getRegularQueryCount() {
            return String.valueOf(regularQueryCount);
        }

    }

}
