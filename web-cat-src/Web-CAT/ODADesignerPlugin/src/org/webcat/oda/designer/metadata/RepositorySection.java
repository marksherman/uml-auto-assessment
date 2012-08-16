/*==========================================================================*\
 |  $Id: RepositorySection.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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


import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.webcat.oda.commons.ReportMetadata;
import org.webcat.oda.designer.i18n.Messages;

//------------------------------------------------------------------------
/**
 * A section on the Overview page that displays information about a report
 * template that was stored when it was uploaded to a Web-CAT template
 * repository.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: RepositorySection.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class RepositorySection extends AbstractSection
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    public RepositorySection(OverviewFormPage formPage, Composite parent,
            FormToolkit toolkit, ModuleHandle model)
    {
        super(formPage, parent, toolkit, model,
                Messages.REPOSITORY_SECTION_TITLE,
                Messages.REPOSITORY_SECTION_DESCRIPTION,
                Section.TWISTIE);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    @Override
    protected void createContent(Composite parent)
    {
        GridLayout layout = new GridLayout(2, false);
        parent.setLayout(layout);

        GridData gd;
        
        createLabel(parent, Messages.REPOSITORY_ID, SWT.CENTER);
        idField = getToolkit().createHyperlink(parent, null, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd.widthHint = 256;
        idField.setLayoutData(gd);
        
        createLabel(parent, Messages.REPOSITORY_VERSION, SWT.CENTER);
        versionField = createFormText(parent, false);
        versionField.setColor("disabled", Display.getCurrent().getSystemColor( //$NON-NLS-1$
                SWT.COLOR_GRAY));

        createLabel(parent, Messages.REPOSITORY_UPLOAD_DATE, SWT.CENTER);
        uploadDateField = createFormText(parent, false);
        uploadDateField.setColor("disabled", Display.getCurrent() //$NON-NLS-1$
                .getSystemColor(SWT.COLOR_GRAY));

        createLabel(parent, Messages.REPOSITORY_ROOT_ID, SWT.CENTER);
        rootIdField = getToolkit().createHyperlink(parent, null, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd.widthHint = 256;
        rootIdField.setLayoutData(gd);

        createLabel(parent, Messages.REPOSITORY_CHANGE_HISTORY, SWT.LEAD);
        changeHistoryField = createFormText(parent, true);
        changeHistoryField.setColor("disabled", Display.getCurrent() //$NON-NLS-1$
                .getSystemColor(SWT.COLOR_GRAY));
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.minimumHeight = 56;
        changeHistoryField.setLayoutData(gd);
    }


    // ----------------------------------------------------------
    public void updateControls()
    {
        String text;

        ModuleHandle module = getModel();

        String id = ReportMetadata.getRepositoryId(module);

        if (id == null)
        {
            idField.setEnabled(false);
            idField.setUnderlined(false);
            idField.setForeground(idField.getDisplay().getSystemColor(
                    SWT.COLOR_GRAY));
            idField.setText(Messages.REPOSITORY_SECTION_NOT_YET_UPLOADED);
        }
        else
        {
            idField.setEnabled(true);
            idField.setUnderlined(true);
            idField.setForeground(idField.getDisplay().getSystemColor(
                    SWT.COLOR_DARK_BLUE));
            idField.setText(id);
        }

        String rootId = ReportMetadata.getRepositoryRootId(module);

        if (rootId == null)
        {
            rootIdField.setEnabled(false);
            rootIdField.setUnderlined(false);
            rootIdField.setForeground(rootIdField.getDisplay().getSystemColor(
                    SWT.COLOR_GRAY));
            rootIdField.setText(Messages.REPOSITORY_SECTION_NOT_YET_UPLOADED);
        }
        else
        {
            rootIdField.setEnabled(true);
            rootIdField.setUnderlined(true);
            rootIdField.setForeground(rootIdField.getDisplay().getSystemColor(
                    SWT.COLOR_DARK_BLUE));
            rootIdField.setText(rootId);
        }

        String version = ReportMetadata.getRepositoryVersion(module);

        if (version == null)
            text = NOT_YET_UPLOADED;
        else
            text = "<form>" + version + "</form>"; //$NON-NLS-1$ //$NON-NLS-2$

        versionField.setText(text, true, true);

        String uploadDate = ReportMetadata.getRepositoryUploadDate(module);

        if (uploadDate == null)
            text = NOT_YET_UPLOADED;
        else
            text = "<form>" + uploadDate + "</form>"; //$NON-NLS-1$ //$NON-NLS-2$

        uploadDateField.setText(text, true, true);

        String changeHistory = ReportMetadata
                .getRepositoryChangeHistory(module);

        if (changeHistory == null)
            text = NOT_YET_UPLOADED;
        else
            text = "<form>" + changeHistory + "</form>"; //$NON-NLS-1$ //$NON-NLS-2$

        changeHistoryField.setText(text, true, true);
    }


    //~ Static/instance variables .............................................

    private static final String NOT_YET_UPLOADED = "<form><p><span color=\"disabled\">"
            + Messages.REPOSITORY_SECTION_NOT_YET_UPLOADED
            + "</span></p></form>"; //$NON-NLS-1$ //$NON-NLS-2$

    private Hyperlink idField;
    private FormText versionField;
    private FormText uploadDateField;
    private Hyperlink rootIdField;
    private FormText changeHistoryField;
}
