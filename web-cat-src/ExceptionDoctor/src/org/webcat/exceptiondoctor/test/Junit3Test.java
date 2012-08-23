package org.webcat.exceptiondoctor.test;

import junit.framework.TestCase;

public class Junit3Test extends TestCase
{
	public void testArrayStore()
	{
		Object x[] = new String[3];
		x[0] = new Integer(0);
	}

	public void testClassCast()
	{
		Object intObject = new Integer(1);
		@SuppressWarnings("unused")
		String test = (String) intObject;
	}

	public void testNoSuchElement()
	{
		Object x[] = new String[3];
		x[4] = "hello";
	}

	public void testThrowError4()
	{
		String x[] = new String[3];
		x[0].toLowerCase();
	}
}
