package home.mutant.probabilistic.mains;


import home.mutant.deep.ui.Image;
import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.ImageUtils;
import home.mutant.deep.utils.MathUtils;
import home.mutant.deep.utils.MnistDatabase;
import home.mutant.probabilistic.cells.ProbabilisticNeuron;
import home.mutant.probabilistic.mains.runnables.InputImageRunnable;
import home.mutant.probabilistic.mains.runnables.OutputRunnableByThread;
import home.mutant.probabilistic.mains.runnables.ProbabilisticNetRunnable;
import home.mutant.probabilistic.nets.ProbabilisticNet;

import java.util.Arrays;

public class RunProbabilisticNet 
{
	public static final int IMAGE_SIZE = 56;
	public static final int CLASS_SIZE = 20;
	public static void main(String[] args) throws Exception
	{
		ResultFrame frame = new ResultFrame(200, 700);
		MnistDatabase.loadGradientImages();
		
		ProbabilisticNet net = new ProbabilisticNet(IMAGE_SIZE, IMAGE_SIZE+10*CLASS_SIZE);


		for (int s=0;s<30;s++)
			supervisedNonProbabilistic(net, s);
//			showSupervised(frame, net, s);
		//Thread.sleep(2000);
		
		Thread netThread = new Thread(new ProbabilisticNetRunnable(net));
		netThread.start();
		for (int s=0;s<30;s++)
			showUnsupervised(frame, net, s);
		while(true)
		{
			frame.showImage(net.generateSample());
			System.in.read();
		}		
	}

	private static void supervisedNonProbabilistic(ProbabilisticNet net, int indexImage) throws Exception
	{
		byte[]  pixels = new byte[IMAGE_SIZE*IMAGE_SIZE+IMAGE_SIZE*CLASS_SIZE*10];
		Image image = ImageUtils.scaleImage(MnistDatabase.trainImages.get(indexImage), IMAGE_SIZE/28.);
		System.arraycopy(image.getDataOneDimensional(), 0, pixels, 0, IMAGE_SIZE*IMAGE_SIZE);
		Integer label = MnistDatabase.trainLabels.get(indexImage);
		Arrays.fill(pixels, IMAGE_SIZE*IMAGE_SIZE+IMAGE_SIZE*CLASS_SIZE*label, IMAGE_SIZE*IMAGE_SIZE+IMAGE_SIZE*CLASS_SIZE*(1+label), (byte)255);
		for (int noPixel=0;noPixel<pixels.length;noPixel++)
		{
			int pixel = pixels[noPixel];
			if (pixel<0)pixel+=255;
			if (pixel<150) continue;
			if (Math.random()>0.4)continue;
			ProbabilisticNeuron neuron = net.neurons.get(noPixel);
			if (neuron == null)
			{
				neuron = new ProbabilisticNeuron(noPixel%RunProbabilisticNet.IMAGE_SIZE, noPixel/RunProbabilisticNet.IMAGE_SIZE);
				net.neurons.put(noPixel, neuron);
			}
			for (int noPixel2=0;noPixel2<pixels.length;noPixel2++)
			{
				int pixel2 = pixels[noPixel2];
				if (pixel2<0)pixel2+=255;
				if (pixel2<150)
				{
					//if (Math.random()>0.5)continue;
					if (noPixel2>IMAGE_SIZE*IMAGE_SIZE || noPixel>IMAGE_SIZE*IMAGE_SIZE) continue;
					ProbabilisticNeuron neuron2 = net.neurons.get(noPixel2);
					if (neuron2 == null)
					{
						neuron2 = new ProbabilisticNeuron(noPixel2%RunProbabilisticNet.IMAGE_SIZE, noPixel2/RunProbabilisticNet.IMAGE_SIZE);
						net.neurons.put(noPixel2, neuron2);
					}
					neuron2.nonLinks.add(neuron);
				}
				else
				{
					ProbabilisticNeuron neuron2 = net.neurons.get(noPixel2);
					if (neuron2 == null)
					{
						neuron2 = new ProbabilisticNeuron(noPixel2%RunProbabilisticNet.IMAGE_SIZE, noPixel2/RunProbabilisticNet.IMAGE_SIZE);
						net.neurons.put(noPixel2, neuron2);
					}
					neuron.links.add(neuron2);
				}
				
			}
		}
	}
	private static void showSupervised(ResultFrame frame, ProbabilisticNet net, int indexImage) throws Exception 
	{
		byte[]  pixels = new byte[IMAGE_SIZE*IMAGE_SIZE+IMAGE_SIZE*CLASS_SIZE*10];
		Image image = ImageUtils.scaleImage(MnistDatabase.trainImages.get(indexImage), IMAGE_SIZE/28.);
		System.arraycopy(image.getDataOneDimensional(), 0, pixels, 0, IMAGE_SIZE*IMAGE_SIZE);
		Integer label = MnistDatabase.trainLabels.get(indexImage);
		Arrays.fill(pixels, IMAGE_SIZE*IMAGE_SIZE+IMAGE_SIZE*CLASS_SIZE*label, IMAGE_SIZE*IMAGE_SIZE+IMAGE_SIZE*CLASS_SIZE*(1+label), (byte)255);
		Thread inputThread = new Thread(new InputImageRunnable(pixels, net, 160000000));
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
		Image image = ImageUtils.scaleImage(MnistDatabase.trainImages.get(indexImage),IMAGE_SIZE/28.);
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
