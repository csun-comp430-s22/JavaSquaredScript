package parser.OpCalls;

import parser.interfaces.Op;

public class MinusOp implements Op {
    public boolean equals(final Object other) {
        return other instanceof MinusOp;
    }

    public int hashCode() {
        return 6;
    }

    public String toString() {
        return "MinusOp";
    }
}
