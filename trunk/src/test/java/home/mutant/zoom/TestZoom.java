package home.mutant.zoom;

import home.mutant.deep.utils.ImageUtils;
import home.mutant.deep.utils.MnistDatabase;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.junit.Test;

public class TestZoom 
{
	@Test
	public void testprintImage() throws Exception
	{
		MnistDatabase.loadGradientImages();
		
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		BufferedImage createBufferedImage = ImageUtils.createBufferedImage(MnistDatabase.trainImages.get(0));
		frame.getContentPane().add(new JLabel(new ImageIcon(ImageUtils.scaleImage(createBufferedImage,0.44))));
		frame.pack();
		frame.setVisible(true);
		System.out.println();
	}
}
