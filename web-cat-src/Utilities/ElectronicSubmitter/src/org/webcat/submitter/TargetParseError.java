/*==========================================================================*\
 |  $Id: TargetParseError.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
 |
 |  This file is part of Web-CAT Electronic Submitter.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU General Public License as published by
 |  the Free Software Foundation; either version 2 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU General Public License along
 |  with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.submitter;

//--------------------------------------------------------------------------
/**
 * Encapsulates information about a parsing error in the submission definitions
 * file. If any XML errors occurred during parsing of the submission target
 * definitions, the {@link TargetParseException} will contain an array of these
 * to describe what went wrong.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public class TargetParseError
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new instance of the error object with the specified line and
     * column numbers and description.
     *
     * @param line the line number at which the error occurred
     * @param column the column at which the error occurred
     * @param message the description of the error that occurred
     */
    public TargetParseError(int line, int column, String message)
    {
        this.line = line;
        this.column = column;
        this.message = message;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the line number at which the error occurred.
     *
     * @return an integer representing the line number
     */
    public int getLine()
    {
        return line;
    }


    // ----------------------------------------------------------
    /**
     * Gets the column number at which the error occurred.
     *
     * @return an integer representing the column number
     */
    public int getColumn()
    {
        return column;
    }


    // ----------------------------------------------------------
    /**
     * Gets the description of the error that occurred.
     *
     * @return a String containing the description
     */
    public String getMessage()
    {
        return message;
    }


    // ----------------------------------------------------------
    /**
     * Produces a human-readable error message from this error object.
     *
     * @return a String containing the formatted error message
     */
    public String toString()
    {
        return "Line " + line + ", column " + column + ": " + message;
    }


    //~ Static/instance variables .............................................

    /* The line number at which the error occurred. */
    private int line;

    /* The column at which the error occurred. */
    private int column;

    /* The description of the error that occurred. */
    private String message;
}
