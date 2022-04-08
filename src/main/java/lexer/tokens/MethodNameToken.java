package lexer.tokens;
public class MethodNameToken implements Token{
    public final String value;

    public MethodNameToken(final String value){
        this.value = value;
    }
    public boolean equals(final Object other){
        if(other instanceof MethodNameToken){
            final MethodNameToken asString = (MethodNameToken)other;
            return value.equals(asString.value);
        }else{
            return false;
        }
    }

    public int hashCode(){
        return 100;
    }

    public String toString(){
        return "MethodNameToken("+value+")";
    }
}
