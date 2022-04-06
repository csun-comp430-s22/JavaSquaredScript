package parser;
import java.util.List;

public class MethodDef{
    public final AccessMod accessmod;
    public final Type returnType;
    public final Exp exp;
    public final Stmt body;

    public MethodDef(final AccessMod accessmod, final Type returnType,
                final Exp exp,
                final Stmt body) {
        this.accessmod = accessmod;
        this.returnType = returnType;
        this.exp = exp;
        this.body = body;
    }

    public int hashCode() {
        return (accessmod.hashCode() + 
                returnType.hashCode() +
                exp.hashCode() +
                body.hashCode());
    }

    public boolean equals(final Object other) {
        if (other instanceof MethodDef) {
            final MethodDef otherDef = (MethodDef)other;
            return (accessmod.equals(otherDef.accessmod) && 
                    (returnType.equals(otherDef.returnType)) &&
                    (exp.equals(otherDef.exp)) &&
                    body.equals(otherDef.body));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("MethodDef(" + 
                accessmod.toString() + ", " +
                returnType.toString() + ", " +
                exp.toString() + ", " +
                body.toString() + ")");
    }
}