package home.mutant.probabilistic.mains.runnables;

import home.mutant.probabilistic.cells.ProbabilisticNeuron;
import home.mutant.probabilistic.mains.RunProbabilisticNet;
import home.mutant.probabilistic.nets.ProbabilisticNet;

public class OutputRunnable implements Runnable 
{
	ProbabilisticNet net;
	private int epochs;
	public long[] outputs = new long[10];
	
	public OutputRunnable(ProbabilisticNet net, int epochs) 
	{
		super();
		this.net = net;
		this.epochs=epochs;
	}
	public void run() 
	{
		int epoch=0;
		while(epoch++<epochs)
		{
			int indexNeuron = RunProbabilisticNet.IMAGE_SIZE*RunProbabilisticNet.IMAGE_SIZE+(int) (Math.random()*net.X*(net.Y-RunProbabilisticNet.IMAGE_SIZE));
			ProbabilisticNeuron neuron = net.neurons.get(indexNeuron);
			if (neuron==null || neuron.output==0) continue;
//			System.out.println(neuron.output);
			indexNeuron = (indexNeuron-RunProbabilisticNet.IMAGE_SIZE*RunProbabilisticNet.IMAGE_SIZE)/(RunProbabilisticNet.IMAGE_SIZE*10);
			outputs[indexNeuron]+=neuron.output;
		}
		//System.out.println(Arrays.toString(outputs));
	}

}
