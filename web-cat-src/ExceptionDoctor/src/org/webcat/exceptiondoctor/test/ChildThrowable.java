package org.webcat.exceptiondoctor.test;

public class ChildThrowable extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChildThrowable(String message, Throwable cause)
	{
		super(message, cause);
	}

}
