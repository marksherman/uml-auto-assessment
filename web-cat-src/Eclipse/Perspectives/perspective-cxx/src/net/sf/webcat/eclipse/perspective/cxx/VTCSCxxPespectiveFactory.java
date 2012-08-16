/*
 *	This file is part of Web-CAT Eclipse Plugins.
 *
 *	Web-CAT is free software; you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation; either version 2 of the License, or
 *	(at your option) any later version.
 *
 *	Web-CAT is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with Web-CAT; if not, write to the Free Software
 *	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package net.sf.webcat.eclipse.perspective.cxx;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * The factory for VTCS's customized C/C++ perspective.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class VTCSCxxPespectiveFactory implements IPerspectiveFactory
{
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	public void createInitialLayout(IPageLayout layout)
	{
		defineActions(layout);
		defineLayout(layout);
	}

	private void defineActions(IPageLayout layout)
	{
		// Make sure the VTCS submission plugin action sets are available
		// to the user by default.
		layout.addActionSet("net.sf.webcat.eclipse.submitter.SubmissionActionSet");
		layout.addActionSet("org.eclipse.debug.ui.launchActionSet");

		// Add the same new wizard shortcuts that we're used to seeing
		// in the C++ perspective.
		layout.addNewWizardShortcut("org.eclipse.cdt.managedbuilder.ui.wizards.StdCCWizard");
		layout.addNewWizardShortcut("net.sf.webcat.eclipse.cxxtest.wizards.StdCCWizard");
		layout.addNewWizardShortcut("org.eclipse.cdt.ui.wizards.NewSourceFolderCreationWizard");
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
		layout.addNewWizardShortcut("org.eclipse.cdt.ui.wizards.NewSourceFileCreationWizard");
		layout.addNewWizardShortcut("org.eclipse.cdt.ui.wizards.NewHeaderFileCreationWizard");
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");
		layout.addNewWizardShortcut("org.eclipse.cdt.ui.wizards.NewClassCreationWizard");
	}

	private void defineLayout(IPageLayout layout)
	{
		String editorArea = layout.getEditorArea();

		// Place navigator and outline to left of
		// editor area.
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT,
				(float)0.25, editorArea);
		left.addView("org.eclipse.cdt.ui.CView");
		left.addView(IPageLayout.ID_PROJECT_EXPLORER);
		left.addView(IPageLayout.ID_OUTLINE);

		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM,
				(float)0.75, editorArea);
		bottom.addView("org.eclipse.ui.console.ConsoleView");
		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottom.addView(IPageLayout.ID_TASK_LIST);
		bottom.addView("net.sf.webcat.eclipse.cxxtest.TestRunnerView");
	}
}
