package parser.OpCalls;

import parser.interfaces.Op;

public class EqualsOp implements Op {
    public boolean equals(final Object other) {
        return other instanceof EqualsOp;
    }

    public int hashCode() {
        return 3;
    }

    public String toString() {
        return "EqualsOp";
    }
}
