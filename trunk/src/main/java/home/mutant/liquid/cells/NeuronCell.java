package home.mutant.liquid.cells;

import java.util.ArrayList;
import java.util.List;

import home.mutant.deep.ui.Image;
import home.mutant.deep.utils.MathUtils;

public class NeuronCell
{
	public double[] weights = null;
	public double output = 0;
	private int noUpdates=0;
	public List<Integer> recognized = new ArrayList<Integer>();
	public int lastRecognized = -1;
	private double threshold=0;
	
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
	public double outputBW(Image image)
	{
		double sum=0;
		byte[] pixels = image.getDataOneDimensional();
		for (int i = 0; i < pixels.length; i++) 
		{
			int pixel = pixels[i]*-1;
			if (pixel==0)pixel=-1;
			sum+=(pixel)*weights[i];
		}
		return sum;
	}
	public boolean isFiringBW(Image image)
	{
		double output = outputBW(image);
		//System.out.println(output);
		double threshold = weights.length;
		threshold -=  threshold/(Math.pow(2, noUpdates));
		threshold*=0.82;
		//System.out.println(threshold);
		//System.out.println();
		return output>threshold;
	}
	public void modifyWeightsBW(Image image)
	{
		byte[] pixels = image.getDataOneDimensional();
		for (int i = 0; i < pixels.length; i++) 
		{
			int pixel = pixels[i]*-1;
			if (pixel==0)pixel=-1;
			weights[i]=(weights[i]+pixel)/2;
		}
		noUpdates++;
	}
	
	public double output(byte[] pixels)
	{
		double sum=0;
		for (int i = 0; i < pixels.length; i++) 
		{
			int pixel = pixels[i];
			if (pixel<0)pixel+=255;
			double weight = weights[i];
			if (weight==0) weight=-150;
			sum+=(pixel)*weight;
		}
		return sum;
	}
	public boolean isFiring(byte[] pixels)
	{
		output = output(pixels);
//		System.out.println(output);
//		System.out.println(threshold);
//		System.out.println();
		return output>=threshold;
	}
	
	public double getDistanceFromImage(byte[] pixels)
	{
		output = output(pixels);
		return threshold - output;
	}
	
	public void modifyWeights(byte[] pixels)
	{
		for (int i = 0; i < pixels.length; i++) 
		{
			int pixel = pixels[i];
			if (pixel<0)pixel+=255;
			weights[i]=(weights[i]+pixel);
		}
		threshold = MathUtils.sumSquared(weights);
	}
}
