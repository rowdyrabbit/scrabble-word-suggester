package core;

public class ScrabbleWord {

    private String word;
    private int score;

    public ScrabbleWord(String word, int score) {
        this.word = word;
        this.score = score;
    }

    public String getWord() {
        return word;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScrabbleWord that = (ScrabbleWord) o;

        if (score != that.score) return false;
        return !(word != null ? !word.equals(that.word) : that.word != null);

    }

    @Override
    public int hashCode() {
        int result = word != null ? word.hashCode() : 0;
        result = 31 * result + score;
        return result;
    }
}
