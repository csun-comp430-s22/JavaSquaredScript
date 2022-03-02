package lexer.tokens;
public class NewToken implements Token{
    public boolean equals(final Object other) {
        return other instanceof NewToken;
    }

    public int hashCode() {
        return 16;
    }

    public String toString() {
        return "new";
    }
}