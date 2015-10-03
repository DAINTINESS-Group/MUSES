package datastructures;

import java.util.ArrayList;

public class TableHistoryElements {
	
	private TransitionInfo transitionInfo;
	private TableHistorySequence tableSequence;
	private ArrayList<AtomicChangeEvent> atomicChangeEvents;
	
	public TableHistoryElements(){
		//transitionInfo = TransitionFactory.createTransitionInfo("Transition");
		atomicChangeEvents = new ArrayList<AtomicChangeEvent>();
	}
	
	public TransitionInfo getTransitionInfo() {
		return transitionInfo;
	}
	
	public void addAtomicChange(AtomicChangeEvent atomicChange){
		this.atomicChangeEvents.add(atomicChange);
	}

	public void setTransitionInfo(TransitionInfo transitionInfo) {
		this.transitionInfo = transitionInfo;
	}

	public ArrayList<AtomicChangeEvent> getAtomicChangeEvents() {
		return atomicChangeEvents;
	}

	public void setAtomicChangeEvent(ArrayList<AtomicChangeEvent> atomicChangeEvents) {
		this.atomicChangeEvents = atomicChangeEvents;
	}

	public TableHistorySequence getTableSequence() {
		return tableSequence;
	}

	public void setTableSequence(TableHistorySequence tableSequence) {
		this.tableSequence = tableSequence;
	}
	public boolean equals(TableHistoryElements el){
		if(this.atomicChangeEvents.size() != el.getAtomicChangeEvents().size())
			return false;
		for(int i=0;i<atomicChangeEvents.size();i++){
			if(atomicChangeEvents.get(i) != el.getAtomicChangeEvents().get(i)){
				return false;
			}
		}
		return true;
	}
}
