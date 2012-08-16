/*==========================================================================*\
 |  $Id: CxxTestEarlyStartup.java,v 1.3 2009/09/13 12:59:28 aallowat Exp $
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

package net.sf.webcat.eclipse.cxxtest.internal;

import net.sf.webcat.eclipse.cxxtest.CxxTestPlugin;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IStartup;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *  
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2009/09/13 12:59:28 $
 */
public class CxxTestEarlyStartup implements IStartup
{
	public void earlyStartup()
	{
		IPreferenceStore store =
			CxxTestPlugin.getDefault().getPreferenceStore();

		// Force the check modally if this is the first time the plugin is
		// begin instantiated.

		boolean firstTime = store.getBoolean(
				CxxTestPlugin.CXXTEST_PREF_FIRST_TIME);

		StackTraceDependencyChecker.checkForDependencies(firstTime);

		store.setValue(CxxTestPlugin.CXXTEST_PREF_FIRST_TIME, false);
	}
}
