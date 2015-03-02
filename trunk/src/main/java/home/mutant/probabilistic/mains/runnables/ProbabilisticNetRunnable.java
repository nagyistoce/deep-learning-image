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
			
			//if(neuron.output<0)neuron.output = 0;
			//System.out.println(neuron.output);
			if (neuron.output>0)
			{
				if (lastNeuron!=null)
				{
					
					if (neuron.Y>=28)
						lastNeuron.links.add(neuron);
					else
					{
						if(lastNeuron.Y<28)
						{
							int x= neuron.X-lastNeuron.X;
							int y= neuron.Y-lastNeuron.Y;
							double radius = Math.sqrt(x*x+y*y);
							if (radius<28)
								lastNeuron.links.add(neuron);
						}
					}
					

				}
				
				lastNeuron = neuron;
			}
			neuron.output-=80;
			if (neuron.output<0)neuron.output=0;
			neuron = neuron.pickLink();
			if (neuron == null) continue;
			//System.out.println("PICKED" + neuron);
			neuron.output+=60;
			if (neuron.output>255)
				neuron.output=255;
			neuron=null;
		}
	}

}
