package home.mutant.logistic;

import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.MnistDatabase;
import home.mutant.weka.Arff;

public class RunLogisticNeuron {

	private static final int TRAIN_SAMPLES = 60000;
	private static final int OUPUT_SAMPLES = 600;
	private static final int NO_NEURONS = 40;

	public static void main(String[] args) throws Exception 
	{
		MnistDatabase.loadImagesCrop(20);
		LogisticNet net = new LogisticNet(NO_NEURONS, 20*20);
		for (int i=0;i<TRAIN_SAMPLES;i++)
		{
//			for (int j = 0; j < net.neurons.length; j++)
//			{
//				if (Math.random()>0.5)
//					net.neurons[j].learn(MnistDatabase.trainImages.get(i).getDataOneDimensional());
//				else
//					net.neurons[j].unlearn(MnistDatabase.trainImages.get(i).getDataOneDimensional());
//			}
			net.neurons[(int) (Math.random()*NO_NEURONS)].learn(MnistDatabase.trainImages.get(i).getDataOneDimensional());
			net.neurons[(int) (Math.random()*NO_NEURONS)].unlearn(MnistDatabase.trainImages.get(i).getDataOneDimensional());
		}
		Arff featuresArff = new Arff("LevelLogistic", NO_NEURONS, 10, OUPUT_SAMPLES);
		double[] features = new double[NO_NEURONS];
		for (int i=0;i<OUPUT_SAMPLES;i++)
		{
			for (int n=0;n<NO_NEURONS;n++)
			{
				features[n] = net.neurons[n].output(MnistDatabase.trainImages.get(i).getDataOneDimensional());
			}
			featuresArff.addInstance(features, MnistDatabase.trainLabels.get(i));
		}
		
		featuresArff.saveToArff();
//		for (int i=0;i<100;i++)
//		{
//			System.out.println(net.neurons[i].output(MnistDatabase.trainImages.get(0).getDataOneDimensional()));
//			System.out.println(net.neurons[i].output(MnistDatabase.trainImages.get(1).getDataOneDimensional()));
//			System.out.println(net.neurons[i].output(MnistDatabase.trainImages.get(2).getDataOneDimensional()));
//			System.out.println(net.neurons[i].output(MnistDatabase.trainImages.get(3).getDataOneDimensional()));
//			System.out.println(net.neurons[i].output(MnistDatabase.trainImages.get(4).getDataOneDimensional()));
//			System.out.println(net.neurons[i].output(MnistDatabase.trainImages.get(5).getDataOneDimensional()));
//			System.out.println(net.neurons[i].output(MnistDatabase.trainImages.get(11).getDataOneDimensional()));
//			System.out.println();
//		}
		ResultFrame frame = new ResultFrame(1300, 700);
		frame.showLogisticNet(net, 30, 1);
	}

}
