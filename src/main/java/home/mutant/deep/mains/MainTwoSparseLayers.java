package home.mutant.deep.mains;

import home.mutant.deep.mains.MainTwoLayers.Style;
import home.mutant.deep.networks.TwoSparseConnectedLayers;
import home.mutant.deep.neurons.FloatNeuron;
import home.mutant.deep.ui.Image;
import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.ImageUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainTwoSparseLayers 
{
	List<Image> trainImages = new ArrayList<Image>();
	List<Integer> trainLabels  = new ArrayList<Integer>();

	List<Image> testImages = new ArrayList<Image>();
	List<Integer> testLabels  = new ArrayList<Integer>();
	public static float max_weights=0;
	public static int NO_SAMPLES = 20; 
	public static void main(String[] args) throws IOException
	{
		new MainTwoSparseLayers().run();
	}

	private void run() throws IOException 
	{
		TwoSparseConnectedLayers layer = new TwoSparseConnectedLayers(300, 28, FloatNeuron.class);
		//layer.randomize();
		ImageUtils.loadImages(trainImages, testImages, trainLabels, testLabels, Style.BW);
		
//		for (int i=0;i<NO_SAMPLES;i++)
//		{
//			ResultFrame frame1 = new ResultFrame(330, 300,(i*350)%1900,((i*350)/1900)*200, "Frame image "+index(i));
//			layer.column.windowStart = i*layer.column.windowSize;
//			frame1.showImage(layer.forwardStep(trainImages.get(index(i)), index(i)));
//			frame1.showImage(trainImages.get(index(i)),300,0);
//		}
		for (int i=0; i<10000;i++)
		{
			int rnd = (int) (Math.random()*NO_SAMPLES);
			
			layer.column.windowStart = index(rnd)*layer.column.windowSize;
			Image res = layer.forwardStep(trainImages.get(index(rnd)), index(rnd));
			if (i%50==0)
			{
				ResultFrame frame1 = new ResultFrame(330, 300,(rnd*350)%1900,((rnd*350)/1900)*200, "Frame image "+index(rnd));
				frame1.showImage(res);
				frame1.showImage(trainImages.get(index(rnd)),300,0);
			}
		}
//		for (int i=0;i<NO_SAMPLES;i++)
//		{
//			ResultFrame frame1 = new ResultFrame(330, 300,(i*350)%1900,((i*350)/1900)*200, "Frame image "+index(i));
//			layer.column.windowStart = i*layer.column.windowSize;
//			frame1.showImage(layer.forwardStep(trainImages.get(index(i)), index(i)));
//			frame1.showImage(trainImages.get(index(i)),300,0);
//		}
		
		ResultFrame frame2 = new ResultFrame(330, 300,0,350, "Frame image 108");
		frame2.showImage(layer.forwardStep(trainImages.get(108),108));
		frame2.showImage(trainImages.get(108),300,0);

		ImageUtils.checkCollisions(layer.forwardStep(trainImages.get(index(0)),index(0)), layer.forwardStep(trainImages.get(index(1)),index(1)));
		ImageUtils.checkCollisions(layer.forwardStep(trainImages.get(index(0)),index(0)), layer.forwardStep(trainImages.get(index(2)),index(2)));
		ImageUtils.checkCollisions(layer.forwardStep(trainImages.get(index(0)),index(0)), layer.forwardStep(trainImages.get(index(3)),index(3)));
		ImageUtils.checkCollisions(layer.forwardStep(trainImages.get(index(0)),index(0)), layer.forwardStep(trainImages.get(index(4)),index(4)));

		ImageUtils.checkCollisions(layer.forwardStep(trainImages.get(105),105), layer.forwardStep(trainImages.get(index(0)),index(0)));
		ImageUtils.checkCollisions(layer.forwardStep(trainImages.get(105),105), layer.forwardStep(trainImages.get(index(1)),index(1)));
		ImageUtils.checkCollisions(layer.forwardStep(trainImages.get(105),105), layer.forwardStep(trainImages.get(index(2)),index(2)));
		ImageUtils.checkCollisions(layer.forwardStep(trainImages.get(105),105), layer.forwardStep(trainImages.get(index(3)),index(3)));
		ImageUtils.checkCollisions(layer.forwardStep(trainImages.get(105),105), layer.forwardStep(trainImages.get(index(4)),index(4)));
		
		ResultFrame frame3 = new ResultFrame(330, 300,350,350, "Frame image 105");
		frame3.showImage(layer.forwardStep(trainImages.get(105),105));
		frame3.showImage(trainImages.get(105),300,0);		
//		ResultFrame frame3 = new ResultFrame(400, 400, "Frame image 3");
//		frame3.showImage(layer.forwardStep(trainImages.get(2)));
//		ResultFrame frame4 = new ResultFrame(400, 400, "Frame image 4");
//		frame4.showImage(layer.forwardStep(trainImages.get(4)));
//		ResultFrame frame5 = new ResultFrame(400, 400, "Frame image 4");
//		frame5.showImage(layer.forwardStep(trainImages.get(1)));
		int d=0;
		for (int i = 0; i < layer.column.neurons.length; i++)
		{
			if (layer.column.neurons[i].getOutputIndex()==-1)
			{
				d++;
			}
		}
		System.out.println("Percent non taken " + ((double)d*100)/layer.column.neurons.length +"%");
	}
	
	private int index(int i)
	{
		return i;//(i%NO_SAMPLES)*2+12;
	}
}
