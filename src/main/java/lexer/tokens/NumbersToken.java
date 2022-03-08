package lexer.tokens;
public class NumbersToken implements Token{
    public final int value;

    public NumbersToken(final int value){
        this.value = value;
    }
    public boolean equals(final Object other){
        if(other instanceof NumbersToken){
            final NumbersToken asInt = (NumbersToken)other;
            return value == asInt.value;
        }else{
            return false;
        }
    }

    public int hashCode(){
        return value;
    }

    public String toString(){
        return "NumbersToken("+value+")";
    }
}
