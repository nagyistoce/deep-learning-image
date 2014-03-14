package home.mutant.deep.neurons;

import home.mutant.deep.abstracts.Neuron;
import home.mutant.deep.ui.Image;
import home.mutant.deep.utils.MathUtils;

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
				break;
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
			sum+=w*in;
		}
		return sum;
	}

	public double calculateOutputProbability(Image image) 
	{
		double output = calculateOutput(image);
		//System.out.println(prob);
		return MathUtils.sigmoidFunction(output-3);
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
			int w = weights[i];
			int in = inputs[i];	
			if (w<0)
			{
				w += 256;
			}
			if (in<0)
			{
				in += 256;
			}

			if (in>w)
			{
				if (w<255)
					w++;
			}
			else if (in<w)
			{
				if (w>0)
					w--;
			}
			if (w>127)
				weights[i] = (byte)(w-256);
			else
				weights[i] = (byte)w;
		}
	}

	public void decayWeights(Image image) 
	{
		for (int i = 0; i < weights.length; i++) 
		{
			int w= weights[i];
			if (w<0)
			{
				w += 256;
			}

			if (w>0)
				w--;

			if (w>127)
				weights[i] = (byte)(w-256);
			else
				weights[i] = (byte)w;
		}
	}
	@Override
	public double calculateOutputDouble(Image image)
	{
		return (double)calculateOutput(image);
	}
}
