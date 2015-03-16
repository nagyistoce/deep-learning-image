package home.mutant.deep.ui;

import home.mutant.automata.ColumnNeuronCell;
import home.mutant.deep.model.ModelTestResult;
import home.mutant.deep.networks.TwoFullConnectedLayers;
import home.mutant.liquid.cells.NeuronCell;
import home.mutant.liquid.networks.FeedForward;
import home.mutant.liquid.networks.SimpleNet;
import home.mutant.logistic.LogisticNet;
import home.mutant.logistic.LogisticNeuron;

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
    
    public ResultFrame(int width, int height,int x, int y, String title)
    {
    	this.width = width;
    	this.height = height;
    	setTitle(title);
    	setSize(this.width+20, this.height+50);
    	setLocation(x, y);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	drawingPanel = new RasterPanel (this.width,this.height);
        add (drawingPanel);
        drawingPanel.init();
        setVisible(true);
    }
    public ResultFrame(int width, int height, String title)
    {
    	this(width, height,0,0, title);
    }
    public ResultFrame(int width, int height)
    {
    	this(width, height, "Title");
    }
    
	public void buildFrame()
	{
		repaint();
	}
	
	public ModelTestResult showOutput(TwoFullConnectedLayers model, Image image, List<Integer> labels)
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
		putImage(image, 0, 0);
		repaint();
	}
	public void showImage(Image image, int x, int y)
	{
		putImage(image, x, y);
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
	
	public void showImages(List<Image> images, int index)
	{
		showImages(images, index, 50);
	}

	public void showImages(List<Image> images, int index, int size)
	{
		drawingPanel.empty();
		if (index>size)
		{
			index = size;
		}
		if (images.size()<size)
		{
			size=images.size();
		}
		for (int n1=index;n1<size;n1++)
		{
			byte[][] image = images.get(n1).getDataTwoDimensional();
			int imageX = images.get(n1).imageX;
			int imageY = images.get(n1).imageY;
			for (int i =0;i<imageX;i++)
			{
				for (int j=0;j<imageY;j++)
				{
					drawingPanel.setPixel(j+(n1-index)*(imageX+1),i,image[j][i]);
				}
			}
		}
		repaint();
	}
	
	public void showImages(List<Image> images)
	{
		showImages(images, 0);
	}
	
	public void showBinaryColumn(TwoFullConnectedLayers column)
	{
		drawingPanel.empty();
		for (int index=0;index<256;index++)
		{
			putImage(column.neurons[index].generateSample(), index*5, 3);
		}
		repaint();
	}
	
	public void putImage(Image image, int xOffset, int yOffset)
	{
		for (int x=0;x<image.imageX;x++)
		{
			for (int y=0;y<image.imageY;y++)
			{
				drawingPanel.setPixel(xOffset+x,yOffset+y,image.getPixel(x, y));
			}
		}
	}
	
	public void showNetworkOutput(FeedForward net, int xOffset, int yOffset)
	{
		drawingPanel.empty();
		for (int x=0;x<net.layers.size();x++)
		{
			for (int y=0;y<net.layers.get(x).size();y++)
			{
				drawingPanel.setPixel(xOffset+x,yOffset+y,(byte)(net.layers.get(x).get(y).output*255));
			}
		}
		repaint();
	}
	
	public void showColumnNeuronCells(ColumnNeuronCell net, int xOffset, int yOffset)
	{
		drawingPanel.empty();
		for (int x=0;x<net.cells.length;x++)
		{
			for (int y=0;y<net.cells[0].length;y++)
			{
				drawingPanel.setPixel(xOffset+x,yOffset+y,(byte)(net.cells[x][y].output*255));
			}
		}
		repaint();
	}
	
	public void showLogisticNeuron(LogisticNeuron neuron, float scale)
	{
		showLogisticNeuron(neuron, 0,0, scale);
	}
	

	
	public void showNetworkWeightsBW(SimpleNet net, int noNeuronsPerLine)
	{
		drawingPanel.empty();
		int layer =0;
		int indexNeuronX=0;
		for (int indexNeuron=0;indexNeuron<net.neurons.size();indexNeuron++)
		{
			NeuronCell neuron = net.neurons.get(indexNeuron);
			int sizeX = (int) Math.sqrt(neuron.weights.length);
			int indexWeight = 0;
			if (indexNeuronX==noNeuronsPerLine)
			{
				indexNeuronX=0;
				layer++;
			}
			for (int y=layer*(sizeX+1);y<layer*(sizeX+1)+sizeX;y++)
			{
				for (int x=indexNeuronX*(sizeX+1);x<indexNeuronX*(sizeX+1)+sizeX;x++)
				{
					int weight = (int)(neuron.weights[indexWeight++]*127+128);
					if(weight<0) weight = 0;
					if(weight>255) weight = 255;
					drawingPanel.setPixel(x,y,(byte)(weight));
				}
			}
			indexNeuronX++;
		}
		repaint();
	}
	
	public void showNetworkWeights(SimpleNet net, int noNeuronsPerLine)
	{
		showNetworkWeights(net, noNeuronsPerLine, 0);
	}
	
	public void showLogisticNeuron(LogisticNeuron neuron, int xOffset, int yOffset, float scale)
	{
		drawingPanel.empty();
		putLogisticNeuron(neuron, xOffset, yOffset, scale);
		repaint();
	}
	
	public void putLogisticNeuron(LogisticNeuron neuron, int xOffset, int yOffset, float scale)
	{
		int length = (int) Math.sqrt(neuron.weights.length);
		int offset = 0;
		for (int x=0;x<length;x++)
		{
			for (int y=0;y<length;y++)
			{
				drawingPanel.setPixel(yOffset+y,xOffset+x,(byte)(neuron.weights[offset++]/scale+128));
			}
		}
	}
	
	public void showLogisticNet(LogisticNet net, int noNeuronsPerLine, float scale)
	{
		drawingPanel.empty();
		int layer =0;
		int indexNeuronX=0;
		for (int indexNeuron=0;indexNeuron<net.neurons.length;indexNeuron++)
		{
			LogisticNeuron neuron = net.neurons[indexNeuron];
			int sizeX = (int) Math.sqrt(neuron.weights.length);
			if (indexNeuronX==noNeuronsPerLine)
			{
				indexNeuronX=0;
				layer++;
			}
			putLogisticNeuron(neuron, indexNeuronX * sizeX, layer * sizeX, scale);
			indexNeuronX++;
		}
		repaint();
	}
	public void showNetworkWeights(SimpleNet net, int noNeuronsPerLine, int interImagesBorderSize)
	{
		drawingPanel.empty();
		int layer =0;
		int indexNeuronX=0;
		for (int indexNeuron=0;indexNeuron<net.neurons.size();indexNeuron++)
		{
			NeuronCell neuron = net.neurons.get(indexNeuron);
			int sizeX = (int) Math.sqrt(neuron.weights.length);
			int indexWeight = 0;
			if (indexNeuronX==noNeuronsPerLine)
			{
				indexNeuronX=0;
				layer++;
			}
			for (int y=layer*(sizeX+interImagesBorderSize);y<layer*(sizeX+interImagesBorderSize)+sizeX;y++)
			{
				for (int x=indexNeuronX*(sizeX+interImagesBorderSize);x<indexNeuronX*(sizeX+interImagesBorderSize)+sizeX;x++)
				{
					int weight = (int)(neuron.weights[indexWeight++]);
					//weight+=128;
					drawingPanel.setPixel(x,y,(byte)(weight));
				}
			}
			indexNeuronX++;
		}
		repaint();
	}
	
	public void addWeightsToFrame(NeuronCell neuron, int xOffset, int yOffset)
	{
		int xIncrease= 0;
		int yIncrease=0;
		
		if (neuron.indexNeuronMinDistance!=5000)
		{
			return;
		}
		if (neuron.indexNeuronMinDistance!=-1)
		{
			double radius = neuron.minDistance/2000;
			System.out.println(radius+"   "+neuron.indexNeuronMinDistance);
			double angle = Math.random()*2*Math.PI;
			xIncrease = (int)(radius*Math.cos(angle));
			yIncrease = (int)(radius*Math.sin(angle));
		}
		else
		{
			xIncrease = neuron.xShow;
			yIncrease = neuron.yShow;
		}
		
		int sizeX = (int) Math.sqrt(neuron.weights.length);
		int indexWeight=0;
		for (int x=0;x<sizeX;x++)
		{
			for (int y=0;y<sizeX;y++)
			{
				int weight = (int)(neuron.weights[indexWeight++]);
				drawingPanel.setPixel(xOffset+xIncrease+x,yOffset+yIncrease+y,(byte)(weight));
			}
		}
		if (neuron.indexNeuronMinDistance!=-1)
		{
			neuron.xShow = xOffset+xIncrease;
			neuron.yShow = yOffset+yIncrease;
		}
	}
}
