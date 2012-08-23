/*==========================================================================*\
 |  $Id: OpenWebCATAction.java,v 1.2 2010/12/06 21:08:41 aallowat Exp $
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.webcat.eclipse.submitter.core.SubmitterCore;

//--------------------------------------------------------------------------
/**
 * The workbench action delegate that opens a browser window to the Web-CAT
 * server that this instance of Eclipse is configured to use.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/12/06 21:08:41 $
 */
public class OpenWebCATAction implements IWorkbenchWindowActionDelegate
{
	//~ Methods ...............................................................
	
	// ----------------------------------------------------------
	/**
	 * Called when the workbench action is invoked.
	 */
	public void run(IAction action)
	{
		String url = SubmitterCore.getDefault().getPreferenceStore().getString(
				SubmitterCore.DEFINITIONS_URL);

		Pattern pattern = Pattern.compile(
				"(https?://.+/Web-CAT.woa)", //$NON-NLS-1$
				Pattern.CASE_INSENSITIVE);
		
		Matcher matcher = pattern.matcher(url);
		String match = matcher.group(1);
		
		Program.launch(match);
	}


	// ----------------------------------------------------------
	public void selectionChanged(IAction action, ISelection selection)
	{
		// Do nothing.
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
	public void init(IWorkbenchWindow window)
	{
		// Do nothing.
	}
}
