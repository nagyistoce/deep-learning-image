package home.mutant.deep.abstracts;

import home.mutant.deep.ui.Image;

public interface Neuron 
{
	long calculateOutput(Image image);
	void randomize();
	Image generateSample();
	void initWeightsFromImage(Image image);
}
