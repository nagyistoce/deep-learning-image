package home.mutant.deep.model;


import home.mutant.deep.utils.MathUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwoFullConnectedLayers implements RecognizerGenerator
{
	public double[][] weights = null;
	public double LEARNING_RATE = 100.;
	public Map<Integer, String> generatives = new HashMap<Integer, String>();
	
	public TwoFullConnectedLayers(int topLayerNeurons, int bottomLayerNeuron)
	{
		weights = new double[topLayerNeurons][bottomLayerNeuron];
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
	
	public Image generateSampleFromInput(Image input) 
	{
		double[] output;
		output = new double[weights[0].length];
		for (int out=0;out<output.length;out++)
		{
			output[out]=0.;
			for (int in=0;in<input.getDataOneDimensional().length;in++)
			{
				output[out]+=input.getDataOneDimensional()[in]*weights[in][out];
			}
			output[out] = MathUtils.sigmoid(output[out]);
		}
		return new Image(output);
	}
	
	public Image generateSample()
	{
		return generateSampleNoBias((int) (Math.random()*weights.length));
	}

	private Image generateSampleNoBias(int softMaxIndex)
	{
		double[] input = new double[weights.length]; // all are zeros
		input[softMaxIndex] = 1.;
		return generateSampleFromInput(new Image(input,weights.length,1));
	}
	
	public void learnMaxGenerative(int generations, List<Image> images)
	{
		for (int i=0; i<generations;i++)
		{
			learnStepMaxGenerative(images);
		}
	}
	public void learnStepMaxGenerative(List<Image> images)
	{
		int softMaxIndex = (int) (Math.random()*weights.length);
		byte[] output = generateSampleNoBias(softMaxIndex).getDataOneDimensional();
		
		int max=Integer.MIN_VALUE;
		int indexMax = 0;
		//System.out.println("Values");
		for (int img = 0;img<images.size();img++)
		{
			int tmpCoincidence = MathUtils.coincidence(images.get(img).getDataOneDimensional(), output);
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
			if (output[i]!=images.get(indexMax).getDataOneDimensional()[i])
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

	public int test(Image bs)
	{
		double[] output = forwardStep(bs.getDataOneDimensional(), false);
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

	public  byte[] forwardStep(byte[] bs)
	{
		double[] output = forwardStep(bs, false);
		int indexMax = MathUtils.indexMax(output);
		byte[] outByte = new byte[output.length]; 
		outByte[indexMax] = 1;
		return outByte;
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