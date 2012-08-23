/*==========================================================================*\
 |  $Id: OgnlExpressionCellEditor.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.webcat.oda.designer.impl.WebCATKeyProvider;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: OgnlExpressionCellEditor.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class OgnlExpressionCellEditor extends DialogCellEditor
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    public OgnlExpressionCellEditor()
    {
        super();
    }


    // ----------------------------------------------------------
    public OgnlExpressionCellEditor(Composite parent)
    {
        super(parent);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public OgnlExpressionCellEditor(Composite parent, int style)
    {
        super(parent, style);
    }


    // ----------------------------------------------------------
    @Override
    protected Object openDialogBox(Control cellEditorWindow)
    {
        OgnlExpressionBuilder builder = new OgnlExpressionBuilder(
                cellEditorWindow.getShell(), rootClassName, (String) getValue());

        if (builder.open() != Window.OK)
        {
            // If editor dialog canceled, we need reset the focus to the text
            // control, otherwise the button would have the focus.
            editor.setFocus();
        }

        return builder.getExpression();
    }


    // ----------------------------------------------------------
    public void setRootClassName(String rootClassName)
    {
        this.rootClassName = rootClassName;
        proposalProvider.setRootClassName(rootClassName);
    }


    // ----------------------------------------------------------
    protected Button createButton(Composite parent)
    {
        button = super.createButton(parent);
        return button;
    }


    // ----------------------------------------------------------
    protected Control createContents(Composite cell)
    {
        editor = new Text(cell, SWT.NONE);

        proposalProvider = new OgnlContentProposalProvider(
                new WebCATKeyProvider());

        // TODO Fix this so that it works correctly with a cell editor (i.e,
        // pressing Enter doesn't kill the editor when the proposal window is
        // popped up, cursor handling, etc.)

        // new ContentProposalAdapter(editor, new TextContentAdapter(),
        // 		proposalProvider, null, new char[] { '.' });

        editor.addKeyListener(new KeyAdapter()
        {
            public void keyReleased(KeyEvent e)
            {
                keyReleaseOccured(e);
            }
        });

        editor.addSelectionListener(new SelectionAdapter()
        {
            public void widgetDefaultSelected(SelectionEvent e)
            {
                fireApplyEditorValue();
                deactivate();
            }
        });

        editor.addTraverseListener(new TraverseListener()
        {
            public void keyTraversed(TraverseEvent e)
            {
                if (e.detail == SWT.TRAVERSE_ESCAPE
                        || e.detail == SWT.TRAVERSE_RETURN)
                {
                    e.doit = false;
                }
            }
        });

        editor.addFocusListener(new FocusAdapter()
        {
            public void focusLost(FocusEvent e)
            {
                // OgnlExpressionCellEditor.this.focusLost();
            }
        });

        setValueValid(true);

        return editor;
    }


    // ----------------------------------------------------------
    protected void updateContents(Object value)
    {
        if (editor != null && value != null)
        {
            editor.setText((String) value);
        }
    }


    // ----------------------------------------------------------
    protected void doSetFocus()
    {
        if (editor != null)
        {
            editor.setFocus();
        }
    }


    // ----------------------------------------------------------
    protected Object doGetValue()
    {
        if (editor != null)
        {
            return editor.getText();
        }

        return null;
    }


    // ----------------------------------------------------------
    protected void doSetValue(Object value)
    {
        if (editor != null)
        {
            if (value == null)
                value = "";

            editor.setText((String) value);
        }
    }


    //~ Static/instance variables .............................................

    private Text editor;
    private Button button;
    private String rootClassName;
    private OgnlContentProposalProvider proposalProvider;
}
