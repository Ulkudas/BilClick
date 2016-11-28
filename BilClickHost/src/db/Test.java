package db;
import question.*;

import java.sql.SQLException;

public class Test {


	public static void main(String[] args) throws SQLException 
	{
		/**
		 * Test the BilClickDatabase below  
		 */
		//QuestionManager.deleteQuestion(0);QuestionManager.deleteQuestion(0);QuestionManager.deleteQuestion(0);QuestionManager.deleteQuestion(0);
		//QuestionManager.deleteQuestion(0);QuestionManager.deleteQuestion(0);QuestionManager.deleteQuestion(0);QuestionManager.deleteQuestion(0);QuestionManager.deleteQuestion(0);QuestionManager.deleteQuestion(0);QuestionManager.deleteQuestion(0);QuestionManager.deleteQuestion(0);QuestionManager.deleteQuestion(0);
		//Question q1 = new Question( "Math", "Krt mn?", 0, "Evet","Hayir");
		//Question q2 = new Question( "Test", "This is q2", 1, "a","b","c");
		//System.out.println(q1);
		//QuestionManager.addQuestion(q1);
		
		
		//QuestionManager.addQuestion(q2);
		
		//Question q1 = QuestionManager.getUpdatedCollection().getQuestion(0);
		
		//ArrayList<Integer> choiceDistribution = new ArrayList<>();
		//choiceDistribution.add(40);
		//choiceDistribution.add(29);
		//q1.addEntry(new Entry("1990", 69,0, choiceDistribution ));
		
		//QuestionManager.addQuestion(q1);
		
		//q1.addEntry(new Entry("Test", 100, 1, choiceDistribution));
		//QuestionManager.saveQuestion(q1);
		

		
		

		//q1.addChoice("Test123");
		//QuestionManager.saveQuestion(q1);
		//QuestionManager.deleteQuestion(q2);
		
		//System.out.println(q1);
		
		//QuestionManager.deleteQuestion(0);
		
		
		
		// 1) Take the collection of questions below
		//QuestionManager.getUpdatedCollection().removeQuestion(0);

		//Question q1 = new Question( )
		
		
			//1.1 Look at what you have
		String code = "";

		for( Question q: QuestionManager.getUpdatedCollection())
		{
			code = q.getQuestionCode();
			System.out.println(q);
			System.out.println(q.getHistory());
			System.out.println("*********************");
		}


		System.out.print("Finally from the code");
		System.out.print( QuestionManager.getQuestion( code));
			//1.1 Look at 
		/**
		 * Test HistoryManager below
		 */
	}

}
