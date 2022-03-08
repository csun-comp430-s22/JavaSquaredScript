package lexer.tokens;
public class TimesToken implements Token {
    public boolean equals(final Object other) {
        return other instanceof TimesToken;
    }

    public int hashCode() {
        return 23;
    }

    public String toString() {
        return "*";
    }
}
