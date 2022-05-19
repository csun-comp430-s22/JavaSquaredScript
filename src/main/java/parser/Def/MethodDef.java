package parser.Def;

import parser.Declarations.Vardec;
import parser.Names.MethodName;
import parser.interfaces.AccessType;
import parser.interfaces.Stmt;
import parser.interfaces.Type;

import java.util.List;

public class MethodDef {

	public final AccessType accessType;
	public final Type returnType;
	public final MethodName methodName;
	public final List<Vardec> arguments;
	public final Stmt body;

	public MethodDef(final AccessType accessType, final Type returnType, final MethodName methodName,
		List<Vardec> arguments, final Stmt body) {
		this.accessType = accessType;
		this.returnType = returnType;
		this.methodName = methodName;
		this.arguments = arguments;
		this.body = body;
	}

	public int hashCode() {
		return accessType.hashCode() + returnType.hashCode() + methodName.hashCode() + arguments.hashCode() +
			body.hashCode();
	}

	public boolean equals(final Object other) {
		if (other instanceof MethodDef) {
			final MethodDef otherDef = (MethodDef) other;

			return accessType.equals(otherDef.accessType) &&
				returnType.equals(otherDef.returnType) &&
				methodName.equals(otherDef.methodName) &&
				arguments.equals(otherDef.arguments) &&
				body.equals(otherDef.body);
		} else {
			return false;
		}
	}

	public String toString() {
		return "MethodDef(" + accessType.toString() + ", " + returnType.toString() + ", " + methodName.toString() +
			", " + arguments.toString() + ", " + body.toString() + ")";
	}

}
