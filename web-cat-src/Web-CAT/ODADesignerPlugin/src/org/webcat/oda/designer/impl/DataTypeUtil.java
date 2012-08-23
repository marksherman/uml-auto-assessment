/*==========================================================================*\
 |  $Id: DataTypeUtil.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
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

package org.webcat.oda.designer.impl;

import java.util.HashMap;
import org.webcat.oda.core.impl.DataTypes;
import org.webcat.oda.designer.i18n.Messages;

//------------------------------------------------------------------------
/**
 * Utility methods to convert between integral data type constants and their
 * display names.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: DataTypeUtil.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
 */
public class DataTypeUtil
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Prevent instantiation.
     */
    private DataTypeUtil()
    {
        // Static class; prevent instantiation.
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the display name for the specified data type constant.
     *
     * @param type
     *            the data type
     *
     * @return the display name of the data type
     */
    public static String getDataTypeDisplayName(int type)
    {
        String s = dataTypeDisplayNameMapping.get(Integer.valueOf(type));
        if (s != null)
            return s;
        else
            return Messages.DATATYPE_DISPLAYNAME_STRING;
    }


    // ----------------------------------------------------------
    /**
     * Gets the data type constant for the data type with the specified display
     * name.
     *
     * @param displayName
     *            the display name of the data type
     *
     * @return the data type constant for the data type with the specified
     *         display name
     */
    public static Integer getDataType(String displayName)
    {
        Integer i = displayNameDataTypeMapping.get(displayName);

        if (i != null)
            return i;
        else
            return Integer.valueOf(DataTypes.STRING);
    }


    //~ Static variables ......................................................

    private static HashMap<String, Integer> displayNameDataTypeMapping = new HashMap<String, Integer>();
    private static HashMap<Integer, String> dataTypeDisplayNameMapping = new HashMap<Integer, String>();

    // ----------------------------------------------------------
    static
    {
        displayNameDataTypeMapping.put(Messages.DATATYPE_DISPLAYNAME_TIMESTAMP,
                Integer.valueOf(DataTypes.TIMESTAMP));
        displayNameDataTypeMapping.put(Messages.DATATYPE_DISPLAYNAME_DECIMAL,
                Integer.valueOf(DataTypes.DECIMAL));
        displayNameDataTypeMapping.put(Messages.DATATYPE_DISPLAYNAME_FLOAT,
                Integer.valueOf(DataTypes.DOUBLE));
        displayNameDataTypeMapping.put(Messages.DATATYPE_DISPLAYNAME_INTEGER,
                Integer.valueOf(DataTypes.INT));
        displayNameDataTypeMapping.put(Messages.DATATYPE_DISPLAYNAME_STRING,
                Integer.valueOf(DataTypes.STRING));
        displayNameDataTypeMapping.put(Messages.DATATYPE_DISPLAYNAME_BOOLEAN,
                Integer.valueOf(DataTypes.BOOLEAN));

        dataTypeDisplayNameMapping.put(Integer.valueOf(DataTypes.TIMESTAMP),
                Messages.DATATYPE_DISPLAYNAME_TIMESTAMP);
        dataTypeDisplayNameMapping.put(Integer.valueOf(DataTypes.DECIMAL),
                Messages.DATATYPE_DISPLAYNAME_DECIMAL);
        dataTypeDisplayNameMapping.put(Integer.valueOf(DataTypes.DOUBLE),
                Messages.DATATYPE_DISPLAYNAME_FLOAT);
        dataTypeDisplayNameMapping.put(Integer.valueOf(DataTypes.INT),
                Messages.DATATYPE_DISPLAYNAME_INTEGER);
        dataTypeDisplayNameMapping.put(Integer.valueOf(DataTypes.STRING),
                Messages.DATATYPE_DISPLAYNAME_STRING);
        dataTypeDisplayNameMapping.put(Integer.valueOf(DataTypes.BOOLEAN),
                Messages.DATATYPE_DISPLAYNAME_BOOLEAN);
    }
}
