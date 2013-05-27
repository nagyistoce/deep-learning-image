package home.mutant.deep.model;

public class ModelTestResult 
{
	public Integer result;
	public Integer noMatchedSample;
	public Double percentCertainty;
	public ModelTestResult(Integer result, Integer noMatchedSample,
			Double percentCertainty) {
		super();
		this.result = result;
		this.noMatchedSample = noMatchedSample;
		this.percentCertainty = percentCertainty;
	}
}
