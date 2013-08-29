package home.mutant.deep.mains.view;


import java.util.List;

import home.mutant.deep.ui.Image;
import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.ImageUtils;

public class TestTransform
{
	public static void main(String[] args)
	{
		List<Image> images = ImageUtils.readMnistAsImage("/mnist/t10k-images.idx3-ubyte");
		ResultFrame frame = new ResultFrame(1200, 600);
		frame.showImage(ImageUtils.affineTransform(images.get(0), 0, 0, 0.5));
		//frame.showImage(images.get(0));
	}
}
