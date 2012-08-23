/*==========================================================================*\
 |  $Id: AverageTimeout.java,v 1.2 2011/06/09 15:35:28 stedwar2 Exp $
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

import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

//-------------------------------------------------------------------------
/**
 * Attempts to give test methods in a class enough time to execute, while at
 * the same time watching out for long-running methods, based on the running
 * average.
 *
 * This class is really just a proof-of-concept class for experimental
 * purposes.
 *
 * @author Craig Estep
 * @author Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2011/06/09 15:35:28 $
 */
public class AverageTimeout
    implements MethodRule
{
    //~ Instance/static variables .............................................

	private static long start;
	private static long last;

	// private static ArrayList<Integer> times;
	private int avg;

	private int allowance;

	private boolean exceeded;
	private int strict;

	private int count;


    //~ Constructors ..........................................................

    // ----------------------------------------------------------
	/**
	 * Sets the default values of 5x average runtime when a test has not timed
	 * out, and a 2x multiplier if one has.
	 */
	public AverageTimeout()
	{
		this(5, 2);
	}


    // ----------------------------------------------------------
	/**
	 * Sets the multipliers to the values provided.
	 *
	 * @param allowanceMultiplier  the multiplier on the average that each
	 *                             method is allowed before a timeout has
	 *                             occurred.
	 * @param strictMultiplier     the multiplier after a timout has occurred.
	 */
	public AverageTimeout(int allowanceMultiplier, int strictMultiplier)
	{
		start = System.currentTimeMillis();
		last = start;
		allowance = allowanceMultiplier;
		strict = strictMultiplier;
		count = 0;
		avg = 0;
		exceeded = false;
		// times = new ArrayList<Integer>();
	}


    //~ Methods ...............................................................

    // ----------------------------------------------------------
	/**
	 * Applies the appropriate multiplier to the running average and gives
	 * this method that as the maximum running time before timeout.
	 */
	public Statement apply(
	    Statement base, FrameworkMethod method, Object target)
	{
		long curr = System.currentTimeMillis();

		if (last != start)
		{
			int diff = (int) (curr - last);
			boolean exceeded = false;
			int t = (avg * ((exceeded) ? strict : allowance));
			// System.out.println("d: " + diff + ", t: " + t);
			if (t > 0 && diff > t)
			{
				exceeded = true;
				// System.out.println("timeout detected");
			}
			// times.add(diff);
			avg = (int) ((curr - start) / count);

			// System.out.println("last method took " + diff + "ms. Average "
			// + avg + "ms");

			if (exceeded)
			{
				// System.out.println("last method timed out");
				this.exceeded = true;
			}
		}
		last = curr;
		count++;

		if (avg != 0)
		{
			// if (exceeded)
			// System.out.println("timeout was exceeded previously");
			int timeout = (exceeded) ? avg * strict : avg * allowance;
			// System.out.println("giving next test " + timeout + "ms to run");
			return new FailOnTimeout(base, timeout);
		} else
		{
			return base;
		}
	}


    // ----------------------------------------------------------
	/**
	 * Prints statistics of run tests to the console.
	 */
	public void printStats()
	{
		System.out.println();
		System.out.println("printing adaptive timeout stats");
		System.out.println(count + " test(s) run");
		long curr = System.currentTimeMillis();
		System.out.println("total running time: " + (curr - start) + "ms");
		avg = (int) ((curr - start) / count);
		System.out.println("average: " + avg + "ms");
		if (exceeded)
		{
			System.out.println("a test timed out");
		}
		else
		{
			System.out.println("no tests timed out");
		}
	}
}