package org.webcat.exceptiondoctor.handlers;

import java.io.FileNotFoundException;
import java.util.StringTokenizer;
import org.webcat.exceptiondoctor.AbstractExceptionHandler;
import org.webcat.exceptiondoctor.AbstractHandler;
import org.webcat.exceptiondoctor.ExceptionHandlerInterface;
import org.webcat.exceptiondoctor.LineNotFoundException;
import org.webcat.exceptiondoctor.SourceCodeHiddenException;


public class ClassFormatErrorHandler extends AbstractHandler
                implements
                ExceptionHandlerInterface
{
    private static final Class<ClassFormatError> CLASS_TYPE = ClassFormatError.class;


    @Override
    protected Class<? extends Throwable> getExceptionType()
    {
        return CLASS_TYPE;
    }


    @Override
    public String getNewMessage( Throwable exToWrap )
    {
        if ( exToWrap.getMessage() == null )
            return null;
        String newMessage = "It appears that one of the class files has been corrupted.  ";

        // figure out which class... it's probably the last part of the message
        String name = getCorruptedFileName( exToWrap );
        if ( name != null )
        {
            newMessage += "Most likely it is the file " + name + ".class.  ";
        }

        newMessage += "You should probably delete all the .class files and "
            + "then recompile.";
        return newMessage;
    }


    private String getCorruptedFileName( Throwable exToWrap )
    {
        StringTokenizer tok = new StringTokenizer( exToWrap.getMessage() );
        String name = null;
        while ( tok.hasMoreTokens() )
        {
            name = tok.nextToken();
        }
        return name;
    }

}
