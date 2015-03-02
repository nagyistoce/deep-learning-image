package home.mutant.probabilistic.mains.runnables;

import home.mutant.probabilistic.cells.ProbabilisticNeuron;
import home.mutant.probabilistic.nets.ProbabilisticNet;

public class OutputRunnableByThread implements Runnable 
{
	ProbabilisticNet net;
	Thread control;
	public long[] outputs = new long[10];
	
	public OutputRunnableByThread(ProbabilisticNet net, Thread control) 
	{
		super();
		this.net = net;
		this.control=control;
	}
	public void run() 
	{
		while(control.isAlive())
		{
			int indexNeuron = 784+(int) (Math.random()*net.X*(net.Y-28));
			ProbabilisticNeuron neuron = net.neurons.get(indexNeuron);
			if (neuron==null || neuron.output==0) continue;
//			System.out.println(neuron.output);
			indexNeuron = (indexNeuron-784)/280;
			outputs[indexNeuron]+=neuron.output;
		}
		//System.out.println(Arrays.toString(outputs));
	}

}
