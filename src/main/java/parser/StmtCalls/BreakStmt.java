package parser.StmtCalls;

import parser.interfaces.Stmt;

public class BreakStmt implements Stmt {
	public int hashCode() {
		return 2;
	}

	public String toString() {
		return "Break()";
	}

	public boolean equals (final Object other) {
		return other instanceof BreakStmt;
	}
}
