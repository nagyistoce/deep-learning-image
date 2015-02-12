package home.mutant.liquid.cells;

import home.mutant.deep.utils.MathUtils;

public class NeuronCellGreyDifference extends NeuronCell
{
	double initThreshold = 0;
	public NeuronCellGreyDifference(int noSynapses) 
	{
		super(noSynapses);
		initThreshold = 120*Math.sqrt(noSynapses);
	}
	public NeuronCellGreyDifference(NeuronCell cell) 
	{
		super(cell.weights.length);
		System.arraycopy(cell.weights, 0, weights, 0, weights.length);
	}
	
	public boolean isFiring(byte[] pixels)
	{
		output = output(pixels);
//		System.out.println(output);
//		System.out.println(output<1000/(noUpdates+1));
//		System.out.println(noUpdates);
//		System.out.println();
		//return output<750/Math.log((noUpdates+Math.E));
		return output<initThreshold/Math.log((noUpdates+Math.E));
	}
	
	public double output(byte[] pixels)
	{
		double sum=0;
		for (int i = 0; i < pixels.length; i++) 
		{
			int pixel = pixels[i];
			if (pixel<0)pixel+=255;
			double weight = weights[i]-pixel;
			sum+=weight*weight;//Math.abs(weight);

		}
		return Math.sqrt(sum);
	}
	
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
	
	public void modifyWeights(byte[] pixels)
	{
		for (int i = 0; i < pixels.length; i++) 
		{
			int pixel = pixels[i];
			if (pixel<0)pixel+=255;
			weights[i]=weights[i]+(pixel - weights[i])/2;
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
