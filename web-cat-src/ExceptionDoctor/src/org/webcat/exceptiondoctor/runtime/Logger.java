package org.webcat.exceptiondoctor.runtime;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This is the logging functionality built into ExceptionDoctor. Each logging
 * function logs a different state ExceptionDoctor has entered.
 * 
 * @author mike
 * 
 */
public class Logger
{

	private static String sysProp = System.getProperty("ExceptionDoctor.log");

	private BufferedWriter writer;

	private FileWriter log;

	/**
	 * create a new logging class.
	 */
	public Logger()
	{
		try
		{
			log = new FileWriter(sysProp, true);
		}
		catch (IOException e)
		{
			HaltHandler.Halt("Error in opening ExceptionDoctor.log");
		}
		writer = new BufferedWriter(log);
	}

	/**
	 * This checks if logging is active.
	 * 
	 * @return returns true if active.
	 */
	public static boolean isActive()
	{
		if (sysProp != null)
		{
			return true;
		}
		return false;
	}

	/**
	 * logs an error such as an internal exception.
	 * 
	 * @param exception
	 *            the exception that ExceptionDoctor was attempting to parse
	 */
	public void logError(Throwable exception)
	{
		try
		{
			writer.append(exception.getClass().getSimpleName() + "\t" + "2\n");
			// writer.close();
		}
		catch (IOException e)
		{
			HaltHandler.Halt("Error writing to ExceptionDoctor.log");
		}

	}

	/**
	 * logs an exception that has successfully be rewritten. The log function
	 * will log success or failure according to internal algorithms
	 * 
	 * @param exception
	 *            the exception that ExceptionDoctor was attempting to parse
	 */
	public void log(Throwable exception)
	{
		try
		{
			if (exception.getMessage().contains(
					"Contact your instructor or a TA"))
			{

				writer.append(exception.getClass().getSimpleName() + "\t"
						+ "0\n");

			}
			else
			{
				writer.append(exception.getClass().getSimpleName() + "\t"
						+ "1\n");
			}

			// writer.close();
		}
		catch (IOException e)
		{
			HaltHandler.Halt("Error writing to ExceptionDoctor.log");
		}
	}

	/**
	 * logs that the exception was unable to finish but there is source in the
	 * stacktrace.
	 * 
	 * @param exception
	 *            the exception that ExceptionDoctor was attempting to parse
	 */
	public void logSource(Throwable exception)
	{
		try
		{

			writer.append(exception.getClass().getSimpleName() + "\t" + "3\n");
			writer.close();
		}
		catch (IOException e)
		{
			HaltHandler.Halt("Error writing to ExceptionDoctor.log");
		}

	}

	/**
	 * logs that the exception was unable to finish and there is no source in
	 * the stacktrace.
	 * 
	 * @param exception
	 *            the exception that ExceptionDoctor was attempting to parse
	 */
	public void logNoSource(Throwable exception)
	{
		try
		{

			writer.append(exception.getClass().getSimpleName() + "\t" + "4\n");
			writer.close();
		}
		catch (IOException e)
		{
			HaltHandler.Halt("Error writing to ExceptionDoctor.log");
		}

	}

	/**
	 * closes the log file.
	 */
	public void close()
	{
		try
		{
			writer.close();
		}
		catch (IOException e)
		{
			HaltHandler.Halt("Error closing ExceptionDoctor.log");
		}

	}
}
