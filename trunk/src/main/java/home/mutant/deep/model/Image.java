package home.mutant.deep.model;

import home.mutant.deep.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class Image 
{
	private int imageX,imageY;
	byte[] data;
	
	public Image(byte[] data, int x, int y)
	{
		this.imageX=x;
		this.imageY=y;
		this.data = data;
	}
	public Image(byte[] data)
	{
		this(data, (int)Math.sqrt(data.length), (int)Math.sqrt(data.length));
	}
	public Image(double[] dataDouble, int x, int y)
	{
		this.data = new byte[x*y];
		for (int i = 0; i < dataDouble.length; i++) 
		{
			this.data[i] = Double.valueOf(dataDouble[i]).byteValue();
		}
		this.imageX = x;
		this.imageY = y;
	}
	public Image(int x, int y)
	{
		this.data = new byte[x*y];
		this.imageX = x;
		this.imageY = y;
	}
	
	public Image(double[] data) 
	{
		this(data, (int)Math.sqrt(data.length), (int)Math.sqrt(data.length));
	}
	public byte[] getDataOneDimensional()
	{
		return data;
	}
	
	public byte[][] getDataTwoDimensional()
	{
		byte[][] data2 = new byte[imageX][imageY];
		for (int i=0; i<data.length; i++)
		{
			int x = i%imageX;
			int y = i/imageX;
			data2[x][y] = data[i];
		}
		return data2;
	}
	
	public List<Image> divideImage(int newX, int newY)
	{
		List<byte[]> dividedBytes = ImageUtils.divideImage(data, newX, newY, imageX, imageY);
		List<Image> dividedImages = new ArrayList<Image>();
		for (byte[] bs : dividedBytes) 
		{
			Image img = new Image(bs);
			dividedImages.add(img);
		}
		return dividedImages;
	}
	
	public void setPixel(int setX, int setY, byte value)
	{
		data[setY*imageX+setX] = value;
	}
	public byte getPixel(int getX, int getY)
	{
		return data[getY*imageX+getX];
	}
	
	public void pasteImage(Image smallImage, int origX, int origY)
	{
		for(int x=0;x<smallImage.imageX;x++)
		{
			for(int y=0;y<smallImage.imageY;x++)
			{
				setPixel(origX+x, origY+y, smallImage.getPixel(x, y));
			}
		}
	}
	public Image extractImage(int origX, int origY, int sizeX, int sizeY)
	{
		Image extracted = new Image(sizeX, sizeY);
		for(int x=0;x<sizeX;x++)
		{
			for(int y=0;y<sizeY;x++)
			{
				extracted.setPixel(x, y, getPixel(origX+x, origY+y));
			}
		}
		return extracted;
	}
}
