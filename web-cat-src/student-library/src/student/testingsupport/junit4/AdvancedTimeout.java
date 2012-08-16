/*==========================================================================*\
 |  $Id: AdvancedTimeout.java,v 1.2 2011/06/09 15:35:28 stedwar2 Exp $
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
import org.junit.rules.*;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

//-------------------------------------------------------------------------
/**
 * A custom MethodRule, following the example of Timeout, that supports both
 * a per-method timeout, and a per-class timeout. The smallest of whichever
 * of these remains is used for the next test method.
 *
 * NOTE: You must declare an instance of this timeout as a static field in
 * the test class you want it to apply to.
 *
 * This class is really just a proof-of-concept class for experimental
 * purposes.
 *
 * @author Craig Estep
 * @author Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2011/06/09 15:35:28 $
 */
public class AdvancedTimeout
    implements MethodRule
{
    //~ Instance/static variables .............................................

	private int classwide;
	private int method;

	private static boolean useClasswideTimeout;
	private static boolean useMethodTimeout;

	private static long beforeTime;
	private static long afterTime;


    //~ Constructors ..........................................................

    // ----------------------------------------------------------
	/**
	 * Creates a new timeout with the specified timeout options. If a value
	 * entered is not positive, that timeout condition will be ignored. The
	 * method timeout is a flat number applied to every method. The classwide
	 * timeout is a countdown that prevents further tests from running once
	 * the specified limit has expired.
	 *
	 * @param classwide  controls the timeout for the test class applied to.
	 * @param method     controls timeout on an individual method.
	 */
	public AdvancedTimeout(int classwide, int method)
	{
		this.classwide = classwide;
		useClasswideTimeout = (classwide > 0);
		this.method = method;
		useMethodTimeout = (method > 0);
		beforeTime = System.currentTimeMillis();
	}


    //~ Methods ...............................................................

    // ----------------------------------------------------------
	/**
	 * Applies the strictest remaining timeout.
	 *
	 * @base the statement to apply the timeout to.
	 */
	public Statement apply(Statement base, FrameworkMethod fm, Object target)
	{
		if (useClasswideTimeout)
		{
			afterTime = System.currentTimeMillis();
			int diff = (int) (afterTime - beforeTime);
			classwide = classwide - diff;
			if (useMethodTimeout)
			{
				return new FailOnTimeout(base, Math.min(classwide, method));
			}
			else
			{
				return new FailOnTimeout(base, classwide);
			}
		}
		else
		{
			if (useMethodTimeout)
			{
				return new FailOnTimeout(base, method);
			}
			else
			{
				return base;
			}
		}
	}
}