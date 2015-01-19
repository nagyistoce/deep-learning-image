package home.mutant.liquid.mains;

import home.mutant.deep.ui.Image;
import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.MnistDatabase;
import home.mutant.liquid.cells.NeuronCellGreyDifference;
import home.mutant.liquid.networks.SimpleNet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowNetworkWeightsSubImages 
{
	public static final int NO_THREADS = 8;
	public static final int NO_NEURONS_PER_THREAD = 1250;
	public static void main(String[] args) throws IOException, InterruptedException
	{
		ResultFrame frame = new ResultFrame(1900, 1080);
		SimpleNet net = new SimpleNet();

		MnistDatabase.loadImages();
		int subImageX=7;
		int subImageStep = 4;
		for (int i=0;i<NO_THREADS * NO_NEURONS_PER_THREAD;i++)
		{
			net.neurons.add(new NeuronCellGreyDifference(subImageX*subImageX));
		}
		long t0=System.currentTimeMillis();
		
		List<OutputNeuronsRunnable>  runnables = new ArrayList<OutputNeuronsRunnable>();
		for (int i=0;i<NO_THREADS;i++)
		{
			OutputNeuronsRunnable ouputRunnable = new OutputNeuronsRunnable(net.neurons.subList(i*NO_NEURONS_PER_THREAD, (i+1)*NO_NEURONS_PER_THREAD));
			runnables.add(ouputRunnable);
		}
		for (int imageIndex=0;imageIndex<6000;imageIndex++)
		{
			
			Image trainImage = MnistDatabase.trainImages.get(imageIndex);
			List<byte[]> subImages = trainImage.divideImage(subImageX, subImageX, subImageStep, subImageStep);
			List<Thread>  threads = new ArrayList<Thread>();
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
		}
		System.out.println(System.currentTimeMillis()-t0);
		frame.showNetworkWeights(net, 1900/(subImageX+1),1);
		System.out.println(net.neurons.size());
//		int count=0;
//		for (int imageIndex=0;imageIndex<10000;imageIndex++)
//		{
//			SimpleNet netTest = new SimpleNet();
//			System.out.println(MnistDatabase.testLabels.get(imageIndex));
//			for (NeuronCell neuron : net.neurons)
//			{
//				
//				if (neuron.isFiringBW(MnistDatabase.testImages.get(imageIndex)))
//				{
//					netTest.neurons.add(neuron);
//				}
//			}
//			if (netTest.neurons.size()==0)
//				count++;
//			for (NeuronCell neuron : netTest.neurons)
//			{
//				System.out.println(neuron.lastRecognized);
//			}
//			System.out.println();
//		}
//		System.out.println(count);
		//frame.showNetworkWeights(netTest, 60);
	}
}
