/*==========================================================================*\
 |  $Id: ChecksumUtils.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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

package org.webcat.reporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// ------------------------------------------------------------------------
/**
 * Methods to assist in the computation of checksums from the contents of a
 * file.
 *
 * @author Tony Allevato
 * @version $Id: ChecksumUtils.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class ChecksumUtils
{
    // ----------------------------------------------------------
    /**
     * Prevent instantiation.
     */
    private ChecksumUtils()
    {
        // Static class, prevent instantiation.
    }


    // ----------------------------------------------------------
    /**
     * Computes an MD5 checksum using the contents of the specified file and
     * returns it as a hex string.
     *
     * @param file
     *            the file whose checksum should be computed
     * @return the file checksum as a hex string
     */
    public static String checksumFromContentsOfFile(File file)
    {
        try
        {
            FileInputStream stream = new FileInputStream(file);

            byte[] buffer = new byte[BUFFER_SIZE];

            MessageDigest digest = MessageDigest.getInstance("MD5");
            int bytesRead;

            while ((bytesRead = stream.read(buffer)) > 0)
            {
                digest.update(buffer, 0, bytesRead);
            }

            stream.close();

            byte[] digestBytes = digest.digest();

            StringBuffer digestString = new StringBuffer(32);

            for (byte digestByte : digestBytes)
            {
                digestString.append(String.format("%02x", digestByte));
            }

            return digestString.toString();
        }
        catch (IOException e)
        {
            return null;
        }
        catch (NoSuchAlgorithmException e)
        {
            return null;
        }
    }


    //~ Static variables ......................................................

    private static final int BUFFER_SIZE = 16384;
}
