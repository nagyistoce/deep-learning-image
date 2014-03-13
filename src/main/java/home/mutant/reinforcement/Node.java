package home.mutant.reinforcement;

import java.util.ArrayList;
import java.util.List;

public class Node 
{
	public double value;
	public double sumValues=0;
	public int countUpdates=0;
	public double reward;
	public List<Node> children = null;
	
	public void addChildren(Node child)
	{
		if (children ==null)
			children = new ArrayList<Node>();
		children.add(child);
	}
}
