package core;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UtilsTest {

    @Test
    public void shouldCalculateCorrectWordValues() {
        int score = Utils.getScore("ETSY");
        assertThat(score, is(7));

        score = Utils.getScore("ZQJKF");
        assertThat(score, is(37));
    }


    @Test
    public void shouldReadLinesFromFile() throws IOException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("input_lines.txt");
        List<String> fileLines = Utils.readLinesFromFile(url.getPath());

        assertThat(fileLines.get(0), is("test"));
        assertThat(fileLines.get(1), is("another"));
        assertThat(fileLines.get(2), is("testing"));
        assertThat(fileLines.get(3), is("testing123"));
    }
}