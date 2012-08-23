package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.StringTokenizer;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.AbstractHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;

public class StringIndexOutOfBoundsExceptionHandler extends AbstractHandler
implements
ExceptionHandlerInterface
{
    private static final Class<StringIndexOutOfBoundsException> CLASS_TYPE = StringIndexOutOfBoundsException.class;
    @Override
    protected Class<? extends Throwable> getExceptionType()
    {
        return CLASS_TYPE;
    }

	@Override
	public String getNewMessage(Throwable exToWrap)
	{
		String newMessage = "";
		newMessage += "It seems that you tried to index a string with a value " +
				"that it outside of the length of the string.  Make sure that your" +
				" index never excedes the actual length of the string.";

		return newMessage;
	}

}
