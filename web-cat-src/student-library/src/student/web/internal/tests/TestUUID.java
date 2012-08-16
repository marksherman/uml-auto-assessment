package student.web.internal.tests;

import java.util.UUID;

import org.junit.Test;

public class TestUUID {
	@Test
	public void uuidtest()
	{
		for(int i = 0; i < 10; i++)
		{
		UUID test = UUID.randomUUID();
		System.out.println(test);
		}
		//UUID test2 = UUID.fromString(test.toString());
		//System.out.println(test.compareTo(test2));
	}
}
