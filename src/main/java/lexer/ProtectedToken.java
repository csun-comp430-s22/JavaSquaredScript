package lexer;
public class ProtectedToken implements Token{
    public boolean equals(final Object other) {
        return other instanceof ProtectedToken;
    }

    public int hashCode() {
        return 18;
    }

    public String toString() {
        return "protected";
    }
}