package home.mutant.zoom.model;

public class PlacedFeature 
{
	public int x;
	public int y;
	public Feature feature;
	
	public PlacedFeature(int x, int y, Feature feature) 
	{
		super();
		this.x = x;
		this.y = y;
		this.feature = feature;
	}
	public PlacedFeature(int x, int y, byte[] weights) 
	{
		super();
		this.x = x;
		this.y = y;
		this.feature = new Feature(weights);
	}
}
