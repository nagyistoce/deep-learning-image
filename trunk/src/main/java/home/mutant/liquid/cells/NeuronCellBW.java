package home.mutant.liquid.cells;


public class NeuronCellBW extends NeuronCell
{
	public NeuronCellBW(int noSynapses)
	{
		super(noSynapses);
	}

	public double output(byte[] pixels)
	{
		double sum=0;
		for (int i = 0; i < pixels.length; i++) 
		{
			int pixel = pixels[i]*-1;
			if (pixel==0)pixel=-1;
			sum+=(pixel)*weights[i];
		}
		return sum;
	}
	public boolean isFiring(byte[] pixels)
	{
		double output = output(pixels);
		//System.out.println(output);
		double threshold = weights.length;
		threshold -=  threshold/(Math.pow(2, noUpdates));
		threshold*=0.82;
		//System.out.println(threshold);
		//System.out.println();
		return output>threshold;
	}
	public void modifyWeights(byte[] pixels)
	{
		for (int i = 0; i < pixels.length; i++) 
		{
			int pixel = pixels[i]*-1;
			if (pixel==0)pixel=-1;
			weights[i]=(weights[i]+pixel)/2;
		}
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

	@Override
	public double output(NeuronCell neuron)
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
