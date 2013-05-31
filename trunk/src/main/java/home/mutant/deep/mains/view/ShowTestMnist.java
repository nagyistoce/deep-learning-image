package home.mutant.deep.mains.view;


import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.ImageUtils;

import java.util.List;

public class ShowTestMnist
{
	public static void main(String[] args)
	{
		List<byte[][]> images = ImageUtils.readMnist("/mnist/t10k-images.idx3-ubyte");
		List<Integer> labels = ImageUtils.readMinstLabels("/mnist/t10k-labels.idx1-ubyte");
		
		ResultFrame frame = new ResultFrame(1200, 600);
		frame.showMnist2(ImageUtils.convertToBW(images), 0);
	}
}
