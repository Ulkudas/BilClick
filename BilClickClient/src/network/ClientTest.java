package network;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import question.NetworkQuestion;

public class ClientTest {
	
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Scanner in = new Scanner(System.in);
		
		System.out.println("enter ip:");
		SessionClient testClient = new SessionClient(in.nextLine());
		testClient.start();
		
		NetworkQuestion test = null;
		while(test == null)
			test=testClient.getCurrentQuestion();
		
		System.out.println(test);
		
		System.out.println("enter answer index:");
		testClient.sendAnswer(Integer.parseInt(in.nextLine()));
		
	}
}
