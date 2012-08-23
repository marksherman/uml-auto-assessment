/*==========================================================================*\
 |  $Id: SubmissionParserErrorHandler.java,v 1.2 2010/05/14 14:45:43 aallowat Exp $
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

package org.webcat.submitter.internal;

import java.util.ArrayList;
import java.util.List;
import org.webcat.submitter.TargetParseError;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

//--------------------------------------------------------------------------
/**
 * This error handler collects all errors that occur during the parsing of the
 * submission definitions file so they can be thrown as a single exception if
 * any occur.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/05/14 14:45:43 $
 */
public class SubmissionParserErrorHandler implements ErrorHandler
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new submission parser error handler.
     */
    public SubmissionParserErrorHandler()
    {
        errors = new ArrayList<TargetParseError>();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Handles a warning that occurs during parsing.
     *
     * @param e an exception that describes the warning
     */
    public void warning(SAXParseException e)
    {
        // Do nothing.
    }


    // ----------------------------------------------------------
    /**
     * Handles an error that occurs during parsing. Errors are stored in a list
     * so they can be communicated back to the user in a single exception after
     * parsing is complete.
     *
     * @param e an exception that describes the error
     */
    public void error(SAXParseException e)
    {
        TargetParseError error = new TargetParseError(e.getLineNumber(), e
                .getColumnNumber(), e.getMessage());
        errors.add(error);
    }


    // ----------------------------------------------------------
    /**
     * Handles a fatal error that occurs during parsing. Errors are stored in a
     * list so they can be communicated back to the user in a single exception
     * after parsing is complete.
     *
     * @param e an exception that describes the error
     */
    public void fatalError(SAXParseException e)
    {
        TargetParseError error = new TargetParseError(e.getLineNumber(), e
                .getColumnNumber(), e.getMessage());
        errors.add(error);
    }


    // ----------------------------------------------------------
    /**
     * Gets the list of errors that occurred during parsing.
     *
     * @return An array of TargetParseError objects, or null if no errors
     *     occurred.
     */
    public TargetParseError[] getErrors()
    {
        if (errors.size() == 0)
        {
            return null;
        }
        else
        {
            TargetParseError[] array = new TargetParseError[errors.size()];
            for (int i = 0; i < errors.size(); i++)
            {
                array[i] = errors.get(i);
            }

            return array;
        }
    }


    //~ Static/instance variables .............................................

    /* Keeps track of all the errors encountered during parsing. */
    private List<TargetParseError> errors;
}
