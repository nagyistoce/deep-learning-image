package home.mutant.probabilistic.mains.runnables;

import home.mutant.probabilistic.cells.ProbabilisticNeuron;
import home.mutant.probabilistic.nets.ProbabilisticNet;

public class ProbabilisticNetRunnable implements Runnable 
{
	ProbabilisticNet net;
	
	public ProbabilisticNetRunnable(ProbabilisticNet net) 
	{
		super();
		this.net = net;
	}
	@Override
	public void run() 
	{
		ProbabilisticNeuron neuron = null;
		ProbabilisticNeuron lastNeuron = null;
		while(true)
		{
			if (neuron==null)
			{
				int indexNeuron = (int) (Math.random()*net.X*net.Y);
				neuron = net.neurons.get(indexNeuron);
			}

			if (neuron==null || neuron.output==0) 
			{
				neuron = null;
				continue;
			}
			
			neuron.output-=20;
			if(neuron.output<0)neuron.output = 0;
			//System.out.println(neuron.output);
			if (lastNeuron!=null && lastNeuron.output>0 && neuron.output>0)
			{
				lastNeuron.links.add(neuron);
			}
			lastNeuron = neuron;
			neuron = neuron.pickLink();
			if (neuron == null) continue;
			//System.out.println("PICKED" + neuron);
			neuron.output+=10;
			if (neuron.output>255)
				neuron.output=255;
			neuron=null;
		}
	}

}
