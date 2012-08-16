/*==========================================================================*\
 |  $Id: ColumnMappingElement.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
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

//------------------------------------------------------------------------
/**
 * Stores information about a result set column while it is being edited in the
 * data set editor.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: ColumnMappingElement.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
 */
public class ColumnMappingElement
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the name of the column.
     *
     * @return the name of the column
     */
    public String getColumnName()
    {
        return columnName;
    }


    // ----------------------------------------------------------
    /**
     * Sets the name of the column.
     *
     * @param value
     *            the name of the column
     */
    public void setColumnName(String value)
    {
        columnName = value;
    }


    // ----------------------------------------------------------
    /**
     * Gets the key path or OGNL expression for the column.
     *
     * @return the key path or OGNL expression for the column
     */
    public String getExpression()
    {
        return expression;
    }


    // ----------------------------------------------------------
    /**
     * Sets the key path or OGNL expression for the column.
     *
     * @param value
     *            the key path or OGNL expression for the column
     */
    public void setExpression(String value)
    {
        expression = value;
    }


    // ----------------------------------------------------------
    /**
     * Gets the data type for the column.
     *
     * @return the data type for the column
     */
    public String getType()
    {
        return type;
    }


    // ----------------------------------------------------------
    /**
     * Sets the data type for the column.
     *
     * @param value
     *            the data type for the column
     */
    public void setType(String value)
    {
        type = value;
    }


    //~ Static/instance variables .............................................

    private String columnName;
    private String expression;
    private String type;
}
