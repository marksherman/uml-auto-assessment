package org.webcat.exceptiondoctor.runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This class is lightly tested and is not intended for normal use. This class
 * will run a source code file from the command line without the use of JUNIT.
 *
 * @author mike
 *
 */
public class CodeRunner
{
    /**
     * Create a new object.
     */
	public CodeRunner()
	{
	    // Nothing to do
	}

	/**
	 * Get the main method from the class file
	 *
	 * @param className
	 * @return
	 */
	private Method getMain(String className)
	    throws Exception
	{
		Class<?> classToRun;
//		try
//		{
			classToRun = Class.forName(className);
//		}
//		catch (ClassNotFoundException e)
//		{
//			return null;
//		}

		Method method;
//		try
//		{
			method = classToRun.getMethod("main", java.lang.String[].class);
//		}
//		catch (SecurityException e)
//		{
//			return null;
//		}
//		catch (NoSuchMethodException e)
//		{
//			return null;
//		}
		return method;
	}

	/**
	 * run the main() method from a class file
	 *
	 * @param className The class containing the main() method.
	 * @param myArguments The arguments to pass to main().
	 * @return True if main() was found and executed, or false otherwise.
	 * @throws Exception If any exception is thrown from main().
	 */
	public boolean runMain(String className, String[] myArguments)
			throws Exception
	{
		String[] arguments = new String[myArguments.length - 1];
		System.arraycopy(myArguments, 1, arguments, 0, myArguments.length - 1);
		Method mainMethod = getMain(className);
		if (mainMethod != null)
		{
			try
			{
				mainMethod.invoke(null, (Object) arguments);
			}
			catch (InvocationTargetException e)
			{
			    Throwable result = e.getCause();

			    // Trim the reflection stuff off the stack trace
		        StackTraceElement [] stackTrace = result.getStackTrace();
		        if (stackTrace.length > 0)
		        {
		            // First, search from the top of the stack to find
		            // this class
		            int i = stackTrace.length - 1;
		            String thisClassName = getClass().getName();
		            for (; i > 0; i--)
		            {
		                if (thisClassName.equals(stackTrace[i].getClassName()))
		                {
		                    i--;
	                        break;
		                }
		            }

		            // Now continue to skip over all the Java reflection stuff
		            for (; i >= 0; i--)
		            {
                        if (!stackTrace[i].getClassName().contains(".reflect."))
                        {
                            break;
                        }
		            }

		            // Finally, truncate the stack at this point
		            if ( i >= 0)
		            {
		                StackTraceElement [] newStackTrace =
		                    new StackTraceElement[i + 1];
		                System.arraycopy(
		                    stackTrace, 0, newStackTrace, 0, i + 1);
		                result.setStackTrace(newStackTrace);
		            }
		        }

		        // Re-wrap the modified exception
		        throw new InvocationTargetException(result);
			}
//			catch (IllegalArgumentException e)
//			{
//				return false;
//			}
//			catch (IllegalAccessException e)
//			{
//				return false;
//			}

			return true;
		}
		return false;
	}

}
