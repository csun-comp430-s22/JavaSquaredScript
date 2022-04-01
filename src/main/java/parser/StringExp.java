package parser;
public class StringExp implements Op{
    public final String value;
    public IntegerExp(final String value){
        this.value = value;
    }
    
    public boolean equals(final Object other) {
        return (other instanceof StringExp &&
                value.equals(((StringExp)other).value));
    }

    public String hashCode() {
        return value;
    }

    public String toString() {
        return "StringExp(" + value + ")";
    }
}
