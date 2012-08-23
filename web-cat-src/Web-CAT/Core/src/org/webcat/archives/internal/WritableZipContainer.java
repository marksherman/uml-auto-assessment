/*==========================================================================*\
 |  $Id: WritableZipContainer.java,v 1.1 2011/05/13 19:42:32 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2011 Virginia Tech
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

package org.webcat.archives.internal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.webcat.archives.IWritableContainer;

//-------------------------------------------------------------------------
/**
 * TODO real description
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: aallowat $
 * @version $Revision: 1.1 $, $Date: 2011/05/13 19:42:32 $
 */
public class WritableZipContainer implements IWritableContainer
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    public WritableZipContainer(File path) throws IOException
    {
        this(new ZipOutputStream(new FileOutputStream(path)));
        this.shouldCloseStream = true;
    }


    // ----------------------------------------------------------
    public WritableZipContainer(ZipOutputStream zipStream)
    {
        this.zipStream = zipStream;
        this.shouldCloseStream = false;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public IWritableContainer createContainer(String name) throws IOException
    {
        return new ZipEntryContainer(name);
    }


    // ----------------------------------------------------------
    public OutputStream createFile(String name, long sizeHint)
            throws IOException
    {
        ZipEntry entry = new ZipEntry(name);

        if (sizeHint != -1)
        {
            entry.setSize(sizeHint);
        }

        zipStream.putNextEntry(entry);
        return new ZipEntryOutputStream();
    }


    // ----------------------------------------------------------
    public void finish() throws IOException
    {
        zipStream.finish();

        if (shouldCloseStream)
        {
            zipStream.close();
        }
    }


    //~ Inner classes .........................................................

    // ----------------------------------------------------------
    private class ZipEntryContainer implements IWritableContainer
    {
        // ----------------------------------------------------------
        public ZipEntryContainer(String path) throws IOException
        {
            if (!path.endsWith("/"))
            {
                path = path + "/";
            }

            this.path = path;

            ZipEntry entry = new ZipEntry(path);
            zipStream.putNextEntry(entry);
            zipStream.closeEntry();
        }


        // ----------------------------------------------------------
        public IWritableContainer createContainer(String name)
                throws IOException
        {
            return new ZipEntryContainer(path + name);
        }


        // ----------------------------------------------------------
        public OutputStream createFile(String name, long sizeHint)
                throws IOException
        {
            return WritableZipContainer.this.createFile(path + name, sizeHint);
        }


        // ----------------------------------------------------------
        public void finish() throws IOException
        {
            // Do nothing.
        }


        //~ Static/instance variables .........................................

        private String path;
    }


    // ----------------------------------------------------------
    private class ZipEntryOutputStream extends OutputStream
    {
        //~ Methods ...........................................................

        // ----------------------------------------------------------
        @Override
        public void close() throws IOException
        {
            zipStream.closeEntry();
        }


        // ----------------------------------------------------------
        @Override
        public void write(int b) throws IOException
        {
            zipStream.write(b);
        }


        // ----------------------------------------------------------
        @Override
        public void write(byte[] b, int off, int len)
            throws IOException
        {
            zipStream.write(b, off, len);
        }
    }


    //~ Static/instance variables .............................................

    private ZipOutputStream zipStream;
    private boolean shouldCloseStream;
}
