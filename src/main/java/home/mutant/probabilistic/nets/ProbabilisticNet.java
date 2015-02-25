package home.mutant.probabilistic.nets;

import java.util.HashMap;
import java.util.Map;

import home.mutant.deep.ui.Image;
import home.mutant.probabilistic.cells.ProbabilisticNeuron;


public class ProbabilisticNet 
{
	public int X;
	public int Y;
	public Map<Integer, ProbabilisticNeuron> neurons = new HashMap<Integer, ProbabilisticNeuron>();
	public ProbabilisticNeuron lastActivated = null;
	public Image image;
	public ProbabilisticNet(int x, int y) 
	{
		super();
		X = x;
		Y = y;
		
	}
	public Image generateImage()
	{
		image = new Image(X, Y);
		for (int x=0;x<X;x++)
		{
			for (int y=0;y<Y;y++)
			{
				ProbabilisticNeuron neuron = neurons.get(y*X+x);
				if (neuron!=null && neuron.output>0)
				{
					image.setPixel(x, y, (byte) 255);
				}
			}
		}
		return image;
	}
	public Image generateSample()
	{
		int count=0;
		ProbabilisticNeuron neuron=null;
		for(Integer key:neurons.keySet())
		{
			neurons.get(key).output=0;
		}
		while (neuron==null)
		{
			neuron = neurons.get((int)(Math.random()*X*Y));
		}
		while(count<1000)
		{
			ProbabilisticNeuron neuronPicked = neuron.pickLink();
			if (neuronPicked!=null) 
			{
				neuronPicked.output=255;
				count++;
			}
		}
		return generateImage();
	}
}
