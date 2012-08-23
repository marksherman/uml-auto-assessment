/*==========================================================================*\
 |  $Id: WebCATConfiguration.java,v 1.2 2011/05/30 13:42:50 aallowat Exp $
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

package org.webcat.plugintester.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

//-------------------------------------------------------------------------
/**
 * Maintains a representation of the Web-CAT application configuration; that
 * is, the subsystem configuration properties and environment variables that
 * are required to properly execute plug-ins.
 *
 * @author Tony Allevato
 * @version $Id: WebCATConfiguration.java,v 1.2 2011/05/30 13:42:50 aallowat Exp $
 */
public class WebCATConfiguration
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes the WebCATConfiguration class using paths obtained from the
     * specified Web-CAT server software location.
     *
     * @param wcHome the path to the expanded Web-CAT server software directory
     */
    public WebCATConfiguration(String wcHome)
    {
        String os = System.getProperty("os.name");
        isWindows = (os != null && os.indexOf("Windows") >= 0);

        webcatHome = new File(wcHome);
        frameworksDir = new File(webcatHome,
                "WEB-INF/Web-CAT.woa/Contents/Library/Frameworks");

        initializeDefaultProperties();
        initializeDefaultEnvironment();

        loadConfigurationOverrides();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the complete environment that should be used to run grading
     * plugins.
     *
     * @return a String[] whose elements are values for environment variables,
     *     in the form "key=value"
     */
    public String[] envp()
    {
        if (envp == null)
        {
            // Convert the values in the map to "key=value" strings and cache
            // them.

            ArrayList<String> envList = new ArrayList<String>();

            for (Map.Entry<String, String> entry : envMap.entrySet())
            {
                envList.add(entry.getKey() + "=" + entry.getValue());
            }

            envp = envList.toArray(new String[envList.size()]);
        }

        return envp;
    }


    // ----------------------------------------------------------
    /**
     * Gets the Web-CAT application properties that are used when executing
     * grading plugins.
     *
     * @return a Properties object containing the Web-CAT application
     *     properties
     */
    public Properties applicationProperties()
    {
        return applicationProperties;
    }


    // ----------------------------------------------------------
    /**
     * Initializes a default set of application properties that contain file
     * and directory locations in the various *ForPlugins subsystems.
     */
    private void initializeDefaultProperties()
    {
        applicationProperties = new Properties();

        // PerlForPlugins
        applicationProperties.setProperty("PerlForPlugins.perl.exe", "perl");

        // CloverForPlugins
        applicationProperties.setProperty("clover.dir",
                new File(frameworksDir,
                        "CloverForPlugins.framework/Resources/clover").
                        getAbsolutePath());

        // CheckstyleForPlugins
        applicationProperties.setProperty("checkstyle.jar",
                new File(frameworksDir,
                        "CheckstyleForPlugins.framework/Resources/checkstyle-all.jar").
                        getAbsolutePath());

        // PMDForPlugins
        applicationProperties.setProperty("pmd.lib",
                new File(frameworksDir,
                        "PMDForPlugins.framework/Resources/pmd/lib").
                        getAbsolutePath());
    }


    // ----------------------------------------------------------
    /**
     * Uses the system shell to determine the default environment under which
     * plugins are executed.
     */
    private void getInheritedEnvironment()
    {
        // First, try Unix command
        try
        {
            Process process = Runtime.getRuntime().exec(
                isWindows ? "cmd /c set" : "printenv");

            BufferedReader in = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

            String line = in.readLine();
            while (line != null)
            {
                int pos = line.indexOf('=');
                if (pos > 0)
                {
                    String key = line.substring(0, pos);
                    String val = line.substring(pos + 1);
                    envMap.put(key, val);
                }

                line = in.readLine();
            }
        }
        catch (IOException e)
        {
            System.out.println(
                    "Error attempting to parse default ENV settings:");
            e.printStackTrace();
        }
    }


    // ----------------------------------------------------------
    /**
     * Initializes a default set of environment variables that contain file
     * and directory locations in the various *ForPlugins subsystems.
     */
    private void initializeDefaultEnvironment()
    {
        envMap = new Hashtable<String, String>();
        getInheritedEnvironment();

        // PerlForPlugins
        envMap.put("PERLLIB", new File(frameworksDir,
                "PerlForPlugins.framework/Resources/lib").getAbsolutePath());

        // ANTForPlugins
        String javaHome = System.getProperty("java.home");
        String antHome = new File(frameworksDir,
            "ANTForPlugins.framework/Resources/ant").getAbsolutePath();
        envMap.put("JAVA_HOME", javaHome);
        envMap.put("ANT_HOME", antHome);

        // Add JAVA_HOME/bin and ANT_HOME/bin to the path
        addToPath(javaHome + File.separator + "bin");
        addToPath(antHome + File.separator + "bin");
    }


    // ----------------------------------------------------------
    /**
     * Adds the specified directory to the PATH environment variable that will
     * be used when executing plugins.
     */
    private void addToPath(String dir)
    {
        String path = "";
        String pathKey;

        if (envMap.containsKey("PATH"))
        {
            path = envMap.get("PATH");
            pathKey = "PATH";
        }
        else if (envMap.containsKey("Path"))
        {
            path = envMap.get("Path");
            pathKey = "Path";
        }
        else
        {
            path = "";
            pathKey = isWindows ? "Path" : "PATH";
        }

        if (path.length() > 0)
        {
            path = path + File.pathSeparator;
        }

        path += dir;

        envMap.put(pathKey, path);
    }


    // ----------------------------------------------------------
    /**
     * Loads any application configuration overrides that the user may have
     * specified.
     */
    private void loadConfigurationOverrides()
    {
        // TODO let the user write Web-CAT.properties and
        // environment.properties that would override the
        // defaults
    }


    //~ Instance/static variables .............................................

    private final boolean isWindows;
    private File webcatHome;
    private File frameworksDir;
    private Map<String, String> envMap;
    private String[] envp;
    private Properties applicationProperties;
}
