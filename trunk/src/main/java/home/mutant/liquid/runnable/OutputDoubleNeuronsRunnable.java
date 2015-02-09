package home.mutant.liquid.runnable;

import home.mutant.liquid.cells.NeuronCell;

import java.util.List;

public class OutputDoubleNeuronsRunnable implements Runnable
{
	public OutputDoubleNeuronsRunnable(List<NeuronCell> netFrom, List<NeuronCell> netTo, int offsetNeuronChunk) 
	{
		super();
		this.netFrom = netFrom;
		this.netTo = netTo;
		this.offsetNeuronChunk = offsetNeuronChunk;
	}

	List<NeuronCell> netFrom;
	List<NeuronCell> netTo;
	int offsetNeuronChunk;
	
	@Override
	public void run() 
	{
		int offset = offsetNeuronChunk-1;
		for (NeuronCell neuronTo : netTo)
		{
			offset++;
			if (offset==0 || offset>=netFrom.size()-1) continue;
			NeuronCell neuronFrom = netFrom.get(offset);
			NeuronCell neuronFrom2 = netFrom.get(offset+1);
			NeuronCell neuronFrom3 = netFrom.get(offset-1);
			for (int i = 0; i < neuronFrom.weights.length; i++)
			{
				neuronTo.weights[i] = (1*neuronFrom.weights[i]+neuronFrom2.weights[i]+ neuronFrom3.weights[i])/3;
			}
		}	
	}

}
