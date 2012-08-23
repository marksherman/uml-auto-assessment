/*==========================================================================*\
 |  $Id: JarPackager.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
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

package org.webcat.submitter.internal.packagers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import org.webcat.submitter.IPackager;
import org.webcat.submitter.ISubmittableItem;
import org.webcat.submitter.SubmittableItemKind;
import org.webcat.submitter.internal.utility.StreamUtils;

//--------------------------------------------------------------------------
/**
 * A packager that collects the submittable items into a Java JAR archive.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public class JarPackager implements IPackager
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * @see IPackager#startPackage(OutputStream, Map)
     */
    public void startPackage(OutputStream stream,
            Map<String, String> parameters)
    throws IOException
    {
        Manifest manifest = new Manifest();
        Attributes attrs = manifest.getMainAttributes();

        attrs.put(Attributes.Name.MANIFEST_VERSION, "1.0");

        for(String key : parameters.keySet())
        {
            String value = parameters.get(key);
            attrs.putValue(key, value);
        }

        jarStream = new JarOutputStream(stream, manifest);
    }


    // ----------------------------------------------------------
    /**
     * @see IPackager#addSubmittableItem(ISubmittableItem)
     */
    public void addSubmittableItem(ISubmittableItem item) throws IOException
    {
        if (item.getKind() == SubmittableItemKind.FOLDER)
        {
            if (item.getFilename().length() > 0 &&
                    !item.getFilename().equals("/"))
            {
                JarEntry entry = new JarEntry(item.getFilename() + "/");

                jarStream.putNextEntry(entry);
                jarStream.closeEntry();
            }
        }
        else if (item.getKind() == SubmittableItemKind.FILE)
        {
            InputStream itemStream = item.getStream();

            // If we try to stream the file directly into the zip file
            // without determining its size for the zip entry, the archive
            // will not expand properly under OS X. Until we can fix this,
            // we stream each file entirely into memory to compute its
            // length, then stream the memory buffer out to the zip file.

            ByteArrayOutputStream memStream = new ByteArrayOutputStream();
            StreamUtils.copy(itemStream, memStream, buffer);
            itemStream.close();

            long length = memStream.size();

            JarEntry entry = new JarEntry(item.getFilename());
            entry.setSize(length);
            jarStream.putNextEntry(entry);

            ByteArrayInputStream memInputStream =
                new ByteArrayInputStream(memStream.toByteArray());
            StreamUtils.copy(memInputStream, jarStream, buffer);

            jarStream.closeEntry();
        }
    }


    // ----------------------------------------------------------
    /**
     * @see IPackager#endPackage()
     */
    public void endPackage() throws IOException
    {
        jarStream.finish();
        jarStream.flush();
    }


    //~ Static/instance variables .............................................

    /* The size of the buffer to use during stream copying. */
    private static final int BUFFER_SIZE = 65536;

    /* The JAR output stream to write the package to. */
    private JarOutputStream jarStream;

    /* The buffer to use during stream copying. */
    private byte[] buffer = new byte[BUFFER_SIZE];
}
