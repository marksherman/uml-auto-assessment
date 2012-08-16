/*==========================================================================*\
 |  $Id: StartSession.java,v 1.2 2010/09/27 00:54:06 stedwar2 Exp $
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

package org.webcat.webapi;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.appserver.WOSession;
import org.apache.log4j.Logger;

//-------------------------------------------------------------------------
/**
 * XML Response page for webapi/startSession requests.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:54:06 $
 */
public class StartSession
    extends XmlResponsePage
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new page.
     *
     * @param context The page's context
     */
    public StartSession(WOContext context)
    {
        super(context);
    }

    public void appendToResponse(WOResponse response, WOContext context)
    {
        WOSession session = session();
        if (log.isDebugEnabled())
        {
            log.debug("session = "
                + ((session == null) ? "null" : session.sessionID() ));
        }
        super.appendToResponse(response, context);
    }

    //~ Instance/static variables .............................................

    static Logger log = Logger.getLogger(StartSession.class);
}
