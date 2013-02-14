package home.mutant.deep;


import home.mutant.deep.model.WeightsModel;
import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.ImageUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MnistMainNonGenetic
{
	private static final int NO_GENERATIONS = 1000;
	List<byte[]> images = new ArrayList<byte[]>();
	List<Integer> numberNeuronsPerLayer;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		List<Integer> neurons = new ArrayList<Integer>();
		neurons.add(50);
		neurons.add(28*28);
		new MnistMainNonGenetic(neurons).run();
	}

	private void run()
	{
		ResultFrame frame = new ResultFrame(1600, 600);
		WeightsModel model = new WeightsModel(numberNeuronsPerLayer, false);
		
		for (int i=0; i<NO_GENERATIONS;i++)
		{
			//model.learnStepNonGenerative(images);
			model.learnStepMaxGenerative(images);
		}
		frame.showModel(model,28);
		model.printGeneratives();
		System.out.println("Testing...");
				
		for (int i = 0; i<images.size();i++)
		{
			System.out.println("The neuron "+model.test(images.get(i))+" recognized the image " + i +" and generated the images "+model.generatives.get(model.test(images.get(i))));
		}
	}

	public MnistMainNonGenetic(List<Integer> numberNeuronsPerLayer) throws IOException
	{
		this.numberNeuronsPerLayer = new ArrayList<Integer>(numberNeuronsPerLayer);
		loadImages();
	}
	private void loadImages() throws IOException
	{
		images = ImageUtils.convertToBW(ImageUtils.readMnist("/mnist/train-images.idx3-ubyte"));
		images = images.subList(0, 20);
	}
}
