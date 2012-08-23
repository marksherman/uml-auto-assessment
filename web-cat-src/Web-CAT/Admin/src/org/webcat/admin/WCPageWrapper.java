/*==========================================================================*\
 |  $Id: WCPageWrapper.java,v 1.2 2010/09/26 23:35:42 stedwar2 Exp $
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
import org.apache.log4j.Logger;
import org.webcat.core.*;

//-------------------------------------------------------------------------
/**
 * The page wrapper for direct-to-web pages.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/09/26 23:35:42 $
 */
public class WCPageWrapper
    extends WOComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new WCPageWrapper object.
     *
     * @param aContext The context to use
     */
    public WCPageWrapper(WOContext aContext)
    {
        super(aContext);
        log.debug("constructor");
    }


    //~ KVC Attributes (must be public) .......................................

    public String  stylesheet;
    public String  externalJavascript;
    public String  inlineHeaderContents;
    public boolean alreadyWrapped = false;
    public String  title;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        log.debug("appendToResponse()");
        // TODO Auto-generated method stub
        if (parent() != null && parent().parent() != null)
        {
            alreadyWrapped = true;
            title = "Target(s)";
        }
        else
        {
            title = ((Session)session()).tabs.selectedDescendant().label();
        }
        super.appendToResponse(response, context);
        if (log.isDebugEnabled())
        {
            log.debug("container = " + context.page().getClass().getName());
            WOComponent c = this.parent();
            String result = this.getClass().getName();
            while (c != null)
            {
                result += ", " + c.getClass().getName();
                c = c.parent();
            }
            log.debug("ancestors: " + result);
        }
    }


    // ----------------------------------------------------------
    @Override
    public void awake()
    {
        if (currentTab != null)
        {
            reselectCurrentTab();
        }
        super.awake();
        currentTab();
    }


    // ----------------------------------------------------------
    /**
     * Get the selected tab that corresponds to this component's page.
     * @return this page's navigation tab
     */
    public TabDescriptor currentTab()
    {
        if (currentTab == null)
        {
            currentTab = ((Session)session()).tabs.selectedDescendant();
        }
        return currentTab;
    }


    // ----------------------------------------------------------
    /**
     * Set the tab that corresponds to this component's page.
     * @param current this page's navigation tab
     */
    public void setCurrentTab(TabDescriptor current)
    {
        currentTab = current;
    }


    // ----------------------------------------------------------
    public void reselectCurrentTab()
    {
        if (currentTab() != null)
        {
            currentTab().select();
        }
    }


    //~ Instance/static variables .............................................

    private TabDescriptor currentTab;

    static Logger log = Logger.getLogger( WCPageWrapper.class );
}
