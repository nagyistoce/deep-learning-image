package home.mutant.deep.neurons;

import home.mutant.deep.abstracts.Neuron;
import home.mutant.deep.ui.Image;

public class ByteNeuron implements Neuron 
{
	public byte[] weights = null;
	public ByteNeuron(int noSynapses)
	{
		weights = new byte[noSynapses];
	}
	@Override
	public long calculateOutput(Image image) 
	{
		byte[] inputs = image.getDataOneDimensional();
		long sum = 0;
		for (int i = 0; i < inputs.length; i++) 
		{
			if (i>=weights.length)
			{
				return sum;
			}
			int w= weights[i];
			int in = inputs[i];
			if (w<0)
			{
				w += 256;
			}
			if (in<0)
			{
				in += 256;
			}		
			int j = Math.abs(w-in);
			sum+=256-j;
		}
		return sum;
	}

	@Override
	public void randomize() 
	{
		for (int i = 0; i < weights.length; i++) 
		{
			weights[i] = (byte)((Math.random()-0.5)*256);
		}
	}

	@Override
	public Image generateSample() 
	{
		return new Image(weights);
	}

	@Override
	public void initWeightsFromImage(Image image) 
	{
		weights = image.getDataOneDimensional();
	}

	/**
	 * Just pull the weights toward inputs
	 */
	public void updateWeightsFromImage(Image image) 
	{
		byte[] inputs = image.getDataOneDimensional();
		for (int i = 0; i < weights.length; i++) 
		{
			if (inputs[i]>weights[i])
			{
				weights[i]++;
			}
			else
			{
				weights[i]--;
			}
		}
	}

	public void decayWeights() 
	{
		for (int i = 0; i < weights.length; i++) 
		{
			if (weights[i]>-128)
			{
				weights[i]--;
			}
		}
	}
}
