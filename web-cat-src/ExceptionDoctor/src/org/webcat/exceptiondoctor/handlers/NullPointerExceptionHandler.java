package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import java.util.List;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.AbstractHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class NullPointerExceptionHandler extends AbstractHandler
                implements
                ExceptionHandlerInterface
{
    private static final Class<NullPointerException> CLASS_TYPE = NullPointerException.class;


    @Override
    public String getNewMessage( Throwable exToWrap )
    {
        String newMessage = "";
        newMessage += "It appears that the code was trying to call a "
            + "method or refer to a field (member variable) on an object "
            + "through a variable that is null.  Make sure the variable has been initialized in your "
            + "code and that it refers to an object.  Remember, declaring the "
            + "variable is not the same as creating a new object.  If you "
            + "intend to create a new object, you need to use the keyword "
            + "\"new\".";
        return newMessage;
    }


    @Override
    protected Class<? extends Throwable> getExceptionType()
    {
        return CLASS_TYPE;
    }

}
