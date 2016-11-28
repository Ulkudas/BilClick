package network;

import java.net.*;
import java.util.ArrayList;
import java.util.Calendar;

import question.NetworkQuestion;
import question.history.Entry;

import java.io.*;

public class SessionServer {

	private static final int PORT = 25565;

	static ArrayList<Handler> clients = new ArrayList<>();

	static int[] choiceDistribution = new int[6];

	static NetworkQuestion questionBeingAsked;

	public static void reset() {
		choiceDistribution = new int[6];
		questionBeingAsked = null;
	}

	public static Entry getEntryAndReset() {
		ArrayList<Integer> cd = new ArrayList<>();
		for (int i=0;i<questionBeingAsked.getChoiceNum();i++)
				cd.add(choiceDistribution[i]);
		Entry ret = new Entry(Calendar.getInstance().getTime().toString(), clients.size(),
				questionBeingAsked.getAnswerIndex(), cd);
		reset();
		return ret;
	}

	public static void startServer() throws Exception {
		Runnable serverTask = new Runnable() {
			@Override
			public void run() {
				try {
					ServerSocket listener = new ServerSocket(PORT);
					while (true)
						new Handler(listener.accept()).start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		Thread serverThread = new Thread(serverTask);
		serverThread.start();
	}

	public static void sendQuestionToAll(NetworkQuestion q) {
		questionBeingAsked = q;
		for (Handler cycler : clients)
			cycler.sendQuestion(q);
	}

	private static class Handler extends Thread {
		private Socket socket;
		private BufferedReader in;
		private ObjectOutputStream out;

		public Handler(Socket socket) {
			this.socket = socket;
		}

		public void sendQuestion(NetworkQuestion q) {
			try {
				out.writeObject(q);
				System.out.println("object sent");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				out = new ObjectOutputStream(socket.getOutputStream());

				clients.add(this);

				while (true) {
					String input = in.readLine();
					if (input != null) {
						System.out.println("OLEY CEVAP GELDI");
						switch (input) {
						case "0":
							choiceDistribution[0]++;
							break;
						case "1":
							choiceDistribution[1]++;
							break;
						case "2":
							choiceDistribution[2]++;
							break;
						case "3":
							choiceDistribution[3]++;
							break;
						case "4":
							choiceDistribution[4]++;
							break;
						case "5":
							choiceDistribution[5]++;
							break;
						default:
							System.err.println("INVALID HANDLER INPUT");
							break;
						}
					}
				}
				// System.out.println("while bitti cok sacma");

			} catch (IOException e) {
				System.out.println(e);
			} finally {
				clients.remove(this);
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
	}

}
