package datastructures;

public class AttributeDeletionAtExistingTable extends AtomicChangeEvent{

	@Override
	public String toString() {
		return "AttrDel@ExistTable";
	}
	
	@Override
	public boolean equals(Object event) {
		 if (!(event instanceof AttributeDeletionAtExistingTable))
	            return false;
		 else
			 return true;
	}

	@Override
	public int hashCode() {
		return 4;
	}
}
