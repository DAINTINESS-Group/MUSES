package parameters;

public abstract class Parameters {
	
	private String algorithmName;
	
	public Parameters(String name){
		this.algorithmName = name;
	}
	
	public String getAlgoName(){
		return algorithmName;
	}
}
