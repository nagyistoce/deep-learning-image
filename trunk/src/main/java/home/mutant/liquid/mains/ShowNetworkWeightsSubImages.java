package home.mutant.liquid.mains;

import home.mutant.deep.ui.Image;
import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.MnistDatabase;
import home.mutant.liquid.cells.NeuronCell;
import home.mutant.liquid.cells.NeuronCellGrey;
import home.mutant.liquid.cells.NeuronCellGreyDifference;
import home.mutant.liquid.networks.SimpleNet;

import java.io.IOException;
import java.util.List;

public class ShowNetworkWeightsSubImages 
{
	public static void main(String[] args) throws IOException
	{
		ResultFrame frame = new ResultFrame(1900, 1080);
		SimpleNet net = new SimpleNet();
		MnistDatabase.loadImages();
		int subImageX=7;
		int subImageStep = 4;
		for (int imageIndex=0;imageIndex<600;imageIndex++)
		{
			
			Image trainImage = MnistDatabase.trainImages.get(imageIndex);
			List<byte[]> subImages = trainImage.divideImage(subImageX, subImageX, subImageStep, subImageStep);
			for (byte[] subImage : subImages) 
			{
				NeuronCell found = null;
				for (NeuronCell neuron : net.neurons)
				{
					if (neuron.isFiring(subImage))
					{
						found=neuron;
						break;
					}
				}
				if (found == null)
				{
					found = new NeuronCellGreyDifference(subImageX*subImageX);
					net.neurons.add(found);
					found.modifyWeights(subImage);
				}
				
				//found.recognized.add(MnistDatabase.trainLabels.get(imageIndex));
				found.lastRecognized=MnistDatabase.trainLabels.get(imageIndex);
			}
		}
		frame.showNetworkWeights(net, 1900/(subImageX));
		System.out.println(net.neurons.size());
//		int count=0;
//		for (int imageIndex=0;imageIndex<10000;imageIndex++)
//		{
//			SimpleNet netTest = new SimpleNet();
//			System.out.println(MnistDatabase.testLabels.get(imageIndex));
//			for (NeuronCell neuron : net.neurons)
//			{
//				
//				if (neuron.isFiringBW(MnistDatabase.testImages.get(imageIndex)))
//				{
//					netTest.neurons.add(neuron);
//				}
//			}
//			if (netTest.neurons.size()==0)
//				count++;
//			for (NeuronCell neuron : netTest.neurons)
//			{
//				System.out.println(neuron.lastRecognized);
//			}
//			System.out.println();
//		}
//		System.out.println(count);
		//frame.showNetworkWeights(netTest, 60);
	}
}
