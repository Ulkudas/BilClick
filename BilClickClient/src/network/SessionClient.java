package network;

import java.net.*;
import question.NetworkQuestion;
import java.io.*;

public class SessionClient extends Thread{
	static final int PORT = 25565;
	ObjectInputStream in;
	PrintWriter out;
	Socket socket;
	private NetworkQuestion currentQuestion;
	
	public SessionClient(String serverAdress) throws UnknownHostException, IOException {
		socket = new Socket(serverAdress, PORT);
		in = new ObjectInputStream(socket.getInputStream());
		out = new PrintWriter(socket.getOutputStream(),true);
	}
	
	public NetworkQuestion getCurrentQuestion(){
		NetworkQuestion ret = currentQuestion;
		currentQuestion = null;
		return ret;
		//return currentQuestion;
	}
	
	public void sendAnswer(int answerIndex){
			out.println(answerIndex);
	}
	
	@Override
	public void run() {
		while(true){
			try {
				NetworkQuestion q=null;
				q = (NetworkQuestion) in.readObject();
				if(q!=null){
					currentQuestion = q;
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	
}
