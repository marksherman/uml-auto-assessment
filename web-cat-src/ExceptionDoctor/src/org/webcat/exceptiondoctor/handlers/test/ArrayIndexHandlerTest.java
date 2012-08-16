package org.webcat.exceptiondoctor.handlers.test;

import org.webcat.exceptiondoctor.handlers.ArrayIndexOutOfBoundsExceptionHandler;
import junit.framework.TestCase;

public class ArrayIndexHandlerTest extends TestCase
{
	int[] arrayObject;

	ArrayIndexOutOfBoundsExceptionHandler handle;

	@Override
	public void setUp()
	{
		arrayObject = new int[30];
		handle = new ArrayIndexOutOfBoundsExceptionHandler();
	}

	public void testGetArrayName()
	{
		ArrayIndexOutOfBoundsException wrapped = null;
		try
		{
			@SuppressWarnings("unused")
			Object test = arrayObject[-1];
		}
		catch (ArrayIndexOutOfBoundsException t)
		{
			try
			{
				wrapped = (ArrayIndexOutOfBoundsException) handle
						.wrapException(t);
			}
			catch (Throwable e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		assertTrue(wrapped != null);
		assertTrue(wrapped.getMessage().contains("arrayObject"));
	}

	public void testGetVariableName()
	{
		ArrayIndexOutOfBoundsException wrapped = null;
		try
		{
			@SuppressWarnings("unused")
			int test = arrayObject[1] + arrayObject[4] + arrayObject[-1];
		}
		catch (ArrayIndexOutOfBoundsException t)
		{
			try
			{
				wrapped = (ArrayIndexOutOfBoundsException) handle
						.wrapException(t);
			}
			catch (Throwable e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		assertTrue(wrapped != null);
		assertTrue(wrapped.getMessage().contains("arrayObject"));
		System.out.println(wrapped.getMessage());
	}

	public void testGetIndexValue()
	{
		ArrayIndexOutOfBoundsException wrapped = null;
		try
		{
			@SuppressWarnings("unused")
			Object test = arrayObject[-1];
		}
		catch (ArrayIndexOutOfBoundsException t)
		{
			try
			{
				wrapped = (ArrayIndexOutOfBoundsException) handle
						.wrapException(t);
			}
			catch (Throwable e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		assertTrue(wrapped != null);

		assertTrue(wrapped.getMessage().contains("-1"));

	}

	public void testGetIndexValueVariable()
	{
		ArrayIndexOutOfBoundsException wrapped = null;
		try
		{
			int a = -1;
			@SuppressWarnings("unused")
			Object test = arrayObject[a];
		}
		catch (ArrayIndexOutOfBoundsException t)
		{
			try
			{
				wrapped = (ArrayIndexOutOfBoundsException) handle
						.wrapException(t);
			}
			catch (Throwable e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		assertTrue(wrapped != null);
		assertTrue(wrapped.getMessage().contains("a"));
		System.out.println(wrapped.getMessage());
	}

	public void testGetVariables()
	{
		ArrayIndexOutOfBoundsException wrapped = null;
		try
		{
			@SuppressWarnings("unused")
			Object test = arrayObject[-1];
		}
		catch (ArrayIndexOutOfBoundsException t)
		{
			try
			{
				wrapped = (ArrayIndexOutOfBoundsException) handle
						.wrapException(t);
			}
			catch (Throwable e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		assertTrue(wrapped != null);

		assertTrue(wrapped.getMessage().contains("arrayObject"));
	}

	public void testGetVariablesMulti()
	{
		ArrayIndexOutOfBoundsException wrapped = null;
		int[] arrayObject2 = new int[4];
		try
		{
			@SuppressWarnings("unused")
			Object test = arrayObject[-1] + arrayObject2[1];
		}
		catch (ArrayIndexOutOfBoundsException t)
		{
			try
			{
				wrapped = (ArrayIndexOutOfBoundsException) handle
						.wrapException(t);
			}
			catch (Throwable e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		assertTrue(wrapped != null);

		assertTrue(wrapped.getMessage().contains("arrayObject2"));
		System.out.println(wrapped.getMessage());
	}

	public void testBadIndex()
	{
		@SuppressWarnings("unused")
		ArrayIndexOutOfBoundsException wrapped = null;
		try
		{
			Object[] tenElementArray = new Object[10];
			@SuppressWarnings("unused")
			Object foo = tenElementArray[15];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			try
			{
				wrapped = (ArrayIndexOutOfBoundsException) handle
						.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

}
