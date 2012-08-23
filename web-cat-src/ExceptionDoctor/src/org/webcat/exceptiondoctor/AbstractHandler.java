package org.webcat.exceptiondoctor;

import java.io.*;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Stack;


public abstract class AbstractHandler
{
    protected abstract Class<? extends Throwable> getExceptionType();


    protected abstract String getNewMessage( Throwable oldException );


    protected String getExceptionName()
    {
        return getExceptionType().getName();
    }


    private File getSourceFile( StackTraceElement culprit )
    {
        File culpritFile = null;
        String fileName = culprit.getClassName().replace( '.', '/' ) + ".java";
        URL fileURL = Thread.currentThread()
            .getContextClassLoader()
            .getResource( fileName );

        if ( fileURL != null )
        {
            culpritFile = new File( fileURL.getFile() );
            if ( culpritFile.exists() )
                return culpritFile;
        }
        fileURL = getClass().getClassLoader().getResource( fileName );
        if ( fileURL != null )
        {
            culpritFile = new File( fileURL.getFile() );
            if ( culpritFile.exists() )
                return culpritFile;
        }
        culpritFile = new File( culprit.getFileName() );
        if ( culpritFile.exists() )
            return culpritFile;
        culpritFile = new File( culprit.getClassName().replace( '.', '/' )
            + ".java" );
        if ( culpritFile.exists() )
            return culpritFile;
        culpritFile = new File( new File( "src" ), culpritFile.getPath() );
        if ( culpritFile.exists() )
            return culpritFile;
        return null;
    }


    public Throwable wrapException( Throwable oldException )
    {
        String newMessage = "";
        if ( getExceptionName() != null && getExceptionName().length() != 0 )
            newMessage += "An exception was thrown.\n";
        if ( oldException.getMessage() != null
            && oldException.getMessage().length() != 0 )
            newMessage += "Original Message:\n\t" + oldException.getMessage()
                + "\n";
        // StackTraceElement culprit = findCulprit( oldException );
        StackTraceElement[] newTrace = getCleanedTrace( oldException );
        int elementNumber = findCulprit( newTrace );
        StackTraceElement culprit = newTrace[elementNumber];
        File source = getSourceFile( culprit );
        if ( source != null )
        {
            newMessage += source.getName() + ":";
            if ( culprit.getLineNumber() > 0 )
            {
                newMessage += culprit.getLineNumber() + ":\n\t";
                try
                {
                    String sourceLine = findLine( source,
                        culprit.getLineNumber() );
                    if ( sourceLine != null )
                        newMessage += "\t" + stripComments( sourceLine.trim() )
                            + "\n";
                }
                catch ( IOException e )
                {
                    // Just dont add source line
                }
            }
            else
            {
                newMessage += "\n\tSource code unavailable";
            }
            newMessage += "\n";
        }
        if ( getNewMessage( oldException ) != null )
        {
            newMessage += "Helpful Hints:\n";
            if ( culprit.equals( newTrace[0] ) )
            {
                newMessage += wrap( getNewMessage( oldException ), 80, "\t" )
                    + "\n";
            }
            else
            {
                String libraryMessage = "";
                boolean direct = false;
                StackTraceElement[] oldTrace = oldException.getStackTrace();
                if ( oldTrace.length >= 2 )
                {
                    if ( oldTrace[1].equals( culprit ) )
                        direct = true;
                    libraryMessage += oldTrace[0].getClassName()
                        + " appears to" + " be a library class that is used ";
                    if ( direct )
                        libraryMessage += "directly";
                    else
                        libraryMessage += "indirectly";
                    libraryMessage += " by your code.  "
                        + "This situation arose as a result of a method call";
                    if ( culprit.getLineNumber() >= 0
                        && culprit.getFileName() != null )
                    {
                        libraryMessage += " on line " + culprit.getLineNumber()
                            + " of " + culprit.getFileName() + ".";
                    }
                    else
                    {
                        libraryMessage += ".";
                    }
                    libraryMessage += "  Make sure you are providing "
                        + "appropriate parameters in your method call,"
                        + " and that the relevant object(s) are in a state "
                        + "where ";
                    StackTraceElement called = oldTrace[elementNumber - 1];
                    if ( called.getMethodName() != null )
                        libraryMessage += called.getMethodName();
                    else
                        libraryMessage += "the method";
                    libraryMessage += " is appropriate to call.";

                }
                newMessage += wrap( libraryMessage, 80, "\t" );
            }
        }
        newMessage += "\nStack Trace:\n";
        return createNewException( oldException, newMessage, newTrace );

    }


    private int findCulprit( StackTraceElement[] newTrace )
    {

        for ( int i = 0; i < newTrace.length; i++ )
        {
            if ( isStudent( newTrace[i] ) )
                return i;
        }
        return 0;
    }


    private Throwable createNewException(
        Throwable oldException,
        String newMessage,
        StackTraceElement[] newTrace )
    {
        Class<? extends Throwable> eType = getExceptionType();
        Throwable newException;

        try
        {
            Constructor<? extends Throwable> messageConstructor = eType.getConstructor( String.class );
            newException = messageConstructor.newInstance( newMessage );
        }
        catch ( Exception e )
        {
            newException = new Throwable( newMessage );
        }
        newException.initCause( oldException );
        newException.setStackTrace( newTrace );
        return newException;
    }


    private StackTraceElement[] getCleanedTrace( Throwable oldException )
    {
        // List<StackTraceElement> cleanedElements = Arrays.asList(
        // oldException.getStackTrace() );
        StackTraceElement[] oldTrace = oldException.getStackTrace();
        Stack<StackTraceElement> cleanedElements = new Stack<StackTraceElement>();
        // Clean Bottom of the stack (Remove infrastructure)
        boolean inStudentCode = false;
        for ( int i = 0; i < oldTrace.length; i++ )
        {
            StackTraceElement currentElement = oldTrace[i];
            // String packageName = getPackageName( currentElement );
            if ( !inStudentCode )
            {
                inStudentCode = isStudent( currentElement );
            }
            else
            {
                if ( !isStudent( currentElement ) )
                    break;
            }
            cleanedElements.push( currentElement );

        }
        return cleanedElements.toArray( new StackTraceElement[cleanedElements.size()] );
    }


    private String getPackageName( StackTraceElement currentElement )
    {
        String packageName = currentElement.getClassName();
        int lastDot = packageName.lastIndexOf( "." );
        if ( lastDot <= 0 )
        {
            packageName = ".";
        }
        else
        {
            packageName = packageName.substring( 0, lastDot );
        }
        return packageName;
    }


    private boolean isStudent( StackTraceElement element )
    {
        File potentialSource = getSourceFile( element );
        if ( potentialSource != null && potentialSource.exists() )
            return true;
        return false;
    }


    protected String findLine( Throwable exToWrap )
    {
        StackTraceElement[] newTrace = getCleanedTrace( exToWrap );
        int elementNumber = findCulprit( newTrace );
        StackTraceElement culprit = newTrace[elementNumber];
        File source = getSourceFile( culprit );
        if ( culprit.getLineNumber() < 0 || !source.exists() )
            return null;
        try
        {
            return findLine( source, culprit.getLineNumber() );
        }
        catch ( IOException e )
        {
            return null;
        }
    }


    protected String findLine( File source, int lineNumber ) throws IOException
    {
        BufferedReader fread = new BufferedReader( new FileReader( source ) );
        String line = null;
        String read = fread.readLine();
        for ( int i = 1; read != null; i++ )
        {
            if ( i == lineNumber )
            {
                line = read;
                break;
            }
            read = fread.readLine();
        }

        return line;
    }


    private static String wrap( String message, int width, String prefix )
    {
        StringBuffer buf = new StringBuffer( message.length()
            + ( prefix.length() + 1 ) * ( 1 + message.length() / width ) );

        int len = message.length();
        int pos = 0;

        while ( len - pos > width )
        {
            int split = message.lastIndexOf( ' ', pos + width );
            if ( split < pos )
            {
                // can't find space earlier on line, so this must be a
                // word longer than the specified width--it can't be split
                split = message.indexOf( ' ', pos + width );
            }

            // If there are no more spaces to split on ...
            if ( split < 0 )
                break;

            int newpos = split + 1;
            // search backwards from split to skip over preceding blanks
            while ( split > 0 && message.charAt( split - 1 ) == ' ' )
            {
                split--;
            }

            // search forwards to skip over trailing blanks
            while ( newpos < len && message.charAt( newpos ) == ' ' )
            {
                newpos++;
            }
            buf.append( "\n" );
            buf.append( prefix );
            buf.append( message.substring( pos, split ) );
            pos = newpos;
        }

        if ( len > pos )
        {
            buf.append( "\n" );
            buf.append( prefix );
            buf.append( message.substring( pos ) );
        }

        return buf.toString();
    }


    /**
     * Removes any Java-style comments from the line, as well as trimming
     * whitespace.
     * 
     * @param line
     *            The line to strip
     * @return The line without any comments and leading/trailing space.
     */
    public static String stripComments( String line )
    {
        return line.trim()
            .replaceAll( "/\\*(.)*?\\*/", "" )
            .replaceFirst( "^.*\\*/", "" )
            .replaceFirst( "//.*$", "" )
            .trim();
    }

}
