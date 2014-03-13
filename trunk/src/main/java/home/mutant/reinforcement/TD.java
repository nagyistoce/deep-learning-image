package home.mutant.reinforcement;

import java.util.Random;

public class TD 
{
	public static final double EPSILON = 0.005;
	public static void evaluateNodes(int noCycles, Node root)
	{
		Random rnd = new Random(System.currentTimeMillis());
		Node start=root;
		
		for (int i=0;i<noCycles;i++)
		{
			if (start.children==null)
			{
				start = root;
			}
			Node next=start.children.get(rnd.nextInt(start.children.size()));
			start.sumValues += start.value;
			start.countUpdates++;
			start.value+= EPSILON*(start.reward+next.value-start.value);
			start=next;
		}
	}
}
