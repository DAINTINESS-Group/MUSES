package datastructures;

public class AttributeDataTypeUpdate extends AtomicChangeEvent{

	@Override
	public String toString() {
		return "AttrTypeUpd";
	}
	@Override
	public boolean equals(Object event) {
		 if (!(event instanceof AttributeDataTypeUpdate))
	            return false;
		 else
			 return true;
	}
	@Override
	public int hashCode() {
		return 3;
	}
}
