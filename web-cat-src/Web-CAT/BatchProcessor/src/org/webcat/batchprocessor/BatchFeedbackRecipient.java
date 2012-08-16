/*==========================================================================*\
 |  $Id: BatchFeedbackRecipient.java,v 1.2 2010/09/27 00:15:32 stedwar2 Exp $
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
 * Describes which users are allowed to see a batch feedback report.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:15:32 $
 */
public enum BatchFeedbackRecipient
{
    //~ Constants .............................................................

    // ----------------------------------------------------------
    /**
     * The feedback report is visible only to the user who owns the batch job
     * that generated it.
     */
    OWNER,


    // ----------------------------------------------------------
    /**
     * The feedback report is visible only to administrators, but not to the
     * owner of the batch job.
     */
    ADMINISTRATOR,


    // ----------------------------------------------------------
    /**
     * The feedback report is visible to both the owner of the job and the
     * administrators.
     */
    ALL;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public static BatchFeedbackRecipient recipientFromPropertyValue(
            String value)
    {
        BatchFeedbackRecipient recipient = OWNER;
        if (value == null
                || value.equalsIgnoreCase("owner"))
        {
            recipient = OWNER;
        }
        else if (value.equalsIgnoreCase("admin")
                || value.equalsIgnoreCase("administrator"))
        {
            recipient = ADMINISTRATOR;
        }
        else if (value.equalsIgnoreCase("all"))
        {
            recipient = ALL;
        }

        return recipient;
    }
}
