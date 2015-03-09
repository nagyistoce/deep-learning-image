package home.mutant.automata;

import java.util.Arrays;
import java.util.List;


public class NeuronCell 
{
	private static final int BIAS_INPUT = 255;
	private static final int BIAS_STATISTIC = 1;
	public int x;
	public int y;
	public float[] statistics;
	public float[] statisticsTmp;
	public byte output;
	public byte outputTmp;
	public NeuronCell[] neighbours;
	
	public void step() 
	{
		double length=0;
		double output2=0;
		float onInputs = normalizeInput();
		for (int i = 0; i < statistics.length; i++) 
		{
			length+=statistics[i];
		}
		for (int i = 0; i < statistics.length; i++) 
		{
			output2+=statisticsTmp[i]*(statistics[i]/length);
		}
		System.out.println(output2);
		if (onInputs>1) 
		{
			if (output==1)
			{
				System.out.println("bb");
				for (int i = 0; i < statistics.length-1; i++) 
				{
					statistics[i]+=neighbours[i].output;
				}
			}
			outputTmp = 1;
		}
		else
		{
			outputTmp = output;
		}
		
	}
	public float normalizeInput()
	{
		float length=0;
		for (int i = 0; i < neighbours.length; i++) 
		{
			statisticsTmp[i]=neighbours[i].output;
			length+= statisticsTmp[i];
		}
		length+= BIAS_INPUT;;
		for (int i = 0; i < statisticsTmp.length; i++) 
		{
			statisticsTmp[i]/=length;
		}
		return length-BIAS_INPUT;
	}
	
	public void sync()
	{
		output=outputTmp;
	}
	public void addNeighbours(List<NeuronCell> newNeighbours) 
	{
		statistics = new float[newNeighbours.size()+1];
		statisticsTmp = new float[newNeighbours.size()+1];
		Arrays.fill(statistics,1);
		statistics[statistics.length-1]=BIAS_STATISTIC;
		statisticsTmp[statisticsTmp.length-1]=255;
		neighbours = new NeuronCell[newNeighbours.size()];
		for (int i = 0; i < neighbours.length; i++) 
		{
			neighbours[i]=newNeighbours.get(i);
		}
	}
}
