package org.webcat.exceptiondoctor.handlers.test;

import org.webcat.exceptiondoctor.handlers.StringIndexOutOfBoundsExceptionHandler;
import junit.framework.TestCase;

public class StringBoundsHandlerTest extends TestCase
{

	public void testHandleException()
	{
		StringIndexOutOfBoundsExceptionHandler handle = new StringIndexOutOfBoundsExceptionHandler();
		StringIndexOutOfBoundsException wrapped = null;
		try
		{
			String smallString = "hi";
			smallString.substring(100);
		}
		catch (StringIndexOutOfBoundsException e)
		{

			try
			{
				wrapped = (StringIndexOutOfBoundsException) handle
						.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		assertNotNull(wrapped);
		assertTrue(wrapped.getMessage().contains("hi"));
		System.out.println(wrapped.getMessage());
	}

}
