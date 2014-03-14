package home.mutant.deep.neurons;

import home.mutant.deep.abstracts.Neuron;
import home.mutant.deep.ui.Image;
import home.mutant.deep.utils.MathUtils;

public class FloatNeuron implements Neuron
{
	public static float MAX_SYNAPSE = 100000;
	public static float STEP_LEARNING = 1;
	
	public float[] weights = null;
	public FloatNeuron(int noSynapses)
	{
		weights = new float[noSynapses];
	}
	@Override
	public double calculateOutputDouble(Image image)
	{
		byte[] input = image.getDataOneDimensional();
		double sum=0;
		for (int i = 0; i < input.length; i++)
		{
			if (i>=weights.length)
				break;
			int in = input[i];
			if (in<0)
			{
				in+=256;
			}
			sum = in*weights[i];
		}
		return  sum;
	}
	public long calculateOutput(Image image)
	{
		return (long) calculateOutput(image);
	}
	@Override
	public double calculateOutputProbability(Image image)
	{
		double output = calculateOutput(image);
		return MathUtils.sigmoidFunction(output);
	}

	@Override
	public void randomize()
	{
		for (int i = 0; i < weights.length; i++)
		{
			weights[i] = (float) (Math.random()-0.5)*MAX_SYNAPSE;
		}
	}

	@Override
	public Image generateSample()
	{
		byte[] byteWeights = new byte[weights.length];
		for (int i = 0; i < byteWeights.length; i++)
		{
			int w = (int) (MathUtils.sigmoidFunction(weights[i])*256);
			if (w>127)
				w-=256;
			byteWeights[i] = (byte) w;
		}
		return new Image(byteWeights);
	}

	@Override
	public void initWeightsFromImage(Image image)
	{
		byte[] input = image.getDataOneDimensional();
		for (int i = 0; i < input.length; i++)
		{
			if (i>=weights.length)
				break;
			int in = input[i];
			if (in<0)
			{
				in+=256;
			}
			weights[i] = in*MAX_SYNAPSE;
		}
	}

	@Override
	public void updateWeightsFromImage(Image image)
	{
		byte[] input = image.getDataOneDimensional();
		for (int i = 0; i < input.length; i++)
		{
			if (i>=weights.length)
				break;
			int in = input[i];
			if (in==0)
			{
				weights[i] -= STEP_LEARNING;
			}
			else
			{
				weights[i] += STEP_LEARNING;
			}
			
		}	
	}

	@Override
	public void decayWeights(Image image)
	{
		for (int i = 0; i < weights.length; i++)
		{
			weights[i] -= STEP_LEARNING;
		}
	}

}
