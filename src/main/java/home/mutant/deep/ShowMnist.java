package home.mutant.deep;


import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.ImageUtils;

import java.util.List;

public class ShowMnist
{
	public static void main(String[] args)
	{
		List<byte[][]> images = ImageUtils.readMnist("/mnist/train-images.idx3-ubyte");
		
		ResultFrame frame = new ResultFrame(1200, 600);
		frame.showMnist2(ImageUtils.convertToBW(images), 0);
	}
}
