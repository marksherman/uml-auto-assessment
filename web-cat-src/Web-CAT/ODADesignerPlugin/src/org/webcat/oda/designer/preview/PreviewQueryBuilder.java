/*==========================================================================*\
 |  $Id: PreviewQueryBuilder.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

package org.webcat.oda.designer.preview;

import java.text.MessageFormat;
import java.util.ArrayList;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.webcat.oda.designer.i18n.Messages;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: PreviewQueryBuilder.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class PreviewQueryBuilder extends TitleAreaDialog implements
        IPreviewQueryClauseEventHandler
{
    public PreviewQueryBuilder(Shell parentShell, String entityType,
            PreviewQueryClause[] clauses)
    {
        super(parentShell);

        this.entityType = entityType;
        this.clauses = clauses;

        clausePanels = new ArrayList<PreviewQueryClausePanel>();
    }


    // -----------------------------------------------------------------------
    /**
     *
     */
    @Override
    protected void setShellStyle(int newShellStyle)
    {
        newShellStyle |= SWT.MAX | SWT.RESIZE;
        super.setShellStyle(newShellStyle);
    }


    @Override
    protected Control createContents(Composite parent)
    {
        Control control = super.createContents(parent);

        setTitle(Messages.QUERY_BUILDER_TITLE);
        setMessage(Messages.QUERY_BUILDER_DESCRIPTION);

        return control;
    }


    // -----------------------------------------------------------------------
    /**
     *
     */
    @Override
    protected Control createDialogArea(Composite parent)
    {
        GridData gd;

        Composite composite = (Composite) super.createDialogArea(parent);

        Composite mainArea = new Composite(composite, SWT.NONE);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        mainArea.setLayoutData(gd);
        GridLayout layout = new GridLayout(1, true);
        layout.marginWidth = 10;
        layout.marginHeight = 10;
        layout.verticalSpacing = 10;
        mainArea.setLayout(layout);

        Label label = new Label(mainArea, SWT.NONE);
        String format = Messages.QUERY_BUILDER_INSTRUCTION;
        label.setText(MessageFormat.format(format, entityType));
        gd = new GridData(SWT.FILL, SWT.TOP, true, false);
        label.setLayoutData(gd);

        createEditor(mainArea);

        return composite;
    }


    private void createEditor(Composite parent)
    {
        GridData gd;

        scroller = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL
                | SWT.V_SCROLL);
        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.widthHint = 750;
        gd.heightHint = 250;
        scroller.setLayoutData(gd);
        scroller.setExpandHorizontal(true);
        scroller.setExpandVertical(true);

        panelContainer = new Composite(scroller, SWT.NONE);
        GridLayout layout = new GridLayout(1, true);
        layout.verticalSpacing = 0;
        layout.horizontalSpacing = 0;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        panelContainer.setLayout(layout);

        scroller.setContent(panelContainer);

        if (clauses == null)
        {
            addEmptyPanel(0);
        }
        else
        {
            addPanelsForClauses();
        }
    }


    private void addPanelsForClauses()
    {
        for (PreviewQueryClause clause : clauses)
        {
            PreviewQueryClausePanel panel = addEmptyPanel(clausePanels.size());

            panel.setClause(clause);
        }
    }


    private int indexOfClausePanel(PreviewQueryClausePanel panel)
    {
        int index = -1;

        for (int i = 0; i < clausePanels.size(); i++)
        {
            if (panel == clausePanels.get(i))
            {
                index = i;
                break;
            }
        }

        return index;
    }


    public void addClauseBelow(PreviewQueryClausePanel panel)
    {
        int index = indexOfClausePanel(panel);

        addEmptyPanel(index + 1);
    }


    public void removeClause(PreviewQueryClausePanel panel)
    {
        int index = indexOfClausePanel(panel);

        PreviewQueryClausePanel toDispose = clausePanels.remove(index);
        toDispose.dispose();
        panelContainer.layout();

        if (clausePanels.size() == 0)
        {
            addEmptyPanel(clausePanels.size());
        }
        else
        {
            scroller.setMinSize(panelContainer.computeSize(SWT.DEFAULT,
                    SWT.DEFAULT));
        }
    }


    private PreviewQueryClausePanel addEmptyPanel(int beforeIndex)
    {
        PreviewQueryClausePanel p = new PreviewQueryClausePanel(panelContainer,
                this, entityType);
        GridData gd = new GridData(SWT.FILL, SWT.TOP, true, false);
        p.setLayoutData(gd);
        p.setFocus();

        clausePanels.add(beforeIndex, p);

        PreviewQueryClausePanel belowPanel = null;

        if (beforeIndex > 0 && beforeIndex <= clausePanels.size())
        {
            belowPanel = clausePanels.get(beforeIndex - 1);
        }

        p.moveBelow(belowPanel);
        panelContainer.layout();

        scroller.setMinSize(panelContainer
                .computeSize(SWT.DEFAULT, SWT.DEFAULT));

        return p;
    }


    protected void okPressed()
    {
        ArrayList<PreviewQueryClause> clauses = new ArrayList<PreviewQueryClause>();

        for (PreviewQueryClausePanel panel : clausePanels)
        {
            PreviewQueryClause clause = panel.getClause();

            if (clause != null)
                clauses.add(clause);
        }

        PreviewQueryClause[] clauseArray = new PreviewQueryClause[clauses
                .size()];
        clauses.toArray(clauseArray);

        this.clauses = clauseArray;

        super.okPressed();
    }


    public PreviewQueryClause[] getClauses()
    {
        return clauses;
    }


    private PreviewQueryClause[] clauses;

    private String entityType;

    private ScrolledComposite scroller;

    private Composite panelContainer;

    private ArrayList<PreviewQueryClausePanel> clausePanels;
}
