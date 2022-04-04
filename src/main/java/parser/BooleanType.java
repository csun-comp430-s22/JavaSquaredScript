package parser;

public class BooleanType implements Type {

	public int hashCode() {
		return 1;
	}

	public boolean equals(final Object other) {
		return other instanceof BooleanType;
	}

	public String toString() {
		return "BooleanType";
	}
}
