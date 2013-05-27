package home.mutant.deep.model;


public class BinaryNeuron
{
	private long[] weights = null;
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
	
	public void setWeights(long[] initWeights)
	{
		int length = initWeights.length;
		if (weights.length<length)
		{
			length = weights.length;
		}
		System.arraycopy(initWeights, 0, weights, 0, length);
	}
	
	public long calculateOutput(long[] inputs)
	{
		long sum = 0;
		for (int i = 0; i < inputs.length; i++) 
		{
			if (i>=weights.length)
			{
				return sum;
			}
			sum+=Long.bitCount(~(inputs[i]^weights[i]));
		}
		return sum - weights.length*64+noSynapses;
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
}
