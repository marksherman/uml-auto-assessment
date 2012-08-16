/*==========================================================================*\
 |  $Id: reportTemplate.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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

package org.webcat.reporter.actions;

import org.webcat.core.DirectAction;
import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WORequest;

// ------------------------------------------------------------------------
/**
 * Acts as an external portal to the report template repository. Unique
 * identifiers for templates are URLs based on this direct action and can be
 * visited by a user to display useful information about and grant other access
 * to a template.
 *
 * @author Tony Allevato
 * @version $Id: reportTemplate.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class reportTemplate extends DirectAction
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     *
     * @param request The incoming request
     */
    public reportTemplate(WORequest request)
    {
        super(request);
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    public WOActionResults reportTemplateAction()
    {
        // TODO: implement a useful portal
        return null;
    }
}
