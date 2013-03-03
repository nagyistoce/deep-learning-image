package home.mutant.deep.model;


import home.mutant.deep.utils.MathUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwoFullConnectedLayers 
{
	public double[][] weights = null;
	public double LEARNING_RATE = 100.;
	public Map<Integer, String> generatives = new HashMap<Integer, String>();
	
	public TwoFullConnectedLayers(int inputLayerNeurons, int outputLayerNeuron)
	{
		weights = new double[inputLayerNeurons][outputLayerNeuron];
		randomize();
	}
	private void randomize()
	{
		for (int i=0; i< weights.length;i++)
		{
			for (int j=0; j< weights[0].length;j++)
			{
				weights[i][j] = (Math.random()-0.5)*100;
			}
		}
	}
	public double[] generateSample()
	{
		return generateSampleNoBias((int) (Math.random()*weights.length));
	}

	private double[] generateSampleNoBias(int softMaxIndex)
	{
		double[] input = new double[weights.length]; // all are zeros
		input[softMaxIndex] = 1.;
		
		double[] output;
		output = new double[weights[0].length];
		for (int out=0;out<output.length;out++)
		{
			output[out]=0.;
			for (int in=0;in<input.length;in++)
			{
				output[out]+=input[in]*weights[in][out];
			}
			output[out] = MathUtils.sigmoid(output[out]);
		}
		return output;
	}
	
	public void learnStepMaxGenerative(List<byte[]> images)
	{
		int softMaxIndex = (int) (Math.random()*weights.length);
		double[] output = generateSampleNoBias(softMaxIndex);
		
		int max=Integer.MIN_VALUE;
		int indexMax = 0;
		//System.out.println("Values");
		for (int img = 0;img<images.size();img++)
		{
			int tmpCoincidence = MathUtils.coincidence(images.get(img), output);
			//System.out.print(tmpCoincidence + " ");
			if (tmpCoincidence>max)
			{
				max = tmpCoincidence;
				indexMax = img;
			}
		}
		//System.out.println("");
		for (int i = 0; i < output.length; i++)
		{
			if ((Double.valueOf(output[i]).byteValue())!=images.get(indexMax)[i])
			{
				if (output[i]==1)
				{
					weights[softMaxIndex][i]-=LEARNING_RATE ;
				}
				else
				{
					weights[softMaxIndex][i]+=LEARNING_RATE ;
				}
			}
		}
		String code = generatives.get(softMaxIndex);
		if (code == null) code="";
		generatives.put(softMaxIndex, code+" "+indexMax);
		//System.out.println("The neuron "+softMaxIndex+" generated the image "+indexMax);
	}

	public int test(byte[] bs)
	{
		double[] output = forwardStep(bs, false);
		return MathUtils.indexMax(output);
	}

	private double[] forwardStep(byte[] bs, boolean applySigmoid)
	{
		double[] input = new double[weights[0].length];
		for (int i = 0; i < input.length; i++)
		{
			input[i] = new Double(bs[i]);
		}
		double[] output = null;

		output = forwardStepLevel(input);
		if (applySigmoid)
		{
			MathUtils.sigmoid(output);
		}
		return output;
	}

	private double[] forwardStepLevel(double[] input)
	{
		double[] output;
		output = new double[weights.length];
		for (int out=0;out<output.length;out++)
		{
			output[out]=0.;
			for (int in=0;in<input.length;in++)
			{
				output[out]+=input[in]*weights[out][in];
			}
		}
		return output;
	}

	public void printGeneratives()
	{
		System.out.println("Printing generatives...");
		for(Integer neuron:generatives.keySet())
		{
			System.out.println("The neuron "+neuron+" generated the image " + generatives.get(neuron));
		}
	}
}