package home.mutant.logistic;

import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.MnistDatabase;
import home.mutant.weka.Arff;
import java.io.IOException;

public class RunLogisticNeuron {

	private static final int TRAIN_SAMPLES = 60000;
	private static final int OUTPUT_SAMPLES = 7000;
	private static final int NO_NEURONS = 400;

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
		createArffFromFeatures("LevelLogistic", net);
		for (int i=0;i<NO_NEURONS;i++)
		{
			for (int image=0;image<12;image++)
			System.out.println(net.neurons[i].output(MnistDatabase.trainImages.get(image).getDataOneDimensional())+" "+net.neurons[i].outputSigmoid(MnistDatabase.trainImages.get(image).getDataOneDimensional()));
			System.out.println();
		}
		ResultFrame frame = new ResultFrame(1300, 700);
		frame.showLogisticNet(net, 30, 1);
	}

    private static void createArffFromFeatures(String name, LogisticNet net) throws IOException {
        Arff featuresArff = new Arff(name, NO_NEURONS, 10, OUTPUT_SAMPLES);
        double[] features = new double[NO_NEURONS];
        int noSamples = 6*OUTPUT_SAMPLES/7;
        for (int i=0;i<noSamples;i++)
        {
            for (int n=0;n<NO_NEURONS;n++)
            {
            	features[n] = (int) (255*net.neurons[n].outputSigmoid(MnistDatabase.trainImages.get(i).getDataOneDimensional()));
            }
            featuresArff.addInstance(features, MnistDatabase.trainLabels.get(i));
        }
        
        noSamples = OUTPUT_SAMPLES - noSamples;
        for (int i=0;i<noSamples;i++)
        {
            for (int n=0;n<NO_NEURONS;n++)
            {
            	features[n] = (int) (255*net.neurons[n].outputSigmoid(MnistDatabase.testImages.get(i).getDataOneDimensional()));
            }
            featuresArff.addInstance(features, MnistDatabase.testLabels.get(i));
        }
        
        featuresArff.saveToArff();
    }

}
