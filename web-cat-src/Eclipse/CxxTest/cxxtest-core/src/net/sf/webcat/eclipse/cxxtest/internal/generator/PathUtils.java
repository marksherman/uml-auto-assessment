/*==========================================================================*\
 |  $Id: PathUtils.java,v 1.3 2009/09/13 12:59:29 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech 
 |
 |	This file is part of Web-CAT Eclipse Plugins.
 |
 |	Web-CAT is free software; you can redistribute it and/or modify
 |	it under the terms of the GNU General Public License as published by
 |	the Free Software Foundation; either version 2 of the License, or
 |	(at your option) any later version.
 |
 |	Web-CAT is distributed in the hope that it will be useful,
 |	but WITHOUT ANY WARRANTY; without even the implied warranty of
 |	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |	GNU General Public License for more details.
 |
 |	You should have received a copy of the GNU General Public License
 |	along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package net.sf.webcat.eclipse.cxxtest.internal.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.webcat.eclipse.cxxtest.i18n.Messages;

//--------------------------------------------------------------------------
/**
 * Utility functions for working with relative paths.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2009/09/13 12:59:29 $
 */
public class PathUtils
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets a relative path from the file or directory "source" to the file or
     * directory "destination". For example, if source is "/a/b/c" and
     * destination is "/a/d/e/x.txt" (assuming that "/a/b/c" is a directory),
     * then this function returns the string "../../d/e/x.txt".
     * 
     * @param source the source path, as a String. If null, the process's
     *     current directory is used. The source can be either a directory or a
     *     file.
     * @param destination the destination path, as a String. Cannot be null.
     *
     * @return the relative path from source to destination, as a String
     */
    public static String relativizePath(String source, String destination)
    {
        File sourceFile;
        
        if (source == null)
        {
            sourceFile = new File("."); //$NON-NLS-1$
        }
        else
        {
            sourceFile = new File(source);
        }
        
        return relativizePath(sourceFile, new File(destination));
    }


    // ----------------------------------------------------------
    /**
     * Gets a relative path from the file or directory "source" to the file or
     * directory "destination". For example, if source is "/a/b/c" and
     * destination is "/a/d/e/x.txt", then this function returns the string
     * "../../d/e/x.txt".
     * 
     * @param source the source path, as a File. If null, the process's current
     *     directory is used
     * @param destination the destination path, as a File. Cannot be null.
     *
     * @return the relative path from source to destination, as a String
     */
    public static String relativizePath(File source, File destination)
    {
        if (destination == null)
        {
            throw new IllegalArgumentException(Messages.PathUtils_DestinationPathNull);
        }

        if (source != null && !source.isDirectory())
        {
            // If the source is a file, use the directory that contains it.

            source = source.getParentFile();
        }

        if (source == null)
        {
            // Use the process's current working directory if the source is
            // null.
            
            source = new File("."); //$NON-NLS-1$
        }

        String[] srcSegments = reversedPathSegments(source);
        String[] destSegments = reversedPathSegments(destination);

        if (srcSegments == null || destSegments == null)
        {
            // If there was a problem segmenting either of the paths, just
            // play it safe and return the full destination path.

            return destination.getAbsolutePath();
        }
        else
        {
            // This is a special check for Windows systems, to make sure that
            // both paths are on the same volume (if they don't, then there is
            // no relative path between them). If the paths are not on the same
            // volume, just return the full destination path.
            
            if (IS_WINDOWS)
            {
                String srcVolume = srcSegments[srcSegments.length - 1];
                String destVolume = destSegments[destSegments.length - 1];

                if (!srcVolume.equalsIgnoreCase(destVolume))
                {
                    return destination.getAbsolutePath();
                }
            }

            return relativizePathSegments(srcSegments, destSegments);
        }
    }


    // ----------------------------------------------------------
    /**
     * Separates a path into its individual segments and returns these segments
     * in a list, in reverse order. For example, the path "/a/b/c/d.txt" will
     * be returned as the list [ "d.txt", "c", "b", "a" ].
     * 
     * @param path the input path
     * @return a List containing the path segments in reverse order
     */
    private static String[] reversedPathSegments(File path)
    {
        List<String> segments = new ArrayList<String>();

        try
        {
            // Canonicalize the path so that both will compare with string
            // equality later.

            File currentFile = path.getCanonicalFile();

            // Navigate up to the root, pruning off each segment of the path.

            while (currentFile != null)
            {
                segments.add(currentFile.getName());
                currentFile = currentFile.getParentFile();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            segments = null;
        }

        if (segments != null)
        {
            return segments.toArray(new String[segments.size()]);
        }
        else
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    /**
     * Computes a string containing the relative path from the directory or
     * file represented by the source segments to the directory or file
     * represented by the destination segments.
     * 
     * @param srcSegments the segments of the source path
     * @param destSegments the segments of the destination path
     */
    private static String relativizePathSegments(String[] srcSegments,
            String[] destSegments)
    {
        int srcIndex = srcSegments.length - 1;
        int destIndex = destSegments.length - 1;

        StringBuffer relativePath = new StringBuffer();

        // Skip past the parts of the paths that both have in common. String
        // equality suffices here since the paths are canonicalized before
        // being passed into this function.
        
        while (srcIndex >= 0 && destIndex >= 0 &&
                srcSegments[srcIndex].equals(destSegments[destIndex]))
        {
            srcIndex--;
            destIndex--;
        }

        // For each remaining segment in the source path, add a reference to
        // the parent directory.
        
        for (; srcIndex >= 0; srcIndex--)
        {
            relativePath.append(".."); //$NON-NLS-1$
            relativePath.append(File.separator);
        }

        // Then, now that we're at the location where the remainder of the
        // destination path starts, append those segments to the end of the
        // path.
        
        for (; destIndex > 0; destIndex--)
        {
            relativePath.append(destSegments[destIndex]);
            relativePath.append(File.separator);
        }

        // Finally, append the filename, if there is one.
        
        if (destIndex == 0)
        {
            relativePath.append(destSegments[destIndex]);
        }

        // Remove a final trailing slash if it is there.
        
        if (relativePath.charAt(relativePath.length() - 1) ==
            File.separatorChar)
        {
            relativePath.deleteCharAt(relativePath.length() - 1);
        }

        return relativePath.toString();
    }
    

    //~ Static initialization .................................................
    
    // ----------------------------------------------------------
    static
    {
        // Determine if we're running on Windows, so we can do a volume check
        // if necessary.
        
        String osName = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$
        IS_WINDOWS = osName.contains("windows"); //$NON-NLS-1$
    }
    

    //~ Static/instance variables .............................................

    private static final boolean IS_WINDOWS; 
}
