package bilclickhost;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
/**
 * @author Jelibon : Basri
 * Creates confirm boxes 
 */
public class ConfirmBox {
	
	static boolean answer;
	public static boolean display(String title, String message){
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);
		
		Label label = new Label();
		label.setText(message);
		
		Button yesButton = new Button("Confirm");
		
		yesButton.setOnAction(e -> {
			answer = true;
			window.close();
		});
		
		VBox layout = new VBox(5);
		layout.setPadding(new Insets( 10, 10, 10, 10));
		layout.setAlignment(Pos.CENTER);
		layout.getChildren().addAll(label, yesButton);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		
		return answer;
	}
}
