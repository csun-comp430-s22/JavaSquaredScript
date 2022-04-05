package parser;

import java.util.List;

public class Program {
    public final List<ClassDef> classes;

    public Program(final List<ClassDef> classes) {
        this.classes = classes;
    }

    public int hashCode() {
        return classes.hashCode();
    }

    public boolean equals(final Object other) {
        return (other instanceof Program) && classes.equals(((Program) other).classes);
    }

    public String toString() {
        return "Program(" + classes.toString() + ")";
    }
}
