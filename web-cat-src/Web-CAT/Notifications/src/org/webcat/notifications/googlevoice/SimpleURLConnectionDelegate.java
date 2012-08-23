/*==========================================================================*\
 |  $Id: SimpleURLConnectionDelegate.java,v 1.1 2010/05/11 14:51:35 aallowat Exp $
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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpResponse;

//-------------------------------------------------------------------------
/**
 * A basic implementation of the {@link URLConnectionDelegate} that collects
 * data as it comes along the pipe and stores it in memory. Implementors need
 * only override {@link URLConnectionDelegate#didFinishLoading()} and call the
 * {@link SimpleURLConnectionDelegate#responseString()} method to retrieve the
 * string response from the request, and the
 * {@link SimpleURLConnectionDelegate#statusCode()} method to retrieve the
 * response status code.
 *
 * @author  Tony Allevato
 * @version $Id: SimpleURLConnectionDelegate.java,v 1.1 2010/05/11 14:51:35 aallowat Exp $
 */
public abstract class SimpleURLConnectionDelegate extends URLConnectionDelegate
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a new simple URL connection delegate.
     */
    public SimpleURLConnectionDelegate()
    {
        byteStream = new ByteArrayOutputStream();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Called when the connection first receives a response header.
     *
     * @param response the response header
     */
    public void didReceiveResponse(HttpResponse response)
    {
        statusCode = response.getStatusLine().getStatusCode();
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
        byteStream.write(data, 0, length);
    }


    // ----------------------------------------------------------
    /**
     * Gets the response from this request as a string.
     *
     * @return the string content of the response
     */
    public String responseString()
    {
        if (responseString == null)
        {
            try
            {
                responseString = byteStream.toString("UTF-8");
            }
            catch (UnsupportedEncodingException e)
            {
                // Do nothing.
            }
        }

        return responseString;
    }


    // ----------------------------------------------------------
    /**
     * Gets the status code of the response.
     *
     * @return the status code
     */
    public int statusCode()
    {
        return statusCode;
    }


    //~ Static/instance variables .............................................

    private int statusCode;
    private ByteArrayOutputStream byteStream;
    private String responseString;
}
