package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.AbstractHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class LinkageErrorHandler extends AbstractHandler
implements
ExceptionHandlerInterface
{
    private static final Class<LinkageError> CLASS_TYPE = LinkageError.class;
    @Override
    protected Class<? extends Throwable> getExceptionType()
    {
        return CLASS_TYPE;
    }
	@Override
	public String getNewMessage(Throwable exToWrap)
	{
		String newMessage = "An error occured in trying to read one of the "
		    + ".class files. It may have been corrupted.  You should "
		    + "probably delete the .class files and then recompile.  I'm "
		    + "sorry, I don't have anything more specific to tell you.  "
		    + "Contact your instructor or a TA if you need more help.";

		return newMessage;
	}

}
