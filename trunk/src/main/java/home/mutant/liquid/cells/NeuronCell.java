package home.mutant.liquid.cells;

import home.mutant.deep.utils.MathUtils;

public class NeuronCell
{
	public float[] weights = null;
	public NeuronCell(int noSynapses)
	{
		weights = new float[noSynapses];
	}
	public double outputProbability(int[] input)
	{
		double sum=0;
		for (int i = 0; i < input.length; i++) 
		{
			sum+=input[i]*weights[i];
		}
		return MathUtils.sigmoidFunction(sum);
	}
}
