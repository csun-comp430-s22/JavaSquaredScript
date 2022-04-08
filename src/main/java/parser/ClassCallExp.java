package parser;
import java.util.List;


public class ClassCallExp implements Exp {
    public final ClassName cname;
    public final List<Exp> params;

    public ClassCallExp(final ClassName cname,
                           final List<Exp> params) {
        this.cname = cname;
        this.params = params;
    }

    public int hashCode() {
        return (cname.hashCode() +
                params.hashCode());
    }

    public boolean equals(final Object other) {
        if (other instanceof ClassCallExp) {
            final ClassCallExp asFunc = (ClassCallExp)other;
            return (cname.equals(asFunc.cname) &&
                    params.equals(asFunc.params));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("ClassCallExp(" + cname.toString() + ", " +
                params.toString() + ")");
    }
}