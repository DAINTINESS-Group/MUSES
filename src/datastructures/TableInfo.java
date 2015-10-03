package datastructures;

public abstract class TableInfo {
	
	private String name;
	private TableHistorySequence tableHistory;
	
	public TableHistorySequence getHistory(){
		return this.tableHistory;
	}
	
	public void setHistory(TableHistorySequence tableHistory){
		this.tableHistory = tableHistory;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
