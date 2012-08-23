package org.webcat.eclipse.submitter.core;

import org.eclipse.ui.IStartup;

public class OpenWebCATEnablementUpdater implements IStartup
{
	// ----------------------------------------------------------
	public void earlyStartup()
	{
		SubmitterCore.getDefault().updateOpenWebCATEnablement();
	}
}
