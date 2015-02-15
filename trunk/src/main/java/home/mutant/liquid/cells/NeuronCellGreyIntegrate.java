package home.mutant.liquid.cells;

import home.mutant.deep.utils.MathUtils;

public class NeuronCellGreyIntegrate extends NeuronCell 
{
	public NeuronCellGreyIntegrate(int noSynapses) 
	{
		super(noSynapses);
		threshold=0;
	}
	public double output(byte[] pixels)
	{
		double sum=0;
		for (int i = 0; i < pixels.length; i++) 
		{
			int pixel = pixels[i];
			if (pixel<0)pixel+=255;
			double weight = weights[i];
			//if (weight==0) weight=-150;
			sum+=(pixel)*weight;
		}
		//System.out.println(sum);
		return sum;
	}
	public boolean isFiring(byte[] pixels)
	{
		output = output(pixels);
//		System.out.println(output);
//		System.out.println(threshold);
//		System.out.println();
		return output>=threshold;
	}
	public void modifyWeights(byte[] pixels)
	{
		double added=0;
		for (int i = 0; i < weights.length; i++) 
		{
			int pixel = pixels[i];
			if (pixel<0)pixel+=255;
			double add = (255-pixel)*0.0001;
			added+=add;
			weights[i]+=add;
			if (weights[i]>127)
				weights[i]=127;
		}
		added/=weights.length/2;
		for (int i = 0; i < weights.length; i++) 
		{
			weights[i]-=added;
			if (weights[i]<-128)
				weights[i]=-128;
		}
		noUpdates++;
		//if (output>threshold)
			threshold =output/1.2;
	}
	public double getDistanceFromImage(byte[] pixels)
	{
		output = output(pixels);
		return threshold - output;
	}
	public double output(double[] pixels)
	{
		double sum=0;
		for (int i = 0; i < pixels.length; i++) 
		{
			double weight = weights[i];
			//if (weight==0) weight=-150;
			sum+=(pixels[i])*weight;
		}
		return sum;
	}
	public double getDistanceFromNeuron(NeuronCell neuron)
	{
		output = output(neuron.weights);
		return output;
	}
	@Override
	public double output(NeuronCell other)
	{
		double sum=0;
		for (int i = 0; i < other.weights.length; i++) 
		{
			double weight = weights[i]-other.weights[i];
			sum+=weight*weight;//Math.abs(weight);

		}
		sum = Math.sqrt(sum);
		//System.out.println(sum);
		return sum;
	}
}
