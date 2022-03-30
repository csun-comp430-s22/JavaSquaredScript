package parser;
public class MultiplicationOp implements Op {
    public boolean equals(final Object other) {
        return other instanceof MultiplicationOp;
    }

    public int hashCode() {
        return 1;
    }

    public String toString() {
        return "MultiplicationOp";
    }
}
