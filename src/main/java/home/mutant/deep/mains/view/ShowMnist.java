package home.mutant.deep.mains.view;


import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.ImageUtils;

import java.util.List;

public class ShowMnist
{
	public static void main(String[] args)
	{
		List<Integer> labels = ImageUtils.readMinstLabels("/mnist/train-labels.idx1-ubyte");
		
		ResultFrame frame = new ResultFrame(1200, 600);
		//frame.showMnist2(ImageUtils.convertToBW(images), 0);
		frame.showImages(ImageUtils.readMnistAsBWImage("/mnist/train-images.idx3-ubyte"), 0);
	}
}
