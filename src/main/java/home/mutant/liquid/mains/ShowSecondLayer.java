package home.mutant.liquid.mains;

import java.io.IOException;
import java.util.List;

import home.mutant.deep.ui.Image;
import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.MnistDatabase;
import home.mutant.liquid.cells.NeuronCell;
import home.mutant.liquid.networks.SimpleNet;

public class ShowSecondLayer
{
	public static void main(String[] args) throws IOException, InterruptedException
	{
		int subImageX=5;
		int subImageStep = 2;
		int filtersMapSize = 10;
		SimpleNet net = ShowNetworkWeightsSubImages.train(subImageX, subImageStep);
		while(net.neurons.size()>filtersMapSize*filtersMapSize)
		{
			net.neurons.remove((int)(net.neurons.size()*Math.random()));
		}
		System.out.println(net.neurons.size());
		
		ResultFrame frame = new ResultFrame(1200, 1080);
		frame.showNetworkWeights(net, 1200/(subImageX+1),1);
		
		
		Image image = MnistDatabase.trainImages.get(0);
		List<byte[]> subImages = image.divideImage(subImageX, subImageX, subImageStep, subImageStep);
		int subImagesSize = (int) Math.sqrt(subImages.size());
		Image secondLayer = new Image(filtersMapSize*subImagesSize,filtersMapSize*subImagesSize);
		int indexSubImage=0;
		for (int indexImageX=0;indexImageX<subImagesSize;indexImageX++)
		{
			for (int indexImageY=0;indexImageY<subImagesSize;indexImageY++)
			{
				int indexFilter=0;
				for (int filterX=0;filterX<filtersMapSize;filterX++)
				{
					for (int filterY=0;filterY<filtersMapSize;filterY++)
					{
						NeuronCell filter = net.neurons.get(indexFilter);
						if (filter.isFiring(subImages.get(indexSubImage)))
						{
							secondLayer.setPixel(indexImageX*filtersMapSize+filterX, indexImageY*filtersMapSize+filterY, (byte) 255);
						}
						indexFilter++;
					}
				}
				indexSubImage++;
			}
		}
		
		ResultFrame frame2 = new ResultFrame(1200, 1080);
		frame2.showImage(secondLayer);
	}

}
