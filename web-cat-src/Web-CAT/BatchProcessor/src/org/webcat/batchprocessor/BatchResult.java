/*==========================================================================*\
 |  $Id: BatchResult.java,v 1.3 2012/02/05 21:59:53 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
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
import org.webcat.core.Application;
import org.webcat.core.User;
import org.webcat.core.WCProperties;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOObjectStore;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSComparator;
import com.webobjects.foundation.NSMutableArray;

// -------------------------------------------------------------------------
/**
 * TODO: place a real description here.
 *
 * @author  Tony Allevato
 * @author  latest changes by: $Author: stedwar2 $
 * @version $Revision: 1.3 $, $Date: 2012/02/05 21:59:53 $
 */
public class BatchResult
    extends _BatchResult
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new BatchResult object.
     */
    public BatchResult()
    {
        super();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * A convenience method to create and insert a new BatchResult with default
     * values.
     *
     * @param editingContext the editing context
     * @return a new BatchResult
     */
    public static BatchResult create(EOEditingContext editingContext)
    {
        return create(editingContext, false);
    }


    // ----------------------------------------------------------
    /**
     * A helper method to get the single BatchJob associated with this result,
     * or null if there are not any jobs.
     *
     * @return BatchResult the BatchJob associated with the job
     */
    public BatchJob batchJob()
    {
        NSArray<BatchJob> jobs = batchJobs();

        if (jobs.count() == 0)
        {
            return null;
        }
        else
        {
            if (jobs.count() > 1)
            {
                log.warn("More than one BatchJob instance was found for "
                        + "this result!");
            }

            return jobs.objectAtIndex(0);
        }
    }


    // ----------------------------------------------------------
    /**
     * Provided as a KVC-mirror to {@link #batchJob()}.  It simply calls
     * {@link #addToBatchJobsRelationship(BatchJob)}.
     * @param job The job to add.
     */
    public void setBatchJob(BatchJob job)
    {
        addToBatchJobsRelationship(job);
    }


    // ----------------------------------------------------------
    public String resultDirName()
    {
        StringBuffer dir = new StringBuffer(50);
        dir.append(Application.configurationProperties().getProperty(
                "grader.submissiondir"));
        dir.append('/');
        dir.append(user().authenticationDomain().subdirName());
        dir.append('/');
        dir.append("BatchResults");
        dir.append('/');
        dir.append(user().userName());
        dir.append('/');
        dir.append(id().toString());

        return dir.toString();
    }


    // ----------------------------------------------------------
    public File resultDir()
    {
        return new File(resultDirName());
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the properties file as a File object.
     *
     * @return the file for this submission
     */
    public File propertiesFile()
    {
        if (propertiesFile == null)
        {
            propertiesFile = new File(resultDirName(), propertiesFileName());
        }

        return propertiesFile;
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the base file name for the result properties file.
     *
     * @return the base file name
     */
    public static String propertiesFileName()
    {
        return "batch.properties";
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the properties object for this submission result.
     * @return the properties object attached to the properties file
     */
    public WCProperties properties()
    {
        if (properties == null)
        {
            properties = new WCProperties(
                    resultDirName() + "/" + propertiesFileName(),
                    null);
        }

        return properties;
    }


    // ----------------------------------------------------------
    /**
     * Gets the array of feedback sections that are visible to the specified
     * user, sorted respecting the order and location properties of the
     * sections.
     *
     * @param viewingUser the user
     * @return the array of feedback sections
     */
    public NSArray<BatchFeedbackSection> sortedVisibleFeedbackSections(
            User viewingUser)
    {
        NSArray<BatchFeedbackSection> sections = feedbackSections();

        if (sections == null)
        {
            return null;
        }

        NSMutableArray<BatchFeedbackSection> filtered =
            new NSMutableArray<BatchFeedbackSection>();

        for (BatchFeedbackSection section : sections)
        {
            boolean visible = false;

            switch (section.recipients())
            {
            case ALL:
                visible = true;
                break;

            case OWNER:
                visible = (viewingUser == user());
                break;

            case ADMINISTRATOR:
                visible = (viewingUser.accessLevel() >= User.WEBCAT_READ_PRIVILEGES);
                break;
            }

            if (visible)
            {
                filtered.addObject(section);
            }
        }

        try
        {
            BatchFeedbackSection.sortSectionsUsingOrder(filtered);
        }
        catch (NSComparator.ComparisonException e)
        {
            log.warn("Error sorting the batch feedback sections", e);
        }

        return filtered;
    }


    // ----------------------------------------------------------
    @Override
    public void mightDelete()
    {
        log.debug("mightDelete()");
        if (isNewObject()) return;

        resultDirToDelete = new File(this.resultDirName());

        super.mightDelete();
    }


    // ----------------------------------------------------------
    @Override
    public void didDelete( EOEditingContext context )
    {
        log.debug("didDelete()");
        super.didDelete( context );

        // should check to see if this is a child ec
        EOObjectStore parent = context.parentObjectStore();
        if (parent == null || !(parent instanceof EOEditingContext))
        {
            if (resultDirToDelete != null)
            {
                if (resultDirToDelete.exists())
                {
                    org.webcat.core.FileUtilities.deleteDirectory(
                            resultDirToDelete);
                }
            }
        }
    }


    //~ Static/instance variables .............................................

    private File propertiesFile;
    private WCProperties properties;

    private File resultDirToDelete;
}
