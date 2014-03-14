package home.mutant.deep.neurons;

import home.mutant.deep.abstracts.Neuron;
import home.mutant.deep.ui.Image;


public class BinaryNeuron implements Neuron
{
	public long[] weights = null;
	public int noSynapses=0;
	public BinaryNeuron(int noSynapses)
	{
		int noLongs  = noSynapses/64;
		if (noSynapses%64!=0)
		{
			noLongs++;
		}
		this.noSynapses = noSynapses;
		weights = new long[noLongs];
	}

	public long calculateOutput(Image image)
	{
		long[] inputs = image.getDataBinary();
		long sum = 0;
		for (int i = 0; i < inputs.length; i++) 
		{
			if (i>=weights.length)
			{
				break;
			}
			sum+=64 - Long.bitCount(~(inputs[i]^weights[i]));
		}
		return sum;
	}

	public double calculateOutputProbability(Image image) 
	{
		double prob = calculateOutput(image);
		return prob/weights.length;
	}
	
	public void randomize() 
	{
		for (int index = 0; index<noSynapses;index++)
		{
			int indexLong = index/64;
			int indexBit = index%64;
			weights[indexLong] = Math.random()>=0.5?weights[indexLong] | (1 << indexBit): weights[indexLong] & ~(1 << indexBit);
		}
	}

	public Image generateSample() 
	{
		return new Image(weights,(int)Math.sqrt(noSynapses), (int)Math.sqrt(noSynapses));
	}

	public void initWeightsFromImage(Image image) 
	{
		weights = image.getDataBinary();
	}

	public void updateWeightsFromImage(Image image) 
	{
		initWeightsFromImage(image);
	}

	public void decayWeights(Image image)
	{
		for (int i = 0; i < weights.length; i++) 
		{
			weights[i]=0;
		}
	}
	public double calculateOutputDouble(Image image)
	{
		return (double)calculateOutput(image);
	}
}
