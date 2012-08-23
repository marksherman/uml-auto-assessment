/*==========================================================================*\
 |  $Id: XmlResponseWrapper.java,v 1.2 2010/09/27 00:54:06 stedwar2 Exp $
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

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSTimestamp;
import org.webcat.core.Session;

//-------------------------------------------------------------------------
/**
 * Sets the MIME type for XML and places an XML header at the top of the
 * output.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:54:06 $
 */
public class XmlResponseWrapper
    extends WOComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new page wrapper.
     *
     * @param context The page's context
     */
    public XmlResponseWrapper(WOContext context)
    {
        super(context);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Determine whether or not to show the timeout attribute on the
     * web-cat-response tag.
     * @return true if the timeout should be shown.
     */
    public boolean showTimeout()
    {
        return hasSession() && ((Session)session()).user() != null;
    }


    // ----------------------------------------------------------
    /**
     * Returns when this page's session will expire.
     * @return a Unix-style timestamp in milliseconds since
     * January 1, 1970, 00:00:00 GMT.
     */
    public long sessionExpireTime()
    {
        if (hasSession())
        {
            return (new NSTimestamp()).getTime()         // now
                + (long)(session().timeOut() * 1000);    // + session timeout
        }
        else
        {
            return 0L;
        }
    }
}
