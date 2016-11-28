package network;

import question.NetworkQuestion;
import question.Question;

public class ServerTest {

	public static void main(String[] args) {
		NetworkQuestion test = new NetworkQuestion(new Question("Test", "This is a test Question", 0, "this is the right answer","this is the wrong answer","this is another wrong answer"),90);
		
		try {
			SessionServer.startServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while(SessionServer.clients.size()<1){
			System.out.println("waiting for client");
		}
		
		//long wait = System.currentTimeMillis();
		//while(System.currentTimeMillis()-wait<1000){
		//	System.out.println(System.currentTimeMillis()-wait);
		//}
		
		//while(true)
		SessionServer.sendQuestionToAll(test);
		
		long start = System.currentTimeMillis();
		while(System.currentTimeMillis()-start<15000){
		//	System.out.println(System.currentTimeMillis()-start);
		}
		
		System.out.println(SessionServer.getEntryAndReset());
		
	}
}
