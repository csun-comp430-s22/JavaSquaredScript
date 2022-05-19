package parser;

import parser.Def.ClassDef;
import parser.StmtCalls.MainStmt;

import java.util.List;

public class Program {
    public final List<ClassDef> classes;
    public final MainStmt entrypoint;

    public Program(List<ClassDef> classes, MainStmt entryPoint) {
        this.classes = classes;
        this.entrypoint = entryPoint;
    }

    public int hashCode() {
        return classes.hashCode() + entrypoint.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof Program) {
            final Program otherProg = (Program) other;

            return classes.equals(otherProg.classes) && entrypoint.equals(otherProg.entrypoint);
        } else
        {
            return false;
        }
    }

    public String toString() {
        return "Program(" + classes.toString() + ", " + entrypoint.toString() + ")";
    }
}
