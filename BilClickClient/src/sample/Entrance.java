
package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application ;
import javafx.geometry.Pos;
import javafx.scene.Scene ;
import javafx.scene.effect.*;
import javafx.stage.Stage ;
import javafx.scene.layout.VBox;
import javafx.scene.control.* ;
import javafx.scene.image.Image ;
import javafx.scene.image.ImageView ;
import javafx.scene.text.Font ;
import javafx.util.Duration;

/**-----------------------------------------------------
 *	This class displays the Entrance of BilClick- Client Part
 *
 *	@author  Jelibon: Enes
 *
 **---------------------------------------------------*/

public class Entrance extends Application
{
    // Entrance properties
    Stage window ;
    Scene mainScene ;
    Label iconHolder , bilclickText;
    Button connectButton ;
    Image logo ;
    Timeline timeline;
    int effectDensity;

    /**---------------------------------------------
     * Creates Entrance window
     *
     * @param primaryStage the window that we are using
     *---------------------------------------------*/
    public void start(Stage primaryStage)
    {
        // Using window - as a shortcut of primaryStage
        window = primaryStage ;
        window.setResizable(false);
        window.setOnCloseRequest(e -> {System.exit(0);});

        connectButton = new Button("Connect a Session");
        connectButton.setMinHeight(40);
        connectButton.setMinWidth(40);
        connectButton.setOnAction( e->
        {
            new ConnectSessionScreen(window);
        });

        bilclickText = new Label("Welcome to\n  Bil Click");
        bilclickText.setFont(new Font ("Monospaced 13", 20));

        logo = new Image(getClass().getResourceAsStream("logo.PNG"));
        iconHolder = new Label("",new ImageView(logo));

        VBox layout = new VBox(60);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(iconHolder,bilclickText,connectButton);
        
        //----------------- Blur Effect -----------------------
        effectDensity = 10;
        timeline = new Timeline( new KeyFrame(Duration.millis(120), e ->
        {
            layout.setEffect(new BoxBlur(effectDensity,effectDensity,1));
            effectDensity -- ;
        })) ;
        timeline.setCycleCount( 10);
        timeline.play() ;
        //-----------------------------------------------------
        
        mainScene = new Scene(layout,300,500);
        mainScene.getStylesheets().add("sample/Theme.css");

        window.setScene(mainScene);
        window.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
