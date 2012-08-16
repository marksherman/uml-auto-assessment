/*==========================================================================*\
 |  $Id: DataSetPreferencePage.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

package org.webcat.oda.designer.preferences;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.webcat.oda.designer.DesignerActivator;
import org.webcat.oda.designer.i18n.Messages;
import org.webcat.oda.designer.preview.PreviewingResultCache;

// ------------------------------------------------------------------------
/**
 * An Eclipse preferences page that allows the user to edit information about
 * the Web-CAT server to use for previewing purposes.
 *
 * @author Tony Allevato
 * @version $Id: DataSetPreferencePage.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class DataSetPreferencePage extends PreferencePage implements
        IWorkbenchPreferencePage
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     *
     */
    @Override
    protected Control createContents(Composite parent)
    {
        Preferences prefs = DesignerActivator.getDefault()
                .getPluginPreferences();

        // Main composite
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);

        // Main page description label
        Label descLabel = new Label(container, SWT.WRAP);
        descLabel.setText(Messages.PREFS_MAIN_LABEL);

        GridData data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        data.widthHint = 400;
        descLabel.setLayoutData(data);

        // Field editor holder
        Composite holder = new Composite(container, SWT.NONE);
        data = new GridData(SWT.FILL, SWT.FILL, true, true);
        holder.setLayoutData(data);

        // Server URL preference editor
        serverURLEditor = new StringFieldEditor(
                IPreferencesConstants.SERVER_URL_KEY,
                Messages.PREFS_SERVER_URL_LABEL, holder);
        serverURLEditor.setPage(this);
        serverURLEditor.setStringValue(prefs
                .getString(IPreferencesConstants.SERVER_URL_KEY));

        // Username preference editor
        usernameEditor = new StringFieldEditor(
                IPreferencesConstants.USERNAME_KEY,
                Messages.PREFS_USERNAME_LABEL, holder);
        usernameEditor.setPage(this);
        usernameEditor.setStringValue(prefs
                .getString(IPreferencesConstants.USERNAME_KEY));

        // Password preference editor
        passwordEditor = new StringFieldEditor(
                IPreferencesConstants.PASSWORD_KEY,
                Messages.PREFS_PASSWORD_LABEL, holder);
        passwordEditor.setPage(this);
        passwordEditor.setStringValue(prefs
                .getString(IPreferencesConstants.PASSWORD_KEY));

        Label sep = new Label(holder, SWT.SEPARATOR | SWT.HORIZONTAL);
        data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        data.horizontalSpan = 2;
        sep.setLayoutData(data);

        descLabel = new Label(holder, SWT.WRAP);
        descLabel.setText(Messages.PREFS_LIMITS_LABEL);

        data = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        data.widthHint = 400;
        data.horizontalSpan = 2;
        descLabel.setLayoutData(data);

        // Maximum records preference editor
        maxRecordsEditor = new IntegerFieldEditor(
                IPreferencesConstants.MAX_RECORDS_KEY,
                Messages.PREFS_MAX_RECORDS_LABEL, holder);
        maxRecordsEditor.setPage(this);
        maxRecordsEditor.setStringValue(prefs
                .getString(IPreferencesConstants.MAX_RECORDS_KEY));

        // Timeout preference editor
        timeoutEditor = new IntegerFieldEditor(
                IPreferencesConstants.CONNECTION_TIMEOUT_KEY,
                Messages.PREFS_TIMEOUT_LABEL, holder);
        timeoutEditor.setPage(this);
        timeoutEditor.setStringValue(prefs
                .getString(IPreferencesConstants.CONNECTION_TIMEOUT_KEY));

        return container;
    }


    // ----------------------------------------------------------
    /**
     *
     */
    public void init(IWorkbench workbench)
    {
        // Do nothing.
    }


    // ----------------------------------------------------------
    /**
     *
     */
    protected void performDefaults()
    {
        serverURLEditor.setStringValue("");
        usernameEditor.setStringValue("");
        passwordEditor.setStringValue("");
        maxRecordsEditor.setStringValue(String.valueOf(DEFAULT_MAX_RECORDS));

        super.performDefaults();
    }


    // ----------------------------------------------------------
    /**
     *
     */
    public boolean performOk()
    {
        Preferences prefs = DesignerActivator.getDefault()
                .getPluginPreferences();

        String url = serverURLEditor.getStringValue();
        String username = usernameEditor.getStringValue();
        String password = passwordEditor.getStringValue();

        int maxRecords = 0, timeout = 0;

        try
        {
            maxRecords = maxRecordsEditor.getIntValue();
        }
        catch (Exception e)
        {
            // Do nothing.
        }

        try
        {
            timeout = timeoutEditor.getIntValue();
        }
        catch (Exception e)
        {
            // Do nothing.
        }

        prefs.setValue(IPreferencesConstants.SERVER_URL_KEY, url);
        prefs.setValue(IPreferencesConstants.USERNAME_KEY, username);
        prefs.setValue(IPreferencesConstants.PASSWORD_KEY, password);
        prefs.setValue(IPreferencesConstants.MAX_RECORDS_KEY, maxRecords);
        prefs.setValue(IPreferencesConstants.CONNECTION_TIMEOUT_KEY, timeout);

        DesignerActivator.getDefault().savePluginPreferences();

        org.webcat.oda.core.Activator.getDefault()
                .refreshResultSetProviderForEmptyAppContext();

        DesignerActivator.getDefault().getContentAssistManager()
                .setServerCredentials(url, username, password);

        PreviewingResultCache previewCache = DesignerActivator.getDefault()
                .getPreviewCache();
        previewCache.setServerCredentials(url, username, password);
        previewCache.setMaxRecords(maxRecords);
        previewCache.setTimeout(timeout);

        return true;
    }


    //~ Static/instance variables .............................................

    public static final int DEFAULT_MAX_RECORDS = 500;

    private StringFieldEditor serverURLEditor;
    private StringFieldEditor usernameEditor;
    private StringFieldEditor passwordEditor;
    private IntegerFieldEditor maxRecordsEditor;
    private IntegerFieldEditor timeoutEditor;
}
