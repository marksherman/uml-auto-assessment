/*==========================================================================*\
 |  $Id: RunnableContextLongRunningTaskManager.java,v 1.2 2010/09/21 18:19:29 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
 |
 |  This file is part of Web-CAT Electronic Submitter.
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

package org.webcat.eclipse.submitter.core;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.webcat.submitter.ILongRunningTask;
import org.webcat.submitter.ILongRunningTaskManager;
import org.webcat.submitter.IProgressChangeListener;

//--------------------------------------------------------------------------
/**
 * A long-running task manager implementation for the electronic submitter
 * that hooks into an Eclipse {@code IRunnableContext} for progress
 * notification and backgrounding.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/09/21 18:19:29 $
 */
public class RunnableContextLongRunningTaskManager implements
		ILongRunningTaskManager
{
	//~ Constructors ..........................................................

	// ----------------------------------------------------------
	/**
	 * Initializes a new {@code RunnableContextLongRunningTaskManager} with
	 * the specified {@code IRunnableContext}.
	 * 
	 * @param context the {@code IRunnableContext} under which the tasks
	 *     should be run
	 */
	public RunnableContextLongRunningTaskManager(IRunnableContext context)
	{
		this.context = context;
	}

	
	//~ Methods ...............................................................

	// ----------------------------------------------------------
	/**
	 * @see ILongRunningTaskManager#run(ILongRunningTask)
	 */
	public void run(final ILongRunningTask task)
	throws InvocationTargetException
	{
		try
		{
			context.run(true, false, new IRunnableWithProgress()
			{
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException
				{
					try
					{
						// Create a new progress change listener that uses the
						// Eclipse progress monitor and attach it to the task.

						ProgressMonitorProgressChangeListener listener =
							new ProgressMonitorProgressChangeListener(
									task, monitor);

						task.addProgressChangeListener(listener);

						task.run();
						
						task.removeProgressChangeListener(listener);
					}
					catch (Exception e)
					{
						throw new InvocationTargetException(e);
					}
				}			
			});
		}
		catch (InterruptedException e)
		{
			SubmitterCore.log("The submitter engine task was interrupted", e);
		}
	}
	

	// ----------------------------------------------------------
	/**
	 * A progress change listener that posts progress change notifications to
	 * an Eclipse progress monitor.
	 */
	private static class ProgressMonitorProgressChangeListener
	implements IProgressChangeListener
	{
		//~ Constructors ......................................................

		// ----------------------------------------------------------
		/**
		 * Initializes a new {@code ProgressMonitorProgressChangeListner} with
		 * the specified task and progress monitor.
		 * 
		 * @param task the task being monitored
		 * @param monitor the progress monitor to notify
		 */
		public ProgressMonitorProgressChangeListener(
				ILongRunningTask task, IProgressMonitor monitor)
		{
			this.monitor = monitor;
			
			monitor.beginTask(task.getDescription(), 100);
		}


		//~ Methods ...........................................................

		// ----------------------------------------------------------
		/**
		 * @see IProgressChangeListener#progressChanged(int)
		 */
		public void progressChanged(int progress)
		{
			monitor.worked(progress - lastProgress);
			lastProgress = progress;
		}
		

		//~ Static/instance variables .........................................

		/* The progress monitor being notified. */
		private IProgressMonitor monitor;

		/* Keeps track of the last progress value encountered, since the
		   IProgressMonitor uses deltas instead of absolute values. */
		private int lastProgress = 0;
	}


	//~ Static/instance variables .............................................
	
	/* The context under which the tasks will be executed. */
	private IRunnableContext context;
}
