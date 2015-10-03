package datastructures;

public class AtomicChangeFactory {
	
	public static final String ATTR_INSERTION = "Insertion";
	public static final String ATTR_DELETION = "Deletion";
	public static final String ATTR_TYPE_CHANGE = "TypeChange";
	public static final String ATTR_KEY_CHANGE = "KeyChange";
	public static final String UPDATE = "Update";
	public static final String NEW_TABLE = "NewTable";
	public static final String DELETE_TABLE = "DeleteTable";
	public static final String UPDATE_TABLE = "UpdateTable";
	
	public static AtomicChangeEvent createEvent(String eventType){
		String[] types = eventType.split(":");
		if(types[0].equals(ATTR_INSERTION) && types[1].equals(NEW_TABLE))
			return new AttributeAdditionAtTableCreation();
		else if(types[0].equals(ATTR_INSERTION) && types[1].equals(UPDATE_TABLE))
			return new AttributeAdditionAtExistingTable();
		else if(types[0].equals(ATTR_DELETION) && types[1].equals(DELETE_TABLE))
			return new AttributeDeletionAtTableDeletion();
		else if(types[0].equals(ATTR_DELETION) && types[1].equals(UPDATE_TABLE))
			return new AttributeDeletionAtExistingTable();
		else if(types[0].equals(UPDATE) && types[1].equals(ATTR_TYPE_CHANGE))
			return new AttributeDataTypeUpdate();
		else if(types[0].equals(UPDATE) && types[1].equals(ATTR_KEY_CHANGE))
			return new PrimaryKeyUpdate();
		
		//Not reachable code TableCreation & TableDeletion are supercategories of AttrIns & AttrDel
		if(types[1].equals(NEW_TABLE))
			return new TableCreation();
		else if(types[1].equals(DELETE_TABLE))
			return new TableDeletion();
		return null;
	}
	
	public static AtomicChangeEvent createEvent(AtomicChangeEvent eventType){
		if(eventType instanceof AttributeAdditionAtTableCreation)
			return new AttributeAdditionAtTableCreation();
		else if(eventType instanceof AttributeAdditionAtExistingTable)
			return new AttributeAdditionAtExistingTable();
		else if(eventType instanceof AttributeDeletionAtTableDeletion)
			return new AttributeDeletionAtTableDeletion();
		else if(eventType instanceof AttributeDeletionAtExistingTable)
			return new AttributeDeletionAtExistingTable();
		else if(eventType instanceof AttributeDataTypeUpdate)
			return new AttributeDataTypeUpdate();
		else if(eventType instanceof PrimaryKeyUpdate)
			return new PrimaryKeyUpdate();
		else if(eventType instanceof TableDeletion)
			return new TableDeletion();
		else if(eventType instanceof TableCreation)
			return new TableCreation();
		
		return null;
	}
	
	public static AtomicChangeEvent stringToEvent(String eventType){
		if(eventType.equals("AttributeAddition@TableCreation"))
			return new AttributeAdditionAtTableCreation();
		else if(eventType.equals("AttributeAddition@ExistingTable"))
			return new AttributeAdditionAtExistingTable();
		else if(eventType.equals("AttributeDeletion@TableDeletion"))
			return new AttributeDeletionAtTableDeletion();
		else if(eventType.equals("AttributeDeletion@ExistingTable"))
			return new AttributeDeletionAtExistingTable();
		else if(eventType.equals("AttributeDataTypeUpdate"))
			return new AttributeDataTypeUpdate();
		else if(eventType.equals("PrimaryKeyUpdate"))
			return new PrimaryKeyUpdate();
		
		//Not reachable code TableCreation & TableDeletion are supercategories of AttrIns & AttrDel
		if(eventType.equals("TableCreation"))
			return new TableCreation();
		else if(eventType.equals("TableDeletion"))
			return new TableDeletion();
		return null;
	}
}
