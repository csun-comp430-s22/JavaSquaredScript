package parser;

public class VardecStmt implements Stmt {

	public final Vardec vardec;
	public final Exp exp;

	public VardecStmt(final Vardec vardec, final Exp exp) {
		this.vardec = vardec;
		this.exp = exp;
	}

	// varDec = exp
}
