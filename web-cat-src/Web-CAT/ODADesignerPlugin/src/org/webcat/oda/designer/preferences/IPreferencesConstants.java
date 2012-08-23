/*==========================================================================*\
 |  $Id: IPreferencesConstants.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

package org.webcat.oda.designer.preferences;

// ------------------------------------------------------------------------
/**
 * This interface provides definitions for the names of keys that are used to
 * store preferences information for the Web-CAT Data Source plug-in.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: IPreferencesConstants.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public interface IPreferencesConstants
{
    //~ Static variables ......................................................

    /**
     * The URL of the Web-CAT server that will be used to obtain data in a
     * report preview. This URL should start with the "http://" prefix and end
     * with the "WebCAT.woa" portion of the server address.
     */
    static final String SERVER_URL_KEY = "serverURL"; //$NON-NLS-1$

    /**
     * The user name that should be used to connect to the Web-CAT server.
     */
    static final String USERNAME_KEY = "username"; //$NON-NLS-1$

    /**
     * The password that should be used to connect to the Web-CAT server.
     */
    static final String PASSWORD_KEY = "password"; //$NON-NLS-1$

    /**
     * The maximum number of records to retrieve from the Web-CAT server during
     * a preview operation.
     */
    static final String MAX_RECORDS_KEY = "maxRecords"; //$NON-NLS-1$

    /**
     * The maximum amount of time, in seconds, to use to obtain preview data for
     * a single data set. If this time elapses, only those rows retrieved up to
     * that point will be returned.
     */
    static final String CONNECTION_TIMEOUT_KEY = "connectionTimeout"; //$NON-NLS-1$

    /**
     * An integer value (one of the SAVE_BEHAVIOR_* values below) that defines
     * how the report designer behaves if problems were detected in the report
     * when it is saved.
     */
    static final String SAVE_BEHAVIOR_KEY = "saveBehavior"; //$NON-NLS-1$


    /**
     * The report problem dialog should show all problems (errors and warnings)
     * when the file is saved.
     */
    static final int SAVE_BEHAVIOR_SHOW_ALL_PROBLEMS = 0;

    /**
     * The report problem dialog should show only errors, not warnings, when
     * the file is saved.
     */
    static final int SAVE_BEHAVIOR_SHOW_ERRORS_ONLY = 1;

    /**
     * The report problem dialog should not appear when the file is saved.
     */
    static final int SAVE_BEHAVIOR_SHOW_NO_PROBLEMS = 2;
}
