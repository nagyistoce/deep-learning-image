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
	public enum Style {BW, GREY};
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
		byte[] bRead;
		try
		{
			readExactly(stream, 16);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		while(true)
		{
			byte[][] image = new byte[28][28];
			
			try
			{
				bRead = readExactly(stream, 28*28);
				if (bRead == null)
				{
					break;
				}
			} 
			catch (IOException e)
			{
				e.printStackTrace();
				break;
			}
			int offsetRead = 0;
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
	
	static byte[] readExactly(InputStream is, int noBytes) throws IOException
	{
		byte[] res = new byte[noBytes];
		int offset=0;
		int read=0;
		while(offset<noBytes)
		{
			read = is.read(res,offset,noBytes-offset);
			offset+=read;
			if (read == -1)
			{
				return null;
			}
		}
		return res;
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
		for (int y=0; y<=actualY-newY; y+=stepY)
		{
			for (int x=0; x<=actualX-newX; x+=stepX)
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
	
	public static void checkCollisions(Image image1, Image image2)
	{
		byte[] img1 = image1.getDataOneDimensional();
		byte[] img2 = image2.getDataOneDimensional();
		int s1=0, s2=0, s12=0;
		for (int i = 0; i < img2.length; i++)
		{
			if (i>=img1.length) break;
			if (img1[i]!=0) s1++;
			if (img2[i]!=0) s2++;
			if (img1[i]!=0 && (img2[i]!=0)) s12++;
		}
		System.out.println("s1="+s1 +" - " +((float)s1*100.)/img1.length+"%");
		System.out.println("s2="+s2 +" - " +((float)s2*100.)/img1.length+"%");
		System.out.println("s12="+s12 +" - " +((float)s12*100.)/img1.length+"%");
		System.out.println();
	}
	public static void loadImages(List<Image> trainImages, List<Image> testImages, List<Integer> trainLabels, List<Integer> testLabels, Style style) throws IOException
	{
		trainLabels.addAll(ImageUtils.readMinstLabels("/mnist/train-labels.idx1-ubyte"));
		testLabels.addAll(ImageUtils.readMinstLabels("/mnist/t10k-labels.idx1-ubyte"));
		if (style == Style.BW)
		{
			trainImages.addAll(ImageUtils.readMnistAsBWImage("/mnist/train-images.idx3-ubyte"));
			testImages.addAll(ImageUtils.readMnistAsBWImage("/mnist/t10k-images.idx3-ubyte"));
		}
		else
		{
			trainImages.addAll(ImageUtils.readMnistAsImage("/mnist/train-images.idx3-ubyte"));
			testImages.addAll(ImageUtils.readMnistAsImage("/mnist/t10k-images.idx3-ubyte"));			
		}
	}
	public static Image gradientImage(Image image)
	{
		Image dest = new Image(image.imageX, image.imageY);
		for (int x=1;x<image.imageX-1;x++)
		{
			for (int y=1;y<image.imageY-1;y++)
			{
				int y1 = image.getPixelInt(x, y-1);
				y1-= image.getPixelInt(x, y+1);
				int x1 = image.getPixelInt(x-1, y);
				x1-= image.getPixelInt(x+1, y);
				dest.setPixel(x,y,(byte)Math.sqrt((y1*y1+x1*x1)/2.));
			}
		}
		for (int x=1;x<image.imageX-1;x++)
		{
			int y1 = -1*image.getPixelInt(x, 1);
			int x1 = image.getPixelInt(x-1, 0);
			x1-= image.getPixelInt(x+1, 0);
			dest.setPixel(x,0,(byte)Math.sqrt((y1*y1+x1*x1)/2.));
			
			y1 = image.getPixelInt(x, image.imageY-2);
			x1 = image.getPixelInt(x-1, image.imageY-1);
			x1-= image.getPixelInt(x+1, image.imageY-1);
			dest.setPixel(x,image.imageY-1,(byte)Math.sqrt((y1*y1+x1*x1)/2.));
		}
		
		for (int y=1;y<image.imageY-1;y++)
		{
			int y1 = -1*image.getPixelInt(1, y);
			int x1 = image.getPixelInt(0,y-1);
			x1-= image.getPixelInt(0,y+1);
			dest.setPixel(0,y,(byte)Math.sqrt((y1*y1+x1*x1)/2.));
			
			y1 = image.getPixelInt(image.imageX-2,y);
			x1 = image.getPixelInt(image.imageX-1, y-1);
			x1-= image.getPixelInt(image.imageX-1, y+1);
			dest.setPixel(image.imageX-1,y,(byte)Math.sqrt((y1*y1+x1*x1)/2.));
		}
		int x1=-1*image.getPixelInt(0,1);
		int y1=-1*image.getPixelInt(1,0);
		dest.setPixel(0,0,(byte)Math.sqrt((y1*y1+x1*x1)/2.));
		
		x1=image.getPixelInt(image.imageX-2,0);
		y1=-1*image.getPixelInt(image.imageX-1,1);
		dest.setPixel(image.imageX-1,0,(byte)Math.sqrt((y1*y1+x1*x1)/2.));
		
		x1=image.getPixelInt(0,image.imageY-2);
		y1=-1*image.getPixelInt(1,image.imageY-1);
		dest.setPixel(0,image.imageY-1,(byte)Math.sqrt((y1*y1+x1*x1)/2.));
		
		x1=image.getPixelInt(image.imageX-1,image.imageY-2);
		y1=-1*image.getPixelInt(image.imageX-2,image.imageY-1);
		dest.setPixel(image.imageX-1,image.imageY-1,(byte)Math.sqrt((y1*y1+x1*x1)/2.));
		
		return dest;
	}
}
