package org.webcat.exceptiondoctor.handlers.test;

import org.webcat.exceptiondoctor.handlers.StackOverflowErrorHandler;
import junit.framework.TestCase;

public class StackOverflowHandlerTest extends TestCase
{

	public void testHandleException()
	{
		StackOverflowErrorHandler handle = new StackOverflowErrorHandler();
		StackOverflowError wrapped = null;
		try
		{
			recurseMethod();
		}
		catch (StackOverflowError e)
		{
			try
			{
				wrapped = (StackOverflowError) handle.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		assertNotNull(wrapped);
	}

	private void recurseMethod()
	{
		recurseMethod();

	}

}
