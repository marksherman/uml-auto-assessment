/*==========================================================================*\
 |  $Id: WebCATInformationHolder.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
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
import java.util.UUID;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.webcat.oda.commons.DataSetDescription;

//------------------------------------------------------------------------
/**
 * A static class that maintains information about the data set currently being
 * edited.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: WebCATInformationHolder.java,v 1.1 2010/05/11 15:52:47 aallowat Exp $
 */
public class WebCATInformationHolder
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Prevent instantiation.
     */
    private WebCATInformationHolder()
    {
        // Static class; prevent instantiation.
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public static String getPropertyValue(String key)
    {
        if (props == null)
            return null;

        return props.getProperty(key);
    }


    // ----------------------------------------------------------
    public static void setPropertyValue(String key, String value)
    {
        if (props == null)
        {
            props = new Properties();
        }

        if (value != null)
            props.setProperty(key, value);
    }


    // ----------------------------------------------------------
    public static void start(DataSetDesign dataSetDesign)
    {
        if (dataSetDesign == null)
            return;

        props = new Properties();

        String queryText = dataSetDesign.getQueryText();
        if (queryText != null && queryText.trim().length() > 0)
        {
            setPropertyValue(Constants.PROP_RELATION_INFORMATION, queryText);

            DataSetDescription info = new DataSetDescription(queryText);
            String entityType = info.getEntityType();
            String dataSetId = info.getUniqueId();

            setPropertyValue(Constants.PROP_ENTITY_TYPE, entityType);
            setPropertyValue(Constants.PROP_DATA_SET_ID, dataSetId);
        }
        else
        {
            // Initialize the data set description with default values.

            DataSetDescription relation = new DataSetDescription();
            relation.setEntityType(DEFAULT_ENTITY);
            relation.setUniqueId(UUID.randomUUID().toString());

            setPropertyValue(Constants.PROP_RELATION_INFORMATION, relation
                    .getQueryText());
        }
    }


    // ----------------------------------------------------------
    public static void destroy()
    {
        props = null;
    }


    // ----------------------------------------------------------
    public static boolean hasDestroyed()
    {
        return props == null;
    }


    //~ Static/instance variables .............................................

    private static final String DEFAULT_ENTITY = "Submission"; //$NON-NLS-1$

    private static Properties props;
}
