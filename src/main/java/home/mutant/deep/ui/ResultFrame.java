package home.mutant.deep.ui;

import home.mutant.deep.model.Image;
import home.mutant.deep.model.ModelTestResult;
import home.mutant.deep.model.TwoFullConnectedLayers;
import home.mutant.deep.model.TwoFullConnectedLayersBinary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public void showModel2by2(TwoFullConnectedLayers model, int width)
	{
		drawingPanel.empty();
		for (int n1=0;n1<20;n1++)
		{
			for (int n=0;n<50;n++)
			{
				Image sample = model.generateSample();
				int indexGene=0;
				for (int y = n1*(width+1);y<n1*(width+1)+width;y++)
				{
					for (int x = n*(width+1) ;x<n*(width+1)+width;x++)
					{
						if (sample.getDataOneDimensional()[indexGene++]>0)
						{
							drawingPanel.set4Pixels(2*x, 2*y);
						}
					}
				}
			}
		}
		repaint();	
	}
	
	public void showModel(TwoFullConnectedLayers model, int width)
	{
		drawingPanel.empty();
		int index=0;
		for (int n1=0;n1<20;n1++)
		{
			for (int n=0;n<50;n++)
			{
				Image sample = model.generateSample(index++);
				int indexGene=0;
				for (int y = n1*(width+1);y<n1*(width+1)+width;y++)
				{
					for (int x = n*(width+1) ;x<n*(width+1)+width;x++)
					{
						if (sample.getDataOneDimensional()[indexGene++]>0)
						{
							drawingPanel.setPixel(x, y);
						}
					}
				}
			}
		}
		repaint();	
	}
	
	public ModelTestResult showOutput(TwoFullConnectedLayersBinary model, Image image, List<Integer> labels)
	{
		return showImage(new Image(model.forwardStep(image,true), 300, 300), labels);
	}
	
	public ModelTestResult showImage(Image image, List<Integer> labels)
	{
		Map<Integer,Integer> maps = new HashMap<Integer, Integer>();
		//drawingPanel.empty();
		int indexLabel=0;
		for (int y=0;y<image.imageY;y++)
		{
			for (int x=0;x<image.imageX;x++)
			{
				if (image.getPixel(x, y)!=0)
				{
					//drawingPanel.setPixel(x, y);
					if(indexLabel<labels.size())
					{
						Integer count = maps.get(labels.get(indexLabel));
						if (count==null)
						{
							count=0;
						}
						count++;
						maps.put(labels.get(indexLabel),count);
					}
				}
				indexLabel++;
			}
		}
		//repaint();
		int max = 0;
		int maxKey=-1;
		int total=0;
		for (Integer key : maps.keySet()) 
		{
			Integer value = maps.get(key);
			if (value>max)
			{
				max = value;
				maxKey = key;
			}
			total+=value;
			//System.out.println("     " +key+ " :"+maps.get(key) );
		}
		return new ModelTestResult(maxKey, max, max*100./total);
	}
	
	public void showImage(Image image)
	{
		drawingPanel.empty();
		putImage(image, 0, 0);
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
	
	public void showBinaryColumn(TwoFullConnectedLayersBinary column)
	{
		drawingPanel.empty();
		for (int index=0;index<300;index++)
		{
			putImage(column.neurons[index].generateSample(), index*5, 3);
		}
		repaint();
	}
	
	public void putImage(Image image, int xOffset, int yOffset)
	{
		for (int y=0;y<image.imageY;y++)
		{
			for (int x=0;x<image.imageX;x++)
			{
				if (image.getPixel(x, y)!=0)
				{
					drawingPanel.setPixel(xOffset+x,yOffset+y);
				}
			}
		}
	}
}
