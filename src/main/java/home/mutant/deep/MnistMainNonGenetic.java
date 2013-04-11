package home.mutant.deep;


import home.mutant.deep.model.Image;
import home.mutant.deep.model.TwoFullConnectedLayers;
import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.ImageUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MnistMainNonGenetic
{
	private static final int NO_GENERATIONS = 5000;
	List<Image> images = new ArrayList<Image>();

	public static void main(String[] args) throws IOException
	{
		new MnistMainNonGenetic().run();
	}

	private void run()
	{
		ResultFrame frame = new ResultFrame(1600, 600);
		TwoFullConnectedLayers model = new TwoFullConnectedLayers(40, 28*28);
		
		//model.learnMaxGenerative(NO_GENERATIONS, images);
		model.initWeightsFromImages(images);
		frame.showModel(model,28);
		model.printGeneratives();
		System.out.println("Testing...");
				
		for (int i = 0; i<images.size();i++)
		{
			int test = model.test(images.get(i));
			System.out.println("The neuron "+test+" recognized the image " + i +" and generated the images "+model.generatives.get(test));
		}
	}

	public MnistMainNonGenetic() throws IOException
	{
		loadImages();
	}
	private void loadImages() throws IOException
	{
		List<byte[]> imagesByte = ImageUtils.convertToBW(ImageUtils.readMnist("/mnist/train-images.idx3-ubyte"));
		for (byte[] bs : imagesByte) 
		{
			images.add(new Image(bs));
		}
		images = images.subList(0, 40);
	}
}
