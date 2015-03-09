package home.mutant.probabilistic.nets;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import home.mutant.deep.ui.Image;
import home.mutant.probabilistic.cells.ProbabilisticNeuron;


public class ProbabilisticNet implements Serializable
{
	public int X;
	public int Y;
	public Map<Integer, ProbabilisticNeuron> neurons = new HashMap<Integer, ProbabilisticNeuron>();
	public ProbabilisticNeuron lastActivated = null;
	public transient Image image;
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
			neuron = neurons.get((int)(Math.random()*X*X));
		}
		while(count++<10000)
		{
			ProbabilisticNeuron neuronPicked = neuron.pickLink();
			if (neuronPicked!=null) 
			{
				neuronPicked.output=255;
			}
		}
		return generateImage();
	}
	public void saveNet()
	{
		ObjectOutputStream oout=null;;
		
        try
        {
        	oout = new ObjectOutputStream( new FileOutputStream("1.net"));
        	oout.writeObject(this);
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
        finally
        {
        	try 
        	{
				if (oout!=null)oout.close();
			} 
        	catch (IOException e) 
        	{
				e.printStackTrace();
			}
        }
	}
}
