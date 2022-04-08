package parser;

import java.util.List;
public class BlockStmt implements Stmt{
    public final List<Stmt> stmts;
    public BlockStmt(final List<Stmt> stmts){
        this.stmts = stmts;
    }

    public int hashCode() {
        return stmts.hashCode();
    }

    public boolean equals(final Object other) {
        if (other instanceof BlockStmt) {
            final BlockStmt otherStmt = (BlockStmt) other;
            return stmts.equals(otherStmt.stmts);
        } else {
            return false;
        }
    }
}
