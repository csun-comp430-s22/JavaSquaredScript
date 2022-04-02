package parser;
public class StringExp implements Exp{
    public final String value;
    public StringExp(final String value){
        this.value = value;
    }
    
    public boolean equals(final Object other) {
        return (other instanceof StringExp &&
                value.equals(((StringExp)other).value));
    }

    public int hashCode() {
        return Integer.MAX_VALUE;
    }

    public String toString() {
        return "StringExp(" + value + ")";
    }
}
