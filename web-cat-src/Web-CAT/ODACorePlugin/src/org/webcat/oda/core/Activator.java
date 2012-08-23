/*==========================================================================*\
 |  $Id: Activator.java,v 1.2 2010/09/20 14:17:35 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2008 Virginia Tech
 |
 |  This file is part of Web-CAT.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU Affero General Public License as published
 |  by the Free Software Foundation; either version 3 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU Affero General Public License
 |  along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.oda.core;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.webcat.oda.commons.IWebCATResultSetProvider;

// ------------------------------------------------------------------------
/**
 * The activator class controls the plug-in life cycle
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: Activator.java,v 1.2 2010/09/20 14:17:35 aallowat Exp $
 */
public class Activator extends Plugin
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Called when the plug-in is activated.
     */
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        plugin = this;

        emptyAppContextProvider = null;
    }


    // ----------------------------------------------------------
    /**
     * Called when the plug-in is deactivated.
     */
    public void stop(BundleContext context) throws Exception
    {
        plugin = null;
        super.stop(context);
    }


    // ----------------------------------------------------------
    /**
     * Returns the shared instance of the plug-in activator class.
     *
     * @return the shared instance
     */
    public static Activator getDefault()
    {
        return plugin;
    }


    // ----------------------------------------------------------
    /**
     * Returns the result set provider that should be used when the app context
     * is empty.
     *
     * @return the IWebCATResultSetProvider to use with empty app contexts
     * @throws CoreException
     *             if there was a problem reading the extension point
     *             information
     */
    public IWebCATResultSetProvider getResultSetProviderForEmptyAppContext()
            throws CoreException
    {
        if (emptyAppContextProvider == null)
        {
            IExtensionRegistry registry = Platform.getExtensionRegistry();
            IExtensionPoint extensionPoint = registry
                    .getExtensionPoint(PLUGIN_ID + ".emptyAppContextHandlers");
            IConfigurationElement[] elements = extensionPoint
                    .getConfigurationElements();

            for (int i = 0; i < elements.length; i++)
            {
                IConfigurationElement element = elements[i];
                Object executable = element.createExecutableExtension("class");

                if (executable instanceof IWebCATResultSetProvider)
                {
                    emptyAppContextProvider = (IWebCATResultSetProvider) executable;
                    break;
                }
            }
        }

        return emptyAppContextProvider;
    }


    // ----------------------------------------------------------
    /**
     * Clears the result set provider used by the plug-in, forcing it to be
     * reinstantiated the next time it is requested.
     */
    public void refreshResultSetProviderForEmptyAppContext()
    {
        emptyAppContextProvider = null;
    }


    //~ Static/Instance Variables .............................................

    /**
     * The unique identifier of the plug-in.
     */
    public static final String PLUGIN_ID = "net.sf.webcat.oda.core";

    /**
     * The singleton instance of the plug-in activator class.
     */
    private static Activator plugin;

    /**
     * The result set provider that should be used in cases where the app
     * context is empty (that is, when previewing a report).
     */
    private IWebCATResultSetProvider emptyAppContextProvider;
}
