package home.mutant.logistic;


public class LogisticNet 
{
	public LogisticNeuron[] neurons;
	public LogisticNet(int noNeurons, int noSynapse)
	{
		neurons = new LogisticNeuron[noNeurons];
		for (int i = 0; i < noNeurons; i++) 
		{
			neurons[i] = new LogisticNeuron(noSynapse);
		}
	}
}
