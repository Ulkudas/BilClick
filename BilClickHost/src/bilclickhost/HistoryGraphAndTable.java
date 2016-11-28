package bilclickhost;

import javafx.geometry.Insets;
import question.*;
import question.history.* ;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HistoryGraphAndTable {
	
	static Question aQuestion;
	static TableView<Entry> entryTable;
	static VBox graph;
	static BorderPane graphAndTable;
	static Button showHistory;
	static VBox vbox;
	static VBox showHistoryVBox;
	
	public static void histGraphAndTable(Question question){
		aQuestion = question;
		Stage window = new Stage();
		window.setMinHeight(600);
		window.setMinWidth(400);
		graphAndTable = new BorderPane();	
		vbox = new VBox();
	    graph = new VBox();
		ObservableList<Entry> history = FXCollections.observableArrayList(question.getHistory());
		System.err.println(question.getHistory());
		graph.setAlignment(Pos.TOP_CENTER);
		// Date Column
		TableColumn<Entry, String> dateColumn = new TableColumn<>("Date");
		dateColumn.setMinWidth(300);
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateAsked"));
		
		// Participants Column
		TableColumn<Entry, String> participantsColumn = new TableColumn<>("Participants");
		participantsColumn.setMinWidth(125);
		participantsColumn.setCellValueFactory(new PropertyValueFactory<>("participants"));
		
		// Correctness Column
		TableColumn<Entry, Double> correctnessColumn = new TableColumn<>("Correctness");
		correctnessColumn.setMinWidth(125);
		correctnessColumn.setCellValueFactory(new PropertyValueFactory<>("correctness"));

		// Correctness Column
		TableColumn<Entry, Double> answerColumn = new TableColumn<>("Answer Index");
		answerColumn.setMinWidth(50);
		answerColumn.setCellValueFactory(new PropertyValueFactory<>("answerIndex"));

		entryTable = new TableView<>();
		entryTable.getColumns().addAll(dateColumn, participantsColumn, correctnessColumn, answerColumn);
		entryTable.setMaxHeight( 250);
		entryTable.setItems(history);

		if(question.getHistory().size() > 0){
			entryTable.getSelectionModel().select(question.getHistory().size()-1);
			graph = HistoryGraph.historyGraph(entryTable.getSelectionModel().getSelectedItem());
		}


		showHistory = new Button("SHOW");
		showHistory.setOnAction(e -> {
			showHistory();
			reDraw();
		});
		showHistoryVBox = new VBox();
		showHistoryVBox.setAlignment(Pos.CENTER);
		showHistoryVBox.getChildren().add(showHistory);
		showHistoryVBox.setPadding(new Insets( 5, 5, 5, 5));
		vbox.getChildren().addAll(entryTable,showHistoryVBox);

		graphAndTable.setCenter(graph);
		graphAndTable.setBottom(vbox);

		Scene scene = new Scene(graphAndTable, 800, 600);
		window.setScene(scene);
		window.show();
	}
	public static void showHistory(){
		graph = HistoryGraph.historyGraph(entryTable.getSelectionModel().getSelectedItem());
	}
	public static void reDraw(){
		graphAndTable.getChildren().clear();
		vbox.getChildren().clear();
		vbox.getChildren().addAll(graph,entryTable);
		graphAndTable.setCenter(vbox);
		graphAndTable.setBottom(showHistoryVBox);
	}
}
