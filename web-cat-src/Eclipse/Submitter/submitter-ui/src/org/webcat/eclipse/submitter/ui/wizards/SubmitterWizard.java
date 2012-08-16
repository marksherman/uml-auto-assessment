/*==========================================================================*\
 |  $Id: SubmitterWizard.java,v 1.2 2010/12/06 21:08:41 aallowat Exp $
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

package org.webcat.eclipse.submitter.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;
import org.webcat.eclipse.submitter.core.RunnableContextLongRunningTaskManager;
import org.webcat.eclipse.submitter.ui.SubmitterUIPlugin;
import org.webcat.eclipse.submitter.ui.editors.BrowserEditor;
import org.webcat.eclipse.submitter.ui.editors.BrowserEditorInput;
import org.webcat.eclipse.submitter.ui.i18n.Messages;
import org.webcat.submitter.Submitter;

//--------------------------------------------------------------------------
/**
 * The main wizard that allows the user to electronically submit a project
 * through Eclipse.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/12/06 21:08:41 $
 */
public class SubmitterWizard extends Wizard
{
	//~ Methods ...............................................................
	
	// ----------------------------------------------------------
	public void addPages()
	{
		// Add the wizard pages to the wizard.

		startPage = new SubmitterStartPage(submitter, project);
		finalPage = new SubmitterSummaryPage(submitter, project);

		addPage(startPage);
		addPage(finalPage);

		submitter.setLongRunningTaskManager(
				new RunnableContextLongRunningTaskManager(getContainer()));
	}


	// ----------------------------------------------------------
	/**
	 * Initializes the wizard.
	 * 
	 * @param aSubmitter the submitter
	 * @param aProject the project
	 */
	public void init(Submitter aSubmitter, IProject aProject)
	{
		// Initialize the wizard.

		this.submitter = aSubmitter;
		this.project = aProject;
		this.setWindowTitle(Messages.WIZARD_TITLE);

		this.setDefaultPageImageDescriptor(SubmitterUIPlugin
		        .getImageDescriptor("banner.png")); //$NON-NLS-1$
		this.setNeedsProgressMonitor(true);
	}


	// ----------------------------------------------------------
	public boolean canFinish()
	{
		// We only want the "Finish" button to be enabled if the user is
		// on the final (summary) page of the wizard.

		if(getContainer().getCurrentPage() == finalPage)
			return true;
		else
			return false;
	}


	// ----------------------------------------------------------
	/**
	 * Sets the project used by the wizard.
	 * 
	 * @param project the project
	 */
	public void setProject(IProject project)
	{
		this.project = project;
	}


	// ----------------------------------------------------------
	public boolean performFinish()
	{
		// Now that the submission is complete, if the submission generated a
		// response (e.g., HTTP POST), we should display that to the user in
		// an embedded browser window.

		if(submitter.hasResponse())
		{
			try
			{
				BrowserEditorInput input = new BrowserEditorInput(project,
				        submitter.getResponse());

				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				        .getActivePage().openEditor(input, BrowserEditor.ID);
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
		}

		submitter.setLongRunningTaskManager(null);

		return true;
	}


	//~ Static/instance variables .............................................
	/**
	 * The main page of the wizard that contains the assignment list and other
	 * entry field.
	 */
	private SubmitterStartPage startPage;

	/**
	 * The summary page that shows the status of the submission, and any errors
	 * that may have occurred.
	 */
	private SubmitterSummaryPage finalPage;

	/**
	 * A reference to the submission engine that should be used by the wizard.
	 */
	private Submitter submitter;

	/**
	 * A reference to the project that will be submitted.
	 */
	private IProject project;
}
