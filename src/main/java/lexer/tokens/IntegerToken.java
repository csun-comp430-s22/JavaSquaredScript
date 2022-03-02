package lexer.tokens;

public class IntegerToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof IntegerToken;
    }

    public int hashCode() {
        return 10;
    }

    public String toString() {
        return "Int";
    }
}