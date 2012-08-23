/*==========================================================================*\
 |  $Id$
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech 
 |
 |	This file is part of Web-CAT Eclipse Plugins.
 |
 |	Web-CAT is free software; you can redistribute it and/or modify
 |	it under the terms of the GNU General Public License as published by
 |	the Free Software Foundation; either version 2 of the License, or
 |	(at your option) any later version.
 |
 |	Web-CAT is distributed in the hope that it will be useful,
 |	but WITHOUT ANY WARRANTY; without even the implied warranty of
 |	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |	GNU General Public License for more details.
 |
 |	You should have received a copy of the GNU General Public License
 |	along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package net.sf.webcat.eclipse.cxxtest.properties;

import net.sf.webcat.eclipse.cxxtest.CxxTestNature;
import net.sf.webcat.eclipse.cxxtest.i18n.Messages;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPropertyPage;

/**
 * A project property page that allows the user to enable or disable CxxTest
 * features in a project.
 * 
 * Future enhancements may allow the user to include/exclude individual tests
 * from execution on this page.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
public class CxxTestPropertyPage extends PreferencePage implements
		IWorkbenchPropertyPage
{
	private IAdaptable element;

	private IProject project;

	private Button checkboxEnableCxxTest;

	protected Control createContents(Composite parent)
	{
		Composite comp = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = layout.marginHeight = 0;
		comp.setLayout(layout);

		checkboxEnableCxxTest = new Button(comp, SWT.CHECK);
		checkboxEnableCxxTest.setText(Messages.CxxTestPropertyPage_EnableCxxTest);

		checkboxEnableCxxTest.setSelection(isCxxTestEnabled());

		return comp;
	}

	public IAdaptable getElement()
	{
		return element;
	}

	public void setElement(IAdaptable element)
	{
		this.element = element;
		
		project = (IProject)element.getAdapter(IProject.class);
	}

	private boolean isCxxTestEnabled()
	{
		try
		{
			return CxxTestNature.hasNature(project);
		}
		catch(CoreException e) { }
		
		return false;
	}

	public boolean performOk()
	{
		try
		{
			if(checkboxEnableCxxTest.getSelection())
				CxxTestNature.addNature(project, null);
			else
				CxxTestNature.removeNature(project);
		}
		catch(CoreException e) { }

		return super.performOk();
	}
}
