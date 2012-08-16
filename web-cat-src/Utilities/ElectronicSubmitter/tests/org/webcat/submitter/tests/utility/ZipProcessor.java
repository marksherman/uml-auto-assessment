/*==========================================================================*\
 |  $Id: ZipProcessor.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.webcat.submitter.internal.utility.StreamUtils;

//--------------------------------------------------------------------------
/**
 * Reads entries from a zip file into a map. Used for ease of testing.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public class ZipProcessor
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a new ZIP processor from the specified archive contents.
     *
     * @param archiveContents a byte array representing the ZIP archive
     * @throws IOException if an I/O exception occurs
     */
    public ZipProcessor(byte[] archiveContents) throws IOException
    {
        entries = new HashMap<String, String>();

        ByteArrayInputStream stream = new ByteArrayInputStream(archiveContents);
        ZipInputStream zipStream = new ZipInputStream(stream);

        processEntries(zipStream);

        zipStream.close();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the entries that were in the ZIP archive.
     *
     * @return a Map containing the ZIP entries' contents
     */
    public Map<String, String> entries()
    {
        return entries;
    }


    // ----------------------------------------------------------
    /**
     * Iterates through the entries in a ZIP stream and builds a map with
     * their contents.
     *
     * @param zipStream the ZIP stream whose entries should be processed
     * @throws IOException if an I/O exception occurred
     */
    private void processEntries(ZipInputStream zipStream) throws IOException
    {
        ZipEntry entry = zipStream.getNextEntry();

        byte[] buffer = new byte[256];

        while (entry != null)
        {
            String content = null;

            if (!entry.isDirectory())
            {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                StreamUtils.copy(zipStream, outStream, buffer);
                content = new String(outStream.toByteArray(), "UTF-8");
            }

            entries.put(entry.getName(), content);

            zipStream.closeEntry();
            entry = zipStream.getNextEntry();
        }
    }


    //~ Static/instance variables .............................................

    /* The contents of the ZIP entries. */
    private Map<String, String> entries;
}
