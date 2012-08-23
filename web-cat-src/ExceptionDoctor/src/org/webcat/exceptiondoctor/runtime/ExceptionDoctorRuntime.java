package org.webcat.exceptiondoctor.runtime;

import java.lang.reflect.InvocationTargetException;
import org.webcat.exceptiondoctor.ExceptionDoctor;

public class ExceptionDoctorRuntime
{

	private boolean verbose = true;

	/**
	 * @param args
	 */
	public ExceptionDoctorRuntime()
	{

	}

	public ExceptionDoctorRuntime(boolean verbosity)
	{
		verbose = verbosity;
	}

	public void printMappings()
	{
		System.out.println(ExceptionMap.getExceptionMap().toString());
	}

	public void start(String[] args)
	{
	    if (args == null || args.length == 0)
	    {
	        throw new IllegalArgumentException(
	            "You must provide a command line with a main class name");
	    }
		Throwable foundException = null;
		boolean runStatus = true;
		try
		{
			runStatus = (new CodeRunner()).runMain(args[0], args);
		}
		catch (InvocationTargetException ex)
		{
            foundException = ex.getCause();
		}
		catch (Exception ex)
		{
			foundException = ex;
		}

		if (runStatus == false || foundException == null)
		{
//			System.out.println("The code ran without any errors");
		}
		else
		{
			foundException = ExceptionDoctor.addExplanation(foundException);
			if (verbose)
			{
				foundException.printStackTrace();
			}

		}

	}

}
