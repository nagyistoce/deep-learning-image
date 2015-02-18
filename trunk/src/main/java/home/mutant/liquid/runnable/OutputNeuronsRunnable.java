package home.mutant.liquid.runnable;

import home.mutant.liquid.cells.NeuronCell;

import java.util.List;

public class OutputNeuronsRunnable implements Runnable
{
	public OutputNeuronsRunnable(List<NeuronCell> net) 
	{
		super();
		this.net = net;
	}

	public List<byte[]> subImages;
	List<NeuronCell> net;
	
	@Override
	public void run() 
	{
		for (byte[] subImage : subImages) 
		{
			for (NeuronCell neuron : net)
			{
				//if (neuron.isFiring(subImage))
				//{
					neuron.modifyWeights(subImage);
				//}
			}
		}
	}

}
