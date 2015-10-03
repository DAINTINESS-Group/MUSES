package algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import output.Candidate;
import output.Metrics;
import output.Subsequence;
import parameters.Dataset;
import datastructures.AtomicChangeEvent;
import datastructures.TableHistoryElements;
import datastructures.TableHistorySequence;

public class CDISTCounter extends SupportCounter{
	
	public CDISTCounter(ArrayList<TableHistorySequence> sequences,Dataset ds) {
		super(sequences,ds);
	}
	@Override
	public void countSupport(ArrayList<Candidate> candidates) {
		frequentCandidates = candidates;
		for(int l=0; l < candidates.size(); l++){
			candidates.get(l).getMetrics().setNumOccurences(0);
		}
		for(int l=0; l < candidates.size(); l++){
		//	ArrayList<Subsequence> subsequences1 = candidates.get(l).getSubsequences();
			ArrayList<Subsequence> subsequences = candidates.get(l).getSubsequences();
			for(int i = 0; i < subsequences.size(); i++){
				subsequences.get(i).getTableSequence().clear();
			}
			for(TableHistorySequence tableHistory:sequences){
				HashMap<Integer, ArrayList<AtomicChangeEvent>> eventTimestampFlag = 
						new HashMap<Integer,ArrayList<AtomicChangeEvent>>();
				HashMap<Integer, ArrayList<Boolean>> atomicChangeUsedinTransition = 
						new HashMap<Integer,ArrayList<Boolean>>();
				ArrayList<TableHistoryElements> elements = tableHistory.getTableHistoryElements();
				//Initialize data structures
				for(int j = 0; j < elements.size(); j++){
//					HashMap<AtomicChangeEvent,Boolean> tmp = new HashMap<AtomicChangeEvent, Boolean>();
					ArrayList<Boolean> tmpBool = new ArrayList<Boolean>();
					ArrayList<AtomicChangeEvent> events = new ArrayList<AtomicChangeEvent>();
					for(AtomicChangeEvent event:elements.get(j).getAtomicChangeEvents()){
						tmpBool.add(false);
						events.add(event);
					}
					atomicChangeUsedinTransition.put(elements.get(j).getTransitionInfo().getId(), tmpBool);
					eventTimestampFlag.put(elements.get(j).getTransitionInfo().getId(),events);
				}
				int startingPos = 0;
				boolean isFinished = false;
				TableHistoryElements selected[] = new TableHistoryElements[subsequences.size()];
				//ArrayList<TableHistoryElements> selectedTables = new ArrayList<TableHistoryElements>();
				int firstPosition = 0;
				boolean firstInwhile = true;
				while(!isFinished){
					int currentPosition = 0;
					if(!firstInwhile){
						currentPosition = firstPosition;// +1;
					}
					int itemsFoundInHistory = 0;
					boolean firstFound = false;
					for(int i = startingPos; i < subsequences.size(); i++){
						int j;
						
						for(j = currentPosition; j < elements.size(); j++){
							//if(eventTimestampFlag.containsKey(elements.get(j).getTransitionInfo().getId())){
							ArrayList<Boolean> boolEvents = atomicChangeUsedinTransition.get(elements.get(j).getTransitionInfo().getId());
							if(subsequences.get(i).isInHistoryElement(elements.get(j))){
								
									if(!isSubsequenceUsed(boolEvents,subsequences.get(i),elements.get(j))){
										if(!firstFound){
											firstPosition = j;
											firstFound = true;
										}
										firstInwhile = false;
										currentPosition = j+1;
										itemsFoundInHistory++;
										//keep here elements.get(j) and add them in if down
										selected[i] = elements.get(j);
										atomicChangeUsedinTransition.put(elements.get(j).getTransitionInfo().getId(), boolEvents);
										break;
									}
							}
							//}
						}
						if(j >= elements.size()){
							isFinished = true;
						}
					}
					if(itemsFoundInHistory == subsequences.size()){
						
						for(int i = 0; i < subsequences.size(); i++){
							subsequences.get(i).addTransitionInfo(selected[i].getTransitionInfo());
							subsequences.get(i).addTableInfo(selected[i]);
						}
						Candidate c = candidates.get(l);				
						c.setSubsequences(subsequences);
						Metrics metrics = new Metrics();
						metrics.setNumOccurences(candidates.get(l).getMetrics().getNumOccurences() + 1);
						c.setMetrics(metrics);
						candidates.set(l, c);
						this.frequentCandidates.set(l,c);
					}
				}
			}	
		}
	}

	@Override
	public ArrayList<Candidate> getFrequentCandidates(Double minSup,int k) {
		ArrayList<Candidate> candidates = new ArrayList<Candidate>();
		for(Candidate candidate:frequentCandidates){
			double support = candidate.getMetrics().getNumOccurences() / (double)super.datasetInfo.getNumOfActiveCells();//(double)sum;//super.numofTables*sequences.get(0).getTableHistoryElements().size();/*(double)sequences.size()*/
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
	
	private boolean isSubsequenceUsed(ArrayList<Boolean> eventsUsed,Subsequence subsequence,TableHistoryElements element){
		int numofEventsUsed = 0;
		ArrayList<Integer> jesused = new ArrayList<Integer>();
		for(int i = 0; i < subsequence.getAtomicChangeEvents().size(); i++){
			for(int j=0; j < element.getAtomicChangeEvents().size(); j++){
				if((eventsUsed.get(j) == true) && (subsequence.getAtomicChangeEvents().get(i).equals(element.getAtomicChangeEvents().get(j))) && ((jesused.isEmpty() || !jesused.contains(j))) ){
					numofEventsUsed++; // return true;
					jesused.add(j);
					break;
				}
			}
			
		}
		if(numofEventsUsed == subsequence.getAtomicChangeEvents().size())
			return true;
		
		ArrayList<Integer> jsused = new ArrayList<Integer>();
		for(int i = 0; i < subsequence.getAtomicChangeEvents().size(); i++){
			for(int j=0; j < element.getAtomicChangeEvents().size();j++){
				if(subsequence.getAtomicChangeEvents().get(i).equals(element.getAtomicChangeEvents().get(j)) && (jsused.isEmpty() || !jsused.contains(j))){
					jsused.add(j);
					eventsUsed.set(j, true);
					break;
				}
			}
		}
		return false;
	}

}
