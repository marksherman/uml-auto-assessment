/*==========================================================================*\
 |  $Id: LicensePropertyFixer.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

package org.webcat.oda.designer.metadata.fixers;

import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.webcat.oda.commons.ReportMetadata;
import org.webcat.oda.commons.ReportModelProblem;
import org.webcat.oda.designer.i18n.Messages;
import org.webcat.oda.designer.metadata.BrowserUtils;
import org.webcat.oda.designer.metadata.IReportProblemFixer;
import org.webcat.oda.designer.metadata.LicenseTable;

// ------------------------------------------------------------------------
/**
 * A report problem fixer for the license and license URL properties.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: LicensePropertyFixer.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class LicensePropertyFixer extends Composite implements
        IReportProblemFixer
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    public LicensePropertyFixer(Composite parent)
    {
        super(parent, SWT.NONE);

        GridLayout layout = new GridLayout(3, false);
        setLayout(layout);
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        Label label = new Label(this, SWT.WRAP);
        label.setText(Messages.LICENSE_FIXER_INSTRUCTION);

        GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd.horizontalSpan = 3;
        label.setLayoutData(gd);

        label = new Label(this, SWT.NONE);
        label.setText(Messages.LICENSE_FIXER_LICENSE);

        licenseField = new Combo(this, SWT.BORDER);

        for (String license : LicenseTable.getInstance().getLicenses())
        {
            licenseField.add(license);
        }

        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd.horizontalSpan = 2;

        licenseField.setLayoutData(gd);

        licenseField.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                String license = licenseField.getText();
                String url =
                    LicenseTable.getInstance().getURLForLicense(license);
                safeSetText(licenseURLField, url);
            }
        });

        label = new Label(this, SWT.NONE);
        label.setText(Messages.LICENSE_FIXER_LICENSE_URL);

        licenseURLField = new Text(this, SWT.BORDER);

        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        licenseURLField.setLayoutData(gd);

        Button goButton = new Button(this, SWT.PUSH);
        goButton.setText(Messages.LICENSE_FIXER_GO);

        goButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if(licenseURLField.getText().length() > 0)
                {
                    BrowserUtils.openURL(licenseURLField.getText());
                }
            }
        });
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void applyFixToModel()
    {
        ModuleHandle module = (ModuleHandle) problem.getHandle();

        ReportMetadata.setLicense(module, licenseField.getText());
        ReportMetadata.setLicenseURL(module, licenseURLField.getText());
    }


    // ----------------------------------------------------------
    public Control getTopLevelFixerControl()
    {
        return this;
    }


    // ----------------------------------------------------------
    public void setReportModelProblem(ReportModelProblem problem)
    {
        this.problem = problem;

        ModuleHandle module = (ModuleHandle) problem.getHandle();

        safeSetText(licenseURLField, ReportMetadata.getLicenseURL(module));
    }


    // ----------------------------------------------------------
    private void safeSetText(Text text, String value)
    {
        if(value == null)
        {
            text.setText(""); //$NON-NLS-1$
        }
        else
        {
            text.setText(value);
        }
    }


    //~ Static/instance variables .............................................

    private ReportModelProblem problem;
    private Combo licenseField;
    private Text licenseURLField;
}
