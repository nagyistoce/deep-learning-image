package home.mutant.deep.utils;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class TestMathUtils
{
	@Test
	public void testMean()
    {
            int[] test = new int[3];
            test[0] = 2;
            test[1] = 3;
            test[2] = 4;
            assertEquals(3.0, MathUtils.mean(test));
            int[][] cc = new int[3][2];
            System.out.println(cc.length);
            System.out.println(cc[0].length);
    }
       
    @Test
    public void testStandardDev()
    {
            int[] test = new int[3];
            test[0] = 5;
            test[1] = 4;
            test[2] = 6;
            assertEquals(0.816496580927726, MathUtils.standardDev(test));
    }
    
    @Test
    public void testIndexMaxMultiple()
    {
    	double[]t = {-2, 3.4, 15, 20 , 30, -25, 0, 17};
    	List<Integer> res = MathUtils.indexMaxMultiple(3, t);
    	assertEquals(3, res.size());
    	assertEquals(4, res.get(0));
    	assertEquals(3, res.get(1));
    	assertEquals(7, res.get(2));
    }
    @Test
    public void testCrossEntropy()
    {
    	double[] p =new double[]{0.333,0.333,0.333};
    	double[] q2 =new double[]{0.333,0.333,0.333};
    	for (double q=0;q<1;q+=0.1)
    	{
    		//System.out.println(p[0]*Math.log(q)+p[1]*Math.log(1-q));
    	}
    	int i=0;
    	while(i++<10000)
    	{
    		p[0]=Math.random()/3;
    		q2[0]=Math.random()/3;
    		q2[1]=Math.random()/3;
    		p[1]=Math.random()/3;
    		p[2]=1-p[0]-p[1];
    		q2[2]=1-q2[0]-q2[1];
    		System.out.println((p[0]*Math.log(p[0])+p[1]*Math.log(p[1])+p[2]*Math.log(p[2]))/(p[0]*Math.log(q2[0])+p[1]*Math.log(q2[1])+p[2]*Math.log(q2[2])));
    		System.out.println(p[0]*q2[0]+p[1]*q2[1]+p[2]*q2[2]);
    		System.out.println();
    	}
    }
}

