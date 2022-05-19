package parser.OpCalls;

import parser.interfaces.Op;

public class NotEqualsOp implements Op {
    public boolean equals(final Object other) {
        return other instanceof NotEqualsOp;
    }

    public int hashCode() {
        return 8;
    }

    public String toString() {
        return "NotEqualsOp";
    }
}
