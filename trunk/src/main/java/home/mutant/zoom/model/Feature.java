package home.mutant.zoom.model;

import home.mutant.deep.utils.MathUtils;

public class Feature 
{
	public int x;
	public int y;
	public float[] weights;
	public double distance;
	public Feature(int x, int y) 
	{
		super();
		this.x = x;
		this.y = y;
		this.weights = new float[x*y];
	}
	public Feature(byte[] pixels) 
	{
		this((int)Math.sqrt(pixels.length), (int)Math.sqrt(pixels.length));
		for (int i = 0; i < pixels.length; i++) 
		{
			int pixel = pixels[i];
			if (pixel<0)pixel+=255;
			weights[i] = pixel;
		}
	}
	public boolean match(byte[] pixels)
	{
		distance = MathUtils.gaussian(output(pixels), 250);
		//System.out.println(distance);
		return distance>0.5;
	}
	public double outputGaussian(byte[] pixels)
	{
		double distance = MathUtils.gaussian(output(pixels), 450);
		//System.out.println(distance);
		return distance;
	}
	public double output(byte[] pixels) 
	{
		double sum=0;
		for (int i = 0; i < pixels.length; i++) 
		{
			int pixel = pixels[i];
			if (pixel<0)pixel+=255;
			double weight = weights[i]-pixel;
			sum+=weight*weight;//Math.abs(weight);
		}
		double distance = Math.sqrt(sum);
		return distance;
	}
}
