/*==========================================================================*\
 |  $Id: Main.java,v 1.1 2009/10/10 17:05:40 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2009 Virginia Tech
 |
 |  This file is part of the Web-CAT CxxTest Distribution.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU Affero General Public License as published
 |  by the Free Software Foundation; either version 3 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU Affero General Public License
 |  along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package net.sf.webcat.cxxtest.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//--------------------------------------------------------------------------
/**
 * The main class for the CxxTest generator. Command-line options include:
 * 
 * <dl>
 * <dt>-o FILE, --output=FILE</dt>
 * <dd>Generates the test runner to FILE; if omitted, output is sent to
 * standard output</dd>
 * <dt>-l CLASS, --listener=CLASS</dt>
 * <dd>The name of the CxxTest listener class that should receive notifications
 * about test case events; it is assumed that this class can be included from
 * the path <tt>&lt;cxxtest/CLASS.h&gt;</tt>. This argument can occur multiple
 * times to supply multiple listeners that will all receive test notifications
 * </dd>
 * <dt>-dl LISTENER, --dereferee-listener=LISTENER</dt>
 * <dd>The name of the listener module to use with Dereferee (as opposed to a
 * CxxTest listener), such as stdio_listener</dd>
 * <dt>-dp PLATFORM, --dereferee-platform=PLATFORM</dt>
 * <dd>The name of the platform module to use with Dereferee, such as
 * gcc_macosx_platform or msvc_win32_platform</dd>
 * <dt>--include-dereferee</dt>
 * <dd>Include the Dereferee source code along with the CxxTest Root.cpp file.
 * Leave this option out if you're linking to a separately compiled Dereferee
 * library.</dd>
 * <dt>--backtrace</dt>
 * <dd>Enables the generation of backtraces upon test case failures; you must
 * also supply a value for -dp/--dereferee-platform</dd>
 * <dt>--no-trap-signals</dt>
 * <dd>Suppresses the use of a signal handler (or under Win32, a structured
 * exception handler) that captures segmentation faults and other system
 * exceptions and translates them to test case failures. With this option set,
 * such exceptions will terminate the application as expected</dd>
 * <dt>--long-long=TYPE</dt>
 * <dd>The name of the C++ type that should be used to represent a "long long"
 * (64-bit) value; if omitted, "long long" will be used</dd>
 * </dl>
 * 
 * Following all the options should be the list of C++ header files that
 * contain the CxxTest test cases that should be executed.
 * 
 * @author Tony ALlevato
 * @version $Id: Main.java,v 1.1 2009/10/10 17:05:40 aallowat Exp $
 */
public class Main
{
    //~ Methods ...............................................................
    
    // ----------------------------------------------------------
    /**
     * The main entry point for the CxxTest generator.
     */
    public static void main(String[] args) throws IOException
    {
        Main main = new Main();
        main.run(new ArrayList<String>(Arrays.asList(args)));
    }
    
    
    // ----------------------------------------------------------
    private void run(List<String> args)
    {
        String outputFilename = null;
        List<String> listeners = new ArrayList<String>();
        List<String> testFiles = new ArrayList<String>();
        List<String> additionalDefines = new ArrayList<String>();

        boolean trapSignals = true;
        boolean backtracing = false;
        boolean includeDereferee = false;
        String longLongType = null;
        String derefereeListener = null;
        String derefereePlatform = null;

        // Process the command line options.
        
        while (!args.isEmpty())
        {
            String arg = args.remove(0);

            if ("-o".equals(arg))
            {
                outputFilename = nextArg(args);
            }
            else if (arg.startsWith("--output"))
            {
                outputFilename = stripValue(arg);
            }
            else if ("-l".equals(arg))
            {
                listeners.add(nextArg(args));
            }
            else if (arg.startsWith("--listener"))
            {
                listeners.add(stripValue(arg));
            }
            else if ("-dl".equals(arg))
            {
                derefereeListener = nextArg(args);
            }
            else if (arg.startsWith("--dereferee-listener"))
            {
                derefereeListener = stripValue(arg);
            }
            else if ("-dp".equals(arg))
            {
                derefereePlatform = nextArg(args);
            }
            else if (arg.startsWith("--dereferee-platform"))
            {
                derefereePlatform = stripValue(arg);
            }
            else if ("-d".equals(arg))
            {
                additionalDefines.add(nextArg(args));
            }
            else if (arg.startsWith("--define"))
            {
                additionalDefines.add(stripValue(arg));
            }
            else if ("--include-dereferee".equals(arg))
            {
                includeDereferee = true;
            }
            else if ("--backtrace".equals(arg))
            {
                backtracing = true;
            }
            else if ("--no-trap-signals".equals(arg))
            {
                trapSignals = false;
            }
            else if (arg.startsWith("--long-long"))
            {
                longLongType = stripValue(arg);
            }
            else if (arg.startsWith("-"))
            {
                System.out.println("Unrecognized option: " + arg);
                printUsageAndExit();
            }
            else
            {
                testFiles.add(arg);
                
                while (!args.isEmpty())
                {
                    arg = args.remove(0);
                    testFiles.add(arg);
                }
            }
        }

        // Print a usage message if there were any errors in the command line.

        if (testFiles.size() == 0)
        {
            printUsageAndExit();
        }
        
        // Collect the test cases from the specified C++ header files and then
        // generate the runner source file.

        TestSuiteCollector collector = new TestSuiteCollector(testFiles);
        
        TestRunnerGenerator generator = new TestRunnerGenerator(
                outputFilename, collector.getSuites(), null, listeners);

        generator.setOption("traceStack", backtracing);
        generator.setOption("trapSignals", trapSignals);
        generator.setOption("longLongType", longLongType);
        generator.setOption("includeDereferee", includeDereferee);
        generator.setOption("derefereeListener", derefereeListener);
        generator.setOption("derefereePlatform", derefereePlatform);
        generator.setOption("additionalDefines", additionalDefines);

        generator.generate();
    }


    // ----------------------------------------------------------
    private String nextArg(List<String> args)
    {
        if (args.isEmpty())
        {
            printUsageAndExit();
            return null; // never reached
        }
        else
        {
            return args.remove(0);
        }
    }

    
    // ----------------------------------------------------------
    private String stripValue(String arg)
    {
        int index = arg.indexOf('=');
        
        if (index == -1)
        {
            printUsageAndExit();
            return null; // never reached
        }
        else
        {
            return arg.substring(index + 1);
        }
    }


    // ----------------------------------------------------------
    private void printUsageAndExit()
    {
        System.out.println("Usage: cxxtestgen "
                + "[OPTION...] FILES...");

        System.out.println("Options:");
        
        System.out.println(
                "  -o, --output=FILE            "
                + "send output to FILE");
        
        System.out.println(
                "  -l, --listener=NAME          "
                + "class name of CxxTest listener");
        
        System.out.println(
                "  -dl, --dereferee-listener=NAME "
                + "name of Dereferee listener module");
        
        System.out.println(
                "  -dp, --dereferee-platform=NAME "
                + "name of Dereferee platform module");
        
        System.out.println(
                "  -d, --define=NAME            "
                + "additional preprocessor symbols to define");

        System.out.println(
                "      --backtrace              "
                + "enable backtrace collection");
        
        System.out.println(
                "      --no-trap-signals        "
                + "suppress signal trapping logic");
        
        System.out.println(
                "      --long-long=TYPE         "
                + "use TYPE as 'long long'");

        System.out.println();

        System.exit(1);
    }
}
