package output;

import java.util.ArrayList;

import datastructures.AtomicChangeEvent;
import datastructures.TransitionInfo;

public class Pattern {

	private ArrayList<Subsequence> subsequences;
	private Candidate candidate;
	private Metrics metrics;
	
	public Pattern(){
		setSubsequences(new ArrayList<Subsequence>());
		metrics = new Metrics();
		candidate = new Candidate();
	}
	
	public Metrics getMetrics() {
		return metrics;
	}
	
	public void setMetrics(Metrics metrics) {
		this.metrics = metrics;
	}

	public ArrayList<Subsequence> getSubsequences() {
		return subsequences;
	}

	public void setSubsequences(ArrayList<Subsequence> subsequences) {
		this.subsequences = subsequences;
	}
	
	public void addSubsequence(Subsequence subsequence) {
		this.subsequences.add(subsequence);
	}
	
	public AtomicChangeEvent getLastEvent(){
		Subsequence selectedSubsequence = this.subsequences.get(subsequences.size()-1);
		int lastPosition = selectedSubsequence.getAtomicChangeEvents().size();
		if(lastPosition == 1)
			return selectedSubsequence.getAtomicChangeEvents().get(0);
		else
			return selectedSubsequence.getAtomicChangeEvents().get(lastPosition-1);
	}
	
	public AtomicChangeEvent getSecondLastEvent(){
		Subsequence selectedSubsequence = this.subsequences.get(subsequences.size()-1);
		int lastPosition = selectedSubsequence.getAtomicChangeEvents().size();
		if(lastPosition == 1){
			selectedSubsequence = this.subsequences.get(subsequences.size()-2);
			lastPosition = selectedSubsequence.getAtomicChangeEvents().size();
			return selectedSubsequence.getAtomicChangeEvents().get(lastPosition);
		}else
			return selectedSubsequence.getAtomicChangeEvents().get(lastPosition-1);
	}
	
	public AtomicChangeEvent getFirstEvent(){
		Subsequence selectedSubsequence = this.subsequences.get(0);
		return selectedSubsequence.getAtomicChangeEvents().get(0);
	}
	
	public boolean equals(Pattern pattern){
		ArrayList<Subsequence> subSequences = pattern.getSubsequences();
		if(subsequences.size() != subSequences.size()) return false;
		for(int i = 0; i < subsequences.size();i++){
			ArrayList<AtomicChangeEvent> events1 = this.subsequences.get(i).getAtomicChangeEvents();
			ArrayList<AtomicChangeEvent> events2 = subSequences.get(i).getAtomicChangeEvents();
			if(events1.size() != events2.size()) return false;
			for(int j = 0; j < events1.size();j++){
					if(!events1.get(j).equals(events2.get(j))){
						return false;
					}
			}
		}
		return true;
	}
	
	public String toString(){
		String stringPattern = "<";
			ArrayList<Subsequence> subsequences = this.getSubsequences();
			for(int i = 0; i < subsequences.size(); i++){
				ArrayList<AtomicChangeEvent> ev = subsequences.get(i).getAtomicChangeEvents();
				stringPattern = stringPattern.concat("{");
				for(int k = 0; k< ev.size(); k++){
					if(k == ev.size() - 1)
						stringPattern = stringPattern.concat(ev.get(k).toString());
					else
						stringPattern = stringPattern.concat(ev.get(k).toString() + ",");
				}
				stringPattern = stringPattern.concat("}");
				if(i != subsequences.size() - 1)
					stringPattern = stringPattern.concat(",");
			}
			stringPattern = stringPattern.concat(">");
		return stringPattern;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}
}
