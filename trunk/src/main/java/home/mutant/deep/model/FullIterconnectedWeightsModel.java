package home.mutant.deep.model;


import home.mutant.deep.utils.MathUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullIterconnectedWeightsModel 
{
	public List<double[][]> weights = null;
	private boolean withBias = false;
	public double LEARNING_RATE = 100.;
	public Map<Integer, String> generatives = new HashMap<Integer, String>();
	
	public FullIterconnectedWeightsModel(List<Integer> numberNeuronsPerLayer, boolean withBias)
	{
		this.withBias = withBias;
		if (withBias)
			constructWithBias(numberNeuronsPerLayer);
		else
			constructWithoutBias(numberNeuronsPerLayer);
		randomize();
	}
	
	private void constructWithBias(List<Integer> numberNeuronsPerLayer)
	{
		if (numberNeuronsPerLayer.size()<1)
		{
			throw new IllegalArgumentException("There should be at least one layer");
		}
		
		List<Integer> cloneNumberNeurons = new ArrayList<Integer>(numberNeuronsPerLayer);
		cloneNumberNeurons.add(0, 0);
		
		weights = new ArrayList<double[][]>();
		for (int i = 0; i<cloneNumberNeurons.size()-1;i++)
		{
			double[][] layerWeights = new double[cloneNumberNeurons.get(i)+1][cloneNumberNeurons.get(i+1)];
			weights.add(layerWeights);
		}
	}
	
	private void constructWithoutBias(List<Integer> numberNeuronsPerLayer)
	{
		if (numberNeuronsPerLayer.size()<2)
		{
			throw new IllegalArgumentException("There should be at least two layers");
		}
		
		weights = new ArrayList<double[][]>();
		for (int i = 0; i<numberNeuronsPerLayer.size()-1;i++)
		{
			double[][] layerWeights = new double[numberNeuronsPerLayer.get(i)][numberNeuronsPerLayer.get(i+1)];
			weights.add(layerWeights);
		}
	}
	
	private void randomize()
	{
		for (int l = 0; l<weights.size();l++)
		{
			for (int i=0; i< weights.get(l).length;i++)
			{
				for (int j=0; j< weights.get(l)[0].length;j++)
				{
					weights.get(l)[i][j] = (Math.random()-0.5)*100;
				}
			}
		}
	}
	public double[] generateSample()
	{
		if (withBias)
			return generateSampleBias();
		else
		{
			return generateSampleNoBias();
		}
	}
	private double[] generateSampleBias()
	{
		double[] input = new double[1];
		input[0]=1.;
		double[] output = null;
		for (int l = 0; l<weights.size();l++)
		{
			output = new double[weights.get(l)[0].length+1];
			for (int out=0;out<output.length-1;out++)
			{
				output[out]=0.;
				for (int in=0;in<input.length;in++)
				{
					output[out]+=input[in]*weights.get(l)[in][out];
				}
				output[out] = MathUtils.sigmoid(output[out]);
			}
			input = output;
			input[input.length-1] = 1.;
		}
		input = new double[output.length-1];
		System.arraycopy(output, 0, input, 0, input.length);
		return input;
	}
	
	private double[] generateSampleNoBias()
	{
		return generateSampleNoBias((int) (Math.random()*weights.get(0).length), weights.size()-1);
	}

	private double[] generateSampleNoBias(int softMaxIndex)
	{
		return generateSampleNoBias(softMaxIndex, weights.size()-1);
	}

	private double[] generateSampleNoBias(int softMaxIndexes[])
	{
		return generateSampleNoBias(softMaxIndexes, weights.size()-1);
	}
	
	private double[] generateSampleNoBias(int softMaxIndexes[], int lastLevel)
	{
		
		double[] input = new double[weights.get(0).length]; // all are zeros
		for (int index : softMaxIndexes) 
		{
			input[index] = 1.;
		}
		
		for (int l = 0; l<=lastLevel;l++)
		{
			input = generateOneLevelSample(input, l);
		}
		return input;
	}
	
	private double[] generateSampleNoBias(int softMaxIndex, int lastLevel)
	{
		
		double[] input = new double[weights.get(0).length]; // all are zeros
		input[softMaxIndex] = 1.;
		for (int l = 0; l<=lastLevel;l++)
		{
			input = generateOneLevelSample(input, l);
		}
		return input;
	}
	
	private double[] generateOneLevelSample(double[] input, int l)
	{
		double[] output;
		output = new double[weights.get(l)[0].length];
		for (int out=0;out<output.length;out++)
		{
			output[out]=0.;
			for (int in=0;in<input.length;in++)
			{
				output[out]+=input[in]*weights.get(l)[in][out];
			}
			output[out] = MathUtils.sigmoid(output[out]);
		}
		return output;
	}
	
	public void learnStepNonGenerative(List<byte[]> images)
	{
		for (int img = 0;img<images.size();img++)
		{
			double[] out = forwardStep(images.get(img), true);
			
			int indexMax = img;//(int) (Math.random()*weights.get(0).length);//MathUtils.indexMax(out);
			System.out.println(indexMax);
			for (int j = 0;j < out.length; j++)
			{
				int desired = 0;
				if (j==indexMax)
				{
					desired = 1;
				}

				if ((Double.valueOf(out[j]).byteValue())!=desired)
				{
					if (Double.valueOf(out[j]).byteValue()==1)
					{
						for (int i = 0; i < images.get(img).length; i++)
						{
							weights.get(0)[j][i]-=images.get(img)[i] ;
						}
					}
					else
					{
						for (int i = 0; i < images.get(img).length; i++)
						{
							weights.get(0)[j][i]+=images.get(img)[i] ;
						}
					}
				}
				
			}
		}
	}
	
	public void learnStepMaxGenerativeLevel(List<byte[]> images, long level)
	{
		if (weights.size()<=level)
		{
			throw new IllegalArgumentException("No such weights level exists");
		}
		
		for (int i =0 ; i<=level; i++)
		{
			
		}
	}
	public void learnStepMaxGenerative(List<byte[]> images)
	{
		int softMaxIndex = (int) (Math.random()*weights.get(0).length);
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
					weights.get(0)[softMaxIndex][i]-=LEARNING_RATE ;
				}
				else
				{
					weights.get(0)[softMaxIndex][i]+=LEARNING_RATE ;
				}
			}
		}
		String code = generatives.get(softMaxIndex);
		if (code == null) code="";
		generatives.put(softMaxIndex, code+" "+indexMax);
		//System.out.println("The neuron "+softMaxIndex+" generated the image "+indexMax);
	}

	public void learnStepMaxGenerativeMultipleSoftMax(List<byte[]> images)
	{
		int softMaxIndexes[] = new int[weights.get(0).length/2];
		int index=0;
		for (int i =0;i<weights.get(0).length;i++) 
		{
			if (MathUtils.sigmoid(0).intValue()==1)
			{
				softMaxIndexes[index++] = i;
				if (index>=softMaxIndexes.length)
				{
					break;
				}
			}
		}
		double[] output = generateSampleNoBias(softMaxIndexes);
		
		int max=Integer.MIN_VALUE;
		int indexMax = 0;
		for (int img = 0;img<images.size();img++)
		{
			int tmpCoincidence = MathUtils.coincidence(images.get(img), output);
			if (tmpCoincidence>max)
			{
				max = tmpCoincidence;
				indexMax = img;
			}
		}
		for (int indexSoft =0;indexSoft<softMaxIndexes.length;indexSoft++)
		{
			for (int i = 0; i < output.length; i++)
			{
				if ((Double.valueOf(output[i]).byteValue())!=images.get(indexMax)[i])
				{
					if (output[i]==1)
					{
						weights.get(0)[softMaxIndexes[indexSoft]][i]-=LEARNING_RATE ;
					}
					else
					{
						weights.get(0)[softMaxIndexes[indexSoft]][i]+=LEARNING_RATE ;
					}
				}
			}
			String code = generatives.get(softMaxIndexes[indexSoft]);
			if (code == null) code="";
			generatives.put(softMaxIndexes[indexSoft], code+" "+indexMax);
		}
	}
	
	public void learnStepRandomGenerative(List<byte[]> images)
	{
		int softMaxIndex = (int) (Math.random()*weights.get(0).length);
		double[] output = generateSampleNoBias();
		
		int indexMax = (int) (Math.random()*images.size());;

		for (int i = 0; i < output.length; i++)
		{
			if ((Double.valueOf(output[i]).byteValue())!=images.get(indexMax)[i])
			{
				if (output[i]==1)
				{
					weights.get(0)[softMaxIndex][i]-=LEARNING_RATE ;
				}
				else
				{
					weights.get(0)[softMaxIndex][i]+=LEARNING_RATE ;
				}
			}
		}
		//System.out.println(softMaxIndex+" "+indexMax);
	}

	public int test(byte[] bs)
	{
		double[] output = forwardStep(bs, false);
		return MathUtils.indexMax(output);
	}

	private double[] forwardStep(byte[] bs, boolean applySigmoid)
	{
		double[] input = new double[weights.get(weights.size()-1)[0].length];
		for (int i = 0; i < input.length; i++)
		{
			input[i] = new Double(bs[i]);
		}
		double[] output = null;
		for (int l = weights.size()-1; l>=0;l--)
		{
			output = forwardStepLevel(input, l);
			if (applySigmoid)
			{
				MathUtils.sigmoid(output);
			}
			input = output;
		}
		return output;
	}

	private double[] forwardStepLevel(double[] input, int l)
	{
		double[] output;
		output = new double[weights.get(l).length];
		for (int out=0;out<output.length;out++)
		{
			output[out]=0.;
			for (int in=0;in<input.length;in++)
			{
				output[out]+=input[in]*weights.get(l)[out][in];
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