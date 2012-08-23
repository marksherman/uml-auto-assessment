package org.webcat.exceptiondoctor.test;

public class UserDefinedProgram
{
	/**
	 * @param args
	 */
	public static void main(String[] args) throws ChildThrowable
	{
		throw new ChildThrowable("Test", new NullPointerException());
	}
}
