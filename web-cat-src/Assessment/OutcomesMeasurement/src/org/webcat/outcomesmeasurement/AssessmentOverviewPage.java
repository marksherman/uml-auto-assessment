/*==========================================================================*\
 |  $Id: AssessmentOverviewPage.java,v 1.1 2010/05/11 14:51:50 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2009 Virginia Tech
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

package org.webcat.outcomesmeasurement;

import com.webobjects.appserver.*;
import org.webcat.core.*;

//-------------------------------------------------------------------------
/**
 *  An entry form for all of the assessment measures associated with a
 *  given course offering.
 *
 *  @author Stephen Edwards
 *  @author Last changed by $Author: aallowat $
 *  @version $Revision: 1.1 $, $Date: 2010/05/11 14:51:50 $
*/
public class AssessmentOverviewPage
    extends WCComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     *
     * @param context The context to use
     */
    public AssessmentOverviewPage(WOContext context)
    {
        super(context);
    }
}
