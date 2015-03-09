package home.mutant.zoom;

import java.util.List;

import home.mutant.deep.ui.Image;
import home.mutant.deep.utils.ImageUtils;
import home.mutant.deep.utils.MnistDatabase;
import home.mutant.zoom.model.Level;
import home.mutant.zoom.model.PlacedFeature;

public class RunZoom 
{
	public static void main(String[] args) throws Exception
	{
		MnistDatabase.loadImages();
		Level level = new Level();
		for (int indexImage=0;indexImage<60000;indexImage++)
		{
			Image image = ImageUtils.gradientImage(ImageUtils.scaleImage(MnistDatabase.trainImages.get(indexImage), 0.25));
			level.addFeature(image.getDataOneDimensional(), 0,0, MnistDatabase.trainLabels.get(indexImage));
		}
		
		for (int indexImage=0;indexImage<60000;indexImage++)
		{
			Image image = ImageUtils.gradientImage(ImageUtils.scaleImage(MnistDatabase.trainImages.get(indexImage), 0.25));
			level.processFeature(image.getDataOneDimensional(), 0,0, MnistDatabase.trainLabels.get(indexImage));
		}
		System.out.println(level.statistic.size());
		for (PlacedFeature pFeature:level.statistic.keySet())
		{
			System.out.println(level.statistic.get(pFeature).correlations);
		}
		testSet(level, MnistDatabase.trainImages, MnistDatabase.trainLabels);
		testSet(level, MnistDatabase.testImages, MnistDatabase.testLabels);
	}

	private static void testSet(Level level, List<Image> images, List<Integer> labels) 
	{
		int errCount=0;
		int testSet=1000;
		for (int indexImage=0;indexImage<testSet;indexImage++)
		{
			Image image = ImageUtils.gradientImage(ImageUtils.scaleImage(images.get(indexImage), 0.25));
			
			Integer label = labels.get(indexImage);
			int prediction = level.getCorrelation(image.getDataOneDimensional());
			if (prediction!=label)
			{
				//System.out.println(label +": "+prediction);
				errCount++;
			}
		}
		System.out.println("Error rate: "+errCount*100./testSet+"%");
	}
}
