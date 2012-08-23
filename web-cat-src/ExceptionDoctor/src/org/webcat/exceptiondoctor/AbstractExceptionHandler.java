package org.webcat.exceptiondoctor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import org.webcat.exceptiondoctor.runtime.Debugger;


/**
 * This is an Abstract handler class that all Handlers extend. It includes many
 * utility functions and standardize the exception messages.
 *
 * @author mike
 *
 */
public abstract class AbstractExceptionHandler
                implements
                ExceptionHandlerInterface
{
    /** The name of the exception being handled. */
    protected final String exceptionName;


    /**
     * This sets the exception name for the exception handler.
     *
     * @param myExceptionName The exception name.
     */
    public AbstractExceptionHandler(String myExceptionName)
    {
        exceptionName = myExceptionName;

    }


    /**
     * A general method to wrap exceptions. This should never be called.
     */
    public Throwable wrapException(Throwable exToWrap)
    {
        return null;
    }


    /**
     * Gets the stack trace element out of an exception
     *
     * @param t The exception to get the stack trace element out of.
     * @return a stack trace element that is not part of the JAVA API
     */
    protected StackTraceElement getTopMostStackTraceElement(Throwable t)
    {
        // need to find the topmost StackTraceElement that is *NOT* part of the
        // Java API
        StackTraceElement[] elements = t.getStackTrace();
        if ( elements.length == 0 )
        {
            return null;
        }
        // this is the index
        int i = 0;
        // start with the first one
        StackTraceElement e = elements[0];
        // keep looking for one that DOESN'T start with "java"
        while ( checkExclusions( e ) )
        {
            i++;
            if ( i == elements.length )
            {
                return null;
            }
            e = elements[i];
        }

        return e;
    }


    private boolean checkExclusions( StackTraceElement e )
    {
        try
        {

            InputStream input =
                getClass().getResourceAsStream( "/exclude.conf" );
            BufferedReader reader =
                new BufferedReader( new InputStreamReader( input ) );
            String exclusion = reader.readLine();
            while ( exclusion != null )
            {
                String className = e.getClassName();
                if ( className.startsWith( exclusion ) )
                {
                    return true;
                }
                exclusion = reader.readLine();
            }
        }
        catch ( IOException e1 )
        {
            System.err.println( "Could not open exclusion list" );
            e1.printStackTrace();
            return true;
        }
        return false;
    }


    /**
     * Get the offending line out of an exception
     *
     * @param exToWrap  The exception that you are looking for the offending
     *                  line from.
     * @param ste       The stack trace element indicating the line to find.
     * @return the offending line of source code.
     */
    protected String getLine( Throwable exToWrap, StackTraceElement ste )
    {
        return findLine( exToWrap, ste );
    }


    /**
     * Get the offending line out of an exception
     *
     * @param exToWrap  The exception that you are looking for the offending
     *                  line from.
     * @return the offending line of source code.
     */
    protected String getLine( Throwable exToWrap )
    {
        StackTraceElement ste = getTopMostStackTraceElement( exToWrap );
        return findLine( exToWrap, ste );
    }


    private String findLine( Throwable exToWrap, StackTraceElement ste )
    {
        List<String> lines = new ArrayList<String>();
        if ( ste == null )
        {
            // throw new FileNotFoundException();
            return "";
        }
        String line = "";
        BufferedReader scan = null;
        try
        {
            scan = getReader( exToWrap, ste );
        }
        catch ( FileNotFoundException e )
        {
            return "";
        }
        catch ( SourceCodeHiddenException e )
        {
            return "";
        }

        int num = ste.getLineNumber();
        if ( num < 0 )
        {
            Debugger.println( "Unknown Sourceline" );
            return "";
            // throw new LineNotFoundException();
        }

        // loop through and count how many lines have been read
        try
        {
            line = scan.readLine();
            while ( line != null )
            {
                lines.add( line );
                line = scan.readLine();
            }
        }
        catch ( IOException e )
        {
            return null;
        }
        finally
        {
            try
            {
                scan.close();
            }
            catch ( IOException e )
            {
                //meh if it cant close, it is ok.
            }
        }

        if(lines.size() == 0 )
            return null;
        line = lines.get( num-1 );
//        int endOfLine = line.trim().lastIndexOf( ';' );
//        String partial = line.substring( 0, endOfLine );
//        int beginOfLine = partial.lastIndexOf( ";" );
//        int begOfLineBracket = line.trim().lastIndexOf( '{' );
//        if ( beginOfLine < begOfLineBracket )
//        {
//            beginOfLine = begOfLineBracket;
//        }
//        line = line.substring( beginOfLine + 1, endOfLine + 1 ).trim();
//        line = line.replaceAll( " ", "" );
//        line = line.replaceAll( "\t", "" );
//        line = line.replaceAll( "\n", "" );
        return line;
    }


    /**
     * This gets a scanner for the source code that caused the exception to
     * happen
     *
     * @param exToWrap
     *            the exception to wrap
     * @param oldStackTraceElement
     *            the stack trace element that has been found to be the first
     *            non java api class
     * @return a scanner for the source code that caused the exception.
     * @throws FileNotFoundException
     */
    private BufferedReader getReader(
        Throwable exToWrap,
        StackTraceElement oldStackTraceElement )
        throws SourceCodeHiddenException,
        FileNotFoundException
    {
        BufferedReader scan;

        // open the file
        String fileName = oldStackTraceElement.getClassName();
        scan = openFile( fileName );
        Debugger.println( "Source file being opened -- " + fileName );
        if ( scan == null )
        {
            StackTraceElement stackTraceSourceCode = getSourceExists( exToWrap );
            if ( stackTraceSourceCode != null )
            {
                throw new SourceCodeHiddenException( stackTraceSourceCode,
                    exToWrap );
            }
            else
            {
                throw new FileNotFoundException();
            }
        }
        return scan;
    }


    /**
     * opens a file with the full class name.
     *
     * @param packageName
     *            fully qualified package and class name
     * @return a scanner for the file.
     */
    private BufferedReader openFile( String packageName )
    {
        BufferedReader scan;
        packageName = cleanFilename( packageName );
        try
        {
            InputStream in = getClass().getClassLoader()
                .getResourceAsStream( packageName );

            if ( in == null )
            {
                in = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream( packageName );
            }
            if ( in == null )
            {
                File existTester = new File( "src/" + packageName );
                FileReader reader = null;
                if ( existTester.exists() )
                {
                    reader = new FileReader( existTester );
                }
                else
                {
                    existTester = new File( packageName );
                    if ( existTester.exists() )
                    {
                        reader = new FileReader( existTester );
                    }
                    else
                    {
                        throw new FileNotFoundException();
                    }
                }
                scan = new BufferedReader( reader );
            }
            else
            {
                scan = new BufferedReader( new InputStreamReader( in ) );
            }
        }
        catch ( FileNotFoundException ex )
        {
            scan = null;
        }
        return scan;
    }


    private String cleanFilename( String packageName )
    {
        packageName = packageName.replace( '.', '/' ) + ".java";
        return packageName;
    }


    /**
     * Get all of the variables used in a line of source code.
     *
     * @param line
     *            the line to be searched for variables
     * @param end
     *            a character or set of characters that should be used to help
     *            find the variables.
     * @return
     */
    /*
     * public List<String> getVariables(String line, String end) { // eliminate
     * any comments first line = stripComments(line); List<String> variables =
     * new ArrayList<String>(); variables.addAll(getAllArguments(line, end));
     * line = ripOutArguments(line);
     *
     * // tokenize it based on the character you're looking for //
     * StringTokenizer tok = new StringTokenizer(line, String.valueOf(line //
     * .charAt(end))); StringTokenizer tok = new StringTokenizer(line, end);
     *
     * // create the array list of Strings to return int numTokens =
     * tok.countTokens() - 1; List<String> classesAndPackages = new
     * ArrayList<String>();
     *
     * // now look for the last part of each token (except for the last) - //
     * that's your variable for (int j = 0; j < numTokens; j++) { // get the
     * next par String part = tok.nextToken(); String thisVariable = null; //
     * remember whether we've found any variable name yet boolean found = false;
     * // now loop *backwards* and look for something that indicates the //
     * start of the variable name for (int i = part.length() - 1; i >= 0; i--) {
     * // keep in mind that there may be some blank spaces between the // [ and
     * the variable name
     *
     * // If we're looking at the end of an arg list, then jump // over it in
     * reverse if (part.charAt(i) == ')') { int left = part.lastIndexOf('(', i);
     * if (left > 0) { i = left - 1; } }
     *
     * if (!isStart(part.charAt(i))) found = true; // if we find the starting
     * character, save it and break if (found && isStart(part.charAt(i))) {
     * thisVariable = part.substring(i + 1, part.length()); break; } } if (found
     * && thisVariable == null) { // The variable is the whole "part"
     * thisVariable = part;
     *
     * // If it is a dotted name, reconstruct it if (j > 0 && end.equals(".") &&
     * variables.size() > 0) { thisVariable = variables.get(variables.size() -
     * 1) + end + thisVariable; } } if (thisVariable != null) { // We should
     * really ignore all class names, but without // parsing the import list for
     * the class, it isn't possible // to determine whether a name is a class
     * name or not. try { // The best we can do is test to see if it is a fully
     * // qualified class name //Class<?> c = Class.forName(thisVariable);
     * addClassAndPackages(thisVariable, classesAndPackages); } catch (Exception
     * e) { try { // OK, we can also check for java.lang classes Class<?> c =
     * Class.forName("java.lang." + thisVariable);
     * addClassAndPackages(thisVariable, classesAndPackages); } catch (Exception
     * e2) { // Ignore any errors, since they mean this isn't // a
     * fully-qualified or java.lang class name } } } if (thisVariable != null &&
     * !variables.contains(thisVariable)) { variables.add(thisVariable); } } for
     * (String className : classesAndPackages) { variables.remove(className); }
     * return variables; }
     */

    /*
     * private String ripOutArguments(String line) { String newLine = ""; while
     * (line.indexOf('(') >= 0) { int left = line.indexOf('('); int right = left
     * + 1 + getMatchingEndArea(line.substring(left + 1), '(', ')'); newLine +=
     * line.substring(0, left + 1); newLine += line.substring(right); line =
     * line.substring(right); } return newLine; }
     *
     * private int getMatchingEndArea(String line, char start, char end) { int
     * level = 0; int i = 0; for (; i < line.length(); i++) { if (line.charAt(i)
     * == start) level++; if (line.charAt(i) == end && level == 0) { return i; }
     * else if (line.charAt(i) == end) { level--; }
     *
     * } return -1; }
     *
     * protected List<String> getAllArguments(String line, String end) {
     * List<String> vars = new ArrayList<String>(); // eliminate any comments
     * first while(line.indexOf(end) >0) { vars.add(line.substring(0,
     * line.indexOf(end))); line = line.substring(line.indexOf(end)); } while
     * (line.indexOf('(') >= 0) { line = stripComments(line); int left =
     * line.indexOf('('); int right = left + 1 +
     * getMatchingEndArea(line.substring(left + 1), '(', ')'); if (left < 0 ||
     * right < 0) { return new ArrayList<String>(); } getArgs0(vars,
     * line.substring(left + 1, right), end); line = line.substring(right); }
     * return vars;
     *
     * }
     *
     * private void getArgs0(List<String> vars, String innerArgs, String end) {
     * String line = innerArgs; innerArgs = innerArgs.trim(); // int left =
     * innerArgs.indexOf('('); // int right = getMatchingEndParen(innerArgs); //
     * if (left >= 0) // getArgs0(vars, innerArgs.substring(left, right)); if
     * (innerArgs.length() == 0) { return; } int eov = innerArgs.length(); int
     * i; for (i = innerArgs.length() - 1; i >= 0; i--) { if
     * (innerArgs.charAt(i) == ',') { String variableName =
     * innerArgs.substring(i + 1, eov);
     * vars.addAll(getVariables(variableName.trim(), end));
     * vars.add(variableName.trim()); eov = i; } }
     * vars.addAll(getVariables(innerArgs.substring(0, eov), end));
     * vars.add(innerArgs.substring(0, eov)); }
     */

    /*
     * private boolean isStart(char c) { return (c == ' ' || c == '.' || c ==
     * '\t' || c == '('); }
     */

    /**
     * returns a string that will be included in the exception error message.
     * This string says the type of exception it is.
     *
     * @return a string saying the type of exception
     */
    protected String getErrorType()
    {
        String article = "a ";
        switch ( Character.toLowerCase( exceptionName.charAt( 0 ) ) )
        {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
                article = "an ";
        }
        return "This error is called " + article + exceptionName + ".";
    }


    /**
     * This creates a new exception with a properly formatted exception
     * message.
     *
     * @param exToWrap The exception to be re-written and re-wrapped.
     * @param newMessage The message to be used in the new exception.
     * @param exceptionType The class that the new exception with be created
     *                      from.
     * @return A new exception with a rewritten message and properly wrapped
     *         exception.
     */
    protected Throwable buildNewException(
        Throwable exToWrap,
        String newMessage,
        Class<?> exceptionType )
    {
        StackTraceElement ste = getTopMostStackTraceElement( exToWrap );
        if ( exToWrap == null )
        {
            return null;
        }
        String sourceline = getSourceLine( exToWrap, ste );
        newMessage = formatMessage( newMessage, sourceline, 70 );

        Throwable newException = rewireException( exToWrap,
            newMessage,
            exceptionType,
            ste );
        return newException;

    }


    /**
     * Create a new exception with a new message and the stack grace from
     * an existing exception.
     *
     * @param exToWrap The existing exception to pull information from.
     * @param newMessage The message for the new exception.
     * @param exceptionType The type of the new exception.
     * @param ste Currently unused (historical artifact).
     * @return The newly created exception, with the stack trace of the
     * original.
     */
    public Throwable rewireException(
        Throwable exToWrap,
        String newMessage,
        Class<?> exceptionType,
        StackTraceElement ste )
    {
        Throwable newException = constructNewException( newMessage,
            exceptionType );
        if ( newException == null )
        {
            return null;
        }
        // StackTraceElement[] elements = { ste };
        // newException.setStackTrace(elements);
        //
        // newException.initCause(exToWrap);
        newException.setStackTrace( exToWrap.getStackTrace() );
        return newException;
    }


    private Throwable constructNewException(
        String newMessage,
        Class<?> exceptionType )
    {
        Class<?>[] args = { String.class };
        Constructor<?> classConstructor;
        Throwable newException;

        try
        {
            classConstructor = exceptionType.getConstructor( args );
            newException = (Throwable)classConstructor.newInstance( newMessage );
        }
        catch ( Throwable e )
        {
            return null;
        }
        return newException;
    }


    /**
     * This method sees if source code exists in the stack trace.
     *
     * @param exception The exception to search the stack trace of.
     * @return a boolean representing the result.
     */
    private StackTraceElement getSourceExists( Throwable exception )
    {
        StackTraceElement[] stack = exception.getStackTrace();

        BufferedReader result = null;
        int i;
        for ( i = 0; ( i < stack.length && result == null ); i++ )
        {
            String fileName = stack[i].getClassName();
            result = openFile( fileName );
        }
        if ( result != null )
        {
            return stack[i - 1];
        }
        return null;
    }


    /**
     * Creates a string to add to the exception message containing the
     * violating line of code.
     *
     * @param ex The exception that is being rewritten.
     * @param ste The stack trace element specifying the location to use.
     * @return A string with the violating source code in it.
     */
    public String getSourceLine( Throwable ex, StackTraceElement ste )
    {
        String line = getLine( ex, ste ).trim();
        line = stripComments( line );
        String source = "In file " + ste.getFileName();
        if ( ste.getLineNumber() > 0 )
        {
            source += " on line " + ste.getLineNumber();
            if ( line.length() > 0 )
            {
                source += ", which reads:\n\n    " + line + "\n";
            }
            else
            {
                source += ".";
            }
        }
        else
        {
            source += ".";
        }
        return source;
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
     * This word-wraps an exception message.
     *
     * @param newMessage The new message to be wrapped in the new exception.
     * @param sourceLine The line of source code for the message.
     * @param charCount Number of characters to format to.
     * @return A string that is properly wrapped.
     */
    public String formatMessage(
        String newMessage,
        String sourceLine,
        int charCount )
    {
        String formattedMessage = wrap( newMessage, charCount, "    " );
        if ( sourceLine != null && sourceLine.length() > 0 )
        {
            formattedMessage = "\n    " + sourceLine + formattedMessage;
        }
        formattedMessage += "\n\n    " + getErrorType();
        formattedMessage += "\n";
        return formattedMessage;
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

    /*
     * private static void addClassAndPackages(String className, List<String>
     * toList) { int pos = className.lastIndexOf('.'); while (pos > 0) {
     * toList.add(className); className = className.substring(0, pos); pos =
     * className.lastIndexOf('.'); } toList.add(className); }
     */

    /*
     * protected List<String> getArrayVariables(String line) { List<String> vars
     * = new ArrayList<String>(); while (line.indexOf('[') >= 0) { int left =
     * line.indexOf('['); int right = left + 1 +
     * getMatchingEndArea(line.substring(left + 1), '[', ']');
     * vars.add(line.substring(left + 1, right)); line = line.substring(right);
     * } return vars; }
     *
     * public List<String> getArrayNames(String line) { List<String> vars = new
     * ArrayList<String>(); while (line.indexOf('[') >= 0) { int left =
     * line.indexOf('['); int right = left + 1 +
     * getMatchingEndArea(line.substring(left + 1), '[', ']'); for (int i =
     * left; i >= 0; i--) { if (isStart(line.charAt(i))) { String varName =
     * line.substring(i, left).trim(); if (!vars.contains(varName))
     * vars.add(varName); line = line.substring(right); break; } } } return
     * vars; }
     */
}
