/*==========================================================================*\
 |  $Id: WebBotTask.java,v 1.2 2010/02/23 17:06:36 stedwar2 Exp $
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

package student.web;

//-------------------------------------------------------------------------
/**
 *  This class defines the common interface for all robot task objects.
 *  It defines two methods.  A concrete robot task class should define
 *  the {@link #task()} method to create and initialize the desired robot(s)
 *  and command them to behave.  Also, the concrete task class should
 *  define {@link #getRobot()} to return the robot that is used in the task.
 *  <p>
 *  A <code>WebBotTask</code> can be executed from the
 *  command-line using {@link RunWebBotTask}.
 *  </p>
 *
 *  @author Stephen Edwards
 *  @author Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/02/23 17:06:36 $
 */
public interface WebBotTask
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * This method encapsulates a sequence of instructions for
     * creating and commanding one or more robots (see {@link WebBot}).
     * Define it in any concrete class that implements this interface.
     */
    public void task();


    // ----------------------------------------------------------
    /**
     * This method provides access to the robot that carries out this
     * task.  In a concrete subclass implementing this interface, declare
     * your own instance variable to hold your robot and define this
     * method to return it.
     * @return The robot used by this task.
     */
    public WebBot getRobot();

}
