package bilclickhost;

/**
 *  Database interaction
 *
 *  @author Jelibon: Enes, Ata Gun
 */

import db.* ;
import question.* ;
import java.sql.SQLException;
import java.util.ArrayList ;

public class QuestionClassifier
{
    /**-------------------------
     *  Categorizes all questions
     *
     *  @return categorized questions
     *------------------------*/
    public static ArrayList<QuestionFolder>  categorizeAllQuestions() throws SQLException
    {
        QuestionCollection allQuestions = new QuestionCollection() ;
        allQuestions = getAllQuestions();


        for ( Question it : allQuestions)
            it.setSelected(false);

        ArrayList<QuestionFolder> folders = new ArrayList<>();

        for(String cycler : allQuestions.getDifferentCategories())
        {
            folders.add(new QuestionFolder(cycler, allQuestions.getQuestions()));
        }

        return folders ;
    }

    /**-------------------------
     *  Gets all questions from database
     *
     *  @return all questions
     *------------------------*/
    public static QuestionCollection getAllQuestions() throws SQLException
    {
        return QuestionManager.getUpdatedCollection();
    }
}
