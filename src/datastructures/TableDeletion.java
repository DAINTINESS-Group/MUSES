package datastructures;

public class TableDeletion extends AtomicChangeEvent{

	@Override
	public String toString() {
		return "TableDeletion";
	}
	
	@Override
	public boolean equals(Object event) {
		 if (!(event instanceof TableDeletion))
	            return false;
		 else
			 return true;
	}

	@Override
	public int hashCode() {
		return 8;
	}
}
