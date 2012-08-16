/*==========================================================================*\
 |  $Id: SubmitterUIPlugin.java,v 1.3 2010/12/06 21:08:41 aallowat Exp $
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

package org.webcat.eclipse.submitter.ui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.webcat.eclipse.submitter.core.RunnableContextLongRunningTaskManager;
import org.webcat.eclipse.submitter.core.SubmitterCore;
import org.webcat.eclipse.submitter.ui.dialogs.SubmissionParserErrorDialog;
import org.webcat.eclipse.submitter.ui.i18n.Messages;
import org.webcat.eclipse.submitter.ui.wizards.SubmitterWizard;
import org.webcat.submitter.Submitter;

//--------------------------------------------------------------------------
/**
 * The main plug-in class for the submitter user interface plug-in.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2010/12/06 21:08:41 $
 */
public class SubmitterUIPlugin extends AbstractUIPlugin
{
	//~ Constructors ..........................................................

	// ----------------------------------------------------------
	/**
	 * Initializes a new instance of the SubmitterUIPlugin.
	 */
	public SubmitterUIPlugin()
	{
		super();
		plugin = this;
		try
		{
			resourceBundle = ResourceBundle.getBundle(PLUGIN_ID +
					".SubmitterUIPluginResources"); //$NON-NLS-1$
		}
		catch(MissingResourceException x)
		{
			resourceBundle = null;
		}
	}


	//~ Methods ...............................................................

	// ----------------------------------------------------------
	/**
	 * Returns the shared instance of the plug-in.
	 * 
	 * @return the shared instance of the plug-in
	 */
	public static SubmitterUIPlugin getDefault()
	{
		return plugin;
	}


	// ----------------------------------------------------------
	/**
	 * Returns the string from the plugin's resource bundle, or the key itself
	 * if not found.
	 * 
	 * @param key the key of the string to return
	 * @return the string with the specified key, or the key itself if the
	 *     string was not found
	 */
	public static String getResourceString(String key)
	{
		ResourceBundle bundle = SubmitterUIPlugin.getDefault()
		        .getResourceBundle();
		try
		{
			return (bundle != null) ? bundle.getString(key) : key;
		}
		catch(MissingResourceException e)
		{
			return key;
		}
	}


	// ----------------------------------------------------------
	/**
	 * Gets the plug-in's resource bundle.
	 * 
	 * @return the plug-in's resource bundle
	 */
	public ResourceBundle getResourceBundle()
	{
		return resourceBundle;
	}


	// ----------------------------------------------------------
	/**
	 * Initializes the submission engine and invokes the submission wizard.
	 * 
	 * @param shell the shell that will be the parent to the wizard
	 * @param project the Eclipse project to be submitted
	 */
	public void spawnSubmissionUI(Shell shell, IProject project)
	{
		URL url;
		Submitter engine = new Submitter();

		try
		{
			url = new URL(SubmitterCore.getDefault().getOption(
			        SubmitterCore.DEFINITIONS_URL));

			ProgressMonitorDialog dlg = new ProgressMonitorDialog(shell);
			
			RunnableContextLongRunningTaskManager taskManager =
				new RunnableContextLongRunningTaskManager(dlg);
			engine.setLongRunningTaskManager(taskManager);
			
			engine.readSubmissionTargets(url);
			
			engine.setLongRunningTaskManager(null);
		}
		catch(MalformedURLException e)
		{
			MessageDialog.openWarning(null, Messages.PLUGINUI_NO_DEF_URL_TITLE,
			        Messages.PLUGINUI_NO_DEF_URL_DESCRIPTION);
			return;
		}
		catch(Throwable e)
		{
			SubmissionParserErrorDialog dlg = new SubmissionParserErrorDialog(
			        shell, e);
			dlg.open();

			return;
		}

		SubmitterWizard wizard = new SubmitterWizard();
		wizard.init(engine, project);

		// Instantiates the wizard container with the wizard and opens it
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.open();
	}


	// ----------------------------------------------------------
	/**
	 * Gets an image descriptor for the specified image in the plug-in's
	 * "icons" directory.
	 * 
	 * @param path the path to the icon that should be loaded, relative to
	 *     the "icons" folder in the plug-in
	 * @return an ImageDescriptor for the image
	 */
	public static ImageDescriptor getImageDescriptor(String path)
	{
		try
		{
			URL base = Platform.getBundle(PLUGIN_ID).getEntry(
					"/icons/"); //$NON-NLS-1$
			URL url = new URL(base, path);

			return ImageDescriptor.createFromURL(url);
		}
		catch(MalformedURLException e)
		{
			// Do nothing.
		}

		return null;
	}
	
	
	// ----------------------------------------------------------
	/**
	 * Gets the most recently entered username in the submission wizard.
	 * 
	 * @return the most recently entered username
	 */
	public String getLastEnteredUsername()
	{
		return lastEnteredUsername;
	}


	// ----------------------------------------------------------
	/**
	 * Sets the most recently entered username in the submission wizard.
	 * 
	 * @param username the most recently entered username
	 */
	public void setLastEnteredUsername(String username)
	{
		lastEnteredUsername = username;
	}


	// ----------------------------------------------------------
	/**
	 * Gets the most recently entered password in the submission wizard.
	 * 
	 * @return the most recently entered password
	 */
	public String getLastEnteredPassword()
	{
		return lastEnteredPassword;
	}


	// ----------------------------------------------------------
	/**
	 * Sets the most recently entered password in the submission wizard.
	 * 
	 * @param password the most recently entered password
	 */
	public void setLastEnteredPassword(String password)
	{
		lastEnteredPassword = password;
	}


	// ----------------------------------------------------------
	/**
	 * Gets the most recently entered partner usernames in the submission
	 * wizard.
	 * 
	 * @return the most recently entered partner usernames
	 */
	public String getLastEnteredPartners()
	{
		return lastEnteredPartners;
	}


	// ----------------------------------------------------------
	/**
	 * Sets the most recently entered partner usernames in the submission
	 * wizard.
	 * 
	 * @param partners the most recently entered partner usernames
	 */
	public void setLastEnteredPartners(String partners)
	{
		lastEnteredPartners = partners;
	}


	// ----------------------------------------------------------
	/**
	 * Gets the path to the most recently selected assignment in the
	 * submission wizard.
	 * 
	 * @return the path to the most recently selected assignment
	 */
	public String getLastSelectedAssignmentPath()
	{
		return lastSelectedAssignmentPath;
	}


	// ----------------------------------------------------------
	/**
	 * Sets the path to the most recently selected assignment in the
	 * submission wizard.
	 * 
	 * @param path the path to the most recently selected assignment
	 */
	public void setLastSelectedAssignmentPath(String path)
	{
		lastSelectedAssignmentPath = path;
	}


	//~ Static/instance variables .............................................

	/**
	 * The unique identifier of the plug-in.
	 */
	public static final String PLUGIN_ID =
		"net.sf.webcat.eclipse.submitter.ui"; //$NON-NLS-1$

	/* The shared instance of the plug-in. */
	private static SubmitterUIPlugin plugin;

	/* The resource bundle of the plug-in. */
	private ResourceBundle resourceBundle;
	
	private String lastSelectedAssignmentPath;
	private String lastEnteredUsername;
	private String lastEnteredPassword;
	private String lastEnteredPartners;
}
