
package bilclickhost;

/**
 * Question selection screen
 *
 * @author Jelibon : Enes
 **/

import javafx.scene.text.Font;
import question.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import java.sql.SQLException;
import java.util.ArrayList ;

public class SelectQuestions
{
    private ArrayList<Button> folders;
    private Image folderImage;
    private GridPane folderGrid;
    private ListView<Question> selectList;
    ArrayList<QuestionFolder> allFolders;
    QuestionCollection selectedQuestions;
    static Button refreshButton;
    /**
     * Displays Folder Screen and lets user to inspect files
     *
     * @return Folder Screen Layout
     **/

    public VBox displaySessionFolderView() throws SQLException
    {
        refreshButton = new Button("refresh");
        refreshButton.setVisible(false);
        refreshButton.setOnAction(we ->
        {
            try
            {
                Main.setQuestionAdderTabContent(displaySessionFolderView());
            } catch (SQLException e) {}
        });

        Label title = new Label("Questions");
        title.setFont((new Font("Monospaced 13", 20)));

        folders = new ArrayList<Button>();

        folderImage = new Image(getClass().getResourceAsStream("folderimage.png"));

        allFolders = QuestionClassifier.categorizeAllQuestions();

        folderGrid = new GridPane();
        folderGrid.setVgap(5);
        folderGrid.setHgap(5);
        folderGrid.setAlignment(Pos.CENTER);

        for ( int i = 0; i < allFolders.size(); i++)
        {
            Button tempB = new Button(allFolders.get(i).getFolderName(), new ImageView(folderImage));
            tempB.setAlignment(Pos.BOTTOM_CENTER);
            tempB.setOnAction(InitializingFolderButtons ->
            {
                for ( int k = 0; k < folders.size(); ++k)
                    if ( tempB == folders.get(k))
                    {
                        try {
                            Main.setQuestionAdderTabContent(displayQuestionSelectionList(k));
                        } catch (SQLException e) {
                        }
                    }
            });

            folders.add(tempB);

            folderGrid.add(folders.get(i), i%3, i/3);
        }

        VBox mainLayout = new VBox(40);
        mainLayout.getChildren().addAll(refreshButton, title, folderGrid);
        mainLayout.setAlignment(Pos.CENTER);

        return mainLayout;
    }

    /**
     * Displays inside of chosen Folder
     *
     * @return Folder Contents Layout
     **/

    public VBox displayQuestionSelectionList(int index) throws SQLException
    {
        selectedQuestions = new QuestionCollection();
        QuestionFolder currentFolder = allFolders.get(index);

        selectList = new ListView<Question>();
        selectList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        for (Question it : currentFolder)
            selectList.getItems().add(it);

        Button goBackButton = new Button("Prev Screen");
        goBackButton.setOnAction(GoBackToPreviousScreen ->
        {
            try
            {
                Main.setQuestionAdderTabContent(displaySessionFolderView());
            }catch (SQLException e){}

        });

        goBackButton.setVisible(false);

        Button doneButton = new Button("Add");
        doneButton.setOnAction(sendSelectedQuestions ->
        {
            for ( Question it : selectList.getSelectionModel().getSelectedItems() )
                selectedQuestions.addQuestion(it);
            CurrentSession.addSelectedQuestions.fire();
            goBackButton.fire();
        });

        // Info for selection
        Label info = new Label("Hold Ctrl to choose multiple questions.");
        info.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(20);

        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(goBackButton, info, selectList, doneButton);

        return mainLayout;
    }

    public QuestionCollection getSelectedQuestions()
    {
        return selectedQuestions;
    }
    public void resetSelectedQuestions(){ selectedQuestions = new QuestionCollection(); }
}
