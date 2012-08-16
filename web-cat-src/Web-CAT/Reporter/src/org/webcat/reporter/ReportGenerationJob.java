/*==========================================================================*\
 |  $Id: ReportGenerationJob.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
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

package org.webcat.reporter;

import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;

// -------------------------------------------------------------------------
/**
 * TODO: place a real description here.
 *
 * @author
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/05/11 14:51:48 $
 */
public class ReportGenerationJob
    extends _ReportGenerationJob
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new ReportGenerationJob object.
     */
    public ReportGenerationJob()
    {
        super();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * A convenience method to create and insert a new ReportGenerationJob with
     * default values; the current time as the enqueueTime, not cancelled, and
     * not ready to run. To run the job, the client will need to call
     * {@link #setIsReady(boolean)} on the job and save those changes to the
     * editing context.
     *
     * @param editingContext the editing context
     * @return a new ReportGenerationJob
     */
    public static ReportGenerationJob create(EOEditingContext editingContext)
    {
        return create(editingContext, new NSTimestamp(), false, false);
    }
}
