package individual;
import java.io.*;
import java.net.*;

public class User {
	final static String host = "127.0.0.1";
	static Socket socket = null;				//declaration of required variables
	static DataInputStream in = null;
	static DataOutputStream out = null;
	static GUI_Frame gui = null;
	static GUI_Panel panel = null;
	static String message = "";
	
	public static void main(String[] args) throws Exception {
		gui = new GUI_Frame();
		panel = gui.getPanel1();
		
		boolean done = false;				//the while loop ahead runs based on this variable
//		System.out.println("USER ONLINE");
		
		while(!done) {						//loop begins
			String message = getUserInput();		//requesting command from user
//			System.out.println(message);
			if((socket == null || socket.isClosed()) && (!message.toLowerCase().contains("hello") && !message.toLowerCase().contains("hi") && !message.toLowerCase().contains("hey"))) {	//until connection is not set up
				if(message.toLowerCase().contains("bye") || message.toLowerCase().contains("goodbye") || message.toLowerCase().contains("good bye")) {
					addMsg("Cannot end conversation without starting one. Please start a new conversation by saying hello.");
				}
				else {
					addMsg("No active conversation. Please start a new conversation by saying hello.");				//client can only 'open' or 'quit'
				}
			}			//if something else is put in, input is asked again
			else {
				if(message.toLowerCase().contains("hello") || message.toLowerCase().contains("hi") ||message.toLowerCase().contains("hey")) {			//ignore case when comparing the strings
					if(socket == null) {				//only allows making connection if socket is currently null or closed
						int port = 20000;				//specifies port number
						socket = open(port);			//initiates connection with server
						if(socket == null) {			//checks if connection was successful
							addMsg("Could not connect to the virtual agent. Try again.");
						} else {
							addMsg("Connection with the virtual agent established.");
							in = new DataInputStream(socket.getInputStream());			//initializes the required data streams
							out = new DataOutputStream(socket.getOutputStream());
							addMsg(in.readUTF());		//server response on connection
							message = getUserInput();
							out.writeUTF(message);
							addMsg(in.readUTF());
							addMsg(in.readUTF());
							message = getUserInput();
							out.writeUTF(message);
							addMsg(in.readUTF());
							addMsg(in.readUTF());
						}
					} else {			//if connection already exist, cannot execute 'open' command
						addMsg("Terminate connection with the current agent to initiate a new conversation.");
					}
				}
				else if(message.toLowerCase().contains("bye") || message.toLowerCase().contains("goodbye") || message.toLowerCase().contains("good bye")) {		//calls quit function
					quit(socket, in, out);		//calls quit and closes the open connection and data streams
					done = true;			//break loop to allow exit
				}
				else {
					out.writeUTF(message);
					addMsg(in.readUTF());
				}
			}
		}
		System.exit(0);			//exit
	}
	
	public static String getUserInput() throws InterruptedException {
		String s;
		while(true) {
			Thread.sleep(500);
			s = panel.getMsg();
			if(s != null && !s.isEmpty())
				break;
		}
		panel.setMsg("");
		return s;
	}
	
	public static void addMsg(String s) throws InterruptedException {
		Thread.sleep(500);
		panel.addToMsgHistory(s);
	}
	
	public static Socket open(int port) {		//open function to establish connection
		Socket sock = null;
		try {									//handling errors
			sock = new Socket(host, port);		//establishes connection
		} catch (UnknownHostException e) {
			panel.addToMsgHistory("Unknown Host Error");
		} catch (IOException e) {
			panel.addToMsgHistory("I/O Error");
		}
		return sock;						//returns socket if connection successful and null if unsuccessful
	}
	
	public static void quit(Socket sock, DataInputStream in, DataOutputStream out) throws InterruptedException {		//quit function to close connection, if any, and exit client
		try {
			out.writeUTF("quit");		//tells server to close connection, if any
			panel.addToMsgHistory(in.readUTF());		//takes response from server
			Thread.sleep(2000);
			sock.close();		//closes connection and other data streams
			in.close();
			out.close();
		} catch (IOException e) {		//for I/O errors
			panel.addToMsgHistory("Unable to exit. Try again.");
		}
	}
}
