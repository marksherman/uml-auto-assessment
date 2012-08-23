package org.webcat.exceptiondoctor.handlers.test;

import org.webcat.exceptiondoctor.handlers.NegativeArraySizeExceptionHandler;
import junit.framework.TestCase;

public class NegativeArraySizeHandlerTest extends TestCase
{

	public void testHandleException()
	{
		NegativeArraySizeExceptionHandler handle = new NegativeArraySizeExceptionHandler();
		NegativeArraySizeException wrapped = null;
		try
		{
			@SuppressWarnings("unused")
			int[] testArray = new int[-1];
		}
		catch (NegativeArraySizeException e)
		{
			try
			{
				wrapped = (NegativeArraySizeException) handle.wrapException(e);
				wrapped.printStackTrace();
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		assertNotNull(wrapped);
		String newMessage = wrapped.getMessage();
		assertTrue(newMessage.contains("negative"));
		assertTrue(newMessage.contains("size"));
	}

}
