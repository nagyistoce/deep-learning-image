package home.mutant.deep.utils;

import static org.junit.Assert.*;
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
}

