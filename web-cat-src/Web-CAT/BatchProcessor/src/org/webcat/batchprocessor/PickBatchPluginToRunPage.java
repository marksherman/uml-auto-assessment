/*==========================================================================*\
 |  $Id: PickBatchPluginToRunPage.java,v 1.5 2012/01/29 18:24:19 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2010-2012 Virginia Tech
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

package org.webcat.batchprocessor;

import java.util.Collection;
import java.util.HashSet;
import net.sf.webcat.FeatureDescriptor;
import net.sf.webcat.FeatureProvider;
import org.webcat.core.User;
import org.webcat.core.WCComponent;
import org.webcat.ui.generators.JavascriptGenerator;
import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSMutableDictionary;
import er.extensions.appserver.ERXDisplayGroup;
import er.extensions.eof.ERXKey;
import er.extensions.foundation.ERXArrayUtilities;

//-------------------------------------------------------------------------
/**
 * This page allows the user to select the batch plug-in that they want to run.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.5 $, $Date: 2012/01/29 18:24:19 $
 */
public class PickBatchPluginToRunPage
    extends WCComponent
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create a new page.
     * @param context The page's context
     */
    public PickBatchPluginToRunPage(WOContext context)
    {
        super(context);
    }


    //~ KVC Attributes (must be public) .......................................

    public ERXDisplayGroup<BatchPlugin> pluginsDisplayGroup;
    public BatchPlugin batchPlugin;
    public ERXDisplayGroup<BatchResult> batchResultDisplayGroup;
    public BatchResult                  batchResult;
    public ERXDisplayGroup<BatchJob>    batchJobDisplayGroup;
    public BatchJob                     batchJob;

    public NSArray<FeatureDescriptor>   newPlugins;
    public FeatureDescriptor            feature;

    public NSData                       uploadedData;
    public String                       uploadedName;
    public String                       providerURL;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        NSMutableDictionary<?, ?> bindings;

        bindings = batchResultDisplayGroup.queryBindings();
        bindings.setObjectForKey(user(), "user");
        batchResultDisplayGroup.fetch();

        bindings = batchJobDisplayGroup.queryBindings();
        bindings.setObjectForKey(user(), "user");
        batchJobDisplayGroup.fetch();

        if (pluginsDisplayGroup == null)
        {
            pluginsDisplayGroup = new ERXDisplayGroup<BatchPlugin>();
            pluginsDisplayGroup.setSortOrderings(
                new ERXKey<String>("displayableName").ascs());
        }
        pluginsDisplayGroup.setObjectArray(
            BatchPlugin.pluginsAccessibleByUser(localContext(), user()));

        if ( newPlugins == null )
        {
            for (FeatureProvider fp : FeatureProvider.providers())
            {
                fp.refresh();
            }
            newPlugins = newPlugins();
        }

        super.appendToResponse(response, context);
    }


    // ----------------------------------------------------------
    public WOComponent pluginChosen()
    {
        DescribeBatchInputsPage page =
            pageWithName(DescribeBatchInputsPage.class);

        page.batchPlugin = batchPlugin;

        return page;
    }


    // ----------------------------------------------------------
    public WOComponent viewBatchResult()
    {
        if (batchResult != null)
        {
            BatchResultPage page = pageWithName(BatchResultPage.class);
            page.result = batchResult;
            return page;
        }
        else
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    public WOActionResults deleteSelectedResults()
    {
        NSArray<BatchResult> results =
            batchResultDisplayGroup.selectedObjects();

        for (BatchResult result : results)
        {
            localContext().deleteObject(result);
        }

        localContext().saveChanges();

        batchResultDisplayGroup.clearSelection();
        batchResultDisplayGroup.fetch();

        return new JavascriptGenerator().refresh("batchResultContainer");
    }


    // ----------------------------------------------------------
    public WOComponent viewBatchProgress()
    {
        BatchResult result = batchJob.batchResult();

        if (result != null)
        {
            BatchResultPage page = pageWithName(BatchResultPage.class);
            page.result = result;
            return page;
        }
        else
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    /**
     * Upload a new plug-in.
     * @return null to refresh the page
     */
    public WOComponent upload()
    {
        if (uploadedName == null || uploadedData == null)
        {
            error("Please select a file to upload.");
            return null;
        }

        BatchPlugin.createNewBatchPlugin(
            localContext(), user(), uploadedName, uploadedData,
            true, messages());

        applyLocalChanges();
        uploadedName = null;
        uploadedData = null;
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Determine whether this user has permissions to edit the current template.
     * @return true if the current template can be edited by the current user
     */
    public boolean canEditPlugin()
    {
        User user = user();
        return user.hasAdminPrivileges() || user == batchPlugin.author();
    }


    // ----------------------------------------------------------
    /**
     * Edit the selected plug-in's configuration settings.
     * @return the subsystem's edit page
     */
    public WOComponent editGlobalSettings()
    {
        EditBatchPluginGlobalsPage newPage =
            pageWithName(EditBatchPluginGlobalsPage.class);
        newPage.nextPage = this;
        newPage.plugin = batchPlugin;
        return newPage;
    }


    // ----------------------------------------------------------
    /**
     * Browse or edit the selected plug-in's files.  Administrators can
     * edit all plug-ins.  Otherwise, users can only edit plug-ins they
     * have authored, and can only browse others.
     * @return the subsystem's edit page
     */
    public WOComponent editFiles()
    {
        EditBatchPluginFilesPage newPage = pageWithName(
                EditBatchPluginFilesPage.class);
        newPage.nextPage = this;
        newPage.batchPlugin = batchPlugin;
        newPage.hideNextAndBack(true);
        newPage.isEditable = user().hasAdminPrivileges() ||
            user().equals(batchPlugin.author());
        return newPage;
    }


    // ----------------------------------------------------------
    public WOComponent download()
    {
/*        File actualFile = new File(batchPlugin.filePath());
        String deliveredName = batchPlugin.name() + "_"
            + batchPlugin.version() + BatchPlugin.TEMPLATE_EXTENSION;

        DeliverFile myNextPage = pageWithName(DeliverFile.class);
        myNextPage.setFileName( actualFile );
        myNextPage.setDeliveredName( deliveredName );
        myNextPage.setContentType( FileUtilities.mimeType( actualFile ) );
        myNextPage.setStartDownload( !FileUtilities.showInline( actualFile ) );
        return myNextPage;*/

        // TODO fix
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Publish/unpublish a plug-in by toggling its isPublished attribute.
     * @return null to refresh the page
     */
    public WOComponent togglePublished()
    {
        batchPlugin.setIsPublished(!batchPlugin.isPublished());
        applyLocalChanges();
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Force a fresh reload of the plug-in's config.plist file to pick up
     * any changes (i.e., new attributes, new default values, etc.).
     * @return null, to force this page to reload in the browser when the
     *         action completes
     */
    public WOComponent reloadPluginDefinition()
    {
        String errMsg = batchPlugin.initializeConfigAttributes();

        if (errMsg != null)
        {
            cancelLocalChanges();
            error(errMsg);
        }
        else
        {
            if (applyLocalChanges())
            {
                confirmationMessage("Configuration definition for plug-in '"
                    + batchPlugin.name() + "' has been reloaded.");
            }
        }

        return null;
    }


    // ----------------------------------------------------------
    /**
     * Calculate the current set of plug-ins that are available from
     * all registered providers, but that are not yet installed.
     * @return an array of feature descriptors for available uninstalled
     *         plug-ins
     */
    public NSArray<FeatureDescriptor> newPlugins()
    {
        Collection<FeatureDescriptor> availablePlugins =
            new HashSet<FeatureDescriptor>();
        for (FeatureProvider provider : FeatureProvider.providers())
        {
            if ( provider != null )
            {
                for (FeatureDescriptor aPlugin : provider.plugins())
                {
                    // Screen out batch plug-ins
                    if (aPlugin.getProperty("batchEntity") != null)
                    {
                        availablePlugins.add(aPlugin);
                    }
                }
            }
        }
        NSArray<BatchPlugin> exclude = BatchPlugin.allObjects(localContext());
        if (exclude != null)
        {
            for (BatchPlugin s : exclude)
            {
                FeatureDescriptor fd = s.descriptor().providerVersion();
                if (fd != null)
                {
                    availablePlugins.remove(fd);
                }
            }
        }
        FeatureDescriptor[] descriptors =
            new FeatureDescriptor[availablePlugins.size()];
        return ERXArrayUtilities.sortedArraySortedWithKey(
            new NSArray<FeatureDescriptor>(
                availablePlugins.toArray(descriptors)),
            "name");
    }


    // ----------------------------------------------------------
    /**
     * Scan the specified provider URL.
     * @return null to refresh the current page
     */
    public WOComponent scanNow()
    {
        if ( providerURL == null || providerURL.equals( "" ) )
        {
            error( "Please specify a provider URL first." );
        }
        else
        {
            try
            {
                // TODO: fix this to correctly re-load ...
                FeatureProvider.getProvider(providerURL);
            }
            catch (java.io.IOException e)
            {
                error("Cannot read feature provider information from "
                    + " specified URL: '" + providerURL + "'.");
            }
        }

        // Erase cache of new subsystems so it will be recalculated now
        newPlugins = null;

        // refresh page
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Download and install a new plug-in.
     * @return null to refresh the current page
     */
    public WOComponent downloadNew()
    {
        String msg =
            BatchPlugin.installOrUpdate( user(), feature, false );
        possibleErrorMessage( msg );
        if ( msg == null )
        {
            if (applyLocalChanges())
            {
                confirmationMessage( "New plug-in '" + feature.name()
                    + "' has been downloaded and installed." );
            }
        }
        else
        {
            cancelLocalChanges();
        }
        newPlugins = null;
        return null;
    }
}
