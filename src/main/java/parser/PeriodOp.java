package parser;
public class PeriodOp implements Op{
    public boolean equals(final Object other) {
        return other instanceof PeriodOp;
    }

    public int hashCode() {
        return 99;
    }

    public String toString() {
        return "PeriodOp";
    }
}
