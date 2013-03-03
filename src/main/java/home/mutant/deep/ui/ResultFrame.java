package home.mutant.deep.ui;

import home.mutant.deep.model.FullIterconnectedWeightsModel;

import java.util.List;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ResultFrame extends JFrame
{
	private int width = 800;
    private int height = 600;
    
    RasterPanel drawingPanel;
    
    public ResultFrame(int width, int height)
    {
    	this.width = width;
    	this.height = height;
    	setSize(this.width+20, this.height+50);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	drawingPanel = new RasterPanel (this.width,this.height);
        add (drawingPanel);
        drawingPanel.init();
        setVisible(true);
    }

	public void buildFrame()
	{
		repaint();
	}

	public void showModel2by2(FullIterconnectedWeightsModel model, int width)
	{
		drawingPanel.empty();
		for (int n1=0;n1<20;n1++)
		{
			for (int n=0;n<50;n++)
			{
				double[] sample = model.generateSample();
				int indexGene=0;
				for (int y = n1*(width+1);y<n1*(width+1)+width;y++)
				{
					for (int x = n*(width+1) ;x<n*(width+1)+width;x++)
					{
						if (sample[indexGene++]>0)
						{
							drawingPanel.set4Pixels(2*x, 2*y);
						}
					}
				}
			}
		}
		repaint();	
	}
	
	public void showModel(FullIterconnectedWeightsModel model, int width)
	{
		drawingPanel.empty();
		for (int n1=0;n1<20;n1++)
		{
			for (int n=0;n<50;n++)
			{
				double[] sample = model.generateSample();
				int indexGene=0;
				for (int y = n1*(width+1);y<n1*(width+1)+width;y++)
				{
					for (int x = n*(width+1) ;x<n*(width+1)+width;x++)
					{
						if (sample[indexGene++]>0)
						{
							drawingPanel.setPixel(x, y);
						}
					}
				}
			}
		}
		repaint();	
	}
	
	public void showMnist(List<byte[][]> images, int index)
	{
		drawingPanel.empty();
		for (int n1=0;n1<20;n1++)
		{
			for (int i =0;i<28;i++)
			{
				for (int j=0;j<28;j++)
				{
					drawingPanel.setPixel(i+n1*28,j,images.get(index+n1)[i][j]);
				}
			}
		}
		repaint();
	}
	
	public void showMnist2(List<byte[]> images, int index)
	{
		drawingPanel.empty();
		for (int n1=0;n1<20;n1++)
		{
			int offset = 0;
			for (int i =0;i<28;i++)
			{
				for (int j=0;j<28;j++)
				{
					byte color = images.get(index+n1)[offset++];
					if (color ==1)
					{
						color = (byte)255;
					}
					drawingPanel.setPixel(j+n1*28,i,color);
				}
			}
		}
		repaint();
	}
}
