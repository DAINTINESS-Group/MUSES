package datastructures;

import java.util.ArrayList;

public abstract class AtomicChangeEvent {
	
	private Details details;
	private ArrayList<TableHistoryElements> elements = new ArrayList<TableHistoryElements>();

	abstract public String toString();
	abstract public boolean equals(Object event);
	abstract public int hashCode();

	public Details getDetails() {
		return details;
	}

	public void setDetails(Details details) {
		this.details = details;
	}

	public ArrayList<TableHistoryElements> getElement() {
		return elements;
	}

	public void setElement(ArrayList<TableHistoryElements> element) {
		this.elements = element;
	}
	
	public void addElement(TableHistoryElements element){
		this.elements.add(element);
	}
	
	public String toDetails(){
		return details.toString();
	}
	
}
