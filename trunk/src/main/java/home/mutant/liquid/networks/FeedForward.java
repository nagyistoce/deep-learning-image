package home.mutant.liquid.networks;

import home.mutant.liquid.cells.NeuronCell;

import java.util.ArrayList;
import java.util.List;

public class FeedForward
{
	List<List<NeuronCell>> layers = new ArrayList<List<NeuronCell>>();
	
	public FeedForward(int noLayers, int NoNeuronsPerLayer) 
	{
		for(int i = 0; i<noLayers;i++)
		{
			List<NeuronCell> layer = new ArrayList<NeuronCell>();
			for (int j=0;j<NoNeuronsPerLayer;j++)
			{
				layer.add(new NeuronCell(4));
			}
			layers.add(layer);
		}
	}
}
