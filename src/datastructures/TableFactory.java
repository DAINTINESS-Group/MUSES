package datastructures;

public class TableFactory {
	
	public static final String TABLE = "Table";
	public static final String TABLE_GROUP = "TableGroup";
	
	public static TableInfo createTableInfo(String type){
		if(type.equals(TABLE))
			return new Table();
		
		//TODO add more TableInfo returns
		return null;
	}
}
