package parameters;



public class AprioriSequenceParameters extends Parameters{
	
	private double minSup;
	private SupportType supportType;
	private Dataset datasetInfo;
	public AprioriSequenceParameters(String algoName,double minSup,Dataset ds){
		super(algoName);
		this.setMinSup(minSup);
		this.setDatasetInfo(ds);
	}

	public double getMinSup() {
		return minSup;
	}

	public void setMinSup(double minSup) {
		this.minSup = minSup;
	}
	
	public String getName() {
		return super.getAlgoName();
	}

	public SupportType getSupportType() {
		return supportType;
	}

	public void setSupportType(SupportType supportType) {
		this.supportType = supportType;
	}

	public Dataset getDatasetInfo() {
		return datasetInfo;
	}

	public void setDatasetInfo(Dataset datasetInfo) {
		this.datasetInfo = datasetInfo;
	}

	
	
}
