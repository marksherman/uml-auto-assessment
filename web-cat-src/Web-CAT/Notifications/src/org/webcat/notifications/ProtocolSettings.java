/*==========================================================================*\
 |  $Id: ProtocolSettings.java,v 1.2 2010/09/27 00:40:53 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
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

package org.webcat.notifications;

import org.webcat.core.MutableDictionary;
import org.webcat.core.User;
import org.webcat.core.messaging.IMessageSettings;
import com.webobjects.eocontrol.EOEditingContext;

// -------------------------------------------------------------------------
/**
 * TODO: place a real description here.
 *
 * @author  Tony Allevato
 * @author  Latest changes by: $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2010/09/27 00:40:53 $
 */
public class ProtocolSettings
    extends _ProtocolSettings
    implements IMessageSettings
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new ProtocolSettings object.
     */
    public ProtocolSettings()
    {
        super();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the system-wide broadcast protocol settings as defined by the
     * system administrator.
     *
     * This object will always have id 1; it is assumed that the Application
     * class has made sure that this object exists during its initialization.
     *
     * @param ec the editing context
     * @return the system-wide broadcast protocol settings, or null if it does
     *     not exist
     */
    public static ProtocolSettings systemSettings(EOEditingContext ec)
    {
        return ProtocolSettings.forId(ec, 1);
    }


    // ----------------------------------------------------------
    /**
     * Retrieves the protocol settings for the given user. If no settings
     * object yet exists for this user, an empty one is created as associated
     * with them.
     *
     * @param theUser the user
     * @return the protocol settings for the user
     */
    public static ProtocolSettings protocolSettingsForUser(
            User theUser)
    {
        ProtocolSettings userSettings =
            ProtocolSettings.uniqueObjectMatchingQualifier(
                theUser.editingContext(),
                ProtocolSettings.user.is(theUser));

        if (userSettings == null)
        {
            userSettings = ProtocolSettings.create(
                theUser.editingContext(), false);
            userSettings.setUserRelationship(theUser);
            theUser.editingContext().saveChanges();
        }

        return userSettings;
    }


    // ----------------------------------------------------------
    public Object settingForKey(String key)
    {
        Object value = settings().objectForKey(key);

        if (value == null && parent() != null)
        {
            value = parent().settingForKey(key);
        }

        return value;
    }


    // ----------------------------------------------------------
    public String stringSettingForKey(String key, String defaultValue)
    {
        String value = (String) settingForKey(key);
        return value == null ? defaultValue : value;
    }


    // ----------------------------------------------------------
    public boolean booleanSettingForKey(String key, boolean defaultValue)
    {
        Boolean value = (Boolean) settingForKey(key);
        return value == null ? defaultValue : value;
    }


    // ----------------------------------------------------------
    public int intSettingForKey(String key, int defaultValue)
    {
        Number value = (Number) settingForKey(key);
        return value == null ? defaultValue : value.intValue();
    }


    // ----------------------------------------------------------
    public double doubleSettingForKey(String key, double defaultValue)
    {
        Number value = (Number) settingForKey(key);
        return value == null ? defaultValue : value.doubleValue();
    }


    // ----------------------------------------------------------
    public MutableDictionary settingsSnapshot()
    {
        MutableDictionary snapshot = new MutableDictionary();
        gatherSettingsForSnapshot(snapshot);
        return snapshot;
    }


    // ----------------------------------------------------------
    private void gatherSettingsForSnapshot(MutableDictionary snapshot)
    {
        // First, get the parent's settings.

        if (parent() != null)
        {
            parent().gatherSettingsForSnapshot(snapshot);
        }

        // Then, replace settings that the child has overridden.

        snapshot.addEntriesFromDictionary(settings());
    }
}
