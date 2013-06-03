package home.mutant.deep.mains;


import home.mutant.deep.abstracts.Neuron;
import home.mutant.deep.model.IndexValue;
import home.mutant.deep.networks.TwoFullConnectedLayers;
import home.mutant.deep.neurons.BinaryNeuron;
import home.mutant.deep.neurons.ByteNeuron;
import home.mutant.deep.ui.Image;
import home.mutant.deep.utils.ImageUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainTwoLayers
{
	public enum Style {BW, GREY};
	List<Image> trainImages = new ArrayList<Image>();
	List<Integer> trainLabels  = new ArrayList<Integer>();

	List<Image> testImages = new ArrayList<Image>();
	List<Integer> testLabels  = new ArrayList<Integer>();
	public Style style=Style.BW;
	public static void main(String[] args) throws IOException
	{
		new MainTwoLayers(Style.GREY).run();
	}

	private void run()
	{
		Class<? extends Neuron> clazz = null;
		if (style == Style.BW)
		{
			clazz = BinaryNeuron.class;
		}
		else
		{
			clazz = ByteNeuron.class;
		}
		TwoFullConnectedLayers model = new TwoFullConnectedLayers(300*200, 28*28,clazz);
		model.initWeightsFromImages(trainImages.subList(0, 60000));
		int missedAverage=0;
		int missedMax=0;
		int total=0;
		for (int test = 0; test<1000; test++)
		{
			total++;
			//int indexMax = model.forwardStepIndex(testImages.get(test));
			List<IndexValue> indexes=model.forwardStepMultipleIndexWithValues(testImages.get(test), 10);
			//if (testLabels.get(test)!=trainLabels.get(indexes.get(0).index))
			if (testLabels.get(test)!=getMaxAverage(indexes))
			{
				missedAverage++;
				System.out.println("AVERAGE Label is " +testLabels.get(test)+", model says "+printIndexes(indexes));
			}
			if (testLabels.get(test)!=trainLabels.get(indexes.get(0).index))
			{
				missedMax++;
				System.out.println("MAX Label is " +testLabels.get(test)+", model says "+trainLabels.get(indexes.get(0).index));
			}			
			if (test%100==99)
			{
				System.out.println("Average "+String.format( "%.2f", missedAverage*100./total )+"%");
				System.out.println("Max "+String.format( "%.2f", missedMax*100./total )+"%");
			}
		}
	}
	
	public String printIndexes(List<IndexValue> indexes)
	{
		String res="";
		for (IndexValue indexVal: indexes) 
		{
			res+=trainLabels.get(indexVal.index)+":"+indexVal.value+ ", ";
		}
		return res;
	}
	
	public int getMaxAverage(List<IndexValue> indexes)
	{
		Map<Integer, Double> average = new HashMap<Integer,Double>();
		for (IndexValue indexValue : indexes) 
		{
			if (average.get(trainLabels.get(indexValue.index))==null)
			{
				average.put(trainLabels.get(indexValue.index),0.);
			}
			average.put(trainLabels.get(indexValue.index),average.get(trainLabels.get(indexValue.index))+indexValue.value*indexValue.value);
		}
		
		double max=0;
		int label=-1;
		for (Integer key : average.keySet()) 
		{
			if (max<average.get(key))
			{
				max = average.get(key);
				label = key;
			}
		}
		return label;
	}
	
	public MainTwoLayers(Style style) throws IOException
	{
		this.style = style;
		loadImages();
	}
	private void loadImages() throws IOException
	{
		trainLabels = ImageUtils.readMinstLabels("/mnist/train-labels.idx1-ubyte");
		testLabels = ImageUtils.readMinstLabels("/mnist/t10k-labels.idx1-ubyte");
		if (style == Style.BW)
		{
			trainImages = ImageUtils.readMnistAsBWImage("/mnist/train-images.idx3-ubyte");
			testImages = ImageUtils.readMnistAsBWImage("/mnist/t10k-images.idx3-ubyte");
		}
		else
		{
			trainImages = ImageUtils.readMnistAsImage("/mnist/train-images.idx3-ubyte");
			testImages = ImageUtils.readMnistAsImage("/mnist/t10k-images.idx3-ubyte");			
		}
	}
}
