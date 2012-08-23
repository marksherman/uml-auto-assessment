/*==========================================================================*\
 |  $Id: ExitCalledException.java,v 1.1 2012/03/05 14:17:44 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2012 Virginia Tech
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

package student.testingsupport;

//-------------------------------------------------------------------------
/**
 *  A specialized security exception that indicates that System.exit() was
 *  called.  This exception type is used by the
 *  {@link ExitPreventingSecurityManager} to turn System.exit() calls into
 *  exceptions inside test cases.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.1 $, $Date: 2012/03/05 14:17:44 $
 */
public class ExitCalledException
    extends SecurityException
{
    //~ Fields ................................................................

    private static final long serialVersionUID = -6036641040049746935L;
    private int status;


    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create a new exception with the given status code--that is, the
     * parameter value passed to the System.exit() call this exception
     * represents.
     * @param status The parameter passed to System.exit().
     */
    public ExitCalledException(int status)
    {
        super("System.exit(" + status + ")");
        this.status = status;
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Get the exit status for the System.exit() call represented by this
     * exception.
     * @return The parameter passed to System.exit().
     */
    public int getStatus()
    {
        return status;
    }
}
