/*==========================================================================*\
 |  $Id: IOHelper.java,v 1.3 2011/06/09 15:30:25 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2007-2010 Virginia Tech
 |
 |  This file is part of the Student-Library.
 |
 |  The Student-Library is free software; you can redistribute it and/or
 |  modify it under the terms of the GNU Lesser General Public License as
 |  published by the Free Software Foundation; either version 3 of the
 |  License, or (at your option) any later version.
 |
 |  The Student-Library is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU Lesser General Public License for more details.
 |
 |  You should have received a copy of the GNU Lesser General Public License
 |  along with the Student-Library; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package student;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


// -------------------------------------------------------------------------
/**
 *  This class provides several static methods that streamline the use of
 *  basic I/O operations in Java.
 *
 *  It is designed to work seamlessly within the BlueJ environment, but
 *  is equally applicable in any other IDE or for standaline applications.
 *  It is designed to make it simple to use {@link Scanner} and
 *  {@link PrintWriter} for basic input and string-based output.
 *  <p>
 *  This class is based on Petr Skoda's "Beginner's IOHelper for BlueJ"
 *  (@href{http://www.rdv.vslib.cz/skodak}).
 *  </p>
 *  @author  Stephen Edwards (based on Petr Skoda's original)
 *  @author Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.3 $, $Date: 2011/06/09 15:30:25 $
 */
public class IOHelper
{
    //~ Instance/static variables .............................................

    private static File cwd;    // current working directory


    //~ Initializers ..........................................................

    static
    {
        Object obj = new Object() { /* empty */ };
        if ( obj.getClass().getClassLoader().toString().startsWith( "bluej" ) )
        {
            String className     = obj.getClass().getName();
            String classFileName = className.substring(
                                       className.lastIndexOf( '.' ) + 1 )
                                   + ".class";
            File   classFile     = new File( obj.getClass().getResource(
                                                 classFileName ).getFile() );
            File   projectDir    = classFile.getParentFile();
            int    i             = className.indexOf( '.', 0 );
            while ( i >= 0 )
            {
                projectDir = projectDir.getParentFile();
                i          = className.indexOf( '.', i + 1 );
            }
            cwd = projectDir;
        }
        else if (System.getProperty("catalina.home") != null)
        {
            cwd = new File( System.getProperty("catalina.home"),
                "/webapps/"
                + System.getProperty("IOHelper.servlet.name", "zk" ) );
        }
        else
        {
            cwd = new File( System.getProperty( "user.dir" ) );
        }
    }


    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new IOHelper object.
     */
    private IOHelper()
    {
        // Nothing to do
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Returns current working directory.
     * In BlueJ, the currect directory is undefined and can not be set,
     * so the directory of the current project is returned instead.
     *
     * @return current working directory
     */
    public static File getCurrentWorkingDirectory()
    {
        return cwd;
    }


    // ----------------------------------------------------------
    /**
     * Changes the current working directory (for the purposes of future
     * calls to IOHelper methods).  This doesn't actually change the JVM
     * process' notion of the current working directory, but does change
     * the directory that other methods in this class use for locating all
     * relative path names.
     * @param newCwd The new current working directory
     */
    public static void setCurrentWorkingDirectory(File newCwd)
    {
        assert newCwd != null  : "current working directory cannot be null";
        assert newCwd.exists() : "current working directory must exist";
        cwd = newCwd;
    }


    // ----------------------------------------------------------
    /**
     * Creates a new {@link File} instance from a relative or an absolute
     * pathname.
     * If given a relative path, it is resolved against the current
     * working directory.  In BlueJ, the directory of the current project
     * is used as the working directory for resolving relative paths.
     * Absolute path names are left unaltered.
     *
     * @param pathname a relative or absolute pathname
     * @return         instance of {@link File} referring to the
     *                 specified path
     */
    public static File getFile( String pathname )
    {
        File child = new File( pathname );
        if ( child.isAbsolute() )
        {
            return child;
        }
        else
        {
            return new File( cwd, pathname );
        }
    }


    // ----------------------------------------------------------
    /**
     * Takes an existing file and, if it contains a relative path name,
     * it is resolved against the current working directory.  In BlueJ,
     * the directory of the current project is used as the working
     * directory for resolving relative paths.  Absolute path names are left
     * unaltered.
     *
     * @param pathname a relative or absolute pathname as a File object
     * @return         instance of {@link File} referring to the
     *                 specified path
     */
    public static File getFile( File pathname )
    {
        if ( pathname.isAbsolute() )
        {
            return pathname;
        }
        else
        {
            return new File( cwd, pathname.getPath() );
        }
    }


    // ----------------------------------------------------------
    /**
     * Creates and returns an instance of {@link BufferedReader} that
     * can be used to read from the keyboard.
     * To enter text, you usually have to switch to some console in your
     * IDE.  Also, remember not to close the keyboard reader.  The {@link
     * BufferedReader#readLine()} method blocks until the user hits enter.
     * <p>
     * This method is provided to support older code, but writers of newer
     * code should consider {@link #createKeyboardScanner()} instead.
     * </p>
     * @return instance of {@link BufferedReader} for reading from keyboard
     */
    public static BufferedReader createKeyboardReader()
    {
        return new BufferedReader( new InputStreamReader( System.in ) );
    }


    // ----------------------------------------------------------
    /**
     * Creates and returns a {@link Scanner} that
     * can be used to read from the keyboard.  This is just a convenience
     * method for uniformity, since the Scanner class provides a constructor
     * that can take {@link System#in} as a parameter.
     * To enter text, you usually have to switch to some console in your
     * IDE.  Also, remember not to close the keyboard scanner.  The {@link
     * Scanner#nextLine()} method blocks until the user hits enter.
     *
     * @return instance of {@link Scanner} for reading from keyboard
     */
    public static Scanner createKeyboardScanner()
    {
        return new Scanner( System.in );
    }


    // ----------------------------------------------------------
    /**
     * Creates an instance of {@link BufferedReader} that can be used
     * to read from the given file.
     * <p>
     * This method is provided to support older code, but writers of newer
     * code should consider {@link #createScanner(File)} instead.
     * </p>
     *
     * @param file  the file to read from
     * @return      instance of {@link BufferedReader} for reading from
     *               the file
     * @throws RuntimeException if there is an error opening the file
     */
    public static BufferedReader createBufferedReader( File file )
    {
        try
        {
            return new BufferedReader( new java.io.FileReader( file ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }


    // ----------------------------------------------------------
    /**
     * Creates a {@link Scanner} that can be used
     * to read from the given file.  This is just a convenience
     * method for uniformity, since the Scanner class provides a constructor
     * that can take {@link System#in} as a parameter.
     * Turns any {@link IOException} that is raised into a
     * {@link RuntimeException} so that it does not have to be
     * placed inside a try/catch block.
     *
     * @param file  the file to read from
     * @return      instance of {@link Scanner} for reading from
     *               the file
     */
    public static Scanner createScanner( File file )
    {
        try
        {
            return new Scanner( file );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }


    // ----------------------------------------------------------
    /**
     * Creates an instance of {@link BufferedReader} that can be used to
     * read from the file referred to by the given name.
     * <p>
     * This method is provided to support older code, but writers of newer
     * code should consider {@link #createScanner(String)} instead.
     * </p>
     * @param pathname  a relative or absolute pathname indicating the
     *                  file to read from
     * @return          instance of {@link BufferedReader} for reading from
     *                  the named file
     * @throws RuntimeException if there is an error opening the file
     */
    public static BufferedReader createBufferedReader( String pathname )
    {
        return createBufferedReader( getFile( pathname ) );
    }


    // ----------------------------------------------------------
    /**
     * Creates a {@link Scanner} that can be used to
     * read from the file referred to by the given name.
     * Turns any {@link IOException} that is raised into a
     * {@link RuntimeException} so that it does not have to be
     * placed inside a try/catch block.
     *
     * @param pathname  a relative or absolute pathname indicating the
     *                  file to read from
     * @return          instance of {@link Scanner} for reading from
     *                  the named file
     */
    public static Scanner createScanner( String pathname )
    {
        return createScanner( getFile( pathname ) );
    }


    // ----------------------------------------------------------
    /**
     * Creates an instance of {@link BufferedReader} that can be used
     * to read from a URL over the net.
     * <p>
     * This method is provided to support older code, but writers of newer
     * code should consider {@link #createScanner(URL)} instead.
     * </p>
     * @param url  the URL to read from
     * @return     instance of {@link BufferedReader} for reading from
     *             the URL
     * @throws RuntimeException if there is an error opening the URL
     */
    public static BufferedReader createBufferedReader( URL url )
    {
        try
        {
            return new BufferedReader(
                new InputStreamReader( url.openStream() ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }


    // ----------------------------------------------------------
    /**
     * Creates a {@link Scanner} that can be used
     * to read from a URL over the net.
     * Turns any {@link IOException} that is raised into a
     * {@link RuntimeException} so that it does not have to be
     * placed inside a try/catch block.
     *
     * @param url  the URL to read from
     * @return     instance of {@link Scanner} for reading from
     *             the URL
     */
    public static Scanner createScanner( URL url )
    {
        try
        {
            java.net.URLConnection connection = url.openConnection();
            // Use a browser-like user agent, so that servers that
            // refuse connections from generic programs might still
            // provide a useful response
            connection.setRequestProperty("User-Agent", USER_AGENT);
            return new Scanner( connection.getInputStream() );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }


    // ----------------------------------------------------------
    /**
     * Creates an instance of {@link BufferedReader} that can be used to
     * read from a URL given as a text string.
     * <p>
     * This method is provided to support older code, but writers of newer
     * code should consider {@link #createScannerForURL(String)} instead.
     * </p>
     * @param url  a string denoting a URL to read from
     * @return     instance of {@link BufferedReader} for reading from
     *             the given URL
     * @throws RuntimeException if there is an error opening the URL or the
     *                     URL is syntactically incorrect
     */
    public static BufferedReader createBufferedReaderForURL( String url )
    {
        try
        {
            return createBufferedReader( new URL( url ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }


    // ----------------------------------------------------------
    /**
     * Creates a {@link Scanner} that can be used to
     * read from a URL given as a text string.
     * Turns any {@link MalformedURLException} or other {@link IOException}
     * that is raised into a {@link RuntimeException} so that it does not
     * have to be placed inside a try/catch block.
     *
     * @param url  a string denoting a URL to read from
     * @return     instance of {@link Scanner} for reading from
     *             the given URL
     */
    public static Scanner createScannerForURL( String url )
    {
        try
        {
            return createScanner( new URL( url ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }


    // ----------------------------------------------------------
    /**
     * Creates an instance of {@link BufferedReader} that can be used to
     * read directly from a given text string.
     * <p>
     * This method is provided to support older code, but writers of newer
     * code should consider {@link #createScannerForString(String)} instead.
     * </p>
     * @param s  the string to read from
     * @return   instance of {@link BufferedReader} for reading from
     *           the given string
     */
    public static BufferedReader createBufferedReaderForString( String s )
    {
        return new BufferedReader( new StringReader( s ) );
    }


    // ----------------------------------------------------------
    /**
     * Creates a {@link Scanner} that can be used to
     * read directly from a given text string.  This is just a convenience
     * method for uniformity, since the Scanner class provides a constructor
     * that can take a String as a parameter.
     *
     * @param s  the string to read from
     * @return   instance of {@link Scanner} for reading from
     *           the given string
     */
    public static Scanner createScannerForString( String s )
    {
        return new Scanner( s );
    }


    // ----------------------------------------------------------
    /**
     * Creates and returns an instance of {@link PrintWriter} that
     * can be used to write to the console.
     * Normally, {@link System#out} is easy enough to use for basic
     * information.  However, this method makes it easier to write
     * I/O-based classes that use {@link PrintWriter} in their interface,
     * but are still capable of writing to the screen as well as to a
     * file.
     *
     * @return instance of {@link PrintWriter} for writing to the console
     */
    public static PrintWriter createConsoleWriter()
    {
        return new PrintWriter( System.out, true );
    }


    // ----------------------------------------------------------
    /**
     * Creates an instance of {@link PrintWriter} that can be used to
     * write to the given file.
     * If the file does not exist, it will be created if possible
     * (including any necessary parent directories that do not exist).
     *
     * @param file    the file to write to
     * @param append  true means preserve any existing file contents and
     *                start write at the end of the current file, while false
     *                means to delete any existing content first and start
     *                writing from scratch.
     * @return        instance of {@link PrintWriter} for writing to the
     *                given file
     */
    public static PrintWriter createPrintWriter( File file, boolean append )
    {
        try
        {
            File parent  = file.getParentFile();
            if ( !parent.exists() )
            {
                parent.mkdirs();
            }
            return new PrintWriter(
                new java.io.FileOutputStream( file, append ), true );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }


    // ----------------------------------------------------------
    /**
     * Creates an instance of {@link PrintWriter} that can be used to
     * write to the file referred to by the given name.
     * If the file does not exist, it will be created if possible
     * (including any necessary parent directories that do not exist).
     *
     * @param pathname  a relative or absolute pathname indicating the file
     *                  to write to
     * @param append    true means preserve any existing file contents and
     *                  start write at the end of the current file, while false
     *                  means to delete any existing content first and start
     *                  writing from scratch.
     * @return          instance of {@link PrintWriter} for writing to the
     *                  given file
     */
    public static PrintWriter createPrintWriter( String pathname,
                                                 boolean append )
    {
        return createPrintWriter( getFile( pathname ), append );
    }


    // ----------------------------------------------------------
    /**
     * Creates an instance of {@link PrintWriter} that can be used to
     * write to the file referred to by the given name.
     * If the file does not exist, it will be created if possible
     * (including any necessary parent directories that do not exist).
     * If the file does exist, it will be overwritten.
     *
     * @param pathname  a relative or absolute pathname indicating the file
     *                  to write to
     * @return          instance of {@link PrintWriter} for writing to the
     *                  given file
     */
    public static PrintWriter createPrintWriter( String pathname )
    {
        return createPrintWriter( getFile( pathname ), false );
    }


    //~ Instance/static variables .............................................

    private static final String USER_AGENT =
        "Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)";
}
