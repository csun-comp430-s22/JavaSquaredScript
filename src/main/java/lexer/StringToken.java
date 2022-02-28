package lexer;
public class StringToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof StringToken;
    }

    public int hashCode() {
        return 8;
    }

    public String toString() {
        return "strg";
    }
}