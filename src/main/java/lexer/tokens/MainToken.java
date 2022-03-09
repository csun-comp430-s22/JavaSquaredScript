package lexer.tokens;
public class MainToken implements Token{
    public boolean equals(final Object other) {
        return other instanceof MainToken;
    }

    public int hashCode() {
        return 32;
    }

    public String toString() {
        return "main";
    }
}
