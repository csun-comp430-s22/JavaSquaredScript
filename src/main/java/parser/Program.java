package parser;

import java.util.List;

public class Program {
    public final List<ClassDef> classes;
    public final Stmt entrypoint;

    public Program(List<ClassDef> classes, Stmt entryPoint) {
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
