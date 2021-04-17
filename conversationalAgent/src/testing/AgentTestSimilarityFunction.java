package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import individual.Agent;

class AgentTestSimilarityFunction {

	@Test
	void test() {

		double similarity = Agent.getSimilarityBetweenStrings("This is a test sentence.", "This is a test sentense.");
		assertEquals(similarity, 23.0/24.0);
			
	}

}
