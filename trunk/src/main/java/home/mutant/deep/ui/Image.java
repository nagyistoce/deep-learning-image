package home.mutant.deep.ui;

import home.mutant.deep.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Image 
{
	public int imageX,imageY;
	byte[] data = null ;
	long[] binaryData = null;
	
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
	
	public Image(int size)
	{
		this((int)Math.sqrt(size),(int)Math.sqrt(size));
	}
	
	public Image(BufferedImage bufferedImage)
	{
		this(bufferedImage.getWidth(),bufferedImage.getHeight());
		int offset=0;
		for (int y = 0; y < bufferedImage.getHeight(); y++) 
		{
			for (int x = 0; x < bufferedImage.getWidth(); x++) 
			{
                int c = bufferedImage.getRGB(x,y);
                int  color = (c & 0x00ffffff);
                
                data[offset]=0;
                if (color>0)
                {
                	data[offset]=(byte)255;
                }
                offset++;
			}
		}

	}
	
	public Image(long[] dataBinary, int x, int y)
	{
		this.imageX=x;
		this.imageY=y;
		this.data = new byte[x*y];
		for(int index=0;index<x*y;index++)
		{
			int indexLong = index/64;
			int indexBit = index%64;
			long mask = ((long)1)<<indexBit;
			if ((mask&dataBinary[indexLong]) != 0)
			{
				data[index]=1;
			}
		}
		binaryData =new long[dataBinary.length];
		System.arraycopy(dataBinary, 0, binaryData, 0, dataBinary.length);
	}
	public Image(double[] data) 
	{
		this(data, (int)Math.sqrt(data.length), (int)Math.sqrt(data.length));
	}
	public Image(byte[][] bs) 
	{
		this.imageX = bs.length;
		this.imageY = bs[0].length;
		this.data = new byte[imageX*imageY];
		int offset=0;
		for (int x=0;x<imageX;x++) 
		{
			for (int y=0;y<imageY;y++)
			{
				data[offset++] = bs[x][y];
			}
		}
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
	
	public List<byte[]> divideImage(int newX, int newY, int stepX, int stepY)
	{
		return ImageUtils.divideImage(data, newX, newY, imageX, imageY, stepX, stepY);
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
			for(int y=0;y<smallImage.imageY;y++)
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
			for(int y=0;y<sizeY;y++)
			{
				extracted.setPixel(x, y, getPixel(origX+x, origY+y));
			}
		}
		return extracted;
	}
	public long[] getDataBinary() 
	{
		if (binaryData!=null)
		{
			return binaryData;
		}

		int length = imageX*imageY;
		if (length%64!=0)
		{
			length = length/64 +1;
		}
		else
		{
			length = length/64;
		}
		binaryData = new long[length];
		long indexBit = 0;
		int indexLong = 0;
		for (int i = 0; i < data.length; i++) 
		{
			long mask = ((long)1) << indexBit;
			if (data[i]==0)
			{
				binaryData[indexLong] &= ~mask;
			}
			else
			{
				binaryData[indexLong] |= mask;
			}
			indexBit++;
			if (indexBit==64)
			{
				indexBit=0;
				indexLong++;
			}
		}
		return binaryData;
	}
}
