package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import individual.Agent;

class AgentTestGetDefaultAnswer {

	@Test
	void test() {
		
		// Check that question are recognized
		String defaultAnswer = Agent.getDefaultAnswer(0, "This is a question?");
		assertEquals(defaultAnswer, "AGENT: Sorry I don't understand your question, could you ask me something else?");

		
		// Test general default answer
		defaultAnswer = Agent.getDefaultAnswer(0, "This is a test sentence.");
		boolean oneOfThreeDefaultAnswer = defaultAnswer.equals("AGENT: Sorry I don't understand, let's talk about something else.") || 
										  defaultAnswer.equals("AGENT: Sorry I don't understand, let's talk about psychology.");		
		assertEquals(oneOfThreeDefaultAnswer, true);
		
	}
}
