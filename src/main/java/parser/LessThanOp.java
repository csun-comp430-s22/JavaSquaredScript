package parser;
public class LessThanOp implements Op {
    public boolean equals(final Object other) {
        return other instanceof LessThanOp;
    }

    public int hashCode() {
        return 5;
    }

    public String toString() {
        return "LessThanOp";
    }
}
