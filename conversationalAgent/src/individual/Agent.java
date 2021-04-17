package individual;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Scanner;

import edu.stanford.nlp.simple.Sentence;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Agent {
	final static String host = "127.0.0.1";
	final static int port = 20000;
	static String[] positiveWords1 = {"happy", "content", "satisfied", "good"};
	static String[] positiveWords2 = {"great", "overjoyed", "exstatic", "awesome", "amazing", "fantastic"};
	static String[] negativeWordsL1 = {"sad", "bad", "unhappy", "bored", "scared"};
	static String[] negativeWordsL2 = {"terrified", "depressed", "extremely unhappy", "awful", "terrible"};
	static File file = new File("questionBank.txt");
	static File file2 = new File("namedResponsesBank.txt");
	static File file3 = new File("questionBank2.txt");



	
	public static void main(String[] args) throws Exception {
		ServerSocket ss = new ServerSocket(port);		//creating server socket
		Socket socket = null;			//declaration of required variables		
		DataInputStream in = null;
		DataOutputStream out = null;
		boolean done = false;			//the while loops ahead run based on this variable
		

		
		
		System.out.println("AGENT ONLINE");
		while(!done) {			//loop begins
			try {
				socket = ss.accept();	//accepts connection request from client
				in = new DataInputStream(socket.getInputStream());		//data streams initialized
				out = new DataOutputStream(socket.getOutputStream());
				System.out.println("('" + host + "', " + port + ") : OPEN");	//server side output
				String name = "";
				int mood = 0;
				out.writeUTF("AGENT: Hello! What is your name?");		//says hello to client
				String msg = name = in.readUTF();
				if(name.toLowerCase().contains("my name is")) {
					name = name.substring(11);
					out.writeUTF("AGENT: Nice to meet you " + name.substring(0, 1).toUpperCase() + name.substring(1) + ". Alice 2.0 at your service.");
				}
				else
					out.writeUTF("AGENT: Nice to meet you " + name.substring(0, 1).toUpperCase() + name.substring(1) + ". Alice 2.0 at your service.");
				out.writeUTF("AGENT: How are you feeling today?");
				msg = in.readUTF();
				mood += updateMood(msg);
				out.writeUTF(moodResponse(msg));
				out.writeUTF("AGENT: What can I help you with today?");
				while(!done) {		//inner loop begins for data transfer or connection termination after connection has been established
					String msgNonEdited = in.readUTF();
					msg = msgNonEdited.toLowerCase();		//takes client message to carry out the required function
			    	String namedResponse = getNamedResponse(msgNonEdited);
					
			    	if(msg.toLowerCase().contains("translate")) { //INDIVIDUAL, translate functionality
			    		out.writeUTF("AGENT: What would you like to translate?");
			    		String word = in.readUTF();
			    		out.writeUTF("AGENT: To what language would you like this translated to?");
			    		String langWord = in.readUTF().toLowerCase();
			    		String lang = langWord;
			    		switch(lang) { // encode input to accepted values for the translate function
			    		case "spanish":
			    			lang = "es";
			    			break;
			    		case "french":
			    			lang = "fr";
			    			break;
			    		case "german":
			    			lang = "de";
			    			break;
			    		case "italian":
			    			lang = "it";
			    			break;
			    		case "russian":
			    			lang = "ru";
			    			break;
			    		default:
			    			lang = "en";
			    		}
			    		out.writeUTF("AGENT: \'" + word + "\' translated to " + langWord + " is: " + Translator.translate("en", lang, word)
			    				+ "\nAGENT: What else can I help you with?");
			    	}
			    	
			    	else if(msg.toLowerCase().contains("twitter")) { //INDIVIDUAL, twitter functionality
			    		ConfigurationBuilder cb = new ConfigurationBuilder(); // keys and functions required to connect to a twitter account
			    		cb.setDebugEnabled(true)
			    		  .setOAuthConsumerKey("XIv0lPMy3f2N3qEZTzmRhljn8")
			    		  .setOAuthConsumerSecret("RrvbkJFr6fmzmItNekIgeTBP1mZgrhErVVEu11R4bV79M6ut3x")
			    		  .setOAuthAccessToken("767559948968595457-ri5GnGVUCzQw4DIFD36NpEgKwRl2c5Q")
			    		  .setOAuthAccessTokenSecret("sSMcmdr7NOD1RveUy8LmVo08osYrI2miVR2YcvaO3UziB");
			    		TwitterFactory tf = new TwitterFactory(cb.build());
			    		
			    		out.writeUTF("AGENT: What keyword should we look for in Twitter?");
			    		String keyword = in.readUTF();
			    		//Twitter twitter = tf.getInstance();
			    		Twitter twitter = tf.getInstance();
			    	    Query query = new Query(keyword);
			    	    QueryResult result = twitter.search(query);
			    	    List<Status> tweets = result.getTweets(); //get a list of many tweets with that keyword
			    	    out.writeUTF("AGENT: @" + tweets.get(0).getUser().getScreenName() + " - " + tweets.get(0).getText()
			    	    		+ "\nAGENT: What else can I help you with?"); //grab the first tweet only
			    	}
			    	
			    	else if(msg.equalsIgnoreCase("close") || msg.equalsIgnoreCase("quit")) {	//for terminating connection with client
						out.writeUTF("AGENT: Goodbye!");		//say goodbye to client
						socket.close();			//terminate connection and other data streams
						in.close();
						out.close();
						System.out.println("Connection terminated. Looking for new user connections.");
						done = true;		//break inner loop 
					}						//this allows for taking connection requests from clients again
					
					else if (namedResponse != null) { // If the users message contains a name, get a response suited to the name
			        	out.writeUTF(namedResponse);
					}
					else {
						mood += updateMood(msg);
						//isQuestion(msg);
						System.out.println("USER: " + msg);
						try {
							
							Scanner scan = new Scanner(file);
						    int numLine = 0;
						    boolean end = false;
						    while (scan.hasNextLine() && !end) {
						    	
						    	// MAIN RESPONSE LOGIC
						        String line = scan.nextLine();
						        
						        numLine++;
						        if(numLine % 2 != 0) {
						        	
						        	// To skip the OR parts in the data
						        	if (line.length() > 6) {
								    	double similarity = getSimilarityBetweenStrings(msg.toLowerCase(), line.toLowerCase().substring(6));
								    	double wordSimilarity = wordSimilarityFactor(msg.toLowerCase(), line.toLowerCase().substring(6));								    	
			
								    	if (similarity > 0.8 || wordSimilarity > 0.6) {	    		
								        	out.writeUTF(scan.nextLine());
								        	end = true;
								    	}
						        	}
						        }
						    }
						    scan.close();
						    
						    // Check all data in questionBank2.txt
						    if (!end) {
								scan = new Scanner(file3);
								numLine = 0;
							    end = false;
							    while (scan.hasNextLine() && !end) {
							    	
							    	// MAIN RESPONSE LOGIC
							        String line = scan.nextLine();
							        
							        numLine++;
							        if(numLine % 2 != 0) {
							        	
							        	// To skip the OR parts in the data
							        	if (line.length() > 6) {
									    	double similarity = getSimilarityBetweenStrings(msg.toLowerCase(), line.toLowerCase().substring(6));
									    	double wordSimilarity = wordSimilarityFactor(msg.toLowerCase(), line.toLowerCase().substring(6));								    	
				
									    	if (similarity > 0.8 || wordSimilarity > 0.6) {	    		
									        	out.writeUTF(scan.nextLine());
									        	end = true;
									    	}
							        	}
							        }
							    }
						    }
						    
						    // If no reponses were found in either questionBank 1 or 2, return a default response
						    if (!end) out.writeUTF(getDefaultAnswer(mood, msg));
						    
						} catch(FileNotFoundException e) { 
							System.out.println("File not found.");
							e.getStackTrace();
							out.writeUTF(getDefaultAnswer(mood, msg));
						}
					}
				}
			} catch(SocketException e) {
				System.out.println("User disconnected abruptly. Seeking new connections now.");
				socket.close();			//terminate connection and other data streams
				in.close();
				out.close();
			} catch(Exception e) {
				System.out.println("Unidentified error! Agent shutting down!");
			}
			finally {
				done = false;		//outer loop needs to continue indefinitely for allowing server to still take connection requests
			}
		}
		ss.close();		//closing server socket
	}
	
	// If the user sends a message with a name in it, give a custom response based on that name.
	public static String getNamedResponse(String s1) {
		Sentence names = new Sentence(s1);
		
		if (names.mentions("PERSON").size() != 0) {
			
			// Go through each line in the namedResponsesBank file and find best match
			try (Scanner scan = new Scanner(file2);) {
			    int numLine = 0;
			    
			    while (scan.hasNextLine()) {
			    	
			    	// MAIN RESPONSE LOGIC
			        String line = scan.nextLine();
			        
			        numLine++;
			        if(numLine % 2 != 0) {
			        	// To skip the OR parts in the data
			        	if (line.length() > 6) {
					    	double wordSimilarity = wordSimilarityFactor(s1.toLowerCase(), line.toLowerCase().substring(6));

			        	
					    	if (wordSimilarity > 0.5) {	    		
					    		String namedResponse = scan.nextLine();
					    		String namedResponseWithName = namedResponse.replace("NAME", names.mentions("PERSON").get(0));
					    		return namedResponseWithName;
					    	}
			        	}
			        }
			    }

			} catch(FileNotFoundException e) { 
				e.getStackTrace();
				System.out.println("File not found.");
			}
			
			return null;
			
		} else {
			return null;
		}
	}
	
	public static double wordSimilarityFactor(String s1, String s2) {
				
		double percentageOfWordsOfS1ThatAreInS2;
		
		Sentence userMessage = new Sentence(s1);
		Sentence userLibrarySentence = new Sentence(s2);
		
		double numberOfCommonWords = 0.0;
		
		for (int i = 0; i < userMessage.words().size(); i++) {
			if (userLibrarySentence.words().contains(userMessage.words().get(i))) {
				numberOfCommonWords++;
			}
		}
		percentageOfWordsOfS1ThatAreInS2 = numberOfCommonWords/userMessage.words().size();
		
		return percentageOfWordsOfS1ThatAreInS2;
	}
	
	// returns: 1 if the strings are exactly the same, and lower the more they are different
	public static double getSimilarityBetweenStrings(String s1, String s2) {
		
		if (s1.length() == s2.length()) {
			double sameCharactersAtSamePlaces = 0;
			
			for (int i = 0; i < s1.length(); i++) {
				if (s1.charAt(i) == s2.charAt(i)) sameCharactersAtSamePlaces++;
			}
			
			return sameCharactersAtSamePlaces/s1.length();
		}
		
		return 0.0;
	}
	
	public static String moodResponse(String s) {
		String s1 = "AGENT: That is good to hear. I feel the same way.";
		String s2 = "AGENT: I am sorry to hear that. Maybe talking to me will make you feel better.";
		for (int i = 0; i < positiveWords1.length; i++)
			if (s.toLowerCase().contains(positiveWords1[i])) return s1;
		for (int i = 0; i < positiveWords2.length; i++)
			if (s.toLowerCase().contains(positiveWords2[i])) return s1;
		for (int i = 0; i < negativeWordsL1.length; i++)
			if (s.toLowerCase().contains(negativeWordsL1[i])) return s2;
		for (int i = 0; i < negativeWordsL2.length; i++)
			if (s.toLowerCase().contains(negativeWordsL2[i])) return s2;
		return "AGENT: Thanks for sharing.";
	}
	
	public static int updateMood(String s) {
		for (int i = 0; i < positiveWords1.length; i++)
			if (s.toLowerCase().contains(positiveWords1[i])) return 1;
		for (int i = 0; i < positiveWords2.length; i++)
			if (s.toLowerCase().contains(positiveWords2[i])) return 3;
		for (int i = 0; i < negativeWordsL1.length; i++)
			if (s.toLowerCase().contains(negativeWordsL1[i])) return -1;
		for (int i = 0; i < negativeWordsL2.length; i++)
			if (s.toLowerCase().contains(negativeWordsL2[i])) return -3;
		return 0;
	}
	
	public static boolean isQuestion(String s) {
		if (s.contains("?")) return true;
			else return false;
	}
	
	// Gives one of 5 possible default responses
	public static String getDefaultAnswer(int mood, String message) {
		if (mood > 5)
			return "AGENT: Sorry I don't quite understand but it sounds like something is going well for you. It is good to know. Lets talk about something else now.";
		if (mood < -5)
			return "AGENT: Sorry I don't quite understand but it sounds like you are not feeling good about something. I am sorry to hear that. Lets divert you mind and talk about something else now.";
		
		if (isQuestion(message)) {
			return "AGENT: Sorry I don't understand your question, could you ask me something else?";
		}
		
		int randomIndex = (int) Math.round(Math.random()*2);
		
		String[] defaultResponses = {"AGENT: Sorry I don't understand, let's talk about something else.",
		                             "AGENT: Sorry I don't understand, let's talk about psychology."};
		
		return defaultResponses[randomIndex];
	}

}
