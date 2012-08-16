/*==========================================================================*\
 |  $Id: TargetParseException.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
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
 * This exception class collects all the errors that occur during the parsing
 * of the submission definitions file. Once parsing is complete, the user of
 * the submitter can access the list of errors, if any, from the exception.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public class TargetParseException extends SubmissionTargetException
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new exception object with the specified errors.
     *
     * @param errors an array of TargetParseError objects that describe the
     *     errors that occurred during parsing.
     */
    public TargetParseException(TargetParseError[] errors)
    {
        this.errors = errors;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the list of errors that occurred during parsing.
     *
     * @return an array of TargetParseError objects, or null if no errors
     *     occurred.
     */
    public TargetParseError[] getErrors()
    {
        return errors;
    }


    //~ Static/instance variables .............................................

    private static final long serialVersionUID = 1L;

    /* The list of errors that occurred during parsing. */
    private TargetParseError[] errors;
}
