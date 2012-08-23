/*==========================================================================*\
 |  $Id: ResultSetMetaData.java,v 1.1 2010/05/11 15:52:44 aallowat Exp $
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

import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.webcat.oda.commons.DataSetDescription;

// ------------------------------------------------------------------------
/**
 * Implementation class of IResultSetMetaData for an ODA runtime driver.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: ResultSetMetaData.java,v 1.1 2010/05/11 15:52:44 aallowat Exp $
 */
public class ResultSetMetaData implements IResultSetMetaData
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    public ResultSetMetaData(DataSetDescription relation)
    {
        this.relation = relation;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public int getColumnCount() throws OdaException
    {
        return relation.getColumnCount();
    }


    // ----------------------------------------------------------
    public String getColumnName(int index) throws OdaException
    {
        return relation.getColumnName(index - 1);
    }


    // ----------------------------------------------------------
    public String getColumnLabel(int index) throws OdaException
    {
        return getColumnName(index);
    }


    // ----------------------------------------------------------
    public int getColumnType(int index) throws OdaException
    {
        return DataTypes.getType(relation.getColumnType(index - 1));
    }


    // ----------------------------------------------------------
    public String getColumnTypeName(int index) throws OdaException
    {
        int nativeTypeCode = getColumnType(index);
        return Driver.getNativeDataTypeName(nativeTypeCode);
    }


    // ----------------------------------------------------------
    public int getColumnDisplayLength(int index) throws OdaException
    {
        return -1;
    }


    // ----------------------------------------------------------
    public int getPrecision(int index) throws OdaException
    {
        return -1;
    }


    // ----------------------------------------------------------
    public int getScale(int index) throws OdaException
    {
        return -1;
    }


    // ----------------------------------------------------------
    public int isNullable(int index) throws OdaException
    {
        return IResultSetMetaData.columnNullableUnknown;
    }


    //~ Static/Instance Variables .............................................

    /**
     * Relational information about the query that owns this result set metadata
     * object.
     */
    private DataSetDescription relation;
}
