package home.mutant.probabilistic.mains;


import home.mutant.deep.ui.Image;
import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.ImageUtils;
import home.mutant.deep.utils.MathUtils;
import home.mutant.deep.utils.MnistDatabase;
import home.mutant.probabilistic.mains.runnables.InputImageRunnable;
import home.mutant.probabilistic.mains.runnables.OutputRunnableByThread;
import home.mutant.probabilistic.mains.runnables.ProbabilisticNetRunnable;
import home.mutant.probabilistic.nets.ProbabilisticNet;

import java.util.Arrays;

public class RunProbabilisticNet 
{
	public static final int IMAGE_SIZE = 14;
	public static void main(String[] args) throws Exception
	{
		ResultFrame frame = new ResultFrame(60, 600);
		MnistDatabase.loadGradientImages();
		
		ProbabilisticNet net = new ProbabilisticNet(IMAGE_SIZE, IMAGE_SIZE+100);
		Thread netThread = new Thread(new ProbabilisticNetRunnable(net));
		netThread.start();

		for (int s=0;s<30;s++)
			showSupervised(frame, net, s);
		//Thread.sleep(2000);
		for (int s=0;s<30;s++)
			showUnsupervised(frame, net, s);
		while(true)
		{
			frame.showImage(net.generateSample());
			System.in.read();
		}		
	}

	private static void showSupervised(ResultFrame frame, ProbabilisticNet net, int indexImage) throws Exception 
	{
		byte[]  pixels = new byte[IMAGE_SIZE*IMAGE_SIZE*11];
		Image image = ImageUtils.scaleImage(MnistDatabase.trainImages.get(indexImage),0.5);
		System.arraycopy(image.getDataOneDimensional(), 0, pixels, 0, IMAGE_SIZE*IMAGE_SIZE);
		Integer label = MnistDatabase.trainLabels.get(indexImage);
		Arrays.fill(pixels, IMAGE_SIZE*IMAGE_SIZE+IMAGE_SIZE*10*label, IMAGE_SIZE*IMAGE_SIZE+IMAGE_SIZE*10*(1+label), (byte)255);
		Thread inputThread = new Thread(new InputImageRunnable(pixels, net, 4000000));
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
		byte[] pixels = new byte[IMAGE_SIZE*IMAGE_SIZE];
		Image image = ImageUtils.scaleImage(MnistDatabase.trainImages.get(indexImage),0.5);
		System.arraycopy(image.getDataOneDimensional(), 0, pixels, 0, IMAGE_SIZE*IMAGE_SIZE);
		Thread inputThread = new Thread(new InputImageRunnable(pixels, net, 1000000));
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
