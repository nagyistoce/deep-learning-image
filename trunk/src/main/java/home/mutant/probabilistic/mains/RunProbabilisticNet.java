package home.mutant.probabilistic.mains;


import java.io.Serializable;
import java.util.Arrays;

import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.MathUtils;
import home.mutant.deep.utils.MnistDatabase;
import home.mutant.probabilistic.mains.runnables.InputImageRunnable;
import home.mutant.probabilistic.mains.runnables.OutputRunnable;
import home.mutant.probabilistic.mains.runnables.OutputRunnableByThread;
import home.mutant.probabilistic.mains.runnables.ProbabilisticNetRunnable;
import home.mutant.probabilistic.nets.ProbabilisticNet;

public class RunProbabilisticNet 
{
	public static void main(String[] args) throws Exception
	{
		ResultFrame frame = new ResultFrame(60, 600);
		MnistDatabase.loadGradientImages();
		ProbabilisticNet net = new ProbabilisticNet(28, 28+100);
		Thread netThread = new Thread(new ProbabilisticNetRunnable(net));
		netThread.start();

		for (int s=0;s<60;s++)
			showSupervised(frame, net, s);
		Thread.sleep(2000);
		for (int s=0;s<60;s++)
			showUnsupervised(frame, net, s);
		while(true)
		{
			frame.showImage(net.generateSample());
			System.in.read();
		}		
	}

	private static void showSupervised(ResultFrame frame, ProbabilisticNet net, int indexImage) throws Exception 
	{
		byte[]  pixels = new byte[28*28*11];
		System.arraycopy(MnistDatabase.trainImages.get(indexImage).getDataOneDimensional(), 0, pixels, 0, 28*28);
		Integer label = MnistDatabase.trainLabels.get(indexImage);
		Arrays.fill(pixels, 28*28+280*label, 784 + 280*(1+label), (byte)255);
		Thread inputThread = new Thread(new InputImageRunnable(pixels, net, 8000000));
		inputThread.start();
		while(inputThread.isAlive())
		{
			frame.showImage(net.generateImage());
		}
	}

	private static void showUnsupervised(ResultFrame frame, ProbabilisticNet net, int indexImage) throws Exception
	{
		System.out.println(indexImage);
		//Thread.sleep(1000);
		byte[] pixels = new byte[28*28];
		System.arraycopy(MnistDatabase.trainImages.get(indexImage).getDataOneDimensional(), 0, pixels, 0, 28*28);
		Thread inputThread = new Thread(new InputImageRunnable(pixels, net, 2000000));
		inputThread.start();
		OutputRunnableByThread outputRunnable = new OutputRunnableByThread( net, inputThread);
		Thread outputThread = new Thread(outputRunnable);
		outputThread.start();
		while(inputThread.isAlive() || outputThread.isAlive())
		{
			frame.showImage(net.generateImage());
		}
		inputThread.join();
		outputThread.join();
		Integer label = MnistDatabase.trainLabels.get(indexImage);
		int max = MathUtils.indexMax(outputRunnable.outputs);
		if (label!=max) 
		{
			System.out.println("Was " + label +", predicted " + max + " "+Arrays.toString(outputRunnable.outputs));
		}
	}
}
