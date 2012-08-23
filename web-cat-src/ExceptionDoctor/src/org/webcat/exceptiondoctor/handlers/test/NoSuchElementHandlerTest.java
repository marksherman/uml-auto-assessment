package org.webcat.exceptiondoctor.handlers.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.webcat.exceptiondoctor.handlers.NoSuchElementExceptionHandler;

import junit.framework.TestCase;

public class NoSuchElementHandlerTest extends TestCase
{
	public void testHandleException()
	{
		NoSuchElementExceptionHandler handle =
		    new NoSuchElementExceptionHandler();
		NoSuchElementException wrapped = null;
		try
		{
			List<Object> list = new ArrayList<Object>(10);
			Iterator<Object> i = list.iterator();
			i.next();
		}
		catch (NoSuchElementException e)
		{
			try
			{
				wrapped = (NoSuchElementException)handle.wrapException(e);
				wrapped.printStackTrace();
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		assertNotNull(wrapped);
	}
}
