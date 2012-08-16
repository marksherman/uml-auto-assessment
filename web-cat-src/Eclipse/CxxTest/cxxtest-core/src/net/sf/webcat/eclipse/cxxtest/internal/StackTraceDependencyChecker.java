/*==========================================================================*\
 |  $Id: StackTraceDependencyChecker.java,v 1.2 2009/09/13 12:59:28 aallowat Exp $
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

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import net.sf.webcat.eclipse.cxxtest.CxxTestPlugin;
import net.sf.webcat.eclipse.cxxtest.IStackTraceDependencyCheck;
import net.sf.webcat.eclipse.cxxtest.i18n.Messages;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2009/09/13 12:59:28 $
 */
public class StackTraceDependencyChecker
{
	private StackTraceDependencyChecker()
	{
		// Prevent instantiation.
	}
	
	
	public static boolean checkForDependencies(final boolean forceModal)
	{
		final IPreferenceStore store =
			CxxTestPlugin.getDefault().getPreferenceStore();

		final MutableBoolean result = new MutableBoolean();

		final boolean alreadyFound = store.getBoolean(
				CxxTestPlugin.CXXTEST_PREF_HAS_REQUIRED_LIBRARIES);

		if (!forceModal && alreadyFound)
		{
			// Run the check in the background if we've already found the
			// dependencies once. That way if anything on the system changes
			// since the last run, we can still detect it and disable stack
			// tracing, but without forcing the user to wait for a modal
			// operation at every startup.
			
			Job job = new Job(
					Messages.StackTraceDependencyChecker_BackgroundJobName) {
				@Override
				protected IStatus run(IProgressMonitor monitor)
				{
					boolean success = runDependencyChecks(
							forceModal || !alreadyFound, store, monitor);

					result.setValue(success);

					return Status.OK_STATUS;
				}
			};

			job.schedule();

			return result.getValue();
		}

		// If this is the first time we're checking, or if it's being forced
		// by the user (for example, by enable stack traces in the preferences
		// window), then display it has a modal dialog.

		Display.getDefault().syncExec(new Runnable() {
			public void run()
			{
				try
				{
					IProgressService service =
						PlatformUI.getWorkbench().getProgressService();

					service.run(true, false,
							new IRunnableWithProgress() {
								public void run(IProgressMonitor monitor)
										throws InvocationTargetException,
										InterruptedException
								{
									boolean success = runDependencyChecks(
											forceModal || !alreadyFound,
											store, monitor);

									result.setValue(success);
								}
					});
				}
				catch (InvocationTargetException e)
				{
					e.printStackTrace();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		});

		return result.getValue();
	}


	private static boolean runDependencyChecks(boolean forceModal,
			IPreferenceStore store, IProgressMonitor monitor)
	{
		boolean anyDependenciesMissing = false;
		final ArrayList<String> missingDependencies = new ArrayList<String>();

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint =
		        registry.getExtensionPoint(CxxTestPlugin.PLUGIN_ID
		                + ".stackTraceDependencyCheck"); //$NON-NLS-1$

		IConfigurationElement[] elements =
		        extensionPoint.getConfigurationElements();

		monitor.beginTask(
				Messages.StackTraceDependencyChecker_BackgroundJobDescription,
				elements.length);

		for(IConfigurationElement element : elements)
		{
			SubProgressMonitor submon = new SubProgressMonitor(monitor, 1);
			
			if ("dependencyCheck".equals(element.getName())) //$NON-NLS-1$
			{
				try
				{
					Object obj = element.createExecutableExtension("class"); //$NON-NLS-1$
					if (obj instanceof IStackTraceDependencyCheck)
					{
						IStackTraceDependencyCheck checker =
							(IStackTraceDependencyCheck) obj;
						
						boolean success = checker.checkForDependencies(submon);
						
						if (!success)
						{
							anyDependenciesMissing = true;
							
							missingDependencies.add(
									checker.missingDependencies());
						}
					}
				}
				catch (CoreException e)
				{
					// Do nothing.
				}
			}
			
			monitor.worked(1);
		}
		
		monitor.done();

		if (anyDependenciesMissing)
		{
			boolean hadDependenciesBefore = store.getBoolean(
					CxxTestPlugin.CXXTEST_PREF_HAS_REQUIRED_LIBRARIES);

			if (hadDependenciesBefore || forceModal)
			{
				final IWorkbench workbench = PlatformUI.getWorkbench();
				workbench.getDisplay().syncExec(new Runnable()
				{
					public void run()
					{
						Shell shell =
							workbench.getActiveWorkbenchWindow().getShell();
						showMissingDependencyDialog(shell, missingDependencies);
					}
				});
			}

			// Disable stack traces in preferences.
			store.setValue(CxxTestPlugin.CXXTEST_PREF_TRACE_STACK, false);
			store.setValue(CxxTestPlugin.CXXTEST_PREF_HAS_REQUIRED_LIBRARIES,
					false);

			return false;
		}
		else
		{
			// Don't enable PREF_TRACE_STACK here. The default value is
			// already true, and since this runs on every startup, 
			store.setValue(CxxTestPlugin.CXXTEST_PREF_HAS_REQUIRED_LIBRARIES,
					true);

			return true;
		}
	}


	private static void showMissingDependencyDialog(Shell shell,
			List<String> missingDependencies)
	{
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(
			Messages.StackTraceDependencyChecker_MissingDependencyMsgStart);

		for (String missingDep : missingDependencies)
		{
			buffer.append(MessageFormat.format(
					Messages.StackTraceDependencyChecker_MissingDependencyPartDescription,
					missingDep));
		}

		buffer.append(Messages.StackTraceDependencyChecker_MissingDependencyMsgEnd);

		MessageDialog.openInformation(shell,
				Messages.StackTraceDependencyChecker_ShellTitle,
				buffer.toString());		
	}
}
