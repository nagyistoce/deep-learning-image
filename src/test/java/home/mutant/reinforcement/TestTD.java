package home.mutant.reinforcement;

import org.junit.Test;

public class TestTD 
{
	@Test
	public void testMouse()
	{
		Node a = new Node();
		Node b = new Node();
		Node c = new Node();
		Node bl = new Node();
		Node br = new Node();
		Node cl = new Node();
		Node cr = new Node();
		a.addChildren(b);
		a.addChildren(c);
		b.addChildren(bl);
		b.addChildren(br);
		c.addChildren(cl);
		c.addChildren(cr);
		bl.value=0;
		br.value=5;
		cl.value=2;
		cr.value=0;
		
		TD.evaluateNodes(100000, a);
		System.out.println(a.sumValues/a.countUpdates+":"+a.value);
		System.out.println(b.sumValues/b.countUpdates+":"+b.value);
		System.out.println(c.sumValues/c.countUpdates+":"+c.value);
		
	}
}
