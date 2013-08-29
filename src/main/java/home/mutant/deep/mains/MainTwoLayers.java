package home.mutant.deep.mains;


import home.mutant.deep.abstracts.Neuron;
import home.mutant.deep.model.IndexValue;
import home.mutant.deep.networks.TwoFullConnectedLayers;
import home.mutant.deep.neurons.BinaryNeuron;
import home.mutant.deep.neurons.ByteNeuron;
import home.mutant.deep.ui.Image;
import home.mutant.deep.utils.ImageUtils;
import home.mutant.deep.utils.MathUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainTwoLayers
{
	public enum Style {BW, GREY};
	List<Image> trainImages = new ArrayList<Image>();
	List<Integer> trainLabels  = new ArrayList<Integer>();

	List<Image> testImages = new ArrayList<Image>();
	List<Integer> testLabels  = new ArrayList<Integer>();
	public Style style=Style.BW;
	public static void main(String[] args) throws IOException
	{
		new MainTwoLayers(Style.BW).run();
	}

	private void run()
	{
		//runMaxAndAverage();
		runMaxForAffine();
	}
	
	private void runMaxAndAverage()
	{
		Class<? extends Neuron> clazz = null;
		if (style == Style.BW)
		{
			clazz = BinaryNeuron.class;
		}
		else
		{
			clazz = ByteNeuron.class;
		}
		TwoFullConnectedLayers model = new TwoFullConnectedLayers(300*200, 28*28,clazz);
		model.initWeightsFromImages(trainImages.subList(0, 60000));
		int missedAverage=0;
		int missedMax=0;
		int total=0;
		for (int test = 0; test<10000; test++)
		{
			total++;
			//int indexMax = model.forwardStepIndex(testImages.get(test));
			List<IndexValue> indexes=model.forwardStepMultipleIndexWithValues(testImages.get(test), 10);
			//if (testLabels.get(test)!=trainLabels.get(indexes.get(0).index))
			if (testLabels.get(test)!=MathUtils.getMaxFromAverage(indexes,trainLabels))
			{
				missedAverage++;
				System.out.println("AVERAGE Label is " +testLabels.get(test)+", model says "+MathUtils.printIndexes(indexes, trainLabels));
			}
			if (testLabels.get(test)!=trainLabels.get(indexes.get(0).index))
			{
				missedMax++;
				System.out.println("MAX Label is " +testLabels.get(test)+", model says "+trainLabels.get(indexes.get(0).index));
			}			
			if (test%100==99)
			{
				System.out.println("Average "+String.format( "%.2f", missedAverage*100./total )+"%");
				System.out.println("Max "+String.format( "%.2f", missedMax*100./total )+"%");
			}
		}
	}

	private void runMaxForAffine()
	{
		Class<? extends Neuron> clazz = null;
		if (style == Style.BW)
		{
			clazz = BinaryNeuron.class;
		}
		else
		{
			clazz = ByteNeuron.class;
		}
		TwoFullConnectedLayers model = new TwoFullConnectedLayers(300*200, 28*28,clazz);
		model.initWeightsFromImages(trainImages.subList(0, 60000));
		RunnerTwoLayers runner1 = new RunnerTwoLayers(model,testImages.subList(0, 2500),testLabels.subList(0, 2500),trainLabels);
		RunnerTwoLayers runner2 = new RunnerTwoLayers(model,testImages.subList(2500, 5000),testLabels.subList(2500, 5000),trainLabels);
		RunnerTwoLayers runner3 = new RunnerTwoLayers(model,testImages.subList(5000, 7500),testLabels.subList(5000, 7500),trainLabels);
		RunnerTwoLayers runner4 = new RunnerTwoLayers(model,testImages.subList(7500, 10000),testLabels.subList(7500, 10000),trainLabels);
		runner1.join();
		runner2.join();
		runner3.join();
		runner4.join();
		System.out.println("FINAL Error rate  "+String.format( "%.2f", (runner1.missedMax+runner2.missedMax+runner3.missedMax+runner4.missedMax)*100./10000)+"%");
	}

	public MainTwoLayers(Style style) throws IOException
	{
		this.style = style;
		loadImages();
	}
	private void loadImages() throws IOException
	{
		trainLabels = ImageUtils.readMinstLabels("/mnist/train-labels.idx1-ubyte");
		testLabels = ImageUtils.readMinstLabels("/mnist/t10k-labels.idx1-ubyte");
		if (style == Style.BW)
		{
			trainImages = ImageUtils.readMnistAsBWImage("/mnist/train-images.idx3-ubyte");
			testImages = ImageUtils.readMnistAsBWImage("/mnist/t10k-images.idx3-ubyte");
		}
		else
		{
			trainImages = ImageUtils.readMnistAsImage("/mnist/train-images.idx3-ubyte");
			testImages = ImageUtils.readMnistAsImage("/mnist/t10k-images.idx3-ubyte");			
		}
	}
}
