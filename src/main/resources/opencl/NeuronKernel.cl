__kernel void outputNeuron(__global float *synapses, __constant float *image)
{
	int gid = get_global_id(0);
	int neuronIndex = gid*50;
	int index=0;
	int indexImage=0;
	float sum=0;
	float updates = synapses[neuronIndex+49];
	float synapseBuffer[49];
	float imageBuffer[49];
	float weight;
	for(index=0;index<49;index++)
		synapseBuffer[index] = synapses[neuronIndex+index];
	for(indexImage=0;indexImage<144;indexImage++)
	{
		sum = 0;
		for(index=0;index<49;index++)
		{
			imageBuffer[index] = image[indexImage*36+index];
			weight = synapseBuffer[index]-imageBuffer[index];
			sum=sum+weight*weight;
		}
		sum = sqrt(sum);
		if (sum<750/log(updates+2.71828))
		{
			for(index=0;index<49;index++)
			{
				synapseBuffer[index] = (synapseBuffer[index]+imageBuffer[index])/2;
			}
			updates=updates+1;
		}
		
	}
	for(index=0;index<49;index++)
		synapses[neuronIndex+index] = synapseBuffer[index] ;
	synapses[neuronIndex+49] = updates;
};