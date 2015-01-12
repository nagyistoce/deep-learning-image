package home.mutant.liquid.networks;

import home.mutant.liquid.cells.NeuronCell;
import home.mutant.liquid.cells.NeuronCellGrey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FeedForward
{
	public final static int NO_SYNAPSES=2;
	public List<List<NeuronCell>> layers = new ArrayList<List<NeuronCell>>();
	
	public FeedForward(int noLayers, int NoNeuronsPerLayer) 
	{
		for(int i = 0; i<noLayers;i++)
		{
			List<NeuronCell> layer = new ArrayList<NeuronCell>();
			for (int j=0;j<NoNeuronsPerLayer;j++)
			{
				layer.add(new NeuronCellGrey(NO_SYNAPSES));
			}
			layers.add(layer);
		}
	}
	public void step(double[] input)
	{
		for (int noLayer=layers.size()-1;noLayer>0;noLayer--) 
		{
			List<NeuronCell> layer = layers.get(noLayer);
			List<NeuronCell> layerBelow = layers.get(noLayer-1);
			for (int noNeuron=0;noNeuron<layerBelow.size()-NO_SYNAPSES+1;noNeuron++)
			{
				layer.get(noNeuron).outputProbability(layerBelow.subList(noNeuron, noNeuron+NO_SYNAPSES));
			}
		}
		
		List<NeuronCell> layer0 = layers.get(0);
		for (int noNeuron=0;noNeuron<input.length-NO_SYNAPSES+1;noNeuron++)
		{
			layer0.get(noNeuron).outputProbability(Arrays.copyOfRange(input, noNeuron, noNeuron+NO_SYNAPSES));
		}
	}
}
