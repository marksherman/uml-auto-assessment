/*==========================================================================*\
 |  $Id: StreamUtils.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
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

package org.webcat.submitter.internal.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//--------------------------------------------------------------------------
/**
 * Utility methods for operating with I/O streams.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public class StreamUtils
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Prevent instantiation.
     */
    private StreamUtils()
    {
        // Prevent instantiation.
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Copy the entire contents of an input stream to an output stream, using
     * the specified intermediate buffer.
     *
     * @param inputStream the input stream to copy from
     * @param outputStream the output stream to copy to
     * @param buffer the buffer that will hold data while it is being copied
     * @throws IOException if an I/O error occurred
     */
    public static void copy(InputStream inputStream, OutputStream outputStream,
            byte[] buffer) throws IOException
    {
        int bytesRead = inputStream.read(buffer);

        while (bytesRead > 0)
        {
            outputStream.write(buffer, 0, bytesRead);
            bytesRead = inputStream.read(buffer);
        }
    }
}
