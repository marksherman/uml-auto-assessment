/*==========================================================================*\
 |  $Id: SubsystemManagerPage.java,v 1.6 2011/05/19 16:57:01 stedwar2 Exp $
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

package org.webcat.admin;

import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;
import com.webobjects.foundation.*;
import er.extensions.foundation.ERXArrayUtilities;
import er.extensions.foundation.ERXValueUtilities;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import net.sf.webcat.FeatureDescriptor;
import net.sf.webcat.FeatureProvider;
import org.apache.log4j.Logger;
import org.webcat.core.*;

// -------------------------------------------------------------------------
/**
 *  The main "control panel" page for subsystems in the administration
 *  tab.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.6 $, $Date: 2011/05/19 16:57:01 $
 */
public class SubsystemManagerPage
    extends WCComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new page object.
     *
     * @param context The context to use
     */
    public SubsystemManagerPage(WOContext context)
    {
        super(context);
    }


    //~ KVC Attributes (must be public) .......................................

    public Subsystem                  subsystem;
    public NSArray<Subsystem>         subsystems;
    public NSArray<FeatureDescriptor> newSubsystems;
    public FeatureDescriptor          feature;
    public int                        index;
    public String                     providerURL;

    public static final String TERSE_DESCRIPTIONS_KEY =
        "terseSubsystemDescriptions";


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        terse = null;
        Application.wcApplication().subsystemManager()
            .refreshSubsystemDescriptorsAndProviders();
        subsystems = ERXArrayUtilities.sortedArraySortedWithKey(
            Application.wcApplication().subsystemManager().subsystems(),
            "name",
            EOSortOrdering.CompareCaseInsensitiveAscending);
        if (newSubsystems == null)
        {
            newSubsystems = ERXArrayUtilities.sortedArraySortedWithKey(
                newSubsystems(),
                "name",
                EOSortOrdering.CompareCaseInsensitiveAscending);
        }
        super.appendToResponse(response, context);
    }


    // ----------------------------------------------------------
    /**
     * Get the current servlet adaptor, if one is available.
     * @return the servlet adaptor, or null when none is available
     */
    public net.sf.webcat.WCServletAdaptor adaptor()
    {
        return net.sf.webcat.WCServletAdaptor.getInstance();
    }


    // ----------------------------------------------------------
    /**
     * Calculate the current set of subsystems that are available from
     * all registered providers, but that are not yet installed.  This
     * method assumes that the private <code>subsystems</code> data member
     * has already been initialized with a list of currently installed
     * subsystems.
     * @return an array of feature descriptors for available uninstalled
     *         subsystems
     */
    public NSArray<FeatureDescriptor> newSubsystems()
    {
        Collection<FeatureDescriptor> availableSubsystems =
            new HashSet<FeatureDescriptor>();
        for (FeatureProvider provider : FeatureProvider.providers())
        {
            if (provider != null)
            {
                availableSubsystems.addAll(provider.subsystems());
            }
        }
        for (Subsystem s : subsystems)
        {
            availableSubsystems.remove(s.descriptor().providerVersion());
        }
        FeatureDescriptor[] descriptors =
            new FeatureDescriptor[availableSubsystems.size()];
        return new NSArray<FeatureDescriptor>(
            availableSubsystems.toArray(descriptors));
    }


    // ----------------------------------------------------------
    /**
     * Determine if update download and installation support is active.
     * @return null to refresh the current page
     */
    public boolean canUpdate()
    {
        return adaptor() != null;
    }


    // ----------------------------------------------------------
    /**
     * Download the latest version of the current subsystem for updating
     * on restart.
     * @return null to refresh the current page
     */
    public WOComponent download()
    {
        String msg = subsystem.descriptor().providerVersion().downloadTo(
            adaptor().updateDownloadLocation());
        possibleErrorMessage(msg);
        if (msg == null)
        {
            confirmationMessage("The subsystem '" + subsystem.name()
                + "' has been downloaded from its provider.  The downloaded "
                + " version will replace the current version when "
                + "Web-CAT restarts.");
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Download a new subsystem for installation on restart.
     * @return null to refresh the current page
     */
    public WOComponent downloadNew()
    {
        String msg = feature.providerVersion().downloadTo(
            adaptor().updateDownloadLocation());
        possibleErrorMessage(msg);
        if (msg == null)
        {
            confirmationMessage("New subsystem '" + feature.name()
                + "' has been downloaded from its provider.  It will be "
                + " installed when Web-CAT restarts.");
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Scan the specified provider URL.
     * @return null to refresh the current page
     */
    public WOComponent scanNow()
    {
        if (providerURL == null || providerURL.equals(""))
        {
            error("Please specify a provider URL first.");
        }
        else
        {
            FeatureProvider provider = null;
            try
            {
                provider = FeatureProvider.getProvider(providerURL);
            }
            catch (IOException e)
            {
                // leave provider == null
            }
            if (provider == null)
            {
                warning("Cannot read feature provider information from "
                    + " specified URL: '" + providerURL + "'.");
            }
        }

        // Erase cache of new subsystems so it will be recalculated now
        newSubsystems = null;

        // refresh page
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Edit the selected subsystem's configuration settings.
     * @return the subsystem's edit page
     */
    public WOComponent edit()
    {
        ConfigureSubsystemPage page =
            pageWithName(ConfigureSubsystemPage.class);
        page.subsystem = subsystem;
        page.nextPage = this;
        return page;
    }


    // ----------------------------------------------------------
    /**
     * Toggle the
     * {@link net.sf.webcat.WCServletAdaptor#willUpdateAutomatically()}
     * attribute.
     * @return null to refresh the current page
     */
    public WOComponent toggleAutoUpdates()
    {
        net.sf.webcat.WCServletAdaptor adaptor = adaptor();
        adaptor.setWillUpdateAutomatically(
            !adaptor.willUpdateAutomatically());
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the history URL for the current subsystem.
     * @return The history URL, or null if none is defined
     */
    public String subsystemHistoryUrl()
    {
        return subsystem.descriptor().getProperty("history.url");
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the information URL for the current subsystem.
     * @return The information URL, or null if none is defined
     */
    public String subsystemInfoUrl()
    {
        return subsystem.descriptor().getProperty("info.url");
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the history URL for the current subsystem.
     * @return The history URL, or null if none is defined
     */
    public String featureHistoryUrl()
    {
        return feature.getProperty("history.url");
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the information URL for the current subsystem.
     * @return The information URL, or null if none is defined
     */
    public String featureInfoUrl()
    {
        return feature.getProperty("info.url");
    }


    // ----------------------------------------------------------
    /**
     * Toggle whether or not the user wants verbose descriptions of subsystems
     * to be shown or hidden.  The setting is stored in the user's preferences
     * under the key specified by the VERBOSE_DESCRIPTIONS_KEY, and will be
     * permanently saved the next time the session's local changes are saved.
     */
    public void toggleVerboseDescriptions()
    {
        boolean verboseOptions = ERXValueUtilities.booleanValue(
            user().preferences().objectForKey(TERSE_DESCRIPTIONS_KEY));
        verboseOptions = !verboseOptions;
        user().preferences().setObjectForKey(
            Boolean.valueOf(verboseOptions), TERSE_DESCRIPTIONS_KEY);
        user().savePreferences();
    }


    // ----------------------------------------------------------
    /**
     * Look up the user's preferences and determine whether or not to show
     * verbose subsystem descriptions in this component.
     * @return true if verbose descriptions should be hidden, or false if
     * they should be shown
     */
    public Boolean terse()
    {
        if (terse == null)
        {
            terse = ERXValueUtilities.booleanValue(
                user().preferences().objectForKey(TERSE_DESCRIPTIONS_KEY))
                    ? Boolean.TRUE : Boolean.FALSE;
        }
        return terse;
    }


    //~ Instance/static variables .............................................
    private Boolean terse;
    static Logger log = Logger.getLogger(SubsystemManagerPage.class);
}
