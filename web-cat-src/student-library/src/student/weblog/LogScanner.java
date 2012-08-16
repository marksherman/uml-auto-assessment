package student.weblog;

import java.io.File;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * A class to scan information from a web server access log.
 * It currently supports log files from the Virginia Tech CS department's
 * server proxy, which are in Apache's log format.
 *
 * @author Dwight Barnette (based on Stephen Edwards' {@link LogReader} class)
 * @version 2003.10.31
 */
public class LogScanner
    implements Iterator<LogEntry>,
        Iterable<LogEntry>
{
    private Scanner in;

    /**
     * Create a LogScanner that reads access log data from the given
     * file.  This constructor is provided for convenience only--it
     * opens the named file and creates a scanner connected to it.
     * @param file The name of the file to open
     */
    public LogScanner( String file )
    {
        this( new File( file ) );
    }


    /**
     * Create a LogScanner that reads access log data from the given
     * file.  This constructor is provided for convenience only--it
     * simply creates a scanner connected to the file.
     * @param file The file to open
     */
    public LogScanner( File file )
    {
        try
        {
            in = new Scanner( file );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    /**
     * Create a LogScanner that reads access log data from the given
     * stream.
     * @param inStream The stream to read from
     */
    public LogScanner( Scanner inStream )
    {
        in = inStream;
    }


    /**
     * Does the scanner have more data to supply?
     * @return true if there is more data available,
     *         false otherwise.
     */
    public boolean hasNext()
    {
        return in != null && in.hasNextLine();
    }


    /**
     * Analyze the next line from the log file and
     * make it available via a LogEntry object.
     *
     * @return A LogEntry containing the data from the
     *         next log line
     * @throws NoSuchElementException if there are no more log entries
     * (call {@link #hasNext()} first if you want to avoid the exception)
     */
    public LogEntry next()
    {
        if ( in == null )
        {
            throw new NoSuchElementException();
        }
        return new LogEntry( in.nextLine() );
    }


    /**
     * Provided for compliance with the {@link Iterator} interface,
     * but this method is not supported.
     *
     * @throws UnsupportedOperationException if you call it, since
     * this operation is not supported on LogScanners
     */
    public void remove()
    {
        throw new UnsupportedOperationException();
    }


    /**
     * Return this object, unchanged, to provide support for foreach-style
     * loops.  Note that this object's log entries can only be iterated
     * over once, since the collection is directly bound to the source
     * lines in the {@link Scanner} (possibly attached to a file) that
     * was used to create this LogScanner.
     *
     * @return this object, unchanged (since it already implements
     * Iterator<LogEntry>)
     */
    public Iterator<LogEntry> iterator()
    {
        return this;
    }


    /**
     * A synonym for {@link #hasNext()} provided for backward
     * compatibility with the older Java 1.4-style {@link LogReader}
     * class.
     * @return true if there is more data available,
     *         false otherwise.
     */
    public boolean hasMoreEntries()
    {
        return hasNext();
    }


    /**
     * A synonym for {@link #hasNext()} provided for backward
     * compatibility with the older Java 1.4-style {@link LogReader}
     * class.  This one silently converts NoSuchElementExceptions
     * into null return values for backward compatibility.
     *
     * @return A LogEntry containing the data from the
     *         next log line, or null if there are no more lines.
     */
    public LogEntry nextEntry()
    {
        LogEntry result = null;
        try
        {
            result = next();
        }
        catch ( NoSuchElementException e )
        {
            // ignore this error and silently return null
        }
        return result;
    }

}
