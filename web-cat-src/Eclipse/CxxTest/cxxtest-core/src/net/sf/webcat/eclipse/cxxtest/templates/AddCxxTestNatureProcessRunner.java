/*==========================================================================*\
 |  $Id: AddCxxTestNatureProcessRunner.java,v 1.2 2009/09/13 12:59:29 aallowat Exp $
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

package net.sf.webcat.eclipse.cxxtest.templates;

import net.sf.webcat.eclipse.cxxtest.CxxTestNature;

import org.eclipse.cdt.core.templateengine.TemplateCore;
import org.eclipse.cdt.core.templateengine.process.ProcessArgument;
import org.eclipse.cdt.core.templateengine.process.ProcessFailureException;
import org.eclipse.cdt.core.templateengine.process.ProcessRunner;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * The implementation of a CDT template process used by the "Empty Project w/
 * CxxTest" project type.  This class adds the CxxTest nature to a new C++
 * project so that the appropriate build settings (include path, framework
 * settings, and platform-specific settings) get loaded into the project when it
 * is created.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2009/09/13 12:59:29 $
 */
public class AddCxxTestNatureProcessRunner extends ProcessRunner
{
	public void process(TemplateCore template, ProcessArgument[] args,
	        String processId, IProgressMonitor monitor)
	        throws ProcessFailureException
	{
		String projectName = args[0].getSimpleValue();
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(
		        projectName);

		try
		{
			CxxTestNature.addNature(project, monitor);
		}
		catch(CoreException e)
		{
			throw new ProcessFailureException(e);
		}
	}
}
