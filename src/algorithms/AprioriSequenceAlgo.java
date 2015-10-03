package algorithms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javafx.util.Pair;
import output.Candidate;
import output.Metrics;
import output.Pattern;
import output.Subsequence;
import parameters.AprioriSequenceParameters;
import parameters.Parameters;
import datastructures.AtomicChangeEvent;
import datastructures.AtomicChangeFactory;
import datastructures.TableHistoryElements;
import datastructures.TableHistorySequence;



public class AprioriSequenceAlgo extends Algorithm{
	
	
	private CandidateGenerator candidateGenerator;
	private SupportCounter suportCounter;
	private ArrayList<Pattern>  patterns;
	private ArrayList<TableHistorySequence> sequences;
	
	public AprioriSequenceAlgo(Parameters param,ArrayList<TableHistorySequence> sequences){
		super(param);
		patterns = new ArrayList<Pattern>();
		Parameters parameter = super.getParameter();
		suportCounter = SupportCounterFactory.createSupportCounter(((AprioriSequenceParameters) parameter).getSupportType(),sequences,((AprioriSequenceParameters) parameter).getDatasetInfo());
		candidateGenerator = new CandidateGenerator(sequences);
		this.sequences = sequences;
	}
	
	public void run() {
		int k = 1;
		Parameters parameter = super.getParameter();
		double minSup = ((AprioriSequenceParameters) parameter).getMinSup();
		ArrayList<Candidate> candidates = findFrequentEvents();
		//used for last time frequent patterns has patterns
		ArrayList<Candidate> previousFrequentCandidates = candidates;
		while(candidates != null && !candidates.isEmpty()){
			k++;
			previousFrequentCandidates = candidates;
			candidates = candidateGenerator.generateCandidates(candidates, k);
			//Count support for each candidate and keep the frequent only
			suportCounter.countSupport(candidates);
			//FilleroutAneparkeis
			candidates = suportCounter.getFrequentCandidates(minSup,k);
			for(Candidate candidate:candidates){
				Pattern pattern = new Pattern();
				Metrics metrics = new Metrics();
				metrics.setNumOccurences(candidate.getMetrics().getNumOccurences());
				metrics.setSupport(candidate.getMetrics().getSupport());
				pattern.setMetrics(metrics);
				for(int i=0;i<candidate.getSubsequences().size();i++){
					Subsequence sub = new Subsequence();
					//pattern.addSubsequence(candidate.getSubsequences().get(i));
					ArrayList<AtomicChangeEvent> ev = candidate.getSubsequences().get(i).getAtomicChangeEvents();
					for(int j=0;j<ev.size();j++){
						sub.addAtomicChangeEvent(ev.get(j));
					}
					ArrayList<TableHistoryElements> th = new ArrayList<TableHistoryElements>();
					for(TableHistoryElements the:candidate.getSubsequences().get(i).getTableSequence()){
						th.add(the);
					}
					sub.setTableSequence(th);
					pattern.addSubsequence(sub);
					//patterns.add(pattern);
				}
				patterns.add(pattern);
			}
			for(int i=0;i < previousFrequentCandidates.size(); i++){
				Pattern pattern = new Pattern();
				pattern.setMetrics(previousFrequentCandidates.get(i).getMetrics());
				pattern.setSubsequences(previousFrequentCandidates.get(i).getSubsequences());
				pattern.setCandidate(previousFrequentCandidates.get(i));
				//patterns.add(pattern);
			}
		}
	}
	
	private ArrayList<Candidate> findFrequentEvents(){
		Parameters parameter = super.getParameter();
		double minSup = ((AprioriSequenceParameters) parameter).getMinSup();
		ArrayList<Candidate> frequentPatterns = new ArrayList<>();
		HashMap<AtomicChangeEvent, Double> patterns = new HashMap<AtomicChangeEvent, Double>();
		for(TableHistorySequence sequence:sequences){
			sequence.getTableInfo().getName();
			//HashMap<String, Integer> pairs = new HashMap<String, Integer>();
			HashMap<AtomicChangeEvent, Integer> pair = new HashMap<AtomicChangeEvent, Integer>();
			for(TableHistoryElements element:sequence.getTableHistoryElements()){
				element.getTransitionInfo().getId();
			    for(AtomicChangeEvent event:element.getAtomicChangeEvents()){
			    	if(!pair.containsKey(event)){
			    		pair.put(event, 1);
			    	}
			    }
			}
			if(!pair.isEmpty()){
				for (Entry<AtomicChangeEvent, Integer> entry : pair.entrySet()) {
					if(patterns.containsKey(entry.getKey())){
						patterns.put(entry.getKey(), patterns.get(entry.getKey()) + 1.0);
					}
					else{
						patterns.put(entry.getKey(), 1.0);
					}
				}
			}
		}
		/*Support counting */
		for (Entry<AtomicChangeEvent, Double> entry : patterns.entrySet()) {
			patterns.put(entry.getKey(), entry.getValue()/((AprioriSequenceParameters) parameter).getDatasetInfo().getNumOfTables());//sequences.size());
		}
		
		for (Entry<AtomicChangeEvent, Double> entry : patterns.entrySet()) {
			if(entry.getValue() > minSup){
				AtomicChangeEvent event = AtomicChangeFactory.stringToEvent(entry.getKey().toString());
				event = entry.getKey();
				Subsequence subsequence = new Subsequence();
				subsequence.addAtomicChangeEvent(event);
				Candidate candidate = new Candidate();
				candidate.addSubsequence(subsequence);
				Metrics metric = new Metrics();
				metric.setSupport(entry.getValue());
				metric.setNumOccurences((int)(entry.getValue()*((AprioriSequenceParameters) parameter).getDatasetInfo().getNumOfTables()));//sequences.size()));
				candidate.setMetrics(metric);
				frequentPatterns.add(candidate);
				
			}
		}
		return frequentPatterns;
	}
	
	public ArrayList<Pattern> getPatterns(){
		return this.patterns;
	}
	
}
