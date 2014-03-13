package home.mutant.reinforcement;

import home.mutant.deep.ui.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils 
{
	public static final int SEED = 1;
	public static List<Integer> createValuesFromImage(Image image, int noValues, int noPixelsPerValue)
	{
		List<Integer> values = new ArrayList<Integer>();
		Random rnd = new Random(SEED);
		for (int i=0;i<noValues;i++)
		{
			int value=0;
			for (int j=0;j<noPixelsPerValue;j++)
			{
				if( image.getPixel(rnd.nextInt(image.imageX),rnd.nextInt(image.imageY))>0)
					value++;
			}
			values.add(value);
		}
		return values;
	}
}
