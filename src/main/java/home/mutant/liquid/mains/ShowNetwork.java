package home.mutant.liquid.mains;

import home.mutant.deep.ui.ResultFrame;
import home.mutant.liquid.networks.FeedForward;

public class ShowNetwork 
{
	public static void main(String[] args)
	{
		ResultFrame frame = new ResultFrame(1200, 600);
		FeedForward net = new FeedForward(500, 500);
		double[] input = new double[500];
		for (int i = 0; i < input.length; i++) 
		{
			input[i]=Math.random();
		}
		while(true)
		{
			for (int i = 0; i < input.length; i++) 
			{
				input[i]= Math.random()-0.5;
			}
			net.step(input);
			frame.showNetwork(net, 0, 0);
		}
			
	}
}
