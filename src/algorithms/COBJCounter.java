package algorithms;

import java.util.ArrayList;

import output.Candidate;
import output.Metrics;
import output.Subsequence;
import parameters.Dataset;
import datastructures.TableHistoryElements;
import datastructures.TableHistorySequence;
import datastructures.TableInfo;

public class COBJCounter extends SupportCounter{
	
	public COBJCounter(ArrayList<TableHistorySequence> sequences,Dataset ds) {
		super(sequences,ds);
		
	}
	public void countSupport(ArrayList<Candidate> candidates){
		frequentCandidates = candidates;
		ArrayList<Candidate> cand = new ArrayList<Candidate>();
		for(int l=0; l < candidates.size(); l++){
			candidates.get(l).getMetrics().setNumOccurences(0);
			cand.add(candidates.get(l));
		}
		for(int l=0; l < candidates.size(); l++){
			ArrayList<Subsequence> subsequences = candidates.get(l).getSubsequences();
			for(int i = 0; i < candidates.get(l).getSubsequences().size(); i++){
				candidates.get(l).getSubsequences().get(i).clear();
				cand.get(l).getSubsequences().get(i).clear();
			}
			for(TableHistorySequence tableHistory:sequences){
				ArrayList<TableHistoryElements> elements = tableHistory.getTableHistoryElements();
				int currentPosition = 0;
				int itemsFoundInHistory = 0;
				TableHistoryElements selected[] = new TableHistoryElements[subsequences.size()];
				for(int i = 0; i < subsequences.size(); i++){
					for(int j = currentPosition; j < elements.size(); j++){
						if(subsequences.get(i).isInHistoryElement(elements.get(j))){
							currentPosition = j+1;
							itemsFoundInHistory++;
							//keep here elements.get(j) and add them in if down
							selected[i] = elements.get(j);
							break;
						}
					}
				}
				if(itemsFoundInHistory == subsequences.size()){
					for(int i = 0; i < subsequences.size(); i++){
						subsequences.get(i).addTransitionInfo(selected[i].getTransitionInfo());
						subsequences.get(i).addTableInfo(selected[i]);
						
					}
					
					Metrics metrics = new Metrics();
					metrics.setNumOccurences(candidates.get(l).getMetrics().getNumOccurences() + 1);

					this.frequentCandidates.get(l).setSubsequences(subsequences);
					this.frequentCandidates.get(l).setMetrics(metrics);
					cand.get(l).setSubsequences(subsequences);
				}
				
				
			}
		}
	}
	
	@Override
	public ArrayList<Candidate> getFrequentCandidates(Double minSup,int k) {
		ArrayList<Candidate> candidates = new ArrayList<Candidate>();
		for(Candidate candidate:frequentCandidates){
			double support = candidate.getMetrics().getNumOccurences() / (double)super.datasetInfo.getNumOfTables();
			if(support > minSup){
				Candidate candidate1 = new Candidate();
				candidate1.setSubsequences(candidate.getSubsequences());
				Metrics metrics = new Metrics();
				metrics.setSupport(support);
				metrics.setNumOccurences(candidate.getMetrics().getNumOccurences());
				candidate1.setMetrics(metrics);
				candidate1.setLength(k);
				candidates.add(candidate1);
			}
		}
		return candidates;
	}
}
