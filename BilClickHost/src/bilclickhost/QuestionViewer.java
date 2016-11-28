package bilclickhost;
import java.sql.SQLException;
import java.util.ArrayList;

import db.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import question.* ;

/**
 * @author Hasan Basri Dulgeroglu, Ata Gun Ogun, Mehmet Enes Keles, Aydemir Mirzayev, Ecem Ilgun
 * 
 */
public class QuestionViewer {
	
	//variables
	static boolean isEdited;
	static ArrayList<RadioButton> choiceButtons;
	static ArrayList<String> choiceNames;
	static ArrayList<TextField> choiceTexts;
	static ArrayList<Button> deleteButtons;
	static ToggleGroup choiceButtonsGroup;
	static TextField questionTextField;
	static Label questionLabel;
	static Button addChoiceButton;
	static Question question1;
	static VBox questionViewerVbox;
	static Button saveButton;
	static Button historyButton;

	public static Question addQuestion(String category) throws SQLException
	{

		Question tempQuestion = new Question(category,"..." , 0 , "", "");
		editQuestion(tempQuestion,false);
		isEdited = true;
		return tempQuestion;
	}
	
	public static boolean editQuestion( Question question, boolean editOrAdd) throws SQLException
	{
		isEdited = false;
		question1 = question;
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setMinWidth(800);
		window.setMinHeight(700);
		BorderPane questionViewer = new BorderPane();
		questionViewerVbox = new VBox(6); 
		
		Question blankQuestion =  new Question("category" ,"..." , 0 , "", "");
		
		// ArrayList for choice names
		choiceNames = new ArrayList<>();
		choiceNames.add("A");
		choiceNames.add("B");
		choiceNames.add("C");
		choiceNames.add("D");
		choiceNames.add("E");
		choiceNames.add("F");
        
		// ArrayList and ToggleGroup for choice radio buttons 
		choiceButtonsGroup = new ToggleGroup();
		choiceButtons = new ArrayList<>();
		for ( int i = 0; i < BasicQuestion.MAX_AMOUNT_OF_CHOICES; i++){
			RadioButton temp = new RadioButton(choiceNames.get(i));
			choiceButtons.add(temp);
			if(question.getAnswerIndex() == i){
				choiceButtons.get(i).setSelected(true);
			}
			temp.setToggleGroup(choiceButtonsGroup);
		}
		
	    // Choice texts 
		choiceTexts = new ArrayList<>();
		for ( int i = 0; i < question.getChoiceNum(); i++)
		{
			TextField temp = new TextField(question.getChoice(i));
			temp.setMinWidth(350);
			choiceTexts.add( temp );
		}
		
		//question label
		questionLabel = new Label("Question");
		questionLabel.setMinWidth(60);
		
		// TextField that contains question text 
		questionTextField = new TextField(question.getQuestionText());
		questionTextField.setAlignment(Pos.CENTER);
		questionTextField.setMinSize(400, 200);
		questionTextField.setMaxSize(600, 400);
		
		
		// Adding the label and textfield of question 
		questionViewerVbox.getChildren().addAll(questionLabel, questionTextField);
		
		// Delete choice buttons
		deleteButtons = new ArrayList<>();
		for(int i = 0; i < BasicQuestion.MAX_AMOUNT_OF_CHOICES; i++){
			int temp = i;
			Button delete = new Button("DLT");
			delete.setMinSize(20, 20);
			delete.setOnAction(e -> {
				deleteChoice(temp);
				renameChoices();
			});
			deleteButtons.add(delete);
		}
		
		// Adds the textfields and radio buttons of choices to the pane

		for(int i = 0; i < question.getChoiceNum(); i++){
			HBox choiceView = new HBox(6);
			choiceView.getChildren().addAll(choiceButtons.get(i), choiceTexts.get(i));
			if(question.getChoiceNum() > 2){
				choiceView.getChildren().add(deleteButtons.get(i));
			}
			choiceView.setAlignment(Pos.CENTER);
			questionViewerVbox.getChildren().add(choiceView);
		}
		
		// Add Choice Button 
		addChoiceButton = new Button("+ New Choice");
		addChoiceButton.setOnAction(e ->
		{
			addNewChoice();
			renameChoices();
		});
		addChoiceButton.setAlignment(Pos.CENTER);
		questionViewerVbox.getChildren().add(addChoiceButton);

		// History Button and vbox for save and this button
		historyButton = new Button("Show History");
		historyButton.setOnAction( e ->
		{
			HistoryGraphAndTable.histGraphAndTable(question);
		});


		// Save Button
		saveButton = new Button("Save");
		saveButton.setOnAction(e ->
		{
			saveQuestion();
			renameChoices();

			isEdited = true;

			FolderViewer.list.getItems().add(blankQuestion);
			FolderViewer.list.getItems().remove(blankQuestion);

			if ( editOrAdd)
				try
				{
					QuestionManager.setQuestion(question, question.getQuestionCode());
					QuestionManager.updateCollection();
				}catch(SQLException ee){}
			else
				try
				{
					QuestionManager.addQuestion(question);
					QuestionManager.updateCollection();
				}catch(SQLException eee1){}
			window.close();

		});
		saveButton.setAlignment(Pos.BOTTOM_RIGHT);


		HBox saveAndHistory = new HBox(5);
		saveAndHistory.getChildren().addAll(historyButton, saveButton);
		saveAndHistory.setAlignment(Pos.CENTER);

		if (editOrAdd)
			questionViewerVbox.getChildren().add(saveAndHistory);
		else
			questionViewerVbox.getChildren().add(saveButton);
		
		// Adding the VBox to the BorderPane
		questionViewerVbox.setAlignment(Pos.CENTER);
		questionViewer.setCenter(questionViewerVbox);


		//Sets up the scene and window
		Scene scene = new Scene(questionViewer);
		window.setScene(scene);
		window.showAndWait();
		return isEdited;
	}
	
	/*
	 * Repaints the whole choices so it will be updated
	 *
	 */
	public static void renameChoices()
	{
		questionViewerVbox.getChildren().clear();
		questionViewerVbox.getChildren().addAll(questionLabel, questionTextField);
		for(int i = 0; i < question1.getChoiceNum(); i++){
			HBox choiceView = new HBox(6);
			choiceView.getChildren().addAll(choiceButtons.get(i), choiceTexts.get(i));
			if(question1.getChoiceNum() > 2){
				choiceView.getChildren().add(deleteButtons.get(i));
			}
			choiceView.setAlignment(Pos.CENTER);
			questionViewerVbox.getChildren().add(choiceView);
		}
		questionViewerVbox.getChildren().add(addChoiceButton);

		if(!isEdited) {
			HBox hbox1 = new HBox(6);
			hbox1.getChildren().addAll(historyButton, saveButton);
			hbox1.setAlignment(Pos.CENTER);
			questionViewerVbox.getChildren().add(hbox1);
		}
		else
			questionViewerVbox.getChildren().add(saveButton);
	}
	
	/*
	 * Deletes a choice from question
	 */
	public static void deleteChoice(int index){
		question1.removeChoice(index);
		choiceTexts.remove(index);
	}
	
	/*
	 * Adds a new choice to question
	 */
	public static void addNewChoice(){
	    String newChoice = "...";
		question1.addChoice(newChoice);
		TextField temp = new TextField(question1.getChoice(question1.getChoiceNum() - 1));
		temp.setMinWidth(350);
		choiceTexts.add(temp);
		
	}

	/*
	 * Saves the questions edited choices, answer and text
	 */
	public static void saveQuestion(){
		question1.modifyQuestionText(questionTextField.getText());
		question1.setAnswerIndex((choiceButtons.indexOf(choiceButtonsGroup.getSelectedToggle())));
		for(int i = 0; i < question1.getChoiceNum(); i++){
			question1.modifyChoice(i, (choiceTexts.get(i).getText()));
		}
	}
}
 