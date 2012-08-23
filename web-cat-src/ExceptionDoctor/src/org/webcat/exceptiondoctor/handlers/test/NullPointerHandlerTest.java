package org.webcat.exceptiondoctor.handlers.test;

import java.io.File;

import org.webcat.exceptiondoctor.handlers.NullPointerExceptionHandler;
import junit.framework.TestCase;

public class NullPointerHandlerTest extends TestCase
{

	@SuppressWarnings("null")
	public void testHandleException()
	{
		NullPointerExceptionHandler handle = new NullPointerExceptionHandler();
		NullPointerException wrapped = null;
		try
		{
			Object test = null;
			test.getClass();
		}
		catch (NullPointerException e)
		{
			try
			{
				wrapped = (NullPointerException) handle.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
//		throw wrapped;
		assertNotNull(wrapped);
		wrapped.printStackTrace();
	}

	@SuppressWarnings("null")
	public void testHandleException2()
	{
		NullPointerExceptionHandler handle = new NullPointerExceptionHandler();
		NullPointerException wrapped = null;
		try
		{
			String nullString = null;
			Integer i = 1;
			Integer j = 2;
			nullString.substring(i.intValue(), j.intValue());

		}
		catch (NullPointerException e)
		{
			try
			{
				wrapped = (NullPointerException) handle.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		assertNotNull(wrapped);
		System.out.println(wrapped.getClass() + ":" + wrapped.getMessage());
	}

	@SuppressWarnings("null")
	public void testHandleException3()
	{
		NullPointerExceptionHandler handle = new NullPointerExceptionHandler();
		NullPointerException wrapped = null;
		try
		{
			Integer i = null;
			System.out.println(i.toString().equals(i.intValue()));

		}
		catch (NullPointerException e)
		{
			try
			{
				wrapped = (NullPointerException) handle.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		assertNotNull(wrapped);
		System.out.println(wrapped.getClass() + ":" + wrapped.getMessage());
	}

	public void testBugException()
	{
		NullPointerExceptionHandler handle = new NullPointerExceptionHandler();
		NullPointerException wrapped = null;
		try
		{
			String foo = "foo";
			String bar = null;
			foo.contains(bar);
		}
		catch (NullPointerException e)
		{
			try
			{
				wrapped = (NullPointerException) handle.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		assertNotNull(wrapped);
		System.out.println(wrapped.getMessage());
	}
	public void testBug2()
	{
		NullPointerExceptionHandler handle = new NullPointerExceptionHandler();
		NullPointerException wrapped = null;
		try
		{
			String toSub = "foo.bar";
			Integer foo = null;
			Integer bar = null;
			toSub.substring(foo.intValue(),bar.intValue());
		}
		catch (NullPointerException e)
		{
			try
			{
				wrapped = (NullPointerException) handle.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		assertNotNull(wrapped);
		System.out.println(wrapped.getMessage());
	}
	public void testLibraryNull()
	{
	    NullPointerExceptionHandler handle = new NullPointerExceptionHandler();
        NullPointerException wrapped = null;
        try
        {
            File library = new File((String)null);
        }
        catch (NullPointerException e)
        {
            try
            {
                wrapped = (NullPointerException) handle.wrapException(e);
            }
            catch (Throwable e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                assertTrue(false);
            }

        }
        wrapped.printStackTrace();
	}
}
