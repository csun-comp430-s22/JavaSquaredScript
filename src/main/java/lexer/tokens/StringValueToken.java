package lexer.tokens;
public class StringValueToken implements Token{
    public final String value;

    public StringValueToken(final String value){
        this.value = value;
    }
    public boolean equals(final Object other){
        if(other instanceof StringValueToken){
            final StringValueToken asString = (StringValueToken)other;
            return value.equals(asString.value);
        }else{
            return false;
        }
    }

    public int hashCode(){
        return 20;
    }

    public String toString(){
        return "StringValueToken("+value+")";
    }
}
