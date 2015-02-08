package home.mutant.liquid.runnable;

import home.mutant.liquid.cells.NeuronCell;

import java.util.List;

public class OutputDoubleNeuronsRunnable implements Runnable
{
	public OutputDoubleNeuronsRunnable(List<NeuronCell> net) 
	{
		super();
		this.net = net;
	}

	List<NeuronCell> net;
	List<NeuronCell> net2;
	
	@Override
	public void run() 
	{
		for (NeuronCell neuron : net)
		{

		}	
	}

}
