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
		return output<100000;
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
		return sum;
	}
	public void modifyWeights(byte[] pixels)
	{
		for (int i = 0; i < pixels.length; i++) 
		{
			int pixel = pixels[i];
			if (pixel<0)pixel+=255;
			weights[i]=(weights[i]+pixel);
		}
		threshold = MathUtils.sumSquared(weights);
	}
	@Override
	public double getDistanceFromImage(byte[] pixels) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public double getDistanceFromNeuron(NeuronCell neuron) {
		// TODO Auto-generated method stub
		return 0;
	}
}
