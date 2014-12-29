package home.mutant.liquid.networks;

import home.mutant.liquid.cells.NeuronCell;

import java.util.ArrayList;
import java.util.List;

public class SimpleNet
{
	public List<NeuronCell> neurons = new ArrayList<NeuronCell>();
	
	public SimpleNet(int noNeurons, int noSynapses) 
	{
		for(int i = 0; i<noNeurons;i++)
		{
			neurons.add(new NeuronCell(noSynapses));
		}
	}

	public SimpleNet()
	{
		// TODO Auto-generated constructor stub
	}
}
