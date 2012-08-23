/*==========================================================================*\
 |  $Id: CopyrightLicenseSection.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.webcat.oda.commons.ReportMetadata;
import org.webcat.oda.designer.i18n.Messages;

//------------------------------------------------------------------------
/**
 * A section on the Overview page that edits properties related to the copyright
 * and license of a report template.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: CopyrightLicenseSection.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class CopyrightLicenseSection extends AbstractSection
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    public CopyrightLicenseSection(OverviewFormPage formPage, Composite parent,
            FormToolkit toolkit, ModuleHandle model)
    {
        super(
                formPage,
                parent,
                toolkit,
                model,
                Messages.COPYRIGHT_SECTION_TITLE,
                Messages.COPYRIGHT_SECTION_DESCRIPTION);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    @Override
    protected void createContent(Composite parent)
    {
        GridLayout layout = new GridLayout(2, false);
        parent.setLayout(layout);

        createLabel(parent, Messages.COPYRIGHT_COPYRIGHT, SWT.CENTER);
        copyrightField = createText(parent, false, SWT.NONE);
        setGridWidthHint(copyrightField, 64);
        
        createLabel(parent, Messages.COPYRIGHT_LICENSE, SWT.CENTER);
        licenseField = createCombo(parent);
        setGridWidthHint(licenseField, 64);

        for (String license : LicenseTable.getInstance().getLicenses())
        {
            licenseField.add(license);
        }

        licenseField.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                if (licenseField.getSelectionIndex() != -1)
                {
                    String license = licenseField.getText();

                    licenseURLField.setText(LicenseTable.getInstance()
                            .getURLForLicense(license));
                }
            }
        });

        createLabel(parent, Messages.COPYRIGHT_LICENSE_URL, SWT.CENTER);

        Composite urlComp = createGridComposite(parent, 2, false);

        licenseURLField = createText(urlComp, false, SWT.NONE);
        setGridWidthHint(licenseURLField, 64);

        licenseURLGoButton = createButton(urlComp, Messages.COPYRIGHT_GO, null);
        licenseURLGoButton.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent e)
            {
                BrowserUtils.openURL(licenseURLField.getText());
            }
        });
    }


    // ----------------------------------------------------------
    @Override
    public void updateControls()
    {
        ModuleHandle module = getModel();

        safeSetText(copyrightField, ReportMetadata.getCopyright(module));
        safeSetText(licenseField, ReportMetadata.getLicense(module));
        safeSetText(licenseURLField, ReportMetadata.getLicenseURL(module));
    }


    // ----------------------------------------------------------
    @Override
    public void saveModel()
    {
        ModuleHandle module = getModel();

        ReportMetadata.setCopyright(module, copyrightField.getText());
        ReportMetadata.setLicense(module, licenseField.getText());
        ReportMetadata.setLicenseURL(module, licenseURLField.getText());
    }


    //~ Static/instance variables .............................................

    private Text copyrightField;
    private Combo licenseField;
    private Text licenseURLField;
    private Button licenseURLGoButton;
}
