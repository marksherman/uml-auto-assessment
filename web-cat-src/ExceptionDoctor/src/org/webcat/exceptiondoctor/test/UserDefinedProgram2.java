package org.webcat.exceptiondoctor.test;

public class UserDefinedProgram2
{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws ChildThrowable
	{
		throw new ChildThrowable("Test", new Exception());
	}

}
