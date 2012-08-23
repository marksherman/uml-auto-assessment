/*==========================================================================*\
 |  $Id: BatchPluginLibraryPage.java,v 1.4 2011/12/06 18:08:28 stedwar2 Exp $
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

package org.webcat.batchprocessor;

import com.webobjects.appserver.*;
import com.webobjects.foundation.NSData;
import er.extensions.foundation.ERXValueUtilities;
import org.apache.log4j.Logger;
import org.webcat.core.User;
import org.webcat.core.WCComponent;

//-------------------------------------------------------------------------
/**
 * Shows the list of available report templates and allows one to upload new
 * templates or manage existing ones.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.4 $, $Date: 2011/12/06 18:08:28 $
 */
public class BatchPluginLibraryPage
    extends WCComponent
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create a new page.
     * @param context The page's context
     */
    public BatchPluginLibraryPage(WOContext context)
    {
        super(context);
    }


    //~ KVC Attributes (must be public) .......................................

    public int               index;
    public BatchPlugin       batchPlugin;
    public WODisplayGroup    publishedPluginsGroup;
    public WODisplayGroup    unpublishedPluginsGroup;
    public WODisplayGroup    personalPluginsGroup;
    public NSData            uploadedData;
    public String            uploadedName;

    public static final String TERSE_DESCRIPTIONS_KEY =
        "terseBatchPluginDescriptions";


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        terse = null;
        publishedPluginsGroup.fetch();
        if (user().hasAdminPrivileges())
        {
            unpublishedPluginsGroup.fetch();
        }
        else
        {
            personalPluginsGroup.queryBindings().setObjectForKey(
                user(), "author");
            personalPluginsGroup.fetch();
        }

        super.appendToResponse(response, context);
    }


    // ----------------------------------------------------------
    /**
     * Determine if there is a download site.
     * @return true if a download url is defined
     */
    public boolean canDownload()
    {
        return batchPlugin.descriptor().getProperty("provider.url") != null;
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
     * Toggle whether or not the user wants verbose descriptions of report
     * templates to be shown or hidden.  The setting is stored in the user's
     * preferences under the key specified by the TERSE_DESCRIPTIONS_KEY, and
     * will be permanently saved the next time the user's local changes are
     * saved.
     */
    public void toggleVerboseDescriptions()
    {
        boolean verboseDescriptions = ERXValueUtilities.booleanValue(
            user().preferences().objectForKey(TERSE_DESCRIPTIONS_KEY));
        verboseDescriptions = !verboseDescriptions;
        user().preferences().setObjectForKey(
            Boolean.valueOf(verboseDescriptions), TERSE_DESCRIPTIONS_KEY);
        user().savePreferences();
    }


    // ----------------------------------------------------------
    /**
     * Look up the user's preferences and determine whether or not to show
     * verbose report template descriptions in this component.
     * @return true if verbose descriptions should be hidden, or false if
     * they should be shown
     */
    public Boolean terse()
    {
        if ( terse == null )
        {
            terse = Boolean.valueOf(ERXValueUtilities.booleanValue(
                user().preferences().objectForKey(TERSE_DESCRIPTIONS_KEY)));
        }
        return terse;
    }


    //~ Instance/static variables .............................................

    private Boolean terse;
    static Logger log = Logger.getLogger(BatchPluginLibraryPage.class);
}
