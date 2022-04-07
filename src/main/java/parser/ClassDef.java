package parser;

import java.util.List;

public class ClassDef {

	public final ClassName className, extendedName;
	public final List<ConstructorDef> constructors;
	public final List<MethodDef> methods;
	public final List<InstanceDec> instances;


	public ClassDef(final ClassName className, final ClassName extendedName,
		final List<ConstructorDef> constructors, final List<MethodDef> methods,
		final List<InstanceDec> instances) {
		this.className = className;
		this.extendedName = extendedName;
		this.constructors = constructors;
		this.methods = methods;
		this.instances = instances;
	}

	public int hashCode() {
		return constructors.hashCode() + className.hashCode() + extendedName.hashCode() + methods.hashCode() + instances.hashCode();
	}

	public boolean equals(final Object other) {
		if (other instanceof ClassDef) {
			final ClassDef otherDef = (ClassDef) other;

			return className.equals(otherDef.className) && extendedName.equals(otherDef.extendedName) && constructors.equals(otherDef.constructors) &&
				methods.equals(otherDef.methods) && instances.equals(otherDef.instances);
		} else {
			return false;
		}
	}

	public String toString() {
		return "ClassDef(" + className.toString() + extendedName.toString() + constructors.toString() + methods.toString() + instances.toString() + ")";
	}

}
