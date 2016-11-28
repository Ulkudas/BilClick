package question;

import java.io.Serializable;

public class NetworkQuestion extends Question implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5268883887226850289L;
	public int time;
	
	public NetworkQuestion(Question q,int time) {
		super(q);
		this.time = time;
	}
	
	public NetworkQuestion() {
	}

	public int getTime(){
		return time;
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
}
