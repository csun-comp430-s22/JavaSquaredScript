package parser;

public class StringType {

	public int hashCode() {
		return 2;
	}

	public boolean equals(final Object other) {
		return other instanceof StringType;
	}

	public String toString() {
		return "StringType";
	}
}
