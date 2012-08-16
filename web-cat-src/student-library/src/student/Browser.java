/*==========================================================================*\
 |  $Id: Browser.java,v 1.4 2010/05/27 21:41:07 stedwar2 Exp $
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.StringTokenizer;
import java.util.Vector;

// -------------------------------------------------------------------------
/**
 *  This class provides a simple interface to your web browser.  The
 *  primary capability it currently supports is opening a URL in a
 *  browser window.
 *
 *  @version 2003.09.25
 *  @author Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.4 $, $Date: 2010/05/27 21:41:07 $
 */
public class Browser
{
    //~ Instance/static variables .............................................

    // These fields are used for overridable world startup
    private static String[] exec          = null;
    private static boolean runningInBatch = false;


    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * This class should not be instantiated.
     */
    private Browser()
    {
        // Nothing to initialize
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Display a file in the system browser.
     * @param pathname a relative or absolute path designating the file to
     *                 display
     */
    public static void openFile( String pathname )
    {
        openFile( IOHelper.getFile( pathname ) );
    }


    // ----------------------------------------------------------
    /**
     * Display a file in the system browser.
     * @param file the file to display
     */
    public static void openFile( File file )
    {
        try
        {
            openURL( "file:///" + file.getCanonicalPath() );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e.getMessage() );
        }
    }


    // ----------------------------------------------------------
    /**
     * Display a URL in the system browser.
     * @param url the url to display
     */
    public static void openURL( String url )
    {
        if ( runningInBatch) return;
        if ( exec == null  ||  exec.length == 0 )
        {
            if ( System.getProperty( "os.name" ).startsWith( "Mac" ) )
            {
                boolean success = false;
                try
                {
                    Class<?> macClass;
                    if ( System.getProperty( "java.vm.version" )
                        .startsWith( "1.3" ) )
                    {
                        macClass = Class.forName( "MRJFileUtils" );
                    }
                    else
                    {
                        macClass =
                            Class.forName( "com.apple.eio.FileManager" );
                    }
                    Method m = macClass.getMethod(
                        "openURL",
                        new Class[] { String.class }
                    );
                    m.invoke( null, (Object[])new String[] { url } );
                    success = true;
                }
                catch(Exception e)
                {
                    // ignore it
                }
                if ( !success )
                {
                    try
                    {
                        Class<?> nSWorkspace;
                        if ( new File( "/System/Library/Java/com/apple/cocoa/application/NSWorkspace.class" ).exists() )
                        {
                            // Mac OS X has NSWorkspace, but it is not in the
                            // classpath, so add it.
                            ClassLoader classLoader = new URLClassLoader(
                                new URL[]{ new File( "/System/Library/Java" )
                                    .toURI().toURL() } );
                            nSWorkspace = Class.forName(
                                "com.apple.cocoa.application.NSWorkspace",
                                true,
                                classLoader );
                        }
                        else
                        {
                            nSWorkspace = Class.forName(
                                "com.apple.cocoa.application.NSWorkspace" );
                        }
                        Method sharedWorkspace = nSWorkspace.getMethod(
                            "sharedWorkspace", new Class[] {} );
                        Object workspace =
                            sharedWorkspace.invoke(
                                null, new Object[] {} );
                        Method openURL = nSWorkspace.getMethod(
                            "openURL",
                            new Class[] { java.net.URL.class } );
                        success = ( (Boolean)openURL.invoke(
                            workspace,
                            new Object[] { new java.net.URL( url ) } )
                        ).booleanValue();
                        // success = com.apple.cocoa.application.NSWorkspace
                        //   .sharedWorkspace().openURL(new java.net.URL(url));
                    }
                    catch ( Exception x )
                    {
                        // swallow exception
                    }
                }
                if ( !success )
                {
                    try
                    {
                        Class<?> mrjFileUtils =
                            Class.forName( "com.apple.mrj.MRJFileUtils" );
                        Method openURL = mrjFileUtils.getMethod(
                            "openURL",
                            new Class[] { String.class }
                        );
                        openURL.invoke( null, new Object[] { url } );
                        // com.apple.mrj.MRJFileUtils.openURL( url );
                    }
                    catch ( Exception x )
                    {
                        throw new RuntimeException( "Browser launch failed:"
                            + x.getMessage() );
                    }
                }
            }
            else
            {
                throw new RuntimeException(
                    "Browser execute command cannot be found" );
            }
        }
        else
        {
            // for security, see if the url is valid.
            // this is primarily to catch an attack in which the url
            // starts with a - to fool the command line flags, bu
            // it could catch other stuff as well, and will throw a
            // MalformedURLException which will give the caller of this
            // function useful information.
            try
            {
                new URL( url );
            }
            catch ( Exception e )
            {
                throw new RuntimeException(e);
            }
            // escape any weird characters in the url.  This is primarily
            // to prevent an attacker from putting in spaces
            // that might fool exec into allowing
            // the attacker to execute arbitrary code.
            StringBuffer sb = new StringBuffer( url.length() );
            for ( int i = 0; i < url.length(); i++ )
            {
                char c = url.charAt( i );
                if (    (c >= 'a' && c <= 'z')
                    || (c >= 'A' && c <= 'Z')
                    || (c >= '0' && c <= '9')
                    || c == '.' || c == ':' || c == '&' || c == '@'
                    || c == '/' || c == '?' || c == '%' || c =='+'
                    || c == '=' || c == '#' || c == '-' || c == '\\' )
                {
                    // characters that are necessary for URLs and should be
                    // safe to pass to exec.  Exec uses a default string
                    // tokenizer with the default arguments (whitespace) to
                    // separate command line arguments, so there should be no
                    // problem with anything but whitespace.
                    sb.append( c );
                }
                else
                {
                    // get the lowest 8 bits (URLEncoding)
                    c = (char)( c & 0xFF );
                    if ( c < 0x10 )
                    {
                        sb.append( "%0" + Integer.toHexString( c ) );
                    }
                    else
                    {
                        sb.append( "%" + Integer.toHexString( c ) );
                    }
                }
            }
            String[] messageArray = new String[1];
            messageArray[0] = sb.toString();
            String command = null;
            boolean found  = false;
            // try each of the exec commands until something works
            try
            {
                for ( int i = 0; i < exec.length  &&  !found; i++ )
                {
                    try
                    {
                        // stick the url into the command
                        command =
                            MessageFormat.format( exec[i],
                                (Object[])messageArray );
                        // parse the command line.
                        Vector<String> argsVector = new Vector<String>();
                        StringTokenizer lex = new StringTokenizer( command );
                        while ( lex.hasMoreTokens() )
                        {
                            argsVector.add( lex.nextToken() );
                        }
                        String[] args = new String[ argsVector.size() ];
                        args = argsVector.toArray( args );
                        // the windows url protocol handler doesn't work well
                        // with file URLs.  Correct those problems here before
                        // continuing Java File.toURL() gives only one /
                        // following file: but we need two.  If there are
                        // escaped characters in the url, we will have
                        // to create an Internet shortcut and open that, as
                        // the command line version of the rundll doesn't like
                        // them.
                        boolean useShortCut = false;
                        if ( args[0].equals( "rundll32" )  &&
                            args[1].equals( "url.dll,FileProtocolHandler" ) )
                        {
                            if ( args[2].startsWith( "file:/" ) )
                            {
                                if ( args[2].charAt( 6 ) != '/' )
                                {
                                    args[2] =
                                        "file://" + args[2].substring( 6 );
                                }
                                if ( args[2].charAt( 7 ) != '/' )
                                {
                                    args[2] =
                                        "file:///" + args[2].substring( 7 );
                                }
                                useShortCut = true;
                            }
                            else if ( args[2].toLowerCase().endsWith( "html" )
                                || args[2].toLowerCase().endsWith("htm"))
                            {
                                useShortCut = true;
                            }
                        }
                        if ( useShortCut )
                        {
                            try
                            {
                                File shortcut = File.createTempFile(
                                    "OpenInBrowser", ".url" );
                                shortcut = shortcut.getCanonicalFile();
                                shortcut.deleteOnExit();
                                PrintWriter out = new PrintWriter(
                                    new FileWriter( shortcut ) );
                                out.println( "[InternetShortcut]" );
                                out.println( "URL=" + args[2] );
                                out.close();
                                args[2] = shortcut.getCanonicalPath();
                            }
                            catch ( Exception e )
                            {
                                throw new RuntimeException(
                                    "Failure to create shortcut to open "
                                    + "file URL",
                                    e );
                            }
                        }
                        // start the browser
                        Process p = Runtime.getRuntime().exec( args );

                        // give the browser a bit of time to fail.
                        // I have found that sometimes sleep doesn't work
                        // the first time, so do it twice.  My tests
                        // seem to show that 1000 milliseconds is enough
                        // time for the browsers I'm using.
                        for ( int j = 0; j < 2; j++ )
                        {
                            try
                            {
                                Thread.sleep( 1000 );
                            }
                            catch ( InterruptedException inte )
                            {
                                // ignore it
                            }
                        }
                        if ( p.exitValue() == 0 )
                        {
                            // this is a weird case.  The browser exited after
                            // a couple seconds saying that it successfully
                            // displayed the url.  Either the browser is lying
                            // or the user closed it *really* quickly. Oh well.
                            found = true;
                        }
                    }
                    catch ( IOException x )
                    {
                        // the command was not a valid command.
                        System.err.println("Warning: " + x.getMessage() );
                    }
                }
                if ( !found )
                {
                    // we never found a command that didn't terminate with an
                    // error.
                    throw new RuntimeException( "Browser launch failed" );
                }
            }
            catch ( IllegalThreadStateException e )
            {
                // the browser is still running.  This is a good sign.
                // lets just say that it is displaying the url right now!
            }
        }
    }


    //~ Static Initialization .................................................
    /*
     * Retrieve the default commands to open a browser for this system.
     */
    static
    {
        runningInBatch = false;
        try
        {
            Browser.class.getClassLoader().loadClass( "student.RunInBatch" );
            runningInBatch = true;
        }
        catch ( Exception e )
        {
            try
            {
                Browser.class.getClassLoader().loadClass( "cs1705.RunInBatch" );
                runningInBatch = true;
            }
            catch ( Exception ee )
            {
                // Leave default value in place
            }
        }

        if ( !runningInBatch )
        {
            if ( System.getProperty( "os.name" ).startsWith( "Windows" ) )
            {
                exec = new String[]{
                    "rundll32 url.dll,FileProtocolHandler {0}"
                };
            }
            else if ( System.getProperty( "os.name" ).startsWith( "Mac" ) )
            {
                Vector<String> browsers = new Vector<String>();
                try
                {
                    Process p = Runtime.getRuntime().exec( "which open" );
                    if ( p.waitFor() == 0 )
                    {
                        browsers.add( "open {0}" );
                    }
                }
                catch ( IOException e )
                {
                    // ignore it
                }
                catch ( InterruptedException e )
                {
                    // ignore it
                }
                if ( browsers.size() == 0 )
                {
                    exec = null;
                }
                else
                {
                    exec = browsers.toArray( new String[0] );
                }
            }
            else
            {
                Vector<String> browsers = new Vector<String>();
                try
                {
                    Process p = Runtime.getRuntime().exec( "which firebird" );
                    if ( p.waitFor() == 0 )
                    {
                        browsers.add( "firebird -remote openURL({0})" );
                        browsers.add( "firebird {0}" );
                    }
                }
                catch ( IOException e )
                {
                    // ignore it
                }
                catch ( InterruptedException e )
                {
                    // ignore it
                }
                try
                {
                    Process p = Runtime.getRuntime().exec( "which mozilla" );
                    if ( p.waitFor() == 0 )
                    {
                        browsers.add( "mozilla -remote openURL({0})" );
                        browsers.add( "mozilla {0}" );
                    }
                }
                catch ( IOException e )
                {
                    // ignore it
                }
                catch ( InterruptedException e )
                {
                    // ignore it
                }
                try
                {
                    Process p = Runtime.getRuntime().exec( "which opera" );
                    if ( p.waitFor() == 0 )
                    {
                        browsers.add( "opera -remote openURL({0})" );
                        browsers.add( "opera {0}" );
                    }
                }
                catch ( IOException e )
                {
                    // ignore it
                }
                catch ( InterruptedException e )
                {
                    // ignore it
                }
                try
                {
                    Process p = Runtime.getRuntime().exec( "which galeon" );
                    if ( p.waitFor() == 0 )
                    {
                        browsers.add( "galeon {0}" );
                    }
                }
                catch ( IOException e )
                {
                    // ignore it
                }
                catch ( InterruptedException e )
                {
                    // ignore it
                }
                try
                {
                    Process p = Runtime.getRuntime().exec( "which konqueror" );
                    if ( p.waitFor() == 0 )
                    {
                        browsers.add( "konqueror {0}" );
                    }
                }
                catch ( IOException e )
                {
                    // ignore it
                }
                catch ( InterruptedException e )
                {
                    // ignore it
                }
                try
                {
                    Process p = Runtime.getRuntime().exec( "which netscape" );
                    if ( p.waitFor() == 0 )
                    {
                        browsers.add( "netscape -remote openURL({0})" );
                        browsers.add( "netscape {0}" );
                    }
                }
                catch ( IOException e )
                {
                    // ignore it
                }
                catch ( InterruptedException e )
                {
                    // ignore it
                }
                try
                {
                    Process p = Runtime.getRuntime().exec( "which xterm" );
                    if ( p.waitFor() == 0 )
                    {
                        p = Runtime.getRuntime().exec( "which lynx" );
                        if ( p.waitFor() == 0 )
                        {
                            browsers.add( "xterm -e lynx {0}" );
                        }
                    }
                }
                catch ( IOException e )
                {
                    // ignore it
                }
                catch ( InterruptedException e )
                {
                    // ignore it
                }
                if ( browsers.size() == 0 )
                {
                    exec = null;
                }
                else
                {
                    exec = browsers.toArray(new String[0]);
                }
            }
        }
    }

}
