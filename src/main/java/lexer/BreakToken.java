package lexer;
public class BreakToken implements Token{
    public boolean equals(final Object other) {
        return other instanceof BreakToken;
    }

    public int hashCode() {
        return 13;
    }

    public String toString() {
        return "break";
    }
}