package home.mutant.probabilistic.cells;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProbabilisticNeuron implements Serializable
{
	public int X;
	public int Y;
	public List<ProbabilisticNeuron> links = new ArrayList<ProbabilisticNeuron>();
	public Map<ProbabilisticNeuron, Integer> correlations = new HashMap<ProbabilisticNeuron,Integer>();
	List<Integer> occurences = new ArrayList<Integer>();
	int totalSamples = 0;
	public int output;
	
	public ProbabilisticNeuron(int x, int y) 
	{
		super();
		X = x;
		Y = y;
	}

	public ProbabilisticNeuron pickLink()
	{
		int size = links.size();
		if (size>1000000)
		{
			links.remove(0);
			size--;
		}
		if (size==0) return null;
		int index = (int) (Math.random()*10*size);
		if (index>=size) return null;
		return links.get(index);
	}
}
