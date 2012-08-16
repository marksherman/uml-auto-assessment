package student.weblog;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Store the data from a single line of a web-server log file.
 * Individual fields are made available via accessors.
 *
 * @author Stephen Edwards
 * @version 2003.10.31
 */
public class LogEntry
{
    // Where the data values extracted from a single
    // log line are stored.
    private String   image;
    private Calendar accessTime;
    private String   request;
    private int      resultCode;
    private String   referrer;
    private String   browser;

    private static DateFormat formatter =
        new SimpleDateFormat( "dd/MMM/yyyy:HH:mm:ss" );

    /**
     * Decompose a log line so that the individual fields
     * are available.
     * @param logLine A single line from the log.
     */
    public LogEntry( String logLine )
    {
        image = logLine;
        int leftBracket  = logLine.indexOf( '[' );
        int rightBracket = logLine.indexOf( ']' );
        if ( leftBracket >= 0 && rightBracket >= 0 )
        {
            String dateStr =
                logLine.substring( leftBracket + 1, rightBracket );
            String rest = logLine.substring( rightBracket + 1 );

            try
            {
                Date date = formatter.parse( dateStr );
                accessTime = Calendar.getInstance();
                accessTime.setTime( date );
            }
            catch ( Exception e )
            {
                System.err.println( "error parsing date: " + dateStr );
            }
            try
            {
                StreamTokenizer tokenizer = new StreamTokenizer(
                    new BufferedReader( new StringReader( rest ) ) );
                int token = tokenizer.nextToken();
                /*
                int counter = 1;
                while ( token != StreamTokenizer.TT_EOF )
                {
                    System.out.println( "" + counter + " = " +
                        ( ( token == StreamTokenizer.TT_NUMBER ) ?
                            Double.toString( tokenizer.nval ) : tokenizer.sval ) );
                    counter++;
                    token = tokenizer.nextToken();
                }
                throw new RuntimeException();
                */

                if ( token != StreamTokenizer.TT_NUMBER )
                {
                    // System.out.println( "request = " + tokenizer.sval );
                    request = tokenizer.sval;
                }
                token = tokenizer.nextToken();
                if ( token == StreamTokenizer.TT_NUMBER )
                {
                    // System.out.println( "code = " + tokenizer.nval );
                    resultCode = (int)tokenizer.nval;
                }
                token = tokenizer.nextToken();
                if ( token == StreamTokenizer.TT_NUMBER )
                {
                    // What is this field for?
                }
                token = tokenizer.nextToken();
                if ( token != StreamTokenizer.TT_NUMBER )
                {
                    // System.out.println( "referrer = " + tokenizer.sval );
                    referrer = tokenizer.sval;
                }
                token = tokenizer.nextToken();
                if ( token != StreamTokenizer.TT_NUMBER )
                {
                    // System.out.println( "browser = " + tokenizer.sval );
                    browser = tokenizer.sval;
                }
                // throw new RuntimeException();
            }
            catch ( Exception e )
            {
                e.printStackTrace();
                // throw new RuntimeException();
            }
        }
    }

    /**
     * Get the access time for the HTTP request contained in this log line.
     * @return The access time as a Calendar object
     */
    public Calendar accessTime()
    {
        return accessTime;
    }

    /**
     * Get the request method, URL, and protocol for this log line.
     * @return A string containing the request method (e.g., GET), the
     *         URL, and the protocol, separated by spaces
     */
    public String request()
    {
        return request;
    }

    /**
     * Get the result code returned by the server.  Among HTTP
     * result codes, 200 is the "normal" reply, while 403 indicates
     * a permission failure and 404 indicates a URL was not found.
     * @return the result code
     */
    public int resultCode()
    {
        return resultCode;
    }

    /**
     * Get the URL of the "referrer" containing the link used to make the request.
     * @return the referring URL as a string
     */
    public String referrer()
    {
        return referrer;
    }

    /**
     * Get the identification information for the browser making the request.
     * @return the browser identification information as a string
     */
    public String browser()
    {
        return browser;
    }

    /**
     * Create a string representation of the data.
     * This is not necessarily identical with the
     * text of the original log line.
     *
     * @return A string representing the data of this entry.
     */
    public String toString()
    {
        return image;
    }

}
