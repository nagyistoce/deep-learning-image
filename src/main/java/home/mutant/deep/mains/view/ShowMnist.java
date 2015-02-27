package home.mutant.deep.mains.view;


import java.io.IOException;

import home.mutant.deep.ui.Image;
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
		MnistDatabase.loadGradientImages();
		Image image = MnistDatabase.trainImages.get(12);
		frame.showImage(image, 30, 0);
	}
}
