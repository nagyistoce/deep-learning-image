package home.mutant.deep.mains;

import home.mutant.deep.mains.MainTwoLayers.Style;
import home.mutant.deep.networks.TwoSparseConnectedLayers;
import home.mutant.deep.neurons.ByteNeuron;
import home.mutant.deep.ui.Image;
import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.ImageUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainTwoSparseLayers 
{
	List<Image> trainImages = new ArrayList<Image>();
	List<Integer> trainLabels  = new ArrayList<Integer>();

	List<Image> testImages = new ArrayList<Image>();
	List<Integer> testLabels  = new ArrayList<Integer>();
	
	public static void main(String[] args) throws IOException
	{
		new MainTwoSparseLayers().run();
	}

	private void run() throws IOException 
	{
		TwoSparseConnectedLayers layer = new TwoSparseConnectedLayers(300, 28, ByteNeuron.class);
		ImageUtils.loadImages(trainImages, testImages, trainLabels, testLabels, Style.BW);
		for (int i=0; i<10;i++)
		{
			layer.forwardStep(trainImages.get(0));
			//layer.forwardStep(trainImages.get(1));
		}
		//ResultFrame frame1 = new ResultFrame(400, 400, "Frame blank image");
		//frame1.showImage(layer.forwardStep(new Image(28, 28)));
		ResultFrame frame2 = new ResultFrame(400, 400, "Frame image 0");
		frame2.showImage(layer.forwardStep(trainImages.get(0)));
		ResultFrame frame3 = new ResultFrame(400, 400, "Frame image 1");
		frame3.showImage(layer.forwardStep(trainImages.get(0)));
		ResultFrame frame4 = new ResultFrame(400, 400, "Frame image 2");
		frame4.showImage(layer.forwardStep(trainImages.get(0)));
		ResultFrame frame5 = new ResultFrame(400, 400, "Frame image 3");
		frame5.showImage(layer.forwardStep(trainImages.get(1)));
	}
}
