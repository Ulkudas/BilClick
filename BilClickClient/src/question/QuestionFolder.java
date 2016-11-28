package question;

import java.util.ArrayList;

public class QuestionFolder extends QuestionCollection{
	String folderCatagory;
	
	public QuestionFolder(String folderCatagory,ArrayList<Question> questions) {
		super();
		this.folderCatagory = folderCatagory;
		
		for(int i = 0;i<questions.size();i++)
			if(questions.get(i).getCategory().equals(folderCatagory))
				this.addQuestion(questions.get(i));
	}
	
	public String getFolderName(){
		return folderCatagory;
	}
	

}
