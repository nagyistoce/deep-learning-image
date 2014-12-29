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
}
