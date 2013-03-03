package home.mutant.deep.model;

import org.junit.Assert;
import org.junit.Test;


public class TestTwoFullConnectedLayers
{	
	@Test
	public void testConstructor1NoBias()
	{
		TwoFullConnectedLayers wm = new TwoFullConnectedLayers(2,4);
		Assert.assertEquals(2,wm.weights.length);
		Assert.assertEquals(4,wm.weights[0].length);
	}

	@Test
	public void testGenerateNoBias()
	{
		TwoFullConnectedLayers wm = new TwoFullConnectedLayers(2,2);
		wm.weights[0][0] = 2.;
		wm.weights[0][1] = 2.;
		wm.weights[1][0] = 2.;
		wm.weights[1][1] = 2.;
		
		double[] output = wm.generateSample();
		Assert.assertEquals(2, output.length);
	}
}
