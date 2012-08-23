/*==========================================================================*\
 |  $Id: WCServletAdaptor.java,v 1.19 2011/06/10 00:08:20 aallowat Exp $
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

package net.sf.webcat;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.zip.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import net.sf.webcat.FileUtilities;
import net.sf.webcat.SubsystemUpdater;
import net.sf.webcat.WCServletContext;
import net.sf.webcat.WCUpdater;

// -------------------------------------------------------------------------
/**
 *  This is a custom subclass of com.webobjects.jspservlet.WOServletAdaptor.
 *  It adds transparent capabilities for self-updating Java subsystems
 *  within Web-CAT, before the application starts up.
 *
 *  @author  stedwar2
 *  @author Last changed by $Author: aallowat $
 *  @version $Revision: 1.19 $, $Date: 2011/06/10 00:08:20 $
 */
@SuppressWarnings("serial")
public class WCServletAdaptor
    extends com.webobjects.jspservlet.WOServletAdaptor
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new adaptor object.
     * @throws ServletException
     */
    public WCServletAdaptor()
        throws ServletException
    {
        super();
        instance = this;
    }


    // ----------------------------------------------------------
    /**
     * Get access to the current adaptor object.
     * @return a reference to the most recently created adaptor, or null
     *     if there isn't one
     */
    public static WCServletAdaptor getInstance()
    {
        return instance;
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Start up the servlet--this method contains the heavy-weight one-time
     * actions at application startup, including updating any necessary
     * subsystems before the main application class is loaded or the
     * classpath is completely set up.
     * @throws ServletException
     */
    public void init()
        throws ServletException
    {
        // Cache the additional HTTP methods that we need to handle (for
        // WebDAV support).
        additionalHttpMethods = new HashSet<String>();
        additionalHttpMethods.add("COPY");
        additionalHttpMethods.add("LOCK");
        additionalHttpMethods.add("MKCOL");
        additionalHttpMethods.add("MOVE");
        additionalHttpMethods.add("PROPFIND");
        additionalHttpMethods.add("PROPPATCH");
        additionalHttpMethods.add("UNLOCK");

        String webInfRoot = super.getServletContext().getRealPath("WEB-INF");
        File webInfDir = new File(webInfRoot);
        propertiesFile = new File(webInfDir, "update.properties");
        loadProperties();

        systemUpdater = WCUpdater.getInstance();
        systemUpdater.setup(webInfDir);
        updateDir = systemUpdater.getUpdateDir();
        frameworkDir = systemUpdater.getFrameworkDir();

        applyNecessaryUpdates(webInfDir);

        try
        {
            super.init();
        }
        catch (NoClassDefFoundError e)
        {
            initFailed = e;
//        	for (StackTraceElement frame : e.getStackTrace())
//        	{
//        		String fileName = frame.getFileName();
//        		if (fileName != null && fileName.toLowerCase().contains("gcj"))
//        		{
//        			gcjDetected = true;
//        			break;
//        		}
//        	}
        }
        catch (javax.servlet.UnavailableException e)
        {
            // Failure during startup
            initFailed = e;
        }

        //Run background update process
        if(willUpdateAutomatically())
        {
            //Hard coded values currently: Update occurs every 4 minutes
            //Most likly change to read in a property containing update checks
            systemUpdater.startBackgroundUpdaterThread(1000, 240000);
        }
    }


    // ----------------------------------------------------------
    /**
     * Access the servlet context object for this servlet.  This is a wrapper
     * around the superclass default implementation that returns a
     * {@link WCServletContext} object wrapped around the real servlet
     * context, to provide access to a customized classpath via
     * {@link WCServletContext#getInitParameter(String)}.
     * @return the context object
     * @see javax.servlet.GenericServlet#getServletContext()
     */
    public javax.servlet.ServletContext getServletContext()
    {
        javax.servlet.ServletContext result = super.getServletContext();
        if (result != null)
        {
            if (result != innerContext)
            {
                wrappedContext = new WCServletContext(result, woClasspath);
            }
            innerContext = result;
            result = wrappedContext;
        }
        return result;
    }


    // ----------------------------------------------------------
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException,
            ServletException
    {
        if (initFailed != null)
        {
            sendExceptionNotice(response);
        }
        else
        {
            super.doGet(request, response);
        }
    }


    // ----------------------------------------------------------
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }


    // ----------------------------------------------------------
    public void doDelete(HttpServletRequest request,
                         HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }


    // ----------------------------------------------------------
    public void doOptions(HttpServletRequest request,
                          HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }


    // ----------------------------------------------------------
    public void service(HttpServletRequest request,
                        HttpServletResponse response)
        throws ServletException, IOException
    {
        if (additionalHttpMethods.contains(request.getMethod()))
        {
            doGet(request, response);
        }
        else
        {
            super.service(request, response);
        }
    }


    // ----------------------------------------------------------
    /**
     * Determine if this adaptor will attempt to automatically update all
     * subsystems on start-up.
     * @return true if subsystems will be automatically updated
     */
    public boolean willUpdateAutomatically()
    {
        String val = properties.getProperty("updateAutomatically", "1")
            .toLowerCase();
        return val.equals("1")
            || val.equals("true")
            || val.equals("on")
            || val.equals("yes");
    }


    // ----------------------------------------------------------
    /**
     * Set whether or not this adaptor will attempt to automatically update
     * all subsystems on start-up.
     * @param value true if this adaptor will auto-update subsystems
     */
    public void setWillUpdateAutomatically(boolean value)
    {
        properties.setProperty("updateAutomatically", value ? "1" : "0");
        commitProperties();
    }


    // ----------------------------------------------------------
    /**
     * Access the collection of subsystems in this application.
     * @return a collection of {@link SubsystemUpdater} objects representing
     *     the available subsystems
     */
    public Collection<SubsystemUpdater> subsystems()
    {
        return systemUpdater.subsystems();
    }


    // ----------------------------------------------------------
    /**
     * Get a file representing the directory where downloaded updates should
     * be placed.
     * @return a collection of {@link SubsystemUpdater} objects representing
     *     the available subsystems
     */
    public File updateDownloadLocation()
    {
        return updateDir;
    }


    // ----------------------------------------------------------
    /**
     * Get the version for the Bootstrap build containing this class.
     * @return the version number as a string
     */
    public String version()
    {
        return VERSION;
    }


    //~ Private Methods .......................................................

    // ----------------------------------------------------------
    /**
     * Build an exception notification message as the designated http response.
     * @param response the http response being generated
     * @throws IOException if one arises while writing the response
     */
    private void sendExceptionNotice(HttpServletResponse response)
        throws IOException
    {
        response.setContentType("text/html");
        PrintWriter out = new PrintWriter(response.getOutputStream());
        out.println("<html><head>");
        out.println("<title>Web-CAT Startup Failure</title>");
        out.println("</head><body>");
        out.println("<h1>Web-CAT Startup Failure</h1>");
        out.println("<p>Web-CAT threw an unexpected exception during ");
        out.println("initialization.  Please shut down the web application ");
        out.println("and fix the problem.</p>");
        String vmName = System.getProperty("java.vm.name");
        if (vmName != null && vmName.toLowerCase().contains("gcj"))
        {
            out.println("<p>In this case, it appears that you are <b>using");
            out.println("gcj</b> to run your servlet container.  Web-CAT ");
            out.println("does not run properly under gcj at this time.");
            out.println("Please install Sun's JDK and");
            out.println("<a href=\"http://web-cat.org/WCWiki/");
            out.println("SwitchToSunJdk\">configure your servlet");
            out.println("container</a> to use it instead.</p>");
        }
        out.println("<p>For more information, locate your ");
        out.println("servlet container's <b>stdout log file</b> and ");
        out.println("examine it to identify the exception stack trace.</p>");
        out.println("<p>For assistance, send the stdout log file to ");
        out.println("the Web-CAT project team at ");
        out.println("<a href=\"mailto:webcat@vt.edu\">webcat@vt.edu</a>.</p>");
        Throwable nested = initFailed;
        while (nested.getCause() != null)
        {
            nested = nested.getCause();
        }
        out.println("<h2>Root Cause</h2>\n<pre>");
        nested.printStackTrace(out);
        out.println("</pre>");
        if (nested != initFailed)
        {
            out.println("<h2>Full Exception Details</h2>\n<pre>");
            initFailed.printStackTrace(out);
            nested = initFailed.getCause();
            while (nested != null)
            {
                out.println("\nCaused by:");
                nested.printStackTrace(out);
                nested = nested.getCause();
            }
            out.println("</pre>");
        }
        out.println("</body></html>");
        out.flush();
        out.close();
    }


    // ----------------------------------------------------------
    /**
     * Apply any necessary updates and compute the woClasspath value.
     * @param webInfDir the WEB-INF directory as a file object
     */
    private void applyNecessaryUpdates(File webInfDir)
    {
        File appDir = webInfDir.getParentFile();
        applyPendingUpdates(frameworkDir, appDir);

        if (frameworkDir != null && frameworkDir.isDirectory())
        {
            File[] subdirs = frameworkDir.listFiles();
            java.util.Arrays.sort(subdirs, new FrameworkComparator());
            woClasspath = classPathFrom(subdirs,  systemUpdater.getMainBundle());
            System.out.println("Dynamically computed classpath:");
            System.out.print(woClasspath);
        }
    }


    // ----------------------------------------------------------
    /**
     * Scan the update directory for any downloaded update files, apply
     * them, and then delete them.
     * @param aFrameworkDir The place to unpack updates
     * @param appDir The application directory, which is where any top-level
     *     updates (e.g., "webcat_*" files) are unpacked
     */
    private void applyPendingUpdates(File aFrameworkDir, File appDir)
    {
        if (updateDir.exists() && updateDir.isDirectory())
        {
            for (File jar : updateDir.listFiles())
            {
                for (String extension :
                    SubsystemUpdater.JAVA_ARCHIVE_EXTENSIONS)
                {
                    if (jar.getName().endsWith(extension))
                    {
                        File unpackDir = aFrameworkDir;
                        if (jar.getName().startsWith(APP_JAR_PREFIX))
                        {
                            unpackDir = appDir;
                        }
                        try
                        {
                            prepareUnpackingDir(unpackDir, jar);
                            System.out.println("Applying update from "
                                + jar.getName());
                            ZipFile zipFile = new ZipFile(jar);
                            FileUtilities.unZip(zipFile, unpackDir);
                            zipFile.close();
                            if (!jar.delete())
                            {
                                System.out.println(
                                    "WCServletAdaptor: ERROR: unable to delete "
                                    + jar.getAbsolutePath());
                            }
                        }
                        catch (java.io.IOException e)
                        {
                            System.out.println("WCServletAdaptor: ERROR: "
                                + "unpacking update bundle: "
                                + e);
                            System.out.println("on file: "
                                + jar.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Perform any pre-cleaning steps needed before unpacking a subsystem
     * update jar.  This method will delete the old contents of the subsystem
     * if necessary, based on the properties stored in the
     * {@link SubsystemUpdater} corresponding to this jar file.
     *
     * @param unpackDir the directory where the jar will be unpacked
     * @param jar the jar file that will be unpacked
     */
    private void prepareUnpackingDir(File unpackDir, File jar)
    {
        String frameworkName = jar.getName().replaceFirst(
            "_[0-9]+(\\.[0-9]+)*\\..*$", "");
        boolean isAppWrapper = APP_JAR_PREFIX.equals(frameworkName);
        File thisFrameworkDir = new File(unpackDir,
            isAppWrapper
                ? "WEB-INF/Web-CAT.woa/Contents"
                : frameworkName + ".framework");
        SubsystemUpdater updater = getUpdaterFor(thisFrameworkDir);

        String[] alsoContains = null;
        {
            String alsoContainsRaw = updater.getProperty("alsoContains");
            if (alsoContainsRaw != null)
            {
                alsoContains = alsoContainsRaw.split(",\\s*");
            }
        }

        String[] removeUnused = null;
        {
            String removeUnusedRaw = updater.getProperty("removeUnused");
            if (removeUnusedRaw != null)
            {
                removeUnused = removeUnusedRaw.split(",\\s*");
            }
        }

        Map<String, String> preserveOnUpdate = null;
        {
            String preserveOnUpdateRaw =
                updater.getProperty("preserveOnUpdate");
            if (isAppWrapper && preserveOnUpdateRaw == null)
            {
                preserveOnUpdateRaw = "WEB-INF/lib,"
                    + "WEB-INF/web.xml,"
                    + "WEB-INF/update.properties,"
                    + "WEB-INF/Web-CAT.woa/Contents/Frameworks,"
                    + "WEB-INF/Web-CAT.woa/Contents/Library,"
                    + "WEB-INF/Web-CAT.woa/configuration.properties,"
                    + "WEB-INF/pending-updates";
            }
            if (preserveOnUpdateRaw != null)
            {
                preserveOnUpdate = new HashMap<String, String>();
                for (String entry : preserveOnUpdateRaw.split(",\\s*"))
                {
                    String key = FileUtilities.normalizeFileName(
                        new File(unpackDir, entry));
                    preserveOnUpdate.put(key, key);
                }
            }
        }
        FileUtilities.deleteDirectory(
            isAppWrapper ? unpackDir : thisFrameworkDir, preserveOnUpdate);
        if (isAppWrapper)
        {
            // Examine configuration.properties to see if we just deleted
            // local copies of the static HTML resources
            File config = new File(unpackDir,
                "Web-INF/Web-CAT.woa/configuration.properties");
            if (config.exists())
            {
                try
                {
                    InputStream is = new FileInputStream(config);
                    Properties configProps = new Properties();
                    configProps.load(is);
                    is.close();
                    if (configProps.getProperty("static.html.dir") == null)
                    {
                        // If this property is not set, then static resources
                        // are stored in the root of the web app and they
                        // were just deleted.  Force re-copying of them.
                        configProps.setProperty(
                            "static.HTML.date", "00000000");
                        OutputStream out = new FileOutputStream(config);
                        configProps.store(
                            out, "Web-CAT configuration settings");
                        out.close();
                    }
                }
                catch (IOException e)
                {
                    // We're not using log4j, since that may be within a
                    // subsystem that needs updating
                    System.out.println("WCServletAdaptor: ERROR: IO error "
                        + "updating properties in "
                        + config.getAbsolutePath()
                        + ":"
                        + e);
                }
            }
        }
        if (alsoContains != null)
        {
            for (String contains : alsoContains)
            {
                FileUtilities.deleteDirectory(
                    new File(unpackDir, contains), preserveOnUpdate);
            }
        }
        if (removeUnused != null)
        {
            for (String unused : removeUnused)
            {
                FileUtilities.deleteDirectory(
                    new File(unpackDir, unused), preserveOnUpdate);
            }
        }
    }

    // ----------------------------------------------------------
    /**
     * Generate the classpath string from a list of framework directories.
     * @param subdirs
     * @param mainBundle
     */
    private String classPathFrom(File[] subdirs, File mainBundle)
    {
        StringBuffer buffer = new StringBuffer(20 * subdirs.length);
        String woroot = properties.getProperty(INSTALLED_WOROOT);
        File installedWOFrameworkDir = null;
        if (woroot != null)
        {
            installedWOFrameworkDir = new File(woroot, "Library/Frameworks");
            if (!installedWOFrameworkDir.exists())
            {
                System.out.println("Cannot locate installed WO framework "
                    + "directory at: " + installedWOFrameworkDir);
                installedWOFrameworkDir = null;
            }
            else if (!installedWOFrameworkDir.isDirectory())
            {
                System.out.println("Installed WO framework location is not a "
                    + "directory: " + installedWOFrameworkDir);
                installedWOFrameworkDir = null;
            }
            System.out.println("using WO root = " + installedWOFrameworkDir);
        }

        // First, handle all the subsystems
        for (File subdir : subdirs)
        {
            // Be sure to use the *local* version of JavaWOExtensions (from
            // project WONDER) rather than the default system version.
            if (installedWOFrameworkDir != null
                && !"JavaWOExtensions.framework".equals(subdir.getName()))
            {
                File localSubdir =
                    new File(installedWOFrameworkDir, subdir.getName());
                if (localSubdir.exists())
                {
                    // use the externally installed version instead
                    subdir = localSubdir;
                }
            }

            getUpdaterFor(subdir).addToClasspath(buffer);
        }

        // Now handle the main bundle itself
        getUpdaterFor(mainBundle).addToClasspath(buffer);

        return buffer.toString();
    }


    // ----------------------------------------------------------
    /**
     * Attempt to load the properties settings for this adaptor.
     */
    private void loadProperties()
    {
        properties = new Properties();
        if (propertiesFile.exists())
        {
            try
            {
                InputStream is = new FileInputStream(propertiesFile);
                properties.load(is);
                is.close();
            }
            catch (IOException e)
            {
                // We're not using log4j, since that may be within a
                // subsystem that needs updating
                System.out.println("Error loading properties from "
                           + propertiesFile.getAbsolutePath()
                           + ":"
                           + e);
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Save the properties settings for this adaptor.
     */
    private void commitProperties()
    {
        try
        {
            OutputStream out = new FileOutputStream(propertiesFile);
            properties.store(out, "WCServletAdaptor properties");
            out.close();
        }
        catch (IOException e)
        {
            System.out.println("Error saving WCServletAdaptor properties to "
                       + propertiesFile.getAbsolutePath()
                       + ":"
                       + e);
        }
    }


    // ----------------------------------------------------------
    /**
     * Get the {@link SubsystemUpdater} for the specified subsystem location.
     * Creates a new updater on demand, if necessary.
     * @param dir the subsystem location to look up
     * @return the corresponding updater
     */
    private SubsystemUpdater getUpdaterFor(File dir)
    {
        return systemUpdater.getUpdaterFor(dir);
    }

    // ----------------------------------------------------------
    /**
     * This comparator is used by {@link #applyNecessaryUpdates(File)} to
     * sort framework directories.  It ensures that the high-priority
     * frameworks are first on the resulting classpath.
     */
    private static class FrameworkComparator
        implements java.util.Comparator<File>
    {
        // ----------------------------------------------------------
        public int compare(File o1, File o2)
        {
            String left  = o1.getName();
            String right = o2.getName();
            if (left.equals(right))
            {
                return 0;
            }
            else
            {
                for (int i = 0; i < PRIORITY_FRAMEWORKS.length; i++)
                {
                    if (PRIORITY_FRAMEWORKS[i].equals(left))
                    {
                        return -1;
                    }
                    else if (PRIORITY_FRAMEWORKS[i].equals(right))
                    {
                        return 1;
                    }
                }
                return left.compareTo(right);
            }
        }
    }


    //~ Instance/static variables .............................................
    private WCUpdater systemUpdater;

    private javax.servlet.ServletContext  innerContext   = null;
    private javax.servlet.ServletContext  wrappedContext = null;
    private String                        woClasspath    = null;
    private Properties                    properties;
    private File                          propertiesFile;
    private File                          updateDir;
    private File                          frameworkDir;

    private Throwable                     initFailed;

    private Set<String>                   additionalHttpMethods;

    private static WCServletAdaptor instance;

    private static final String[] PRIORITY_FRAMEWORKS = {
        "EOJDBCPrototypes.framework",
        "ERJars.framework",
        "ERExtensions.framework"
    };

    private static final String APP_JAR_PREFIX   = "webcat";
    private static final String INSTALLED_WOROOT = "installed.woroot";
    private static final String VERSION          = "1.5";
}
