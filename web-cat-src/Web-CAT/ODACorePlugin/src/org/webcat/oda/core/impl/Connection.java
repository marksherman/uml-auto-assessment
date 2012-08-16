/*==========================================================================*\
 |  $Id: Connection.java,v 1.1 2010/05/11 15:52:44 aallowat Exp $
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

import java.util.Map;
import java.util.Properties;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDataSetMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.webcat.oda.commons.IWebCATResultSetProvider;
import org.webcat.oda.core.Activator;
import org.webcat.oda.core.Constants;
import com.ibm.icu.util.ULocale;

// ------------------------------------------------------------------------
/**
 * Implementation class of IConnection for an ODA runtime driver.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: Connection.java,v 1.1 2010/05/11 15:52:44 aallowat Exp $
 */
public class Connection implements IConnection
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new instance of the Connection class.
     */
    public Connection()
    {
        isOpen = false;
    }


    // ----------------------------------------------------------
    /**
     * Opens the connection and initializes the result set provider that will be
     * used for queries.
     *
     * @param connProperties
     *            additional properties for the connection
     * @throws OdaException
     */
    public void open(Properties connProperties) throws OdaException
    {
        if (isOpen)
            return;

        Object provider = null;

        if (appContext != null)
        {
            provider = appContext.get(Constants.APPCONTEXT_RESULT_SET_PROVIDER);
        }

        if (provider != null && provider instanceof IWebCATResultSetProvider)
        {
            // Use the data providers passed into the app context.

            resultSets = (IWebCATResultSetProvider) provider;
        }
        else
        {
            // If no result set provider was given, try to create one using an
            // extension point in the plug-in.

            try
            {
                resultSets = Activator.getDefault()
                        .getResultSetProviderForEmptyAppContext();
            }
            catch (CoreException e)
            {
                Throwable th = e.getStatus().getException();

                if (th instanceof OdaException)
                    throw (OdaException) th;
            }
        }

        isOpen = true;
    }


    // ----------------------------------------------------------
    /**
     * Sets the application context for this connection.
     *
     * @param context
     *            the application context, which should be a map.
     * @throws OdaException
     */
    public void setAppContext(Object context) throws OdaException
    {
        if (!(context instanceof Map))
        {
            throw new OdaException(
                    "App context must be an instance of java.util.Map.");
        }

        appContext = (Map<?, ?>) context;
    }


    // ----------------------------------------------------------
    /**
     * Sets the locale for the connection. We ignore this.
     *
     * @param locale
     *            the locale for the connection
     * @throws OdaException
     */
    public void setLocale(ULocale locale) throws OdaException
    {
        // Do nothing.
    }


    // ----------------------------------------------------------
    /**
     * Closes the connection.
     *
     * @throws OdaException
     */
    public void close() throws OdaException
    {
        isOpen = false;
    }


    // ----------------------------------------------------------
    /**
     * Gets a value indicating whether the connection is open.
     *
     * @return a boolean value indicating whether the connection is open
     * @throws OdaException
     */
    public boolean isOpen() throws OdaException
    {
        return isOpen;
    }


    // ----------------------------------------------------------
    /**
     * Gets the metadata that describes data sets of the specified type.
     *
     * @param dataSetType
     *            the data set type
     * @return an object that implements the IDataSetMetaData interface
     * @throws OdaException
     */
    public IDataSetMetaData getMetaData(String dataSetType) throws OdaException
    {
        return new DataSetMetaData(this);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new query for the specified data set type.
     *
     * @param dataSetType
     *            the data set type
     * @return an object that implements the IQuery interface
     * @throws OdaException
     */
    public IQuery newQuery(String dataSetType) throws OdaException
    {
        return new Query(resultSets);
    }


    // ----------------------------------------------------------
    /**
     * Gets the maximum number of queries that can be executed on this
     * connection.
     *
     * @return the maximum number of queries
     * @throws OdaException
     */
    public int getMaxQueries() throws OdaException
    {
        return 0; // no limit
    }


    // ----------------------------------------------------------
    public void commit() throws OdaException
    {
        // do nothing; assumes no transaction support needed
    }


    // ----------------------------------------------------------
    public void rollback() throws OdaException
    {
        // do nothing; assumes no transaction support needed
    }


    //~ Static/Instance Variables .............................................

    /**
     * True if the connection is currently open, false if it is closed.
     */
    private boolean isOpen;

    /**
     * The app context passed in from the client code that invokes BIRT.
     */
    private Map<?, ?> appContext;

    /**
     * A result set provider that was either passed in via the app context, or
     * created locally if none was passed in (for previewing purposes).
     */
    private IWebCATResultSetProvider resultSets;
}
