/*==========================================================================*\
 |  $Id: WCMenuHeader.java,v 1.2 2010/09/26 23:35:42 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2008 Virginia Tech
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

import org.webcat.core.*;
import com.webobjects.appserver.*;
import com.webobjects.directtoweb.*;
import com.webobjects.foundation.*;

//-------------------------------------------------------------------------
/**
 * The menu header for all of our direct-to-web pages.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/09/26 23:35:42 $
 */
public class WCMenuHeader
    extends WOComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new WCInspectPageTemplate object.
     *
     * @param aContext The context to use
     */
    public WCMenuHeader(WOContext aContext)
    {
        super(aContext);
    }


    //~ KVC Attributes (must be public) .......................................

    public String  entityNameInList;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public String manipulatedEntityName()
    {
        if (manipulatedEntityName == null)
        {
            WOComponent currentPage = context().page();
            manipulatedEntityName = D2W.entityNameFromPage(currentPage);
        }
        return manipulatedEntityName;
    }


    // ----------------------------------------------------------
    public void setManipulatedEntityName(String newValue)
    {
        manipulatedEntityName = newValue;
    }


    // ----------------------------------------------------------
    public NSArray<?> visibleEntityNames()
    {
        return D2W.factory().visibleEntityNames(session());
    }


    // ----------------------------------------------------------
    public WOComponent findEntityAction()
    {
        QueryPageInterface newQueryPage = D2W.factory()
            .queryPageForEntityNamed(manipulatedEntityName, session());
        return (WOComponent)newQueryPage;
    }


    // ----------------------------------------------------------
    public WOComponent newObjectAction()
    {
        WOComponent nextPage = null;
        try
        {
            EditPageInterface epi = D2W.factory()
                .editPageForNewObjectWithEntityNamed(
                    manipulatedEntityName, session());
            epi.setNextPage(context().page());
            nextPage = (WOComponent)epi;
        }
        catch (IllegalArgumentException e)
        {
            ErrorPageInterface epf = D2W.factory().errorPage(session());
            epf.setMessage(e.toString());
            epf.setNextPage(context().page());
            nextPage = (WOComponent)epf;
        }
        return nextPage;
    }


    // ----------------------------------------------------------
    public WOComponent queryAllAction()
    {
        return D2W.factory().defaultPage(session());
    }


    // ----------------------------------------------------------
    public WOComponent homeAction()
    {
        return pageWithName(
            ((Session)session()).tabs.selectDefault().pageName());
    }


    //~ Instance/static variables .............................................

    private String manipulatedEntityName;
}
