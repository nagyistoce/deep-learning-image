package home.mutant.deep.utils;

import home.mutant.deep.ui.Image;
import home.mutant.deep.utils.ImageUtils;
import home.mutant.deep.utils.ImageUtils.Style;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MnistDatabase
{
	public static List<Image> trainImages = new ArrayList<Image>();
	public static List<Integer> trainLabels  = new ArrayList<Integer>();

	public static List<Image> testImages = new ArrayList<Image>();
	public static List<Integer> testLabels  = new ArrayList<Integer>();
	public static Style style=Style.BW;
	
	public static void loadImagesBW() throws IOException
	{
		ImageUtils.loadImages(trainImages, testImages, trainLabels, testLabels, style=Style.BW);
	}
	public static void loadImages() throws IOException
	{
		ImageUtils.loadImages(trainImages, testImages, trainLabels, testLabels, style=Style.GREY);
	}
	
	public static void loadImagesCrop(int newImageSize) throws IOException
	{
		trainLabels.addAll(ImageUtils.readMinstLabels("/mnist/train-labels.idx1-ubyte"));
		testLabels.addAll(ImageUtils.readMinstLabels("/mnist/t10k-labels.idx1-ubyte"));
		trainImages.addAll(ImageUtils.readMnistAsImage("/mnist/train-images.idx3-ubyte", newImageSize));
		testImages.addAll(ImageUtils.readMnistAsImage("/mnist/t10k-images.idx3-ubyte", newImageSize));			
		
	}
	
	public static void loadGradientImages() throws IOException
	{
		MnistDatabase.loadImages();
		replaceImagesWithGradient(trainImages);
		replaceImagesWithGradient(testImages);
	}
	private static void replaceImagesWithGradient(List<Image> images)
	{
		for(int i=0;i<images.size();i++)
		{
			images.set(i, ImageUtils.gradientImage(images.get(i)));
		}
	}
}
