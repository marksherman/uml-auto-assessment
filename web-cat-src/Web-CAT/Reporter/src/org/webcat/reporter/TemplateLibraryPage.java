/*==========================================================================*\
 |  $Id: TemplateLibraryPage.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
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

package org.webcat.reporter;

import com.webobjects.appserver.*;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSMutableDictionary;
import er.extensions.foundation.ERXValueUtilities;
import java.io.File;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.webcat.core.DeliverFile;
import org.webcat.core.FileUtilities;
import org.webcat.core.User;
import org.webcat.core.WCComponent;

//-------------------------------------------------------------------------
/**
 * Shows the list of available report templates and allows one to upload new
 * templates or manage existing ones.
 *
 * @author Tony Allevato
 * @version $Id: TemplateLibraryPage.java,v 1.1 2010/05/11 14:51:48 aallowat Exp $
 */
public class TemplateLibraryPage
    extends WCComponent
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create a new page.
     * @param context The page's context
     */
    public TemplateLibraryPage(WOContext context)
    {
        super(context);
    }


    //~ KVC Attributes (must be public) .......................................

    public int               index;
    public ReportTemplate    reportTemplate;
    public WODisplayGroup    publishedTemplateGroup;
    public WODisplayGroup    unpublishedTemplateGroup;
    public WODisplayGroup    personalTemplateGroup;
    public NSData            uploadedData;
    public String            uploadedName;

    public static final String TERSE_DESCRIPTIONS_KEY =
        "terseReportTemplateDescriptions";


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse( WOResponse response, WOContext context )
    {
        terse = null;
        publishedTemplateGroup.fetch();
        if ( user().hasAdminPrivileges() )
        {
            unpublishedTemplateGroup.fetch();
        }
        else
        {
            personalTemplateGroup.queryBindings().setObjectForKey(
                user(), "user"
            );
            personalTemplateGroup.fetch();
        }

        super.appendToResponse( response, context );
    }


    // ----------------------------------------------------------
    /**
     * Upload a new plug-in.
     * @return null to refresh the page
     */
    public WOComponent upload()
    {
        if ( uploadedName == null || uploadedData == null )
        {
            error( "Please select a file to upload." );
            return null;
        }

        ReportTemplate template = ReportTemplate.createNewReportTemplate(
            localContext(), user(), uploadedName, uploadedData, messages());

        if(template != null)
        {
            template.setRootTemplateRelationship(template);
            template.setVersion(VersionUtils.initialVersion());
            template.setChangeHistory("Initial version.");

            IRepositoryIdProvider idProvider =
                new DirectActionRepositoryIdProvider(context());

            template.updateRepositoryMetadataAndFinalize(idProvider);
        }

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
    public boolean canEditTemplate()
    {
        User user = user();
        return user.hasAdminPrivileges() || user == reportTemplate.user();
    }


    // ----------------------------------------------------------
    public WOComponent download()
    {
        File actualFile = new File(reportTemplate.filePath());
        String deliveredName = reportTemplate.name() + "_"
            + reportTemplate.version() + ReportTemplate.TEMPLATE_EXTENSION;

        DeliverFile myNextPage = pageWithName(DeliverFile.class);
        myNextPage.setFileName( actualFile );
        myNextPage.setDeliveredName( deliveredName );
        myNextPage.setContentType( FileUtilities.mimeType( actualFile ) );
        myNextPage.setStartDownload( !FileUtilities.showInline( actualFile ) );
        return myNextPage;
    }


    // ----------------------------------------------------------
    /**
     * Publish/unpublish a plug-in by toggling its isPublished attribute.
     * @return null to refresh the page
     */
    public WOComponent togglePublished()
    {
        reportTemplate.setIsPublished( !reportTemplate.isPublished() );
        applyLocalChanges();
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
            user().preferences().objectForKey( TERSE_DESCRIPTIONS_KEY ) );
        verboseDescriptions = !verboseDescriptions;
        user().preferences().setObjectForKey(
            Boolean.valueOf( verboseDescriptions ), TERSE_DESCRIPTIONS_KEY );
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
            terse = ERXValueUtilities.booleanValue(
                user().preferences().objectForKey( TERSE_DESCRIPTIONS_KEY ) )
                ? Boolean.TRUE : Boolean.FALSE;
        }
        return terse;
    }


    //~ Instance/static variables .............................................

    private Boolean terse;
    static Logger log = Logger.getLogger( TemplateLibraryPage.class );
}