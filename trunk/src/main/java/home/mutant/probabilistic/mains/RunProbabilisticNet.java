package home.mutant.probabilistic.mains;


import java.util.Arrays;

import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.MnistDatabase;
import home.mutant.probabilistic.mains.runnables.InputImageRunnable;
import home.mutant.probabilistic.mains.runnables.ProbabilisticNetRunnable;
import home.mutant.probabilistic.nets.ProbabilisticNet;

public class RunProbabilisticNet 
{
	public static void main(String[] args) throws Exception
	{
		ResultFrame frame = new ResultFrame(60, 600);
		MnistDatabase.loadImages();
		byte[] pixels = new byte[28*280];
		System.arraycopy(MnistDatabase.trainImages.get(0).getDataOneDimensional(), 0, pixels, 0, 28*28);
		Arrays.fill(pixels, 28*28, 2*28*28, (byte)255);
		ProbabilisticNet net = new ProbabilisticNet(28, 280);
		Thread netThread = new Thread(new ProbabilisticNetRunnable(net));
		netThread.start();
		
		for (int s=0;s<4;s++)
			showSupervised(frame, net, s);
		for (int s=0;s<4;s++)
			showUnsupervised(frame, net, s);

		while(true)
			frame.showImage(net.generateSample());
		
	}

	private static void showSupervised(ResultFrame frame, ProbabilisticNet net, int indexImage) 
	{
		byte[]  pixels = new byte[28*280];
		System.arraycopy(MnistDatabase.trainImages.get(indexImage).getDataOneDimensional(), 0, pixels, 0, 28*28);
		Arrays.fill(pixels, 28*28*(1+indexImage), (2+indexImage)*28*28, (byte)255);
		Thread inputThread = new Thread(new InputImageRunnable(pixels, net, 10000000));
		inputThread.start();
		while(inputThread.isAlive())
		{
			frame.showImage(net.generateImage());
		}
	}

	private static void showUnsupervised(ResultFrame frame, ProbabilisticNet net, int indexImage)
	{
		byte[] pixels = new byte[28*280];
		System.arraycopy(MnistDatabase.testImages.get(indexImage).getDataOneDimensional(), 0, pixels, 0, 28*28);
		Thread inputThread = new Thread(new InputImageRunnable(pixels, net, 10000000));
		inputThread.start();
		while(inputThread.isAlive())
		{
			frame.showImage(net.generateImage());
		}
	}
}
