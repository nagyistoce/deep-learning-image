package home.mutant.zoom.model;

import java.util.ArrayList;
import java.util.List;

public class Corelations 
{
	public List<Integer> correlations;
	public Corelations()
	{
		correlations = new ArrayList<Integer>();
		for (int i=0;i<10;i++) 
		{
			correlations.add(0);
		}
	}
	public void add(Corelations corelations) 
	{
		for (int i=0;i<10;i++) 
		{
			correlations.set(i, correlations.get(i)+corelations.correlations.get(i));
		}
	}
	
	public void add(Corelations corelations, double weight) 
	{
		for (int i=0;i<10;i++) 
		{
			correlations.set(i, correlations.get(i)+(int)(corelations.correlations.get(i)*weight));
		}
	}
}
