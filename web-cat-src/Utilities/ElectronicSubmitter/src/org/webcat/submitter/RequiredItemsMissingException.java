/*==========================================================================*\
 |  $Id: RequiredItemsMissingException.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
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

import java.io.IOException;
import java.util.Arrays;

//--------------------------------------------------------------------------
/**
 * Thrown by the submitter if the user tries to submit a project that does not
 * include all of the required files.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public class RequiredItemsMissingException extends IOException
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new instance of this exception.
     *
     * @param files an array of Strings representing the patterns that could
     *     not be matched
     */
    public RequiredItemsMissingException(String[] files)
    {
        missingFiles = files;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets an array of required file patterns that could not be matched during
     * the submission process.
     *
     * @return an array of Strings representing the missing files
     */
    public String[] getMissingFiles()
    {
        return missingFiles;
    }


    // ----------------------------------------------------------
    /**
     * @see Throwable#getMessage()
     */
    @Override
    public String getMessage()
    {
        return "No files satisfying the following patterns could be found: "
            + Arrays.toString(missingFiles);
    }


    //~ Static/instance variables .............................................

    private static final long serialVersionUID = 1L;

    /* The required file patterns that could not be matched. */
    private String[] missingFiles;
}
