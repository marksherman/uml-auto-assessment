/*==========================================================================*\
 |  $Id: DataSetDesignPopulator.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
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

import java.util.Properties;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.webcat.oda.core.impl.Driver;

//------------------------------------------------------------------------
/**
 * Populates the design properties of an ODA data set based on the data set
 * description encoded in the query text.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: DataSetDesignPopulator.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
 */
public class DataSetDesignPopulator
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Prevent instantiation.
     */
    private DataSetDesignPopulator()
    {
        // Static class; prevent instantiation.
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Populates the data set design based on the encoded description in the
     * query text.
     *
     * @param dataSetDesign
     *            the data set design to populate
     */
    public static void populateResultSet(DataSetDesign dataSetDesign)
    {
        IConnection conn = null;

        try
        {
            IDriver jdbcDriver = new Driver();
            conn = jdbcDriver.getConnection(null);

            Properties properties = new Properties();
            conn.open(properties);

            IQuery query = conn.newQuery(null);

            query.setMaxRows(1);
            query.prepare(WebCATInformationHolder
                    .getPropertyValue(Constants.PROP_RELATION_INFORMATION));

            IResultSetMetaData metadata = query.getMetaData();
            setResultSetMetaData(dataSetDesign, metadata);
        }
        catch (OdaException e)
        {
            // no result set definition available, reset in dataSetDesign
            dataSetDesign.setResultSets(null);
        }
        finally
        {
            closeConnection(conn);
        }
    }


    // ----------------------------------------------------------
    /**
     * Close the connection that was opened to populate the data set design.
     *
     * @param conn
     *            the connection
     */
    private static void closeConnection(IConnection conn)
    {
        try
        {
            if (conn != null)
                conn.close();
        }
        catch (OdaException e)
        {
            // Do nothing.
        }

    }


    // ----------------------------------------------------------
    /**
     * Sets the result set metadata for the given data set design.
     *
     * @param dataSetDesign
     *            the data set design
     * @param md
     *            the result set metadata to set
     *
     * @throws OdaException
     *             if an ODA error occurs
     */
    private static void setResultSetMetaData(DataSetDesign dataSetDesign,
            IResultSetMetaData md) throws OdaException
    {
        ResultSetColumns columns = DesignSessionUtil
                .toResultSetColumnsDesign(md);

        ResultSetDefinition resultSetDefn = DesignFactory.eINSTANCE
                .createResultSetDefinition();
        resultSetDefn.setResultSetColumns(columns);

        // no exception; go ahead and assign to specified dataSetDesign
        dataSetDesign.setPrimaryResultSet(resultSetDefn);
        dataSetDesign.getResultSets().setDerivedMetaData(true);
    }
}
