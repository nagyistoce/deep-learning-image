package home.mutant.automata;

import home.mutant.deep.ui.ResultFrame;
import home.mutant.deep.utils.ImageUtils;
import home.mutant.deep.utils.MnistDatabase;

public class RunColumnCells {

	public static void main(String[] args) throws Exception 
	{
		ColumnNeuronCell net = new ColumnNeuronCell(28, 28);
		MnistDatabase.loadGradientImages();
		net.init(MnistDatabase.trainImages.get(0));
		for (int step=0;step<10;step++)
			net.step();
		net.step();
		ResultFrame frame = new ResultFrame(400, 600);
		frame.showColumnNeuronCells(net, 0, 0);
	}

}
