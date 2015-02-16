package home.mutant.liquid.cells;

import java.util.ArrayList;
import java.util.List;

import home.mutant.deep.ui.Image;
import home.mutant.deep.utils.MathUtils;

public abstract class NeuronCell implements Comparable<NeuronCell>
{
	public double[] weights = null;
	public double output = 0;
	public int noUpdates=0;
	public List<Integer> recognized = new ArrayList<Integer>();
	public int lastRecognized = -1;
	protected double threshold=0;
	
	public double minDistance;
	public int indexNeuronMinDistance = -1;
	public int xShow = 950;
	public int yShow = 540;
	
	public NeuronCell(int noSynapses)
	{
		weights = new double[noSynapses];
		for (int i=0;i<noSynapses;i++)
		{
			weights[i]=128;
			if (Math.random()>.7)
				weights[i]=Math.random()*256;
		}
	}
	public double outputProbability(double[] input)
	{
		double sum=0;
		for (int i = 0; i < input.length; i++) 
		{
			sum+=input[i]*weights[i];
		}
		output = MathUtils.sigmoidFunction(sum)-0.5;
		return output;
	}
	public double outputProbability(List<NeuronCell> input)
	{
		double sum=0;
		for (int i = 0; i < input.size(); i++) 
		{
			sum+=input.get(i).output*weights[i];
		}
		output = MathUtils.sigmoidFunction(sum)-0.5;
		return output;
	}
	public double outputProbability(Image image)
	{
		double sum=0;
		byte[] pixels = image.getDataOneDimensional();
		for (int i = 0; i < pixels.length; i++) 
		{
			sum+=(pixels[i]*(-1))*weights[i];
		}
		output = MathUtils.sigmoidFunction(sum);
		return output;
	}
	public int compareTo(NeuronCell other)
	{
		byte [] zeroPixels = new byte[weights.length];
		double o1 = output(zeroPixels);
		return (int)(o1-other.output(zeroPixels));
	}

	public abstract double output(byte[] pixels);
	public abstract double output(NeuronCell neuron);
	public abstract boolean isFiring(byte[] pixels);
	public abstract void modifyWeights(byte[] pixels);
	public abstract double getDistanceFromImage(byte[] pixels);
	public abstract double getDistanceFromNeuron(NeuronCell neuron);
}
