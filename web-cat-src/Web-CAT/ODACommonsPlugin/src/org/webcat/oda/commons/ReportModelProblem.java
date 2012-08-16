/*==========================================================================*\
 |  $Id: ReportModelProblem.java,v 1.1 2010/05/11 15:52:50 aallowat Exp $
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

import org.eclipse.birt.report.model.api.DesignElementHandle;

// ------------------------------------------------------------------------
/**
 * Represents a problem discovered within the report template.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: ReportModelProblem.java,v 1.1 2010/05/11 15:52:50 aallowat Exp $
 */
public class ReportModelProblem
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new report model problem object.
     *
     * @param handle
     *            the handle of the object in the model that has the problem
     * @param key
     *            a unique key that identifies the cause of the problem
     * @param severity
     *            the severity of the problem, defined by one of the SEVERITY_*
     *            constants
     * @param description
     *            a text description of the problem
     */
    public ReportModelProblem(DesignElementHandle handle, String key,
            int severity, String description)
    {
        this.handle = handle;
        this.key = key;
        this.severity = severity;
        this.description = description;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the handle of the design element in the model that this problem is
     * related to.
     *
     * @return the design element handle
     */
    public DesignElementHandle getHandle()
    {
        return handle;
    }


    // ----------------------------------------------------------
    /**
     * Gets the key that uniquely identifies the problem.
     *
     * @return the unique key that identifies the problem
     */
    public String getKey()
    {
        return key;
    }


    // ----------------------------------------------------------
    /**
     * Gets the severity of the problem.
     *
     * @return the problem severity
     */
    public int getSeverity()
    {
        return severity;
    }


    // ----------------------------------------------------------
    /**
     * Gets the description of the problem.
     *
     * @return the problem description
     */
    public String getDescription()
    {
        return description;
    }


    //~ Static/instance variables .............................................

    /**
     * The problem is a simple informative message that does not require user
     * intervention.
     */
    public static final int SEVERITY_OK = 0;

    /**
     * The problem is one which will not prevent the report template from being
     * used, but will cause the full functionality of Web-CAT to be reduced in
     * some way or cause the user interface to be less clear or informative for
     * the user.
     */
    public static final int SEVERITY_WARNING = 1;

    /**
     * The problem is serious enough that Web-CAT will not accept the report
     * template until it is fixed.
     */
    public static final int SEVERITY_ERROR = 2;

    /** The handle of the related object in the report model. */
    private DesignElementHandle handle;

    /** The unique identifier of the problem. */
    private String key;

    /** The severity of the problem. */
    private int severity;

    /** The description of the problem. */
    private String description;
}
