package parser.Declarations;

import parser.interfaces.Stmt;
import parser.interfaces.Type;
import parser.ExpCalls.VariableExp;

public class Vardec implements Stmt {

	public final Type type;
	public final VariableExp variable;

	public Vardec(final Type type, final VariableExp variable) {
		this.type = type;
		this.variable = variable;
	}

	public int hashCode() {
		return type.hashCode() + variable.hashCode();
	}

	public boolean equals(final Object other) {
		if (other instanceof Vardec) {
			final Vardec otherVar = (Vardec) other;
			return type.equals(otherVar.type) && variable.equals(otherVar.variable);
		} else {
			return false;
		}
	}

	public String toString() {
		return "Vardec(" + type.toString() + ", " + variable.toString() + ")";
	}
}
