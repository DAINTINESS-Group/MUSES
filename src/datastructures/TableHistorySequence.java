package datastructures;

import java.util.ArrayList;

public class TableHistorySequence {
	
	private TableInfo tableInfo;
	private ArrayList<TableHistoryElements> tableHistoryElements;
	
	public TableHistorySequence(){
		tableHistoryElements = new ArrayList<TableHistoryElements>();
	}
	
	public TableInfo getTableInfo() {
		return tableInfo;
	}
	
	public void addElement(TableHistoryElements element){
		this.tableHistoryElements.add(element);
	}

	public void setTableInfo(TableInfo tableInfo) {
		this.tableInfo = tableInfo;
	}

	public ArrayList<TableHistoryElements> getTableHistoryElements() {
		return tableHistoryElements;
	}

	public void setTableHistoryElements(ArrayList<TableHistoryElements> tableHistoryElements) {
		this.tableHistoryElements = tableHistoryElements;
	} 
	
	
	//( When there's no changes in table@specific transition)
	public void fillWithNoEvents(){
		
	}
}
