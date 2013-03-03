package home.mutant.deep.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


public class TestWeightsModel
{
	@Test
	public void testConstructor1Bias()
	{
		List<Integer> neurons = new ArrayList<Integer>();
		neurons.add(2);
		neurons.add(4);
		FullIterconnectedWeightsModel wm = new FullIterconnectedWeightsModel(neurons,true);
		Assert.assertEquals(2, wm.weights.size());
		Assert.assertEquals(1,wm.weights.get(0).length);
		Assert.assertEquals(2,wm.weights.get(0)[0].length);
		Assert.assertEquals(3,wm.weights.get(1).length);
		Assert.assertEquals(4,wm.weights.get(1)[0].length);
	}

	@Test
	public void testGenerateBias()
	{
		List<Integer> neurons = new ArrayList<Integer>();
		neurons.add(2);
		neurons.add(2);
		FullIterconnectedWeightsModel wm = new FullIterconnectedWeightsModel(neurons,true);
		wm.weights.get(0)[0][0] = 2.;
		wm.weights.get(0)[0][1] = 2.;
		
		wm.weights.get(1)[0][0] = 3.;
		wm.weights.get(1)[0][1] = 4.;
		wm.weights.get(1)[1][0] = 5.;
		wm.weights.get(1)[1][1] = 6.;
		wm.weights.get(1)[2][0] = 5.;
		wm.weights.get(1)[2][1] = 6.;
		double[] output = wm.generateSample();
		Assert.assertEquals(2, output.length);
	}
	
	@Test
	public void testConstructor1NoBias()
	{
		List<Integer> neurons = new ArrayList<Integer>();
		neurons.add(2);
		neurons.add(4);
		FullIterconnectedWeightsModel wm = new FullIterconnectedWeightsModel(neurons,false);
		Assert.assertEquals(1, wm.weights.size());
		Assert.assertEquals(2,wm.weights.get(0).length);
		Assert.assertEquals(4,wm.weights.get(0)[0].length);
	}

	@Test
	public void testGenerateNoBias()
	{
		List<Integer> neurons = new ArrayList<Integer>();
		neurons.add(2);
		neurons.add(2);
		FullIterconnectedWeightsModel wm = new FullIterconnectedWeightsModel(neurons,false);
		wm.weights.get(0)[0][0] = 2.;
		wm.weights.get(0)[0][1] = 2.;
		wm.weights.get(0)[1][0] = 2.;
		wm.weights.get(0)[1][1] = 2.;
		
		double[] output = wm.generateSample();
		Assert.assertEquals(2, output.length);
	}
}
