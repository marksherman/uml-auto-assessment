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
package net.sf.webcat.eclipse.cxxtest.framework;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class FrameworkPlugin extends Plugin {

	//The shared instance.
	private static FrameworkPlugin plugin;
	
	/**
	 * The constructor.
	 */
	public FrameworkPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static FrameworkPlugin getDefault() {
		return plugin;
	}

	public String getFrameworkPath()
	{
		String path = null;

		try
		{
			URL entry = FileLocator.find(getBundle(), new Path("/cxxtest"), null); //$NON-NLS-1$
			URL url = FileLocator.resolve(entry);
			path = url.getFile();

			// This special check is somewhat shady, but it looks like it's
			// the only way to handle a Windows path properly, since Eclipse
			// returns a string like "/C:/folder/...".
			if(path.charAt(2) == ':')
				path = path.substring(1);
			
			path = new Path(path).toOSString();
			if(path.charAt(path.length() - 1) == File.separatorChar)
				path = path.substring(0, path.length() - 1);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		return path;
	}
}