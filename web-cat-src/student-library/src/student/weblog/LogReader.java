package student.weblog;

import java.io.*;

/**
 * A class to read information from a file of web server accesses.
 * It currently supports log files from the Virginia Tech CS department's
 * server proxy, which are in Apache's log format.
 *
 * @author Stephen Edwards
 * @version 2003.10.31
 */
public class LogReader
{
    private BufferedReader in;
    private String         nextLine;

    /**
     * Create a LogfileReader to supply data from the specified stream.
     * @param inStream the stream to read from
     */
    public LogReader( BufferedReader inStream )
    {
        in = inStream;
    }

    /**
     * Does the reader have more data to supply?
     * @return true if there is more data available,
     *         false otherwise.
     */
    public boolean hasMoreEntries()
    {
        if ( nextLine == null )
        {
            try
            {
                nextLine = in.readLine();
            }
            catch ( Exception e )
            {
                // nothing to do
            }
        }
        return nextLine != null;
    }

    /**
     * Analyze the next line from the log file and
     * make it available via a LogEntry object.
     *
     * @return A LogEntry containing the data from the
     *         next log line, or null if there are no more lines.
     */
    public LogEntry nextEntry()
    {
        LogEntry result = null;
        if ( hasMoreEntries() )
        {
            result = new LogEntry( nextLine );
            nextLine = null;
        }
        return result;
    }
}
