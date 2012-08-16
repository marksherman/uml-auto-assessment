/*==========================================================================*\
 |  $Id: AdminStatusPage.java,v 1.3 2010/09/26 23:35:42 stedwar2 Exp $
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
import er.extensions.appserver.ERXApplication;

//-------------------------------------------------------------------------
/**
 * Represents a standard Web-CAT page that has not yet been implemented
 * (is "to be defined").
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.3 $, $Date: 2010/09/26 23:35:42 $
 */
public class AdminStatusPage
    extends WCComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new AdminStatusPage object.
     *
     * @param context The context to use
     */
    public AdminStatusPage(WOContext context)
    {
        super(context);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public WOComponent gracefulShutdown()
    {
        ERXApplication.erxApplication().startRefusingSessions();
        return null;
    }


    // ----------------------------------------------------------
    public WOComponent dieNow()
    {
        ERXApplication.erxApplication().killInstance();
        return null;
    }


    // ----------------------------------------------------------
    public boolean canRestart()
    {
        return net.sf.webcat.WCServletAdaptor.getInstance() == null
            || Application.configurationProperties()
                .stringForKey("coreKillAction") != null;
    }
}
