/*==========================================================================*\
 |  $Id: DescribeBatchInputsPage.java,v 1.3 2012/01/05 20:01:44 stedwar2 Exp $
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

import java.io.File;
import org.webcat.core.MutableDictionary;
import org.webcat.core.ObjectQuery;
import org.webcat.core.WCComponent;
import org.webcat.core.objectquery.ObjectQuerySurrogate;
import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.eocontrol.EOEditingContext;

//-------------------------------------------------------------------------
/**
 * This page allows the user to select the items for the batch job and any
 * other arguments that the plug-in needs to run.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.3 $, $Date: 2012/01/05 20:01:44 $
 */
public class DescribeBatchInputsPage extends WCComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a new instance of the DescribeBatchInputsPage class.
     *
     * @param context the context
     */
    public DescribeBatchInputsPage(WOContext context)
    {
        super(context);
    }


    //~ KVC attributes (must be public) .......................................

    public BatchPlugin batchPlugin;
    public MutableDictionary configSettings;
    public File baseDir;

    public ObjectQuerySurrogate objectQuerySurrogate;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void appendToResponse(WOResponse response, WOContext context)
    {
        configSettings = new MutableDictionary();
        objectQuerySurrogate = new ObjectQuerySurrogate(batchPlugin.batchEntity());

        if (baseDir == null)
        {
            baseDir = new java.io.File(BatchPlugin.userPluginDirName(
                user(), true).toString());
        }

        super.appendToResponse(response, context);
    }


    // ----------------------------------------------------------
    public WOActionResults runBatchJob()
    {
        EOEditingContext ec = localContext();

        ObjectQuery query = objectQuerySurrogate.commitAndGetQuery(ec, user());
        applyLocalChanges();

        String desc = batchPlugin.displayableName();
        String extra = query.description();
        if (extra != null)
        {
            desc += ": " + extra;
        }

        BatchJob job = BatchJob.create(ec);

        job.setDescription(desc);
        job.setUserRelationship(user());
        job.setBatchPluginRelationship(batchPlugin);
        job.setCurrentState(BatchJob.STATE_START);
        job.setObjectQueryRelationship(query);

        if (!configSettings.isEmpty())
        {
            job.setConfigSettings(configSettings);
        }

        applyLocalChanges();

        // Create a BatchResult object to store the results of the operation.

        BatchResult result = BatchResult.create(localContext());
        result.setBatchPluginRelationship(batchPlugin);
        result.setDescription(desc);
        result.setUserRelationship(job.user());
        result.setObjectQueryRelationship(job.objectQuery());
        localContext().saveChanges();

        job.setBatchResultRelationship(result);
        applyLocalChanges();

        // Start the job.

        job.setIsReady(true);
        applyLocalChanges();

        // Load the batch result page.

        BatchResultPage page = pageWithName(BatchResultPage.class);
        page.result = result;

        return page;
    }
}
