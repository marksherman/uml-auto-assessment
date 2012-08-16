/*==========================================================================*\
 |  $Id: Institutions.java,v 1.2 2010/09/27 00:54:06 stedwar2 Exp $
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

import org.webcat.core.AuthenticationDomain;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;

//-------------------------------------------------------------------------
/**
 * XML Response page for webapi/institutions requests.
 *
 * @author  Stephen Edwards
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:54:06 $
 */
public class Institutions
    extends XmlResponsePage
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new page.
     *
     * @param context The page's context
     */
    public Institutions(WOContext context)
    {
        super(context);
    }


    //~ KVC Properties ........................................................

    public AuthenticationDomain          anInstitution;
    public NSArray<AuthenticationDomain> institutions;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        institutions = AuthenticationDomain.authDomains();
        super.appendToResponse(response, context);
    }

    // ----------------------------------------------------------
    public String symbolicName()
    {
        String result = anInstitution.propertyName();
        int pos = result.indexOf('.');
        if (pos >= 0)
        {
            result = result.substring(pos + 1);
        }
        return result;
    }
}
