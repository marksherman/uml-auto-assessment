package org.webcat.exceptiondoctor.handlers.test;

import org.webcat.exceptiondoctor.handlers.NumberFormatExceptionHandler;
import junit.framework.TestCase;

public class NumberFormatHandlerTest extends TestCase
{

	public void testHandleException()
	{
		NumberFormatExceptionHandler handler = new NumberFormatExceptionHandler();
		NumberFormatException wrapped = null;
		try
		{
			Integer.parseInt("test");
		}
		catch (NumberFormatException e)
		{
			try
			{
				wrapped = (NumberFormatException) handler.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		assertNotNull(wrapped);
		assertTrue(wrapped.getMessage().contains("test"));
	}

	public void testHandleException2()
	{
		NumberFormatExceptionHandler handler = new NumberFormatExceptionHandler();
		NumberFormatException wrapped = null;
		try
		{
			Integer.parseInt("3.59");
		}
		catch (NumberFormatException e)
		{
			try
			{
				wrapped = (NumberFormatException) handler.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			wrapped.printStackTrace();
		}
		assertNotNull(wrapped);
		assertTrue(wrapped.getMessage().contains("3.59"));

	}

	public void testHandleException3()
	{
		NumberFormatExceptionHandler handler = new NumberFormatExceptionHandler();
		NumberFormatException wrapped = null;
		try
		{
			Float.parseFloat("asdf");
		}
		catch (NumberFormatException e)
		{
			try
			{
				wrapped = (NumberFormatException) handler.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		assertNotNull(wrapped);
		assertTrue(wrapped.getMessage().contains("asdf"));
	}

}
