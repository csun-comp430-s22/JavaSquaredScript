package parser;
public class ProtectedModifier implements AccessMod{
    public int hashCode(){
        return 10002;
    }
    public boolean equals(final Object other){
        return other instanceof ProtectedModifier;
    }
    public String toString(){
        return "protected";
    }
}
