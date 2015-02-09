package home.mutant.liquid.mains;

import home.mutant.deep.ui.Image;
import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.MnistDatabase;
import home.mutant.deep.utils.kmeans.Kmeans;
import home.mutant.liquid.cells.NeuronCell;
import home.mutant.liquid.cells.NeuronCellGreyDifference;
import home.mutant.liquid.networks.DoubleNet;
import home.mutant.liquid.networks.SimpleNet;
import home.mutant.liquid.runnable.OutputDoubleNeuronsRunnable;
import home.mutant.liquid.runnable.OutputNeuronsRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowNWSOFM 
{
	public static final int NO_THREADS = 8;
	public static final int NO_NEURONS_PER_THREAD = 125;

	public static void main(String[] args) throws IOException, InterruptedException
	{
		
		DoubleNet net = new DoubleNet();

		MnistDatabase.loadImages();
		int subImageX=7;
		int subImageStep = 4;
		for (int i=0;i<NO_THREADS * NO_NEURONS_PER_THREAD;i++)
		{
			net.neurons.add(new NeuronCellGreyDifference(subImageX*subImageX));
			net.neurons2.add(new NeuronCellGreyDifference(subImageX*subImageX));
		}
		
		List<OutputNeuronsRunnable>  runnables = new ArrayList<OutputNeuronsRunnable>();
		List<OutputNeuronsRunnable>  runnables2 = new ArrayList<OutputNeuronsRunnable>();
		List<OutputDoubleNeuronsRunnable>  runnablesInter = new ArrayList<OutputDoubleNeuronsRunnable>();
		List<OutputDoubleNeuronsRunnable>  runnablesInter2 = new ArrayList<OutputDoubleNeuronsRunnable>();
		for (int i=0;i<NO_THREADS;i++)
		{
			OutputNeuronsRunnable ouputRunnable = new OutputNeuronsRunnable(net.neurons.subList(i*NO_NEURONS_PER_THREAD, (i+1)*NO_NEURONS_PER_THREAD));
			runnables.add(ouputRunnable);
		}

		for (int i=0;i<NO_THREADS;i++)
		{
			OutputNeuronsRunnable ouputRunnable = new OutputNeuronsRunnable(net.neurons2.subList(i*NO_NEURONS_PER_THREAD, (i+1)*NO_NEURONS_PER_THREAD));
			runnables2.add(ouputRunnable);
		}
		for (int i=0;i<NO_THREADS;i++)
		{
			OutputDoubleNeuronsRunnable ouputRunnable = new OutputDoubleNeuronsRunnable(net.neurons, net.neurons2.subList(i*NO_NEURONS_PER_THREAD, (i+1)*NO_NEURONS_PER_THREAD), i*NO_NEURONS_PER_THREAD);
			runnablesInter.add(ouputRunnable);
		}
		
		for (int i=0;i<NO_THREADS;i++)
		{
			OutputDoubleNeuronsRunnable ouputRunnable = new OutputDoubleNeuronsRunnable(net.neurons2, net.neurons.subList(i*NO_NEURONS_PER_THREAD, (i+1)*NO_NEURONS_PER_THREAD), i*NO_NEURONS_PER_THREAD);
			runnablesInter2.add(ouputRunnable);
		}
		
		List<byte[]> subImages = new ArrayList<byte[]>();
		long t0=System.currentTimeMillis();
		for (int imageIndex=0;imageIndex<600;imageIndex++)
		{
			
			Image trainImage = MnistDatabase.trainImages.get(imageIndex);
			subImages.addAll(trainImage.divideImage(subImageX, subImageX, subImageStep, subImageStep));
		}
		List<Thread>  threads = new ArrayList<Thread>();
		
		for(int cycles=0;cycles<30;cycles++)
		{
			threads.clear();
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
			for (int i=0;i<NO_THREADS;i++)
			{
				threads.add(new Thread(runnablesInter.get(i)));
				threads.get(i).start();
			}
			for (int i=0;i<NO_THREADS;i++)
			{
				threads.get(i).join();
			}
			
			threads.clear();
			for (int i=0;i<NO_THREADS;i++)
			{
				runnables2.get(i).subImages = subImages;
				threads.add(new Thread(runnables2.get(i)));
				threads.get(i).start();
			}
			for (int i=0;i<NO_THREADS;i++)
			{
				threads.get(i).join();
			}
			
			threads.clear();
			for (int i=0;i<NO_THREADS;i++)
			{
				threads.add(new Thread(runnablesInter2.get(i)));
				threads.get(i).start();
			}
			for (int i=0;i<NO_THREADS;i++)
			{
				threads.get(i).join();
			}
		}
		ResultFrame frame = new ResultFrame(1200, 1080);
		/*
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
		*/
		System.out.println(System.currentTimeMillis()-t0);

		
//		byte[] zeroPixels = new byte[49];
//		int countZeroUpdates = 0;
//		for(int i=0; i<net.neurons.size()/10-1;i++)
//		{
//			System.out.println(net.neurons.get(i).output(net.neurons.get(i+1)));
//			//System.out.println(net.neurons.get(i).output(zeroPixels));
//			if (net.neurons.get(i).noUpdates == 0)
//			{
//				countZeroUpdates++;
//			}
//		}
//		System.out.println(countZeroUpdates);
		
		frame.showNetworkWeights(net, 1200/(subImageX+1),1);
	}
}
