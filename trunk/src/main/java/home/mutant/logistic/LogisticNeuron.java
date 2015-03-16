package home.mutant.logistic;

public class LogisticNeuron 
{
	public static float MAX_SYNAPSE = (float) 0.1;
	public float weights[];
	
	public LogisticNeuron(int noSynapse)
	{
		weights = new float[noSynapse];
	}
	public void randomize()
	{
		for (int i = 0; i < weights.length; i++)
		{
			weights[i] = (float) (Math.random()-0.5)*MAX_SYNAPSE;
		}
	}
	
	public void learn(byte[] pixels)
	{
		for (int i = 0; i < weights.length; i++)
		{
			double pixel = pixels[i];
			if (pixel<0)pixel+=255;
			weights[i]+=pixel/100;
		}
	}
	
	public double output(byte[] pixels)
	{
		double output = 0;
		for (int i = 0; i < weights.length; i++)
		{
			double pixel = pixels[i];
			if (pixel<0)pixel+=255;
			output+=weights[i]*pixel/100;
		}
		return output;
	}
	
	public void unlearn(byte[] pixels)
	{
		for (int i = 0; i < weights.length; i++)
		{
			double pixel = pixels[i];
			if (pixel<0)pixel+=255;
			weights[i]-=pixel/100;
		}
	}
}
