/*==========================================================================*\
 |  $Id: DataSetMetadata.java,v 1.2 2010/09/20 14:17:34 aallowat Exp $
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

package org.webcat.oda.commons;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;

//-------------------------------------------------------------------------
/**
 * Methods in this class mirror each of the properties that Web-CAT uses to
 * maintain information about a data set in a report template. These methods
 * provide uniform access to these properties regardless of whether they are
 * implemented as user properties or by shadowing an existing BIRT property.
 * Other useful utility methods are provided as well.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: DataSetMetadata.java,v 1.2 2010/09/20 14:17:34 aallowat Exp $
 */
public class DataSetMetadata
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * A private constructor to prevent instantiation.
     */
    private DataSetMetadata()
    {
        // Static class; prevent instantiation.
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the name of the specified data set.
     *
     * @param handle
     *            the data set handle
     * @return the name of the data set
     */
    public static String getName(DataSetHandle handle)
    {
        return nullifyIfEmpty(handle.getName());
    }


    // ----------------------------------------------------------
    /**
     * Sets the name of the specified data set.
     *
     * @param handle
     *            the data set handle
     * @param value
     *            the new name of the data set
     */
    public static void setName(DataSetHandle handle, String value)
    {
        try
        {
            handle.setName(nullifyIfEmpty(value));
        }
        catch (NameException e)
        {
            // Do nothing.
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets the description of the specified data set.
     *
     * @param handle
     *            the data set handle
     * @return the description of the data set
     */
    public static String getDescription(DataSetHandle handle)
    {
        return nullifyIfEmpty(handle.getComments());
    }


    // ----------------------------------------------------------
    /**
     * Sets the description of the specified data set.
     *
     * @param handle
     *            the data set handle
     * @param value
     *            the new description of the data set
     */
    public static void setDescription(DataSetHandle handle, String value)
    {
        try
        {
            handle.setComments(nullifyIfEmpty(value));
        }
        catch (NameException e)
        {
            // Do nothing.
        }
        catch (SemanticException e)
        {
            // Do nothing.
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets a value indicating whether the data set is a Web-CAT data set.
     *
     * @param handle
     *            the data set handle
     * @return true if the data set is a Web-CAT ODA data set; false if it is
     *         any other type of data set
     */
    public static boolean isWebCATDataSet(DataSetHandle handle)
    {
        String extensionID = handle.getStringProperty("extensionID");
        return ("net.sf.webcat.oda.core.dataSet".equals(extensionID));
    }


    // ----------------------------------------------------------
    /**
     * Returns null if the specified string is null or empty.
     *
     * @param string
     *            the string to check
     * @return the same string if it is not null or empty, otherwise null
     */
    private static String nullifyIfEmpty(String string)
    {
        if (string == null || string.trim().length() == 0)
        {
            return null;
        }
        else
        {
            return string;
        }
    }
}
