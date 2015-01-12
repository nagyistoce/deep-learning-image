package home.mutant.liquid.mains;

import home.mutant.deep.ui.Image;
import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.MnistDatabase;
import home.mutant.liquid.cells.NeuronCell;
import home.mutant.liquid.networks.SimpleNet;

import java.io.IOException;
import java.util.List;

public class ShowNetworkSubImagesDistanceMap 
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
				double minDistance = Double.MAX_VALUE;
				int indexNeuronMinDistance = -1;
				for (int neuronIndex = 0;neuronIndex<net.neurons.size(); neuronIndex++)
				{
					NeuronCell neuron = net.neurons.get(neuronIndex);
					double distanceFromImage = neuron.getDistanceFromImage(subImage);
					if (distanceFromImage<=0)
					{
						found=neuron;
						break;
					}
					else
					{
						if (distanceFromImage<minDistance)
						{
							minDistance = distanceFromImage;
							indexNeuronMinDistance = neuronIndex;
						}
					}
				}
				if (found == null)
				{
					found = new NeuronCell(subImageX*subImageX);
					found.minDistance = minDistance;
					found.indexNeuronMinDistance = indexNeuronMinDistance;
					net.neurons.add(found);
					found.modifyWeights(subImage);
				}
				
				//found.recognized.add(MnistDatabase.trainLabels.get(imageIndex));
				found.lastRecognized=MnistDatabase.trainLabels.get(imageIndex);
			}
		}
		for (NeuronCell neuron:net.neurons)
		{
			int xOffset=0;
			int yOffset=0;
			if (neuron.indexNeuronMinDistance!=-1)
			{
				NeuronCell neuronCell = net.neurons.get(neuron.indexNeuronMinDistance);
				xOffset+=neuronCell.xShow;
				yOffset+=neuronCell.yShow;
			}
			frame.addWeightsToFrame(neuron, xOffset, yOffset);
		}
		frame.repaint();
		System.out.println(net.neurons.size());
		System.out.println( net.neurons.get(10).getDistanceFromNeuron(net.neurons.get(11)));
		System.out.println( net.neurons.get(11).getDistanceFromNeuron(net.neurons.get(10)));
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
