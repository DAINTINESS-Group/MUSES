package datastructures;

import java.util.ArrayList;

public abstract class TransitionInfo {
	
	private int startVersionId;
	private int endVersionId;
	private int startTimestamp;
	private int endTimestamp;
	private int id;
	private ArrayList<TableHistoryElements> historyElements;
	
	public TransitionInfo(){
		historyElements = new ArrayList<TableHistoryElements>();
	}
	
	public int getStartVersionId() {
		return startVersionId;
	}
	
	public void setStartVersionId(int startVersionId) {
		this.startVersionId = startVersionId;
	}
	
	public void addHistoryElement(TableHistoryElements element){
		historyElements.add(element);
	}
	
	public int getEndVersionId() {
		return endVersionId;
	}
	
	public void setEndVersionId(int endVersionId) {
		this.endVersionId = endVersionId;
	}
	
	public int getStartTimestamp() {
		return startTimestamp;
	}
	
	public void setStartTimestamp(int startTimestamp) {
		this.startTimestamp = startTimestamp;
	}
	
	public int getEndTimestamp() {
		return endTimestamp;
	}
	
	public void setEndTimestamp(int endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<TableHistoryElements> getHistoryElements() {
		return historyElements;
	}

	public void setHistoryElements(ArrayList<TableHistoryElements> historyElements) {
		this.historyElements = historyElements;
	}
	
}
