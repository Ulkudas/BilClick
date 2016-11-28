package sample;

import network.* ;
import question.* ;
import javafx.animation.KeyFrame;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.VBox ;
import javafx.geometry.Pos ;
import javafx.scene.paint.Color;
import javafx.scene.image.Image ;
import javafx.scene.image.ImageView ;
import javafx.scene.text.Font ;
import javafx.animation.Timeline ;
import javafx.util.Duration;

/**--------------------------------------------------------
 *	This class displays the No-Question Screen and displays incoming questions
 *
 *	@author  Jelibon: Enes
 *
 **--------------------------------------------------------*/
public class QuestionScreen
{
    // Common properties
    int durationOfQuestion ;
    Stage window;

    // No Question Screen properties
    Label noQuestionLabel ;
    Image cycleImage ;
    Label cycleImageHolder ;

    // Question Displayer properties
    Label questionDurationLabel ;
    Button confirmButton;
    ListView<String> choiceList;
    TextArea questionArea;
    VBox questionLayout ;
    Timeline timeline ;
    SessionClient clientHolder ;
    
    /**-------------------------------------------
     * Creates a No Question Screen
     *
     * @param givenWindow window that we are using
     * @param curClient session client object that we are using 
     *-------------------------------------------*/
    public QuestionScreen(Stage givenWindow, SessionClient curClient)
    {
        // Globalizing givenWindow for using in questionDisplay
        window = givenWindow ;
        clientHolder = curClient ;

        Timeline timeline = new Timeline( new KeyFrame(Duration.millis(50), e -> 
        {
        	NetworkQuestion temp = curClient.getCurrentQuestion();
        	if ( temp != null)
        		questionDisplay(temp);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
        noQuestionLabel = new Label("No Questions Yet");
        noQuestionLabel.setFont((new Font ("Monospaced 13", 20)));
        noQuestionLabel.setTextFill(Color.AQUA);

        cycleImage = new Image(getClass().getResourceAsStream("waiting.gif"));
        cycleImageHolder = new Label("",new ImageView(cycleImage));

        VBox layout = new VBox(65);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(noQuestionLabel, cycleImageHolder);

        Scene scene = new Scene(layout,300,500);
        scene.getStylesheets().add("sample/Theme.css");

        window.setScene(scene) ;
    }

    /**-------------------------------------------
     * Creates a No Question Screen
     *
     * @param currentQuestion question that we are displaying
     *-------------------------------------------*/
    private void questionDisplay(NetworkQuestion currentQuestion)
    {
    	boolean isSent = false ; 
        durationOfQuestion = currentQuestion.time; //currentQuestion.getDuration();

        questionDurationLabel = new Label(durationOfQuestion + "");
        questionDurationLabel.setFont(new Font ("Monospaced 13",20 ));

        questionArea = new TextArea(currentQuestion.getQuestionText());
        questionArea.setWrapText(true);
        questionArea.setEditable(false);

        choiceList = new ListView<>();
        choiceList.setFixedCellSize(30);
        choiceList.setPrefSize(200, 300);
        choiceList.setEditable(true);

        for ( int i = 0; i < currentQuestion.getChoiceNum(); ++i)
            choiceList.getItems().add(currentQuestion.getChoice(i));


        confirmButton = new Button("Confirm");
        confirmButton.setMinWidth(26);
        confirmButton.setMinHeight(26);
        confirmButton.setOnAction( e ->
        {
            confirmButton.setDisable(true);
            clientHolder.sendAnswer(choiceList.getSelectionModel().getSelectedIndex());
            
        });

        timeline = new Timeline( new KeyFrame( Duration.seconds(1), e->
        {
            durationOfQuestion -- ;

            if ( durationOfQuestion == 0)
            {
            	if ( !confirmButton.isDisable())
            		confirmButton.fire();
                new QuestionScreen(window, clientHolder);
            }

            questionDurationLabel.setText(""+ durationOfQuestion);
        }));

        timeline.setCycleCount(durationOfQuestion);
        timeline.play();

        questionLayout = new VBox(5) ;
        questionLayout.getChildren().addAll(questionDurationLabel,questionArea, choiceList, confirmButton);
        questionLayout.setAlignment(Pos.CENTER);

        Scene questionScene = new Scene(questionLayout,300,500);
        questionScene.getStylesheets().add("sample/Theme.css");

        window.setScene(questionScene);
    }

}
