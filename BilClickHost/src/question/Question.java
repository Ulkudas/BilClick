package question;

import java.io.Serializable;
import java.util.ArrayList;

import question.history.Entry;
import question.history.History;

public class Question extends BasicQuestion implements Serializable,History,Categorizable,Selectable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6185059498727469732L;
	/** Used for holding the history of the question 
	 * @see Entry  	*/
	transient ArrayList<Entry> history;
	/** Used for categorizing the question 
	 * @see Categorizable */
	String category;
	/**	Used to determine if the question is selected or not. 
	 *	Can be used to add selected questions to a QuestionFolder or QuestionCollection */
	boolean selected;
	
	/**
	 * Used to make sure that particular question is never confusd with another one in execution time or
	 * in the database*/
	String questionCode;

	public Question(String questionCode, String category, String questionText, int answerIndex, String... choices)
	{
		super(questionText, answerIndex, choices);
		this.questionCode = questionCode;
		this.category = category;
		history = new ArrayList<Entry>();
	}
	
	public Question(String category, String questionText, int answerIndex, String... choices)
	{
		super(questionText, answerIndex, choices);
		this.questionCode = "a"+(int)(this.hashCode()*Math.random())+"a";
		this.category = category;
		history = new ArrayList<Entry>(BasicQuestion.MAX_AMOUNT_OF_CHOICES);
	}
	
	public Question() {
	}
	
	public Question(Question q) {
		super(q);
		this.questionCode=q.questionCode;
		this.category=q.category;
		this.history=q.history;
		this.selected=q.selected;
	}
	

	public String getQuestionCode()
	{
		return questionCode;
	}
	@Override
	public String getCategory()
	{
		return category;
	}

	@Override
	public void setSelected(boolean select)
	{	//TODO select adini degistirebilirsin
		this.selected = select;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}
	
	public ArrayList<Entry> getHistory() {
		return history;
	}
	
	public void addEntry(Entry newEntry){
		history.add(newEntry);

			//try {
			//	HistoryManager.questionAsked(this.getQuestionCode(), newEntry );
			//} catch (SQLException | ClassNotFoundException e) {
			//	// TODO Auto-generated catch block
			//	e.printStackTrace();
			//}
	}
	
	/**
	 * To String
	 *
	 * */

	public String toString()
	{
		String result = ""; 
		
		result += " Category: "    +getCategory()+ "\n"+
				  " QuestionText: " +getQuestionText() + "\n"+ // Give fist 10 letters og the body
				  " Answer Index: " +getAnswerIndex()+ "\n";
		
		// Add the choices
		for( String choice: super.getChoices())
			result += "* " +choice+ "\n";
		
		return result;
	}
}
