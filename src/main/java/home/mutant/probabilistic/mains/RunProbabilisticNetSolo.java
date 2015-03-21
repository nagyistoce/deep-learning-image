package home.mutant.probabilistic.mains;


import home.mutant.deep.ui.Image;
import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.ImageUtils;
import home.mutant.deep.utils.MnistDatabase;
import home.mutant.probabilistic.cells.ProbabilisticNeuron;
import home.mutant.probabilistic.mains.runnables.ProbabilisticNetRunnable;
import home.mutant.probabilistic.nets.ProbabilisticNet;

public class RunProbabilisticNetSolo 
{
	public static final int IMAGE_SIZE = 56;
	public static void main(String[] args) throws Exception
	{
		ResultFrame frame = new ResultFrame(200, 700);
		MnistDatabase.loadGradientImages();
		
		ProbabilisticNet net = new ProbabilisticNet(IMAGE_SIZE, IMAGE_SIZE);


		for (int s=0;s<10;s++)
			supervisedNonProbabilistic(net, s);
//			showSupervised(frame, net, s);
		//Thread.sleep(2000);
		byte[] pixels = ImageUtils.scaleImage(MnistDatabase.trainImages.get(1), IMAGE_SIZE/28.).getDataOneDimensional();
		for (int i = 0; i < pixels.length; i++)
		{
			int pixel = pixels[i];
			if (pixel<0)pixel+=255;
			if (pixel<150) continue;
			ProbabilisticNeuron neuron = net.neurons.get(i);
			if (neuron == null)
			{
				neuron = new ProbabilisticNeuron(i%RunProbabilisticNetSolo.IMAGE_SIZE, i/RunProbabilisticNetSolo.IMAGE_SIZE);
				net.neurons.put(i, neuron);
			}
			neuron.output = pixel;
		}
		
		Thread netThread = new Thread(new ProbabilisticNetRunnable(net));
		netThread.start();
		while(true)
		{
			frame.showImage(net.generateImage());
			//System.in.read();
		}		
	}

	private static void supervisedNonProbabilistic(ProbabilisticNet net, int indexImage) throws Exception
	{
		byte[]  pixels = new byte[IMAGE_SIZE*IMAGE_SIZE];
		Image image = ImageUtils.scaleImage(MnistDatabase.trainImages.get(indexImage), IMAGE_SIZE/28.);
		System.arraycopy(image.getDataOneDimensional(), 0, pixels, 0, IMAGE_SIZE*IMAGE_SIZE);
		for (int noPixel=0;noPixel<pixels.length;noPixel++)
		{
			int pixel = pixels[noPixel];
			if (pixel<0)pixel+=255;
			if (pixel<150) continue;
			if (Math.random()>0.4)continue;
			ProbabilisticNeuron neuron = net.neurons.get(noPixel);
			if (neuron == null)
			{
				neuron = new ProbabilisticNeuron(noPixel%RunProbabilisticNetSolo.IMAGE_SIZE, noPixel/RunProbabilisticNetSolo.IMAGE_SIZE);
				net.neurons.put(noPixel, neuron);
			}
			for (int noPixel2=0;noPixel2<pixels.length;noPixel2++)
			{
				int pixel2 = pixels[noPixel2];
				if (pixel2<0)pixel2+=255;
				if (pixel2<150)
				{
					//if (Math.random()>0.5)continue;
					ProbabilisticNeuron neuron2 = net.neurons.get(noPixel2);
					if (neuron2 == null)
					{
						neuron2 = new ProbabilisticNeuron(noPixel2%RunProbabilisticNetSolo.IMAGE_SIZE, noPixel2/RunProbabilisticNetSolo.IMAGE_SIZE);
						net.neurons.put(noPixel2, neuron2);
					}
					neuron.nonLinks.add(neuron2);
				}
				else
				{
					ProbabilisticNeuron neuron2 = net.neurons.get(noPixel2);
					if (neuron2 == null)
					{
						neuron2 = new ProbabilisticNeuron(noPixel2%RunProbabilisticNetSolo.IMAGE_SIZE, noPixel2/RunProbabilisticNetSolo.IMAGE_SIZE);
						net.neurons.put(noPixel2, neuron2);
					}
					neuron.links.add(neuron2);
				}
				
			}
		}
	}
}
