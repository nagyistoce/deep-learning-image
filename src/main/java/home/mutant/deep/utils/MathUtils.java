package home.mutant.deep.utils;

import home.mutant.deep.model.IndexValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MathUtils
{
	public static double standardDev(int[] values)
	{
		double mean = mean(values);
		double sum=0;
		for (int val:values)
		{
			sum+=(mean-val)*(mean-val);
		}
		return Math.sqrt(sum/values.length);
	}
	public static double mean(int[] values)
	{
		double sum=0;
		for (int val:values)
		{
			sum+=val;
		}
		return sum/values.length;
	}
	
	public static long max(long[] array)
	{
		Long max = Long.MIN_VALUE;
		for (int out=0;out<array.length;out++)
		{
			if (array[out]>max)
			{
				max = array[out];
			}
		}
		return max;
	}
	
	public static String printArray(Double[] array)
	{
		String out = "Values:";
		for (int i=0;i<array.length;i++)
		{
			out+=array[i] + " ";
		}
		return out;
	}
	
	public static int indexMax(double[] array)
	{
		double max = Double.NEGATIVE_INFINITY;
		int indexMax=-1;
		for (int out=0;out<array.length;out++)
		{
			if (array[out]>max)
			{
				max = array[out];
				indexMax = out;
			}
		}
		return indexMax;
	}
	
	public static int indexMax(long[] array)
	{
		long max = Long.MIN_VALUE;
		int indexMax=-1;
		for (int out=0;out<array.length;out++)
		{
			if (array[out]>max)
			{
				max = array[out];
				indexMax = out;
			}
		}
		return indexMax;
	}
	
	public static double max(double[] array)
	{
		double max = Double.NEGATIVE_INFINITY;
		for (int out=0;out<array.length;out++)
		{
			if (array[out]>max)
			{
				max = array[out];
			}
		}
		return max;
	}
	
	public static Double sigmoid(double totalInput)
	{
		return (Math.random()<= 1./(1+Math.exp(-totalInput)))?1.:0;
	}
	
	public static Double sigmoid(double totalInput, double threshhold)
	{
		return (Math.random()<= 1./(1+Math.exp(-totalInput+threshhold)))?1.:0;
	}	
	public static Double asymptoticToZero(double x)
	{
		return 10000*Math.exp(-1*x/1000.);
	}
	public static void sigmoid(double[] input)
	{
		for (int i=0; i<input.length; i++)
		{
			input[i] = MathUtils.sigmoid(input[i]);
		}
	}
	public static void sigmoid(double[] input, double threshhold)
	{
		for (int i=0; i<input.length; i++)
		{
			input[i] = MathUtils.sigmoid(input[i],threshhold);
		}
	}
	
	public static int coincidence(byte[] test, double[] sample)
	{
		int coincidence=0;
		for (int i = 0; i < sample.length; i++)
		{
			if (test[i]==sample[i])
			{
				coincidence+=1;
			}
			else
			{
				coincidence-=1;
			}
		}
		return coincidence;
	}
	public static int coincidence(byte[] test, byte[] sample)
	{
		int coincidence=0;
		for (int i = 0; i < sample.length; i++)
		{
			if (test[i]==sample[i])
			{
				coincidence+=1;
			}
			else
			{
				coincidence-=1;
			}
		}
		return coincidence;
	}
	public static List<Integer> indexMaxMultiple(int numberMax, double[] output) 
	{
		List<Integer> resIndex = new ArrayList<Integer>();
		List<Double> resValues = new ArrayList<Double>();
		for (int i = 0; i < output.length; i++)
		{
			int index = insertSortedValue(output[i], resValues, numberMax);
			if (index<numberMax)
			{
				resIndex.add(index, i);
			}
		}
		
		return resIndex.subList(0, numberMax);
	}
	public static int insertSortedValue(double value,List<Double> arr, int maxCapacity)
	{
		int indexToInsert = 0;
		for (Double integer : arr) 
		{
			if (value>integer)
			{
				break;
			}
			indexToInsert++;

		}
		arr.add(indexToInsert, value);
		if (arr.size()>maxCapacity)
		{
			arr.remove(maxCapacity);
		}
		return indexToInsert;
	}
	public static List<IndexValue> indexMaxMultipleWithValues(int numberMax, double[] output) 
	{
		List<Integer> resIndex = new ArrayList<Integer>();
		List<Double> resValues = new ArrayList<Double>();
		for (int i = 0; i < output.length; i++)
		{
			int index = insertSortedValue(output[i], resValues, numberMax);
			if (index<numberMax)
			{
				resIndex.add(index, i);
			}
		}
		
		List<IndexValue> res = new ArrayList<IndexValue>();
		resIndex =  resIndex.subList(0, numberMax);
		for (int indexRes=0;indexRes<resIndex.size();indexRes++) 
		{
			res.add(new IndexValue(resIndex.get(indexRes), resValues.get(indexRes)));
		}
		return res;
	}
	public static int getMaxFromAverage(List<IndexValue> indexes,List<Integer> trainLabels)
	{
		Map<Integer, Double> average = new HashMap<Integer,Double>();
		for (IndexValue indexValue : indexes) 
		{
			if (average.get(trainLabels.get(indexValue.index))==null)
			{
				average.put(trainLabels.get(indexValue.index),0.);
			}
			average.put(trainLabels.get(indexValue.index),average.get(trainLabels.get(indexValue.index))+indexValue.value*indexValue.value);
		}
		
		double max=0;
		int label=-1;
		for (Integer key : average.keySet()) 
		{
			if (max<average.get(key))
			{
				max = average.get(key);
				label = key;
			}
		}
		return label;
	}
	
	public static int getKeyForMaxValue(Map<Integer, Integer> map)
	{
		int max=Integer.MIN_VALUE;
		int label=-1;
		for (Integer key : map.keySet()) 
		{
			if (max<map.get(key))
			{
				max = map.get(key);
				label = key;
			}
		}
		return label;
	}
	public static String printIndexes(List<IndexValue> indexes, List<Integer> trainLabels)
	{
		String res="";
		for (IndexValue indexVal: indexes) 
		{
			res+=trainLabels.get(indexVal.index)+":"+indexVal.value+ ", ";
		}
		return res;
	}
	public static IndexValue indexValueMax(double[] output) 
	{
		double max = Double.NEGATIVE_INFINITY;
		int indexMax=-1;
		for (int out=0;out<output.length;out++)
		{
			if (output[out]>max)
			{
				max = output[out];
				indexMax = out;
			}
		}
		return new IndexValue(indexMax, max);
	}
}
