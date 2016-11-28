package sample;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.layout.VBox ;
import javafx.geometry.Pos ;
import javafx.stage.Modality ;
import javafx.scene.text.Font ;
import network.* ;
/**--------------------------------------------------------------
 *	This class displays the Session List and allow user to connect a session
 *
 *	@author  Jelibon: Enes
 *
 *--------------------------------------------------------------*/


public class ConnectSessionScreen
{
    // Session List properties
    Stage thisStage ;
    Button connectIpButton ;
    TextField ipField ;
    Label sessionListTitle;
    SessionClient curClient; 

    /**-------------------------------------------
     * Creates a SessionList
     *
     * @param givenWindow window that we are using
     *-------------------------------------------*/
    public ConnectSessionScreen(Stage givenWindow)
    {
        // Globalizing givenWindow for using in login screen
        thisStage = givenWindow;
        
        ipField = new TextField();
        ipField.setMaxWidth(110);
        connectIpButton = new Button("Connect to IP");
        connectIpButton.setMinWidth(35);
        connectIpButton.setMinHeight(35);
        connectIpButton.setOnAction(e ->
        {
        	
           try 
           {
			curClient = new SessionClient(ipField.getText());
           }catch (Exception e1) 
           {
        	   e1.printStackTrace();
           }
           curClient.start();
           new QuestionScreen(thisStage,curClient);
        });

        sessionListTitle = new Label("Connect a Session");
        sessionListTitle.setFont(new Font ("Monospaced 13", 17));

        VBox layout = new VBox(80);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(sessionListTitle, ipField, connectIpButton);

        Scene sessionListScene = new Scene(layout, 300, 500);
        sessionListScene.getStylesheets().add("sample/Theme.css");

        thisStage.setScene(sessionListScene) ;
    }

}
