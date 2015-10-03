package output;

import java.awt.Event;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import datastructures.AtomicChangeEvent;
import datastructures.AtomicChangeFactory;
import datastructures.TableHistoryElements;
import datastructures.TableInfo;
import datastructures.TransitionInfo;

public class Subsequence {

	private ArrayList<AtomicChangeEvent> atomicChangeEvents;
	private ArrayList<TableHistoryElements> tableSequence;
	private ArrayList<TransitionInfo> transitionInfo;
	
	public Subsequence(){
		setAtomicChangeEvents(new ArrayList<AtomicChangeEvent>());
		setTableSequence(new ArrayList<TableHistoryElements>());
		setTransitionInfo(new ArrayList<TransitionInfo>());
	}

	public ArrayList<AtomicChangeEvent> getAtomicChangeEvents() {
		return atomicChangeEvents;
	}

	public void setAtomicChangeEvents(ArrayList<AtomicChangeEvent> atomicChangeEvents) {
		this.atomicChangeEvents = atomicChangeEvents;
	}
	
	public void addAtomicChangeEvent(AtomicChangeEvent atomicChangeEvent) {
		atomicChangeEvents.add(atomicChangeEvent);
	}
	
	public void sortEvents(){
		Collections.sort(atomicChangeEvents,new AtomicChangeComparator());
	}
	
	/*
	 * For each AtomicChange(Subsequence) if it appears in element keep ++counter and keep
	 * searching from the currentPosition of the atomicChange in elements and after.
	 * if my counter is equals to this.subsequence size return true
	 */
	public boolean isInHistoryElement(TableHistoryElements element,Subsequence subsequence){
		ArrayList<AtomicChangeEvent> events = element.getAtomicChangeEvents();
		int currentPosition = 0;
		int itemsFoundInElement = 0; //Counter which counts the number of events that appears in element 
		ArrayList<Integer> positionsUsed = new ArrayList<Integer>();
		for(int i=0; i < atomicChangeEvents.size(); i++ ){
			for(int j = 0/*currentPosition*/; j < events.size(); j++ ){
				if(!positionsUsed.contains(j) || positionsUsed.isEmpty()){
					if(atomicChangeEvents.get(i).equals(events.get(j))){
						positionsUsed.add(j);
						currentPosition = j+1;
						itemsFoundInElement++;
						break;
					}
				}
			}
		}
		if(itemsFoundInElement == atomicChangeEvents.size()){
			for(int i=0; i < atomicChangeEvents.size(); i++){
				atomicChangeEvents.get(i).getElement().clear();
				atomicChangeEvents.get(i).addElement(element);
			}
			subsequence.setAtomicChangeEvents(atomicChangeEvents);
			return true;
		}
		else
			return false;
	}
	
	public boolean isInHistoryElement(TableHistoryElements element){
		ArrayList<AtomicChangeEvent> events = element.getAtomicChangeEvents();
		int currentPosition = 0;
		int itemsFoundInElement = 0; //Counter which counts the number of events that appears in element 
		ArrayList<Integer> positionsUsed = new ArrayList<Integer>();
		for(int i=0; i < atomicChangeEvents.size(); i++ ){
			for(int j = 0/*currentPosition*/; j < events.size(); j++ ){
				if(!positionsUsed.contains(j) || positionsUsed.isEmpty()){
					if(atomicChangeEvents.get(i).equals(events.get(j))){
						positionsUsed.add(j);
						currentPosition = j+1;
						itemsFoundInElement++;
						break;
					}
				}
			}
		}
		if(itemsFoundInElement == atomicChangeEvents.size()){
			return true;
		}
		else
			return false;
	}
	
	public ArrayList<TableHistoryElements> getTableSequence() {
		return tableSequence;
	}
	
	public void clear(){
		tableSequence.clear();
	}

	public void setTableSequence(ArrayList<TableHistoryElements> tableSequence) {
		this.tableSequence = new ArrayList<TableHistoryElements>();
		for(TableHistoryElements the:tableSequence)
			this.tableSequence.add(the);
	}

	public ArrayList<TransitionInfo> getTransitionInfo() {
		return transitionInfo;
	}

	public void setTransitionInfo(ArrayList<TransitionInfo> transitionInfo) {
		this.transitionInfo = transitionInfo;
	}
	
	public void addTransitionInfo(TransitionInfo tr) {
		this.transitionInfo.add(tr);
	}
	public void addTableInfo(TableHistoryElements ti) {
		this.tableSequence.add(ti);
	}
	
	public String toString(){
		String stringSubseqquence="";
		for(int k = 0; k< atomicChangeEvents.size(); k++){
			if(k == (atomicChangeEvents.size() - 1))
				stringSubseqquence = stringSubseqquence.concat(atomicChangeEvents.get(k).toString());
			else
				stringSubseqquence = stringSubseqquence.concat(atomicChangeEvents.get(k) + ",");
		}
		return stringSubseqquence;
	}
	
	@Override 
	public Subsequence clone(){
		Subsequence cloneSubsequence = new Subsequence();
		ArrayList<AtomicChangeEvent> event = new ArrayList<AtomicChangeEvent>();
		for(AtomicChangeEvent ev: this.getAtomicChangeEvents()){
			event.add(ev);
		}
		cloneSubsequence.setTransitionInfo(this.transitionInfo);
		cloneSubsequence.setTableSequence(this.tableSequence);
		cloneSubsequence.setAtomicChangeEvents(event);
		return cloneSubsequence;
	}	
}
