/*==========================================================================*\
 |  $Id: DeptOutcomesPage.java,v 1.1 2010/05/11 14:51:50 aallowat Exp $
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
import com.webobjects.foundation.NSArray;

//-------------------------------------------------------------------------
/**
 *  Represents a standard Web-CAT page that has not yet been implemented
 *  (is "to be defined").
 *
 *  @author Stephen Edwards
 *  @author Last changed by $Author: aallowat $
 *  @version $Revision: 1.1 $, $Date: 2010/05/11 14:51:50 $
*/
public class DeptOutcomesPage
    extends BasePage
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new TBDPage object.
     *
     * @param context The context to use
     */
    public DeptOutcomesPage( WOContext context )
    {
        super( context );
    }


    //~ KVC Attributes (must be public) .......................................

    public NSArray<ProgramOutcome> outcomes;
    public ProgramOutcome outcome;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        if (outcomes == null)
        {
            outcomes = ProgramOutcome.objectsMatchingQualifier(localContext(),
                ProgramOutcome.program.is(program),
                ProgramOutcome.microLabel.ascs());
        }
        super.appendToResponse(response, context);
    }
}
