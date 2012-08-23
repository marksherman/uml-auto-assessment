/*==========================================================================*\
 |  $Id: MockProtocol.java,v 1.2 2010/09/14 18:13:30 aallowat Exp $
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

package org.webcat.submitter.tests.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.webcat.submitter.ILongRunningTask;
import org.webcat.submitter.IProtocol;
import org.webcat.submitter.SubmissionManifest;
import org.webcat.submitter.SubmissionTargetException;

//--------------------------------------------------------------------------
/**
 * A mock protocol used during testing. The protocol "transmits" the submitted
 * package to a static byte array that can be accessed using the
 * {@link MockProtocol#getTransmittedBytes()} method.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/09/14 18:13:30 $
 */
public class MockProtocol implements IProtocol
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * @see IProtocol#submit(SubmissionManifest, ILongRunningTask)
     */
    public void submit(SubmissionManifest params, ILongRunningTask task)
    throws IOException
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try
        {
            params.packageContentsIntoStream(stream, task, null);
        }
        catch (SubmissionTargetException e)
        {
            // Do nothing.
        }
        finally
        {
            stream.close();
        }

        transmittedBytes = stream.toByteArray();
    }


    // ----------------------------------------------------------
    /**
     * @see IProtocol#getResponse()
     */
    public String getResponse()
    {
        return null;
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
     * Gets the byte array that represents the contents of the submitted
     * package.
     *
     * @return a byte array representing the package contents
     */
    public static byte[] getTransmittedBytes()
    {
        return transmittedBytes;
    }


    //~ Static/instance variables .............................................

    /* The byte array that represents the contents of the submitted package. */
    private static byte[] transmittedBytes;
}
