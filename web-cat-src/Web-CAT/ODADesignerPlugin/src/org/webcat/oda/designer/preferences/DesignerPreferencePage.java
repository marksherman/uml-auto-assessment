/*==========================================================================*\
 |  $Id: DesignerPreferencePage.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.webcat.oda.designer.DesignerActivator;
import org.webcat.oda.designer.i18n.Messages;

// ------------------------------------------------------------------------
/**
 * A preferences page containing settings that deal with behavior of the
 * report designer.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: DesignerPreferencePage.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class DesignerPreferencePage extends PreferencePage implements
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
        Preferences prefs = DesignerActivator.getDefault().getPluginPreferences();

        // Main composite
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);

        // Main page description label
        Label descLabel = new Label(container, SWT.WRAP);
        descLabel.setText(Messages.DESIGNER_PREFS_PROBLEM_MESSAGE);

        //
        saveShowAllButton = new Button(container, SWT.RADIO);
        saveShowAllButton.setText(Messages.DESIGNER_PREFS_SHOW_ALL_PROBLEMS);

        saveShowErrorsButton = new Button(container, SWT.RADIO);
        saveShowErrorsButton.setText(Messages.DESIGNER_PREFS_SHOW_ERRORS_ONLY);

        saveShowNothingButton = new Button(container, SWT.RADIO);
        saveShowNothingButton.setText(Messages.DESIGNER_PREFS_SHOW_NO_PROBLEMS);

        int saveBehavior = prefs.getInt(IPreferencesConstants.SAVE_BEHAVIOR_KEY);

        switch(saveBehavior)
        {
        case IPreferencesConstants.SAVE_BEHAVIOR_SHOW_ALL_PROBLEMS:
            saveShowAllButton.setSelection(true);
            break;

        case IPreferencesConstants.SAVE_BEHAVIOR_SHOW_ERRORS_ONLY:
            saveShowErrorsButton.setSelection(true);
            break;

        case IPreferencesConstants.SAVE_BEHAVIOR_SHOW_NO_PROBLEMS:
            saveShowNothingButton.setSelection(true);
            break;
        }

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
        saveShowAllButton.setSelection(true);

        super.performDefaults();
    }


    // ----------------------------------------------------------
    /**
     *
     */
    public boolean performOk()
    {
        Preferences prefs = DesignerActivator.getDefault().getPluginPreferences();

        int saveBehavior = IPreferencesConstants.SAVE_BEHAVIOR_SHOW_ALL_PROBLEMS;

        if(saveShowAllButton.getSelection())
        {
            saveBehavior = IPreferencesConstants.SAVE_BEHAVIOR_SHOW_ALL_PROBLEMS;
        }
        else if(saveShowErrorsButton.getSelection())
        {
            saveBehavior = IPreferencesConstants.SAVE_BEHAVIOR_SHOW_ERRORS_ONLY;
        }
        else if(saveShowNothingButton.getSelection())
        {
            saveBehavior = IPreferencesConstants.SAVE_BEHAVIOR_SHOW_NO_PROBLEMS;
        }

        prefs.setValue(IPreferencesConstants.SAVE_BEHAVIOR_KEY, saveBehavior);

        DesignerActivator.getDefault().savePluginPreferences();

        return true;
    }


    //~ Static/instance variables .............................................

    private Button saveShowAllButton;
    private Button saveShowErrorsButton;
    private Button saveShowNothingButton;
}
