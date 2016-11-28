
package bilclickhost;

/**
 * @author Jelibon : Enes
 **/

import db.*;
import question.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.stage.Stage ;
import javafx.scene.Scene ;
import javafx.scene.text.Font ;
import java.sql.SQLException;
import java.util.ArrayList ;

public class FolderViewer
{
    private ArrayList<Button> folders;
    private ArrayList<CheckBox> checkboxes;
    private Image folderImage ;
    private GridPane folderGrid ;
    private Button removeFolderButton;
    private Button addNewFolderButton;
    public static ListView<Question> list ;
    private ArrayList<QuestionFolder> allFolders ;
    static Button goBackButton;
    static Button refreshButton;
    /**
     * Displays Folder Screen and lets user to inspect files
     *
     * @return Folder Screen Layout
     **/

    public VBox displayFolderView() throws SQLException
    {
        // label on the top

        refreshButton = new Button("refresh");
        refreshButton.setVisible(false);

        refreshButton.setOnAction( we ->
        {
            try
            {
                Main.setQuestionTabContent(displayFolderView());
            }catch (SQLException e){}
        });

        Label title = new Label("Questions");
        title.setFont((new Font ("Monospaced 13", 20)));

        folders = new ArrayList<Button>();
        checkboxes = new ArrayList<>();
        folderImage = new Image(getClass().getResourceAsStream("folderimage.png"));
        allFolders = QuestionClassifier.categorizeAllQuestions();

        folderGrid = new GridPane();
        folderGrid.setVgap(5);
        folderGrid.setHgap(5);
        folderGrid.setAlignment(Pos.CENTER);

        for ( int i = 0 ; i < allFolders.size(); i++)
        {
            Button tempB = new Button(allFolders.get(i).getFolderName(), new ImageView(folderImage));
            tempB.setAlignment(Pos.BOTTOM_CENTER);
            tempB.setOnAction( InitializingFolderButtons ->
            {
                for ( int k = 0 ; k < folders.size(); ++k)
                   if ( tempB == folders.get(k))
                   {
                    try {
                        Main.setQuestionTabContent(displayFolderContents(k));
                        } catch (SQLException e) {}
                   }
            });

            folders.add(tempB);
            checkboxes.add(new CheckBox());

            HBox gridContent = new HBox();
            gridContent.setAlignment(Pos.CENTER);
            gridContent.getChildren().addAll(checkboxes.get(i), folders.get(i));

            folderGrid.add(gridContent, i%3, i/3);
        }

        removeFolderButton = new Button("Remove Folder");
        removeFolderButton.setOnAction( RemoveSelectedFolders ->
        {
            for ( int k = 0 ; k < checkboxes.size(); )
                if ( checkboxes.get(k).isSelected() )
                {
                    folderGrid.getChildren().remove(k);
                    folders.remove(k);
                    checkboxes.remove(k);

                    for ( Question it: allFolders.get(k))
                    {
                        try
                        {
                            QuestionManager.deleteQuestion(it.getQuestionCode());
                        }catch (SQLException e){}
                        //allFolders.get(k).removeQuestion();
                    }
                    allFolders.remove(k);

                }
                else k++;
            refreshButton.fire();
        });


        /**
         *  Creating an Add Folder Popup
         */
        addNewFolderButton = new Button("Add New Folder");
        addNewFolderButton.setOnAction( CreatingFolderPopup ->
        {
            Stage window = new Stage();
            TextField folderNameField = new TextField();
            folderNameField.setMaxWidth(100);
            Button okButton = new Button("ok");
            Label enterNameLabel = new Label("Please Enter Name of the Folder");

            okButton.setOnAction( CreateCorrespondingFolder ->
            {

                folders.add(new Button(folderNameField.getText(),new ImageView(folderImage)));
                folders.get(folders.size()-1).setAlignment( Pos.BOTTOM_CENTER );
                folders.get(folders.size()-1).setOnAction( InitializingThisFoldersButton ->
                {
                    try
                    {
                        Main.setQuestionTabContent(displayFolderContents(folders.size()-1));
                    }catch (SQLException e){}
                });

                checkboxes.add(new CheckBox());

                HBox gridContent = new HBox();
                gridContent.setAlignment(Pos.CENTER);
                gridContent.getChildren().addAll(checkboxes.get(checkboxes.size()-1), folders.get(folders.size()-1));

                folderGrid.add(gridContent,(folders.size()-1)%3,(folders.size()-1)/3);

                QuestionFolder newFolder = new QuestionFolder(folderNameField.getText(), new ArrayList<Question>());

                allFolders.add(newFolder);

                window.close();
            });

            VBox newFolderLayout = new VBox(10);
            newFolderLayout.setAlignment(Pos.CENTER);
            newFolderLayout.getChildren().addAll(enterNameLabel,folderNameField, okButton);

            Scene scene = new Scene(newFolderLayout);

            window.setScene(scene);
            window.showAndWait();
        });

        HBox buttonHolder = new HBox(5);
        buttonHolder.setAlignment(Pos.CENTER);
        buttonHolder.getChildren().addAll(addNewFolderButton, removeFolderButton );

        VBox mainLayout = new VBox(40);
        mainLayout.getChildren().addAll(refreshButton,title,folderGrid,buttonHolder);
        mainLayout.setAlignment(Pos.CENTER);

        return mainLayout ;
    }

    /**
     *  Displays inside of chosen Folder
     *
     *  @return Folder Contents Layout
     **/

    public VBox displayFolderContents( int index ) throws SQLException
    {
        list = new ListView<Question>();

        QuestionFolder currentFolder = allFolders.get(index);
        for ( Question it : currentFolder )
        {
            list.getItems().add( it );
        }

        Button addQuestionButton = new Button("Add a Question");
        Button removeQuestionButton = new Button("Remove Question");
        Button editQuestionButton = new Button("Edit Question");

        removeQuestionButton.setOnAction( removeTheQuestion ->
        {
            Question temp = list.getSelectionModel().getSelectedItem();
            try
            {
                QuestionManager.deleteQuestion(temp.getQuestionCode());
                QuestionManager.updateCollection();
            }catch (SQLException e){}

            list.getItems().remove(temp);
        });

        editQuestionButton.setOnAction( editTheQuestion ->
        {
            try {
                if (QuestionViewer.editQuestion(list.getSelectionModel().getSelectedItem(), true)){
                }

            }catch (SQLException e){}
        });

        addQuestionButton.setOnAction( addAQuestion ->
        {
            try
            {
                list.getItems().add(QuestionViewer.addQuestion(folders.get(index).getText()));
            }catch (SQLException e){}
        });

        goBackButton = new Button("Prev Screen");
        goBackButton.setOnAction( GoBackToPreviousScreen ->
        {
            try
            {
                Main.setQuestionTabContent(displayFolderView());
            }catch (SQLException e){}
        });

        HBox modifyButtonsHolder = new HBox(5);
        modifyButtonsHolder.getChildren().addAll(addQuestionButton,removeQuestionButton,editQuestionButton);
        modifyButtonsHolder.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(goBackButton,list, modifyButtonsHolder);

        return mainLayout ;
    }
}
