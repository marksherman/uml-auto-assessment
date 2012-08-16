/*==========================================================================*\
 |  $Id: VersionUtils.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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

// ------------------------------------------------------------------------
/**
 * The methods in this class simplify dealing with version strings in report
 * templates.
 *
 * @author Tony Allevato
 * @version $Id: VersionUtils.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class VersionUtils
{
    // ----------------------------------------------------------
    /**
     * Prevent instantiation.
     */
    private VersionUtils()
    {
        // Static class; prevent instantiation.
    }


    // ----------------------------------------------------------
    /**
     * Gets the string that represents the initial (root) version of an asset.
     *
     * Currently, the initial version of an asset is "1".
     *
     * @return the string that represents the initial version of an asset
     */
    public static String initialVersion()
    {
        return "1";
    }


    // ----------------------------------------------------------
    /**
     * Gets the string that represents a version of an asset incremented once
     * from the specified version. This causes the number in the last segment of
     * the version string to be incremented by one.
     *
     * For example, "1" increments to "2", "2" to "3", and so forth. "3b2.4"
     * increments to "3b2.5", and "2b1.5b4.3b2.2" to "2b1.5b4.3b2.3".
     *
     * @param version
     *            the version to be incremented
     * @return the incremented version
     */
    public static String incrementVersion(String version)
    {
        int lastDot = version.lastIndexOf('.');

        if (lastDot == -1)
        {
            // No dot means the version has no branches; it's just a version of
            // the root asset.

            int numVersion = Integer.parseInt(version);
            return Integer.toString(numVersion + 1);
        }
        else
        {
            String prefix = version.substring(0, lastDot + 1);
            String postfix = version.substring(lastDot + 1);

            int numVersion = Integer.parseInt(postfix);
            return prefix + Integer.toString(numVersion);
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets the string that represents the version of an asset that is branched
     * off from the asset with the specified version. This adds a new "bN.1" to
     * the end of the string, where N corresponds to the branch argument passed
     * to the method.
     *
     * @param version
     *            the version to be branched from
     * @param branch
     *            the number of the branch to create
     * @return the branched version
     */
    public static String branchOfVersion(String version, int branch)
    {
        return version + "b" + Integer.toString(branch) + ".1";
    }
}
