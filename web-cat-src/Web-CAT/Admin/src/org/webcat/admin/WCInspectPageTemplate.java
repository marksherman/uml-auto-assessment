/*==========================================================================*\
 |  $Id: WCInspectPageTemplate.java,v 1.2 2010/09/26 23:35:42 stedwar2 Exp $
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

package org.webcat.admin;

import com.webobjects.appserver.*;

//-------------------------------------------------------------------------
/**
 * An inspect page generated by the direct-to-web template engine.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/09/26 23:35:42 $
 */
public class WCInspectPageTemplate
    extends er.directtoweb.pages.templates.ERD2WInspectPageTemplate
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new WCInspectPageTemplate object.
     *
     * @param aContext The context to use
     */
    public WCInspectPageTemplate(WOContext aContext)
    {
        super(aContext);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        rowFlip = true;
        super.appendToResponse(response, context);
    }


    // ----------------------------------------------------------
    /**
     * Returns the alternate row color for tables on this page.
     * Because of the way the alternating row info is computed,
     * this is really the color of the "first" row.  The d2w.d2wmodel
     * rule file sets the table background to "#eeeeee" for list
     * tasks to create the alternating effect.
     *
     * @return The color as a string
     */
    public String backgroundColorForTableDark()
    {
        return "white";
    }


    // ----------------------------------------------------------
    public String backgroundColorForRow()
    {
        rowFlip = !rowFlip;
        if (rowFlip || !alternateRowColor())
        {
            return backgroundColorForTable();
        } else
        {
            return backgroundColorForTableDark();
        }
    }


    // ----------------------------------------------------------
    public void setBackgroundColorForRow(String value)
    {
        // This isn't a settable attribute, so do nothing
    }


    // ----------------------------------------------------------
    public String cssClassForRow()
    {
        rowFlip = !rowFlip;
        if (rowFlip && alternateRowColor())
        {
            return "e";
        } else
        {
            return "o";
        }
    }


    //~ Instance/static variables .............................................

    private boolean rowFlip = true;
}
