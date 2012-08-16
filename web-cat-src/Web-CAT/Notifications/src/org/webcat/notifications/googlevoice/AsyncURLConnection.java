/*==========================================================================*\
 |  $Id: AsyncURLConnection.java,v 1.2 2011/12/25 21:18:26 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2009-2011 Virginia Tech
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
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;

//-------------------------------------------------------------------------
/**
 * A helper class for starting an asynchronous HTTP request. A delegate is used
 * to receive events as data comes across the pipe.
 *
 * @author  Tony Allevato
 * @author  Last changed by: $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2011/12/25 21:18:26 $
 */
public class AsyncURLConnection
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a new URL connection and executes it immediately.
     *
     * @param request the HTTP request
     * @param delegate the delegate
     */
    public AsyncURLConnection(HttpUriRequest request,
                              URLConnectionDelegate delegate)
    {
        this(request, delegate, true);
    }


    // ----------------------------------------------------------
    /**
     * Initializes a new URL connection.
     *
     * @param request the HTTP request
     * @param delegate the delegate
     * @param startImmediately true to start the request immediately; false to
     *     not start it until the {@link #start()} method is called
     */
    public AsyncURLConnection(HttpUriRequest request,
                              URLConnectionDelegate delegate,
                              boolean startImmediately)
    {
        this.request = request;
        this.delegate = delegate;

        client = new DefaultHttpClient();
        client.setRedirectHandler(new DefaultRedirectHandler());
        client.getParams().setBooleanParameter(
                "http.protocol.expect-continue", false);

        if (startImmediately)
        {
            start();
        }
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Asynchronously executes the request if it has not already been executed.
     */
    public void start()
    {
        if (thread == null)
        {
            thread = new RequestThread();
            thread.start();
        }
    }


    // ----------------------------------------------------------
    /**
     * Synchronously starts the request and waits for it to complete.
     */
    public void startAndWait()
    {
        start();

        try
        {
            thread.join();
        }
        catch (InterruptedException e)
        {
            // Do nothing.
        }
    }


    //~ Private classes .......................................................

    // ----------------------------------------------------------
    /**
     * A background thread that executes the request and reads its response.
     */
    private class RequestThread extends Thread
    {
        // ----------------------------------------------------------
        public RequestThread()
        {
            super(RequestThread.class.getName());
        }


        //~ Methods ...........................................................

        // ----------------------------------------------------------
        @Override
        public void run()
        {
            byte[] buffer = new byte[BUFFER_SIZE];

            try
            {
                HttpResponse response = client.execute(request);

                if (delegate != null)
                {
                    delegate.didReceiveResponse(response);
                }

                HttpEntity entity = response.getEntity();

                InputStream stream = entity.getContent();
                int bytesRead;

                while ((bytesRead = stream.read(buffer)) != -1)
                {
                    if (delegate != null && bytesRead > 0)
                    {
                        delegate.didReceiveData(buffer, bytesRead);
                    }
                }

                stream.close();
                entity.consumeContent();

                if (delegate != null)
                {
                    delegate.didFinishLoading();
                }
            }
            catch (IOException e)
            {
                if (delegate != null)
                {
                    delegate.didFailWithException(e);
                }
            }
        }


        //~ Static/instance variables .........................................

        private static final int BUFFER_SIZE = 32768;
    }


    //~ Static/instance variables .............................................

    private RequestThread thread;
    private DefaultHttpClient client;
    private HttpUriRequest request;
    private URLConnectionDelegate delegate;
}
