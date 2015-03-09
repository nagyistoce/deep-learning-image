package home.mutant.zoom.model;

import home.mutant.deep.utils.MathUtils;

import java.util.HashMap;
import java.util.Map;


public class Level 
{
	public Map<PlacedFeature, Corelations> statistic = new HashMap<PlacedFeature, Corelations>();

	public void processFeature(byte[] inputFeature, int x, int y, int toCorrelate) 
	{
		for(PlacedFeature placedFeature:statistic.keySet())
		{
			if (placedFeature.feature.match(inputFeature))
			{
				addCorelation(toCorrelate, placedFeature);
			}
		}
	}
	
	public void addFeature(byte[] inputFeature, int x, int y, int toCorrelate) 
	{
		PlacedFeature found = null;
		for(PlacedFeature placedFeature:statistic.keySet())
		{
			if (placedFeature.feature.match(inputFeature))
			{
				found = placedFeature;
				break;
			}
		}
		if (found!=null)
			return;
		found = new PlacedFeature(x,y, inputFeature);
		statistic.put(found, new Corelations());
		addCorelation(toCorrelate, found);
	}
	public int getCorrelation(byte[] inputFeature)
	{
		Corelations corelation = new Corelations();
		for(PlacedFeature placedFeature:statistic.keySet())
		{
			if (placedFeature.feature.match(inputFeature))
			{
				corelation.add(statistic.get(placedFeature),placedFeature.feature.distance);
			}
		}
		return MathUtils.indexMax(corelation.correlations);
	}
	private void addCorelation(int toCorrelate, PlacedFeature found) 
	{
		Corelations corelation = statistic.get(found);
		corelation.correlations.set(toCorrelate, corelation.correlations.get(toCorrelate)+10);
	}
	
	private void addCorelation(int toCorrelate, PlacedFeature found, double weight) 
	{
		Corelations corelation = statistic.get(found);
		corelation.correlations.set(toCorrelate, corelation.correlations.get(toCorrelate)+(int)(weight*10));
	}
}
