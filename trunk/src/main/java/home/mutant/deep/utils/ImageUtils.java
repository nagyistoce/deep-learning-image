package home.mutant.deep.utils;

import home.mutant.deep.ui.Image;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
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
	
	public static List<Integer> readMinstLabels(String labelsResourcePath)
	{
		List<Integer> labels = new ArrayList<Integer>();
		InputStream stream = ImageUtils.class.getResourceAsStream(labelsResourcePath);
		byte[] bRead = new byte[8];
		try
		{
			stream.read(bRead);
		} 
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		bRead = new byte[1];
		while(true)
		{
			try
			{
				if (stream.read(bRead)!=1)
				{
					break;
				}
			} 
			catch (IOException e)
			{
				e.printStackTrace();
				break;
			}
			labels.add((int)bRead[0]);
		}
		return labels;
	}
	
	public static List<Image> readMnistAsImage(String imageResourcePath)
	{
		List<byte[][]> bytes = readMnist(imageResourcePath);
		List<Image> images = new ArrayList<Image>();
		for (byte[][] bs : bytes) 
		{
			images.add(new Image(bs));
		}
		return images;
	}
	
	public static List<Image> readMnistAsBWImage(String imageResourcePath)
	{
		List<byte[]> bytes =convertToBW(readMnist(imageResourcePath));
		List<Image> images = new ArrayList<Image>();
		for (byte[] bs : bytes) 
		{
			images.add(new Image(bs));
		}
		return images;
	}
	
	public static List<byte[][]> readMnist(String imageResourcePath)
	{
		List<byte[][]> images = new ArrayList<byte[][]>();
		InputStream stream = ImageUtils.class.getResourceAsStream(imageResourcePath);
		byte[] bRead = new byte[16];
		try
		{
			stream.read(bRead);
		}
		catch (IOException e1)
		{
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
					image[i][j] = bRead[offsetRead++];
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
					if (image[i][j]!=0)
					{
						newImage[offset++]=(byte) 255;
						image[i][j]=(byte) 255;
					}
					else
					{
						newImage[offset++]=(byte) 0;
						image[i][j]=(byte) 0;
					}
				}				
			}
			newImages.add(newImage);
		}
		return newImages;
	}
	
	public static List<byte[]> divideImage(byte[] image, int newX, int newY, int actualX, int actualY)
	{
		return divideImage(image, newX, newY, actualX, actualY, newX, newY);
	}
	
	public static List<byte[]> divideImage(byte[] image, int newX, int newY, int actualX, int actualY, int stepX, int stepY)
	{
		List<byte[]> dividedImages = new ArrayList<byte[]>();
		for (int y=0; y<actualY; y+=stepY)
		{
			for (int x=0; x<actualX; x+=stepX)
			{
				byte[] newImage = new byte[newX*newY];
				int offsetImage = 0;
				for (int imageY = y; imageY<y+newY; imageY++)
				{
					for (int imageX = x; imageX<x+newX; imageX++)
					{
						newImage[offsetImage++] = image[imageY*actualX+imageX];
					}
				}
				dividedImages.add(newImage);
			}		
		}
		return dividedImages;
	}
	public static Image affineTransform(Image image, int offsetX, int offsetY, double theta)
	{
		AffineTransform tx = new AffineTransform();
		tx.translate(offsetX, offsetY);
		tx.rotate(theta, image.imageX/2, image.imageY/2);
		Image dest = new Image(image.imageX, image.imageY);
		for (int x=0;x<dest.imageX;x++)
		{
			for (int y=0;y<dest.imageY;y++)
			{
				Point ptDst = new Point(x, y);
				try 
				{
					tx.inverseTransform(new Point(x, y), ptDst);
					if (ptDst.x>=0 && ptDst.x<image.imageX && ptDst.y>=0 && ptDst.y<image.imageY)
					{
						dest.setPixel(x, y, image.getPixel(ptDst.x, ptDst.y));
					}
					else
					{
						dest.setPixel(x, y, (byte)0);
					}
				} 
				catch (NoninvertibleTransformException e) 
				{
					dest.setPixel(x, y, (byte)0);
				}
			}
		}
		return dest;
	}
}
