package home.mutant.deep.utils.kmeans;

import home.mutant.liquid.cells.NeuronCell;
import home.mutant.liquid.cells.NeuronCellGreyDifference;

import java.util.ArrayList;
import java.util.List;

public class Kmeans
{
	private static final int NO_ITERATIONS = 40;

	public static List<List<Integer>>run(List<NeuronCell> list, int noClusters)
	{
		List<List<Integer>> clusters = new ArrayList<List<Integer>>();
		List<NeuronCell> centers = new ArrayList<NeuronCell>();
		for (int i=0;i<noClusters;i++)
		{
			centers.add(new NeuronCellGreyDifference(list.get((int) (Math.random()*list.size()))));
			clusters.add(new ArrayList<Integer>());
		}
		
		for (int i=0;i<NO_ITERATIONS;i++)
		{
			populateClusters(list, clusters, centers);
			recalculateClustersCenters(list, clusters, centers);
		}
		return clusters;
	}
	
	private static void populateClusters(List<NeuronCell> list,List<List<Integer>> clusters, List<NeuronCell> centers)
	{
		for(int i=0 ; i<clusters.size(); i++)
		{
			clusters.get(i).clear();
		}
		
		for (int i = 0; i<list.size(); i++)
		{
			double minDistance = 1000000;
			int minCluster=-1;
			for (int j = 0; j<centers.size(); j++)
			{
				NeuronCell centre = centers.get(j);
				double distance = centre.output(list.get(i));
				if (distance<minDistance)
				{
					minDistance = distance;
					minCluster = j;
				}
			}
			clusters.get(minCluster).add(i);
		}
	}
	
	private static void recalculateClustersCenters(List<NeuronCell> list,List<List<Integer>> clusters, List<NeuronCell> centers)
	{
		for (int j = 0; j<clusters.size(); j++)
		{
			List<Integer> cluster = clusters.get(j);
			NeuronCell center = centers.get(j);
			center.weights = new double[center.weights.length];
			
			for(int w=0; w<center.weights.length; w++)
			{
				for (int i = 0; i<cluster.size(); i++)
				{
					center.weights[w]+=list.get(cluster.get(i)).weights[w];
				}
				center.weights[w]/=cluster.size();
			}
		}
	}
}
