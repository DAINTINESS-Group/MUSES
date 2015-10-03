package input;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import datastructures.AtomicChangeEvent;
import datastructures.AtomicChangeFactory;
import datastructures.Details;
import datastructures.TableFactory;
import datastructures.TableHistoryElements;
import datastructures.TableHistorySequence;
import datastructures.TableInfo;
import datastructures.TransitionFactory;
import datastructures.TransitionInfo;


public class TransitionsParser {
	
	private String path="";
	private ArrayList<TableHistorySequence> tablesHistory;
	private HashMap<String, Set<Integer>> cellCountActive = new HashMap<String, Set<Integer>>();
	public TransitionsParser(String path){
		tablesHistory = new ArrayList<TableHistorySequence>();
		this.path = path;
	}
	
	public ArrayList<TableHistorySequence> getTablesHistory(){
		return this.tablesHistory;
	}
	
	public int getActiveCells(){
		int activeCells = 0;;
		for(Map.Entry<String, Set<Integer>> entry : cellCountActive.entrySet()){ 
			activeCells += entry.getValue().size(); 
		}
	//	System.out.println("ACels: " + activeCells);
		return activeCells;
	}
	
	public void parse(){
		String line;
		
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(path));
			reader.readLine();
			String tableName = "";
			while ((line = reader.readLine()) != null) {
				String[] row = line.split(";");
				if(!cellCountActive.containsKey(row[3]) && !row[3].equals("-")){
					Set<Integer> tmp = new HashSet<Integer>();
					tmp.add(Integer.parseInt(row[0]));
					cellCountActive.put(row[3], tmp);
				}
				else if(!row[3].equals("-")){
					Set<Integer> tmp = cellCountActive.get(row[3]);
					tmp.add(Integer.parseInt(row[0]));
					cellCountActive.put(row[3], tmp);
				}
				//There is no sequence stored
				if(tablesHistory.isEmpty() && !row[3].equals("-")){ 
					TableInfo tableInfo = TableFactory.createTableInfo("Table");
					tableInfo.setName(row[3]);
					TableHistorySequence tableHistory = new TableHistorySequence();
					TableHistoryElements tableHistoryElement = new TableHistoryElements();
					TransitionInfo transitionInfo = TransitionFactory.createTransitionInfo("Transition");
					transitionInfo.setId(Integer.parseInt(row[0]));
					AtomicChangeEvent event = AtomicChangeFactory.createEvent(row[4]);
					Details details = new Details();
					details.setAttributeName(row[5]);
					details.setAttributeType(row[6]);
					event.setDetails(details);
					transitionInfo.addHistoryElement(tableHistoryElement);
					tableHistoryElement.setTransitionInfo(transitionInfo);
					tableInfo.setHistory(tableHistory);
					tableHistory.setTableInfo(tableInfo);
					tableHistoryElement.setTableSequence(tableHistory);//New line added
					event.addElement(tableHistoryElement);//New line added
					tableHistoryElement.addAtomicChange(event);
					tableHistory.addElement(tableHistoryElement);
					this.tablesHistory.add(tableHistory);
				}
				else{ //There are sequences stored
					int index = 0;
					boolean changesFound = false;
					boolean sequenceforTableCreated = false;
					TableHistorySequence temp = new TableHistorySequence();
					for(TableHistorySequence sequence:tablesHistory){
						//Table Column, sequence for table exist
						if(sequence.getTableInfo().getName().equals(row[3]) && !row[3].equals("-")){ 
							ArrayList<TableHistoryElements> tableHistoryElements = sequence.getTableHistoryElements();
							boolean timestampFound = false;
							TableHistoryElements selectedElement = new TableHistoryElements();
							for(TableHistoryElements element: tableHistoryElements){
								//Transition with specific id exists
								if(element.getTransitionInfo().getId() == Integer.parseInt(row[0])){
									selectedElement = element;
									AtomicChangeEvent event = AtomicChangeFactory.createEvent(row[4]);
									Details details = new Details();
									details.setAttributeName(row[5]);
									details.setAttributeType(row[6]);
									event.setDetails(details);
									selectedElement.addAtomicChange(event);
									/*boolean haveBirthDeath = false;
									AtomicChangeEvent ev = null;
									//creation and deletion atomic change here?
									if(row[4].split(":")[1].equals("NewTable") && !tableName.equals(row[3])){
										tableName = row[3];
										ev = new TableCreation();
										ev.addElement(selectedElement);
										selectedElement.addAtomicChange(ev);
										haveBirthDeath = true;
									}
									else if(row[4].split(":")[1].equals("DeleteTable") && !tableName.equals(row[3])){
										tableName = row[3];
										ev = new TableDeletion();
										ev.addElement(selectedElement);
										selectedElement.addAtomicChange(ev);
										haveBirthDeath = true;
									}*/
									selectedElement.getTransitionInfo().addHistoryElement(element); //add reference totransitionifo pointing to TableHistoryElement
									selectedElement.setTableSequence(sequence);//New line added
									event.addElement(selectedElement);//New line added
									//if(haveBirthDeath)
									//	ev.addElement(element);
									sequence.getTableHistoryElements().set(sequence.getTableHistoryElements().indexOf(selectedElement), selectedElement);
									timestampFound = true;
									changesFound = true;
								}
							}
							//Transition with specific id does not exists
							if(!timestampFound){ 
								TableHistoryElements element = new TableHistoryElements();
								TransitionInfo transitionInfo = TransitionFactory.createTransitionInfo("Transition");
								transitionInfo.setId(Integer.parseInt(row[0]));
								AtomicChangeEvent event = AtomicChangeFactory.createEvent(row[4]);
								Details details = new Details();
								details.setAttributeName(row[5]);
								details.setAttributeType(row[6]);
								event.setDetails(details);
								boolean haveBirthDeath = false;
								/*AtomicChangeEvent ev = null;
								System.out.println("Transition that not exist: " + transitionInfo.getId() + " -- " + row[4].split(":")[1]);
								//creation and deletion atomic change here?
								if(row[4].split(":")[1].equals("NewTable") && !tableName.equals(row[3])){
									tableName = row[3];
									ev = new TableCreation();
									ev.addElement(element);
									element.addAtomicChange(ev);
									haveBirthDeath = true;
								}
								else if(row[4].split(":")[1].equals("DeleteTable")){ //&& !tableName.equals(row[3])){
									System.out.println("I am here... : " + transitionInfo.getId() + " - - "+ row[4].split(":")[1]);
									tableName = row[3];
									ev = new TableDeletion();
									ev.addElement(element);
									element.addAtomicChange(ev);
									haveBirthDeath = true;
								}*/
								transitionInfo.addHistoryElement(element);//add reference totransitionifo pointing to TableHistoryElement
								element.setTransitionInfo(transitionInfo);
								tableHistoryElements.add(element);
								element.setTableSequence(sequence);//new line
								event.addElement(element);//new line
								//if(haveBirthDeath)
								//	ev.addElement(element);
								element.addAtomicChange(event);
								sequence.setTableHistoryElements(tableHistoryElements);
								changesFound = true;
							}	
						}
						temp = sequence;
						index = tablesHistory.indexOf(sequence);
					}
					if(!changesFound && !row[3].equals("-")){
						TableInfo tableInfo = TableFactory.createTableInfo("Table");
						TableHistorySequence tableHistory = new TableHistorySequence();
						tableInfo.setName(row[3]);
						//tableHistory.setTableInfo(tableInfo);
						TableHistoryElements tableHistoryElement = new TableHistoryElements();
						TransitionInfo transitionInfo = TransitionFactory.createTransitionInfo("Transition");
						transitionInfo.setId(Integer.parseInt(row[0]));
						tableHistoryElement.setTransitionInfo(transitionInfo);
						AtomicChangeEvent event = AtomicChangeFactory.createEvent(row[4]);
						Details details = new Details();
						details.setAttributeName(row[5]);
						details.setAttributeType(row[6]);
						event.setDetails(details);
						sequenceforTableCreated = true;
						tableInfo.setHistory(tableHistory);
						tableHistory.setTableInfo(tableInfo);
						tableHistoryElement.setTableSequence(tableHistory);//New line
						event.addElement(tableHistoryElement);//New line
						tableHistoryElement.addAtomicChange(event);
						tableHistory.addElement(tableHistoryElement);
						temp = tableHistory;
						changesFound = true;
						this.tablesHistory.add(temp);
							
					}
					if(changesFound && !sequenceforTableCreated){
						if(sequenceforTableCreated){
							this.tablesHistory.add(temp);
						}
						else
							tablesHistory.set(index, temp);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	//	print();
		
	}
	
	public void print(){
		PrintWriter writer;
		try {
			writer = new PrintWriter("outfile.csv");
			for(TableHistorySequence sequence:getTablesHistory()){
				writer.print(sequence.getTableInfo().getName() + ";");
				int changes = 0 ;
				for(TableHistoryElements element:sequence.getTableHistoryElements()){
					writer.print(element.getTransitionInfo().getId() + ";");
				    for(AtomicChangeEvent event:element.getAtomicChangeEvents()){
				    	writer.print(event.toString() + ";");
				    	changes++;
				    }
				}
				writer.println(changes + ";");
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
