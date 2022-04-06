package parser;
public class PublicModifier implements AccessMod{
    public int hashCode(){
        return 10000;
    }
    public boolean equals(final Object other){
        return other instanceof PublicModifier;
    }
    public String toString(){
        return "public";
    }
}
