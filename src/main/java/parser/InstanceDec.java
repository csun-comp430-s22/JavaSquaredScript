package parser;
public class InstanceDec {
    public final AccessMod accessmod;
    public final Stmt stmt; 

    public InstanceDec(final AccessMod accessmod, Stmt stmt) {
        this.accessmod = accessmod;
        this.stmt = stmt;
    }

    public int hashCode() {
        return (accessmod.hashCode() + 
                stmt.hashCode());
    }

    public boolean equals(final Object other) {
        if (other instanceof InstanceDec) {
            final InstanceDec otherDef = (InstanceDec)other;
            return (accessmod.equals(otherDef.accessmod) && 
                    stmt.equals(otherDef.stmt));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("MethodDef(" + 
                accessmod.toString() + ", " +
                stmt.toString() +")");
    }
}
