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
<<<<<<< HEAD
        return 99;
=======
        return Integer.MAX_VALUE;
>>>>>>> ef8222837eeba420be63a4414676fb6129ab693d
    }

    public String toString() {
        return "StringExp(" + value + ")";
    }
}
