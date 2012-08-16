/*==========================================================================*\
 |  $Id: HttpProtocol.java,v 1.2 2010/09/14 18:13:30 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
 |
 |  This file is part of Web-CAT Electronic Submitter.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU General Public License as published by
 |  the Free Software Foundation; either version 2 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU General Public License along
 |  with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.submitter.internal.protocols;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.webcat.submitter.ILongRunningTask;
import org.webcat.submitter.IProtocol;
import org.webcat.submitter.IStringEncoder;
import org.webcat.submitter.SubmissionManifest;
import org.webcat.submitter.URLStringEncoder;
import org.webcat.submitter.internal.utility.MultipartBuilder;
import org.webcat.submitter.targets.AssignmentTarget;

//--------------------------------------------------------------------------
/**
 * A protocol for the "http" URI scheme that supports sending the submitted file
 * as part of an HTTP POST request to a remote server. This protocol generates
 * an HTTP response that can be displayed to the user in a browser window.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/09/14 18:13:30 $
 */
public class HttpProtocol implements IProtocol
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * @see IProtocol#submit(SubmissionManifest, ILongRunningTask)
     */
    public void submit(SubmissionManifest manifest, ILongRunningTask task)
    throws IOException
    {
    	IStringEncoder encoder = new URLStringEncoder();

        URL url = manifest.getResolvedTransport(encoder).toURL();

        HttpURLConnection connection =
            (HttpURLConnection) url.openConnection();
        MultipartBuilder multipart = new MultipartBuilder(connection);

        AssignmentTarget asmt = manifest.getAssignment();

        Set<Map.Entry<String, String>> transportParams = asmt
                .getTransportParameters().entrySet();

        for (Iterator<Map.Entry<String, String>> it = transportParams.
                iterator(); it.hasNext(); )
        {
            Map.Entry<String, String> entry = it.next();
            String paramName = entry.getKey();
            String paramValue = entry.getValue();
            String convertedValue = manifest.resolveParameters(
            		paramValue, null);

            if (paramName.startsWith("$file."))
            {
                String key = paramName.substring(6);
                OutputStream outStream = multipart.beginWriteFile(key,
                        convertedValue, "application/octet-stream");

                manifest.packageContentsIntoStream(outStream, task, null);

                multipart.endWriteFile();
            }
            else
            {
                multipart.writeParameter(paramName, convertedValue);
            }
        }

        multipart.close();

        InputStream inStream = connection.getInputStream();
        StringBuffer buffer = new StringBuffer();

        int nextChar = inStream.read();
        while (nextChar != -1)
        {
            buffer.append((char)nextChar);
            nextChar = inStream.read();
        }

        inStream.close();

        response = buffer.toString();
    }


    // ----------------------------------------------------------
    /**
     * @see IProtocol#hasResponse()
     */
    public boolean hasResponse()
    {
        return true;
    }


    // ----------------------------------------------------------
    /**
     * @see IProtocol#getResponse()
     */
    public String getResponse()
    {
        return response;
    }


    //~ Static/instance variables .............................................

    /* The response returned by the HTTP server. */
    private String response;
}
