package gui;

import javafx.beans.property.SimpleStringProperty;

public class TransitionSubsequencePair {

	private SimpleStringProperty transition;
	private SimpleStringProperty subsequence;
	private SimpleStringProperty table;
	
	public TransitionSubsequencePair(String tr,String sub,String table) {
		transition = new SimpleStringProperty(tr);
		subsequence = new SimpleStringProperty(sub);
		this.table = new SimpleStringProperty(table);
	}
	
	public String getTransition() {
		return transition.get();
	}
	public void setTransition(SimpleStringProperty transition) {
		this.transition = transition;
	}
	public String getSubsequence() {
		return subsequence.get();
	}
	public void setSubsequence(SimpleStringProperty subsequence) {
		this.subsequence = subsequence;
	}

	public String getTable() {
		return table.get();
	}

	public void setTable(SimpleStringProperty table) {
		this.table = table;
	}
}
