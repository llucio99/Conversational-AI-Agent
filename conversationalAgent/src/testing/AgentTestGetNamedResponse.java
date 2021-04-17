package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import individual.Agent;

class AgentTestGetNamedResponse {

	@Test
	void test() {
		String response = Agent.getNamedResponse("My friend Jacob is feeling anxious.");
		assertEquals("AGENT: Why is your friend Jacob feeling anxious?", response);
	}

}
