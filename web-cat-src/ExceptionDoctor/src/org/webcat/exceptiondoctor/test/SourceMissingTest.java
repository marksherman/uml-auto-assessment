package org.webcat.exceptiondoctor.test;

import junit.framework.TestCase;

public class SourceMissingTest extends TestCase
{
	public void testMissingSource()
	{
	    try
	    {
	        Object x = null;
	        System.out.println(x.toString());
	    }
	    catch (NullPointerException e)
	    {
	        System.out.println("original:");
	        e.printStackTrace();
	        // As a test, replace the class name in the top-level stack
	        // trace element with a non-existent class name, to see how
	        // ExceptionDoctor deals with it.
	        StackTraceElement[] oldTrace = e.getStackTrace();
            oldTrace[0] = new StackTraceElement(
                "ImpossibleClassName",
                oldTrace[0].getMethodName(),
                oldTrace[0].getFileName(),
                oldTrace[0].getLineNumber());
            e.setStackTrace(oldTrace);

            System.out.println("modified:");
            e.printStackTrace();
	        throw e;
	    }
	}
}
