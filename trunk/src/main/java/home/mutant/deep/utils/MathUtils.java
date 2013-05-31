package home.mutant.deep.utils;

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
	public static Map<Integer, Double> indexMaxMultipleWithValues(int numberMax, double[] output) 
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
		
		Map<Integer, Double> res = new HashMap<Integer, Double>();
		resIndex =  resIndex.subList(0, numberMax);
		for (int indexRes=0;indexRes<resIndex.size();indexRes++) 
		{
			res.put(resIndex.get(indexRes), resValues.get(indexRes));
		}
		return res;
	}
}
