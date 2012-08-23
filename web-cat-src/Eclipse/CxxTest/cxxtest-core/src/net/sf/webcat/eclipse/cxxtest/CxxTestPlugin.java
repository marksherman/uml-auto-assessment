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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.sf.webcat.eclipse.cxxtest.i18n.Messages;
import net.sf.webcat.eclipse.cxxtest.internal.CxxTestPreferencesChangeListener;
import net.sf.webcat.eclipse.cxxtest.internal.options.ExtraOptionsUpdater;
import net.sf.webcat.eclipse.cxxtest.options.IExtraOptionsUpdater;
import net.sf.webcat.eclipse.cxxtest.ui.TestRunnerViewPart;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.BundleContext;

//------------------------------------------------------------------------
/**
 * The main plugin class to be used in the desktop.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
public class CxxTestPlugin extends AbstractUIPlugin
{
	//~ Constructors ..........................................................
	
	// ----------------------------------------------------------
	public CxxTestPlugin()
	{
		super();
		plugin = this;

		try
		{
			resourceBundle = ResourceBundle.getBundle(
					"net.sf.webcat.eclipse.cxxtest.BuilderPluginResources"); //$NON-NLS-1$
		}
		catch(MissingResourceException x)
		{
			resourceBundle = null;
		}
	}


	//~ Methods ...............................................................

	// ----------------------------------------------------------
	/**
	 * This method is called upon plug-in activation.
	 */
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		
		getPreferenceStore().addPropertyChangeListener(
				new CxxTestPreferencesChangeListener());
	}
	

	// ----------------------------------------------------------
	/**
	 * This method is called when the plug-in is stopped.
	 */
	public void stop(BundleContext context) throws Exception
	{
		super.stop(context);
	}


	// ----------------------------------------------------------
	/**
	 * Returns the shared instance.
	 */
	public static CxxTestPlugin getDefault()
	{
		return plugin;
	}


	// ----------------------------------------------------------
	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key)
	{
		ResourceBundle bundle = CxxTestPlugin.getDefault().getResourceBundle();

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
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle()
	{
		return resourceBundle;
	}


	// ----------------------------------------------------------
	public String getString(String id)
	{
		IPreferenceStore store = getPreferenceStore();
		return store.getString(id);
	}


	// ----------------------------------------------------------
	public boolean getBoolean(String id)
	{
		IPreferenceStore store = getPreferenceStore();
		return store.getBoolean(id);
	}


	// ----------------------------------------------------------
	public boolean getConfigurationBoolean(String id)
	{
		IPreferenceStore store = new ScopedPreferenceStore(
		        new ConfigurationScope(), PLUGIN_ID);
		return store.getBoolean(id);
	}


	// ----------------------------------------------------------
	public MessageConsole getBuilderConsole()
	{
		if(builderConsole == null)
		{
			builderConsole = new MessageConsole(Messages.CxxTestPlugin_ConsoleTitle,
			        null);

			IConsoleManager manager = ConsolePlugin.getDefault()
			        .getConsoleManager();
			manager.addConsoles(new IConsole[] { builderConsole });
		}

		return builderConsole;
	}


	// ----------------------------------------------------------
	public static IWorkbenchWindow getActiveWorkbenchWindow()
	{
		if(plugin == null)
			return null;

		IWorkbench workBench = plugin.getWorkbench();

		if(workBench == null)
			return null;

		return workBench.getActiveWorkbenchWindow();
	}


	// ----------------------------------------------------------
	public static IWorkbenchPage getActivePage()
	{
		IWorkbenchWindow activeWorkbenchWindow = getActiveWorkbenchWindow();

		if(activeWorkbenchWindow == null)
			return null;

		return activeWorkbenchWindow.getActivePage();
	}


	// ----------------------------------------------------------
	public TestRunnerViewPart getTestRunnerView()
	{
		IWorkbenchPage page = getActivePage();

		if(page == null)
			return null;

		TestRunnerViewPart view = (TestRunnerViewPart)page
		        .findView(TestRunnerViewPart.ID);

		if(view == null)
		{
			try
			{
				view = (TestRunnerViewPart)page.showView(TestRunnerViewPart.ID);
			}
			catch(PartInitException e)
			{
			}
		}

		return view;
	}


	// ----------------------------------------------------------
	public IExtraOptionsUpdater getExtraOptionsUpdater()
	{
		if(extraOptionsUpdater == null)
			extraOptionsUpdater = new ExtraOptionsUpdater();
		
		return extraOptionsUpdater;
	}


	// ----------------------------------------------------------
	public static ImageDescriptor getImageDescriptor(String relativePath)
	{
		try
		{
			return ImageDescriptor.createFromURL(new URL(Platform.getBundle(
			        PLUGIN_ID).getEntry("/icons/full/"), relativePath)); //$NON-NLS-1$
		}
		catch(MalformedURLException e)
		{
			// should not happen
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}


	//~ Static/instance variables .............................................
	
	/* The shared instance. */
	private static CxxTestPlugin plugin;

	/* Resource bundle. */
	private ResourceBundle resourceBundle;

	private MessageConsole builderConsole;
	
	private ExtraOptionsUpdater extraOptionsUpdater;
	
	public static final String PLUGIN_ID = "net.sf.webcat.eclipse.cxxtest"; //$NON-NLS-1$

	public static final String CXXTEST_NATURE = PLUGIN_ID + ".cxxtestNature"; //$NON-NLS-1$

	public static final String CXXTEST_BUILDER = PLUGIN_ID + ".cxxtestbuilder"; //$NON-NLS-1$

	public static final String CXXTEST_ENABLED = PLUGIN_ID + ".enabled"; //$NON-NLS-1$

	public static final String CXXTEST_RUNNER = PLUGIN_ID + ".cxxtestrunner"; //$NON-NLS-1$

	public static final String CXXTEST_PREF_DRIVER_FILENAME = PLUGIN_ID
	        + ".preferences.driver"; //$NON-NLS-1$

	public static final String CXXTEST_PREF_TRACK_HEAP = PLUGIN_ID
	        + ".preferences.trackHeap"; //$NON-NLS-1$

	public static final String CXXTEST_PREF_TRAP_SIGNALS = PLUGIN_ID
	        + ".preferences.trapSignals"; //$NON-NLS-1$

	public static final String CXXTEST_PREF_TRACE_STACK = PLUGIN_ID
	        + ".preferences.traceStack"; //$NON-NLS-1$
	
	public static final String CXXTEST_PREF_HAS_REQUIRED_LIBRARIES = PLUGIN_ID
			+ ".preferences.hasRequiredLibraries"; //$NON-NLS-1$

	public static final String CXXTEST_PREF_FIRST_TIME = PLUGIN_ID
			+ ".preferences.firstTime"; //$NON-NLS-1$
}
