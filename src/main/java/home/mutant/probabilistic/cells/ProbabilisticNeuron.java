package home.mutant.probabilistic.cells;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProbabilisticNeuron implements Serializable
{
	private static final long serialVersionUID = 1L;
	public int X;
	public int Y;
	public List<ProbabilisticNeuron> links = new ArrayList<ProbabilisticNeuron>();
	public List<ProbabilisticNeuron> nonLinks = new ArrayList<ProbabilisticNeuron>();
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
		int index = (int) (Math.random()*1*size);
		if (index>=size) return null;
		ProbabilisticNeuron ret =  links.get(index);
//		if (ret.output>0)
//			links.remove(ret);
		return ret;
	}
	
	public ProbabilisticNeuron pickNonLink()
	{
		int size = nonLinks.size();
		if (size>1000000)
		{
			nonLinks.remove(0);
			size--;
		}
		if (size==0) return null;
		int index = (int) (Math.random()*1*size);
		if (index>=size) return null;
		ProbabilisticNeuron ret =  nonLinks.get(index);
//		if (ret.output>0)
//			links.remove(ret);
		return ret;
	}
	
}
