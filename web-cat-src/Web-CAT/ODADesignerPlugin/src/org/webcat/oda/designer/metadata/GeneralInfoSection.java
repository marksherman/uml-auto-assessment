/*==========================================================================*\
 |  $Id: GeneralInfoSection.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.webcat.oda.commons.ReportMetadata;
import org.webcat.oda.designer.i18n.Messages;

//------------------------------------------------------------------------
/**
 * A section on the Overview page that edits general properties about the report
 * template such as its title and description.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: GeneralInfoSection.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class GeneralInfoSection extends AbstractSection
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    public GeneralInfoSection(OverviewFormPage formPage, Composite parent,
            FormToolkit toolkit, ModuleHandle model)
    {
        super(formPage, parent, toolkit, model,
                Messages.GENERAL_INFO_SECTION_TITLE,
                Messages.GENERAL_INFO_SECTION_DESCRIPTION);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    @Override
    protected void createContent(Composite parent)
    {
        GridLayout layout = new GridLayout(2, false);
        parent.setLayout(layout);

        createLabel(parent, Messages.GENERAL_INFO_TITLE, SWT.CENTER);
        titleField = createText(parent, false, SWT.NONE);
        titleField.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                if (titleField.getText().length() == 0)
                {
                    addMessage(EMPTY_TITLE_KEY, EMPTY_TITLE_MESSAGE, null,
                            IMessageProvider.ERROR, titleField);
                }
                else
                {
                    removeMessage(EMPTY_TITLE_KEY, titleField);
                }
            }
        });

        createLabel(parent, Messages.GENERAL_INFO_DESCRIPTION, SWT.LEAD);
        descriptionField = createText(parent, true, SWT.NONE, 56);
        descriptionField.addModifyListener(new ModifyListener()
        {
            public void modifyText(ModifyEvent e)
            {
                if (descriptionField.getText().length() == 0)
                {
                    addMessage(EMPTY_DESCRIPTION_KEY,
                            EMPTY_DESCRIPTION_MESSAGE, null,
                            IMessageProvider.ERROR, descriptionField);
                }
                else
                {
                    removeMessage(EMPTY_DESCRIPTION_KEY, descriptionField);
                }
            }
        });

        createLabel(parent, Messages.GENERAL_INFO_KEYWORDS, SWT.LEAD);
        keywordsField = createText(parent, true, SWT.NONE, 32);
    }


    // ----------------------------------------------------------
    public void updateControls()
    {
        ModuleHandle module = getModel();

        safeSetText(titleField, ReportMetadata.getTitle(module));
        safeSetText(descriptionField, ReportMetadata.getDescription(module));
        safeSetText(keywordsField, ReportMetadata.getKeywords(module));
    }


    // ----------------------------------------------------------
    public void saveModel()
    {
        ModuleHandle module = getModel();

        ReportMetadata.setTitle(module, titleField.getText());
        ReportMetadata.setDescription(module, descriptionField.getText());
        ReportMetadata.setKeywords(module, keywordsField.getText());
    }


    //~ Static/instance variables .............................................

    private static final String EMPTY_TITLE_KEY = "generalInfo.emptyTitle"; //$NON-NLS-1$
    private static final String EMPTY_TITLE_MESSAGE = Messages.GENERAL_INFO_ERROR_EMPTY_TITLE;

    private static final String EMPTY_DESCRIPTION_KEY = "generalInfo.emptyDescription"; //$NON-NLS-1$
    private static final String EMPTY_DESCRIPTION_MESSAGE = Messages.GENERAL_INFO_ERROR_EMPTY_DESCRIPTION;

    private Text titleField;
    private Text descriptionField;
    private Text keywordsField;
}
