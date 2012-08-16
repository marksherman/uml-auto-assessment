package org.webcat.exceptiondoctor.runtime;

/**
 * This class handles how to tell a system that it should halt.
 * 
 * @author mike
 * 
 */
public abstract class HaltHandler
{
	/**
	 * Try to bail best as possible
	 * 
	 * @param customMessage
	 */
	public static void Halt(String customMessage)
	{
		System.out.println("EDOC HALT:\t" + customMessage);
		Runtime.getRuntime().halt(-1);
	}
}
