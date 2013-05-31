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
}

