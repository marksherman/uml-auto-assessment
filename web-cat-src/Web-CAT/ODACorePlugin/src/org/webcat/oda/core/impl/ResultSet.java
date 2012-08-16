/*==========================================================================*\
 |  $Id: ResultSet.java,v 1.1 2010/05/11 15:52:44 aallowat Exp $
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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import org.eclipse.datatools.connectivity.oda.IBlob;
import org.eclipse.datatools.connectivity.oda.IClob;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.webcat.oda.commons.DataSetDescription;
import org.webcat.oda.commons.IWebCATResultSet;
import org.webcat.oda.commons.WebCATDataException;

// ------------------------------------------------------------------------
/**
 * Implementation class of IResultSet for an ODA runtime driver.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: ResultSet.java,v 1.1 2010/05/11 15:52:44 aallowat Exp $
 */
public class ResultSet implements IResultSet
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new ResultSet.
     *
     * @param relation
     *            the RelationInformation object that describes the query that
     *            created this result set
     * @param results
     *            the IWebCATResultSet object that is the source of the data for
     *            this result set
     */
    public ResultSet(DataSetDescription relation, IWebCATResultSet results)
    {
        this.relation = relation;
        this.results = results;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public IResultSetMetaData getMetaData() throws OdaException
    {
        return new ResultSetMetaData(relation);
    }


    // ----------------------------------------------------------
    public void setMaxRows(int max) throws OdaException
    {
        maxRows = max;
    }


    // ----------------------------------------------------------
    /**
     * Returns the maximum number of rows that can be fetched from this result
     * set.
     *
     * @return the maximum number of rows to fetch.
     */
    protected int getMaxRows()
    {
        return maxRows;
    }


    // ----------------------------------------------------------
    public boolean next() throws OdaException
    {
        try
        {
            int maxRows = getMaxRows();

            if (maxRows == 0 || results.currentRow() < maxRows)
            {
                return results.moveToNextRow();
            }
            else
            {
                return false;
            }
        }
        catch (WebCATDataException e)
        {
            throw new OdaException(e.getCause());
        }
    }


    // ----------------------------------------------------------
    public void close() throws OdaException
    {
        try
        {
            results.close();
        }
        catch (WebCATDataException e)
        {
            throw new OdaException(e.getCause());
        }
    }


    // ----------------------------------------------------------
    public int getRow() throws OdaException
    {
        try
        {
            return results.currentRow() + 1;
        }
        catch (WebCATDataException e)
        {
            throw new OdaException(e.getCause());
        }
    }


    // ----------------------------------------------------------
    public String getString(int index) throws OdaException
    {
        try
        {
            return results.stringValueAtIndex(index - 1);
        }
        catch (WebCATDataException e)
        {
            throw new OdaException(e.getCause());
        }
    }


    // ----------------------------------------------------------
    public String getString(String columnName) throws OdaException
    {
        return getString(findColumn(columnName));
    }


    // ----------------------------------------------------------
    public int getInt(int index) throws OdaException
    {
        try
        {
            return results.intValueAtIndex(index - 1);
        }
        catch (WebCATDataException e)
        {
            throw new OdaException(e.getCause());
        }
    }


    // ----------------------------------------------------------
    public int getInt(String columnName) throws OdaException
    {
        return getInt(findColumn(columnName));
    }


    // ----------------------------------------------------------
    public double getDouble(int index) throws OdaException
    {
        try
        {
            return results.doubleValueAtIndex(index - 1);
        }
        catch (WebCATDataException e)
        {
            throw new OdaException(e.getCause());
        }
    }


    // ----------------------------------------------------------
    public double getDouble(String columnName) throws OdaException
    {
        return getDouble(findColumn(columnName));
    }


    // ----------------------------------------------------------
    public BigDecimal getBigDecimal(int index) throws OdaException
    {
        try
        {
            return results.decimalValueAtIndex(index - 1);
        }
        catch (WebCATDataException e)
        {
            throw new OdaException(e.getCause());
        }
    }


    // ----------------------------------------------------------
    public BigDecimal getBigDecimal(String columnName) throws OdaException
    {
        return getBigDecimal(findColumn(columnName));
    }


    // ----------------------------------------------------------
    public Date getDate(int index) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // ----------------------------------------------------------
    public Date getDate(String columnName) throws OdaException
    {
        return getDate(findColumn(columnName));
    }


    // ----------------------------------------------------------
    public Time getTime(int index) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // ----------------------------------------------------------
    public Time getTime(String columnName) throws OdaException
    {
        return getTime(findColumn(columnName));
    }


    // ----------------------------------------------------------
    public Timestamp getTimestamp(int index) throws OdaException
    {
        try
        {
            return results.timestampValueAtIndex(index - 1);
        }
        catch (WebCATDataException e)
        {
            throw new OdaException(e.getCause());
        }
    }


    // ----------------------------------------------------------
    public Timestamp getTimestamp(String columnName) throws OdaException
    {
        return getTimestamp(findColumn(columnName));
    }


    // ----------------------------------------------------------
    public IBlob getBlob(int index) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // ----------------------------------------------------------
    public IBlob getBlob(String columnName) throws OdaException
    {
        return getBlob(findColumn(columnName));
    }


    // ----------------------------------------------------------
    public IClob getClob(int index) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // ----------------------------------------------------------
    public IClob getClob(String columnName) throws OdaException
    {
        return getClob(findColumn(columnName));
    }


    // ----------------------------------------------------------
    public boolean getBoolean(int index) throws OdaException
    {
        try
        {
            return results.booleanValueAtIndex(index - 1);
        }
        catch (WebCATDataException e)
        {
            throw new OdaException(e.getCause());
        }
    }


    // ----------------------------------------------------------
    public boolean getBoolean(String columnName) throws OdaException
    {
        return getBoolean(findColumn(columnName));
    }


    // ----------------------------------------------------------
    public Object getObject(int index) throws OdaException
    {
        // Do nothing.
        return null;
    }


    // ----------------------------------------------------------
    public Object getObject(String columnName) throws OdaException
    {
        // Do nothing.
        return null;
    }


    // ----------------------------------------------------------
    public boolean wasNull() throws OdaException
    {
        try
        {
            return results.wasValueNull();
        }
        catch (WebCATDataException e)
        {
            throw new OdaException(e.getCause());
        }
    }


    // ----------------------------------------------------------
    public int findColumn(String columnName) throws OdaException
    {
        for (int i = 0; i < relation.getColumnCount(); i++)
            if (relation.getColumnName(i).equals(columnName))
                return i + 1;

        // Return a dummy value if the column wasn't found.
        return 1;
    }


    //~ Static/Instance Variables .............................................

    /**
     * The maximum number of rows to retrieve from the server.
     */
    private int maxRows;

    /**
     * The relation information that describes the query, including its entity
     * type and columns to retrieve.
     */
    private DataSetDescription relation;

    /**
     * An object containing the result set of the query.
     */
    private IWebCATResultSet results;
}
