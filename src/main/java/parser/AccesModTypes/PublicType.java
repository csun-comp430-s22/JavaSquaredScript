package parser.AccesModTypes;

import parser.interfaces.AccessType;

public class PublicType implements AccessType {

	public int hashCode() {
		return 1;
	}

	public boolean equals(final Object other) {
		return other instanceof PublicType;
	}

	public String toString() {
		return "PublicType";
	}

}
