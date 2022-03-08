package lexer.tokens;
public class DoubleEqualsToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof DoubleEqualsToken;
    }

    public int hashCode() {
        return 28;
    }

    public String toString() {
        return "==";
    }
}
