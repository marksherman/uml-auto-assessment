package org.webcat.exceptiondoctor;

import org.webcat.exceptiondoctor.runtime.ExceptionCatcher;
import org.webcat.exceptiondoctor.runtime.ExceptionDoctorRuntime;

public class ExceptionDoctor
{

	/**
	 * Supports running ExceptionDoctor from the command line.
	 *
	 * @param args command line arguments, beginning with the name of
	 * the main class to run
	 */
	public static void main(String[] args)
	{
        if (args == null || args.length == 0)
        {
            System.err.println(
                "usage: java "
                + ExceptionDoctor.class.getName()
                + " <main class name> [args ...]");
            return;
        }
		ExceptionDoctorRuntime br = new ExceptionDoctorRuntime();
		br.start(args);

	}


	/**
     * Supports one-line use of ExceptionDoctor from within other code.
     *
     * @param original The exception you want to explain
     * @return A new Throwable that is equivalent to the original,
     * but with a better, more explanatory message (if one can be found).
     */
    public static Throwable addExplanation(Throwable original)
    {
        return CATCHER.findException(original);
    }


    private static final ExceptionCatcher CATCHER = new ExceptionCatcher();
}
