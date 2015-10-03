package parameters;

import java.util.HashMap;

public class Dataset {
	
	private String transitionsPath;
	private String statsPath;
	private int numOfTables;
	private HashMap<String, Integer> tablesChanges;
	private int numOfActiveCells;
	
	public Dataset(){
		tablesChanges = new HashMap<String, Integer>();
	}
	public String getTransitionsPath() {
		return transitionsPath;
	}
	public void setTransitionsPath(String transitionsPath) {
		this.transitionsPath = transitionsPath;
	}
	public String getStatsPath() {
		return statsPath;
	}
	public void setStatsPath(String statsPath) {
		this.statsPath = statsPath;
	}
	public int getNumOfTables() {
		return numOfTables;
	}
	public void setNumOfTables(int numOfTables) {
		this.numOfTables = numOfTables;
	}
	public HashMap<String, Integer> getTablesChanges() {
		return tablesChanges;
	}
	public void setTablesChanges(HashMap<String, Integer> tablesChanges) {
		this.tablesChanges = tablesChanges;
	}
	
	public void setTableChanges(String tableName, int changes){
		this.tablesChanges.put(tableName, changes);	
	}
	public int getNumOfActiveCells() {
		return numOfActiveCells;
	}
	public void setNumOfActiveCells(int numOfActiveCells) {
		this.numOfActiveCells = numOfActiveCells;
	}
	
}
