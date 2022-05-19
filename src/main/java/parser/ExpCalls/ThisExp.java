package parser.ExpCalls;

import parser.interfaces.Exp;

public class ThisExp implements Exp {
    public int hashCode() { return 0; }
    public boolean equals(final Object other) {
        return other instanceof ThisExp;
    }
    public String toString() { return "ThisExp"; }
}
