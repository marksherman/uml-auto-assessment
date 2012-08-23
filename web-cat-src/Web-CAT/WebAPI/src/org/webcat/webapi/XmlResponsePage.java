/*==========================================================================*\
 |  $Id: XmlResponsePage.java,v 1.2 2010/09/27 00:54:06 stedwar2 Exp $
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
import er.extensions.components.ERXComponent;
import org.webcat.core.Application;
import org.webcat.core.Session;

//-------------------------------------------------------------------------
/**
 * A common base class for all of the XML response pages in this subsystem.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:54:06 $
 */
public class XmlResponsePage
    extends ERXComponent
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new page.
     *
     * @param context The page's context
     */
    public XmlResponsePage(WOContext context)
    {
        super(context);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Returns the current session object as the application-specific
     * subtype <code>Session</code>.  This avoids the need for writing a
     * downcast on each <code>session</code> call.
     *
     * @return The current session
     */
    public Session session()
    {
        return (Session)super.session();
    }


    // ----------------------------------------------------------
    /**
     * Returns the current application object as the application-specific
     * subtype <code>Application</code>.  This avoids the need for
     * writing the downcast for each <code>application</code> call.
     *
     * @return The current application
     */
    public Application application()
    {
        return (Application)super.application();
    }
}
