package parser;

import java.util.List;

public class Program {
    public final List<ClassDef> classes;

    public Program(List<ClassDef> classes) {
        this.classes = classes;
        //this.entryPoint = entryPoint;
    }

    //public final Stmt entryPoint;

    public int hashCode() {
        return classes.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof Program) {
            final Program otherProg = (Program) other;

            return classes.equals(otherProg.classes);
        } else
        {
            return false;
        }
    }

    public String toString() {
        return "Program(" + classes.toString() + ")";
    }
}
