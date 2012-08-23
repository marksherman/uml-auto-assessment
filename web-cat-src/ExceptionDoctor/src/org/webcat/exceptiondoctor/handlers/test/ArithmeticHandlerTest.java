package org.webcat.exceptiondoctor.handlers.test;

import org.webcat.exceptiondoctor.handlers.ArithmeticExceptionHandler;
import junit.framework.TestCase;

public class ArithmeticHandlerTest extends TestCase
{
	ArithmeticExceptionHandler handle = null;

	@Override
	public void setUp()
	{
		handle = new ArithmeticExceptionHandler();
	}

	public void testFindDenomExpression()
	{

		ArithmeticException wrapped = null;
		try
		{
			@SuppressWarnings("unused")
			int a = 1 / 0;
		}
		catch (ArithmeticException e)
		{
			try
			{
				wrapped = (ArithmeticException) handle.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		assertNotNull(wrapped);
		assertTrue(wrapped.getMessage().contains("divide by zero"));
        System.out.println(wrapped.getClass() + ":" + wrapped.getMessage());
	}

	public void testFindDenomExpressionComplex()
	{
		ArithmeticException wrapped = null;
		try
		{
			int b = 0;
			int c = 0;
			@SuppressWarnings("unused")
			int a = 1 / (b + c);
		}
		catch (ArithmeticException e)
		{
			try
			{
				wrapped = (ArithmeticException) handle.wrapException(e);
			}
			catch (Throwable e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		assertNotNull(wrapped);
		assertTrue(wrapped.getMessage().contains("(b + c)"));
        System.out.println(wrapped.getClass() + ":" + wrapped.getMessage());
	}

    public void testFindDenomExpressionDoubleDivide()
    {
        ArithmeticException wrapped = null;
        try
        {
            int b = 0;
            @SuppressWarnings("unused")
            /* a / c */ int a = 100 / 2 / b / 3; // d /e/f
        }
        catch (ArithmeticException e)
        {
            try
            {
                wrapped = (ArithmeticException) handle.wrapException(e);
            }
            catch (Throwable e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }

        assertNotNull(wrapped);
        assertTrue(wrapped.getMessage().contains("\"b\""));
        System.out.println(wrapped.getClass() + ":" + wrapped.getMessage());
    }

    public void testFindDenomExpressionTripleDivide()
    {
        ArithmeticException wrapped = null;
        try
        {
            int b = 0;
            int g = 0;
            @SuppressWarnings("unused")
            /* a / c */ int a = 100 / 2 / b / 3 /    g; // d /e/f
        }
        catch (ArithmeticException e)
        {
            try
            {
                wrapped = (ArithmeticException) handle.wrapException(e);
            }
            catch (Throwable e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }

        assertNotNull(wrapped);
        assertTrue(wrapped.getMessage().contains("\"b\" or \"g\""));
//        System.out.println(wrapped.getClass() + ":" + wrapped.getMessage());
        wrapped.printStackTrace();
    }
}
