package home.mutant.weka;

import java.io.File;
import java.io.IOException;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class Arff 
{
	private String name;
	private int noInstances;
	private Instances data;

	public Arff(String name, int noAttrs, int noClasses, int noInstances)
	{
		this.name = name;
		this.noInstances = noInstances;
		FastVector atts = new FastVector();
		for (int i=0;i<noAttrs;i++)
		{
			atts.addElement(new Attribute("att"+i));
		}
		FastVector attVals = new FastVector();
	    for (int i = 0; i < noClasses; i++)
	      attVals.addElement(""+i);
	    atts.addElement(new Attribute("class", attVals));
	    
		data = new Instances(name, atts, noInstances);
	}
	
	public void addInstance(double[] values, int dataClass)
	{
		double[] vals = new double[data.numAttributes()];
		System.arraycopy(values, 0, vals, 0, values.length);
		vals[data.numAttributes()-1] = dataClass;
		data.add(new Instance(1.0, vals));
	}
	
	public void saveToArff() throws IOException
	{
	    ArffSaver saver = new ArffSaver();
	    saver.setInstances(data);
	    saver.setFile(new File(name+noInstances+".arff"));
	    saver.writeBatch();
	}
}
