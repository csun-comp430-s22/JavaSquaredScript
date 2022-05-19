package parser.StmtCalls;

import parser.Names.ClassName;
import parser.interfaces.Stmt;

public class MainStmt implements Stmt {
    public final ClassName className;

    public int hashCode() {
        return className.hashCode();
    }

    public boolean equals(final Object other) {
        return other instanceof MainStmt && className.equals(((MainStmt) other).className);
    }

    public String toString() {
        return "Main(" + className.toString() + ")";
    }

    public MainStmt(ClassName className) {
        this.className = className;
    }
}
