package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.AbstractHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class NoSuchElementExceptionHandler extends AbstractHandler
implements
ExceptionHandlerInterface
{
    private static final Class<NoSuchElementException> CLASS_TYPE = NoSuchElementException.class;
    @Override
    protected Class<? extends Throwable> getExceptionType()
    {
        return CLASS_TYPE;
    }

	@Override
	public String getNewMessage(Throwable exToWrap)
	{
		String newMessage = "It appears that the code was trying to access "
		    + "an element but none exists.  Try to use a method like "
		    + "hasNext() or hasMoreElements() before trying to access "
		    + "each element.";

		return newMessage;
	}

}
