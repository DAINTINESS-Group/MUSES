package algorithms;

import java.util.ArrayList;

import output.Candidate;
import parameters.Dataset;
import datastructures.TableHistorySequence;



public  abstract class SupportCounter {
	
	protected ArrayList<TableHistorySequence> sequences;
	protected ArrayList<Candidate> frequentCandidates;
	protected Dataset datasetInfo;
	
	public SupportCounter(ArrayList<TableHistorySequence> sequences,Dataset ds){
		this.sequences = sequences;
		frequentCandidates = new ArrayList<Candidate>();
		this.datasetInfo = ds;
	}
	
	public abstract void countSupport(ArrayList<Candidate> candidates);
	public abstract ArrayList<Candidate> getFrequentCandidates(Double minSup,int k);
	
}
