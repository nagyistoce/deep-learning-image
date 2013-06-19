package home.mutant.deep.networks;

import home.mutant.deep.abstracts.Neuron;
import home.mutant.deep.ui.Image;

import java.util.List;

public class TwoConvolutedLayersBinary 
{
	public TwoFullConnectedLayers column; 
	int columnXYDimensionBottom;
	int columnXYDimensionTop;
	public TwoConvolutedLayersBinary(int columnXYDimensionBottom, int columnXYDimensionTop, Class<? extends Neuron> clazz)
	{
		column = new  TwoFullConnectedLayers(columnXYDimensionTop*columnXYDimensionTop, columnXYDimensionBottom*columnXYDimensionBottom,clazz);
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
					if (max<columnXYDimensionBottom*columnXYDimensionBottom)
					{
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
				Image subImageForwarded = column.forwardStepImageThreshold(subImage,columnXYDimensionBottom*columnXYDimensionBottom-10);
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
