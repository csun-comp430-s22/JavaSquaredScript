package parser;
public class GreaterThanOp implements Op {
    public boolean equals(final Object other) {
        return other instanceof GreaterThanOp;
    }

    public int hashCode() {
        return 1;
    }

    public String toString() {
        return "GreaterThanOp";
    }
}
