package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.AbstractHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class ClassCastExceptionHandler extends AbstractHandler
implements
ExceptionHandlerInterface
{
    private static final Class<ClassCastException> CLASS_TYPE = ClassCastException.class;
    @Override
    protected Class<? extends Throwable> getExceptionType()
    {
        return CLASS_TYPE;
    }

	public String getActualType(String[] message)
	{
		String actual = message[0];
		return actual;
	}

	@Override
	public String getNewMessage(Throwable exToWrap)
	{
		if(exToWrap.getMessage() == null)
			return null;
			
		String[] splitMessage = exToWrap.getMessage().split(" ");
		String actual = getActualType(splitMessage);
		String newMessage = "It appears that the code tried to cast a "
				+ actual + " to the wrong type.  ";
		newMessage += "Remember, if you are casting, the object must be the "
		    + "same class as (or a subclass of) the type to which you are "
		    + "trying to cast.";
		return newMessage;
	}
}
