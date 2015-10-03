package gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import output.Pattern;
import algorithms.Algorithm;

public class Gui extends Application {

	@FXML
	private TableView<TransitionSubsequencePair> tableView = new TableView<>(); 
	@FXML
	private MenuBar menuBar = new MenuBar();
	@FXML
	 private ListView<String> listView = new ListView<String>();
	@FXML
	 private Set<String> stringSet;
	@FXML
	 ObservableList observableList = FXCollections.observableArrayList();
	private File path;
	private Stage stage;
	private ObservableList<TransitionSubsequencePair> personData = FXCollections.observableArrayList();
	private Algorithm algo;
	private ArrayList<Pattern> patterns;
	@Override
	public void start(Stage primaryStage) throws IOException {
		
		setStage(primaryStage);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Gui.fxml"));
		GuiEventHandler handler = new GuiEventHandler();
		handler.setStage(primaryStage);
		loader.setController(handler);
		
		Pane mainPane = loader.load();
		Scene scene = new Scene(mainPane);
		primaryStage.getIcons().add(new Image("/graphics/muses-icon.png"));
		primaryStage.setTitle("MUSES - Miner of Useful Sequences in the Evolution of Schemata");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	

	public static void main(String[] args) {
		launch(args);
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
		return personData;
	}

	public void setPersonData(ObservableList<TransitionSubsequencePair> personData) {
		this.personData = personData;
	}
	public ArrayList<Pattern> getPatterns() {
		return patterns;
	}
	public void setPatterns(ArrayList<Pattern> patterns) {
		this.patterns = patterns;
	}
}
