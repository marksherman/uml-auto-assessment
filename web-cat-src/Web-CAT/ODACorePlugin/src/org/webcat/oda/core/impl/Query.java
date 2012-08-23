/*==========================================================================*\
 |  $Id: Query.java,v 1.1 2010/05/11 15:52:44 aallowat Exp $
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
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.SortSpec;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;
import org.webcat.oda.commons.DataSetDescription;
import org.webcat.oda.commons.IWebCATResultSet;
import org.webcat.oda.commons.IWebCATResultSetProvider;
import org.webcat.oda.commons.WebCATDataException;

// ------------------------------------------------------------------------
/**
 * Implementation class of IQuery for an ODA runtime driver. <br>
 * For demo purpose, the auto-generated method stubs have hard-coded
 * implementation that returns a pre-defined set of meta-data and query results.
 * A custom ODA driver is expected to implement own data source specific
 * behavior in its place.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @author $Id: Query.java,v 1.1 2010/05/11 15:52:44 aallowat Exp $
 */
public class Query implements IQuery
{
    //~ Constructors ..........................................................

    // -----------------------------------------------------------
    /**
     * Creates a new query that gets its results from the specified result set
     * provider.
     *
     * @param resultSets
     *            the result set provider from which to get query results
     */
    public Query(IWebCATResultSetProvider resultSets)
    {
        this.resultSets = resultSets;
    }


    //~ Methods ...............................................................

    // -----------------------------------------------------------
    public void prepare(String queryText) throws OdaException
    {
        relation = new DataSetDescription(queryText);

        // Find the Web-CAT result set associated with this query.
        String dataSetId = relation.getUniqueId();
        results = resultSets.resultSetWithId(dataSetId);

        String[] expressions = new String[relation.getColumnCount()];
        for (int i = 0; i < relation.getColumnCount(); i++)
            expressions[i] = relation.getColumnExpression(i);

        try
        {
            results.prepare(relation.getEntityType(), expressions);
        }
        catch (WebCATDataException e)
        {
            throw new OdaException(e.getCause());
        }
    }


    // -----------------------------------------------------------
    public void setAppContext(Object context) throws OdaException
    {
        // do nothing; assumes no support for pass-through context
    }


    // -----------------------------------------------------------
    public void close() throws OdaException
    {
        // do nothing; queries don't contain any resources that need to be
        // released
    }


    // -----------------------------------------------------------
    public IResultSetMetaData getMetaData() throws OdaException
    {
        return new ResultSetMetaData(relation);
    }


    // -----------------------------------------------------------
    public IResultSet executeQuery() throws OdaException
    {
        try
        {
            results.execute();
            IResultSet resultSet = new ResultSet(relation, results);
            resultSet.setMaxRows(getMaxRows());
            return resultSet;
        }
        catch (WebCATDataException e)
        {
            throw new OdaException(e.getCause());
        }
    }


    // -----------------------------------------------------------
    public void setProperty(String name, String value) throws OdaException
    {
        // do nothing; assumes no data set query property
    }


    // -----------------------------------------------------------
    public void setMaxRows(int max) throws OdaException
    {
        maxRows = max;
    }


    // -----------------------------------------------------------
    public int getMaxRows() throws OdaException
    {
        return maxRows;
    }


    // -----------------------------------------------------------
    public void clearInParameters() throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setInt(String parameterName, int value) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setInt(int parameterId, int value) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setDouble(String parameterName, double value)
            throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setDouble(int parameterId, double value) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setBigDecimal(String parameterName, BigDecimal value)
            throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setBigDecimal(int parameterId, BigDecimal value)
            throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setString(String parameterName, String value)
            throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setString(int parameterId, String value) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setDate(String parameterName, Date value) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setDate(int parameterId, Date value) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setTime(String parameterName, Time value) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setTime(int parameterId, Time value) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setTimestamp(String parameterName, Timestamp value)
            throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setTimestamp(int parameterId, Timestamp value)
            throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setBoolean(String parameterName, boolean value)
            throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setBoolean(int parameterId, boolean value) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setNull(String parameterName) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setNull(int parameterId) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public int findInParameter(String parameterName) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public IParameterMetaData getParameterMetaData() throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public void setSortSpec(SortSpec sortBy) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // -----------------------------------------------------------
    public SortSpec getSortSpec() throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // ----------------------------------------------------------
    public void cancel() throws OdaException, UnsupportedOperationException
    {
        // Do nothing.
    }


    // ----------------------------------------------------------
    public String getEffectiveQueryText()
    {
        // Do nothing.
        return null;
    }


    // ----------------------------------------------------------
    public QuerySpecification getSpecification()
    {
        // Do nothing.
        return null;
    }


    // ----------------------------------------------------------
    public void setObject(String arg0, Object arg1) throws OdaException
    {
        // Do nothing.
    }


    // ----------------------------------------------------------
    public void setObject(int arg0, Object arg1) throws OdaException
    {
        // Do nothing.
    }


    // ----------------------------------------------------------
    public void setSpecification(QuerySpecification arg0) throws OdaException,
            UnsupportedOperationException
    {
        // Do nothing.
    }


    //~ Static/Instance Variables .............................................

    /**
     * The maximum number of rows to be returned in a result set generated by
     * this query.
     */
    private int maxRows;

    /**
     * A RelationInformation object that describes the query.
     */
    private DataSetDescription relation;

    /**
     * A result set provider that maintains the result sets for the report that
     * opened this connection.
     */
    private IWebCATResultSetProvider resultSets;

    /**
     * The Web-CAT result set containing the data for this query.
     */
    private IWebCATResultSet results;
}
