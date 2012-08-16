package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.AbstractHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class ThrowableHandler extends AbstractHandler
implements
ExceptionHandlerInterface
{
    private static final Class<Throwable> CLASS_TYPE = Throwable.class;
    @Override
    protected Class<? extends Throwable> getExceptionType()
    {
        return CLASS_TYPE;
    }

	@Override
	public String getNewMessage(Throwable exToWrap)
	{
		String newMessage = "We could not identify the Exception that was thrown, "
				+ "this is a generic message";
		newMessage += "I'm sorry, I don't have anything more "
				+ "specific to tell you. Contact your instructor or a "
				+ "TA and provide the following information:";
		return newMessage;
	}

}
