package parser.OpCalls;

import parser.interfaces.Op;

public class GreaterThanOp implements Op {
    public boolean equals(final Object other) {
        return other instanceof GreaterThanOp;
    }

    public int hashCode() {
        return 4;
    }

    public String toString() {
        return "GreaterThanOp";
    }
}
