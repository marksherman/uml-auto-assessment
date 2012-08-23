package org.webcat.exceptiondoctor.test;

public class NullPointerProgram2
{
	/**
	 * @param args
	 */
	@SuppressWarnings("null")
	public static void main(String[] args)
	{
        Integer i = null;
        System.out.println("i = " + i.intValue());
	}
}
