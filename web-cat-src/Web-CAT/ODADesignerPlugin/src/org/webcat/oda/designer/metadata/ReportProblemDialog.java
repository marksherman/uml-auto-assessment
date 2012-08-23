/*==========================================================================*\
 |  $Id: ReportProblemDialog.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

import java.util.ArrayList;


import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.webcat.oda.commons.ReportModelProblem;
import org.webcat.oda.commons.ReportModelProblemFinder;
import org.webcat.oda.designer.DesignerActivator;
import org.webcat.oda.designer.i18n.Messages;
import org.webcat.oda.designer.metadata.fixers.AddAuthorFixer;
import org.webcat.oda.designer.metadata.fixers.LicensePropertyFixer;
import org.webcat.oda.designer.metadata.fixers.TextPropertyFixer;
import org.webcat.oda.designer.preferences.IPreferencesConstants;

// ------------------------------------------------------------------------
/**
 * A dialog that is displayed if any problems are found in the report template
 * when it is saved.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: ReportProblemDialog.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class ReportProblemDialog extends TitleAreaDialog
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new report problem dialog with the specified problems to
     * display.
     *
     * @param parentShell
     *            the shell that will be the parent of this dialog
     * @param problems
     *            the problems to be displayed in the dialog
     */
    public ReportProblemDialog(Shell parentShell, ReportModelProblem[] problems)
    {
        super(parentShell);

        int style = getShellStyle() | SWT.RESIZE;
        setShellStyle(style);

        this.problems = problems;
        fixers = new IReportProblemFixer[problems.length];
    }


    // ----------------------------------------------------------
    @Override
    protected Control createContents(Composite parent)
    {
        Control control = super.createContents(parent);

        getShell().setText(Messages.REPORT_PROBLEM_DIALOG_TITLE);
        setTitle(Messages.REPORT_PROBLEM_DIALOG_TITLE);
        setMessage(Messages.REPORT_PROBLEM_DIALOG_MESSAGE);

        return control;
    }


    // ----------------------------------------------------------
    @Override
    protected Control createDialogArea(Composite parent)
    {
        Preferences prefs =
            DesignerActivator.getDefault().getPluginPreferences();
        saveBehavior = prefs.getInt(IPreferencesConstants.SAVE_BEHAVIOR_KEY);

        Composite composite = (Composite) super.createDialogArea(parent);

        Composite panel = new Composite(composite, SWT.NONE);
        GridLayout layout = new GridLayout(1, true);
        panel.setLayout(layout);
        panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        Label label = new Label(panel, SWT.WRAP);
        label
                .setText(Messages.REPORT_PROBLEMS_INSTRUCTION);
        GridData gd = new GridData(SWT.FILL, SWT.TOP, false, false);
        gd.widthHint = 500;
        label.setLayoutData(gd);

        problemTable = new TableViewer(panel, SWT.BORDER | SWT.FULL_SELECTION);

        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.widthHint = 500;
        gd.heightHint = 150;
        problemTable.getControl().setLayoutData(gd);

        ProblemProvider provider = new ProblemProvider();
        problemTable.setContentProvider(provider);
        problemTable.setLabelProvider(provider);
        problemTable.setInput(problems);

        problemTable
                .addPostSelectionChangedListener(new ISelectionChangedListener()
                {
                    public void selectionChanged(SelectionChangedEvent event)
                    {
                        selectedProblemChanged();
                    }
                });

        fixContainer = new Composite(panel, SWT.NONE);
        fixersLayout = new StackLayout();
        fixContainer.setLayout(fixersLayout);

        gd = new GridData(SWT.FILL, SWT.FILL, true, false);
        fixContainer.setLayoutData(gd);

        int i = 0;
        for (ReportModelProblem problem : problems)
        {
            if (saveBehavior == IPreferencesConstants.SAVE_BEHAVIOR_SHOW_ALL_PROBLEMS
                    || (saveBehavior == IPreferencesConstants.SAVE_BEHAVIOR_SHOW_ERRORS_ONLY && problem
                            .getSeverity() == ReportModelProblem.SEVERITY_ERROR))
            {
                fixers[i++] = createFixerForProblem(problem, fixContainer);
            }
        }

        Label sep = new Label(panel, SWT.HORIZONTAL | SWT.SEPARATOR);
        gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        sep.setLayoutData(gd);

        Composite futurePanel = new Composite(panel, SWT.NONE);
        layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        futurePanel.setLayout(layout);

        new Label(futurePanel, SWT.NONE).setText(Messages.REPORT_PROBLEMS_IN_THE_FUTURE);

        futureCombo = new Combo(futurePanel, SWT.DROP_DOWN | SWT.READ_ONLY);
        futureCombo.add(Messages.REPORT_PROBLEMS_SHOW_ALL_PROBLEMS);
        futureCombo.add(Messages.REPORT_PROBLEMS_SHOW_ERRORS_ONLY);
        futureCombo.add(Messages.REPORT_PROBLEMS_SHOW_NO_PROBLEMS);

        futureCombo.select(saveBehavior);

        return composite;
    }


    // ----------------------------------------------------------
    private IReportProblemFixer createFixerForProblem(
            ReportModelProblem problem, Composite parent)
    {
        String key = ReportModelProblemFinder.getKeyWithoutDetail(problem
                .getKey());

        IReportProblemFixer fixer = null;

        if (ReportModelProblemFinder.KEY_NO_TITLE.equals(key))
        {
            fixer = new TextPropertyFixer(parent,
                    TextPropertyFixer.PROP_REPORT_TITLE, false);
        }
        else if (ReportModelProblemFinder.KEY_NO_DESCRIPTION.equals(key))
        {
            fixer = new TextPropertyFixer(parent,
                    TextPropertyFixer.PROP_REPORT_DESCRIPTION, true);
        }
        else if (ReportModelProblemFinder.KEY_NO_AUTHORS.equals(key))
        {
            fixer = new AddAuthorFixer(parent);
        }
        else if (ReportModelProblemFinder.KEY_AUTHOR_NO_NAME.equals(key))
        {
            fixer = new TextPropertyFixer(parent,
                    TextPropertyFixer.PROP_REPORT_AUTHOR_NAME, false);
        }
        else if (ReportModelProblemFinder.KEY_NO_LICENSE.equals(key))
        {
            fixer = new LicensePropertyFixer(parent);
        }
        else if (ReportModelProblemFinder.KEY_NO_COPYRIGHT.equals(key))
        {
            fixer = new TextPropertyFixer(parent,
                    TextPropertyFixer.PROP_REPORT_COPYRIGHT, false);
        }
        else if (ReportModelProblemFinder.KEY_DATASET_NO_DESCRIPTION
                .equals(key))
        {
            fixer = new TextPropertyFixer(parent,
                    TextPropertyFixer.PROP_REPORT_DATASET_DESCRIPTION, true);
        }

        if (fixer != null)
            fixer.setReportModelProblem(problem);

        return fixer;
    }


    // ----------------------------------------------------------
    private void selectedProblemChanged()
    {
        IStructuredSelection selection = (IStructuredSelection) problemTable
                .getSelection();

        if (!selection.isEmpty())
        {
            ReportModelProblem problem = (ReportModelProblem) selection
                    .getFirstElement();

            for (int i = 0; i < problems.length; i++)
            {
                if (problems[i] == problem)
                {
                    fixersLayout.topControl = fixers[i]
                            .getTopLevelFixerControl();
                    fixContainer.layout();
                    break;
                }
            }
        }
    }


    // ----------------------------------------------------------
    protected void okPressed()
    {
        // Apply fixes.
        for (IReportProblemFixer fixer : fixers)
        {
            if (fixer != null)
                fixer.applyFixToModel();
        }

        Preferences prefs = DesignerActivator.getDefault()
                .getPluginPreferences();
        int behavior = futureCombo.getSelectionIndex();

        prefs.setValue(IPreferencesConstants.SAVE_BEHAVIOR_KEY, behavior);

        super.okPressed();
    }


    //~ Inner classes .........................................................

    // ----------------------------------------------------------
    /**
     * A JFace content and label provider that uses the problem list as its data
     * source.
     */
    private class ProblemProvider implements IStructuredContentProvider,
            ITableLabelProvider
    {
        // ----------------------------------------------------------
        public Object[] getElements(Object inputElement)
        {
            if (saveBehavior
                    == IPreferencesConstants.SAVE_BEHAVIOR_SHOW_ERRORS_ONLY)
            {
                ArrayList<ReportModelProblem> filteredProblems =
                    new ArrayList<ReportModelProblem>();

                for (ReportModelProblem problem : problems)
                {
                    if (problem.getSeverity()
                            == ReportModelProblem.SEVERITY_ERROR)
                    {
                        filteredProblems.add(problem);
                    }
                }

                return filteredProblems.toArray();
            }
            else
            {
                return problems;
            }
        }


        // ----------------------------------------------------------
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
        {
            // Do nothing.
        }


        // ----------------------------------------------------------
        public void dispose()
        {
            // Do nothing.
        }


        // ----------------------------------------------------------
        public Image getColumnImage(Object element, int columnIndex)
        {
            int severity = ((ReportModelProblem) element).getSeverity();

            switch (severity)
            {
            case ReportModelProblem.SEVERITY_OK:
                return JFaceResources.getImage(DLG_IMG_MESSAGE_INFO);

            case ReportModelProblem.SEVERITY_WARNING:
                return JFaceResources.getImage(DLG_IMG_MESSAGE_WARNING);

            case ReportModelProblem.SEVERITY_ERROR:
                return JFaceResources.getImage(DLG_IMG_MESSAGE_ERROR);

            default:
                return null;
            }
        }


        // ----------------------------------------------------------
        public String getColumnText(Object element, int columnIndex)
        {
            return ((ReportModelProblem) element).getDescription();
        }


        // ----------------------------------------------------------
        public boolean isLabelProperty(Object element, String property)
        {
            return false;
        }


        // ----------------------------------------------------------
        public void addListener(ILabelProviderListener listener)
        {
            // Do nothing.
        }


        // ----------------------------------------------------------
        public void removeListener(ILabelProviderListener listener)
        {
            // Do nothing.
        }
    }


    //~ Static/instance variables .............................................

    private int saveBehavior;
    private TableViewer problemTable;
    private ReportModelProblem[] problems;
    private IReportProblemFixer[] fixers;
    private StackLayout fixersLayout;
    private Composite fixContainer;
    private Combo futureCombo;
}
