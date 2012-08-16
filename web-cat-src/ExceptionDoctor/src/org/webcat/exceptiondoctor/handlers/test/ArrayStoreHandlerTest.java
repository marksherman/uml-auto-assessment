package org.webcat.exceptiondoctor.handlers.test;

import org.webcat.exceptiondoctor.handlers.ArrayStoreExceptionHandler;
import junit.framework.TestCase;

public class ArrayStoreHandlerTest extends TestCase
{

	public void testGetBadObjectType()
	{
		ArrayStoreExceptionHandler handle = new ArrayStoreExceptionHandler();
		ArrayStoreException wrapped = null;

		try
		{
			Object x[] = new String[3];
			x[0] = new Integer(0);

		}
		catch (ArrayStoreException e)
		{
			try
			{
				wrapped = (ArrayStoreException) handle.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		assertTrue(wrapped.getMessage().contains("java.lang.Integer"));
	}

}
