/*==========================================================================*\
 |  $Id: AddAuthorFixer.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.webcat.oda.commons.ReportMetadata;
import org.webcat.oda.commons.ReportModelProblem;
import org.webcat.oda.designer.i18n.Messages;
import org.webcat.oda.designer.metadata.IReportProblemFixer;

// ------------------------------------------------------------------------
/**
 * A report problem fixer to add an author to the report if there are none.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: AddAuthorFixer.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class AddAuthorFixer extends Composite implements IReportProblemFixer
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    public AddAuthorFixer(Composite parent)
    {
        super(parent, SWT.NONE);

        GridLayout layout = new GridLayout(4, false);
        setLayout(layout);

        GridData gd;

        new Label(this, SWT.NONE).setText(Messages.ADD_AUTHOR_FIXER_NAME);
        nameField = new Text(this, SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        gd.horizontalSpan = 3;
        nameField.setLayoutData(gd);

        new Label(this, SWT.NONE).setText(Messages.ADD_AUTHOR_FIXER_EMAIL);
        emailField = new Text(this, SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        emailField.setLayoutData(gd);

        new Label(this, SWT.NONE).setText(Messages.ADD_AUTHOR_FIXER_URL);
        urlField = new Text(this, SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        urlField.setLayoutData(gd);

        new Label(this, SWT.NONE).setText(Messages.ADD_AUTHOR_FIXER_AFFILIATION);
        affiliationField = new Text(this, SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        affiliationField.setLayoutData(gd);

        new Label(this, SWT.NONE).setText(Messages.ADD_AUTHOR_FIXER_PHONE);
        phoneField = new Text(this, SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        phoneField.setLayoutData(gd);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public Control getTopLevelFixerControl()
    {
        return this;
    }


    // ----------------------------------------------------------
    public void setReportModelProblem(ReportModelProblem problem)
    {
        this.problem = problem;
    }


    // ----------------------------------------------------------
    public void applyFixToModel()
    {
        ModuleHandle module = (ModuleHandle) problem.getHandle();

        ReportMetadata.setAuthorsCount(module, 1);
        ReportMetadata.setAuthorName(module, 1, nameField.getText());
        ReportMetadata.setAuthorEmail(module, 1, emailField.getText());
        ReportMetadata.setAuthorURL(module, 1, urlField.getText());
        ReportMetadata.setAuthorAffiliation(module, 1, affiliationField
                .getText());
        ReportMetadata.setAuthorPhone(module, 1, phoneField.getText());
    }


    //~ Static/instance variables .............................................

    private ReportModelProblem problem;
    private Text nameField;
    private Text emailField;
    private Text urlField;
    private Text affiliationField;
    private Text phoneField;
}
