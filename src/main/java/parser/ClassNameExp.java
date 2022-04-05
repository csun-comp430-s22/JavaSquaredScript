package parser;

public class ClassNameExp implements Exp {

	public final String name;

	public ClassNameExp(final String name) {
		this.name = name;
	}

	public int hashCode() {
		return name.hashCode();
	}

	public boolean equals(final Object other) {
		return (other instanceof ClassNameExp) && name.equals(((ClassNameExp) other).name);
	}

	public String toString() {
		return "ClassName(" + name + ")";
	}
}
