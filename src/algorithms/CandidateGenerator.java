package algorithms;

import java.util.ArrayList;

import datastructures.AtomicChangeEvent;
import datastructures.TableHistorySequence;
import output.Candidate;
import output.Pattern;
import output.Subsequence;

public class CandidateGenerator {
	
	private ArrayList<TableHistorySequence> tablesHistory;
	
	public  CandidateGenerator(ArrayList<TableHistorySequence> tablesHistory) {
		this.tablesHistory = tablesHistory;
	}
	
	public ArrayList<Candidate> generateCandidates(ArrayList<Candidate> frequentSequences,int k){
		ArrayList<Candidate> prunedCandidates = new ArrayList<Candidate>();
		ArrayList<Candidate> candidates = new ArrayList<Candidate>();
		if(k == 2){
			for(int i=0;i < frequentSequences.size(); i++){
				for(int j = 0 ; j < frequentSequences.size() ; j++){
					ArrayList<Candidate> tempCandidates = generate2SizeCandidates(frequentSequences.get(i),frequentSequences.get(j),k);
					for(Candidate candidate: tempCandidates){
						if(!prunedCandidates.contains(candidate)){
							prunedCandidates.add(candidate);
						}
					}
				}
			}
		}
		else{
			System.gc();
			for(int i=0;i < frequentSequences.size(); i++){
				for(int j = i+1; j < frequentSequences.size(); j++){
					//e.g checks for (1)(2)(3) and (1,2)(2)
					Candidate pattern1 = frequentSequences.get(i).cutPreffix();
					Candidate pattern2 = frequentSequences.get(j).cutSuffix();
					//Check if mergable
					if(pattern1.equals(pattern2)){
						//merge into new
						Candidate candidate = new Candidate();
						candidate.setLength(k);
						candidate.mergeSubsequences(frequentSequences.get(i),frequentSequences.get(j));
						candidates.add(candidate);
					}
					//e.g checks for (1,2)(2) and (1)(2)(3) reverse than above					
					pattern2 = frequentSequences.get(i).cutSuffix();
					pattern1 = frequentSequences.get(j).cutPreffix();
					//Check if mergable
					if(pattern1.equals(pattern2)){
						//merge into new
						Candidate candidate = new Candidate();
						candidate.setLength(k);
						candidate.mergeSubsequences(frequentSequences.get(j),frequentSequences.get(i));
						candidates.add(candidate);
					}
				}
			}
			prunedCandidates = pruneCandidates(candidates);
		}
		return prunedCandidates;
	}
	
	public ArrayList<Candidate> pruneCandidates(ArrayList<Candidate> candidates){
		ArrayList<Candidate> pruned = new ArrayList<Candidate>();
		for(Candidate candidate:candidates){
			if(candidate.existsInHistory(tablesHistory)){
				Candidate cand = new Candidate();
				cand.setMetrics(candidate.getMetrics());
				cand.setLength(candidate.getLength());
				cand.setSubsequences(candidate.getSubsequences());
				cand.setSupport(candidate.getSupport());
				pruned.add(cand);
			}
		}
		return pruned;
	}
	
	public ArrayList<Candidate> generate2SizeCandidates(Candidate pattern1,Candidate pattern2,int k){
		//Base case: each pattern has 1 subsequence and 1 event
		AtomicChangeEvent event1 = pattern1.getSubsequences().get(0).getAtomicChangeEvents().get(0); 
		AtomicChangeEvent event2 = pattern2.getSubsequences().get(0).getAtomicChangeEvents().get(0); 
		ArrayList<Candidate> ret = new ArrayList<Candidate>();
		Candidate candidate = new Candidate();
		candidate.setLength(k);
		//Add {x,x}
		Subsequence subSequence = new Subsequence();
		subSequence.addAtomicChangeEvent(event1);
		subSequence.addAtomicChangeEvent(event2);
		subSequence.sortEvents();
		
		candidate.addSubsequence(subSequence);
		ret.add(candidate);
		candidate = new Candidate();
		candidate.setLength(k);
		//Add {x}{x}
		subSequence = new Subsequence();
		subSequence.addAtomicChangeEvent(event1);
		candidate.addSubsequence(subSequence);
		subSequence = new Subsequence();
		subSequence.addAtomicChangeEvent(event2);
		candidate.addSubsequence(subSequence);
		ret.add(candidate);
		return ret;
	}
}
