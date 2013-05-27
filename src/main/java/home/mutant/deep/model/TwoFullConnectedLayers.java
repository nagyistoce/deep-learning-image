package home.mutant.deep.model;


import home.mutant.deep.utils.MathUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TwoFullConnectedLayers implements RecognizerGenerator
{
	public double[][] weights = null;
	public double LEARNING_RATE = 100;
	public Map<Integer, String> generatives = new HashMap<Integer, String>();
	private Set<Integer> doneNeurons = new HashSet<Integer>();
	public static final int THRESHOLD = 100;
	int rate = 100000;
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
				output[out]+=THRESHOLD*input.getDataOneDimensional()[in]*weights[in][out];
			}
			output[out] = MathUtils.sigmoid(output[out]-THRESHOLD/2);
		}
		return new Image(output);
	}
	
	public Image generateSample()
	{
		return generateSample((int) (Math.random()*weights.length));
	}

	public void initWeightsFromImages(List<Image> images)
	{
		for (int indexNeuron=0;indexNeuron<weights.length; indexNeuron++)
		{
			if (images.size()<=indexNeuron)
			{
				return;
			}
			for (int indexNeuronBottom=0;indexNeuronBottom<weights[indexNeuron].length; indexNeuronBottom++)
			{
				weights[indexNeuron][indexNeuronBottom] = images.get(indexNeuron).getDataOneDimensional()[indexNeuronBottom]==1?1:0;
			}
		}
	}
	
	public Image generateSample(int softMaxIndex)
	{
		softMaxIndex = softMaxIndex % weights.length;
		double[] input = new double[weights.length]; // all are zeros
		input[softMaxIndex] = 1.;
		return generateSampleFromInput(new Image(input,weights.length,1));
	}
	
	public void learnMaxGenerative(int generations, List<Image> images)
	{
		for (int i=0; i<generations;i++)
		{
			//learnStepMaxGenerative(images);
			//learnStepMaxGenerativeInverse(images);
			learnStepMaxGenerativeCombined(images);
			//System.out.println("Generation "+i+" done");
		}
	}
	public void learnStepMaxGenerative(List<Image> images)
	{
		int softMaxIndex = (int) (Math.random()*weights.length);
		byte[] output = generateSample(softMaxIndex).getDataOneDimensional();
		
		int max=Integer.MIN_VALUE;
		int indexMax = 0;
		rate++;
		for (int img = 0;img<images.size();img++)
		{
			int tmpCoincidence = MathUtils.coincidence(images.get(img).getDataOneDimensional(), output);
			//tmpCoincidence+= Math.random()*MathUtils.asymptoticToZero(rate);
			if (tmpCoincidence>max)
			{
				max = tmpCoincidence;
				indexMax = img;
			}
		}
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
	}

	public void learnStepMaxGenerativeCombined(List<Image> images)
	{
		int softMaxIndex = (int) (Math.random()*weights.length);
		if (doneNeurons.contains(softMaxIndex))
		{
			return;
		}
		byte[] output = generateSample(softMaxIndex).getDataOneDimensional();
		int imageIndex = (int) (Math.random()*images.size());
		
		int tmpCoincidence = MathUtils.coincidence(images.get(softMaxIndex).getDataOneDimensional(), output);
		System.out.println(tmpCoincidence);
/*		if (tmpCoincidence<0)
		{
			return;
		}*/
		if (tmpCoincidence>28*28-2)
		{
			String code = generatives.get(softMaxIndex);
			if (code == null) code="";
			generatives.put(softMaxIndex, code+" "+imageIndex);
			doneNeurons.add(softMaxIndex);
			//return;
		}
		for (int i = 0; i < output.length; i++)
		{
/*			if (output[i]!=images.get(imageIndex).getDataOneDimensional()[i])
			{
				if (output[i]==1)
				{
					weights[softMaxIndex][i]=-1000;//-=LEARNING_RATE;//*(tmpCoincidence+28*28) ;
				}
				else
				{
					weights[softMaxIndex][i]=1000;//+=LEARNING_RATE;//*(tmpCoincidence+28*28) ;
				}
			}*/
			if (images.get(imageIndex).getDataOneDimensional()[i]==1)
			{
				weights[softMaxIndex][i]=10000;
			}
			else
			{
				weights[softMaxIndex][i]=-10000;
			}
		}
	}
	
	public void learnStepMaxGenerativeInverse(List<Image> images)
	{
		int imageIndex = (int) (Math.random()*images.size());
		
		int indexNeuronMax = 0;
		int max=Integer.MIN_VALUE;
		byte[] output = null;
		rate++;
		for (int neuron = 0;neuron<weights.length;neuron++)
		{
			byte[] outputTmp = generateSample(neuron).getDataOneDimensional();
			int tmpCoincidence = MathUtils.coincidence(images.get(imageIndex).getDataOneDimensional(), outputTmp);
			tmpCoincidence+= Math.random()*MathUtils.asymptoticToZero(rate);
			if (tmpCoincidence>max)
			{
				max = tmpCoincidence;
				output = outputTmp;
				indexNeuronMax = neuron;
			}
		}

		for (int i = 0; i < output.length; i++)
		{
			if (output[i]!=images.get(imageIndex).getDataOneDimensional()[i])
			{
				if (output[i]==1)
				{
					weights[indexNeuronMax][i]-=LEARNING_RATE ;
				}
				else
				{
					weights[indexNeuronMax][i]+=LEARNING_RATE ;
				}
			}
		}
		String code = generatives.get(indexNeuronMax);
		if (code == null) code="";
		//System.out.println(indexNeuronMax +" "+  code+" "+indexNeuronMax);
		generatives.put(indexNeuronMax, code+" "+imageIndex);
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

	public Image forwardStep(Image bs)
	{
		double[] output = forwardStep(bs.getDataOneDimensional(), false);
		int indexMax = MathUtils.indexMax(output);
		byte[] outByte = new byte[output.length]; 
		outByte[indexMax] = 1;
		return new Image(outByte);
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