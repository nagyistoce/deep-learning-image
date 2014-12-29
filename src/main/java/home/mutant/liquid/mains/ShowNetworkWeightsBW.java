package home.mutant.liquid.mains;

import java.io.IOException;

import home.mutant.deep.ui.Image;
import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.MnistDatabase;
import home.mutant.liquid.cells.NeuronCell;
import home.mutant.liquid.networks.SimpleNet;

public class ShowNetworkWeightsBW 
{
	public static void main(String[] args) throws IOException
	{
		ResultFrame frame = new ResultFrame(1800, 1200);
		SimpleNet net = new SimpleNet();
		MnistDatabase.loadImagesBW();
		for (int imageIndex=0;imageIndex<60000;imageIndex++)
		{
			NeuronCell found = null;
			Image testImage = MnistDatabase.trainImages.get(imageIndex);
			for (NeuronCell neuron : net.neurons)
			{
				if (neuron.isFiringBW(testImage))
				{
					found=neuron;
					break;
				}
			}
			if (found == null)
			{
				found = new NeuronCell(784);
				net.neurons.add(found);
			}
			found.modifyWeightsBW(testImage);
			found.recognized.add(MnistDatabase.trainLabels.get(imageIndex));
			found.lastRecognized=MnistDatabase.trainLabels.get(imageIndex);
		}
		//frame.showNetworkWeightsBW(net, 60);
		System.out.println(net.neurons.size());
		int count=0;
		for (int imageIndex=0;imageIndex<10000;imageIndex++)
		{
			SimpleNet netTest = new SimpleNet();
			System.out.println(MnistDatabase.testLabels.get(imageIndex));
			for (NeuronCell neuron : net.neurons)
			{
				
				if (neuron.isFiringBW(MnistDatabase.testImages.get(imageIndex)))
				{
					netTest.neurons.add(neuron);
				}
			}
			if (netTest.neurons.size()==0)
				count++;
			for (NeuronCell neuron : netTest.neurons)
			{
				System.out.println(neuron.lastRecognized);
			}
			System.out.println();
		}
		System.out.println(count);
		//frame.showNetworkWeights(netTest, 60);
	}
}
