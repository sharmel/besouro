package athos.integration;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.junit.Test;

import athos.listeners.mock.JUnitEventFactory;
import athos.listeners.mock.ResourceChangeEventFactory;

public class ProductionEpisodesRecognition extends IntegrationTest {

	@Test 
	public void productionCategory1() throws Exception {
		
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfStatements()).thenReturn(14);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// TODO [rule] its a strange case without an edit after the test failure :-/
		// we do not need this failure
		// Unit test failue
//		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));

		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] production 1", stream.getRecognizedEpisodes().get(0));
		
	}
	
	@Test 
	public void productionCategory2() throws Exception {
		
		// method increase but byte size decrease
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfStatements()).thenReturn(2);
		when(meter.getNumOfMethods()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",5));
		
		// TODO [rule] its a strange case without an edit after the test failure :-/
		// we do not need this failure
		// Unit test failue
//		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));
		

		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] production 2", stream.getRecognizedEpisodes().get(0));
		
	}
	
	@Test 
	public void productionCategory2_2() throws Exception {
		
		// method increase but byte statement decrease
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfMethods()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",15));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));
		
		// TODO [rule] its a strange case without an edit after the test failure :-/
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(2, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] production 2", stream.getRecognizedEpisodes().get(0));
		//TODO [rule] this one was not considered by hingbings test
		Assert.assertEquals("[episode] refactoring 2A", stream.getRecognizedEpisodes().get(1));
		
	}
	
	@Test 
	public void productionCategory3() throws Exception {
		
		// method increase, and size increase and LARGE byte increase
		
		// Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		when(meter.getNumOfMethods()).thenReturn(5);
		when(meter.getNumOfStatements()).thenReturn(5);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",133));

		
		// TODO [rule] its a strange case without an edit after the test failure :-/
		// we do not need this failure
		// Unit test failue
//		junitListener.sessionFinished(JUnitEventFactory.createFailingSession("TestFile.java"));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createPassingSession("TestFile.java"));
		
		Assert.assertEquals(1, stream.getRecognizedEpisodes().size());
		Assert.assertEquals("[episode] production 3", stream.getRecognizedEpisodes().get(0));
		
	}
	
}
