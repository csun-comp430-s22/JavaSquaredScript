package parser;

import java.util.List;

public class ClassDef {

	public final ClassName className;
	public final List<MethodDef> methods;

	public ClassDef(final ClassName className, final List<MethodDef> methods) {
		this.className = className;
		this.methods = methods;
	}

	public int hashCode() {
		return methods.hashCode();
	}

	public boolean equals(final Object other) {
		if (other instanceof ClassDef) {
			final ClassDef otherDef = (ClassDef) other;

			return methods.equals(otherDef.methods);
		} else {
			return false;
		}
	}

	public String toString() {
		return "ClassDef(" + methods.toString() + ")";
	}

}
