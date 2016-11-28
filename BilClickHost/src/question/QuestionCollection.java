package question;
/**
 * @author Jelibon: Ata Gun
 * */
import java.util.ArrayList;
import java.util.Iterator;

public class QuestionCollection implements Iterable<Question> {
	
	ArrayList<Question> questions;
	
	public QuestionCollection(ArrayList<Question> questions) {
		this.questions= questions;
	}
	
	public QuestionCollection() {
		questions = new ArrayList<Question>();
	}
	
	public void addQuestion(Question q){
		questions.add(q);
	}
	
	public void removeQuestion(int index){
		questions.remove(index);
	}
	
	public Question getQuestion(int index){
		return questions.get(index);
	}
	
	public ArrayList<String> getDifferentCategories(){	//TODO test this , might not work
		ArrayList<String> ret = new ArrayList<String>();
		for(Question cycler:this){
			String category = cycler.getCategory();
			if(!ret.contains(category))
				ret.add(category);
		}
		return ret;
	}
	
	@Override
	public Iterator<Question> iterator() {
		return questions.iterator();
	}

	public ArrayList<Question> getQuestions(){return questions;}

}
