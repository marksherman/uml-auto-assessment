/*==========================================================================*\
 |  $Id: OgnlExpressionBuilder.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

package org.webcat.oda.designer.ognl;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.webcat.oda.designer.i18n.Messages;
import org.webcat.oda.designer.impl.WebCATKeyProvider;
import org.webcat.oda.designer.widgets.KeyPathBrowser;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: OgnlExpressionBuilder.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class OgnlExpressionBuilder extends TitleAreaDialog
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     *
     * @param parentShell
     * @param rootClass
     * @param initialExpr
     */
    public OgnlExpressionBuilder(Shell parentShell, String rootClass,
            String initialExpr)
    {
        super(parentShell);

        expression = initialExpr;
        rootClassName = rootClass;
        keyProvider = new WebCATKeyProvider();

        Font font = JFaceResources.getDefaultFont();
        FontData[] fd = font.getFontData();
        fd[0].setStyle(fd[0].getStyle() | SWT.BOLD);

        boldFont = new Font(parentShell.getDisplay(), fd[0]);
    }


    @Override
    protected void handleShellCloseEvent()
    {
        keyProvider.dispose();
        boldFont.dispose();

        super.handleShellCloseEvent();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     *
     */
    protected void setShellStyle(int newShellStyle)
    {
        newShellStyle |= SWT.MAX | SWT.RESIZE;
        super.setShellStyle(newShellStyle);
    }


    // ----------------------------------------------------------
    /**
     *
     */
    @Override
    protected Control createContents(Composite parent)
    {
        Control control = super.createContents(parent);

        setTitle(Messages.EXPR_BUILDER_TITLE);
        setMessage(Messages.EXPR_BUILDER_PROMPT);

        return control;
    }


    // ----------------------------------------------------------
    /**
     *
     */
    protected Control createDialogArea(Composite parent)
    {
        Composite composite = (Composite) super.createDialogArea(parent);
        createToolbar(composite);

        SashForm sash = new SashForm(composite, SWT.VERTICAL | SWT.SMOOTH);
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        sash.setLayoutData(gd);

        createEditor(sash);
        createBrowser(sash);

        sash.setWeights(new int[] { 50, 50 });

        editor.getControl().setFocus();

        return composite;
    }


    // ----------------------------------------------------------
    private SelectionAdapter selectionAdapterForTemplate(final String template)
    {
        return new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                OgnlBuilderTemplates.insertTemplate(editor, template);
                editor.getControl().setFocus();
            }
        };
    }


    // ----------------------------------------------------------
    private void addTemplateButton(String title, String tooltip, String template)
    {
        ToolItem toolItem = new ToolItem(templateBar, SWT.PUSH);
        toolItem.setText(title);
        toolItem.setToolTipText(tooltip);
        toolItem.addSelectionListener(selectionAdapterForTemplate(template));
    }


    // ----------------------------------------------------------
    private void addTemplateSeparator()
    {
        new ToolItem(templateBar, SWT.SEPARATOR);
    }


    // ----------------------------------------------------------
    private void createTemplateMenu()
    {
        mathTemplateMenu = new Menu(templateBar);

        MenuItem item;

        item = new MenuItem(mathTemplateMenu, SWT.PUSH);
        item.setText("min"); //$NON-NLS-1$
        item
                .addSelectionListener(selectionAdapterForTemplate(OgnlBuilderTemplates.TEMPLATE_MIN));

        item = new MenuItem(mathTemplateMenu, SWT.PUSH);
        item.setText("max"); //$NON-NLS-1$
        item
                .addSelectionListener(selectionAdapterForTemplate(OgnlBuilderTemplates.TEMPLATE_MAX));

        item = new MenuItem(mathTemplateMenu, SWT.PUSH);
        item.setText("floor"); //$NON-NLS-1$
        item
                .addSelectionListener(selectionAdapterForTemplate(OgnlBuilderTemplates.TEMPLATE_FLOOR));

        item = new MenuItem(mathTemplateMenu, SWT.PUSH);
        item.setText("ceil"); //$NON-NLS-1$
        item
                .addSelectionListener(selectionAdapterForTemplate(OgnlBuilderTemplates.TEMPLATE_CEIL));

        item = new MenuItem(mathTemplateMenu, SWT.PUSH);
        item.setText("round"); //$NON-NLS-1$
        item
                .addSelectionListener(selectionAdapterForTemplate(OgnlBuilderTemplates.TEMPLATE_ROUND));

        item = new MenuItem(mathTemplateMenu, SWT.PUSH);
        item.setText("abs"); //$NON-NLS-1$
        item
                .addSelectionListener(selectionAdapterForTemplate(OgnlBuilderTemplates.TEMPLATE_ABS));

        item = new MenuItem(mathTemplateMenu, SWT.PUSH);
        item.setText("signum"); //$NON-NLS-1$
        item
                .addSelectionListener(selectionAdapterForTemplate(OgnlBuilderTemplates.TEMPLATE_SIGNUM));
    }


    // ----------------------------------------------------------
    /**
     *
     * @param parent
     */
    private void createToolbar(Composite parent)
    {
        GridData gd;

        Composite composite = new Composite(parent, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.TOP, true, false);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);

        Label label = new Label(composite, SWT.NONE);
        label.setText(Messages.EXPR_BUILDER_OGNL_TEMPLATES);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);

        templateBar = new ToolBar(composite, SWT.FLAT | SWT.WRAP | SWT.TRAIL);
        gd = new GridData(SWT.FILL, SWT.TOP, true, true);
        templateBar.setLayoutData(gd);

        createTemplateMenu();

        final ToolItem toolItem = new ToolItem(templateBar, SWT.PUSH);
        toolItem.setText(Messages.EXPR_BUILDER_MATH);
        toolItem.setToolTipText(Messages.EXPR_BUILDER_MATH_TOOLTIP);
        toolItem.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                Point pt = templateBar.toDisplay(toolItem.getBounds().x,
                        toolItem.getBounds().y + toolItem.getBounds().height);
                mathTemplateMenu.setLocation(pt);
                mathTemplateMenu.setVisible(true);
            }
        });

        addTemplateSeparator();

        addTemplateButton(Messages.EXPR_BUILDER_LIST,
                Messages.EXPR_BUILDER_LIST_TOOLTIP,
                OgnlBuilderTemplates.TEMPLATE_LIST);

        addTemplateButton(Messages.EXPR_BUILDER_MAP,
                Messages.EXPR_BUILDER_MAP_TOOLTIP,
                OgnlBuilderTemplates.TEMPLATE_MAP);

        addTemplateSeparator();

        addTemplateButton(Messages.EXPR_BUILDER_SELECT_ALL,
                Messages.EXPR_BUILDER_SELECT_ALL_TOOLTIP,
                OgnlBuilderTemplates.TEMPLATE_SELECT_ALL);

        addTemplateButton(Messages.EXPR_BUILDER_SELECT_FIRST,
                Messages.EXPR_BUILDER_SELECT_FIRST_TOOLTIP,
                OgnlBuilderTemplates.TEMPLATE_SELECT_FIRST);

        addTemplateButton(Messages.EXPR_BUILDER_SELECT_LAST,
                Messages.EXPR_BUILDER_SELECT_LAST_TOOLTIP,
                OgnlBuilderTemplates.TEMPLATE_SELECT_LAST);

        addTemplateSeparator();

        addTemplateButton(Messages.EXPR_BUILDER_LAMBDA,
                Messages.EXPR_BUILDER_LAMBDA_TOOLTIP,
                OgnlBuilderTemplates.TEMPLATE_LAMBDA);
    }


    // ----------------------------------------------------------
    /**
     *
     * @param parent
     */
    private void createEditor(Composite parent)
    {
        GridLayout layout = new GridLayout();
        layout.marginWidth = layout.marginHeight = 0;

        Composite composite = new Composite(parent, SWT.BORDER
                | SWT.LEFT_TO_RIGHT);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(layout);

        CompositeRuler ruler = new CompositeRuler();
        ruler.addDecorator(0, new LineNumberRulerColumn());

        editor = new SourceViewer(composite, ruler, SWT.H_SCROLL | SWT.V_SCROLL);
        editor
                .configure(new OgnlSourceViewerConfiguration(
                        new OgnlSyntaxContext(rootClassName, keyProvider,
                                keyProvider)));

        if (expression != null)
        {
            OgnlEditorInput editorInput = new OgnlEditorInput(expression);
            OgnlDocumentProvider documentProvider = new OgnlDocumentProvider();

            try
            {
                documentProvider.connect(editorInput);
            }
            catch (CoreException e)
            {
                e.printStackTrace();
            }

            IDocument document = documentProvider.getDocument(editorInput);
            editor.setDocument(document);
        }

        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = 144;
        editor.getControl().setLayoutData(gd);
    }


    // ----------------------------------------------------------
    /**
     *
     * @param parent
     */
    private void createBrowser(Composite parent)
    {
        GridLayout layout = new GridLayout();
        layout.marginWidth = layout.marginHeight = 0;

        Composite composite = new Composite(parent, SWT.BORDER
                | SWT.LEFT_TO_RIGHT);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(layout);

        GridData gd;

        Composite keyPathComposite = new Composite(composite, SWT.NONE);
        layout = new GridLayout(2, false);
        keyPathComposite.setLayout(layout);
        gd = new GridData(SWT.FILL, SWT.FILL, true, false);
        keyPathComposite.setLayoutData(gd);

        Label label = new Label(keyPathComposite, SWT.NONE);
        label.setFont(boldFont);
        label.setText(Messages.EXPR_BUILDER_CURRENT_KEY_PATH);
        gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
        label.setLayoutData(gd);

        selectedKeyPathLabel = new Label(keyPathComposite, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        selectedKeyPathLabel.setLayoutData(gd);

        browser = new KeyPathBrowser(composite, SWT.BORDER);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = 144;
        browser.setLayoutData(gd);
        browser.addSelectionListener(new SelectionListener()
        {
            public void widgetDefaultSelected(SelectionEvent e)
            {
                Point range = editor.getSelectedRange();

                try
                {
                    editor.getDocument().replace(range.x, range.y,
                            browser.getSelectedKeyPath());
                    editor.getControl().setFocus();
                }
                catch (BadLocationException e1)
                {
                    // Ignore exception.
                }
            }


            public void widgetSelected(SelectionEvent e)
            {
                selectedKeyPathLabel.setText(browser.getSelectedKeyPath());
            }
        });

        browser.setKeyProvider(keyProvider);
        browser.setKeyLabelProvider(keyProvider);
        browser.setRootClassName(rootClassName);
    }


    // ----------------------------------------------------------
    /**
     *
     */
    protected void okPressed()
    {
        expression = editor.getTextWidget().getText().trim();
        super.okPressed();
    }


    // ----------------------------------------------------------
    /**
     * Gets the OGNL expression entered into the expression builder.
     *
     * @return the expression entered into the builder
     */
    public String getExpression()
    {
        return expression;
    }


    //~ Static/instance variables .............................................

    private Font boldFont;
    private ToolBar templateBar;
    private Menu mathTemplateMenu;
    private SourceViewer editor;
    private Label selectedKeyPathLabel;
    private String rootClassName;
    private WebCATKeyProvider keyProvider;
    private KeyPathBrowser browser;
    private String expression;
}
