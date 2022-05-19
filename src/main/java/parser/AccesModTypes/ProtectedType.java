package parser.AccesModTypes;

import parser.interfaces.AccessType;

public class ProtectedType implements AccessType {

	public int hashCode() {
		return 3;
	}

	public boolean equals(final Object other) {
		return other instanceof ProtectedType;
	}

	public String toString() {
		return "ProtectedType";
	}

}
