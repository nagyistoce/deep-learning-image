package home.mutant.liquid.mains;

import home.mutant.deep.ui.Image;
import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.MnistDatabase;
import home.mutant.deep.utils.kmeans.Kmeans;
import home.mutant.liquid.cells.NeuronCell;
import home.mutant.liquid.cells.NeuronCellGreyDifference;
import home.mutant.liquid.networks.SimpleNet;
import home.mutant.liquid.runnable.OutputNeuronsRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShowNetworkWeightsSubImages 
{
	public static final int NO_THREADS = 8;
	public static final int NO_NEURONS_PER_THREAD = 1250;

	public static void main(String[] args) throws IOException, InterruptedException
	{
		int subImageX=5;
		int subImageStep = 2;
		
		SimpleNet net = train(subImageX, subImageStep);
		ResultFrame frame = new ResultFrame(1200, 1080);
		frame.showNetworkWeights(net, 1200/(subImageX+1),1);
	}

	static SimpleNet train(int subImageX, int subImageStep)
			throws IOException, InterruptedException
	{
		SimpleNet net = new SimpleNet();

		MnistDatabase.loadImages();

		for (int i=0;i<NO_THREADS * NO_NEURONS_PER_THREAD;i++)
		{
			net.neurons.add(new NeuronCellGreyDifference(subImageX*subImageX));
		}
		
		List<OutputNeuronsRunnable>  runnables = new ArrayList<OutputNeuronsRunnable>();
		for (int i=0;i<NO_THREADS;i++)
		{
			OutputNeuronsRunnable ouputRunnable = new OutputNeuronsRunnable(net.neurons.subList(i*NO_NEURONS_PER_THREAD, (i+1)*NO_NEURONS_PER_THREAD));
			runnables.add(ouputRunnable);
		}

		List<byte[]> subImages = new ArrayList<byte[]>();
		long t0=System.currentTimeMillis();
		for (int imageIndex=0;imageIndex<600;imageIndex++)
		{
			Image trainImage = MnistDatabase.trainImages.get(imageIndex);
			subImages.addAll(trainImage.divideImage(subImageX, subImageX, subImageStep, subImageStep));
		}
		List<Thread>  threads = new ArrayList<Thread>();
		for(int cycles=0;cycles<1;cycles++)
		{
			for (int i=0;i<NO_THREADS;i++)
			{
				runnables.get(i).subImages = subImages;
				threads.add(new Thread(runnables.get(i)));
				threads.get(i).start();
			}
			for (int i=0;i<NO_THREADS;i++)
			{
				threads.get(i).join();
			}
			threads.clear();
		}
		
		
		
		List<List<Integer>> clusters = Kmeans.run(net.neurons, 100);
		List<NeuronCell> neurons = new ArrayList<NeuronCell>();
		for (List<Integer> list : clusters)
		{
			for (Integer integer : list)
			{
				neurons.add(net.neurons.get(integer));
			}
		}
		net.neurons = neurons;
		
		System.out.println(net.neurons.size());
		net.sortNeuronsByDistance();
		//Collections.sort(net.neurons);
		System.out.println(net.neurons.size());
		
		System.out.println(System.currentTimeMillis()-t0);
		return net;
	}
}
