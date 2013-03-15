package home.mutant.deep.utils;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestImageUtils 
{
	@Test
	public void testDivideImage()
	{
		byte [] image = new byte[24];
		image[0]=1;
		image[1]=1;
		image[6]=1;
		image[7]=1;
		
		image[4]=2;
		image[5]=2;
		image[10]=2;
		image[11]=2;
		
		image[14]=3;
		image[15]=3;
		image[20]=3;
		image[21]=3;
		
		List<byte[]> divided =  ImageUtils.divideImage(image, 2, 2, 6, 4);
		assertEquals(6, divided.size());
		
		assertDivideImage(divided.get(0),(byte)1);
		assertDivideImage(divided.get(1),(byte)0);
		assertDivideImage(divided.get(2),(byte)2);
		assertDivideImage(divided.get(3),(byte)0);
		assertDivideImage(divided.get(4),(byte)3);
		assertDivideImage(divided.get(5),(byte)0);
	}
	
	private void assertDivideImage(byte[] image, byte value)
	{
		assertEquals(value, image[0]);
		assertEquals(value, image[1]);
		assertEquals(value, image[2]);
		assertEquals(value, image[3]);
	}
}
