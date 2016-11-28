package bilclickhost;

import question.* ;
import question.history.*;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HistoryTest extends Application{
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Stage window = primaryStage;
		window.setTitle("LAMO");
		VBox vbox = new VBox();
		Question question = new Question("123","asdfmasdf",2,"asdfasdf","adsfdsf","adsfasdf","adsfasdf", "asdfklmasdf");
		
		
		ArrayList<Integer> cd = new ArrayList<>();
		cd.add(30);
		cd.add(40);
		cd.add(50);
		cd.add(20);
		cd.add(90);
		Entry entry = new Entry("24.10.2010", 350, 2, cd);
		
		ArrayList<Integer> bd = new ArrayList<>();
		bd.add(90);
		bd.add(20);
		bd.add(70);
		bd.add(40);
		bd.add(10);
		Entry entry2 = new Entry("12.06.2014", 200, 2, bd);
		
		question.addEntry(entry);
		question.addEntry(entry2);
		
		HistoryGraphAndTable.histGraphAndTable(question);
		Scene scene = new Scene(vbox, 300,300);
		window.setScene(scene);
	}
}
