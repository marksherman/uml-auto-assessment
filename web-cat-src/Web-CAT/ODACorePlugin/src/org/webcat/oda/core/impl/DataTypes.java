/*==========================================================================*\
 |  $Id: DataTypes.java,v 1.2 2010/09/20 14:17:35 aallowat Exp $
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

package org.webcat.oda.core.impl;

import java.sql.Types;
import java.util.Locale;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.manifest.DataTypeMapping;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;

// ------------------------------------------------------------------------
/**
 * Manages the mapping between native data type codes and their string
 * representation.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: DataTypes.java,v 1.2 2010/09/20 14:17:35 aallowat Exp $
 */
public class DataTypes
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Prevent instantiation of the DataTypes class.
     */
    private DataTypes()
    {
        // Static class; prevent instantiation.
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the native data type code that corresponds to the specified data
     * type name.
     *
     * @param typeName
     *            the name of the data type
     *
     * @return the native data type code
     *
     * @throws OdaException
     *             if the typename is invalid
     */
    public static int getType(String typeName) throws OdaException
    {
        if (typeName == null || typeName.trim().length() == 0)
            return STRING;

        String preparedTypeName = typeName.trim().toUpperCase(Locale.ENGLISH);

        DataTypeMapping typeMapping = getManifest().getDataSetType(null)
                .getDataTypeMapping(preparedTypeName);

        if (typeMapping != null)
            return typeMapping.getNativeTypeCode();

        throw new OdaException("Invalid typename " + typeName);
    }


    // ----------------------------------------------------------
    /**
     * Gets the data type name for the specified native data type code.
     *
     * @param type
     *            the native data type code
     *
     * @return a String containing the data type name
     *
     * @throws OdaException
     *             if the native data type code is invalid
     */
    public static String getTypeString(int type) throws OdaException
    {
        DataTypeMapping typeMapping = getManifest().getDataSetType(null)
                .getDataTypeMapping(type);
        if (typeMapping != null)
            return typeMapping.getNativeType();

        throw new OdaException("Invalid type " + type);
    }


    // ----------------------------------------------------------
    /**
     * Returns a value indicating whether the specified type name represents a
     * valid type.
     *
     * @param typeName
     *            the name of the data type
     *
     * @return true if the data type is valid; otherwise, false.
     */
    public static boolean isValidType(String typeName)
    {
        String preparedTypeName = typeName.trim().toUpperCase(Locale.ENGLISH);

        DataTypeMapping typeMapping = null;

        try
        {
            typeMapping = getManifest().getDataSetType(null)
                    .getDataTypeMapping(preparedTypeName);
        }
        catch (OdaException e)
        {
            // Ignore exception, use null instead.
        }

        return typeMapping != null;
    }


    // ----------------------------------------------------------
    /**
     * Gets the extension manifest for the Web-CAT data source.
     *
     * @return the extension manifest
     * @throws OdaException
     */
    private static ExtensionManifest getManifest() throws OdaException
    {
        return ManifestExplorer.getInstance().getExtensionManifest(
                WEBCAT_DATA_SOURCE_ID);
    }


    //~ Static/Instance Variables .............................................

    public static final int INT = Types.INTEGER;
    public static final int DOUBLE = Types.DOUBLE;
    public static final int DECIMAL = Types.DECIMAL;
    public static final int STRING = Types.VARCHAR;
    public static final int TIMESTAMP = Types.TIMESTAMP;
    public static final int BOOLEAN = Types.BOOLEAN;

    private static final String WEBCAT_DATA_SOURCE_ID =
        "net.sf.webcat.oda.core.dataSource";
}
