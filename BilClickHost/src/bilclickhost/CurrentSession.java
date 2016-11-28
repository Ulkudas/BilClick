package bilclickhost;

/**
 * Session creation window
 *
 * @author Jelibon : Basri
 *
 */
import db.QuestionManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import network.SessionServer;
import question.* ;
import java.sql.SQLException ;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import question.history.Entry;


public class CurrentSession
{
	//TODO change BasicQuestions to Question objects and I need a toString method that returns only the question texts.
	private ListView<BasicQuestion> questionTextList = new ListView<BasicQuestion>();
	static Button addSelectedQuestions;
	private ObservableList<NetworkQuestion> queue = FXCollections.observableArrayList();
	private TextField qTime;
	TableView<NetworkQuestion> queueTable;
	SelectQuestions selectQView;
	Label ipLabel;
	String ip;
	int time, i, queTime;
    static Timeline timeline;
    NetworkQuestion tempQuestion;


    /**
	 * Creating the properties of the new session tab
	 *
     * @return the new session layout
	 */
	public BorderPane displayNewSession()
	{
		System.setProperty("java.net.preferIPv4Stack" , "true");
		BorderPane cSession = new BorderPane();

		// setting the login screen
		VBox vbox = new VBox(30);
		Label yourIpLabel = new Label("Your IP");
		yourIpLabel.setAlignment(Pos.CENTER);
		ip = null;
		try{
			ip = java.net.InetAddress.getLocalHost().getHostAddress();
		}catch(Exception e){ e.printStackTrace();}
		ipLabel = new Label(ip);
		Button startSession = new Button("Start Session");
		
		// TODO if there is a Session object create it here, Starting a session
		startSession.setOnAction(e ->
		{

			Main.setTabText("Current Session");
			Main.changeTabContent(displayCurrentSession());

			selectQView = new SelectQuestions();
			try
			{
				Main.setQuestionAdderTabContent(selectQView.displaySessionFolderView());
				Main.addQuestionTab();
			}catch(SQLException ex){}

			try {
				SessionServer.startServer();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		});



		//adding the components to vbox
		vbox.getChildren().addAll( yourIpLabel, ipLabel, startSession);
		vbox.setAlignment(Pos.CENTER);
			 
		//adding the vbox to border pane
		cSession.setCenter(vbox);			 
		return cSession;
	}
	
	/**
	 * Creates the current session tab layout
	 *
     *  @return the current session layout
	 */
	public BorderPane displayCurrentSession()
	{
		BorderPane cSession = new BorderPane();
		
		
		// TODO discuss about implementing multiple selection
		
		// adding to queue through a button
		Button addToQueue = new Button("Add to Queue");
		addToQueue.setOnAction(e -> addToQueue());


		// Duration label and text field
		Label duration = new Label("Enter the Duration(seconds)");
	    qTime = new TextField();
		Label emptyLabel = new Label();

		// Adding the selected questions button
		addSelectedQuestions = new Button();
		addSelectedQuestions.setOnAction( addQuestionsToTheList ->
		{
			try
			{
				for (Question it : getQuestions())
					questionTextList.getItems().add(it);

			}catch(SQLException exc){}
			selectQView.resetSelectedQuestions();
		});
		addSelectedQuestions.setVisible(false);



		// creating new vbox for queue button and timer label and text field
		VBox qButtonAndLabel = new VBox(10);
		qButtonAndLabel.getChildren().addAll( duration, qTime, emptyLabel, addSelectedQuestions, addToQueue);
		qButtonAndLabel.setAlignment(Pos.CENTER_LEFT);
		
		//List view of questions
		FlowPane listAndButtons = new FlowPane();
		listAndButtons.setVgap(7);
		listAndButtons.setHgap(7);


		questionTextList.setMaxHeight(250);

		listAndButtons.setAlignment(Pos.CENTER);
		listAndButtons.getChildren().addAll(questionTextList, qButtonAndLabel);

		// Start Q Button
		Button startQueueButton = new Button("Start Queue");
        startQueueButton.setOnAction( e -> startQueue());
		startQueueButton.setAlignment(Pos.CENTER_LEFT);

		// Show History Button
		Button showHistory = new Button("Show History");
		showHistory.setOnAction( e ->
		{
			HistoryGraphAndTable.histGraphAndTable(queueTable.getSelectionModel().getSelectedItem());
		});
		// Reset Queue Button
		Button resetQueueButton = new Button("Reset Queue");
		resetQueueButton.setOnAction( e -> queueTable.getItems().clear());

		//Hbox for buttons in center
		HBox centerButtons = new HBox(50);
		centerButtons.getChildren().addAll( startQueueButton, resetQueueButton, showHistory);
		centerButtons.setAlignment(Pos.CENTER);
		centerButtons.setPadding(new Insets( 5, 5, 5, 5));

        BorderPane bPane = new BorderPane();
		HBox ipLabelBox = new HBox();
		ipLabelBox.setAlignment(Pos.CENTER);
		ipLabelBox.setPadding(new Insets( 5, 5, 5, 5));
		ipLabel.setText("Your IP :" + ip);
		ipLabelBox.getChildren().add(ipLabel);
		bPane.setTop(ipLabelBox);
		bPane.setBottom(centerButtons);
		bPane.setCenter(listAndButtons);

		cSession.setCenter(bPane);
		
		//TODO add columns for time,graph,queue,correctness,questionID ???
		queueTable = new TableView<>();
		queueTable.setMaxHeight(200);
		// Question Text Column
		TableColumn<NetworkQuestion, String> qTextColumn = new TableColumn("Question");
		qTextColumn.setMinWidth(600);
		qTextColumn.setCellValueFactory(new PropertyValueFactory<>("questionText"));

		// Choice Number Column
		TableColumn<NetworkQuestion, String> choiceNumColumn = new TableColumn("Choice Amount");
		choiceNumColumn.setMinWidth(100);
		choiceNumColumn.setCellValueFactory(new PropertyValueFactory<>("choiceNum"));

		// Time Column
		TableColumn<NetworkQuestion, String> timeColumn = new TableColumn("Duration");
		timeColumn.setMinWidth(100);
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

		queueTable.setItems(queue);
		queueTable.getColumns().addAll(qTextColumn , choiceNumColumn, timeColumn);
		
	    cSession.setBottom(queueTable);

		return cSession;
	}

	/**
	 * Getting selected questions for this session
	 *
	 * @return selected questions
	 */
	public QuestionCollection getQuestions() throws SQLException
	{
		return selectQView.getSelectedQuestions();
	}


    /**
     * Adding questions to queue
     * */
	public void addToQueue()
	{
		if (!qTime.getText().isEmpty()) {
			time = Integer.parseInt(qTime.getText());
			NetworkQuestion temp = new NetworkQuestion((Question) questionTextList.getSelectionModel().getSelectedItem(), time);
			if (temp != null) {
				queue.add(temp);
				questionTextList.getItems().remove(questionTextList.getSelectionModel().getSelectedItem());
			}
		}
		else
			ConfirmBox.display("Error", "Please enter a duration !");
	}

	public void startQueue()
	{
        int sum = 1 ;

        timeline = new Timeline();
        queueTable.getSelectionModel().select(0);

        for ( int k = 0 ; k < queueTable.getItems().size(); ++k )
        {
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(sum), e ->
            {
                tempQuestion = queueTable.getSelectionModel().getSelectedItem();
                int temp = queueTable.getSelectionModel().getSelectedIndex();
                SessionServer.sendQuestionToAll(queueTable.getSelectionModel().getSelectedItem());
                queueTable.getSelectionModel().select(temp+1);
            }));

            sum += queueTable.getItems().get(k).getTime();

            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(sum), e-> {
				 Entry tempEntry = SessionServer.getEntryAndReset();
                 tempQuestion.addEntry(tempEntry);

				try {
					Question dbQ = QuestionManager.getQuestion(tempQuestion.getQuestionCode());
					dbQ.addEntry(tempEntry);
                    //QuestionManager.saveQuestion((Question)tempQuestion);
					QuestionManager.saveQuestion(dbQ);
					QuestionManager.updateCollection();
                } catch (Exception e2){e2.printStackTrace();}
            }));

            sum += 1;

        }
        timeline.play();

    }

}