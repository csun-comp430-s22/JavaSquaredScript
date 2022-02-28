package lexer;
public class PublicToken implements Token{
    public boolean equals(final Object other) {
        return other instanceof PublicToken;
    }

    public int hashCode() {
        return 17;
    }

    public String toString() {
        return "public";
    }
}