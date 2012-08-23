/*==========================================================================*\
 |  $Id: SubmitterCore.java,v 1.3 2010/09/21 18:19:29 aallowat Exp $
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

package org.webcat.eclipse.submitter.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

//--------------------------------------------------------------------------
/**
 * The main class for the Eclipse electronic submitter plug-in.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2010/09/21 18:19:29 $
 */
public class SubmitterCore extends AbstractUIPlugin
{
	//~ Constructors ..........................................................

	// ----------------------------------------------------------
	/**
	 * Creates a new instance of the SubmitterCore class.
	 */
	public SubmitterCore()
	{
		plugin = this;

		try
		{
			resourceBundle = ResourceBundle.getBundle(
					PLUGIN_ID + ".core.SubmitterCoreResources");
		}
		catch (MissingResourceException e)
		{
			resourceBundle = null;
		}
	}


	// ----------------------------------------------------------
	/**
	 * Gets the shared instance of the plug-in.
	 * 
	 * @return the shared instance of the plug-in
	 */
	public static SubmitterCore getDefault()
	{
		return plugin;
	}

	
	// ----------------------------------------------------------
	/**
	 * Writes a log message to the Eclipse error log.
	 * 
	 * @param message the log message
	 */
	public static void log(String message)
	{
		log(message, null);
	}

	
	// ----------------------------------------------------------
	/**
	 * Writes a log message and its associated exception to the Eclipse
	 * error log.
	 * 
	 * @param message the log message
	 * @param exception the exception, or null if there wasn't one
	 */
	public static void log(String message, Exception exception)
	{
		getDefault().getLog().log(new Status(IStatus.ERROR,
				SubmitterCore.PLUGIN_ID, IStatus.OK, message, exception));
	}


	// ----------------------------------------------------------
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		
		getPreferenceStore().addPropertyChangeListener(
				new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event)
			{
				if (DEFINITIONS_URL.equals(event.getProperty()))
				{
					updateOpenWebCATEnablement();
				}
			}			
		});
		
		updateOpenWebCATEnablement();
	}


	// ----------------------------------------------------------
	public void updateOpenWebCATEnablement()
	{
		String url = getPreferenceStore().getString(DEFINITIONS_URL);
		boolean isWebCAT = false;

		if (url != null && url.length() > 0)
		{
			Pattern wcPattern = Pattern.compile(
					"https?://.+/Web-CAT.woa/.*",
					Pattern.CASE_INSENSITIVE);
			
			isWebCAT = wcPattern.matcher(url).matches();
		}
		
		System.setProperty(PROP_SUBMISSION_URL_IS_WEBCAT,
				Boolean.toString(isWebCAT));
	}


	// ----------------------------------------------------------
	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 * 
	 * @param key the key to look up in the resource bundle
	 * @return the value of the string
	 */
	public static String getResourceString(String key)
	{
		ResourceBundle bundle = SubmitterCore.getDefault().getResourceBundle();

		try
		{
			return (bundle != null) ? bundle.getString(key) : key;
		}
		catch (MissingResourceException e)
		{
			return key;
		}
	}


	// ----------------------------------------------------------
	/**
	 * Gets the plugin's resource bundle.
	 * 
	 * @return the plugin's resource bundle
	 */
	public ResourceBundle getResourceBundle()
	{
		return resourceBundle;
	}


	// ----------------------------------------------------------
	/**
	 * Gets the value of the specified preference string.
	 * 
	 * @param id the key of the preference value to obtain
	 * @return the value of the requested preference
	 */
	public String getOption(String id)
	{
		IPreferenceStore store = getPreferenceStore();
		return store.getString(id);
	}


	//~ Static/instance variables .............................................

	/* The shared instance of the plug-in. */
	private static SubmitterCore plugin;

	/* The plug-in identifier of the submitter's core support. */
	public static final String PLUGIN_ID = "net.sf.webcat.eclipse.submitter";

	/* The preference key that stores the URL to the submission targets
	   file. */
	public static final String DEFINITIONS_URL = PLUGIN_ID + ".definitions.URL";

	/* The preference key that stores the outgoing mail server hostname. */
	public static final String IDENTIFICATION_SMTPSERVER = PLUGIN_ID
	        + ".identification.smtpServer";

	/* The preference key that stores the default username. */
	public static final String IDENTIFICATION_DEFAULTUSERNAME = PLUGIN_ID
	        + ".identification.defaultUsername";

	/* The preference key that stores the e-mail address of the user. */
	public static final String IDENTIFICATION_EMAILADDRESS = PLUGIN_ID
	        + ".identification.emailAddress";

	private static final String PROP_SUBMISSION_URL_IS_WEBCAT =
			PLUGIN_ID + ".SubmissionURLIsWebCAT";

	/* The plug-in's resource bundle. */
	private ResourceBundle resourceBundle;
}
