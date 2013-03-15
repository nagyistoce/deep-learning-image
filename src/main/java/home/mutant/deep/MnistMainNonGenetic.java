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
	private static final int NO_GENERATIONS = 1000;
	List<Image> images = new ArrayList<Image>();

	public static void main(String[] args) throws IOException
	{
		List<Integer> neurons = new ArrayList<Integer>();
		neurons.add(50);
		neurons.add(28*28);
		new MnistMainNonGenetic().run();
	}

	private void run()
	{
		ResultFrame frame = new ResultFrame(1600, 600);
		TwoFullConnectedLayers model = new TwoFullConnectedLayers(50, 28*28);
		
		model.learnMaxGenerative(NO_GENERATIONS, images);
		
		frame.showModel(model,28);
		model.printGeneratives();
		System.out.println("Testing...");
				
		for (int i = 0; i<images.size();i++)
		{
			System.out.println("The neuron "+model.test(images.get(i))+" recognized the image " + i +" and generated the images "+model.generatives.get(model.test(images.get(i))));
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
		images = images.subList(0, 20);
	}
}
