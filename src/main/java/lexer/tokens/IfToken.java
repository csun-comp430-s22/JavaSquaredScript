package lexer.tokens;

public class IfToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof IfToken;
    }

    public int hashCode() {
        return 2;
    }

    public String toString() {
        return "if";
    }
}