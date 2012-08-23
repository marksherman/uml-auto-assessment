package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.AbstractHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class OutOfMemoryErrorHandler extends AbstractHandler
implements
ExceptionHandlerInterface
{
    private static final Class<OutOfMemoryError> CLASS_TYPE = OutOfMemoryError.class;
    @Override
    protected Class<? extends Throwable> getExceptionType()
    {
        return CLASS_TYPE;
    }

	@Override
	public String getNewMessage(Throwable exToWrap)
	{
		String newMessage = "The Java Virtual Machine has run out of "
		    + "memory.  This may be caused by an infinite loop in your "
		    + "code, or perhaps you should increase the amount of memory "
		    + "allocated to java.";
		return newMessage;
	}


	@Override
	public Throwable wrapException(Throwable oldException)
	{
	    try
	    {
	        return super.wrapException(oldException);
	    }
	    catch (Throwable t)
	    {
	        // If anything bad happens trying to explain the OOM error,
	        // then just swallow the inner error and rethrow the OOM original
	        return oldException;
	    }
	}
}
