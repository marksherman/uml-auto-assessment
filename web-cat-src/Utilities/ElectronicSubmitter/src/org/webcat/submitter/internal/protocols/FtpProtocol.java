/*==========================================================================*\
 |  $Id: FtpProtocol.java,v 1.2 2010/09/14 18:13:30 aallowat Exp $
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
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import org.webcat.submitter.ILongRunningTask;
import org.webcat.submitter.IProtocol;
import org.webcat.submitter.SubmissionManifest;

//--------------------------------------------------------------------------
/**
 * A protocol for the "ftp" URI scheme that supports uploading the submitted
 * file to a remote server via FTP.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/09/14 18:13:30 $
 */
public class FtpProtocol implements IProtocol
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * @see IProtocol#submit(SubmissionManifest, ILongRunningTask)
     */
    public void submit(SubmissionManifest manifest, ILongRunningTask task)
    throws IOException
    {
        URL url = manifest.getResolvedTransport(null).toURL();

        URLConnection connection = url.openConnection();
        OutputStream outStream = connection.getOutputStream();

        manifest.packageContentsIntoStream(outStream, task, null);

        outStream.close();
    }


    // ----------------------------------------------------------
    /**
     * @see IProtocol#hasResponse()
     */
    public boolean hasResponse()
    {
        return false;
    }


    // ----------------------------------------------------------
    /**
     * @see IProtocol#getResponse()
     */
    public String getResponse()
    {
        return null;
    }
}
