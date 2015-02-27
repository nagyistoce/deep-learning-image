package home.mutant.probabilistic.cells;

import java.util.ArrayList;
import java.util.List;

public class ProbabilisticNeuron 
{
	public List<ProbabilisticNeuron> links = new ArrayList<ProbabilisticNeuron>();
	List<Integer> occurences = new ArrayList<Integer>();
	int totalSamples = 0;
	public int output;
	
	public ProbabilisticNeuron pickLink()
	{
		int size = links.size();
		if (size>100000)
		{
			links.remove(0);
			size--;
		}
		if (size==0) return null;
		int index = (int) (Math.random()*3*size);
		if (index>=size) return null;
		return links.get(index);
	}
}
