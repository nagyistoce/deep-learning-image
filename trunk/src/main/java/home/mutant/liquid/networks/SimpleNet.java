package home.mutant.liquid.networks;

import home.mutant.liquid.cells.NeuronCell;
import home.mutant.liquid.cells.NeuronCellGrey;

import java.util.ArrayList;
import java.util.List;

public class SimpleNet
{
	public List<NeuronCell> neurons = new ArrayList<NeuronCell>();
	
	public SimpleNet(int noNeurons, int noSynapses) 
	{
		for(int i = 0; i<noNeurons;i++)
		{
			neurons.add(new NeuronCellGrey(noSynapses));
		}
	}

	public SimpleNet()
	{
		// TODO Auto-generated constructor stub
	}
	
	public void sortNeuronsByDistance()
	{
		List<NeuronCell> newList = new ArrayList<NeuronCell>();
		newList.add(neurons.get(0));
		for(int i=1;i<neurons.size();i++)
		{
			int minIndex = findMinIndex(neurons.get(i), newList);
			if (minIndex>=0)
			{
				newList.add(minIndex, neurons.get(i));
			}
		}
		neurons = newList;
	}
	
	private int findMinIndex(NeuronCell neuron, List<NeuronCell> list)
	{
		int minIndex=-1;
		double min = 1000000;
		for(int i=0;i<list.size();i++)
		{
			double difference = neuron.output(list.get(i));
			if (difference<min)
			{
				minIndex = i;
				min = difference;
			}
		}
		if (min<50)
			return -1;
		return minIndex;
	}
}
