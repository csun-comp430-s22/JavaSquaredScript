package parser;

public class PrivateType implements AccessType {

	public int hashCode() {
		return 2;
	}

	public boolean equals(final Object other) {
		return other instanceof PrivateType;
	}

	public String toString() {
		return "PrivateType";
	}

}
