package home.mutant.liquid.cells;

import home.mutant.deep.utils.MathUtils;

public class NeuronCellGreyDifference extends NeuronCell
{
	double initThreshold = 0;
	double maxThreshold = 0;
	public NeuronCellGreyDifference(int noSynapses) 
	{
		super(noSynapses);
		initThreshold = 100*Math.sqrt(noSynapses);
		maxThreshold = 255*Math.sqrt(noSynapses);
		threshold = initThreshold;
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
		//double prob = (maxThreshold-output)/maxThreshold;
		//System.out.println(prob);
		//return prob>1-0.5/Math.log((noUpdates+Math.E));
		threshold+= Math.random()*100-50;
		return output<threshold;// initThreshold/Math.log((noUpdates+Math.E));
		
		//System.out.println(prob*prob*prob*prob);
		//if (Math.random()<prob*prob*prob*prob*prob*prob) return true;
		//return false;
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
			weights[i]=(pixel + 2* weights[i])/3;
		}
		//threshold = MathUtils.sumSquared(weights);
		//if (noUpdates<20)
		noUpdates++;
		threshold=output;
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
