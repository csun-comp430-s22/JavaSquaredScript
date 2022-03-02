package lexer.tokens;
public class ThisToken implements Token{
    public boolean equals(final Object other) {
        return other instanceof ThisToken;
    }

    public int hashCode() {
        return 11;
    }

    public String toString() {
        return "this";
    }
}