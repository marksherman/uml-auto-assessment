/*==========================================================================*\
 |  $Id: BatchFeedbackLocation.java,v 1.2 2010/09/27 00:15:32 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2010 Virginia Tech
 |
 |  This file is part of Web-CAT.
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

package org.webcat.batchprocessor;

//-------------------------------------------------------------------------
/**
 * Describes the location of a batch feedback report among all reports.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:15:32 $
 */
public enum BatchFeedbackLocation
{
    //~ Constants .............................................................

    // ----------------------------------------------------------
    /**
     * The feedback section will appear before any sections with location
     * DEFAULT and TRAILING. Among multiple sections with location LEADING,
     * order is determined by their relative numbering in the
     * grading.properties file.
     */
    LEADING,


    // ----------------------------------------------------------
    /**
     * The feedback section will appear in the default order as described by
     * the numbering in the grading.properties file. This is the default
     * behavior.
     */
    DEFAULT,


    // ----------------------------------------------------------
    /**
     * The feedback section will appear after any sections with location
     * LEADING and DEFAULT. Among multiple sections with location TRAILING,
     * order is determined by their relative numbering in the
     * grading.properties file.
     */
    TRAILING;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public static BatchFeedbackLocation locationFromPropertyValue(String value)
    {
        BatchFeedbackLocation location = DEFAULT;

        if (value != null)
        {
            if (value.equalsIgnoreCase("leading"))
            {
                location = LEADING;
            }
            else if (value.equalsIgnoreCase("trailing"))
            {
                location = TRAILING;
            }
        }

        return location;
    }
}
