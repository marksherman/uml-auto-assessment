/*==========================================================================*\
 |  $Id: PreviewingResultSetProvider.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

package org.webcat.oda.designer.preview;


import org.eclipse.core.runtime.Preferences;
import org.webcat.oda.commons.IWebCATResultSet;
import org.webcat.oda.commons.IWebCATResultSetProvider;
import org.webcat.oda.designer.DesignerActivator;
import org.webcat.oda.designer.preferences.IPreferencesConstants;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: PreviewingResultSetProvider.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class PreviewingResultSetProvider implements IWebCATResultSetProvider
{
    // === Methods ============================================================

    // ------------------------------------------------------------------------
    /**
     * Creates a new instance of the PreviewingResultSetProvider class.
     *
     * @param url
     *            the URL of the Web-CAT server to obtain preview data from
     * @param username
     *            the username used to log in to Web-CAT
     * @param password
     *            the password used to log in to Web-CAT
     * @param maxRecords
     *            the maximum number of records to preview
     */
    public PreviewingResultSetProvider()
    {
        Preferences prefs = DesignerActivator.getDefault().getPluginPreferences();

        // Try to construct a previewing result set provider based on the
        // current preferences settings.
        int maxRecords = 0;

        try
        {
            maxRecords = Integer.parseInt(prefs
                    .getString(IPreferencesConstants.MAX_RECORDS_KEY));
        }
        catch (NumberFormatException e)
        {
            // Ignore exception; retain original value of 0.
        }

        this.maxRecords = maxRecords;
    }


    // ------------------------------------------------------------------------
    /**
     * Gets the result set with the specified name.
     *
     * @param id
     *            the unique identifier of the result set to return
     *
     * @return an IWebCATResultSet object that allows the caller to iterate over
     *         the results for the data set with the given name
     */
    public IWebCATResultSet resultSetWithId(String id)
    {
        return new PreviewingResultSet(id, maxRecords);
    }


    // === Instance Variables =================================================

    /**
     * The maximum number of records to return in a preview.
     */
    private int maxRecords;
}
