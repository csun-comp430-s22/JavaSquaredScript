package lexer.tokens;
public class PrivateToken implements Token{
    public boolean equals(final Object other) {
        return other instanceof PrivateToken;
    }

    public int hashCode() {
        return 19;
    }

    public String toString() {
        return "private";
    }
}