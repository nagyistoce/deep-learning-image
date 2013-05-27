package home.mutant.deep.model;


import home.mutant.deep.utils.MathUtils;

import java.util.List;

public class TwoFullConnectedLayersBinary implements RecognizerGenerator
{
	public BinaryNeuron[] neurons;
	int firstFreeSlot = 0;
	public TwoFullConnectedLayersBinary(int topLayerNeurons, int bottomLayerNeuron)
	{
		neurons = new BinaryNeuron[topLayerNeurons];
		for (int i = 0; i < neurons.length; i++) {
			neurons[i] = new BinaryNeuron(bottomLayerNeuron);
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
			output[i] = neurons[i].calculateOutput(image.getDataBinary());
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
			output[i] = neurons[i].calculateOutput(image.getDataBinary());
		}
		return MathUtils.indexMax(output);
	}
	
	public long forwardStepMax(Image image)
	{
		long[] output = new long[neurons.length];
		for (int i = 0; i < neurons.length; i++) {
			output[i] = neurons[i].calculateOutput(image.getDataBinary());
		}
		return MathUtils.max(output);
	}
	
	public Image forwardStepImageMax(Image image)
	{
		long [] output = new long[neurons.length];
		for (int i = 0; i < neurons.length; i++) {
			output[i] = neurons[i].calculateOutput(image.getDataBinary());
		}
		int indexMax = MathUtils.indexMax(output);
		byte[] outByte = new byte[output.length]; 
		outByte[indexMax] = 1;
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
			System.out.println("CUCUCUC");
			firstFreeSlot=0;
		}
	}

}