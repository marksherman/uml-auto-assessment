/*==========================================================================*\
 |  $Id: URLConnectionDelegate.java,v 1.1 2010/05/11 14:51:35 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2009 Virginia Tech
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

package org.webcat.notifications.googlevoice;

import java.io.IOException;
import org.apache.http.HttpResponse;

//-------------------------------------------------------------------------
/**
 * A class that receives notifications during an asynchronous HTTP request.
 *
 * @author  Tony Allevato
 * @version $Id: URLConnectionDelegate.java,v 1.1 2010/05/11 14:51:35 aallowat Exp $
 */
public abstract class URLConnectionDelegate
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Called when the connection first receives a response header.
     *
     * @param response the response header
     */
    public void didReceiveResponse(HttpResponse response)
    {
        // Default implementation does nothing.
    }


    // ----------------------------------------------------------
    /**
     * Called when more data is available in the response.
     *
     * @param data the buffer containing the response data
     * @param length the number of bytes currently in the buffer
     */
    public void didReceiveData(byte[] data, int length)
    {
        // Default implementation does nothing.
    }


    // ----------------------------------------------------------
    /**
     * Called when the connection fails.
     *
     * @param e an exception describing the failure
     */
    public void didFailWithException(IOException e)
    {
        // Default implementation does nothing.
    }


    // ----------------------------------------------------------
    /**
     * Called when the response is finished loading.
     */
    public void didFinishLoading()
    {
        // Default implementation does nothing.
    }
}
