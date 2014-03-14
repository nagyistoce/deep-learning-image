package home.mutant.deep.mains;


import home.mutant.deep.mains.MainTwoLayers.Style;
import home.mutant.deep.model.IndexValue;
import home.mutant.deep.networks.ThreeConvolutedLayersBinary;
import home.mutant.deep.neurons.BinaryNeuron;
import home.mutant.deep.ui.Image;
import home.mutant.deep.utils.ImageUtils;
import home.mutant.deep.utils.MathUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainConvolutedBinary
{
	List<Image> trainImages = new ArrayList<Image>();
	List<Integer> trainLabels  = new ArrayList<Integer>();

	List<Image> testImages = new ArrayList<Image>();
	List<Integer> testLabels  = new ArrayList<Integer>();
	
	public static void main(String[] args) throws IOException
	{
		new MainConvolutedBinary().run();
	}

/*	private void run()
	{
		ThreeConvolutedLayersBinary model = new ThreeConvolutedLayersBinary(28,4,16,60000, BinaryNeuron.class);
		model.initWeightsFromImages(trainImages.subList(0, 60000));
		ResultFrame frame = new ResultFrame(1600, 600);
		//frame.showBinaryColumn(model.bottom.column);
		//frame.showImage(model.bottom.reconstruct(testImages.get(0)));
		//frame.showImage(testImages.get(0));
		frame.showImage(model.bottom.forwardStep(testImages.get(1)));
		int missed=0;
		int total=0;
		for (int test = 0; test<10000; test++)
		{
			total++;
			int indexMax = model.forwardStepIndex(testImages.get(test));
			//long max = model.forwardStepMax(testImages.get(test));
			if (testLabels.get(test)!=trainLabels.get(indexMax))
			{
				missed++;
				System.out.println("Label is " +testLabels.get(test)+", model says "+trainLabels.get(indexMax));
			}
			
			if (test%100==99)
			{
				System.out.println("Error rate "+missed*100./total);
			}
		}
		System.out.println();
		System.out.println("FINAL error rate "+missed*100./total);
	}
*/
	private void run()
	{
		ThreeConvolutedLayersBinary model = new ThreeConvolutedLayersBinary(28,4,16,60000, BinaryNeuron.class);
		model.initWeightsFromImages(trainImages.subList(0, 60000));
		int missedAverage=0;
		int missedMax=0;
		int missedMaxSingle=0;
		int total=0;
		for (int test = 0; test<10000; test++)
		{
			total++;
			int indexMax = model.forwardStepIndex(testImages.get(test));
			List<IndexValue> indexes=model.forwardStepMultipleIndexWithValues(testImages.get(test), 7);
			//if (testLabels.get(test)!=trainLabels.get(indexes.get(0).index))
			if (testLabels.get(test)!=MathUtils.getMaxFromAverage(indexes, trainLabels))
			{
				missedAverage++;
				System.out.println("+++++++ average Label is " +testLabels.get(test)+", model says "+MathUtils.printIndexes(indexes, trainLabels));
			}
			if (testLabels.get(test)!=trainLabels.get(indexes.get(0).index))
			{
				missedMax++;
				System.out.println("------- max Label is " +testLabels.get(test)+", model says "+trainLabels.get(indexes.get(0).index));
			}			
			if (testLabels.get(test)!=trainLabels.get(indexMax))
			{
				missedMaxSingle++;
				System.out.println("------- single max Label is " +testLabels.get(test)+", model says "+trainLabels.get(indexMax));
			}
			if (test%100==99)
			{
				System.out.println("Average "+String.format( "%.2f", missedAverage*100./total )+"%");
				System.out.println("Max "+String.format( "%.2f", missedMax*100./total )+"%");
				System.out.println("Single Max "+String.format( "%.2f", missedMaxSingle*100./total )+"%");
			}
		}
	}
	public MainConvolutedBinary() throws IOException
	{
		ImageUtils.loadImages(trainImages, testImages, trainLabels, testLabels, Style.BW);
	}
}
