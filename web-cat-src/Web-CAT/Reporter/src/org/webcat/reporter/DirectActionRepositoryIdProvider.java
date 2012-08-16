/*==========================================================================*\
 |  $Id: DirectActionRepositoryIdProvider.java,v 1.2 2010/10/15 00:47:37 stedwar2 Exp $
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

package org.webcat.reporter;

import org.webcat.core.Application;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSMutableDictionary;
import er.extensions.appserver.ERXWOContext;

// ------------------------------------------------------------------------
/**
 * An implementation of {@link IRepositoryIdProvider} that generated IDs using a
 * direct action URL parameterized with the database ID of a report template.
 *
 * @author Tony Allevato
 * @version $Id: DirectActionRepositoryIdProvider.java,v 1.2 2010/10/15 00:47:37 stedwar2 Exp $
 */
public class DirectActionRepositoryIdProvider implements IRepositoryIdProvider
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new repository ID provider that uses the specified context to
     * build URLs.
     *
     * @param context
     *            the context to use to generate URLs
     */
    public DirectActionRepositoryIdProvider(WOContext context)
    {
        this.context = context;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets a repository ID for the specified report template.
     *
     * @param template
     *            the report template
     * @return the repository ID for the template
     */
    public String idForReportTemplate(ReportTemplate template)
    {
        String templateId = template.id().toString();
        String params = "id=" + templateId;

        String handler = Application.application()
            .directActionRequestHandlerKey();

        return Application.completeURLWithRequestHandlerKey(
            context, handler, actionName,
            params, false, 0);
    }


    //~ Static/instance variables .............................................

    private static final String actionName = "reportTemplate";

    private WOContext context;
}
