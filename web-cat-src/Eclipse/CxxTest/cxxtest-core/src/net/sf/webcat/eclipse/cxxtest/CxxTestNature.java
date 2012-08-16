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

package net.sf.webcat.eclipse.cxxtest;

import net.sf.webcat.eclipse.cxxtest.options.IExtraOptionsUpdater;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.IPreferenceStore;

//------------------------------------------------------------------------
/**
 * The project nature attached to CxxTest projects.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
public class CxxTestNature implements IProjectNature
{
	//~ Methods ...............................................................

	// ----------------------------------------------------------
	public void configure() throws CoreException
	{
		addBuilders(getProject());
	}


	// ----------------------------------------------------------
	public void deconfigure() throws CoreException
	{
		removeBuilder(getProject(), CxxTestPlugin.CXXTEST_BUILDER);
		removeBuilder(getProject(), CxxTestPlugin.CXXTEST_RUNNER);
	}


	// ----------------------------------------------------------
	public IProject getProject()
	{
		return project;
	}


	// ----------------------------------------------------------
	public void setProject(IProject project)
	{
		this.project = project;
	}


	// ----------------------------------------------------------
	public static boolean hasNature(IProject project) throws CoreException
	{
		String natureId = CxxTestPlugin.CXXTEST_NATURE;
		IProjectDescription description = project.getDescription();
		String[] natureIds = description.getNatureIds();
		int index = -1;

		for(int i = 0; i < natureIds.length; ++i)
		{
			if(natureIds[i].equals(natureId))
			{
				index = i;
			}
		}

		return index != -1;
	}


	// ----------------------------------------------------------
	public static boolean addNature(IProject project, IProgressMonitor monitor)
	        throws CoreException
	{
		IProjectDescription description = project.getDescription();
		String[] natureIds = description.getNatureIds();

		int index = -1;

		for(int i = 0; i < natureIds.length; ++i)
		{
			if(natureIds[i].equals(CxxTestPlugin.CXXTEST_NATURE))
			{
				index = i;
				break;
			}
		}

		try
		{
			if(index == -1)
			{
				String[] newNatureIds = new String[natureIds.length + 1];
				System.arraycopy(natureIds, 0, newNatureIds, 1,
				        natureIds.length);

				newNatureIds[0] = CxxTestPlugin.CXXTEST_NATURE;

				description.setNatureIds(newNatureIds);
				project.setDescription(description, monitor);
			}
		}
		catch(CoreException ex)
		{
			if(description != null && natureIds != null)
			{
				description.setNatureIds(natureIds);
				project.setDescription(description, monitor);
			}

			throw ex;
		}

		IExtraOptionsUpdater updater =
		        CxxTestPlugin.getDefault().getExtraOptionsUpdater();
		updater.updateOptions(project);
		
		IPreferenceStore store = CxxTestPlugin.getDefault().getPreferenceStore();
		boolean stackTrace = store.getBoolean(CxxTestPlugin.CXXTEST_PREF_TRACE_STACK);

		project.setPersistentProperty(new QualifiedName(
				CxxTestPlugin.PLUGIN_ID, ICxxTestConstants.PROP_STACK_TRACE_ENABLED),
				Boolean.toString(stackTrace));

		return index == -1;
	}


	// ----------------------------------------------------------
	public static void addBuilders(IProject project) throws CoreException
	{
		IProjectDescription description = project.getDescription();
		ICommand[] commands = description.getBuildSpec();

		for(int i = 0; i < commands.length; i++)
		{
			if(commands[i].getBuilderName().equals(
			        CxxTestPlugin.CXXTEST_BUILDER))
				return;
		}

		ICommand builderCommand = description.newCommand();
		builderCommand.setBuilderName(CxxTestPlugin.CXXTEST_BUILDER);

		ICommand runnerCommand = description.newCommand();
		runnerCommand.setBuilderName(CxxTestPlugin.CXXTEST_RUNNER);

		ICommand[] newCommands = new ICommand[commands.length + 2];
		System.arraycopy(commands, 0, newCommands, 1, commands.length);

		newCommands[0] = builderCommand;
		newCommands[newCommands.length - 1] = runnerCommand;

		description.setBuildSpec(newCommands);
		project.setDescription(description, null);
	}


	// ----------------------------------------------------------
	public static boolean removeNature(IProject project) throws CoreException
	{
		IExtraOptionsUpdater updater =
	        CxxTestPlugin.getDefault().getExtraOptionsUpdater();
		updater.removeAllOptions(project);

		IProjectDescription description = project.getDescription();
		String[] natureIds = description.getNatureIds();

		for(int i = 0; i < natureIds.length; ++i)
		{
			if(natureIds[i].equals(CxxTestPlugin.CXXTEST_NATURE))
			{
				String[] newNatureIds = new String[natureIds.length - 1];

				System.arraycopy(natureIds, 0, newNatureIds, 0, i);
				System.arraycopy(natureIds, i + 1, newNatureIds, i,
				        natureIds.length - i - 1);

				try
				{
					description.setNatureIds(newNatureIds);
					project.setDescription(description, null);
				}
				catch(CoreException ex)
				{
					if(description != null && natureIds != null)
					{
						description.setNatureIds(natureIds);
						project.setDescription(description, null);
					}

					throw ex;
				}

				return true;
			}
		}

		return false;
	}


	// ----------------------------------------------------------
	protected static boolean removeBuilder(IProject project, String builderId)
	        throws CoreException
	{
		IProjectDescription description = project.getDescription();
		ICommand[] commands = description.getBuildSpec();

		for(int i = 0; i < commands.length; ++i)
		{
			if(commands[i].getBuilderName().equals(builderId))
			{
				ICommand[] newCommands = new ICommand[commands.length - 1];

				System.arraycopy(commands, 0, newCommands, 0, i);
				System.arraycopy(commands, i + 1, newCommands, i,
				        commands.length - i - 1);

				description.setBuildSpec(newCommands);
				project.setDescription(description, null);

				return true;
			}
		}

		return false;
	}


	//~ Static/instance variables .............................................
	
	/* The project that the nature is being added to/removed from. */
	private IProject project;
}
