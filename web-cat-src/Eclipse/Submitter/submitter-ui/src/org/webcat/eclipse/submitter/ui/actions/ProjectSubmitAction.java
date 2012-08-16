/*==========================================================================*\
 |  $Id: ProjectSubmitAction.java,v 1.2 2010/12/06 21:08:41 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
 |
 |  This file is part of Web-CAT Eclipse Plugins.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU General Public License as published by
 |  the Free Software Foundation; either version 2 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU General Public License along
 |  with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.eclipse.submitter.ui.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.webcat.eclipse.submitter.ui.SubmitterUIPlugin;
import org.webcat.eclipse.submitter.ui.dialogs.AmbiguousProjectToSubmitDialog;

//--------------------------------------------------------------------------
/**
 * The workbench action delegate that invokes the submission wizard. This action
 * is used by the Submit Project option in the Project menu, as well as the
 * Submit button in the main toolbar. (The Submit option in a project's context
 * menu is provided by the similar class in the .popup.actions package.)
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/12/06 21:08:41 $
 */
public class ProjectSubmitAction implements IWorkbenchWindowActionDelegate
{
	//~ Methods ...............................................................
	
	// ----------------------------------------------------------
	/**
	 * Called when the workbench action is invoked.
	 */
	public void run(IAction action)
	{
		IProject projectToSubmit = null;

		if(selectedProject == activeEditorProject)
		{
			projectToSubmit = selectedProject;
		}
		else if(selectedProject != null && activeEditorProject == null)
		{
			projectToSubmit = selectedProject;
		}
		else if(selectedProject == null && activeEditorProject != null)
		{
			projectToSubmit = activeEditorProject;
		}
		else
		{
			// The current workspace selection (Package Explorer,
			// Navigator, etc.) is one project, but the active editor
			// contains a file in another project. Ask the user to
			// choose which of the two projects they want to submit.

			AmbiguousProjectToSubmitDialog dialog =
				AmbiguousProjectToSubmitDialog.createWithProjects(
						window.getShell(),
						selectedProject, activeEditorProject);

			int result = dialog.open();

			if(result == 0)
			{
				projectToSubmit = dialog.getSelectedProject();
			}
			else
			{
				return;
			}
		}

		SubmitterUIPlugin.getDefault().spawnSubmissionUI(window.getShell(),
		        projectToSubmit);
	}


	// ----------------------------------------------------------
	/**
	 * Called when the selection in the workbench has changed.
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
		if(selection != null)
		{
			if(selection instanceof IStructuredSelection)
			{
				IStructuredSelection ss = (IStructuredSelection)selection;
				Object obj = ss.getFirstElement();

				if(obj instanceof IAdaptable)
				{
					IAdaptable adapt = (IAdaptable)obj;
					selectedProject = (IProject)adapt
					        .getAdapter(IProject.class);
				}
			}
		}

		IWorkbenchPage activePage = window.getActivePage();
		if(activePage != null)
		{
			IEditorPart activeEditor = activePage.getActiveEditor();

			if(activeEditor != null)
			{
				IEditorInput editorInput = activeEditor.getEditorInput();
				if(editorInput instanceof IFileEditorInput)
				{
					IFile file = ((IFileEditorInput)editorInput).getFile();
					activeEditorProject = file.getProject();
				}
			}
		}
	}


	// ----------------------------------------------------------
	/**
	 * Called when the delegate is disposed.
	 */
	public void dispose()
	{
		// Do nothing.
	}


	// ----------------------------------------------------------
	/**
	 * Called when the delegate is initialized.
	 */
	public void init(IWorkbenchWindow aWindow)
	{
		this.window = aWindow;
	}


	//~ Static/instance variables .............................................

	/* The workbench window to which this action belongs. */
	private IWorkbenchWindow window;

	/* The project that is currently selected in the workbench (in the
	   Navigator, Package Explorer, or similar view). */
	private IProject selectedProject;

	/* The project to which the file in the currently active workbench editor
	   belongs. */
	private IProject activeEditorProject;
}
