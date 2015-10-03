package datastructures;

public class PrimaryKeyUpdate extends AtomicChangeEvent{

	@Override
	public String toString() {
		return "PrimaryKeyUpd";
	}
	
	@Override
	public boolean equals(Object event) {
		 if (!(event instanceof PrimaryKeyUpdate))
	            return false;
		 else
			 return true;
	}

	@Override
	public int hashCode() {
		return 6;
	}
}
