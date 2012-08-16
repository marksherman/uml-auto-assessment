package org.webcat.exceptiondoctor.test;

import junit.framework.TestCase;

import org.webcat.exceptiondoctor.handlers.NullPointerExceptionHandler;

public class MultiLineTest extends TestCase
{
	public void testMulti()
	{
		NullPointerExceptionHandler handle = new NullPointerExceptionHandler();
		NullPointerException wrapped = null;
		try
		{
			Object x = null;
			x.
			getClass();
		}
		catch (NullPointerException e)
		{

			wrapped = (NullPointerException) handle.wrapException(e);
			wrapped.printStackTrace();
		}
	}
	public void testMulti2()
	{
		NullPointerExceptionHandler handle = new NullPointerExceptionHandler();
		NullPointerException wrapped = null;
		Object x = null;
		try
		{
			x.
			getClass();
		}
		catch (NullPointerException e)
		{

			wrapped = (NullPointerException) handle.wrapException(e);
			wrapped.printStackTrace();
		}
	}
}

