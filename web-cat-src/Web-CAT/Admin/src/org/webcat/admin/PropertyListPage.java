/*==========================================================================*\
 |  $Id: PropertyListPage.java,v 1.3 2010/10/26 19:36:36 stedwar2 Exp $
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

package org.webcat.admin;

import java.util.Map;
import java.util.Set;
import com.webobjects.appserver.*;
import com.webobjects.foundation.*;
import er.extensions.appserver.ERXDisplayGroup;
import org.apache.log4j.Logger;
import org.webcat.core.*;

// -------------------------------------------------------------------------
/**
 * A property listing page.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.3 $, $Date: 2010/10/26 19:36:36 $
 */
public class PropertyListPage
    extends WCComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new PropertyList object.
     *
     * @param context The context to use
     */
    public PropertyListPage(WOContext context)
    {
        super(context);
    }


    //~ KVC Attributes (must be public) .......................................

    public Map.Entry<String, String> property;
    public ERXDisplayGroup<Entry>    propertyDisplayGroup;
    public int                       propertyIndex;
    public String                    newPropertyName;
    public String                    newPropertyValue;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public String title()
    {
        return "Application Properties";
    }


    // ----------------------------------------------------------
    /**
     * Access the key associated with the current property.
     * @return The property key as a string
     */
    public String propertyKey()
    {
        return property.getKey();
    }


    // ----------------------------------------------------------
    /**
     * Access the value associated with the current property.
     * @return The property value as a string
     */
    public String propertyValue()
    {
        String result = "";
        String value = property.getValue();
        int pos = 0;
        while (pos < value.length())
        {
            int remaining = value.length() - pos;
            if (remaining > 60)
            {
                result += value.substring(pos, pos + 60) + "<br/>";
                pos += 60;
            }
            else
            {
                result += value.substring(pos, value.length());
                pos += remaining;
            }
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Setup this page before rendering.
     */
    public void awake()
    {
        super.awake();
        Set<?> set = ((Session)session()).properties().inheritedEntrySet();
        @SuppressWarnings("unchecked")
        Map.Entry<String, String>[] entryArray =
            new Map.Entry[set.size()];
        entryArray = set.toArray(entryArray);
        NSMutableArray<Map.Entry<String, String>> entries =
            new NSMutableArray<Map.Entry<String, String>>(entryArray);
        for (int i = 0; i < entries.count(); i++)
        {
            entries.set(i, new Entry(entries.get(i)));
        }
        propertyDisplayGroup.setObjectArray(entries);
    }


    // ----------------------------------------------------------
    public WOComponent back()
    {
        clearMessages();
        return pageWithName(SettingsPage.class);
    }


    // ----------------------------------------------------------
    public WOComponent setNewProperty()
    {
        if (newPropertyName == null || newPropertyName.equals(""))
        {
            error("Please specify a property name to set.");
        }
        else
        {
            if (newPropertyValue == null)
            {
                newPropertyValue = "";
            }
            WCConfigurationFile config = Application.configurationProperties();
            config.setProperty(newPropertyName, newPropertyValue);
            config.attemptToSave();
            // This may be redundant if the file is actually saved and the
            // changes are picked up by the ERExtensions notification
            // listeners, but that may not happen in a production environment,
            // and won't happen if the config file isn't writeable, so we'll
            // be conservative and do it anyway.
            config.updateToSystemProperties();
            confirmationMessage("System property \"" + newPropertyName
                + "\" set to \"" + newPropertyValue + "\".");
        }
        return null;
    }


    // ----------------------------------------------------------
    public static class Entry
        implements Map.Entry<String, String>,
            NSKeyValueCoding
    {
        // ----------------------------------------------------------
        public Entry(java.util.Map.Entry<String, String> inner)
        {
            this.inner = inner;
        }


        // ----------------------------------------------------------
        public String getKey()
        {
            return inner.getKey();
        }


        // ----------------------------------------------------------
        public String getValue()
        {
            return inner.getValue();
        }


        // ----------------------------------------------------------
        public String setValue(String value)
        {
            return inner.setValue(value);
        }


        // ----------------------------------------------------------
        public void takeValueForKey(Object value, String key)
        {
            if (key.equals("value"))
            {
                setValue(value.toString());
            }
            else
            {
                NSKeyValueCoding.DefaultImplementation
                    .handleTakeValueForUnboundKey(this, value, key);
            }
        }


        // ----------------------------------------------------------
        public Object valueForKey(String key)
        {
            if (key.equals("key"))
            {
                return getKey();
            }
            else if (key.equals("value"))
            {
                return getValue();
            }
            else
            {
                return NSKeyValueCoding.DefaultImplementation
                    .handleQueryWithUnboundKey(this, key);
            }
        }


        //~ Instance/static variables .........................................
        private Map.Entry<String, String> inner;
    }


    //~ Instance/static variables .............................................
    static Logger log = Logger.getLogger(PropertyListPage.class);
}
