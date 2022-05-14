package parser;

public class VardecStmt implements Stmt {
	// int a = new A();
	// NewExp(className())
	public final Vardec vardec;
	public final Exp exp;

	public VardecStmt(final Vardec vardec, final Exp exp) {
		this.vardec = vardec;
		this.exp = exp;
	}

	public int hashCode() {
		return vardec.hashCode() + exp.hashCode();
	}

	public boolean equals (final Object other) {
		if (other instanceof VardecStmt) {
			final VardecStmt otherWhile = (VardecStmt) other;
			return (vardec.equals(otherWhile.vardec) && exp.equals(otherWhile.exp));
		} else {
			return false;
		}
	}

	public String toString() {
		return "VardecStmt(" + vardec.toString() + ", " + exp.toString() + ")";
	}

	// varDec = exp
}
