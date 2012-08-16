package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.AbstractHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class NegativeArraySizeExceptionHandler extends AbstractHandler
implements
ExceptionHandlerInterface
{
    private static final Class<NegativeArraySizeException> CLASS_TYPE = NegativeArraySizeException.class;
    @Override
    protected Class<? extends Throwable> getExceptionType()
    {
        return CLASS_TYPE;
    }

	@Override
	public String getNewMessage(Throwable exToWrap)
	{
		String newMessage = "It appears that the code is trying to create an "
		    + "array with a negative size.  Remember, the array must have a "
		    + "size that is a positive number.";
		return newMessage;
	}
}
