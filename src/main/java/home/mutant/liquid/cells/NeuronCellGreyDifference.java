package home.mutant.liquid.cells;

import home.mutant.deep.utils.MathUtils;

public class NeuronCellGreyDifference extends NeuronCell
{

	public NeuronCellGreyDifference(int noSynapses) 
	{
		super(noSynapses);
	}
	public boolean isFiring(byte[] pixels)
	{
		output = output(pixels);
//		System.out.println(output);
//		System.out.println(threshold);
//		System.out.println();
		return output<800/(noUpdates+1);
	}
	public double output(byte[] pixels)
	{
		double sum=0;
		for (int i = 0; i < pixels.length; i++) 
		{
			int pixel = pixels[i];
			if (pixel<0)pixel+=255;
			double weight = weights[i]-pixel;
			sum+=weight*weight;
		}
		return Math.sqrt(sum);
	}
	public void modifyWeights(byte[] pixels)
	{
		for (int i = 0; i < pixels.length; i++) 
		{
			int pixel = pixels[i];
			if (pixel<0)pixel+=255;
			weights[i]=(weights[i]+pixel)/2;
		}
		//threshold = MathUtils.sumSquared(weights);
		noUpdates++;
	}
	@Override
	public double getDistanceFromImage(byte[] pixels) 
	{
		return 0;
	}
	@Override
	public double getDistanceFromNeuron(NeuronCell neuron) 
	{
		return 0;
	}
}
