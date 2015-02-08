package home.mutant.liquid.networks;

import home.mutant.liquid.cells.NeuronCell;
import home.mutant.liquid.cells.NeuronCellGrey;

import java.util.ArrayList;
import java.util.List;

public class DoubleNet extends SimpleNet
{
	public List<NeuronCell> neurons2 = new ArrayList<NeuronCell>();
	
	public DoubleNet(int noNeurons, int noSynapses) 
	{
		super(noNeurons, noSynapses);
		for(int i = 0; i<noNeurons;i++)
		{
			neurons2.add(new NeuronCellGrey(noSynapses));
		}
	}

	public DoubleNet()
	{
		// TODO Auto-generated constructor stub
	}
	
}
