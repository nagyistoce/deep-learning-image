package home.mutant.deep.networks;

import home.mutant.deep.abstracts.Neuron;
import home.mutant.deep.ui.Image;


/**
 * We have for each subset of the input neurons we have several neurons fully connected to the same inputs
 * This will be called column.
 * Hopefully, for each input we should have different neurons that will respond to one or another input
 * @author clazar
 *
 */
public class TwoSparseConnectedLayers 
{
	public TwoFullConnectedLayers column; 
	int columnXYDimensionBottom;
	int columnXYDimensionTop;
	/**
	 * @param columnXYDimensionBottom  is the dimension for the square that are connected to the input
	 * @param columnXYDimensionTop is the dimension of the square for output neurons, so we will have 
	 * 					columnXYDimensionTop*columnXYDimensionTop output neurons for a column	
	 * @param clazz
	 */
	public TwoSparseConnectedLayers(int columnXYDimensionTop, int columnXYDimensionBottom, Class<? extends Neuron> clazz)
	{
		column = new  TwoFullConnectedLayers(columnXYDimensionTop*columnXYDimensionTop, columnXYDimensionBottom*columnXYDimensionBottom,clazz);
		this.columnXYDimensionBottom = columnXYDimensionBottom;
		this.columnXYDimensionTop = columnXYDimensionTop;
	}
	
	public void randomize()
	{
		for (int i = 0; i < column.neurons.length; i++) {
			column.neurons[i].randomize();
		}
	}
	/**
	 * When forwarding the image, there will be some changes in the synapses
	 * The learning algorithm will be embedded here:
	 * 		- present the same sub-image to the column, calculate the outputs for each, select with probability 
	 * 			which neurons will be activated and then increase a little the synapses for that input 
	 * 			(increase here will mean add for synapses that have "1" at input and subtract for synapses that have "0" )
	 * 		- hopefully after presenting many (sub)images, the output layer of the column will be organized in a way that 
	 * 			will show contrast (and learn) all the inputs presented
	 * @param image
	 * @return
	 */
	public Image forwardStep(Image image)
	{
		//Image subImage = image.extractImage(0, 0, columnXYDimensionBottom, columnXYDimensionBottom);
		return column.forwardStepImageLearning(image);
	}
}
