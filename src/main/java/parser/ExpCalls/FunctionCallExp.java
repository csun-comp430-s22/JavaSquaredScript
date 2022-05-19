package parser.ExpCalls;

import parser.interfaces.Exp;
import parser.Names.MethodName;
import parser.ReturnTypes.ClassNameType;

import java.util.List;


public class FunctionCallExp implements Exp {
    public final MethodName fname;

    public ClassNameType targetType; // filled in by the typechecker
    public final Exp target;
    public final List<Exp> params;

    public FunctionCallExp(final MethodName fname, final Exp target,
                           final List<Exp> params) {
        this.fname = fname;
        this.target = target;
        this.params = params;
    }

    public int hashCode() {
        return (fname.hashCode() +
                params.hashCode() + target.hashCode());
    }

    public boolean equals(final Object other) {
        if (other instanceof FunctionCallExp) {
            final FunctionCallExp asFunc = (FunctionCallExp)other;
            return (fname.equals(asFunc.fname) &&
                    params.equals(asFunc.params) && target.equals(asFunc.target));
        } else {
            return false;
        }
    }

    public String toString() {
        return ("FunctionCallExp(" + fname.toString() + ", " +
                params.toString() + ", " + target.toString() + ")");
    }
}