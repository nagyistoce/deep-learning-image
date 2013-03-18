package home.mutant.deep.model;


public class ThreeSparseConnectedLayers implements RecognizerGenerator
{
	private TwoFullConnectedLayers topLayers;
	private TwoSparseConnectedLayers bottomLayers;
	
	public ThreeSparseConnectedLayers(int topNeuronsPerColumn, int middleNeuronsPerColumn, int noColumns, int bottomNeuronsPerColumn)
	{
		topLayers = new TwoFullConnectedLayers(topNeuronsPerColumn*noColumns, middleNeuronsPerColumn*noColumns);
		bottomLayers = new TwoSparseConnectedLayers(noColumns, middleNeuronsPerColumn, bottomNeuronsPerColumn);
	}

	public Image generateSample() 
	{
		Image top = topLayers.generateSample();
		return bottomLayers.generateSampleFromInput(top);
	}

	public int test(Image data) 
	{
		return 0;
	}

	@Override
	public Image forwardStep(Image bs) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
