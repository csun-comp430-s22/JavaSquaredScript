package parser;

import java.util.List;

public class ConstructorDef {

	public final List<Vardec> parameters;
	public final Stmt body;

	public ConstructorDef(final List<Vardec> parameters, final Stmt body) {
		this.parameters = parameters;
		this.body = body;
	}

	public int hashCode() {
		return parameters.hashCode() + body.hashCode();
	}

	public boolean equals(final Object other) {
		if (other instanceof ConstructorDef) {
			final ConstructorDef otherDef = (ConstructorDef) other;
			return parameters.equals(otherDef.parameters) && body.equals(otherDef.body);
		} else {
			return false;
		}
	}

	public String toString() {
		return "ConstructorDef(" + parameters.hashCode() + body.hashCode() + ")";
	}

}
