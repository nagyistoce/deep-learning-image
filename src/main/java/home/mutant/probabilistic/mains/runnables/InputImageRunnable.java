package home.mutant.probabilistic.mains.runnables;

import home.mutant.probabilistic.cells.ProbabilisticNeuron;
import home.mutant.probabilistic.nets.ProbabilisticNet;

public class InputImageRunnable implements Runnable 
{
	byte[] pixels;
	ProbabilisticNet net;
	private int epochs;
	
	public InputImageRunnable(byte[] pixels, ProbabilisticNet net, int epochs) 
	{
		super();
		this.pixels = pixels;
		this.net = net;
		this.epochs=epochs;
	}

	@Override
	public void run() 
	{
		int epoch=0;
		while(epoch++<epochs)
		{
			int indexNeuron = (int) (Math.random()*pixels.length);
			int pixel = pixels[indexNeuron];
			if (pixel<0)pixel+=255;
			if(pixel>0 && Math.random()>0.7)
			{
				ProbabilisticNeuron neuron = net.neurons.get(indexNeuron);
				if (neuron == null)
				{
					neuron = new ProbabilisticNeuron();
					net.neurons.put(indexNeuron, neuron);
				}
				neuron.output+=100;
				if (neuron.output>255)
					neuron.output=255;
			}
		}
		System.out.println("OUT InputThread");
	}

}
