package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.AbstractHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class ArrayStoreExceptionHandler extends AbstractHandler
implements
ExceptionHandlerInterface
{
    private static final Class<ArrayStoreException> CLASS_TYPE = ArrayStoreException.class;
    @Override
    protected Class<? extends Throwable> getExceptionType()
    {
        return CLASS_TYPE;
    }
	@Override
	public String getNewMessage(Throwable exToWrap)
	{
		String newMessage = "It seems that the code tried to store an object of type "
				+ getBadObjectType(exToWrap);
		newMessage += " in an array that was initialized to hold objects of a different type.";

		return newMessage;
	}

	public String getBadObjectType(Throwable exToWrap)
	{
		return exToWrap.getMessage();
	}
}
