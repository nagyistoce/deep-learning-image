package home.mutant.deep;


import home.mutant.deep.model.Image;
import home.mutant.deep.model.TwoFullConnectedLayersBinary;
import home.mutant.deep.utils.ImageUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainBinary
{
	List<Image> trainImages = new ArrayList<Image>();
	List<Integer> trainLabels  = new ArrayList<Integer>();

	List<Image> testImages = new ArrayList<Image>();
	List<Integer> testLabels  = new ArrayList<Integer>();
	
	public static void main(String[] args) throws IOException
	{
		new MainBinary().run();
	}

//	private void run()
//	{
//		ResultFrame frame = new ResultFrame(1600, 600);
//		TwoFullConnectedLayersBinary model = new TwoFullConnectedLayersBinary(300*200, 28*28);
//		model.initWeightsFromImages(trainImages);
//		int missed=0;
//		int total=0;
//		for (int test = 0; test<2000; test++)
//		{
//			ModelTestResult max = frame.showOutput(model, testImages.get(test), trainLabels);
//			total++;
//			if (testLabels.get(test)!=max.result)
//			{
//				missed++;
//				System.out.println("Label is " +testLabels.get(test)+", model says "+max.result+", "+ max.noMatchedSample+" matched samples, "+String.format( "%.1f", max.percentCertainty ) + "% percent certainity");
//			}
//		}
//		System.out.println("Error rate "+missed*100./total);
//	}

	private void run()
	{
		//ResultFrame frame = new ResultFrame(1600, 600);
		TwoFullConnectedLayersBinary model = new TwoFullConnectedLayersBinary(300*200, 28*28);
		model.initWeightsFromImages(trainImages);
		int missed=0;
		int total=0;
		for (int test = 0; test<10000; test++)
		{
			total++;
			int indexMax = model.forwardStepIndex(testImages.get(test));
			if (testLabels.get(test)!=trainLabels.get(indexMax))
			{
				missed++;
			}
			if (test%100==99)
			{
				System.out.println("Error rate "+missed*100./total);
			}
		}
		
	}
	
	public MainBinary() throws IOException
	{
		loadImages();
	}
	private void loadImages() throws IOException
	{
		List<byte[]> imagesByte = ImageUtils.convertToBW(ImageUtils.readMnist("/mnist/train-images.idx3-ubyte"));
		for (byte[] bs : imagesByte) 
		{
			trainImages.add(new Image(bs));
		}
		trainLabels = ImageUtils.readMinstLabels("/mnist/train-labels.idx1-ubyte");
		
		imagesByte = ImageUtils.convertToBW(ImageUtils.readMnist("/mnist/t10k-images.idx3-ubyte"));
		for (byte[] bs : imagesByte) 
		{
			testImages.add(new Image(bs));
		}
		testLabels = ImageUtils.readMinstLabels("/mnist/t10k-labels.idx1-ubyte");
	}
}
