/*==========================================================================*\
 |  $Id: Driver.java,v 1.2 2010/09/20 14:17:35 aallowat Exp $
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
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.LogConfiguration;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.manifest.DataTypeMapping;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;

// ------------------------------------------------------------------------
/**
 * Implementation class of IDriver for an ODA runtime driver.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: Driver.java,v 1.2 2010/09/20 14:17:35 aallowat Exp $
 */
public class Driver implements IDriver
{
    //~ Methods ...............................................................

    // -----------------------------------------------------------
    public IConnection getConnection(String dataSourceType) throws OdaException
    {
        return new Connection();
    }


    // -----------------------------------------------------------
    public void setLogConfiguration(LogConfiguration logConfig)
            throws OdaException
    {
        // do nothing; assumes simple driver has no logging
    }


    // -----------------------------------------------------------
    public int getMaxConnections() throws OdaException
    {
        return 0; // no limit
    }


    // -----------------------------------------------------------
    public void setAppContext(Object context) throws OdaException
    {
        // do nothing; assumes no support for pass-through context
    }


    // -----------------------------------------------------------
    /**
     * Returns the object that represents this extension's manifest.
     *
     * @throws OdaException
     */
    private static ExtensionManifest getManifest() throws OdaException
    {
        return ManifestExplorer.getInstance().getExtensionManifest(
                ODA_DATA_SOURCE_ID);
    }


    // -----------------------------------------------------------
    /**
     * Returns the native data type name of the specified code, as defined in
     * this data source extension's manifest.
     *
     * @param nativeDataTypeCode
     *            the native data type code
     * @return corresponding native data type name
     * @throws OdaException
     *             if lookup fails
     */
    public static String getNativeDataTypeName(int nativeDataTypeCode)
            throws OdaException
    {
        DataTypeMapping typeMapping = getManifest().getDataSetType(null)
                .getDataTypeMapping(nativeDataTypeCode);
        if(typeMapping != null)
            return typeMapping.getNativeType();
        return "Non-defined";
    }


    //~ Static/instance variables .............................................

    /**
     * The unique identifier of the Web-CAT ODA data source.
     */
    private static final String ODA_DATA_SOURCE_ID =
        "net.sf.webcat.oda.core.dataSource"; //$NON-NLS-1$
}
