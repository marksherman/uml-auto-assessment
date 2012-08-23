package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.AbstractHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class InputMismatchExceptionHandler extends AbstractHandler
                implements
                ExceptionHandlerInterface
{
    private static final Class<InputMismatchException> CLASS_TYPE = InputMismatchException.class;


    @Override
    protected Class<? extends Throwable> getExceptionType()
    {
        return CLASS_TYPE;
    }


    @Override
    public String getNewMessage( Throwable exToWrap )
    {
        // look at the method being called to see what the user was supposed to
        // enter
        String line = findLine( exToWrap );
        String newMessage = "The code was expecting the user to enter a "
            + "particular type of data but the user entered something "
            + "of the wrong type.";
        if ( line != null )
        {
            if ( line.contains( "nextInt" ) || line.contains( "nextByte" )
                || line.contains( "nextShort" ) || line.contains( "nextLong" )
                || line.contains( "nextBigInteger" ) )
            {
                newMessage = "The code may have been expecting the user to enter "
                    + "an integer.  However, the user entered something that was "
                    + "not an integer.  Make sure you do not enter a String or a "
                    + "floating-point number.  If floating-point values are okay, "
                    + "use a method like nextFloat instead.";
            }
            else if ( line.contains( "nextFloat" )
                || line.contains( "nextDouble" )
                || line.contains( "nextBigDecimal" ) )
            {
                newMessage = "The code may have been expecting the user to "
                    + "enter a floating-point number.  However, the user entered "
                    + "something that was not a floating-point number.  Make "
                    + "sure you do not enter a String.";
            }
            else if ( line.contains( "nextBoolean" ) )
            {
                newMessage = "The code may have been expecting the user to "
                    + "enter a boolean.  However, the user entered something "
                    + "that was not a boolean.  The only valid inputs are true, "
                    + "TRUE, false, or FALSE.";
            }
        }
        return newMessage;
    }
}
