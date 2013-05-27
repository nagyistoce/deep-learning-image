package home.mutant.deep.model;

import org.junit.Test;

import junit.framework.Assert;

public class TestBinarySynapses 
{
	@Test
	public void testOutput()
	{
		BinaryNeuron syn = new BinaryNeuron(56);
		long[] inputs = new long[1];
		inputs[0] = -1;
		syn.setWeights(inputs);
		Assert.assertEquals(64, syn.calculateOutput(inputs));
		inputs[0] = 0;
		Assert.assertEquals(0, syn.calculateOutput(inputs));
		inputs[0] = 33;
		Assert.assertEquals(2, syn.calculateOutput(inputs));
	}
	@Test
	public void testMem()
	{
		BinaryNeuron[] neurons = new BinaryNeuron[100000];
		for (int i = 0; i < neurons.length; i++) {
			neurons[i] = new BinaryNeuron(28*28);
		}
		Assert.assertEquals(100000, neurons.length);
	}
}
