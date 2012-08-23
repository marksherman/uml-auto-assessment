/*==========================================================================*\
 |  $Id: TextPropertyFixer.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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


import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.webcat.oda.commons.DataSetMetadata;
import org.webcat.oda.commons.ReportMetadata;
import org.webcat.oda.commons.ReportModelProblem;
import org.webcat.oda.commons.ReportModelProblemFinder;
import org.webcat.oda.designer.i18n.Messages;
import org.webcat.oda.designer.metadata.IReportProblemFixer;

// ------------------------------------------------------------------------
/**
 * A fixer that provides a text field interface for setting the value of a
 * text property.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: TextPropertyFixer.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class TextPropertyFixer extends Composite implements IReportProblemFixer
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    public TextPropertyFixer(Composite parent, int type, boolean multiline)
    {
        super(parent, SWT.NONE);

        this.type = type;

        GridLayout layout = new GridLayout(1, true);
        setLayout(layout);
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        Label label = new Label(this, SWT.WRAP);
        label.setText(propLabels[type]);

        int textStyle = SWT.BORDER;

        if(multiline)
            textStyle |= SWT.V_SCROLL | SWT.MULTI | SWT.WRAP;

        textField = new Text(this, textStyle);

        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, multiline);

        if(multiline)
            gd.heightHint = 36;

        textField.setLayoutData(gd);
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
        String value = textField.getText();
        String problemDetail = ReportModelProblemFinder.getKeyDetail(
                problem.getKey());

        ModuleHandle module;
        DataSetHandle dataSet;
        int index;

        switch(type)
        {
        case PROP_REPORT_TITLE:
            module = (ModuleHandle) problem.getHandle();
            ReportMetadata.setTitle(module, value);
            break;

        case PROP_REPORT_DESCRIPTION:
            module = (ModuleHandle) problem.getHandle();
            ReportMetadata.setDescription(module, value);
            break;

        case PROP_REPORT_AUTHOR_NAME:
            module = (ModuleHandle) problem.getHandle();
            index = Integer.parseInt(problemDetail);
            ReportMetadata.setAuthorName(module, index, value);
            break;

        case PROP_REPORT_COPYRIGHT:
            module = (ModuleHandle) problem.getHandle();
            ReportMetadata.setCopyright(module, value);
            break;

        case PROP_REPORT_LICENSE:
            module = (ModuleHandle) problem.getHandle();
            ReportMetadata.setLicense(module, value);
            break;

        case PROP_REPORT_DATASET_DESCRIPTION:
            dataSet = (DataSetHandle) problem.getHandle();
            DataSetMetadata.setDescription(dataSet, value);
            break;
        }
    }


    //~ Static/instance variables .............................................

    public static final int PROP_REPORT_TITLE = 0;
    public static final int PROP_REPORT_DESCRIPTION = 1;
    public static final int PROP_REPORT_AUTHOR_NAME = 2;
    public static final int PROP_REPORT_COPYRIGHT = 3;
    public static final int PROP_REPORT_LICENSE = 4;
    public static final int PROP_REPORT_DATASET_DESCRIPTION = 5;

    private static final String[] propLabels = {
        Messages.TEXT_FIXER_ENTER_TITLE,
        Messages.TEXT_FIXER_ENTER_DESCRIPTION,
        Messages.TEXT_FIXER_ENTER_AUTHOR_NAME,
        Messages.TEXT_FIXER_ENTER_COPYRIGHT,
        Messages.TEXT_FIXER_ENTER_LICENSE,
        Messages.TEXT_FIXER_ENTER_DATASET_DESCRIPTION,
    };

    private ReportModelProblem problem;
    private int type;
    private Text textField;
}
