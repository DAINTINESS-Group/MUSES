package gui;

import javafx.beans.property.SimpleStringProperty;

public class PatternMetrics {

	private SimpleStringProperty pattern;
	private SimpleStringProperty support;
	private SimpleStringProperty occurences;
	
	public PatternMetrics(String pattern,String support,String occurences) {
		this.pattern = new SimpleStringProperty(pattern);
		this.support = new SimpleStringProperty(support);
		this.occurences = new SimpleStringProperty(occurences);
		
	}
	public String getPattern() {
		return pattern.get();
	}
	public void setPattern(SimpleStringProperty pattern) {
		this.pattern = pattern;
	}
	public String getSupport() {
		return support.get();
	}
	public void setSupport(SimpleStringProperty support) {
		this.support = support;
	}
	public String getOccurences() {
		return occurences.get();
	}
	public void setOccurences(SimpleStringProperty occurences) {
		this.occurences = occurences;
	}
}
