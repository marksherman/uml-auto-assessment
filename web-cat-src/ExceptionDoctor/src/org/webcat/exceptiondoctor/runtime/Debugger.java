package org.webcat.exceptiondoctor.runtime;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * this class handles debug printing for ExceptionDoctor.
 * 
 * @author mike
 * 
 */
public abstract class Debugger
{
	private static String prefix = "EDOC:\t";

	private static String verbose = System
			.getProperty("ExceptionDoctor.verbose");

	private static String verboseFile = System
			.getProperty("ExceptionDoctor.outputLog");

	// private static BufferedWriter writer = null;

	/**
	 * Get the writer that the debug log should be written to.
	 * 
	 * @return the actual writer.
	 */
	private static BufferedWriter getWriter()
	{
		// if (writer != null)
		// {
		// return writer;
		// }
		// else
		// {
		FileWriter log = null;
		BufferedWriter writer = null;
		try
		{
			log = new FileWriter(verboseFile);
		}
		catch (IOException e)
		{
			System.out.println("HALT:  Error In Output Logging File");
			Runtime.getRuntime().halt(-1);
		}
		writer = new BufferedWriter(log);
		return writer;
	}

	/**
	 * print a line to the log.
	 * 
	 * @param log
	 *            what to print
	 */
	public static void println(String log)
	{
		log += "\n";
		if (verbose != null && verbose.equals("true"))
		{
			if (verboseFile != null)
			{
				BufferedWriter outputLog = getWriter();
				try
				{
					outputLog.append(prefix + log);
					outputLog.close();
				}
				catch (IOException e)
				{
					HaltHandler
							.Halt("Error writing to ExceptionDoctor.outputLog");
				}
			}
			System.out.println(prefix + log);
		}

	}
}
