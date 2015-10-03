package output;

import java.util.ArrayList;
import java.util.Collection;

import datastructures.AtomicChangeEvent;
import datastructures.TableHistoryElements;
import datastructures.TableHistorySequence;
import datastructures.TableInfo;
import datastructures.Transition;

public class Candidate {
	
	private int length;
	private ArrayList<Subsequence> subsequences;
	private int support;
	private Metrics metrics;
	
	public Candidate(){
		subsequences = new ArrayList<Subsequence>();
		this.setSupport(0);
		setMetrics(new Metrics());
	}
	
	public Candidate(Candidate old){
		this.length = old.length;
		subsequences = this.cloneSubsequences();
		this.support = old.getSupport();
		setMetrics(old.metrics);
	}
	
	public ArrayList<Subsequence> cloneSubsequences(){
		ArrayList<Subsequence> newList = new ArrayList<Subsequence>();
		for(Subsequence subSeq: this.subsequences)
			newList.add(subSeq.clone());
		return newList;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public ArrayList<Subsequence> getSubsequences() {
		return subsequences;
	}
	
	public void setSubsequences(ArrayList<Subsequence> subsequences) {
		this.subsequences = new ArrayList<Subsequence>();
		for(Subsequence sub: subsequences){
			this.subsequences.add(sub);
		}
		//this.subsequences = subsequences;
	}
	
	public void addSubsequence(Subsequence subsequence){
		this.subsequences.add(subsequence);
	}
	
	public boolean mergeSubsequences(Candidate pattern1,Candidate pattern2){
		int lastPosition = pattern2.getSubsequences().size() - 1;
		ArrayList<Subsequence> editedSubsequence = new ArrayList<Subsequence>();
		Subsequence lastSubsequence = pattern2.getSubsequences().get(lastPosition);
		boolean inif = false;
		/* Case 1. from Book - if the 2 last events in pattern2 belonging to the same subsequence
		 * then the last event from pattern2 is merged into the last subsequence of pattern1 
		 * */
		if(lastSubsequence.getAtomicChangeEvents().size() > 1){
			editedSubsequence = pattern1.cloneSubsequences();
			editedSubsequence.get(editedSubsequence.size() - 1).addAtomicChangeEvent(pattern2.getLastEvent());
			inif=true;
		}
		else{
		/* Case 2. from Book - if the 2 last events in pattern2 are not belonging to the same subsequence
		 * then the last event from pattern2 is placed in a new subsequence in the end of pattern1 
		 * */
			editedSubsequence = pattern1.cloneSubsequences();
			Subsequence newSubsequence = new Subsequence();
			newSubsequence.addAtomicChangeEvent(pattern2.getLastEvent());
			editedSubsequence.add(newSubsequence);
		}
		this.setSubsequences(editedSubsequence);
		return inif;
	}
	
	public boolean existsInHistory(ArrayList<TableHistorySequence> tablesHistory){
		for(TableHistorySequence tableHistory:tablesHistory){
			ArrayList<TableHistoryElements> elements = tableHistory.getTableHistoryElements();
			int currentPosition = 0;
			int itemsFoundInHistory = 0;
			
	//		this.clearEvents();
			ArrayList<Subsequence> subSeqs = new ArrayList<Subsequence>();
			for(int i = 0; i < subsequences.size(); i++){
				for(int j = currentPosition; j < elements.size(); j++){
					Subsequence subsequence = new Subsequence();
					if(subsequences.get(i).isInHistoryElement(elements.get(j),subsequence)){
						currentPosition = j+1;
						itemsFoundInHistory++;
						//transition = elements.get(j).getTransitionInfo().getId();
						subSeqs.add(subsequence);
						break;
					}

				}
			}
			if(itemsFoundInHistory == subsequences.size()){
				//this.setSubsequences(subSeqs);
				return true;	
			}
		}
		return false;
	}
	
	

	public int getSupport() {
		return support;
	}

	public void setSupport(int support) {
		this.support = support;
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
	
	@Override
	public boolean equals(Object candidate1){
		Candidate candidate = (Candidate) candidate1;
		ArrayList<Subsequence> subSequences = candidate.getSubsequences();
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
	
	public Candidate cutPreffix(){
		ArrayList<Subsequence> subSeq = new ArrayList<Subsequence>();
		ArrayList<AtomicChangeEvent> events = subsequences.get(0).getAtomicChangeEvents();
		if(events.size() == 1){
			for(int i=1;i<subsequences.size();i++){
				subSeq.add(subsequences.get(i));
			}
			//subSeq.remove(0);
		}
		else{
			int jstart=0;
			for(int i=0; i<subsequences.size();i++){
				Subsequence sub = new Subsequence();
				if(i == 0) jstart=1;
				else jstart=0;
				for(int j=jstart;j<subsequences.get(i).getAtomicChangeEvents().size();j++){
					sub.addAtomicChangeEvent(subsequences.get(i).getAtomicChangeEvents().get(j));
				}
				subSeq.add(sub);
			}
		//	subSeq.get(0).getAtomicChangeEvents().remove(0);
		}
		
		Candidate retCandidate = new Candidate();
		retCandidate.setSubsequences(subSeq);
		return retCandidate;
	}
	
	public Candidate cutSuffix(){
		ArrayList<Subsequence> subSeq = new ArrayList<Subsequence>();
		
		ArrayList<AtomicChangeEvent> events = subsequences.get(subsequences.size()-1).getAtomicChangeEvents();
		if(events.size() == 1){
			//subSeq.remove(0);
			for(int i=0;i<subsequences.size()-1;i++){
				subSeq.add(subsequences.get(i));
			}
		}
		else{
			int jstart=0;
			for(int i=0;i<subsequences.size();i++){
				Subsequence sub = new Subsequence();
				if (i == subsequences.size()-1){
					for(int j=0;j<subsequences.get(i).getAtomicChangeEvents().size()-1;j++){
						//tmpevents.add(subsequences.get(i).getAtomicChangeEvents().get(j));
						sub.addAtomicChangeEvent(subsequences.get(i).getAtomicChangeEvents().get(j));
					}
				}
				else{
					for(int j=0;j<subsequences.get(i).getAtomicChangeEvents().size();j++){
						//tmpevents.add(subsequences.get(i).getAtomicChangeEvents().get(j));
						sub.addAtomicChangeEvent(subsequences.get(i).getAtomicChangeEvents().get(j));
					}
				}
//				subSeq.add(subsequences.get(i));
				subSeq.add(sub);
			}
		//	subSeq.get(subSeq.size()-1).getAtomicChangeEvents().remove(subSeq.get(subSeq.size()-1).getAtomicChangeEvents().size()-1);
		}
		Candidate retCandidate = new Candidate();
		retCandidate.setSubsequences(subSeq);
		return retCandidate;
	}

	public Metrics getMetrics() {
		return metrics;
	}

	public void setMetrics(Metrics metrics) {
		this.metrics = metrics;
	}
	
	public String toString(){
		String ret = "";
		for(Subsequence subsequence:subsequences){
			ret = ret + "{";
			for(int i =0; i<subsequence.getAtomicChangeEvents().size();i++){
				if(i == subsequence.getAtomicChangeEvents().size() -1)
					ret = ret  + subsequence.getAtomicChangeEvents().get(i).toString();
				else
					ret = ret  + subsequence.getAtomicChangeEvents().get(i).toString() + ",";
			}
			ret = ret + "}";
		}
		return ret;
	}
	
	public void clearEvents(){
		for(Subsequence subSeq:this.subsequences){
			for(AtomicChangeEvent ev: subSeq.getAtomicChangeEvents()){
				ev.getElement().clear();
			}
		}
	}
	
	public void cloneMe(ArrayList<Subsequence> subseqs){
		ArrayList<Subsequence> newlist = new ArrayList<Subsequence>();
		for(int i= 0; i< subseqs.size();i++)
			newlist.add(subseqs.get(i));
		this.setSubsequences(newlist);
		
	}
}
