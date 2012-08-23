/*==========================================================================*\
 |  $Id: WOActionResponse.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
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
import java.net.HttpURLConnection;
import org.eclipse.core.runtime.IStatus;

//------------------------------------------------------------------------
/**
 * Instances of the class are returned by WOActionDispatcher to encapsulate both
 * the content stream and the status of an action request.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: WOActionResponse.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
 */
public class WOActionResponse
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new WOActionResponse object.
     *
     * @param status
     *            the status of the response
     * @param connection
     *            the connection from which the response came
     * @param stream
     *            the stream that contains the response content
     */
    public WOActionResponse(IStatus status, HttpURLConnection connection,
            InputStream stream)
    {
        this.status = status;
        this.connection = connection;
        this.stream = stream;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the status of the response.
     *
     * @return the status of the response
     */
    public IStatus status()
    {
        return status;
    }


    // ----------------------------------------------------------
    /**
     * Gets the content stream of the response.
     *
     * @return the content stream of the response
     */
    public InputStream stream()
    {
        return stream;
    }


    // ----------------------------------------------------------
    /**
     * Closes the connection from which the response came.
     */
    public void close()
    {
        connection.disconnect();
    }


    //~ Static/instance variables .............................................

    private IStatus status;
    private HttpURLConnection connection;
    private InputStream stream;
}
