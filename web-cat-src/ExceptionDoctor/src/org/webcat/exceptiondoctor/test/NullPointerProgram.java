package org.webcat.exceptiondoctor.test;

public class NullPointerProgram
{
	/**
	 * @param args
	 */
	@SuppressWarnings("null")
	public static void main(String[] args)
	{
	    System.out.print(args.length + " args");
	    if (args.length > 0)
	    {
	        System.out.print(":");
	        for (String arg : args)
	        {
	            System.out.print(" \"" + arg + "\"");
	        }
	        System.out.println();
	    }

		Object test = null;
		test.getClass();
	}
}
