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
package net.sf.webcat.eclipse.cxxtest.submitterExtensions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import net.sf.webcat.eclipse.cxxtest.ICxxTestConstants;
import net.sf.webcat.eclipse.submitter.core.ISubmissionListener;
import net.sf.webcat.eclipse.submitter.core.SubmissionParameters;

public class TestResultsSubmissionListener implements ISubmissionListener {

	public void submissionFailed(SubmissionParameters params,
			Throwable exception)
	{
	}

	public void submissionStarted(SubmissionParameters params)
	{
	}

	public void submissionSucceeded(SubmissionParameters params, String response)
	{
		IFile resultsFile = params.getProject().getFile(
				ICxxTestConstants.BINARY_LOG_FILE);
		
		try
		{
			resultsFile.delete(true, new NullProgressMonitor());
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}
	}
}
