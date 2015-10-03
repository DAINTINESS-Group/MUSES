package datastructures;

public class AttributeDeletionAtTableDeletion extends AtomicChangeEvent{

	@Override
	public String toString() {
		return "AttrDel@TableDel";
	}
	
	@Override
	public boolean equals(Object event) {
		 if (!(event instanceof AttributeDeletionAtTableDeletion))
	            return false;
		 else
			 return true;
	}

	@Override
	public int hashCode() {
		return 5;
	}
}
