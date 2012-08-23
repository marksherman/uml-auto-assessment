/*==========================================================================*\
 |  $Id: WCListPageTemplate.java,v 1.2 2010/09/26 23:35:42 stedwar2 Exp $
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
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.*;
import com.webobjects.directtoweb.*;

// -------------------------------------------------------------------------
/**
 * The template for D2W list pages in Web-CAT.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/09/26 23:35:42 $
 */
public class WCListPageTemplate
    extends er.directtoweb.pages.ERD2WListPage
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new WCListPageTemplate object.
     *
     * @param context The page's context
     */
    public WCListPageTemplate(WOContext context)
    {
        super(context);
    }


    //~ Methods ...............................................................

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
    public void setLocalContext(D2WContext arg0)
    {
        super.setLocalContext(arg0);
        if (setUpSortOrdering)
        {
          // override default sort ordering with a new one from the
          // d2w properties
          NSArray<EOSortOrdering> sortOrderings = sortOrderings();
            if (sortOrderings != null)
            {
                displayGroup().setSortOrderings(sortOrderings);
            }
            setUpSortOrdering = false;
        }
    }


    // ----------------------------------------------------------
    public NSArray<EOSortOrdering> sortOrderings()
    {
        NSArray<EOSortOrdering> sortOrderings = null;
        if (userPreferencesCanSpecifySorting())
        {
            @SuppressWarnings("unchecked")
            NSArray<EOSortOrdering> theOrderings = (NSArray<EOSortOrdering>)
                userPreferencesValueForPageConfigurationKey("sortOrdering");
            sortOrderings = theOrderings;
            if (log.isDebugEnabled())
                log.debug(
                    "Found sort Orderings in user prefs " + sortOrderings);
        }
        if (sortOrderings == null)
        {
            @SuppressWarnings("unchecked")
            NSArray<EOSortOrdering> theOrderings = (NSArray<EOSortOrdering>)
                d2wContext().valueForKey("defaultSortOrdering");
            sortOrderings = theOrderings;
            if (log.isDebugEnabled())
                log.debug("Found sort Orderings in rules " + sortOrderings);
        }
        return sortOrderings;
    }


    // ----------------------------------------------------------
    public String cssClassForRow()
    {
        String result = "o";
        _rowFlip = !_rowFlip;
        if (alternateRowColor() && _rowFlip)
        {
            result = "e";
        }
        if (isSelecting() && selectedObjects().containsObject(object()))
        {
            result += " selected";
        }
        return result;
    }


    //~ Instance/static variables .............................................
    private boolean setUpSortOrdering = true;
}
