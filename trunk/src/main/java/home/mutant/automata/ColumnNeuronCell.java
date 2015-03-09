package home.mutant.automata;

import home.mutant.deep.ui.Image;

import java.util.ArrayList;
import java.util.List;

public class ColumnNeuronCell 
{
	public NeuronCell[][] cells;
	public int x;
	public int y;
	public ColumnNeuronCell(int x, int y)
	{
		this.x=x;
		this.y=y;
		int neighboursRadius=1;
		cells = new NeuronCell[x][y];
		for (int i=0;i<x;i++)
		{
			for (int j=0;j<y;j++)
			{
				cells[i][j]=new NeuronCell();
			}
		}
		
		for (int i=0;i<x;i++)
		{
			for (int j=0;j<y;j++)
			{
				List<NeuronCell> neighbours = new ArrayList<NeuronCell>();
				for (int ni=i-neighboursRadius;ni<i+neighboursRadius+1;ni++)
				{
					if (ni<0 || ni>=x) continue;
					for (int nj=j-neighboursRadius;nj<j+neighboursRadius+1;nj++)
					{
						if (nj<0 || nj>=y || (nj==j && ni==i)) continue;
						neighbours.add(cells[ni][nj]);
					}
				}
				cells[i][j].addNeighbours(neighbours);
			}
		}
	}
	
	public void init(Image image)
	{
		for (int i=0;i<x;i++)
		{
			for (int j=0;j<y;j++)
			{
				if (image.getPixelInt(i, j)>Math.random()*255*3)
				{
					cells[i][j].output = 1;
				}
				else
				{
					cells[i][j].output = 0;
				}
			}
		}
	}
	public void step()
	{
		for (int i=0;i<x;i++)
		{
			for (int j=0;j<y;j++)
			{
				cells[i][j].step();
			}
		}
		for (int i=0;i<x;i++)
		{
			for (int j=0;j<y;j++)
			{
				cells[i][j].sync();
			}
		}
	}
}
