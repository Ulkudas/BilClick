package question.history;

import java.util.ArrayList;
import java.util.Date;

public class Entry {
	
	int participants;
	ArrayList<Integer> choiceDistribution;
	int answerIndex;
	String dateAsked; 
	
	//TODO constructor yaz
	public Entry( String dateAsked, int participants, int answerIndex, ArrayList<Integer> choiceDistribution) {
		this.dateAsked = dateAsked;
		this.participants = participants;
		this.answerIndex = answerIndex;
		this.choiceDistribution = choiceDistribution;
	}
	
	//TODO getCorrectness tarzi metodlar ekle
	
	public double getCorrectnessPercent() {
		return (double)choiceDistribution.get(answerIndex)/participants*100;
	}
	
	public ArrayList<Integer> getChoiceDistribution(){
		return choiceDistribution;
	}
	
	public int getAnswerIndex(){
		return answerIndex;
	}
	
	public int getParticipants(){
		return participants;
	}
	
	public String getDateAsked(){
		return dateAsked;
	}
	
	@Override
	public String toString() {
		return "Date asked:"+dateAsked+" participants:"+participants+" answer index:"+answerIndex+ "choiceDist:"+choiceDistribution;
	}
	
}
