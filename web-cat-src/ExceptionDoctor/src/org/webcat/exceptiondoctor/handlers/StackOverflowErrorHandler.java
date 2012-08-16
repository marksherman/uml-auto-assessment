package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.AbstractHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class StackOverflowErrorHandler extends AbstractHandler
implements
ExceptionHandlerInterface
{
    private static final Class<StackOverflowError> CLASS_TYPE = StackOverflowError.class;
    @Override
    protected Class<? extends Throwable> getExceptionType()
    {
        return CLASS_TYPE;
    }

	@Override
	public String getNewMessage(Throwable exToWrap)
	{
		String errorString = "It seems the recursive part of your "
		    + "code is not reaching its exit condition.  You may have "
		    + "inadvertently written an infinitely recursive method.  "
		    + "Check the recursive method(s) and make sure that it (they)"
		    + " will always eventually reach some base case.";
		return errorString;
	}

}
