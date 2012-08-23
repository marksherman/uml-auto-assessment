package org.webcat.exceptiondoctor;

/**
 * This Exception tracks ExceptionDoctor's inability to locate source. The
 * information contained is important because as ExceptionDoctor backs out of
 * the call structure, it eventually envokes a unique handler to handle the
 * exception.
 * 
 * @author mike
 * 
 */
public class SourceCodeHiddenException extends Exception
{
	private static final long serialVersionUID = 1L;

	private StackTraceElement sourceElement;

	private Throwable origEx;

	/**
	 * Constructs an instance to track the two parameters
	 * 
	 * @param element
	 *            the stack trace element where source is found in the stack
	 *            trace
	 * @param origException
	 *            the original exception that was thrown.
	 */
	public SourceCodeHiddenException(StackTraceElement element,
			Throwable origException)
	{
		sourceElement = element;
		origEx = origException;
	}

	public StackTraceElement getSourceElement()
	{
		return sourceElement;
	}

	public Throwable getHiddenException()
	{
		return origEx;
	}
}
