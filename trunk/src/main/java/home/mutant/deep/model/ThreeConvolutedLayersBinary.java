package home.mutant.deep.model;

import java.util.List;

public class ThreeConvolutedLayersBinary 
{
	public TwoConvolutedLayersBinary bottom;
	public TwoFullConnectedLayersBinary top;
	
	public ThreeConvolutedLayersBinary(int bottomXYDimension, int columnXYDimensionBottom, int columnXYDimensionTop, int topNeurons)
	{
		bottom = new TwoConvolutedLayersBinary(columnXYDimensionBottom, columnXYDimensionTop);
		int ratio = bottomXYDimension/columnXYDimensionBottom;
		top = new TwoFullConnectedLayersBinary(topNeurons, columnXYDimensionTop*columnXYDimensionTop*ratio*ratio);
	}
	public void initWeightsFromImages(List<Image> images)
	{
		bottom.initWeightsFromImages(images);
		int indexNeuron=0;
		for (Image image : images) 
		{
			Image bottomGenerated = bottom.forwardStep(image);
			top.initWeightsFromOneImage(bottomGenerated, indexNeuron++);
		}
	}
	public long forwardStepMax(Image image)
	{
		Image bottomGenerated = bottom.forwardStep(image);
		return top.forwardStepMax(bottomGenerated);
	}
	public Image forwardStep(Image image)
	{
		Image bottomGenerated = bottom.forwardStep(image);
		return top.forwardStepImageMax(bottomGenerated);
	}
	
	public int forwardStepIndex(Image image) 
	{
		Image bottomGenerated = bottom.forwardStep(image);
		return top.forwardStepIndex(bottomGenerated);
	}
}
