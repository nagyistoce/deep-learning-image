package home.mutant.deep.model;

import java.util.List;

public class TwoConvolutedLayersBinary 
{
	public TwoFullConnectedLayersBinary column; 
	int columnXYDimensionBottom;
	int columnXYDimensionTop;
	public TwoConvolutedLayersBinary(int columnXYDimensionBottom, int columnXYDimensionTop)
	{
		column = new  TwoFullConnectedLayersBinary(columnXYDimensionTop*columnXYDimensionTop, columnXYDimensionBottom*columnXYDimensionBottom);
		this.columnXYDimensionBottom = columnXYDimensionBottom;
		this.columnXYDimensionTop = columnXYDimensionTop;
	}
	
	public void initWeightsFromImages(List<Image> images)
	{
		for (Image image : images) 
		{
			for (int xIndex=0;xIndex<image.imageX;xIndex+=columnXYDimensionBottom)
			{
				for (int yIndex=0;yIndex<image.imageY;yIndex+=columnXYDimensionBottom)
				{
					Image subImage = image.extractImage(xIndex, yIndex, columnXYDimensionBottom, columnXYDimensionBottom);
					long max = column.forwardStepMax(subImage);
					
					if (max<columnXYDimensionBottom*columnXYDimensionBottom-1)
					{
						System.out.println(max);
						column.initWeightsFromOneImage(subImage);
					}
				}
			}
		}
	}
	public Image forwardStep(Image image)
	{
		int ratio = image.imageX/columnXYDimensionBottom;
		Image ret = new Image(ratio*columnXYDimensionTop, ratio*columnXYDimensionTop);
		for (int xIndex=0;xIndex<ratio;xIndex+=1)
		{
			for (int yIndex=0;yIndex<ratio;yIndex+=1)
			{
				Image subImage = image.extractImage(xIndex*columnXYDimensionBottom, yIndex*columnXYDimensionBottom, columnXYDimensionBottom, columnXYDimensionBottom);
				Image subImageForwarded = column.forwardStepImageMax(subImage);
				ret.pasteImage(subImageForwarded, xIndex*columnXYDimensionTop, yIndex*columnXYDimensionTop);
			}
		}
		return ret;
	}
	
	public Image reconstruct(Image image)
	{
		int ratio = image.imageX/columnXYDimensionBottom;
		Image ret = new Image(image.imageX,image.imageY);
		for (int xIndex=0;xIndex<ratio;xIndex+=1)
		{
			for (int yIndex=0;yIndex<ratio;yIndex+=1)
			{
				Image subImage = image.extractImage(xIndex*columnXYDimensionBottom, yIndex*columnXYDimensionBottom, columnXYDimensionBottom, columnXYDimensionBottom);
				int max = column.forwardStepIndex(subImage);
				ret.pasteImage(column.neurons[max].generateSample(), xIndex*columnXYDimensionBottom, yIndex*columnXYDimensionBottom);
			}
		}
		return ret;
	}
}
