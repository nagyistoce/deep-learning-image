package home.mutant.deep.networks;

import home.mutant.deep.abstracts.Neuron;
import home.mutant.deep.model.IndexValue;
import home.mutant.deep.ui.Image;

import java.util.List;

public class ThreeConvolutedLayersBinary 
{
	public TwoConvolutedLayersBinary bottom;
	public TwoFullConnectedLayers top;
	
	public ThreeConvolutedLayersBinary(int bottomXYDimension, int columnXYDimensionBottom, int columnXYDimensionTop, int topNeurons, Class<? extends Neuron> clazz)
	{
		bottom = new TwoConvolutedLayersBinary(columnXYDimensionBottom, columnXYDimensionTop, clazz);
		int ratio = bottomXYDimension/columnXYDimensionBottom;
		top = new TwoFullConnectedLayers(topNeurons, columnXYDimensionTop*columnXYDimensionTop*ratio*ratio, clazz);
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
	
	public List<IndexValue> forwardStepMultipleIndexWithValues(Image image, int numberIndexes)
	{
		Image bottomGenerated = bottom.forwardStep(image);
		return top.forwardStepMultipleIndexWithValues(bottomGenerated, numberIndexes);
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
