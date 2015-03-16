package home.mutant.weka;

import java.io.File;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import home.mutant.deep.utils.MnistDatabase;

public class ConvertMnistToArff {

	private static final int NO_IMAGES = 6000;

	public static void main(String[] args) throws Exception 
	{
		MnistDatabase.loadImagesCrop(20);
		FastVector atts = new FastVector();
		for (int i=0;i<20*20;i++)
		{
			atts.addElement(new Attribute("pixel"+i));
		}
		
		FastVector attVals = new FastVector();
	    for (int i = 0; i < 10; i++)
	      attVals.addElement(""+i);
	    atts.addElement(new Attribute("class", attVals));
	    
		Instances data = new Instances("MnistTrain", atts, NO_IMAGES);
		
		for (int indexImage=0;indexImage<NO_IMAGES;indexImage++)
		{
			double[] vals = new double[data.numAttributes()];
			byte[] pixels = MnistDatabase.trainImages.get(indexImage).getDataOneDimensional();
			for (int i = 0; i < pixels.length; i++) 
			{
				vals[i] = pixels[i];
				if (vals[i]<0)vals[i]+=255;
			}
			vals[data.numAttributes()-1] = MnistDatabase.trainLabels.get(indexImage);
			data.add(new Instance(1.0, vals));
		}
		
	    ArffSaver saver = new ArffSaver();
	    saver.setInstances(data);
	    saver.setFile(new File("mnisTrainCrop"+NO_IMAGES+".arff"));
	    saver.writeBatch();
	}

}
