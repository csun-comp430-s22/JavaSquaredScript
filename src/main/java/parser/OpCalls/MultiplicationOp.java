package parser.OpCalls;

import parser.interfaces.Op;

public class MultiplicationOp implements Op {
    public boolean equals(final Object other) {
        return other instanceof MultiplicationOp;
    }

    public int hashCode() {
        return 7;
    }

    public String toString() {
        return "MultiplicationOp";
    }
}
