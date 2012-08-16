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

import java.util.Map;

import net.sf.webcat.eclipse.cxxtest.i18n.Messages;
import net.sf.webcat.eclipse.cxxtest.ui.TestRunnerViewPart;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.swt.widgets.Display;

//------------------------------------------------------------------------
/**
 * This builder executes the generates test-case executable and adds problem
 * markers to the source file for any failed test cases. It also populates the
 * CxxTest view with information about the executed tests.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
public class CxxTestDriverRunner extends IncrementalProjectBuilder
{
	private class RunnerLaunchThread extends Thread
	{
		private IProject project;

		public RunnerLaunchThread(IProject project)
		{
			this.project = project;
			start();
		}

		public void run()
		{
			try
			{
				final ICProject cproject =
					CCorePlugin.getDefault().getCoreModel().create(project);

				IPath exePath = getExecutableFile().getProjectRelativePath();

				ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
				ILaunchConfigurationType type = launchManager.getLaunchConfigurationType(
						ICDTLaunchConfigurationConstants.ID_LAUNCH_C_APP);
				final ILaunchConfigurationWorkingCopy config = type.newInstance(null, "CxxTestRunner"); //$NON-NLS-1$
	
				config.setAttribute(IDebugUIConstants.ATTR_PRIVATE, true);
				config.setAttribute(IDebugUIConstants.ATTR_LAUNCH_IN_BACKGROUND, false);

				config.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROJECT_NAME,
						project.getName());
				config.setAttribute(ICDTLaunchConfigurationConstants.ATTR_PROGRAM_NAME,
						exePath.toString());

				IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
					public void run(IProgressMonitor monitor) throws CoreException {
						final ILaunch launch = config.launch(ILaunchManager.RUN_MODE, null);
						
						Display.getDefault().syncExec(new Runnable() {
							public void run()
							{
								TestRunnerViewPart runnerPart =
									CxxTestPlugin.getDefault().getTestRunnerView();
								runnerPart.testRunStarted(cproject, launch);
							}
						});

						// Wait for the launched process to complete.
						waitForProcess(launch);

						// Parse the CxxTest results file and enter the results in the
						// CxxTest view.
						Display.getDefault().syncExec(new Runnable() {
							public void run()
							{
								TestRunnerViewPart runnerPart =
									CxxTestPlugin.getDefault().getTestRunnerView();
								runnerPart.testRunEnded();
							}
						});				
					}
				};
				
				project.getWorkspace().run(runnable, project, IWorkspace.AVOID_UPDATE, null);
			}
			catch(CoreException e)
			{
			}
		}
		
		private void waitForProcess(ILaunch launch)
		{
			try
			{
				while(!launch.isTerminated())
					Thread.sleep(250);
			}
			catch(InterruptedException e) { }
		}
	}

	private class ExecutableChangedVisitor implements IResourceDeltaVisitor
	{
		private boolean exeChanged;

		public ExecutableChangedVisitor()
		{
			exeChanged = false;
		}

		public boolean visit(IResourceDelta delta) throws CoreException
		{
			if(delta.getResource().equals(getExecutableFile()))
				exeChanged = true;

			return true;
		}
		
		public boolean isExecutableChanged()
		{
			return exeChanged;
		}
	}

	private IFile getExecutableFile()
	{
		IProject project = getProject();

		IManagedBuildInfo buildInfo =
			ManagedBuildManager.getBuildInfo(project);

		IConfiguration configuration = buildInfo.getDefaultConfiguration();

		String exeName = buildInfo.getBuildArtifactName();
		String exeExtension = buildInfo.getBuildArtifactExtension();

		if(exeExtension.length() > 0)
			exeName += "." + exeExtension; //$NON-NLS-1$

		IFile file = project.getFile(configuration.getName() + "/" + exeName); //$NON-NLS-1$
		return file;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int, java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@SuppressWarnings("rawtypes")
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException
	{
		IProject project = getProject();

		// Since the test runner generates files in the project directory,
		// this will cause Eclipse to try to rebuild the project, and this
		// builder will execute again. To prevent an infinite loop, we only
		// run the test runner when the executable has changed (and so is
		// found in the resource delta).
		IResourceDelta delta = getDelta(project);
		ExecutableChangedVisitor visitor = new ExecutableChangedVisitor();
		
		if(delta != null)
		{
			delta.accept(visitor);
	
			if(!visitor.isExecutableChanged())
	       	{
				// We don't need to rebuild the test case runner, so bail out.
				monitor.done();
				return null;
			}
		}
		else
		{
			monitor.done();
			return null;
		}

        monitor.beginTask(Messages.CxxTestDriverRunner_RunningDriverTaskDescription, 1);
		deleteMarkers();

		// If there are any problems in the project after it has been
		// built, we probably shouldn't try to run the driver code.
        // UPDATE: We need to be more specific here, even warnings were
        // preventing the runner from executing.
		IMarker[] problems = project.findMarkers(IMarker.PROBLEM, true,
				IResource.DEPTH_INFINITE);
		if(problems.length > 0)
		{
			for(int i = 0; i < problems.length; i++)
			{
				if(problems[i].getAttribute(IMarker.SEVERITY,
						IMarker.SEVERITY_INFO) == IMarker.SEVERITY_ERROR)
				{
					monitor.done();
					return null;
				}
			}
		}

		monitor.worked(1);
		new RunnerLaunchThread(project);

		monitor.done();
		
		forgetLastBuiltState();

		return null;
	}

	protected void clean(IProgressMonitor monitor) throws CoreException
	{
		super.clean(monitor);
		
		deleteMarkers();
	}

	/**
	 * Delete any markers that were generated by the test runner.
	 * 
	 * @throws CoreException if a problem occurs when deleting the markers.
	 */
	private void deleteMarkers() throws CoreException
	{
		final IProject project = getProject();

		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor)
			{
				try
				{
					project.deleteMarkers(ICxxTestConstants.MARKER_INVOCATION_PROBLEM,
							true, IResource.DEPTH_INFINITE);
					project.deleteMarkers(ICxxTestConstants.MARKER_FAILED_TEST, true,
							IResource.DEPTH_INFINITE);
				}
				catch (CoreException e) { }
			}
		};

		project.getWorkspace().run(runnable, new NullProgressMonitor());
	}
}
