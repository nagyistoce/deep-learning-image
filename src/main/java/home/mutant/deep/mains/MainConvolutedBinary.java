package home.mutant.deep.mains;


import home.mutant.deep.networks.ThreeConvolutedLayersBinary;
import home.mutant.deep.ui.Image;
import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.ImageUtils;

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

	private void run()
	{
		ThreeConvolutedLayersBinary model = new ThreeConvolutedLayersBinary(28,4,50,60000);
		System.out.println("START");
		model.initWeightsFromImages(trainImages.subList(0, 20000));
		ResultFrame frame = new ResultFrame(1600, 600);
		//frame.showBinaryColumn(model.bottom.column);
		//frame.showImage(model.bottom.reconstruct(testImages.get(0)));
		frame.showImage(model.bottom.forwardStep(testImages.get(0)));
		int missed=0;
		int total=0;
		for (int test = 0; test<100; test++)
		{
			total++;
			int indexMax = model.forwardStepIndex(testImages.get(test));
			//long max = model.forwardStepMax(testImages.get(test));
			if (testLabels.get(test)!=trainLabels.get(indexMax))
			{
				missed++;
			}
			System.out.println("Label is " +testLabels.get(test)+", model says "+trainLabels.get(indexMax));
			if (test%100==99)
			{
				System.out.println("Error rate "+missed*100./total);
			}
		}
		System.out.println();
		System.out.println("FINAL error rate "+missed*100./total);
	}
	
	public MainConvolutedBinary() throws IOException
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
