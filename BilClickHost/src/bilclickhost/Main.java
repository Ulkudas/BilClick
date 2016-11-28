package bilclickhost;

import db.DataLaunch;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;

import static bilclickhost.FolderViewer.goBackButton;
import static bilclickhost.SelectQuestions.refreshButton;


/**
 * @author Jelibon : Basri
 *
 * Interface of the Bil-Click host desktop program
 */

public class Main extends Application
{
	static TabPane menu;
	Stage window;
	static Tab newSession;
	static Tab questionEditor;
	static Tab questionAdder;
	CurrentSession session;
	
	public static void main(String[] args)
	{
		try {
			DataLaunch.main(args);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception
	{
		window = primaryStage;

		window.setTitle("Bil-Click");
		window.setOnCloseRequest( e -> System.exit(0) );
		// creting a border pane for the
		BorderPane borderPane = new BorderPane();
		Scene scene = new Scene(borderPane,800,600);
		session = new CurrentSession();
				
		// creating a tab pane and tabs, making them unclosable and adding them to the pane
		menu = new TabPane();

		newSession = new Tab("New Session");
		questionEditor = new Tab("Question Editor");
		questionAdder = new Tab("Question Adder");

		newSession.setClosable(false);
		questionEditor.setClosable(false);
		questionAdder.setClosable(false);

		menu.getTabs().add(newSession);
		menu.getTabs().add(questionEditor);

		menu.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<Tab>() {
					@Override
					public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
						if(questionAdder.getContent() != null)
							refreshButton.fire();
							FolderViewer.refreshButton.fire();
					}
				}
		);

		// initializing tab pane content
		newSession.setContent(session.displayNewSession());

		FolderViewer fView = new FolderViewer() ;
		questionEditor.setContent(fView.displayFolderView());

		borderPane.setCenter(menu);
		borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());
        
		window.setScene(scene);
		window.show();
	}
	
	public static void setTabText(String newText){
		newSession.setText(newText);
	}

	public static void changeTabContent(BorderPane pane){
		newSession.setContent(pane);
	}

	public static void setQuestionTabContent(VBox pane) {
		questionEditor.setContent(pane);
	}

	public static void setQuestionAdderTabContent(VBox pane){
		questionAdder.setContent(pane);
	}

	public static void addQuestionTab(){
		menu.getTabs().add(2, questionAdder);
	}
}
