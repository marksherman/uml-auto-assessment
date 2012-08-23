/*==========================================================================*\
 |  $Id: WOActionDispatcher.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
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

package org.webcat.oda.designer.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.webcat.oda.designer.DesignerActivator;

//------------------------------------------------------------------------
/**
 * A utility class to ease the process of sending a direct action request to the
 * Web-CAT server.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: WOActionDispatcher.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
 */
public class WOActionDispatcher
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new action dispatcher that sends requests to the server at the
     * specified URL.
     *
     * @param serverUrl
     *            the URL of the Web-CAT server
     */
    public WOActionDispatcher(String serverUrl)
    {
        this.serverUrl = serverUrl;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Sends an action to the Web-CAT server and returns its response.
     *
     * @param action
     *            the name of the action, either of the form "actionClass" or
     *            "actionClass/actionName"
     * @param sessionId
     *            the ID of a session started from a previous request, or null
     *            to use a new session
     * @param parameters
     *            query parameters to send to the action
     *
     * @return the response returned from the action
     */
    public WOActionResponse send(String action, String sessionId,
            Map<String, String> parameters)
    {
        HttpURLConnection connection = null;
        IStatus status = Status.OK_STATUS;
        InputStream stream = null;

        try
        {
            StringBuilder params = new StringBuilder();
            boolean firstParameter = true;

            if (sessionId != null)
            {
                params.append("wosid="); //$NON-NLS-1$
                params.append(URLEncoder.encode(sessionId, "UTF-8")); //$NON-NLS-1$

                firstParameter = false;
            }

            if (parameters != null)
            {
                for (String parameterName : parameters.keySet())
                {
                    if (!firstParameter)
                        params.append('&');

                    params.append(parameterName);
                    params.append('=');
                    params.append(URLEncoder.encode(parameters
                            .get(parameterName), "UTF-8")); //$NON-NLS-1$

                    firstParameter = false;
                }
            }

            byte[] paramBytes = params.toString().getBytes();

            URL actionUrl = new URL(serverUrl + "/wa/" + action); //$NON-NLS-1$

            connection = (HttpURLConnection) actionUrl.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST"); //$NON-NLS-1$
            connection.setRequestProperty("Content-Type", //$NON-NLS-1$
                    "application/x-www-form-urlencoded"); //$NON-NLS-1$
            connection.setRequestProperty("Content-Length", Integer //$NON-NLS-1$
                    .toString(paramBytes.length));
            connection.connect();

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(paramBytes);
            outputStream.flush();

            stream = connection.getInputStream();
        }
        catch (Exception e)
        {
            status = new Status(IStatus.ERROR, DesignerActivator.PLUGIN_ID, e
                    .getMessage(), e);
        }

        return new WOActionResponse(status, connection, stream);
    }


    //~ Static/instance variables .............................................

    private String serverUrl;
}
