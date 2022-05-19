package parser.OpCalls;

import parser.interfaces.Op;

public class DoubleEqualsOp implements Op {
    public boolean equals(final Object other) {
        return other instanceof DoubleEqualsOp;
    }

    public int hashCode() {
        return 2;
    }

    public String toString() {
        return "DoubleEqualsOp";
    }
}
