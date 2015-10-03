package gui;

import input.TransitionsParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Pair;
import output.Pattern;
import output.Subsequence;
import parameters.AprioriSequenceParameters;
import parameters.Dataset;
import parameters.SupportType;
import algorithms.Algorithm;
import algorithms.AprioriSequenceAlgo;
import datastructures.TableHistoryElements;
import datastructures.TableHistorySequence;

public class GuiEventHandler {
	@FXML private Label attrName;
    @FXML private Label attrType;  
    @FXML private Label eventType;
    @FXML private ListView tables;
    @FXML private Label supportCType;
    @FXML private Label fileLoaded;
    @FXML private Button runButton;
	@FXML private TableView<TransitionSubsequencePair> tableView;
	
	@FXML private TableView eventsTableView;
    @FXML private TextField minSupport;
    @FXML private TextField ini;
	@FXML private MenuBar menuBar = new MenuBar();
	@FXML private ListView<String> listView = new ListView<String>();
	@FXML private Set<String> stringSet;
	@FXML private TableView<PatternMetrics> tablePatternView;
    @FXML private TableColumn<PatternMetrics, String> pattern;
    @FXML private TableColumn<PatternMetrics, String> support;
    @FXML private TableColumn<PatternMetrics, String> occurences;
	@FXML
	ObservableList<PatternMetrics> patternMetric = FXCollections.observableArrayList();
	 @FXML
     private ComboBox<String> supportType;
	
	private  List<TransitionSubsequencePair> transitionss;
	private String outputPath;
	private File path;
	private Stage stage;
	private ObservableList<TransitionSubsequencePair> subsequencesInfo = FXCollections.observableArrayList();
	private Algorithm algo;
	private ArrayList<Pattern> patterns;
	private int selectedPattern;
	private ArrayList<ArrayList<TransitionSubsequencePair>> traEv;
	private Dialog<Pair<String, String>> dialog;
	private Dataset ds;
	
	public GuiEventHandler(){
		ds =new Dataset();
	}
	
	@FXML
	 private void handleRunButtonAction(ActionEvent event) {
		String path = this.path.getAbsolutePath();
		TransitionsParser trParser = new TransitionsParser(path);
		trParser.parse();	
		ArrayList<TableHistorySequence> tablesHistory = trParser.getTablesHistory();
		patternMetric.clear();
		tablePatternView.getItems().clear();
		ds.setNumOfActiveCells(trParser.getActiveCells());
	    Double minSup;
		if(minSupport.getText().isEmpty()){
			minSup = 0.1;
	    }
	    else{
	    	minSup = Double.parseDouble(minSupport.getText());
	    }
		AprioriSequenceParameters param = new AprioriSequenceParameters("Apriori", minSup,ds);
	    if(supportType.getValue().equals("COBJ")){
	    	param.setSupportType(SupportType.COBJ);
	    }
	    else{
	    	param.setSupportType(SupportType.CDIST);
	    }
	    algo = new AprioriSequenceAlgo(param, tablesHistory);
	    
	    long startTime = System.nanoTime();
	    algo.run();
	    long stopTime = System.nanoTime();
	    //Print algotithm execution time in sec
	    double seconds =  (double)(stopTime - startTime) / 1000000000.0;
	    System.out.println("Algorithm execution took: " + seconds);
	    
		setPatterns(algo.getPatterns());
		for(Pattern pattern:this.getPatterns()){
			String patter ="<";
			for(Subsequence subSeq: pattern.getSubsequences()){
				patter += "{";
				for(int i=0; i < subSeq.getAtomicChangeEvents().size();i++){
					if(i == subSeq.getAtomicChangeEvents().size()-1)
						patter += subSeq.getAtomicChangeEvents().get(i).toString();
					else
						patter += subSeq.getAtomicChangeEvents().get(i).toString()+",";
				}
				patter += "}";
			}
			patter += ">";
			PatternMetrics pm = new PatternMetrics(patter, Double.toString(pattern.getMetrics().getSupport()), Integer.toString(pattern.getMetrics().getNumOccurences()));
			patternMetric.add(pm);
		}
		pattern.setCellValueFactory(new PropertyValueFactory<PatternMetrics, String>("pattern"));
		pattern.setSortable(false);
		support.setCellValueFactory(new PropertyValueFactory<PatternMetrics, String>("support"));
		support.setSortable(false);
		occurences.setCellValueFactory(new PropertyValueFactory<PatternMetrics, String>("occurences"));
		occurences.setSortable(false);
		tablePatternView.getItems().setAll(patternMetric);
		supportCType.setText("Support type: " + supportType.getValue());  	
	 }
	
	@FXML
	private void initialize() {
		traEv = new ArrayList<ArrayList<TransitionSubsequencePair>>();
	}
	
	@FXML
    public void handleMouseClick(final MouseEvent event) {
			
       int index = tablePatternView.getSelectionModel().getSelectedIndex();
        if(selectedPattern > -1 && selectedPattern != index && index > -1){
        	ArrayList<Pattern> patterns = getPatterns();
        	selectedPattern = index;
        	traEv.clear();
	        transitionss = new ArrayList<TransitionSubsequencePair>();
	        for(int i=0;i<patterns.get(selectedPattern).getSubsequences().size();i++){
	            ArrayList<TransitionSubsequencePair> tmp = new ArrayList<TransitionSubsequencePair>();
	            for(TableHistoryElements the:patterns.get(selectedPattern).getSubsequences().get(i).getTableSequence()){
					TransitionSubsequencePair pair = new TransitionSubsequencePair(Integer.toString(the.getTransitionInfo().getId()),patterns.get(selectedPattern).getSubsequences().get(i).toString()
								,the.getTableSequence().getTableInfo().getName());
					transitionss.add(pair);
					subsequencesInfo.add(pair);
					tmp.add(pair);
				}
				traEv.add(tmp);
			} 
			ArrayList<String> tables = new ArrayList<String>();
			for(int i=0;i<patterns.get(selectedPattern).getSubsequences().size();i++){
				for(TableHistoryElements the:patterns.get(selectedPattern).getSubsequences().get(i).getTableSequence()){
					if(!tables.contains(the.getTableSequence().getTableInfo().getName()))
						tables.add(the.getTableSequence().getTableInfo().getName());
				}
			}
			ObservableList<String> ob = FXCollections.observableList(tables);
			this.tables.setItems(ob);
			eventsTableView.getItems().clear();
			eventsTableView.getColumns().clear();
        }
     }


	
	@FXML
	private void handleFileAction(final ActionEvent event){
	    FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		File file = fileChooser.showOpenDialog(stage);
		if(file != null){
			setPath(file);
			fileLoaded.setText("File loaded: " + file.getAbsolutePath());
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Successful load");
	        alert.setHeaderText("Success!");
	        alert.setContentText("The file was loaded successfully!!");
	        alert.showAndWait();
		}
		else{
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Unsuccessful load");
	        alert.setHeaderText("Error!");
	        alert.setContentText("The file was not loaded successfully!!");
	        alert.showAndWait();
		}
		
	}
	

	@FXML
	private void loadini(){
		 FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Stats File");
			File file = fileChooser.showOpenDialog(stage);
			if(file != null){
				//setPath(file);
				ini.setText(file.getAbsolutePath().toString().trim());
				Alert alert = new Alert(AlertType.INFORMATION);
		        alert.setTitle("Successful load");
		        alert.setHeaderText("Success!");
		        alert.setContentText("The file was loaded successfully!!");
		        alert.showAndWait();
		        BufferedReader in;
				try {
					in = new BufferedReader(new FileReader(file.getAbsolutePath()));
					String line;
					line = in.readLine();
					int numberOfTables = 0;
					while((line = in.readLine()) != null){
						numberOfTables++;
						String[] row = line.split(";");
						if(Integer.parseInt(row[4]) > 0)
							ds.setTableChanges(row[0],1);
						else
							ds.setTableChanges(row[0],0);
					}
					ds.setNumOfTables(numberOfTables);
					in.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else{
				Alert alert = new Alert(AlertType.INFORMATION);
		        alert.setTitle("Unsuccessful load");
		        alert.setHeaderText("Error!");
		        alert.setContentText("The file was not loaded successfully!!");
		        alert.showAndWait();
			}	
	}
	
	
	@FXML
	public void handleEventsClick(){
		String tableName = (String) tables.getSelectionModel().getSelectedItem();
		ObservableList<ObservableList<String>> transitionEvents = FXCollections.observableArrayList();
		eventsTableView.getItems().clear();
		eventsTableView.getColumns().clear();
		int k=0;
		for(int i = 0; i < traEv.size();i++){
			double colPercentage = 1.0 / (double)traEv.size();
			TableColumn tc = new TableColumn("TrEv:" + i);
			tc.setPrefWidth(colPercentage);
		    final int j = i;
		    tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
		    	public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
		    		return new SimpleStringProperty(param.getValue().get(j).toString());                        
		        }                    
		    });
		    tc.setSortable(false);
				eventsTableView.getColumns().addAll(tc);
				k=i;
		}
		//int size = traEv.get(0).size();

		int mysize = traEv.get(0).size();
		
		
		for(int l = 0; l < mysize;l++){
			ObservableList<String> row = FXCollections.observableArrayList();
			for(int i = 0; i < traEv.size();i++){
				ArrayList<TransitionSubsequencePair> tep = traEv.get(i); 
				String subsequence = "";
				if(tep.get(l).getTable().equals(tableName)){
					subsequence += tep.get(l).getTransition() + ":" + tep.get(l).getSubsequence();// + ",";
					row.add(subsequence);
					//transitionEvents.add(row);
				}
			}
			if(!row.isEmpty()){
				transitionEvents.add(row);
			}
		}
		
		
		
		//for(int l = 0; l < size;l++){
			
		/*	for(int i = 0; i < traEv.size();i++){
				for(int l = 0; l < traEv.get(i).size();l++){//size
				ArrayList<TransitionSubsequencePair> tep = traEv.get(i); 
					String subsequence = "";
					System.out.println("-------\n t: " + tep.get(l).getTable());
					if(tep.get(l).getTable().equals(tableName)){
						System.out.println("Subseq:" + tep.get(l).getSubsequence() + "| " + tep.get(l).getTable());
						subsequence += tep.get(l).getTransition() + ":" + tep.get(l).getSubsequence();// + ",";
						row.add(subsequence);
					}
				}
				for(int p=0;p< row.size();p++){
					System.out.println("row: " + row.get(p));
				}
				//row = FXCollections.observableArrayList();
			}*/
			
			
			
		/*	ObservableList<String> ob; 
			if(!row.isEmpty()){
				System.out.println("Laala row: " + row);
				transitionEvents.addAll(row);
				ob = FXCollections.observableList(row);
				
			}*/
		//}
		eventsTableView.setItems(transitionEvents);
	}

	@FXML
	public void openFile(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
	}
	
	@FXML
	public void exportPatterns(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export Patterns");
		//Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        File savedFile = fileChooser.showSaveDialog(stage);
        if(savedFile != null){
        	outputPath =  savedFile.getAbsolutePath();
    		writePatterns(savedFile);
		}
		else{
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Unsuccessful load");
	        alert.setHeaderText("Error!");
	        alert.setContentText("The file was not exported successfully!!");
	        alert.showAndWait();
		}
	}
	
	@FXML
	public void exit(){
		Platform.exit();
	}
	
	@FXML
	public void showAbout(){
		
   
        final Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(stage);
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/about.fxml"));
		GuiEventHandler handler = new GuiEventHandler();
		
		
		Pane mainPane;
		try {
			mainPane = loader.load();
			Scene scene = new Scene(mainPane);
			dialog.setTitle("About");
			 dialog.setScene(scene);
			 dialog.setResizable(false);
		        dialog.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}
	
	private void writePatterns(File savedfile){
		ArrayList<Pattern> patterns = this.getPatterns();
		if(patterns == null){
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Unsuccessful load");
	        alert.setHeaderText("Error!");
	        alert.setContentText("The are no patterns to be extracted!!!");
	        alert.showAndWait();
		}
		else{	
			try {
				
				PrintWriter writer = new PrintWriter(savedfile.getAbsolutePath().replace(".csv", "") + "_" +minSupport.getText() + "_" + supportType.getValue() + ".csv", "UTF-8");
				writer.println("Pattern;Support;Occurences");
				for(Pattern p: patterns){
					DecimalFormat numberFormat = new DecimalFormat("#.0000");
					writer.println(p.toString() + ";" + numberFormat.format(p.getMetrics().getSupport()) + ";" + p.getMetrics().getNumOccurences()); 
				}
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Successful save");
	        alert.setHeaderText("Success!");
	        alert.setContentText("The file was exported successfully!!");
	        alert.showAndWait();
		}
	}
	
	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public File getPath() {
		return path;
	}

	public void setPath(File path) {
		this.path = path;
	}
	public ObservableList<TransitionSubsequencePair> getPersonData() {
		return subsequencesInfo;
	}

	public void setPersonData(ObservableList<TransitionSubsequencePair> personData) {
		this.subsequencesInfo = personData;
	}
	public ArrayList<Pattern> getPatterns() {
		return patterns;
	}
	public void setPatterns(ArrayList<Pattern> patterns) {
		this.patterns = patterns;
	}
}
