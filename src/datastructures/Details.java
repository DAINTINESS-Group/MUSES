package datastructures;

public class Details {
	
	private String  attributeName;
	private String attributeType;
	
	public String getAttributeName() {
		return attributeName;
	}
	
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}
	
	public String toString(){
		return ("Attr Name: " + attributeName + "," + "Attr Type: " + attributeType);
	}
}
