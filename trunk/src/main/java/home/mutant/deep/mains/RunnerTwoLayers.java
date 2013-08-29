package home.mutant.deep.mains;

import java.util.ArrayList;
import java.util.List;

import home.mutant.deep.model.IndexValue;
import home.mutant.deep.networks.TwoFullConnectedLayers;
import home.mutant.deep.ui.Image;
import home.mutant.deep.utils.ImageUtils;

public class RunnerTwoLayers implements Runnable 
{
	Thread thread;
	public TwoFullConnectedLayers model;
	public List<Image> testImages = new ArrayList<Image>();
	public List<Integer> testLabels  = new ArrayList<Integer>();
	public List<Integer> trainLabels  = new ArrayList<Integer>();
	public int missedMax=0;
	
	
	public RunnerTwoLayers(TwoFullConnectedLayers model,
			List<Image> testImages, List<Integer> testLabels,
			List<Integer> trainLabels) 
	{
		super();
		this.model = model;
		this.testImages = testImages;
		this.testLabels = testLabels;
		this.trainLabels = trainLabels;
		thread = new Thread(this);
		thread.start();
	}

	public void join()
	{
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() 
	{
		
		int total=0;
		for (int test = 0; test<testLabels.size(); test++)
		{
			total++;
			IndexValue indexValueMax = model.forwardStepIndexValue(testImages.get(test));
			IndexValue indexValueMaxBack = indexValueMax;
			for (int x=-3;x<=3;x++)
			{
				for (int y=-3;y<=3;y++)
				{
					for (double theta=-0.5;theta<0.5;theta+=0.1)
					{
						IndexValue indexValue = model.forwardStepIndexValue(ImageUtils.affineTransform(testImages.get(test), x, y,theta));
						if (indexValue.value>indexValueMax.value)
						{
							indexValueMax = indexValue;
						}
					}
				}
			}
			if (testLabels.get(test)!=trainLabels.get(indexValueMax.index))
			{
				missedMax++;
				synchronized(model)
				{
					System.out.println("Label is " +testLabels.get(test)+", model affine says "+trainLabels.get(indexValueMax.index)+", model max says "+trainLabels.get(indexValueMaxBack.index)+" "+indexValueMax.value+" "+indexValueMaxBack.value);
				}
			}			
			if (test%100==99)
			{
				synchronized(model)
				{
					System.out.println("Error rate  "+String.format( "%.2f", missedMax*100./total )+"% from "+total);
				}
			}
		}

	}

}
