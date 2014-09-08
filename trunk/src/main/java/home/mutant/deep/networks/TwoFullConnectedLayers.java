package home.mutant.deep.networks;


import home.mutant.deep.abstracts.Neuron;
import home.mutant.deep.model.IndexValue;
import home.mutant.deep.ui.Image;
import home.mutant.deep.utils.MathUtils;

import java.lang.reflect.Array;
import java.util.List;

public class TwoFullConnectedLayers
{
	public Neuron[] neurons;
	int firstFreeSlot = 0;
	public int windowStart = 0;
	public int windowSize = 3000;

	public TwoFullConnectedLayers(int topLayerNeurons, int bottomLayerNeuron, Class<? extends Neuron> clazz)
	{
		neurons = (Neuron[])Array.newInstance(clazz, topLayerNeurons);
		try 
		{
			for (int i = 0; i < neurons.length; i++) 
			{
				neurons[i] = clazz.getConstructor(int.class).newInstance(bottomLayerNeuron);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void randomize()
	{
		for (int i = 0; i < neurons.length; i++) {
			neurons[i].randomize();
		}
	}
	
	public Image generateSample()
	{
		return neurons[(int) (Math.random()*neurons.length)].generateSample();
	}

	public void initWeightsFromImages(List<Image> images)
	{
		for (int indexNeuron=0;indexNeuron<neurons.length; indexNeuron++)
		{
			if (images.size()<=indexNeuron)
			{
				return;
			}
			neurons[indexNeuron].initWeightsFromImage(images.get(indexNeuron));
		}
	}

	public void initWeightsFromOneImage(Image image, int indexNeuron)
	{
		neurons[indexNeuron].initWeightsFromImage(image);
	}
	public int test(Image bs)
	{
		double[] output = forwardStep(bs, false);
		return MathUtils.indexMax(output);
	}

	public double[] forwardStep(Image image, boolean applySigmoid)
	{
		double[] output = new double[neurons.length];
		for (int i = 0; i < neurons.length; i++) {
			output[i] = neurons[i].calculateOutput(image);
		}
//		if (applySigmoid)
//		{
//			MathUtils.sigmoid(output,760);
//		}
		double[] outputTmp = new double[neurons.length];
		System.arraycopy(output, 0, outputTmp, 0, output.length);
		for (int threshold = 780;threshold>700;threshold--)
		{
			System.arraycopy(outputTmp, 0, output, 0, output.length);
			Integer samples = noSamplesForThreshold(output, threshold);
			if (samples>0)
			{
				System.out.println(samples+" "+threshold);
				return output;
			}
		}
		return output;
	}

	public int forwardStepIndex(Image image)
	{
		double[] output = new double[neurons.length];
		for (int i = 0; i < neurons.length; i++) {
			output[i] = neurons[i].calculateOutput(image);
		}
		return MathUtils.indexMax(output);
	}
	
	public IndexValue forwardStepIndexValue(Image image)
	{
		double[] output = new double[neurons.length];
		for (int i = 0; i < neurons.length; i++) {
			output[i] = neurons[i].calculateOutput(image);
		}
		return MathUtils.indexValueMax(output);
	}
	
	public List<Integer> forwardStepMultipleIndex(Image image, int numberIndexes)
	{
		double[] output = new double[neurons.length];
		for (int i = 0; i < neurons.length; i++) {
			output[i] = neurons[i].calculateOutput(image);
		}
		return MathUtils.indexMaxMultiple(numberIndexes,output);
	}
	
	public List<IndexValue> forwardStepMultipleIndexWithValues(Image image, int numberIndexes)
	{
		double[] output = new double[neurons.length];
		for (int i = 0; i < neurons.length; i++) {
			output[i] = neurons[i].calculateOutput(image);
		}
		return MathUtils.indexMaxMultipleWithValues(numberIndexes,output);
	}
	
	public long forwardStepMax(Image image)
	{
		long[] output = new long[neurons.length];
		for (int i = 0; i < neurons.length; i++) {
			output[i] = neurons[i].calculateOutput(image);
		}
		return MathUtils.max(output);
	}
	
	public Image forwardStepImageMax(Image image)
	{
		long [] output = new long[neurons.length];
		for (int i = 0; i < neurons.length; i++) {
			output[i] = neurons[i].calculateOutput(image);
		}
		int indexMax = MathUtils.indexMax(output);
		byte[] outByte = new byte[output.length]; 
		outByte[indexMax] = 1;
		return new Image(outByte);
	}

	public Image forwardStepImageThreshold(Image image, int threshold)
	{
		byte[] outByte = new byte[neurons.length]; 
		for (int i = 0; i < neurons.length; i++) {
			if(neurons[i].calculateOutput(image)>threshold)
			{
				outByte[i] = 1;
			}
		}
		return new Image(outByte);
	}
	
	public Image forwardStepImageLearning(Image image, int indexImage)
	{
		byte[] outByte = new byte[neurons.length];
		double exp = 0.15;
		for (int i = 0; i < neurons.length; i++) 
		{
//			if (i>windowStart && i<windowStart+windowSize)
//			{
//				exp = 30.;
//			}
//			else
//			{
//				exp = 0.075;
//			}
			if(MathUtils.abruptDistrib(exp)<neurons[i].calculateOutputProbability(image))
			{
				if (neurons[i].getOutputIndex()==-1)
					neurons[i].setOutputIndex(indexImage);
//				if (neurons[i].getOutputIndex()!=indexImage)
//					continue;
				
				outByte[i] = (byte)255;
				neurons[i].updateWeightsFromImage(image);
			}
			else
			{
				//if (Math.random()>0.9)
					//neurons[i].decayWeights(image);
			}
		}
		return new Image(outByte);
	}
	
	private Integer noSamplesForThreshold(double[] output, Integer threshold)
	{
		Integer ret=0;
		for (int i = 0; i < output.length; i++) {
			if (output[i]>threshold)
			{
				output[i]=1;
				ret++;
			}
			else
				output[i]=0;
		}
		return ret;
	}
	public Image forwardStep(Image bs)
	{
		double[] output = forwardStep(bs, false);
		int indexMax = MathUtils.indexMax(output);
		byte[] outByte = new byte[output.length]; 
		outByte[indexMax] = 1;
		return new Image(outByte);
	}
	public void initWeightsFromOneImage(Image image) 
	{
		neurons[firstFreeSlot].initWeightsFromImage(image);
		firstFreeSlot++;
		if (firstFreeSlot>=neurons.length)
		{
			firstFreeSlot=0;
		}
	}
}