package datastructures;

public class TableCreation extends AtomicChangeEvent{

	@Override
	public String toString() {
		return "TableCreation";
	}
	
	@Override
	public boolean equals(Object event) {
		 if (!(event instanceof TableCreation))
	            return false;
		 else
			 return true;
	}

	@Override
	public int hashCode() {
		return 7;
	}

	@Override
	public AtomicChangeEvent clone() {
		return null;
	}

}
