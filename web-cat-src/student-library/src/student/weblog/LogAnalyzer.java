package student.weblog;

import java.util.Scanner;

/**
 * An interface for a web server log analyzer that processes
 * web log data analyses hourly and weekly access patterns.
 * This interface is based on the web log analyzer from Chapter
 * 4 of <i>Objects First with Java</i>, by
 * David J. Barnes and Michael Kolling.
 *
 * @author Stephen Edwards
 * @version 2003.11.02
 */
public interface LogAnalyzer
{
    /**
     * Add all log entries from a given stream to the current
     * access pattern data.  Multiple logs can be combined
     * by calling this method multiple times.
     * @param inStream the stream to read log entries from
     */
    void accumulateLogData( Scanner inStream );
    /**
     * Add all log entries from a given file to the current
     * access pattern data.  This is a convenience method
     * that provides the same features as
     * {@link #accumulateLogData(Scanner)}, but by taking a file
     * name instead.
     * @param file the file to read log entries from
     */
    void accumulateLogDataFromFile( String file );
    /**
     * Add all log entries from a given URL to the current
     * access pattern data.  This is a convenience method
     * that provides the same features as
     * {@link #accumulateLogData(Scanner)}, but by taking a URL
     * instead.
     * @param url the URL to read log entries from
     */
    void accumulateLogDataFromURL( String url );

    /**
     * Retrieve a count of web accesses for the specified time period.
     * The time period is specified by giving a day of the week
     * using a {@link java.util.Calendar} day constant (e.g.,
     * {@link java.util.Calendar#SUNDAY} ...
     * {@link java.util.Calendar#SATURDAY}), and an hour of the
     * day (0-23).
     * @param day   the weekday for which accesses are reported
     * @param hour  the hour of the given day
     * @return a count of all logged accesses occuring during the
     *         specified hour on the given day of the week
     */
    int accessCountsForDayHour( int day, int hour );
    /**
     * Retrieve a count of web accesses for the specified day of the week.
     * The day of the week is specified by
     * using a {@link java.util.Calendar} day constant (e.g.,
     * {@link java.util.Calendar#SUNDAY} ...
     * {@link java.util.Calendar#SATURDAY}).
     * @param day   the weekday for which accesses are reported
     * @return a count of all logged accesses occuring on the
     *         given day of the week
     */
    int accessCountsForDay( int day );
    /**
     * Retrieve a count of web accesses during the specified hour of the
     * day.
     * @param hour  the hour to report on (0-23)
     * @return a count of all logged accesses occuring during the
     *         specified hour, over all days that have been logged
     */
    int accessCountsForHour( int hour );
}
