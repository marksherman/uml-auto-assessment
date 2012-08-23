/*==========================================================================*\
 |  $Id: AdaptiveTimeout.java,v 1.4 2012/03/05 14:16:59 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2011 Virginia Tech
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

package student.testingsupport.junit4;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

//-------------------------------------------------------------------------
/**
 * Custom rule for managing a test class so as to allow time for well-behaved
 * methods but still cut off methods that run longer than expected or are
 * nonterminating.
 * <p>
 * The problem this rule solves is where an automatic grading server must
 * grade student code based on professor-written tests. Most of the student
 * code may work properly, while some of it fails to run within a reasonable
 * time frame.  It would slow down the entire server if methods were not
 * given a time limit for execution, and there may be many other students.
 * Hard limits on the entire test class could be adopted, but this may
 * confuse students and would not help pinpoint problem areas. A per-method
 * limit could be adopted, but it would have to be generous enough to handle
 * all proper methods. This would be problematic if there were a large number
 * of nonterminating methods.
 * </p><p>
 * The solution arrived upon is a sort of adaptive timeout, where the test
 * class is given an initial (generous) ceiling for each method. It does not
 * cause slow downs to have a large timeout, unless there are nonterminating
 * methods.  If a nonterminating method is detected, the ceiling is ramped
 * down swiftly, choking out other nonterminating methods, and, if enough
 * occur, even correct methods. This approach saves server time while still
 * giving all tests a chance to run.
 * </p><p>
 * A running "ceiling" is applied as the timeout for the next method. This
 * ceiling may be adjusted upward to a maximum in the case of methods that
 * run close to the ceiling, or downward to a minimum if methods repeatedly
 * time out.
 * </p>
 *
 * @author Craig Estep
 * @author Last changed by $Author: stedwar2 $
 * @version $Revision: 1.4 $, $Date: 2012/03/05 14:16:59 $
 */
public class AdaptiveTimeout
    implements MethodRule
{
    //~ Instance/static variables .............................................

	private int ceiling;
	private final int maximum;
	private final int minimum;

	private final double threshold;
	private final double rampup;
	private final double rampdown;

	private long start;
	private long end;

	private final List<String> methodLog;
	private int numTestMethodsInTestClass;
	private int numTestMethodsInTotal;
    private int numNonterminatingTestMethods;
    private boolean headerPrinted;

	private String className;
	private String methodName;

	private static final String PROPERTY_PREFIX =
	    AdaptiveTimeout.class.getName();
	private static final String LOGFILE_NAME = PROPERTY_PREFIX + ".logfile";
	private static final String USER_NAME = PROPERTY_PREFIX + ".user";
    private static final String INCLUDE_HEADER = PROPERTY_PREFIX + ".header";
    private static final String CEILING = PROPERTY_PREFIX + ".ceiling";
    private static final String MAXIMUM = PROPERTY_PREFIX + ".maximum";
    private static final String MINIMUM = PROPERTY_PREFIX + ".minimum";
    private static final String THRESHOLD = PROPERTY_PREFIX + ".threshold";
    private static final String RAMP_UP = PROPERTY_PREFIX + ".rampup";
    private static final String RAMP_DOWN = PROPERTY_PREFIX + ".rampdown";
    private static final boolean IS_DEBUGGING;
    static // initialize IS_DEBUGGING
    {
        boolean isDebug = false;
        try
        {
            isDebug = AccessController.doPrivileged(
                new PrivilegedAction<Boolean>()
                {
                    public Boolean run()
                    {
                        return java.lang.management.ManagementFactory
                            .getRuntimeMXBean(). getInputArguments()
                            .toString().contains("-agentlib:jdwp");
                    }
                });
        }
        catch (Exception e)
        {
            // ignore it, and accept default
        }
        IS_DEBUGGING = isDebug;
    }


    //~ Constructors ..........................................................

	// ----------------------------------------------------------
	/**
	 * Default constructor assigns the following values: <br />
	 * <pre>
	 * ceiling   = 10000 ms
	 * maximum   = 20000 ms
	 * minimum   = 250 ms
	 * threshold = 0.6 (60%)
	 * rampup    = 1.4 (+ 40%)
	 * rampdown  = 0.5 (- 50%)
	 * </pre>
	 */
	public AdaptiveTimeout()
	{
		this(getIntProperty(CEILING, 10000),
		    getIntProperty(MAXIMUM, 20000),
		    getIntProperty(MINIMUM, 250),
		    getDoubleProperty(THRESHOLD, 0.6),
		    getDoubleProperty(RAMP_UP, 1.4),
		    getDoubleProperty(RAMP_DOWN, 0.5));
	}


	// ----------------------------------------------------------
	/**
	 * Creates a timeout with the given options.
	 *
	 * @param ceiling    The initial ceiling (in milliseconds).  Note that
	 *                   the parameters must satisfy
	 *                   0 <= minimum <= ceiling <= maximum.
	 * @param maximum    The maximum ceiling (in milliseconds).  The value
	 *                   must be >= minimum.
	 * @param minimum    The minimum ceiling (in milliseconds).  The value
	 *                   must be >= 0.
	 * @param threshold  The % of ceiling such that if a test runs longer
	 *                   than this percentage but still shorter than the
	 *                   ceiling, the ceiling is increased according to the
	 *                   ramping up strategy (should be between 0 and 1; use a
	 *                   value of 1 to disable).
	 * @param rampup     The value that is used to calculate a new higher
	 *                   ceiling, up to the maximum (should be between 0 and 1;
	 *                   use a value of 1 to disable ramp up).
	 *                   NewCeiling = OldCeiling * rampup.
	 * @param rampdown   The value that is used to calculate the new ceiling
	 *                   after a timeout occurs (should be less than or equal
	 *                   to 1; use a value of 1 to disable ramp down).
	 *                   NewCeiling = OldCeiling * rampdown.
	 */
	public AdaptiveTimeout(int ceiling, int maximum, int minimum,
			double threshold, double rampup, double rampdown)
	{
	    assert rampup >= 1.0
	        : "rampup must be >= 1.0";
        assert 0.0 <= rampdown && rampdown <= 1.0
            : "rampdown must be between 0.0 and 1.0";
        assert 0.0 <= minimum && minimum <= ceiling && ceiling <= maximum
            : "parameters must satisfy 0.0 <= minimum <= ceiling <= maximum";

        this.ceiling = ceiling;
		this.maximum = maximum;
		this.minimum = minimum;

		this.threshold = threshold;
		this.rampup = rampup;
		this.rampdown = rampdown;

		methodLog = new ArrayList<String>();
		headerPrinted = false;
		clearLog();
	}


	//~ Methods ...............................................................

	// ----------------------------------------------------------
	/**
	 * Adjusts the current ceiling based on the last method, and applies the
	 * ceiling to the next method to run.
	 */
	public Statement apply(
	    Statement base, FrameworkMethod method, Object target)
	{
	    if (IS_DEBUGGING)
	    {
	        // Don't use timeouts inside the debugger
	        return base;
	    }

	    // Make sure to log the previous test method as non-terminating
	    // if it failed due to timeout, since under those conditions,
	    // the @After logTestMethod() won't be executed.
	    logTestMethod(false);

	    long diff = end - start;

		if (diff > ceiling)
		{
		    numNonterminatingTestMethods++;
		    if (numNonterminatingTestMethods >= 2)
		    {
		        if ((ceiling * rampdown) < minimum)
		        {
		            ceiling = minimum;
		        }
		        else
		        {
		            // round the result
		            ceiling = (int) (ceiling * rampdown + 0.5);
		        }
		    }
		}
		else if (diff > ceiling * threshold)
		{
		    if ((ceiling * rampup) > maximum)
		    {
		        ceiling = maximum;
		    }
		    else
		    {
		        // round the result
		        ceiling = (int) (ceiling * rampup + 0.5);
		    }
		}

        numTestMethodsInTestClass++;
        numTestMethodsInTotal++;

        // Used to be this, which produces the declaring class (possibly
        // a superclass of the actual test class):
        // className = method.getMethod().getDeclaringClass().getName();

        // Changed to this, which produces the test class name, even if the
        // test class inherits the actual test method:
        className = target.getClass().getName();
		methodName = method.getName();

		start = end = System.currentTimeMillis();
		return new FailOnTimeout(base, ceiling);
	}


    // ----------------------------------------------------------
	/**
	 * Should be called in an @After in implementing class. Gathers
	 * statistics on last run method, if it did not time out.
	 * @param terminated Indicates whether the test method being logged
	 *                   terminated within the allowed time or not.
	 */
	public void logTestMethod(boolean terminated)
	{
	    long now = System.currentTimeMillis();

	    if (methodLog.size() >= numTestMethodsInTestClass)
	    {
	        return;
	    }

	    end = now;
        methodLog.add(
            numTestMethodsInTotal + ","
            + className + ","
            + methodName + ","
            + numTestMethodsInTestClass + ","
            + terminated + ","
            + (end - start) + ","
            + minimum + ","
            + ceiling + ","
            + maximum);
	}


    // ----------------------------------------------------------
    /**
     * Writes out statistics from the run test, as they stand, to the given
     * file. Statistics are written in CSV format, according to:
     * <pre>
     * <b>ClassName,MethodName,DidPreviousTerminate,PreviousRuntime,Minimum,
     * Ceiling,Maximum</b>
     * </pre>
     * If the file does not exist, it is created and these values are written
     * to the first line before statistics are written.
     */
    public void appendStatsToFile()
    {
        // Make sure to log the very last test method as non-terminating
        // if it failed due to timeout, since under those conditions,
        // the @After logTestMethod() won't be executed.
        logTestMethod(false);

        String logFileName = System.getProperty(LOGFILE_NAME);
        if (logFileName != null)
        {
            String userName = System.getProperty(USER_NAME);

            File logFile = new File(logFileName);
            try
            {
                BufferedWriter logWriter =
                    new BufferedWriter(new FileWriter(logFile, true));

                // Write the header row if needed
                if (System.getProperty(INCLUDE_HEADER) != null
                    && !headerPrinted)
                {
                    if (userName != null)
                    {
                        logWriter.append("Username,");
                    }
                    logWriter.append("Number,ClassName,MethodName,"
                        + "MethodNumberInClass,Terminated,Time,"
                        + "Minimum,Ceiling,Maximum");
                    logWriter.newLine();
                    headerPrinted = true;
                }

                // Now, write out all of the accumulated log lines
                if (userName == null)
                {
                    userName = "";
                }
                else
                {
                    userName += ",";
                }
                for (String line : methodLog)
                {
                    logWriter.append(userName);
                    logWriter.append(line);
                    logWriter.newLine();
                }
                logWriter.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        clearLog();
    }


    // ----------------------------------------------------------
    private void clearLog()
    {
        methodLog.clear();
        numTestMethodsInTestClass = 0;
        numNonterminatingTestMethods = 0;
        start = end;
    }


    // ----------------------------------------------------------
    private static int getIntProperty(
        final String name, final int defaultValue)
    {
        return AccessController.doPrivileged(
            new PrivilegedAction<Integer>()
            {
                public Integer run()
                {
                    String val = System.getProperty(name);
                    if (val == null || val.isEmpty())
                    {
                        return defaultValue;
                    }
                    try
                    {
                        return Integer.parseInt(val);
                    }
                    catch (NumberFormatException e)
                    {
                        return defaultValue;
                    }
                }
            });
    }


    // ----------------------------------------------------------
    private static double getDoubleProperty(
        final String name, final double defaultValue)
    {
        return AccessController.doPrivileged(
            new PrivilegedAction<Double>()
            {
                public Double run()
                {
                    String val = System.getProperty(name);
                    if (val == null || val.isEmpty())
                    {
                        return defaultValue;
                    }
                    try
                    {
                        return Double.parseDouble(val);
                    }
                    catch (NumberFormatException e)
                    {
                        return defaultValue;
                    }
                }
            });
    }
}
