package home.mutant.deep.mains.view;


import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.ImageUtils;
import home.mutant.deep.utils.MnistDatabase;

public class ShowMnist
{
	public static void main(String[] args) throws Exception
	{
		//List<Integer> labels = ImageUtils.readMinstLabels("/mnist/train-labels.idx1-ubyte");
		
		ResultFrame frame = new ResultFrame(1200, 600);
		//frame.showMnist2(ImageUtils.convertToBW(images), 0);
		//frame.showImages(ImageUtils.readMnistAsBWImage("/mnist/train-images.idx3-ubyte"), 0, 20);
		MnistDatabase.loadImagesCrop(20);
		frame.showImages(MnistDatabase.trainImages, 0, 40);
		//frame.showImage(ImageUtils.scaleImage(ImageUtils.gradientImage(MnistDatabase.trainImages.get(0)), 10));
	}
}
