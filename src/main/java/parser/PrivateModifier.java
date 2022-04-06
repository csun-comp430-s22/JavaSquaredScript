package parser;
public class PrivateModifier implements AccessMod{
    public int hashCode(){
        return 10001;
    }
    public boolean equals(final Object other){
        return other instanceof PrivateModifier;
    }
    public String toString(){
        return "private";
    }
}
