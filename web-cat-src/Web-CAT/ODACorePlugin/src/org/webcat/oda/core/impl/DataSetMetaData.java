/*==========================================================================*\
 |  $Id: DataSetMetaData.java,v 1.1 2010/05/11 15:52:44 aallowat Exp $
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

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSet;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.webcat.oda.core.Constants;

// ------------------------------------------------------------------------
/**
 * Implementation class of IDataSetMetaData for an ODA runtime driver.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: DataSetMetaData.java,v 1.1 2010/05/11 15:52:44 aallowat Exp $
 */
public class DataSetMetaData implements IDataSetMetaData
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new instance of the DataSetMetaData object.
     *
     * @param connection
     */
    public DataSetMetaData(IConnection connection)
    {
        this.connection = connection;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public IConnection getConnection() throws OdaException
    {
        return connection;
    }


    // ----------------------------------------------------------
    public IResultSet getDataSourceObjects(String catalog, String schema,
            String object, String version) throws OdaException
    {
        throw new UnsupportedOperationException();
    }


    // ----------------------------------------------------------
    public int getDataSourceMajorVersion() throws OdaException
    {
        return Constants.DATA_SOURCE_MAJOR_VERSION;
    }


    // ----------------------------------------------------------
    public int getDataSourceMinorVersion() throws OdaException
    {
        return Constants.DATA_SOURCE_MINOR_VERSION;
    }


    // ----------------------------------------------------------
    public String getDataSourceProductName() throws OdaException
    {
        return "Web-CAT Data Source";
    }


    // ----------------------------------------------------------
    public String getDataSourceProductVersion() throws OdaException
    {
        return Integer.toString(getDataSourceMajorVersion()) + "." + //$NON-NLS-1$
                Integer.toString(getDataSourceMinorVersion());
    }


    // ----------------------------------------------------------
    public int getSQLStateType() throws OdaException
    {
        return IDataSetMetaData.sqlStateSQL99;
    }


    // ----------------------------------------------------------
    public boolean supportsMultipleResultSets() throws OdaException
    {
        return false;
    }


    // ----------------------------------------------------------
    public boolean supportsMultipleOpenResults() throws OdaException
    {
        return false;
    }


    // ----------------------------------------------------------
    public boolean supportsNamedResultSets() throws OdaException
    {
        return false;
    }


    // ----------------------------------------------------------
    public boolean supportsNamedParameters() throws OdaException
    {
        return false;
    }


    // ----------------------------------------------------------
    public boolean supportsInParameters() throws OdaException
    {
        return true;
    }


    // ----------------------------------------------------------
    public boolean supportsOutParameters() throws OdaException
    {
        return false;
    }


    // ----------------------------------------------------------
    public int getSortMode()
    {
        return IDataSetMetaData.sortModeNone;
    }


    //~ Static/Instance Variables .............................................

    /**
     * The connection that created this data set metadata object.
     */
    private IConnection connection;
}
