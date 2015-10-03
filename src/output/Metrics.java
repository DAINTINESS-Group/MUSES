package output;

public class Metrics {

	private double support;
	private int numOccurences;

	public Metrics(){
		support = 0;
		numOccurences = 0;
	}

	public Metrics(Metrics old){
		support = old.support;
		numOccurences = old.numOccurences;
	}
	
	public double getSupport() {
		return support;
	}

	public void setSupport(double support) {
		this.support = support;
	}

	public int getNumOccurences() {
		return numOccurences;
	}

	public void setNumOccurences(int numOccurences) {
		this.numOccurences = numOccurences;
	}
}
