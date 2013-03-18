package home.mutant.deep.model;

public interface RecognizerGenerator 
{
	Image generateSample();
	public Image forwardStep(Image bs);
}

