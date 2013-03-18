package home.mutant.deep.model;

public class TwoSparseConnectedLayers implements RecognizerGenerator 
{
	private TwoFullConnectedLayers[][] columns;
	private int topSizePerDimension;
	private int bottomSizePerDimension;
	
	public TwoSparseConnectedLayers(int noColumns, int topLayerNeuronPerColumn, int bottomLayerNeuronsPerColumn)
	{
		int columnPerDimension = (int)Math.sqrt(noColumns);
		this.topSizePerDimension = columnPerDimension * (int)Math.sqrt(topLayerNeuronPerColumn);
		this.bottomSizePerDimension = columnPerDimension * (int)Math.sqrt(bottomLayerNeuronsPerColumn);
		
		columns = new TwoFullConnectedLayers[columnPerDimension][columnPerDimension];
		for (int cx= 0;cx<columnPerDimension;cx++)
		{
			for (int cy= 0;cy<columnPerDimension;cy++)
			{
				columns[cx][cy] = new TwoFullConnectedLayers(topLayerNeuronPerColumn, bottomLayerNeuronsPerColumn);
			}
		}
	}
	
	public Image generateSample() 
	{
		Image sample = new Image(bottomSizePerDimension, bottomSizePerDimension);
		int columnsPerDimension = columns.length;
		int neuronsColumnPerDimension = bottomSizePerDimension/columnsPerDimension;
		for (int cx= 0;cx<columnsPerDimension;cx++)
		{
			for (int cy= 0;cy<columnsPerDimension;cy++)
			{
				Image columnImage = columns[cx][cy].generateSample();
				sample.pasteImage(columnImage, cx*neuronsColumnPerDimension, cy*neuronsColumnPerDimension);
			}
		}
		return sample;
	}
	public Image generateSampleFromInput(Image input) 
	{
		Image sample = new Image(bottomSizePerDimension, bottomSizePerDimension);
		int columnsPerDimension = columns.length;
		int bottomNeuronsColumnPerDimension = bottomSizePerDimension/columnsPerDimension;
		int topNeuronsColumnPerDimension = topSizePerDimension/columnsPerDimension;
		
		for (int cx= 0;cx<columnsPerDimension;cx++)
		{
			for (int cy= 0;cy<columnsPerDimension;cy++)
			{
				Image columnImage = columns[cx][cy].generateSampleFromInput(input.extractImage(cx*topNeuronsColumnPerDimension, cy*topNeuronsColumnPerDimension, topNeuronsColumnPerDimension, topNeuronsColumnPerDimension));
				sample.pasteImage(columnImage, cx*bottomNeuronsColumnPerDimension, cy*bottomNeuronsColumnPerDimension);
			}
		}
		return sample;
	}

	public Image forwardStep(Image bs) 
	{
		int columnsPerDimension = columns.length;
		int bottomNeuronsColumnPerDimension = bottomSizePerDimension/columnsPerDimension;
		int topNeuronsColumnPerDimension = topSizePerDimension/columnsPerDimension;
		
		Image result = new Image (topSizePerDimension,topSizePerDimension);
		for (int cx= 0;cx<columns.length;cx++)
		{
			for (int cy= 0;cy<columns.length;cy++)
			{
				Image columnForward = columns[cx][cy].forwardStep(bs.extractImage(cx*topNeuronsColumnPerDimension, cy*topNeuronsColumnPerDimension, topNeuronsColumnPerDimension, topNeuronsColumnPerDimension));
			}
		}
		return result;
	}
}
