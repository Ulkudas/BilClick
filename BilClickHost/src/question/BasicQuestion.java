package question;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *	This class is used for creating and manipulating multiple-choice questions
 * 
 *	@author  Jelibon: Aydemir , Ata Gun
 */
public class BasicQuestion implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -20262162135443776L;


	public static final int MAX_AMOUNT_OF_CHOICES = 6;
	

	String questionText;
	
	int choiceNum; 
	ArrayList<String> choices;
	int answerIndex;	//index starting from 0
	


	public BasicQuestion(BasicQuestion q) {
		this.questionText = q.questionText;
		this.answerIndex = q.answerIndex;
		this.choices = q.choices;
		this.choiceNum = q.choiceNum;
	}
	
	public BasicQuestion() {
	}
	
	/**	
	 * Creates a question with given properties.
	 * 
	 * @param questionText the question itself
	 * @param answerIndex index of the right choice (starting from 0)
	 * @param choices list of choices 
	 */
	public BasicQuestion(String questionText, int answerIndex,String... choices) {
		this.questionText = questionText;
		this.answerIndex = answerIndex;
		
		this.choices = new ArrayList<String>();
		for(String aChoice:choices)
			addChoice(aChoice);
		choiceNum = this.choices.size();
	}
	
	
	//TODO add new constructor directly taking choices as a String[]
	
	/**
	 * 	Used for getting the question text.
	 * 
	 *	@return the question text 
	 */
	public String getQuestionText(){
		return questionText;
	}
	
	
	/**
	 * 	Used for getting the choice at a specific index
	 * 
	 *	@param i index of the choice you want to get 
	 *
	 *	@return the choice with given index
	 */
	public String getChoice(int i)
	{
		if(choiceNum<=i)
			return null;
		
		return choices.get(i);
	}

	public int getChoiceNum()
	{
		return choiceNum ;
	}
	
	/**
	 *	Used for getting all of the choices as an ArrayList
	 * 
	 *	@return the actual ArrayList with all the choices
	 */
	public ArrayList<String> getChoices(){
		return choices;
	}
	
	
	/**
	 * Used for getting the index of the answer
	 * 
	 *	@return the index of the answer 
	 */
	public int getAnswerIndex(){
		return answerIndex;
	}
	
	
	
	/**
	 * 	Used to modify a choice text. 
	 * 	
	 *	@param index index of the choice to be modified
	 *	@param newChoiceText desired choice text
	 *
	 *	@return whether or not the choice is modified
	 */
	public boolean modifyChoice(int index,String newChoiceText){
		if(index>=choices.size())
			return false;
		else{
			choices.set(index, newChoiceText);
			return true;
		}
	}
	
	
	/**
	 *	Used to set/change the answer of a question 
	 *
	 *	@param i index of the answer
	 *	
	 *	@return whether or not the answer is set
	 */
	public boolean setAnswerIndex(int i){
		if(i >= choices.size())
			return false;
		else{
			answerIndex = i;
			return true;
		}
	}
	
	
	/**
	 * 	Removes a choice, shifts the other choices so that there are no empty indexes
	 * 
	 *	@param i index of the choice to be removed
	 */
	public void removeChoice(int i)
	{
		choices.remove(i); 
		choiceNum -- ;
	}
	
	
	/**
	 * Used for adding new choices
	 * 
	 * @param choiceText text of the choice to be added
	 * 
	 * @return whether or not the choice is added
	 */
	public boolean addChoice(String choiceText){
		if(choices.size() >= MAX_AMOUNT_OF_CHOICES)
			return false;
		else
		{
			choices.add(choiceText);
			choiceNum ++ ;
			return true;
		}
	}
	
	
	/**
	 * Used to modify the question text
	 * 
	 *	@param newQuestionText desired question text
	 */
	public void modifyQuestionText(String newQuestionText){
		questionText = newQuestionText;
	}
	
	
	

	
}