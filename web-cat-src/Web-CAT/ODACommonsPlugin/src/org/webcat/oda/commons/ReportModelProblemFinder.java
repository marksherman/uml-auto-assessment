/*==========================================================================*\
 |  $Id: ReportModelProblemFinder.java,v 1.1 2010/05/11 15:52:50 aallowat Exp $
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

package org.webcat.oda.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;

// ------------------------------------------------------------------------
/**
 * Detects problems in a report template that will cause Web-CAT to complain so
 * that the user has an opportunity to fix them before uploading the template.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: ReportModelProblemFinder.java,v 1.1 2010/05/11 15:52:50 aallowat Exp $
 */
public class ReportModelProblemFinder
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new report problem finder and gathers the problems that exist
     * in the report.
     *
     * @param module
     *            the report template handle to search for problems
     */
    public ReportModelProblemFinder(ModuleHandle module)
    {
        this.module = module;

        problems = new ArrayList<ReportModelProblem>();

        findMetadataProblems();
        findDataSetProblems();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets a value indicating whether any problems were found in the report
     * template.
     *
     * @return true if there were problems, otherwise false.
     */
    public boolean hasProblems()
    {
        return problems.size() > 0;
    }


    // ----------------------------------------------------------
    /**
     * Gets the highest severity among all of the problems that were found.
     *
     * @return the highest severity among all of the problems
     */
    public int getSeverityOfWorstProblem()
    {
        int maxSeverity = ReportModelProblem.SEVERITY_OK;

        for (ReportModelProblem problem : problems)
        {
            if (problem.getSeverity() > maxSeverity)
            {
                maxSeverity = problem.getSeverity();
            }
        }

        return maxSeverity;
    }


    // ----------------------------------------------------------
    /**
     * Gets the problems that were found in the report template.
     *
     * @return an array of ReportProblem objects describing the problems.
     */
    public ReportModelProblem[] getProblems()
    {
        ReportModelProblem[] problemArray = new ReportModelProblem[problems
                .size()];
        problems.toArray(problemArray);
        return problemArray;
    }


    // ----------------------------------------------------------
    /**
     * Finds problems related to Web-CAT metadata, such as the lack of a title
     * or description.
     */
    private void findMetadataProblems()
    {
        // Check for empty title.
        if (ReportMetadata.getTitle(module) == null)
        {
            addProblem(module, KEY_NO_TITLE, ReportModelProblem.SEVERITY_ERROR,
                    MSG_NO_TITLE);
        }

        // Check for empty description.
        if (ReportMetadata.getDescription(module) == null)
        {
            addProblem(module, KEY_NO_DESCRIPTION,
                    ReportModelProblem.SEVERITY_ERROR, MSG_NO_DESCRIPTION);
        }

        // Check that at least one author has been provided.
        int authorCount = ReportMetadata.getAuthorsCount(module);

        if (authorCount == 0)
        {
            addProblem(module, KEY_NO_AUTHORS,
                    ReportModelProblem.SEVERITY_ERROR, MSG_NO_AUTHORS);
        }

        // Check that every author has a name.
        for (int i = 0; i < authorCount; i++)
        {
            if (ReportMetadata.getAuthorName(module, i) == null)
            {
                String key = String.format(KEY_AUTHOR_NO_NAME_WITH_DETAIL, i);

                addProblem(module, key, ReportModelProblem.SEVERITY_ERROR,
                        MSG_AUTHOR_NO_NAME, i);
            }
        }

        // Check that a copyright statement has been provided.
        if (ReportMetadata.getCopyright(module) == null)
        {
            addProblem(module, KEY_NO_COPYRIGHT,
                    ReportModelProblem.SEVERITY_WARNING, MSG_NO_COPYRIGHT);
        }

        // Check that a license has been provided.
        if (ReportMetadata.getLicense(module) == null)
        {
            addProblem(module, KEY_NO_LICENSE,
                    ReportModelProblem.SEVERITY_WARNING, MSG_NO_LICENSE);
        }
    }


    // ----------------------------------------------------------
    /**
     * Finds problems related to data sets, such as missing descriptions.
     */
    private void findDataSetProblems()
    {
        Iterator<?> it = module.getDataSets().iterator();

        // Check that every Web-CAT data set has a description in its comments
        // section.
        while (it.hasNext())
        {
            DataSetHandle dataSet = (DataSetHandle) it.next();

            if (DataSetMetadata.isWebCATDataSet(dataSet))
            {
                String name = DataSetMetadata.getName(dataSet);
                String description = DataSetMetadata.getDescription(dataSet);

                if (description == null)
                {
                    String key = String.format(
                            KEY_DATASET_NO_DESCRIPTION_WITH_DETAIL, name);

                    addProblem(dataSet, key,
                            ReportModelProblem.SEVERITY_WARNING,
                            MSG_DATASET_NO_DESCRIPTION, name);
                }
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Utility method to add a problem with a formatted description to the
     * problem list.
     *
     * @param handle
     *            the design element with the problem
     * @param key
     *            the key that uniquely identifies the problem
     * @param severity
     *            the severity of the problem
     * @param format
     *            the format string for the description
     * @param params
     *            arguments to the format string
     */
    private void addProblem(DesignElementHandle handle, String key,
            int severity, String format, Object... params)
    {
        ReportModelProblem problem = new ReportModelProblem(handle, key,
                severity, String.format(format, params));

        problems.add(problem);
    }


    // ----------------------------------------------------------
    /**
     * Utility method to access the main part of a key, without the detail part.
     * Use this method instead of manipulating the string directly in case the
     * key format changes in a later version.
     *
     * @param key
     *            the unique key
     * @return the main part of the key
     */
    public static String getKeyWithoutDetail(String key)
    {
        int colon = key.indexOf(':');

        if (colon == -1)
        {
            return key;
        }
        else
        {
            return key.substring(0, colon);
        }
    }


    // ----------------------------------------------------------
    /**
     * Utility method to access the detail value from a key, if it has one. Use
     * this method instead of manipulating the string directly in case the key
     * format changes in a later version.
     *
     * @param key
     *            the unique key
     * @return the detail part attached to the key, or null if there was none
     */
    public static String getKeyDetail(String key)
    {
        int colon = key.indexOf(':');

        if (colon == -1)
        {
            return null;
        }
        else
        {
            return key.substring(colon + 1);
        }
    }


    //~ Static/instance variables .............................................

    private ModuleHandle module;
    private List<ReportModelProblem> problems;

    /*
     * Problem key identifiers for each of the problems that this class detects
     * in the report template model.
     */

    public static final String KEY_NO_TITLE = "noTitle";
    public static final String KEY_NO_DESCRIPTION = "noDescription";
    public static final String KEY_NO_AUTHORS = "noAuthors";
    public static final String KEY_NO_COPYRIGHT = "noCopyright";
    public static final String KEY_NO_LICENSE = "noLicense";

    public static final String KEY_DATASET_NO_DESCRIPTION = "dataSet.noDescription";
    private static final String KEY_DATASET_NO_DESCRIPTION_WITH_DETAIL = KEY_DATASET_NO_DESCRIPTION
            + ":%s";

    public static final String KEY_AUTHOR_NO_NAME = "author.noName";
    private static final String KEY_AUTHOR_NO_NAME_WITH_DETAIL = KEY_AUTHOR_NO_NAME
            + ":%d";

    /*
     * Description format strings for each of the problems that this class
     * detects in the report template model.
     */

    private static final String MSG_NO_TITLE = "The report template does not "
            + "have a title.";

    private static final String MSG_NO_DESCRIPTION = "The report template "
            + "does not have a description.";

    private static final String MSG_NO_AUTHORS = "The report template does "
            + "not list any authors.";

    private static final String MSG_AUTHOR_NO_NAME = "Author #%d does not "
            + "have a name.";

    private static final String MSG_NO_COPYRIGHT = "You have not provided a "
            + "copyright statement. A default copyright will be made from the "
            + "current year and institution of the first author.";

    private static final String MSG_NO_LICENSE = "You have not provided a "
            + "license. A default license of \"All rights reserved by the "
            + "copyright holder\" will be used.";

    private static final String MSG_DATASET_NO_DESCRIPTION = "The Web-CAT "
            + "data set \"%s\" does not have a description.";
}
