package parser;

public class BreakStmt implements Stmt {

	public int hashCode() {
		return Integer.MAX_VALUE - 1;
	}

	public String toString() {
		return "Break()";
	}

	public boolean equals (final Object other) {
		return other instanceof BreakStmt;
	}
}
