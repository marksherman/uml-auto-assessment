/*==========================================================================*\
 |  $Id: AppConstants.java,v 1.1 2010/05/10 16:15:19 aallowat Exp $
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

package org.webcat.plugintester;

//-------------------------------------------------------------------------
/**
 * Various constants used in the Web-CAT Plugin Tester application.
 * 
 * @author Tony Allevato
 * @version $Id: AppConstants.java,v 1.1 2010/05/10 16:15:19 aallowat Exp $
 */
public interface AppConstants
{
    /**
     * The name of the configuration file that maintains the application's
     * settings across runs.
     */
    static final String CONFIG_FILE = ".plugintester.config";

    /**
     * The property that stores the path to the Web-CAT server software.
     */
    static final String PROP_WEBCAT_HOME = "webcat.home";
    
    /**
     * The property that stores the path to the submission being used for
     * testing.
     */
    static final String PROP_LAST_SUBMISSION_PATH = "last.submission.path";
    
    /**
     * The property that stores the paths to the plugins that are being used
     * for testing.
     */
    static final String PROP_LAST_PLUGIN_PATHS = "last.plugin.paths";
    
    /**
     * The property that stores the user overrides for the values in the
     * grading.properties file.
     */
    static final String PROP_USER_GRADING_PROPERTIES =
        "user.grading.properties";
}
