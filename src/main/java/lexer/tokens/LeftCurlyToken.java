package lexer.tokens;

public class LeftCurlyToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof LeftCurlyToken;
    }

    public int hashCode() {
        return 5;
    }

    public String toString() {
        return "{";
    }
}