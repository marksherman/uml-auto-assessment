/*==========================================================================*\
 |  $Id: DesignerActivator.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

package org.webcat.oda.designer;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.webcat.oda.designer.contentassist.ContentAssistManager;
import org.webcat.oda.designer.i18n.Messages;
import org.webcat.oda.designer.preferences.IPreferencesConstants;
import org.webcat.oda.designer.preview.PreviewQueryManager;
import org.webcat.oda.designer.preview.PreviewingResultCache;

// ------------------------------------------------------------------------
/**
 * The activator class controls the plug-in life cycle.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: DesignerActivator.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class DesignerActivator extends AbstractUIPlugin implements IStartup
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        plugin = this;
    }


    // ----------------------------------------------------------
    public void stop(BundleContext context) throws Exception
    {
        plugin = null;
        super.stop(context);
    }


    // ----------------------------------------------------------
    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static DesignerActivator getDefault()
    {
        return plugin;
    }


    // ----------------------------------------------------------
    /**
     * Force loading of the content assist information in the background when
     * Eclipse starts.
     */
    public void earlyStartup()
    {
        getContentAssistManager();
    }


    // ----------------------------------------------------------
    /**
     * Gets the in-memory cache that stores the results of a preview query
     * operation.
     *
     * @return the previewing result cache
     */
    public PreviewingResultCache getPreviewCache()
    {
        if (previewCache == null)
        {
            Preferences prefs = DesignerActivator.getDefault()
                    .getPluginPreferences();

            // Try to construct a previewing result set provider based on the
            // current preferences settings.
            String url = prefs.getString(IPreferencesConstants.SERVER_URL_KEY);
            String username = prefs
                    .getString(IPreferencesConstants.USERNAME_KEY);
            String password = prefs
                    .getString(IPreferencesConstants.PASSWORD_KEY);
            int maxRecords = 0;
            int timeout = 0;

            try
            {
                maxRecords = Integer.parseInt(prefs
                        .getString(IPreferencesConstants.MAX_RECORDS_KEY));

                timeout = Integer
                        .parseInt(prefs
                                .getString(IPreferencesConstants.CONNECTION_TIMEOUT_KEY));
            }
            catch (NumberFormatException e)
            {
                // Do nothing; keep default value of 0.
            }

            if (url == null || url.trim().length() == 0)
            {
                MessageBox box = new MessageBox(PlatformUI.getWorkbench()
                        .getActiveWorkbenchWindow().getShell(), SWT.OK
                        | SWT.ICON_WARNING);

                box.setMessage(Messages.ACTIVATOR_SERVER_URL_NOT_SET);
                box.open();
            }
            else
            {
                previewCache = new PreviewingResultCache();
                previewCache.setServerCredentials(url, username, password);
                previewCache.setMaxRecords(maxRecords);
                previewCache.setTimeout(timeout);
            }
        }

        return previewCache;
    }


    // ----------------------------------------------------------
    /**
     * Gets the object that manages the content assist information read from
     * the Web-CAT server.
     *
     * @return the content assist manager
     */
    public ContentAssistManager getContentAssistManager()
    {
        if (contentAssistManager == null)
        {
            Preferences prefs = DesignerActivator.getDefault()
                    .getPluginPreferences();
            String url = prefs.getString(IPreferencesConstants.SERVER_URL_KEY);
            String username = prefs
                    .getString(IPreferencesConstants.USERNAME_KEY);
            String password = prefs
                    .getString(IPreferencesConstants.PASSWORD_KEY);

            contentAssistManager = new ContentAssistManager();
            contentAssistManager.setServerCredentials(url, username, password);
        }

        return contentAssistManager;
    }


    // ----------------------------------------------------------
    /**
     * Gets the object that manages stored preview queries in the workspace.
     *
     * @return the preview query manager
     */
    public PreviewQueryManager getPreviewQueryManager()
    {
        if (previewQueryManager == null)
        {
            previewQueryManager = new PreviewQueryManager();
        }

        return previewQueryManager;
    }


    // ----------------------------------------------------------
    /**
     * Convenience function to return an integer representation of the designer
     * plug-in's version identifier. Used to communicate the designer version
     * to Web-CAT for previewing and content-assist operations.
     *
     * @return the integer representation of the designer version
     */
    public int getVersionAsInteger()
    {
        String version = (String) getBundle().getHeaders().get(
                org.osgi.framework.Constants.BUNDLE_VERSION);

        String[] parts = version.split("\\.");

        int major = 0, minor = 0, revision = 0;

        if (parts.length >= 1)
            major = Integer.parseInt(parts[0]);

        if (parts.length >= 2)
            minor = Integer.parseInt(parts[1]);

        if (parts.length >= 3)
            revision = Integer.parseInt(parts[2]);

        return (major * 256 * 256) + (minor * 256) + revision;
    }

    //~ Static/instance variables .............................................

    public static final String PLUGIN_ID = "org.webcat.oda.designer"; //$NON-NLS-1$

    private static DesignerActivator plugin;

    private PreviewingResultCache previewCache;
    private ContentAssistManager contentAssistManager;
    private PreviewQueryManager previewQueryManager;
}
