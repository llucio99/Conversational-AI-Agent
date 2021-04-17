# COSC310 - Luis Lucio
**Note: All medical information in this project is not professional medical advice and should not be percieved as such. The sole purpose of the project is to learn how to create AI chatbots.**

**The Agent and User class files must be run to start the program. We need to start the Agent (server side) first and then start the execution of the User (client side) so that an Agent is available to provide service to a User.**

- The project is focused on creating an AI conversational agent which takes the role of a psychologist.
- Two Java programs need to be run for the converstaion, these are Agent.java and User.java. The agent acts as the server seeking connections from users continuously whereas the user establishes connection with the server when in need of a conversation, acting as a client. Only the User.java file runs at the users end. The User.java file also uses the GUI_Frame.java and GUI_Panel.java files to make a UI which the user can use to talk with the agent.
- Apart from the basic conversation and functionality such as ending and establishing connections, etc., the agent makes use of the questionBank.txt, questionBank2.txt and namedResponsesBank.txt files to provide answers to the user's questions. The file needs to be placed correctly in reference to the Agent.java file in the server-end storage.
- The code functions as required.

- The project now integrates two different API services: Google Cloud Translation API and Twitter API.
- These can be called at any point during the conversation to create a small interaction with the agent in order to either translate a word/sentence, or to search for a random tweet with a specified keyword.


## **Updates**

### Features added:

**Java classes and libraries were added to integrate new APIs:**
* Translator.java file was added to connect to the Google Cloud Translation API
* Twitter4j java library was added to the classpath to connect to the Twitter API

**Added Google Could Translation API:**
* Integration was done through Google Scripts and the Translator.java file
* You can prompt the translate functionality by typing 'Translate' into the input box
* The agent will then initialize a specific interaction with the user that goes through steps to translate an input to desired language
* It supports translation to 5 languages including: Spanish, French, Italian, Russian, and German
* The language selector is done through a switch statement, with a default value to translating back to english
* After printing the final result, the agent will resume normal interaction with the user
* Example:
<img src="API examples/translate.png?raw=true" width="500">

**Added Twitter API:**
* Integration was done through the Twitter4j Java Library
* You can prompt the twitter functionality by typing 'Twitter' into the input box
* The agent will then ask you to type in a keyword/phrase to look for in Twitter
* Then, the agent will display a random tweet with that keword before returning to normally interacting with the user
* *Side note: As this was done on my personal twitter profile, I disabled any posting functionality*
* Example:
<img src="API examples/twitter.png?raw=true" width="500">
