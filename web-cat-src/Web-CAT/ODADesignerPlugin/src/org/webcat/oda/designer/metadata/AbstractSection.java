/*==========================================================================*\
 |  $Id: AbstractSection.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

//------------------------------------------------------------------------
/**
 * The base class for all of the form sections that are used in the Web-CAT
 * report designer form pages.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: AbstractSection.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public abstract class AbstractSection extends SectionPart
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a section.
     *
     * @param formPage
     *            the form page that will own this section
     * @param parent
     *            the Composite container that should be the parent of this
     *            section
     * @param toolkit
     *            the form toolkit of the form page
     * @param model
     *            the report design that this section will edit
     * @param title
     *            the title of the section
     * @param description
     *            the description of the section
     */
    public AbstractSection(OverviewFormPage formPage, Composite parent,
            FormToolkit toolkit, ModuleHandle model, String title,
            String description)
    {
        this(formPage, parent, toolkit, model, title, description, 0);
    }


    // ----------------------------------------------------------
    /**
     * Creates a section.
     *
     * @param formPage
     *            the form page that will own this section
     * @param parent
     *            the Composite container that should be the parent of this
     *            section
     * @param toolkit
     *            the form toolkit of the form page
     * @param model
     *            the report design that this section will edit
     * @param title
     *            the title of the section
     * @param description
     *            the description of the section
     * @param style
     *            SWT styles to apply to this section widget
     */
    public AbstractSection(OverviewFormPage formPage, Composite parent,
            FormToolkit toolkit, ModuleHandle model, String title,
            String description, int style)
    {
        super(parent, toolkit, Section.TITLE_BAR | Section.DESCRIPTION | style);

        this.formPage = formPage;
        this.model = model;
        this.toolkit = toolkit;

        Section section = getSection();

        section.setText(title);
        section.setDescription(description);

        Composite client = getToolkit().createComposite(section);
        createContent(client);
        section.setClient(client);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Subclasses should override this method to create the controls that will
     * make up this section.
     *
     * @param parent
     *            the parent of the controls
     */
    protected abstract void createContent(Composite parent);


    // ----------------------------------------------------------
    /**
     * Subclasses should override this method to populate their controls with
     * values from the report model.
     */
    public void updateControls()
    {
        // Implement in subclasses if necessary.
    }


    // ----------------------------------------------------------
    /**
     * Subclasses should override this method to populate the report model with
     * values from their controls.
     */
    public void saveModel()
    {
        // Implement in subclasses if necessary.
    }


    // ----------------------------------------------------------
    protected Label createLabel(Composite parent, String text, int valign)
    {
        Label label = getToolkit().createLabel(parent, text);
        label.setForeground(getToolkit().getColors()
                .getColor(IFormColors.TITLE));

        GridData gd = new GridData(SWT.LEAD, valign, false, false);
        label.setLayoutData(gd);

        return label;
    }


    // ----------------------------------------------------------
    protected FormText createFormText(Composite parent, boolean vFill)
    {
        FormText formText = new FormText(parent, SWT.NONE);

        int valign = vFill ? SWT.FILL : SWT.CENTER;

        GridData gd = new GridData(SWT.FILL, valign, true, vFill);
        formText.setLayoutData(gd);

        return formText;
    }


    // ----------------------------------------------------------
    protected Text createText(Composite parent, boolean multiline, int style)
    {
        return createText(parent, multiline, style, SWT.DEFAULT, null);
    }


    // ----------------------------------------------------------
    protected Text createText(Composite parent, boolean multiline, int style,
            TrackingFocusListener focusListener)
    {
        return createText(parent, multiline, style, SWT.DEFAULT, focusListener);
    }


    // ----------------------------------------------------------
    protected Text createText(Composite parent, boolean multiline, int style,
            int height)
    {
        return createText(parent, multiline, style, height, null);
    }


    // ----------------------------------------------------------
    protected Text createText(Composite parent, boolean multiline, int style,
            int height, TrackingFocusListener focusListener)
    {
        return createText(parent, multiline, style, height, 1, focusListener);
    }


    // ----------------------------------------------------------
    protected Text createText(Composite parent, boolean multiline, int style,
            int height, int colSpan)
    {
        return createText(parent, multiline, style, height, colSpan, null);
    }


    // ----------------------------------------------------------
    protected Text createText(Composite parent, boolean multiline, int style,
            int height, int colSpan, TrackingFocusListener focusListener)
    {
        style |= SWT.BORDER;

        if (multiline)
        {
            style |= SWT.MULTI | SWT.WRAP | SWT.V_SCROLL;
        }

        int valign = multiline ? SWT.FILL : SWT.CENTER;

        final Text text = getToolkit().createText(parent, "", style); //$NON-NLS-1$
        GridData gd = new GridData(SWT.FILL, valign, true, false);
        gd.widthHint = 30;
        gd.heightHint = height;
        gd.horizontalSpan = colSpan;
        text.setLayoutData(gd);

        if (focusListener == null)
        {
            focusListener = new TrackingFocusListener(getFormPage());
        }

        text.addFocusListener(focusListener);

        return text;
    }


    // ----------------------------------------------------------
    protected Combo createCombo(Composite parent)
    {
        return createCombo(parent, null);
    }


    // ----------------------------------------------------------
    protected Combo createCombo(Composite parent,
            TrackingFocusListener focusListener)
    {
        Combo combo = new Combo(parent, SWT.NONE);
        GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        combo.setLayoutData(gd);

        if (focusListener == null)
        {
            focusListener = new TrackingFocusListener(getFormPage());
        }

        combo.addFocusListener(focusListener);

        return combo;
    }


    // ----------------------------------------------------------
    protected Button createButton(Composite parent, String text,
            SelectionListener listener)
    {
        Button button = toolkit.createButton(parent, text, SWT.PUSH);
        GridData gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
        button.setLayoutData(gd);

        if (listener != null)
        {
            button.addSelectionListener(listener);
        }

        return button;
    }


    // ----------------------------------------------------------
    protected Composite createGridComposite(Composite parent, int numColumns,
            boolean sameWidth)
    {
        Composite comp = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout(numColumns, sameWidth);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        comp.setLayout(layout);

        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
        comp.setLayoutData(gd);

        return comp;
    }


    protected void setGridWidthHint(Control control, int hint)
    {
        GridData gd = (GridData) control.getLayoutData();
        gd.widthHint = hint;
    }


    // ----------------------------------------------------------
    protected void safeSetText(Text control, String value)
    {
        control.setText(value != null ? value : ""); //$NON-NLS-1$
    }


    // ----------------------------------------------------------
    protected void safeSetText(Combo control, String value)
    {
        control.setText(value != null ? value : ""); //$NON-NLS-1$
    }


    // ----------------------------------------------------------
    protected void addMessage(Object key, String messageText, Object data,
            int type, Control control)
    {
//        getManagedForm().getMessageManager().addMessage(key, messageText, data,
//                type, control);
    }


    // ----------------------------------------------------------
    protected void removeMessage(Object key, Control control)
    {
        getManagedForm().getMessageManager().removeMessage(key, control);
    }


    // ----------------------------------------------------------
    protected OverviewFormPage getFormPage()
    {
        return formPage;
    }


    // ----------------------------------------------------------
    protected FormToolkit getToolkit()
    {
        return toolkit;
    }


    // ----------------------------------------------------------
    protected ModuleHandle getModel()
    {
        return model;
    }


    //~ Static/instance variables .............................................

    private OverviewFormPage formPage;
    private FormToolkit toolkit;
    private ModuleHandle model;
}
