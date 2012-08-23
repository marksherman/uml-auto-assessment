package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.AbstractHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class IOExceptionHandler extends AbstractHandler
implements
ExceptionHandlerInterface
{
    private static final Class<IOException> CLASS_TYPE = IOException.class;
    @Override
    protected Class<? extends Throwable> getExceptionType()
    {
        return CLASS_TYPE;
    }
	@Override
	public String getNewMessage(Throwable exToWrap)
	{
		String newMessage = "An error occured while trying to perform an "
		    + "input/output operation.  I'm sorry, I don't have anything "
		    + "more specific to tell you.  Contact your instructor or a TA "
		    + "if you need more help.";
		return newMessage;
	}

}
