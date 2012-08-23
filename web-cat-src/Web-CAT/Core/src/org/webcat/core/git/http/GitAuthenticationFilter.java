/*==========================================================================*\
 |  $Id: GitAuthenticationFilter.java,v 1.1 2011/05/13 19:46:57 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2011 Virginia Tech
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

package org.webcat.core.git.http;

import org.webcat.core.Application;
import org.webcat.core.EntityRequestInfo;
import org.webcat.core.RepositoryProvider;
import org.webcat.core.User;
import org.webcat.core.http.BasicAuthenticationFilter;
import org.webcat.core.http.RequestFilterChain;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOEnterpriseObject;

//-------------------------------------------------------------------------
/**
 * A request filter that performs HTTP basic authentication on a Git repository
 * URL, to validate the user against the Web-CAT user database.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: aallowat $
 * @version $Revision: 1.1 $, $Date: 2011/05/13 19:46:57 $
 */
public class GitAuthenticationFilter extends BasicAuthenticationFilter
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Filters the request, only passing it further down the chain if
     * authentication was successful.
     *
     * @param request the request
     * @param response the response
     * @param filterChain the filter chain
     * @throws Exception if an error occurs
     */
    @Override
    public void filterRequest(WORequest request, WOResponse response,
            RequestFilterChain filterChain) throws Exception
    {
        String path = request.requestHandlerPath();
        requestInfo = EntityRequestInfo.fromRequestHandlerPath(path);

        super.filterRequest(request, response, filterChain);
    }


    // ----------------------------------------------------------
    @Override
    protected boolean isRequestValid(WORequest request)
    {
        return requestInfo != null;
    }


    // ----------------------------------------------------------
    @Override
    protected String realmForContext(WOContext context)
    {
        if (requestInfo != null)
        {
            return "Web-CAT Git repository "
                + requestInfo.entityName() + "/" + requestInfo.objectID()
                + " on " + Application.wcApplication().host();
        }
        else
        {
            return "Web-CAT Git repositories on "
                + Application.wcApplication().host();
        }
    }


    // ----------------------------------------------------------
    @Override
    protected boolean userHasAccess(User user)
    {
        EOEnterpriseObject object = requestInfo.requestedObject(
                user.editingContext());

        if (object instanceof RepositoryProvider)
        {
            RepositoryProvider provider = (RepositoryProvider) object;

            return (user.hasAdminPrivileges() ||
                    provider.userCanAccessRepository(user));
        }
        else
        {
            return false;
        }
    }


    //~ Static/instance variables .............................................

    private EntityRequestInfo requestInfo;
}
