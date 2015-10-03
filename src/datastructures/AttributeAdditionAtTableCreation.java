package datastructures;

public class AttributeAdditionAtTableCreation extends AtomicChangeEvent{

	@Override
	public String toString() {
		return "AttrAdd@TableCreation";
	}

	@Override
	public boolean equals(Object event) {
		 if (!(event instanceof AttributeAdditionAtTableCreation))
	            return false;
		 else
			 return true;
	}

	@Override
	public int hashCode() {
		return 2;
	}

}
