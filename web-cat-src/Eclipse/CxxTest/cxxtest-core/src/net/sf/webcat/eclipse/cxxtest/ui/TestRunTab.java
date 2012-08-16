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

package net.sf.webcat.eclipse.cxxtest.ui;

import net.sf.webcat.eclipse.cxxtest.ICxxTestRunListener;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.dnd.Clipboard;

/**
 * The base class for all the CxxTest view tabs.  (Currently there is only
 * one, TestHierarchyTab).
 * 
 * Influenced greatly by the same JUnit class.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
public abstract class TestRunTab implements ICxxTestRunListener
{	
	/**
	 * Create the tab control
	 * @param tabFolder the containing tab folder
	 * @param clipboard the clipboard to be used by the tab
	 * @param runner the testRunnerViewPart containing the tab folder
	 */
	public abstract void createTabControl(CTabFolder tabFolder,
			Clipboard clipboard, TestRunnerViewPart runner);
	
	/**
	 * Returns the name of the currently selected Test in the View
	 */
	public abstract Object getSelectedObject();

	/**
	 * Activates the TestRunView
	 */
	public void activate()
	{
	}

	/**
	 * Sets the focus in the TestRunView
	 */
	public void setFocus()
	{
	}
	
	/**
	 * Returns the name of the RunView
	 */
	public abstract String getName();

	/**
	 * Select next test failure.
	 */
	public void selectNext()
	{
	}
	
	/**
	 * Select previous test failure.
	 */
	public void selectPrevious()
	{
	}
}
