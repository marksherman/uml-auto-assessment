package org.webcat.exceptiondoctor.runtime;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;


public class ExceptionMap
{
	// Singleton
	private static ExceptionMap singletonMap = new ExceptionMap();

	// The map that stores the Exception names and handlers that handle them.
	private HashMap<String, ExceptionHandlerInterface> exMap;

	/**
	 * Create a new exception map, this initializes the hashmap.
	 *
	 */
	private ExceptionMap()
	{
		exMap = new HashMap<String, ExceptionHandlerInterface>();
	}

	/**
	 * Exception map getter
	 *
	 * @return this gets the exception map singleton
	 */
	public synchronized static ExceptionMap getExceptionMap()
	{
		return singletonMap;
	}

	/**
	 * This method adds a mapping from an exception to a handler
	 *
	 * @param exceptionName
	 *            Exception to be mapped
	 * @param handlerClassRef
	 *            Handler to be mapped
	 */
	private synchronized void addMapping(String exceptionName,
			ExceptionHandlerInterface handlerClassRef)
	{
		exMap.put(exceptionName, handlerClassRef);
	}

	/**
	 * The method looks for a maping from the exception to an appropriate
	 * handler. If there is no mapping it will create one (Only if there is a
	 * handler for the specific exception.
	 *
	 * @param exception
	 *            The exception to find a handler for.
	 * @return A handler for the class. This will never return null because All
	 *         exceptions are inherited from Throwable.
	 */
	public synchronized ExceptionHandlerInterface getHandler(Throwable exception)
	{
		ExceptionHandlerInterface classRef = null;
		/*
		 * This tracks if the method is at the first level of recursion. It
		 * makes sure that no mapping is created between a throwable and a
		 * derived cause.
		 */
		boolean topLevel = true;

		classRef = getHandler(exception, topLevel);
		return classRef;
	}

	/**
	 * This method creates an instance of a handler from a class name.
	 *
	 * @param classRef
	 *            The handler class to be initiated.
	 * @return an instance of the class or null if it could not be instanciated.
	 */
	private AbstractExceptionHandler getHandlerInstance(Class<?> classRef)
			throws ClassNotFoundException
	{
		AbstractExceptionHandler ex = null;
		// All exception handlers have a no argument constructor.
		Class<?>[] args = {};
		Constructor<?> maker;

		// Try to create an instance of the handler. If not then
		// return null.
		try
		{
			maker = classRef.getConstructor(args);
			Object[] instArgs = {};
			ex = (AbstractExceptionHandler) maker.newInstance(instArgs);
		}
		catch (Throwable e)
		{
			throw new ClassNotFoundException();
		}

		return ex;
	}

	/**
	 * This method gets the handler for a corresponding exception.
	 *
	 * @param exception
	 *            The exception we are looking for a handler for
	 * @param topLevel
	 *            The level of recursion
	 * @return An instance of the corresponding handler
	 */
	private ExceptionHandlerInterface getHandler(Throwable exception,
			boolean topLevel)
	{
		// Look for an existing mapping
		String exceptionName = getExceptionName(exception);
		ExceptionHandlerInterface classRef = exMap.get(exceptionName);
		// If mapping doesnt exist try to make one.
		if (classRef == null)
		{
			classRef = buildMapEntry(exception, topLevel);
		}
		return classRef;
	}

	/**
	 * This is a utility to get the simple name for an exception.
	 *
	 * @param exception
	 *            the named exception
	 * @return The simple name for the exception
	 */
	private String getExceptionName(Throwable exception)
	{
		return exception.getClass().getSimpleName();
	}

	/**
	 * This builds a map entry for the exception if you are at the top level of
	 * recursion, otherwise it simply returns the appropriate handler
	 *
	 * @param exception
	 *            the exception to find a mapping for
	 * @param topLevel
	 *            Level of recursion
	 * @return either the appropriate handle or null.
	 */
	private ExceptionHandlerInterface buildMapEntry(Throwable exception,
			boolean topLevel)
	{
		Class<?> classRef = null;
		ExceptionHandlerInterface handle;
		String exName = getExceptionName(exception);
		// Try to find a mapping in the classpath.
		try
		{
		    try
		    {
		        // First, try the default package
		        classRef = Class.forName(exName + "Handler");
		    }
	        catch (ClassNotFoundException e)
	        {
                try
                {
                    // Next, try the built-in location
                    classRef = Class.forName(
                        "org.webcat.exceptiondoctor.handlers."
                        + exName
                        + "Handler");
                }
                catch (ClassNotFoundException ee)
                {
                    // Finally, look in the older, legacy location
                    classRef = Class.forName("handlers." + exName + "Handler");
                }
	        }
			handle = getHandlerInstance(classRef);
			if (topLevel)
			{
				addMapping(exName, handle);
			}
		}
		catch (ClassNotFoundException e)
		{
			topLevel = false;
			if (exception.getCause() != null)
			{
				// Recursive call on the cause of the exception.
				handle = getHandler(exception.getCause(), topLevel);

			}
			else
			{
				return null;
			}
		}

		return handle;
	}
}
