package lexer.tokens;
public class NotEqualsToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof NotEqualsToken;
    }

    public int hashCode() {
        return 29;
    }

    public String toString() {
        return "!=";
    }
}
