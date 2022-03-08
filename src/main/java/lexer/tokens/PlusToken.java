package lexer.tokens;
public class PlusToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof PlusToken;
    }

    public int hashCode() {
        return 21;
    }

    public String toString() {
        return "+";
    }
}
