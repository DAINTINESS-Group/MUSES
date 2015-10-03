package datastructures;

public class AttributeAdditionAtExistingTable extends AtomicChangeEvent{

	@Override
	public String toString() {
		return "AttrAdd@ExistTable";
	}

	@Override
	public boolean equals(Object event) {
		 if (!(event instanceof AttributeAdditionAtExistingTable))
	            return false;
		 if(event instanceof AttributeAdditionAtExistingTable)
			 return true;

		 return false;
	}

	@Override
	public int hashCode() {
		return 1;
	}


}
