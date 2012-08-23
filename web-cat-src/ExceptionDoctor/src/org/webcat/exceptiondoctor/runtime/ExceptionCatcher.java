package org.webcat.exceptiondoctor.runtime;

import org.webcat.exceptiondoctor.ExceptionHandlerInterface;

public class ExceptionCatcher
{
	/**
	 * Generic Constructor for ExceptionCatcher
	 *
	 */
	public ExceptionCatcher()
	{

	}

	/**
	 * This is the main entry point for the Backstop Redux suite.
	 *
	 * @param exception
	 *            the exception that will be wrapped.
	 * @return A wrapped exception with a more appropriate error message
	 */
	public Throwable findException(Throwable exception)
	{
		Debugger.println("Running Exception Doctor");
		Throwable wrapper = exception;
		Logger log = null;
		if (Logger.isActive())
		{
			log = new Logger();
		}
		try
		{
			// Grabs an instance of the Exception Map singleton
			ExceptionMap mapper = ExceptionMap.getExceptionMap();
			// Grabs a handle appropriate for the exception that is passed.
			ExceptionHandlerInterface handle = mapper.getHandler(exception);
			// Wrap the exception with an improved message

			if (handle != null)
			{
				Debugger.println("ExceptionDoctor Found an appropriate handler");
				wrapper = handle.wrapException(exception);
			}
			else
			{
				Debugger.println("ExceptionDoctor Could not find an appropriate handler");
				wrapper = exception;
				if (log != null)
				{
					log.logError(wrapper);
				}
			}

			if (log != null && handle != null)
			{
				log.log(wrapper);
			}

		}
		catch (Throwable t)
		{
			Debugger.println("ExceptionDoctor threw an exception");
			t.printStackTrace();
			if (Logger.isActive())
			{

				log.logError(exception);
			}
			wrapper = exception;
		}
		if (log != null)
		{
			log.close();
		}
		if (wrapper == null)
		{
			wrapper = exception;
		}
		return wrapper;

	}

}
