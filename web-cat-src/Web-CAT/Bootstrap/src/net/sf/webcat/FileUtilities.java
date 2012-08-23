/*==========================================================================*\
 |  $Id: FileUtilities.java,v 1.9 2011/05/27 15:30:56 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2008 Virginia Tech
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

package net.sf.webcat;

import java.io.*;
import java.util.*;
import java.util.zip.*;

// -------------------------------------------------------------------------
/**
 *  Provides utility methods for manipulating files and directories.
 *
 *  @author  stedwar2
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.9 $, $Date: 2011/05/27 15:30:56 $
 */
public class FileUtilities
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * This constructor is private since this class should not be
     * instantiated.  It simply provides a few static helper methods
     * used by other classes.
     */
    private FileUtilities()
    {
        // No actions to perform, since this class has no state.
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Unzips the zipfile in the output directory.  This method is
     * provided purely for use by the Bootstrap code.  All other code
     * should use the org.webcat.archives.ArchiveManager
     * class instead.
     *
     * @param zipFile the zip file
     * @param outputDir full path of the output directory
     * @throws java.io.IOException if occurs during unzipping
     */
    public static void unZip(ZipFile zipFile, File outputDir)
        throws java.io.IOException
    {
        Enumeration<? extends ZipEntry> e = zipFile.entries();
        while (e.hasMoreElements())
        {
            ZipEntry entry = e.nextElement();
            File entryFile = new File(outputDir, entry.getName());
            if (entry.isDirectory())
            {
                entryFile.mkdirs();
            }
            else
            {
                File parent = entryFile.getParentFile();
                if (!parent.exists())
                {
                    parent.mkdirs();
                }
                BufferedInputStream  in = new BufferedInputStream(
                    zipFile.getInputStream(entry));
                BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(entryFile));
                copyStream(in, out);
                in.close();
                out.close();
                long modTime = entry.getTime();
                if (modTime != -1)
                {
                    entryFile.setLastModified(modTime);
                }
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Copies the contents of an input stream onto an existing output
     * stream.  The output stream is flushed when the operation
     * is complete.
     *
     * @param in  the input stream to copy
     * @param out the destination
     * @throws IOException if there are IO errors
     */
    public static void copyStream(InputStream in, OutputStream out)
        throws IOException
    {
        // read in increments of BUFFER_SIZE
        byte[] b = new byte[BUFFER_SIZE];
        int count = in.read(b);
        while (count > -1)
        {
            out.write(b, 0, count);
            count = in.read(b);
        }
        out.flush();
    }


    // ----------------------------------------------------------
    /**
     * Checks to see if the given directory contains a file with the given
     * name.
     * @param dir The directory to check.
     *
     * @param filename the name of the file
     * @return True if there is a file in the directory with the given
     *         file name.
     */
    public static boolean inDirectory(File dir, String filename)
    {
    	if (dir != null && filename != null && dir.isDirectory())
    	{
    		for (File file : dir.listFiles())
    		{
    			if (filename.equals(file.getName()))
    			{
    				return true;
    			}
    		}
    	}

    	return false;
    }

    // ----------------------------------------------------------
    /**
     * Deletes older versions of the update files found in the specified
     * directory.
     * @param Directory containing update files
     */
    public static void deleteOlderFiles(File dir)
    {
    	if (dir != null && dir.isDirectory())
    	{
	        File[] dirFiles = dir.listFiles();

	    	for (int i = 0; i+1 < dirFiles.length; i++)
	    	{
	    		if (dirFiles[i] == null)
	    		{
	    			continue;
	    		}

	    		String[] iFile = dirFiles[i].getName().split("\\_");
	    		for (int j = i+1; j < dirFiles.length; j++)
	    		{
	    			String[] jFile = dirFiles[j].getName().split("\\_");

	    			if (iFile[0].equals(jFile[0]))
	    			{
	    				iFile = iFile[1].split("\\.");
	    				jFile = jFile[1].split("\\.");

	    				int delete;

	    				if (iFile[2].compareTo(jFile[2]) > 0)
	    				{
	    					delete = j;
	    				}
	    				else
	    				{
	    					delete = i;
	    				}

	    				if (iFile[1].compareTo(jFile[1]) > 0)
	    				{
	    					delete = j;
	    				}
	    				else
	    				{
	    					delete = i;
	    				}

	    				if (iFile[0].compareTo(jFile[0]) > 0)
	    				{
	    					delete = j;
	    				}
	    				else
	    				{
	    					delete = i;
	    				}

	    				dirFiles[delete].delete();
	    				dirFiles[delete] = null;
	    			}
	    		}
	    	}
    	}
    }


    // ----------------------------------------------------------
    /**
     * Return a canonical version of the file name, using "/" as the path
     * seperator instead of "\".
     *
     * @param name the File with the name to convert
     * @return the canonical version of the file's name
     */
    public static String normalizeFileName(File name)
    {
        try
        {
            return name.getCanonicalPath().replace('\\', '/');
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    // ----------------------------------------------------------
    /**
     * Return a canonical version of the file name, using "/" as the path
     * seperator instead of "\".
     *
     * @param name the name to convert
     * @return the canonical version of the file's name
     */
    public static String normalizeFileName(String name)
    {
        return normalizeFileName(new File(name));
    }


    // ----------------------------------------------------------
    /**
     * Recursively deletes a directory
     *
     * @param dir the File object for the directory
     * @param preserve a Map with String keys indicating elements within
     *        the target directory to keep (that is, not to delete); this
     *        parameter can be null if none are to be preserved.
     * @return true if the directory was removed, false if it was not
     *  (because at least some of its contents were preserved).
     */
    public static boolean deleteDirectory(File dir, Map<String, ?> preserve)
    {
        if (dir == null || !dir.exists())
        {
            return true;
        }

        File[] files = dir.listFiles();
        boolean deletedAll = true;
        for (int i = 0; i < files.length; i++)
        {
            if (preserve != null)
            {
                // This is just a linear search, because the preserve
                // lists are so short in general that it is not worth the
                // effort to speed them up.
                if (preserve.containsKey(normalizeFileName(files[i])))
                {
                    deletedAll = false;
                    continue;
                }
            }

            if (files[i].isDirectory())
            {
                deletedAll = deleteDirectory(files[i], preserve)
                    && deletedAll;
            }
            files[i].delete();
        }
        if (deletedAll)
        {
            dir.delete();
        }
        return deletedAll;
    }


    // ----------------------------------------------------------
    /**
     * Gets the CRC checksum of a file.
     * @param file The file to compute the checksum of.
     * @return the checksum value
     */
    public static long getCRCChecksum(File file)
    {
    	try
    	{
			CheckedInputStream cis =
			    new CheckedInputStream(new FileInputStream(file), new CRC32());

			byte[] buf = new byte[BUFFER_SIZE];
			while (cis.read(buf) >= 0)
			{
			    // nothing to do ... just accumulating checksum
			}
			long checksum = cis.getChecksum().getValue();
			cis.close();
			return checksum;
		}
    	catch (FileNotFoundException e)
    	{
    	    // silently swallow exception
		}
    	catch (IOException e)
    	{
            // silently swallow exception
		}

    	return 0;
    }


    //~ Instance/static variables .............................................

    static final private int BUFFER_SIZE = 65536;
}
