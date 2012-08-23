/*==========================================================================*\
 |  $Id: OverviewFormPage.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

import java.lang.reflect.InvocationTargetException;
import org.eclipse.birt.report.designer.internal.ui.command.WrapperCommandStack;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.ModelEventManager;
import org.eclipse.birt.report.designer.internal.ui.views.data.DataViewPage;
import org.eclipse.birt.report.designer.internal.ui.views.data.DataViewTreeViewerPage;
import org.eclipse.birt.report.designer.internal.ui.views.outline.DesignerOutlinePage;
import org.eclipse.birt.report.designer.ui.editors.IPageStaleType;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorPage;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.designer.ui.editors.pages.ReportFormPage;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributeViewPage;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.command.ResourceChangeEvent;
import org.eclipse.birt.report.model.api.core.IResourceChangeListener;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.webcat.oda.commons.ReportModelProblemFinder;
import org.webcat.oda.designer.DesignerActivator;
import org.webcat.oda.designer.i18n.Messages;
import org.webcat.oda.designer.preferences.IPreferencesConstants;

// ------------------------------------------------------------------------
/**
 * A form page that is added to the standard BIRT multi-page editor to add a
 * user interface that lets the user easily edit and view the Web-CAT metadata
 * for a report template.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: OverviewFormPage.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class OverviewFormPage extends ReportFormPage
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    @Override
    public void doSave(IProgressMonitor monitor)
    {
        saveModelWithProgress(monitor);

        IReportProvider provider = getProvider();

        if (provider != null)
        {
            provider.saveReport(getModel(), getEditorInput(), monitor);
            firePropertyChange(PROP_DIRTY);
        }

        isDirty = false;
        markPageStale(IPageStaleType.NONE);
        getEditor().editorDirtyStateChanged();
    }


    // ----------------------------------------------------------
    @Override
    public void doSaveAs()
    {
        final IReportProvider provider = getProvider();

        if (provider != null)
        {
            IPath path = provider.getSaveAsPath(getEditorInput());

            if (path == null)
            {
                return;
            }

            final IEditorInput input = provider.createNewEditorInput(path);

            setInput(input);

            IRunnableWithProgress op = new IRunnableWithProgress()
            {
                public synchronized final void run(IProgressMonitor monitor)
                        throws InvocationTargetException, InterruptedException
                {
                    final InvocationTargetException[] iteHolder = new InvocationTargetException[1];

                    try
                    {
                        IWorkspaceRunnable workspaceRunnable = new IWorkspaceRunnable()
                        {
                            public void run(IProgressMonitor pm)
                                    throws CoreException
                            {
                                execute(pm);
                            }
                        };

                        ResourcesPlugin.getWorkspace().run(workspaceRunnable,
                                ResourcesPlugin.getWorkspace().getRoot(),
                                IResource.NONE, monitor);
                    }
                    catch (CoreException e)
                    {
                        throw new InvocationTargetException(e);
                    }
                    catch (OperationCanceledException e)
                    {
                        throw new InterruptedException(e.getMessage());
                    }

                    // Re-throw the InvocationTargetException, if any occurred
                    if (iteHolder[0] != null)
                    {
                        throw iteHolder[0];
                    }
                }


                public void execute(final IProgressMonitor monitor)
                {
                    try
                    {
                        doSave(monitor);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            };

            try
            {
                new ProgressMonitorDialog(getSite().getWorkbenchWindow()
                        .getShell()).run(false, true, op);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    // ----------------------------------------------------------
    private void saveModel(boolean showProgress)
    {
        if (showProgress)
        {
            try
            {
                new ProgressMonitorDialog(getSite().getWorkbenchWindow()
                        .getShell()).run(false, true,
                        new IRunnableWithProgress()
                        {
                            public void run(IProgressMonitor monitor)
                                    throws InvocationTargetException,
                                    InterruptedException
                            {
                                saveModelWithProgress(monitor);
                            }
                        });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            saveModelWithProgress(null);
        }
    }


    // ----------------------------------------------------------
    private void saveModelWithProgress(IProgressMonitor monitor)
    {
        int count = managedForm.getParts().length;

        if (monitor != null)
            monitor.beginTask(Messages.OVERVIEW_PAGE_STORING_PROGRESS, count);

        for (IFormPart part : managedForm.getParts())
        {
            if (part instanceof AbstractSection)
            {
                // Create a progress dialog here
                AbstractSection section = (AbstractSection) part;
                section.saveModel();
            }

            if (monitor != null)
                monitor.worked(1);
        }

        if (monitor != null)
            monitor.done();
    }


    // ----------------------------------------------------------
    private void moduleWasSaved(ModuleHandle module, ResourceChangeEvent event)
    {
        Preferences prefs = DesignerActivator.getDefault()
                .getPluginPreferences();
        int saveBehavior = prefs
                .getInt(IPreferencesConstants.SAVE_BEHAVIOR_KEY);

        if (saveBehavior == IPreferencesConstants.SAVE_BEHAVIOR_SHOW_NO_PROBLEMS)
        {
            return;
        }

        ReportModelProblemFinder finder = new ReportModelProblemFinder(module);

        if (finder.hasProblems())
        {
            ReportProblemDialog dialog = new ReportProblemDialog(getSite()
                    .getShell(), finder.getProblems());

            if (dialog.open() == Window.OK)
            {
                updateContentValues();

                // Remove the change listener while we're saving the immediate
                // fixes in the report, or else we would get called again and
                // present another fix dialog.
                module.removeResourceChangeListener(resourceChangeListener);
                getReportEditor().doSave(null);
                module.addResourceChangeListener(resourceChangeListener);
            }
        }
    }


    // ----------------------------------------------------------
    @SuppressWarnings("deprecation")
    protected void hookModelEventManager(Object model)
    {
        // TODO: replace this deprecated class

        getModelEventManager().hookRoot(model);
        getModelEventManager().hookCommandStack(new WrapperCommandStack());
    }


    // ----------------------------------------------------------
    protected void unhookModelEventManager(Object model)
    {
        getModelEventManager().unhookRoot(model);
    }


    // ----------------------------------------------------------
    @Override
    public void dispose()
    {
        if (managedForm != null)
        {
            managedForm.dispose();
        }

        super.dispose();
    }


    // ----------------------------------------------------------
    @Override
    public boolean isDirty()
    {
        return isDirty;
    }


    // ----------------------------------------------------------
    @Override
    public void createPartControl(Composite parent)
    {
        try
        {
            managedForm = new ManagedForm(parent);

/*            TableWrapLayout layout = new TableWrapLayout();
            layout.numColumns = 2;
            layout.makeColumnsEqualWidth = false;
            managedForm.getForm().getBody().setLayout(layout);*/

            FormToolkit toolkit = managedForm.getToolkit();
            toolkit.decorateFormHeading(managedForm.getForm().getForm());
            managedForm.getForm().setText(Messages.OVERVIEW_PAGE_TITLE);

            createBodyContent(managedForm.getForm().getBody());

            hookModelEventManager(getModel());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        resourceChangeListener = new IResourceChangeListener()
        {
            public void resourceChanged(ModuleHandle module,
                    ResourceChangeEvent event)
            {
                moduleWasSaved(module, event);
            }
        };

        getModel().addResourceChangeListener(resourceChangeListener);

        updateContentValues();
    }


    // ----------------------------------------------------------
    private void createBodyContent(final Composite parent)
    {
        SectionPart part;
        TableWrapData twd;

        FormToolkit toolkit = managedForm.getToolkit();

        TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;
        layout.makeColumnsEqualWidth = false;
        parent.setLayout(layout);

        part = new GeneralInfoSection(this, parent, toolkit, getModel());
        twd = new TableWrapData(TableWrapData.FILL_GRAB);
        part.getSection().setLayoutData(twd);
        managedForm.addPart(part);

        part = new AuthorsSection(this, parent, toolkit, getModel());
        twd = new TableWrapData(TableWrapData.FILL_GRAB);
        part.getSection().setLayoutData(twd);
        managedForm.addPart(part);

        part = new CopyrightLicenseSection(this, parent, toolkit, getModel());
        twd = new TableWrapData(TableWrapData.FILL_GRAB);
        part.getSection().setLayoutData(twd);
        managedForm.addPart(part);

        part = new AppearanceBehaviorSection(this, parent, toolkit,
                getModel());
        twd = new TableWrapData(TableWrapData.FILL_GRAB);
        part.getSection().setLayoutData(twd);
        managedForm.addPart(part);

        part = new RepositorySection(this, parent, toolkit, getModel());
        twd = new TableWrapData(TableWrapData.FILL_GRAB);
        twd.colspan = 2;
        part.getSection().setLayoutData(twd);
        managedForm.addPart(part);
    }


    // ----------------------------------------------------------
    public int getStaleType()
    {
        return staleType;
    }


    // ----------------------------------------------------------
    public void markPageStale(int type)
    {
        staleType = type;
    }


    // ----------------------------------------------------------
    public void markAsDirty()
    {
        isDirty = true;
        markPageStale(IPageStaleType.MODEL_CHANGED);
        firePropertyChange(PROP_DIRTY);
        getEditor().editorDirtyStateChanged();
    }


    // ----------------------------------------------------------
    public boolean onBroughtToTop(IReportEditorPage prevPage)
    {
        updateContentValues();

        return true;
    }


    // ----------------------------------------------------------
    private void updateContentValues()
    {
        for (IFormPart part : managedForm.getParts())
        {
            if (part instanceof AbstractSection)
            {
                AbstractSection section = (AbstractSection) part;
                section.updateControls();
            }
        }
    }


    // ----------------------------------------------------------
    public String getId()
    {
        return ID;
    }


    // ----------------------------------------------------------
    public Control getPartControl()
    {
        if (managedForm != null)
        {
            return managedForm.getForm();
        }
        else
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    public IManagedForm getManagedForm()
    {
        return managedForm;
    }


    // ----------------------------------------------------------
    public boolean canLeaveThePage()
    {
        if (isDirty())
        {
            saveModel(true);
        }

        return super.canLeaveThePage();
    }


    // ----------------------------------------------------------
    private IReportProvider getProvider()
    {
        return (IReportProvider) getEditor().getAdapter(IReportProvider.class);
    }


    // ----------------------------------------------------------
    protected ModelEventManager getModelEventManager()
    {
        if (manager == null)
        {
            manager = new ModelEventManager();
        }

        return manager;
    }


    // ----------------------------------------------------------
    @SuppressWarnings("unchecked")
    public Object getAdapter(Class adapter)
    {
        if (adapter == IContentOutlinePage.class)
        {
            DesignerOutlinePage outlinePage = new DesignerOutlinePage(
                    getModel());
            getModelEventManager().addModelEventProcessor(
                    outlinePage.getModelProcessor());
            return outlinePage;
        }
        else if (adapter == DataViewPage.class)
        {
            DataViewTreeViewerPage page = new DataViewTreeViewerPage(getModel());
            getModelEventManager().addModelEventProcessor(
                    page.getModelProcessor());
            return page;
        }
        else if (adapter == AttributeViewPage.class)
        {
            return new AttributeViewPage();
        }

        return super.getAdapter(adapter);
    }


    //~ Static/instance variables .............................................

    private static final String ID =
        "org.webcat.oda.ui.editors.metadata"; //$NON-NLS-1$

    private IResourceChangeListener resourceChangeListener;
    private boolean isDirty = false;
    private int staleType;
    private ManagedForm managedForm;
    private ModelEventManager manager;
}
