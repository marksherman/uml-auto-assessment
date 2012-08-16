/*==========================================================================*\
 |  $Id: LanguageTable.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

package org.webcat.oda.designer.metadata;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import com.ibm.icu.util.ULocale;

//------------------------------------------------------------------------
/**
 * A singleton that maintains a list of the languages and locales available on
 * the system.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: LanguageTable.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class LanguageTable
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    private LanguageTable()
    {
        displayNameMap = new Hashtable<String, String>();

        ULocale[] locales = ULocale.getAvailableLocales();

        for (ULocale locale : locales)
        {
            String rfcName = locale.getName().replace('_', '-');
            displayNameMap.put(locale.getDisplayName(), rfcName);
        }
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public static LanguageTable getInstance()
    {
        if (instance == null)
        {
            instance = new LanguageTable();
        }

        return instance;
    }


    // ----------------------------------------------------------
    public String[] getDisplayNames()
    {
        String[] displayNames = new String[displayNameMap.size()];
        displayNameMap.keySet().toArray(displayNames);
        Arrays.sort(displayNames, String.CASE_INSENSITIVE_ORDER);

        return displayNames;
    }


    // ----------------------------------------------------------
    public String getNameForDisplayName(String displayName)
    {
        return displayNameMap.get(displayName);
    }


    // ----------------------------------------------------------
    public String getDisplayNameForName(String name)
    {
        name = name.replace('-', '_');

        ULocale locale = new ULocale(name);
        return locale.getDisplayName();
    }


    //~ Static/instance variables .............................................

    private static LanguageTable instance;

    private Map<String, String> displayNameMap;
}
