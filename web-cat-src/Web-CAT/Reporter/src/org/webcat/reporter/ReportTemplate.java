/*==========================================================================*\
 |  $Id: ReportTemplate.java,v 1.3 2011/05/27 15:36:46 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2011 Virginia Tech
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

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import org.webcat.oda.commons.DataSetDescription;
import org.webcat.oda.commons.DataSetMetadata;
import org.webcat.oda.commons.ReportMetadata;
import org.apache.log4j.Logger;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.webcat.core.MutableArray;
import org.webcat.core.User;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSMutableSet;
import com.webobjects.foundation.NSSet;
import com.webobjects.foundation.NSTimestamp;
import er.extensions.foundation.ERXArrayUtilities;

// -------------------------------------------------------------------------
/**
 * Represents a BIRT report template and its associated metadata.
 *
 * @author Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.3 $, $Date: 2011/05/27 15:36:46 $
 */
public class ReportTemplate extends _ReportTemplate
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new ReportTemplate object.
     */
    public ReportTemplate()
    {
        super();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Retrieve the name of the directory where this script is stored.
     *
     * @return the directory name
     */
    public String dirName()
    {
        StringBuffer dir = userTemplateDirName(user());
        return dir.toString();
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the path name for this report template.
     *
     * @return the path to the template
     */
    public String filePath()
    {
        return dirName() + "/" + id().toString() + TEMPLATE_EXTENSION;
    }


    // ----------------------------------------------------------
    public String toString()
    {
        return filePath();
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the name of the directory where a user's report templates are
     * stored.
     *
     * @param author
     *            the user
     * @return the directory name
     */
    public static StringBuffer userTemplateDirName(User author)
    {
        StringBuffer dir = new StringBuffer(50);
        dir.append(templateRoot());
        if (author != null)
        {
            dir.append('/');
            dir.append(author.authenticationDomain().subdirName());
            dir.append('/');
            dir.append(author.userName());
        }
        return dir;
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the name of the directory where all user report templates are
     * stored.
     *
     * @return the directory name
     */
    public static String templateRoot()
    {
        if (templateRoot == null)
        {
            templateRoot = org.webcat.core.Application
                    .configurationProperties().getProperty(
                            "grader.reporttemplatesroot");

            if (templateRoot == null)
            {
                templateRoot = org.webcat.core.Application
                        .configurationProperties().getProperty(
                                "grader.submissiondir")
                        + "/UserReportTemplates";
            }
        }
        return templateRoot;
    }


    // ----------------------------------------------------------
    /**
     * Gets the report template that represents the next version of this
     * template.
     *
     * @return the next version of the report template
     */
    public ReportTemplate successorTemplate()
    {
        @SuppressWarnings("unchecked")
        NSArray<ReportTemplate> successors = (NSArray<ReportTemplate>)
            storedValueForKey("successorTemplateArray");

        if (successors == null || successors.count() == 0)
        {
            return null;
        }
        else
        {
            return successors.objectAtIndex(0);
        }
    }


    // ----------------------------------------------------------
    @Override
    public MutableArray parameters()
    {
        // Older reports will have a null parameter set, so to ensure clean
        // code elsewhere we just return an empty array for these.

        MutableArray params = super.parameters();

        if (params == null)
        {
            return new MutableArray();
        }
        else
        {
            return params;
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets a set that contains the visual design elements that are contained in
     * this report template.
     *
     * This is the preferred method to use to access this property, rather than
     * the raw string returned by {@link designElementsRaw}.
     *
     * @return an {@link NSSet} that contains the kinds of design elements in
     *         the report template
     */
    public NSSet<String> designElements()
    {
        NSMutableSet<String> set = new NSMutableSet<String>();

        String raw = designElementsRaw();

        if (raw != null)
        {
            String[] elements = designElementsRaw().split(" ");

            for (String element : elements)
            {
                set.addObject(element);
            }
        }

        return set;
    }


    // ----------------------------------------------------------
    /**
     * Sets the visual design elements that are contained in this report
     * template.
     *
     * This is the preferred method to use to access this property, rather than
     * passing a raw string into {@link setDesignElementsRaw}.
     *
     * @param set
     *            an {@link NSSet} that contains the kinds of design elements in
     *            the report template
     */
    public void setDesignElements(NSSet<String> set)
    {
        StringBuffer buffer = new StringBuffer(32);

        if (set.count() > 0)
        {
            boolean first = true;

            for (String element : set)
            {
                if (first)
                    first = false;
                else
                    buffer.append(' ');

                buffer.append(element);
            }
        }

        setDesignElementsRaw(buffer.toString());
    }


    // ----------------------------------------------------------
    /**
     * Create a new report template object from uploaded file data.
     *
     * @param ec
     *            the editing context in which to add the new object
     * @param owner
     *            the user uploading the template
     * @param uploadedName
     *            the template's file name
     * @param uploadedData
     *            the file's data
     * @param errors
     *            a dictionary in which to store any error messages for display
     *            to the user
     * @return the new report template, if successful, or null if unsuccessful
     */
    public static ReportTemplate createNewReportTemplate(EOEditingContext ec,
            User owner, String uploadedName, NSData uploadedData,
            NSMutableDictionary<?, ?> errors)
    {
        ReportTemplate template = new ReportTemplate();
        ec.insertObject(template);
        template.setName("");
        ec.saveChanges();

        template.setUploadedTime(new NSTimestamp());
        template.setUserRelationship(owner);

        // Save the file to disk
        log.debug("Saving report template to disk: " + template.filePath());
        File templateFile = new File(template.filePath());
        try
        {
            templateFile.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(templateFile);
            uploadedData.writeToStream(out);
            out.close();
        }
        catch (java.io.IOException e)
        {
            log.error("Error saving report template to disk:", e);
            String msg = e.getMessage();
            errors.setObjectForKey(msg, msg);
            ec.deleteObject(template);
            templateFile.delete();
            ec.saveChanges();
            return null;
        }

        // Migrate the report template to the new extension points if
        // needed.

        template.migrateTemplate();

        SessionHandle designSession = Reporter.getInstance().designSession();
        ReportDesignHandle reportHandle = null;

        try
        {
            reportHandle = designSession.openDesign(template.filePath());

            String msg = template.processMetadata(reportHandle);

            if (msg != null)
            {
                log.error("Error processing report template metadata: " + msg);
                errors.setObjectForKey(msg, msg);
                ec.deleteObject(template);
                templateFile.delete();
                ec.saveChanges();
                return null;
            }

            msg = template.visitReportParameters(reportHandle);
            if (msg != null)
            {
                log.error("Error processing report parameters: " + msg);
                errors.setObjectForKey(msg, msg);
                ec.deleteObject(template);
                templateFile.delete();
                ec.saveChanges();
                return null;
            }

            msg = template.deeplyVisitTemplate(ec, reportHandle);
            if (msg != null)
            {
                log.error("Error walking report template: " + msg);
                errors.setObjectForKey(msg, msg);
                ec.deleteObject(template);
                templateFile.delete();
                ec.saveChanges();
                return null;
            }

            // Save any changes that we have made to the report template up to
            // this point (mainly, the data set IDs in the query text).
            reportHandle.save();

            return template;
        }
        catch (Exception e)
        {
            log.error("Error opening report template:", e);
            String msg = "There was an internal error opening the report template: "
                    + e.toString();
            errors.setObjectForKey(msg, msg);
            ec.deleteObject(template);
            templateFile.delete();
            ec.saveChanges();
            return null;
        }
        finally
        {
            if (reportHandle != null)
            {
                reportHandle.close();
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * After a report template has been uploaded and all of its properties set,
     * call this method to update the repository metadata properties in the
     * template and then compute its checksum.
     *
     * @param idProvider
     *            the object used to generate repository IDs for this template
     */
    public void updateRepositoryMetadataAndFinalize(
            IRepositoryIdProvider idProvider)
    {
        SessionHandle designSession = Reporter.getInstance().designSession();
        ReportDesignHandle reportHandle = null;

        try
        {
            reportHandle = designSession.openDesign(filePath());

            DateFormat dateFmt = DateFormat.getDateTimeInstance(
                    DateFormat.MEDIUM, DateFormat.FULL);

            String repositoryId = idProvider.idForReportTemplate(this);
            String rootId = idProvider.idForReportTemplate(rootTemplate());

            ReportMetadata.setRepositoryId(reportHandle, repositoryId);
            ReportMetadata.setRepositoryRootId(reportHandle, rootId);
            ReportMetadata.setRepositoryVersion(reportHandle, version());
            ReportMetadata.setRepositoryUploadDate(reportHandle, dateFmt
                    .format(uploadedTime()));
            ReportMetadata.setRepositoryChangeHistory(reportHandle,
                    changeHistory());

            // Save any changes that we have made back to the file.
            reportHandle.save();
        }
        catch (Exception e)
        {
            log.error("Exception occurred trying to update report template "
                    + "metadata: ", e);
        }

        // Compute the MD5 checksum of the file contents. This must be the
        // last thing we do, because setting the repository properties
        // (and other modifications) will change the contents of the file.
        setChecksum(ChecksumUtils.checksumFromContentsOfFile(
            new File(filePath())));
    }


    // ----------------------------------------------------------
    /**
     * Performs any updates to the report template that might be necessary
     * after changes have been made to the Web-CAT server.
     */
    public void migrateTemplate()
    {
        // It would be nice if we could use the report designer APIs for this,
        // but it turns out trying to set the extensionID of a data source or
        // data set silently fails, so we have to use the caveman approach
        // instead.

        /*String templateContents =
            FileUtilities.stringWithContentsOfFile(filePath());

        if (templateContents != null)
        {
            Matcher matcher =
                packageNameMigrationPattern.matcher(templateContents);

            if (matcher.find())
            {
                templateContents = matcher.replaceAll("extensionID=\"org.webcat");

                log.info("Migrating template " + filePath()
                        + " to org.webcat.* package names");

                FileUtilities.writeStringToFile(templateContents, filePath());
            }
        }
        else
        {
            log.error("Could not load template file contents for migration: "
                    + filePath());
        }*/
    }


    // ----------------------------------------------------------
    /**
     * Gets the array of all report templates that are accessible by the
     * specified user. This is the union of that user's own uploaded templates
     * and all of the published templates.
     *
     * @param ec the editing context to load the templates into
     * @param forUser the user
     *
     * @return the array of report templates accessible by the user
     */
    public static NSArray<ReportTemplate> templatesAccessibleByUser(
            EOEditingContext ec, User forUser)
    {
        // Admins have access to everything, so just short-circuit this.

        if (forUser.hasAdminPrivileges())
        {
            return allTemplatesOrderedByName(ec);
        }

        NSArray<ReportTemplate> userTemplates = templatesForUser(ec, forUser);
        NSArray<ReportTemplate> publishedTemplates = publishedTemplates(ec);

        NSMutableArray<ReportTemplate> allTemplates =
            new NSMutableArray<ReportTemplate>();

        allTemplates.addObjectsFromArray(userTemplates);
        ERXArrayUtilities.addObjectsFromArrayWithoutDuplicates(allTemplates,
                publishedTemplates);

        ERXArrayUtilities.sortArrayWithKey(allTemplates,
                ReportTemplate.NAME_KEY);

        return allTemplates;
    }


    // ----------------------------------------------------------
    /**
     * Initializes various report template attributes in the EO model.
     */
    private String processMetadata(ReportDesignHandle reportHandle)
    {
        // Set the title and description of the report.
        String title = ReportMetadata.getTitle(reportHandle);
        @SuppressWarnings("hiding")
        String description = ReportMetadata.getDescription(reportHandle);

        if (title == null || title.trim().length() == 0)
        {
            String msg = "The report template you tried to upload does not "
                + "have a title.  Please enter one in the <b>Title</b> field "
                + "on the Overview page of the report in the report designer "
                + "and then upload it again.";
            return msg;
        }

        if (description == null || description.trim().length() == 0)
        {
            String msg = "The report template you tried to upload does not "
                + "have a description.  Please enter one in the "
                + "<b>Description</b> field on the Overview page of the "
                + "report in the report designer and then upload it again.";
            return msg;
        }

        setName(title);
        setDescription(description);

        // Set the language identifier of the template.
        @SuppressWarnings("hiding")
        String language = ReportMetadata.getLanguage(reportHandle);
        if (language == null)
        {
            language = "en";
        }

        setLanguage(language);

        // Set the preferred renderer of the template.
        String renderer = ReportMetadata.getPreferredRenderer(reportHandle);
        if (renderer == null)
        {
            renderer = "html";
        }

        setPreferredRenderer(renderer);

        return null;
    }


    // ----------------------------------------------------------
    private String visitReportParameters(ReportDesignHandle reportHandle)
    {
        ReportParameterVisitor visitor = new ReportParameterVisitor();
        visitor.apply(reportHandle);

        setParameters(new MutableArray(visitor.parameterGroups()));

        return null;
    }


    // ----------------------------------------------------------
    /**
     * Collects information about the report template by performing a deep
     * visitation of its layout.
     *
     * @param ec
     *            the editing context in which to work
     * @param reportHandle
     *            the BIRT report design handle
     *
     * @return a String indicating any errors that occurred, or null if it was
     *         successful
     */
    private String deeplyVisitTemplate(EOEditingContext ec,
            ReportDesignHandle reportHandle)
    {
        UploadedTemplateVisitor visitor = new UploadedTemplateVisitor();
        visitor.apply(reportHandle);

        NSDictionary<DataSetHandle, Integer> sets = visitor
                .dataSetsAndRefCounts();

        for (DataSetHandle dataSetHandle : sets.allKeys())
        {
            int refCount = sets.objectForKey(dataSetHandle);

            @SuppressWarnings("hiding")
            String name = DataSetMetadata.getName(dataSetHandle);
            @SuppressWarnings("hiding")
            String description = DataSetMetadata.getDescription(dataSetHandle);

            String queryText = dataSetHandle.getStringProperty("queryText");
            DataSetDescription relation = new DataSetDescription(queryText);

            ReportDataSet dataSet = ReportDataSet
                    .createNewReportDataSet(ec, this, relation.getEntityType(),
                            name, description, refCount);

            String realId = dataSet.id().toString();
            relation.setUniqueId(realId);

            try
            {
                dataSetHandle.setStringProperty("queryText", relation
                        .getQueryText());
            }
            catch (SemanticException e)
            {
                return "An exception occurred when manipulating the data set "
                        + "query text in the report template: "
                        + e.getMessage();
            }
        }

        // Set the design elements that were found by the visitor.
        setDesignElements(visitor.reportElements());

        return null;
    }


    //~ Instance/static variables .............................................

    public static final String TEMPLATE_EXTENSION = ".rptdesign";

    /*
     * Constants that define the valid "kinds" of report elements that are
     * catalogued when a report template is uploaded.
     */
    public static final String ELEMENT_TABLE = "table";
    public static final String ELEMENT_CHART = "chart";
    public static final String ELEMENT_CROSSTAB = "crosstab";

    static private String templateRoot = null;
    static Logger log = Logger.getLogger(ReportTemplate.class);
}
