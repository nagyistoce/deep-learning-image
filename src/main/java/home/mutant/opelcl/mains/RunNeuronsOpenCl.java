package home.mutant.opelcl.mains;

import home.mutant.deep.ui.Image;
import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.MnistDatabase;
import home.mutant.deep.utils.kmeans.Kmeans;
import home.mutant.liquid.cells.NeuronCell;
import home.mutant.liquid.cells.NeuronCellGreyDifference;
import home.mutant.liquid.networks.SimpleNet;
import home.mutant.opelcl.OpenClWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jocl.cl_mem;

public class RunNeuronsOpenCl 
{
	public static final int NO_NEURONS = 102400;
	public static final int NO_SYNAPSES = 49;
	public static final int NO_IMAGES = 36;
	public static final int NO_BATCH_IMAGES = 800;
    public static void main(String args[]) throws IOException
    {
    	float[] synapses = new float[NO_NEURONS*(NO_SYNAPSES+1)];
    	for (int i = 0; i < synapses.length; i++) 
    	{
			synapses[i] = (float) (Math.random()*256);
		}
    	for (int i = 49; i < synapses.length; i+=NO_SYNAPSES+1) 
    	{
			synapses[i] = 0;
		}
    	MnistDatabase.loadImages();
    	InputStream resourceAsStream = RunNeuronsOpenCl.class.getResourceAsStream("/opencl/NeuronKernel.cl");
		Scanner scanner  = new Scanner(resourceAsStream, "UTF-8");
		String text = scanner.useDelimiter("\\A").next();
		scanner.close();
		resourceAsStream.close();
    	OpenClWrapper wrapper = new OpenClWrapper(text, "outputNeuron");
    	
    	cl_mem synapsesMem = wrapper.addInputMemObject(synapses);
		cl_mem imageMem = wrapper.addInputMemObject(NO_SYNAPSES*NO_IMAGES*NO_BATCH_IMAGES);
        
		int subImageX=7;
		int subImageStep = 4;
		long t0=System.currentTimeMillis();

		for (int imageIndex=0;imageIndex<60000;imageIndex+=NO_BATCH_IMAGES)
		{
			List<byte[]> subImages = new ArrayList<byte[]>();
			for (int batch=0;batch<NO_BATCH_IMAGES;batch++)
			{
				subImages.addAll(MnistDatabase.trainImages.get(imageIndex+batch).divideImage(subImageX, subImageX, subImageStep, subImageStep));
			}
			float[] subImageFloats = OpenClWrapper.listBytesToFloats(subImages);
			//System.out.println("Image: "+java.util.Arrays.toString(subImageFloats));
			wrapper.copyDataHtoD(imageMem, subImageFloats);
	        wrapper.runKernel(NO_NEURONS, 64);
	        wrapper.finish();
	        //System.out.println("Result: "+java.util.Arrays.toString(outputs));
	        //System.out.println();
		}
		
		wrapper.copyDataDtoH(synapsesMem, synapses);
        System.out.println(System.currentTimeMillis()-t0);
        wrapper.release();
        SimpleNet net = new SimpleNet();
		for (int i=0;i<NO_NEURONS;i++)
		{
			NeuronCellGreyDifference neuron = new NeuronCellGreyDifference(subImageX*subImageX);
			net.neurons.add(neuron);
			for (int j=0;j<NO_SYNAPSES;j++)
			{
				neuron.weights[j] = synapses[i*50+j];
			}
		}
		ResultFrame frame = new ResultFrame(1200, 1000);
//		List<List<Integer>> clusters = Kmeans.run(net.neurons, 100);
//		List<NeuronCell> neurons = new ArrayList<NeuronCell>();
//		for (List<Integer> list : clusters)
//		{
//			for (int i =0;i<list.size() & i<150;i++)
//			{
//				neurons.add(net.neurons.get(list.get(i)));
//			}
//		}
//		net.neurons = neurons;
		frame.showNetworkWeights(net, 1200/(subImageX+1),1);
    }
}
