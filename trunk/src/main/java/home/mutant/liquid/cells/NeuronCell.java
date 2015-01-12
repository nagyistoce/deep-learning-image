package home.mutant.liquid.cells;

import java.util.ArrayList;
import java.util.List;

import home.mutant.deep.ui.Image;
import home.mutant.deep.utils.MathUtils;

public abstract class NeuronCell
{
	public double[] weights = null;
	public double output = 0;
	protected int noUpdates=0;
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
			//if (Math.random()>.6)
				weights[i]=0;//Math.random()*256-128;
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
	
	public abstract double output(byte[] pixels);
	public abstract boolean isFiring(byte[] pixels);
	public abstract void modifyWeights(byte[] pixels);
	public abstract double getDistanceFromImage(byte[] pixels);
	public abstract double getDistanceFromNeuron(NeuronCell neuron);
}
