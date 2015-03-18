package home.mutant.logistic;

import home.mutant.deep.utils.MathUtils;

public class LogisticNeuron 
{
	public static float MAX_SYNAPSE = (float) 10;
	public float weights[];
	
	public LogisticNeuron(int noSynapse)
	{
		weights = new float[noSynapse];
		//randomize();
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
	
	public double outputPositive(byte[] pixels)
	{
		double output = output(pixels);
		return Math.abs(output+100000);
	}
	
	public double outputSigmoid(byte[] pixels)
	{
		double output = 0;
		for (int i = 0; i < weights.length; i++)
		{
			double pixel = pixels[i];
			if (pixel<0)pixel+=255;
			output+=weights[i]*pixel/100;
		}
		return MathUtils.sigmoidFunctionGamma(output, 0.0005);
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
