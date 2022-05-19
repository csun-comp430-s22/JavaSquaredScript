package parser.Declarations;

import parser.interfaces.AccessType;

public class InstanceDec {

	public final AccessType accessType;
	public final Vardec vardec;

	public InstanceDec(final AccessType accessType, Vardec vardec) {
		this.accessType = accessType;
		this.vardec = vardec;
	}

	public int hashCode() {
		return (accessType.hashCode() +
			vardec.hashCode());
	}

	public boolean equals(final Object other) {
		if (other instanceof InstanceDec) {
			final InstanceDec otherDef = (InstanceDec)other;
			return (accessType.equals(otherDef.accessType) &&
				vardec.equals(otherDef.vardec));
		} else {
			return false;
		}
	}

	public String toString() {
		return ("InstanceDec(" +
			accessType.toString() + ", " +
			vardec.toString() +")");
	}
}
