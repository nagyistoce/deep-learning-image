package home.mutant.deep.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageUtils 
{
	public static void readResourceImage(String imageResourcePath, byte[] inValues) throws IOException 
	{
		readImage(ImageIO.read(ImageUtils.class.getResourceAsStream(imageResourcePath)), inValues);
	}
	public static void readImage(BufferedImage image, byte[] inValues) throws IOException 
	{
		int offset=0;
		for (int y = 0; y < image.getHeight(); y++) 
		{
			for (int x = 0; x < image.getWidth(); x++) 
			{
                int c = image.getRGB(x,y);
                int  color = (c & 0x00ffffff);
                
                inValues[offset]=0;
                if (color>0)
                {
                	inValues[offset]=1;
                }
                offset++;
			}
		}
	}
	
	public static List<byte[][]> readMnist(String imageResourcePath)
	{
		List<byte[][]> images = new ArrayList<byte[][]>();
		InputStream stream = ImageUtils.class.getResourceAsStream(imageResourcePath);
		byte[] bRead = new byte[16];
		try
		{
			stream.read(bRead);
		} catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		bRead = new byte[28*28];
		while(true)
		{
			byte[][] image = new byte[28][28];
			int offsetRead = 0;
			try
			{
				if (stream.read(bRead)!=28*28)
				{
					break;
				}
			} 
			catch (IOException e)
			{
				e.printStackTrace();
				break;
			}
			for (int i = 0;i<28;i++)
			{
				for (int j = 0;j<28;j++)
				{
					image[j][i] = bRead[offsetRead++];
				}				
			}
			images.add(image);
		}
		return images;
	}
	
	public static List<byte[]> convertToBW(List<byte[][]> images)
	{
		List<byte[]> newImages = new ArrayList<byte[]>();
		for (byte[][] image : images)
		{
			int offset = 0;
			byte[] newImage = new byte[28*28];
			for (int i = 0;i<28;i++)
			{
				for (int j = 0;j<28;j++)
				{
					if (image[j][i]<0)
					{
						newImage[offset++]=(byte) 1;
						image[j][i]=(byte) 1;
					}
					else
					{
						newImage[offset++]=(byte) 0;
						image[j][i]=(byte) 0;
					}
				}				
			}
			newImages.add(newImage);
		}
		return newImages;
	}
}
