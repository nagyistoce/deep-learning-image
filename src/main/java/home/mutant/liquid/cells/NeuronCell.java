package home.mutant.liquid.cells;

import java.util.List;

import home.mutant.deep.utils.MathUtils;

public class NeuronCell
{
	public double[] weights = null;
	public double output = 0;
	
	public NeuronCell(int noSynapses)
	{
		weights = new double[noSynapses];
		for (int i=0;i<noSynapses;i++)
		{
			if (Math.random()>.6)
				weights[i]=Math.random()*10-5;
		}
	}
	public double outputProbability(double[] input)
	{
		double sum=0;
		for (int i = 0; i < input.length; i++) 
		{
			sum+=input[i]*weights[i];
		}
		output = MathUtils.sigmoidFunction(sum)-0.5;
		return output;
	}
	public double outputProbability(List<NeuronCell> input)
	{
		double sum=0;
		for (int i = 0; i < input.size(); i++) 
		{
			sum+=input.get(i).output*weights[i];
		}
		output = MathUtils.sigmoidFunction(sum)-0.5;
		return output;
	}
}
