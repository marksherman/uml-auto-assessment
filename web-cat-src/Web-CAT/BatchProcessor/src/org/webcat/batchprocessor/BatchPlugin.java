/*==========================================================================*\
 |  $Id: BatchPlugin.java,v 1.8 2012/03/07 03:21:13 stedwar2 Exp $
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import net.sf.webcat.FeatureDescriptor;
import net.sf.webcat.FeatureProvider;
import org.webcat.core.Application;
import org.webcat.core.MutableDictionary;
import org.webcat.core.User;
import org.webcat.woextensions.ECAction;
import static org.webcat.woextensions.ECAction.run;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSTimestamp;
import er.extensions.foundation.ERXArrayUtilities;
import er.extensions.foundation.ERXValueUtilities;

// -------------------------------------------------------------------------
/**
 * Represents an uploaded batch processing plug-in.
 *
 * @author  Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.8 $, $Date: 2012/03/07 03:21:13 $
 */
public class BatchPlugin
    extends _BatchPlugin
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new BatchPlugin object.
     */
    public BatchPlugin()
    {
        super();
    }


    //~ Constants .............................................................

    public static final String NO_AUTO_UPDATE_KEY =
        "batchprocessor.willNotAutoUpdatePlugins";
    public static final String NO_AUTO_INSTALL_KEY =
        "batchprocessor.willNotAutoInstallPlugins";
    public static final String AUTO_PUBLISH_KEY =
        "autoPublish";


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Determine whether or not this plug-in is stored in its own
     * subdirectory, or as a single file.
     * @return true if there is a subdirectory for this plug-in
     */
    public boolean hasSubdir()
    {
        return subdirName() != null;
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the name of the directory where this plug-in is stored.
     * @return the directory name
     */
    public String dirName()
    {
        StringBuffer dir = userPluginDirName(author(), false);
        if (hasSubdir())
        {
            dir.append('/');
            dir.append(subdirName());
        }

        return dir.toString();
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the path name for this plug-in's entry point--its main
     * executable file.
     * @return the path to the main file
     */
    public String mainFilePath()
    {
        String pluginName = null;
        @SuppressWarnings("unchecked")
        NSDictionary<String, Object> config = configDescription();

        if (config != null)
        {
            pluginName = (String)config.objectForKey("executable");
        }

        if (pluginName == null)
        {
            pluginName = mainFileName();
        }

        return dirName() + "/" + pluginName;
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the path name for this plug-in's configuration description.
     * @return the path to the config.plist file
     */
    public String configPlistFilePath()
    {
        return dirName() + "/config.plist";
    }


    // ----------------------------------------------------------
    /**
     * Execute this plug-in with the given command line argument(s).
     *
     * @param args the arguments to pass to the plug-in on the command line
     * @param cwd the working directory to use
     * @return the Java Process object representing the external process
     *
     * @throws java.io.IOException if one occurs
     * @throws InterruptedException if one occurs
     */
    public Process execute(String args, File cwd)
    throws IOException
    {
        if (log.isDebugEnabled())
        {
            log.debug("execute(): args = '" + args + "', cwd = " + cwd);
        }

        String command = "";

        if (configDescription().containsKey("interpreter.prefix"))
        {
            // Look up the associated value, perform property substitution
            // on it, and add it before the main file name.

            command = command
                + Application.configurationProperties().
                    substitutePropertyReferences(
                        configDescription().valueForKey("interpreter.prefix")
                            .toString())
                            + " ";
        }

        command += mainFilePath();

        if (args != null)
        {
            command = command + " " + args;
        }

        return Application.wcApplication().executeExternalCommandAsync(
            command, cwd);
    }


    // ----------------------------------------------------------
    /**
     * Get a short (no longer than 60 characters) description of this plug-in,
     * which currently returns {@link #name()}.
     * @return the description
     */
    public String userPresentableDescription()
    {
        return name();
    }


    // ----------------------------------------------------------
    public String displayableName()
    {
        Object nameObj = configDescription().valueForKey("displayableName");

        String displayableName =
            (nameObj == null) ? name() : nameObj.toString();

        if (displayableName == null)
        {
            displayableName = uploadedFileName();
        }

        return displayableName;
    }


    // ----------------------------------------------------------
    /**
     * If this plug-in's config.plist file has been modified, then
     * reparse it and store its config information.
     */
    public void reinitializeConfigAttributesIfNecessary()
    {
        if (hasSubdir())
        {
            File configPlist = new File(configPlistFilePath());
            NSTimestamp lastRead = lastModified();
            NSTimestamp modified =
                new NSTimestamp(configPlist.lastModified());

            if (lastRead != null && modified.after(lastRead))
            {
                // silently swallow returned message
                initializeConfigAttributes(configPlist);
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Parse this plug-in's config.plist file and store its config information.
     * @return null on success, or an error message if parsing failed
     */
    public String initializeConfigAttributes()
    {
        if (hasSubdir())
        {
            return initializeConfigAttributes(
                new File(configPlistFilePath()));
        }

        return null;
    }


    // ----------------------------------------------------------
    /**
     * Parse this plug-in's config.plist file and store its config information.
     * @param configPlist the config.plist file to parse
     * @return null on success, or an error message if parsing failed
     */
    public String initializeConfigAttributes(File configPlist)
    {
        // reset the cached descriptor, if any
        descriptor = null;

        if (configPlist.exists())
        {
            try
            {
                log.debug("reloading " + configPlist.getCanonicalPath());
            }
            catch (IOException e)
            {
                log.error("error attempting to load confg.plist file for "
                    + this, e);
            }

            try
            {
                MutableDictionary dict =
                    MutableDictionary.fromPropertyList( configPlist );
                setConfigDescription( dict );
//              log.debug( "script config.plist = " + dict );
                String dictName = (String)dict.objectForKey( "name" );
                setName(dictName);

                String entity = (String)dict.objectForKey("batchEntity");
                setBatchEntity(entity);

                NSArray<?> options =
                    (NSArray<?>)dict.objectForKey( "options" );
//              log.debug( "options = " + options );
                if (options != null)
                {
                    MutableDictionary defaults = new MutableDictionary();
                    for (int i = 0; i < options.count(); i++)
                    {
                        @SuppressWarnings("unchecked")
                        NSDictionary<String, ?> thisOption =
                            (NSDictionary<String, ?>)options.objectAtIndex(i);
//                      log.debug( "this option = " + thisOption );
                        if (thisOption.objectForKey("disable") == null)
                        {
                            String property = (String)thisOption
                                .objectForKey("property");
                            Object value = thisOption
                                .objectForKey("default");
                            if (property != null && value != null)
                            {
                                defaults.setObjectForKey(value, property);
                            }
                        }
                    }
                    setDefaultConfigSettings(defaults);
                }
                else
                {
                    setDefaultConfigSettings(null);
                }
                setLastModified(
                    new NSTimestamp(configPlist.lastModified()));
                if (ERXValueUtilities.booleanValue(
                    configDescription().get(AUTO_PUBLISH_KEY)))
                {
                    setIsPublished(true);
                }
            }
            catch (Exception e)
            {
                return e.getMessage()
                    + " (error reading plug-in's config.plist file)";
            }
        }
        else
        {
            return "This plug-in is missing its 'config.plist' file.";
        }

        return null;
    }


    // ----------------------------------------------------------
    /**
     * Get the FeatureDescriptor for this plug-in.
     *
     * @return this plug-in's descriptor
     */
    public FeatureDescriptor descriptor()
    {
        if (descriptor == null)
        {
            descriptor = new BatchPluginDescriptor(this);
        }

        return descriptor;
    }


    // ----------------------------------------------------------
    /**
     * Download this plug-in's latest file from its provider on-line
     * and install it for the given user, overwriting any existing
     * version.
     * @return null on success, or an error message on failure
     */
    public String installUpdate()
    {
        return installOrUpdate(
            author(), descriptor().providerVersion(), true, this);
    }


    // ----------------------------------------------------------
    /**
     * Download the specified plug-in file and install it for the given
     * user.  If the download succeeds, a new BatchPlugin object will be
     * created under the specified user object's editing context.  The
     * new BatchPlugin object is not returned, by can be retrieved after
     * comitting the user object's editing context and refetching.
     * @param installedBy the user
     * @param plugin the plug-in to download
     * @param overwrite true if the named plug-in already exists in the
     *        user's directory
     * @return null on success, or an error message on failure
     */
    public static String installOrUpdate(
        User                            installedBy,
        net.sf.webcat.FeatureDescriptor plugin,
        boolean                         overwrite)
    {
        return installOrUpdate(installedBy, plugin, overwrite, null);
    }


    // ----------------------------------------------------------
    /**
     * Automatically update any installed plug-ins if auto-updates are
     * enabled, and automatically install any new plug-ins, if
     * auto-installation is enabled.
     */
    public static void autoUpdateAndInstall()
    {
        run(new ECAction() { public void action() {
            autoInstallNewPlugins(ec, autoUpdatePlugins(ec));
        }});
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the name of the directory where a user's plug-ins are stored.
     * @param pluginAuthor the user
     * @param isData true if this is the directory for a plug-in data/config
     *               file, or false if this is the directory where plug-ins
     *               themselves are stored
     * @return the directory name
     */
    public static StringBuffer userPluginDirName(
        User pluginAuthor, boolean isData)
    {
        StringBuffer dir = new StringBuffer(50);
        dir.append(isData ? pluginDataRoot() : pluginRoot());
        dir.append('/');
        dir.append(pluginAuthor.authenticationDomain().subdirName());
        dir.append('/');
        dir.append(pluginAuthor.userName());
        return dir;
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the name of the directory where this plug-in is stored.
     * @param fileName the plug-in's file name
     * @return the subdirectory name based on the uploaded file name, with
     * all dots replaced by underscores
     */
    public static String convertToSubdirName(String fileName)
    {
        return fileName.replace('.', '_').replace(' ', '-');
    }


    // ----------------------------------------------------------
    /**
     * Create a new plug-in file object from uploaded file data.
     * @param ec           the editing context in which to add the new object
     * @param pluginAuthor       the user uploading the plug-in
     * @param uploadedName the plug-in's file name
     * @param uploadedData the file's data
     * @param isData       true if this is a plug-in data/config file, or
     *                     false if this is a plug-in itself
     * @param expand       true if zip/jar files should be expanded, or false
     *                     otherwise
     * @param errors       a dictionary in which to store any error messages
     *                     for display to the user
     * @return the new plug-in file, if successful, or null if unsuccessful
     */
    public static BatchPlugin createNewBatchPlugin(
            EOEditingContext                    ec,
            User                                pluginAuthor,
            String                              uploadedName,
            NSData                              uploadedData,
            boolean                             expand,
            NSMutableDictionary<String, Object> errors)
    {
        String userPluginDir =
            userPluginDirName(pluginAuthor, false).toString();
        String newSubdirName = null;
        uploadedName = (new File(uploadedName)).getName();
        String uploadedNameLC = uploadedName.toLowerCase();
        File toLookFor;
        if (expand && (uploadedNameLC.endsWith(".zip") ||
                       uploadedNameLC.endsWith(".jar")))
        {
            newSubdirName = convertToSubdirName(uploadedName);
            toLookFor = new File(userPluginDir + "/" + newSubdirName);
        }
        else
        {
            toLookFor = new File(userPluginDir + "/" + uploadedName);
        }
        if (toLookFor.exists())
        {
            String msg = "You already have an uploaded batch plug-in with "
                + "this name.  If you want to change that plug-in's "
                + "files, then edit its configuration.  Otherwise, please "
                + "use a different file name for this new plug-in.";
            errors.setObjectForKey(msg, msg);
            return null;
        }

        BatchPlugin batchPlugin = new BatchPlugin();
        ec.insertObject(batchPlugin);
        batchPlugin.setUploadedFileName(uploadedName);
        batchPlugin.setMainFileName(uploadedName);
        batchPlugin.setLastModified(new NSTimestamp());
        batchPlugin.setAuthorRelationship(pluginAuthor);

        // Save the file to disk
        log.debug("saving to file " + batchPlugin.mainFilePath());
        File pluginPath = new File(batchPlugin.mainFilePath());
        try
        {
            pluginPath.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(pluginPath);
            uploadedData.writeToStream(out);
            out.close();
        }
        catch (java.io.IOException e)
        {
            String msg = e.getMessage();
            errors.setObjectForKey(msg, msg);
            ec.deleteObject(batchPlugin);
            pluginPath.delete();
            return null;
        }

        if (expand && (uploadedNameLC.endsWith(".zip") ||
                       uploadedNameLC.endsWith(".jar")))
        {
            try
            {
                // ZipFile zip = new ZipFile(script.mainFilePath());
                batchPlugin.setSubdirName(newSubdirName);
                log.debug("unzipping to " + batchPlugin.dirName());
                org.webcat.archives.ArchiveManager.getInstance()
                    .unpack(new File(batchPlugin.dirName()), pluginPath);
                //Grader.unZip(zip, new File(script.dirName()));
                //zip.close();
                pluginPath.delete();
            }
            catch (java.io.IOException e)
            {
                String msg = e.getMessage();
                errors.setObjectForKey(msg, msg);
                batchPlugin.setSubdirName(newSubdirName);
                org.webcat.core.FileUtilities
                    .deleteDirectory(batchPlugin.dirName());
                pluginPath.delete();
                log.warn("error unzipping:", e);
                // throw new NSForwardException(e);
                ec.deleteObject(batchPlugin);
                return null;
            }
            batchPlugin.setMainFileName(null);
            String msg = batchPlugin.initializeConfigAttributes();
            if (msg != null)
            {
                errors.setObjectForKey(msg, msg);
            }
        }
        return batchPlugin;
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the name of the directory where all user plug-ins are stored.
     * @return the directory name
     */
    public static String pluginRoot()
    {
        if (pluginRoot == null)
        {
            pluginRoot = org.webcat.core.Application
                .configurationProperties().getProperty("grader.scriptsroot");
            if (pluginRoot == null)
            {
                pluginRoot = org.webcat.core.Application
                    .configurationProperties()
                        .getProperty("grader.submissiondir")
                    + "/BatchPlugins";
            }
        }
        return pluginRoot;
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the name of the directory where all user plug-in config/data
     * files are stored.
     * @return the directory name
     */
    public static String pluginDataRoot()
    {
        if (pluginDataRoot == null)
        {
            pluginDataRoot = org.webcat.core.Application
                .configurationProperties()
                .getProperty("grader.scriptsdataroot");
            if (pluginDataRoot == null)
            {
                pluginDataRoot = pluginRoot() + "Data";
            }
        }
        return pluginDataRoot;
    }


    // ----------------------------------------------------------
    /**
     * Gets the array of all report templates that are accessible by the
     * specified user. This is the union of that user's own uploaded templates
     * and all of the published templates.
     *
     * @param ec the editing context to load the templates into
     * @param user the user
     *
     * @return the array of report templates accessible by the user
     */
    public static NSArray<BatchPlugin> pluginsAccessibleByUser(
            EOEditingContext ec, User user)
    {
        // Admins have access to everything, so just short-circuit this.

        if (user.hasAdminPrivileges())
        {
            return allPluginsOrderedByName(ec);
        }

        NSMutableArray<BatchPlugin> allPlugins =
            publishedPlugins(ec).mutableClone();

        ERXArrayUtilities.addObjectsFromArrayWithoutDuplicates(
            allPlugins, pluginsForUser(ec, user));

        ERXArrayUtilities.sortArrayWithKey(allPlugins, BatchPlugin.NAME_KEY);

        return allPlugins;
    }


    //~ Private Methods .......................................................

    // ----------------------------------------------------------
    /**
     * Download the specified plug-in file and install it for the given
     * user.  If the download succeeds, the given BatchPlugin object
     * will be updated appropriately.  If none is provided, a new BatchPlugin
     * object will be created under the specified user object's editing
     * context.  The new BatchPlugin object is not returned, by can be
     * retrieved after comitting the user object's editing context and
     * refetching.
     * @param installedBy the user
     * @param plugin the plug-in to download
     * @param overwrite true if the named plug-in already exists in the
     *        user's directory
     * @param batchPlugin the BatchPlugin object to update (or null to force
     *        creation of a new one)
     * @return null on success, or an error message on failure
     */
    private static String installOrUpdate(
        User                            installedBy,
        net.sf.webcat.FeatureDescriptor plugin,
        boolean                         overwrite,
        BatchPlugin                     batchPlugin)
    {
        if (batchPlugin != null && !batchPlugin.hasSubdir())
        {
            return "Installed plug-in does not support downloads!";
        }

        BatchPlugin newBatchPlugin = null;
        String pluginSubdirName = convertToSubdirName(plugin.name());
        File newBatchPluginPath = null;
        if (batchPlugin == null)
        {
            newBatchPlugin = new BatchPlugin();
            installedBy.editingContext().insertObject(newBatchPlugin);
            newBatchPlugin.setLastModified(new NSTimestamp());
            newBatchPlugin.setAuthorRelationship(installedBy);
            newBatchPlugin.setSubdirName(pluginSubdirName);
            batchPlugin = newBatchPlugin;
        }
        else if (!pluginSubdirName.equals(batchPlugin.subdirName()))
        {
            newBatchPluginPath = new File (
                userPluginDirName(installedBy, false).toString(),
                pluginSubdirName);
            if (newBatchPluginPath.exists())
            {
                return "The plug-in you are updating has changed names, but "
                    + "you already have an installed plug-in with the new "
                    + "name, so there is a conflict.  The original plug-in "
                    + "cannot be updated until the name conflict is resolved.";
            }
        }

        File pluginSubdir = new File(batchPlugin.dirName());
        if (pluginSubdir.exists())
        {
            log.debug(
                "directory " + pluginSubdir.getAbsolutePath() + " exists");
            if (overwrite)
            {
                org.webcat.core.FileUtilities.deleteDirectory(pluginSubdir);
            }
            else
            {
                if (newBatchPlugin != null)
                {
                    newBatchPlugin.editingContext()
                        .deleteObject(newBatchPlugin);
                }
                return "You already have an installed plug-in with this name."
                    + " If you want to change that plug-in's files, then "
                    + " use its browse/edit action icon instead.";
            }
        }

        // Save the file to disk
        log.debug("downloading plug-in archive");
        if (newBatchPluginPath == null)
        {
            newBatchPluginPath = new File(batchPlugin.dirName());
        }
        else
        {
            batchPlugin.setSubdirName(pluginSubdirName);
        }
        File downloadPath = newBatchPluginPath.getParentFile();
        File archiveFile = new File(downloadPath.getAbsolutePath()
            + "/" + plugin.name() + "_" + plugin.currentVersion() + ".jar");
        downloadPath.mkdirs();
        plugin.downloadTo(downloadPath);
        try
        {
            org.webcat.archives.ArchiveManager.getInstance()
                .unpack(newBatchPluginPath,  archiveFile);
        }
        catch (java.io.IOException e)
        {
            if (newBatchPlugin != null)
            {
                newBatchPlugin.editingContext()
                    .deleteObject(newBatchPlugin);
            }
            return e.getMessage();
        }

        archiveFile.delete();
        String msg = batchPlugin.initializeConfigAttributes();
        if (msg != null)
        {
            if (newBatchPlugin != null)
            {
                newBatchPlugin.editingContext()
                    .deleteObject(newBatchPlugin);
            }
        }
        return msg;
    }


    // ----------------------------------------------------------
    private static NSArray<BatchPlugin> autoUpdatePlugins(EOEditingContext ec)
    {
        NSArray<BatchPlugin> pluginList = allObjects(ec);
        if (!Application.configurationProperties()
            .booleanForKey(NO_AUTO_UPDATE_KEY))
        {
            for (BatchPlugin plugin : pluginList)
            {
                try
                {
                    if (plugin.descriptor().updateIsAvailable())
                    {
                        log.info(
                            "Updating plug-in: \"" + plugin.name() + "\"" );
                        String msg = plugin.installUpdate();
                        if (msg != null)
                        {
                            log.error("Error updating plug-in \""
                                + plugin.name() + "\": " + msg);
                        }
                        ec.saveChanges();
                    }
                    else
                    {
                        log.debug("Plug-in \"" + plugin.name()
                            + "\" is up to date.");
                    }
                }
                catch (IOException e)
                {
                    log.error("Error checking for updates to plug-in \""
                        + plugin.name() + "\": " + e);
                }
            }
        }
        return pluginList;
    }


    // ----------------------------------------------------------
    private static void autoInstallNewPlugins(
        EOEditingContext ec, NSArray<BatchPlugin> pluginList)
    {
        if (Application.configurationProperties()
            .booleanForKey(NO_AUTO_INSTALL_KEY))
        {
            return;
        }
        String adminUserName = Application.configurationProperties()
            .getProperty("AdminUsername");
        if (adminUserName == null)
        {
            log.error("No definition for 'AdminUsername' config property!\n"
                + "Cannot install new plug-ins without admin user name.");
            return;
        }
        User admin = null;
        NSArray<User> candidates = User.objectsMatchingQualifier(ec,
            User.userName.eq(adminUserName));
        for (User user : candidates)
        {
            if (user.hasAdminPrivileges())
            {
                if (admin == null)
                {
                    admin = user;
                }
                else
                {
                    log.warn("Duplicate admin accounts with user name \""
                        + adminUserName + "\" found.  Using " + admin
                        + ", ignoring " + user);
                }
            }
        }
        if (admin == null)
        {
            log.error("Cannot find admin account with user name \""
                + adminUserName + "\"!");
            return;
        }

        Collection<FeatureDescriptor> availablePlugins =
            new HashSet<FeatureDescriptor>();
        for (FeatureProvider provider : FeatureProvider.providers())
        {
            if (provider != null)
            {
                availablePlugins.addAll(provider.plugins());
            }
        }
        if (pluginList != null)
        {
            for (BatchPlugin s : pluginList)
            {
                FeatureDescriptor fd = s.descriptor().providerVersion();
                if (fd != null)
                {
                    if (availablePlugins.size() > 0
                        && !availablePlugins.remove(fd))
                    {
                        Iterator<FeatureDescriptor> available =
                            availablePlugins.iterator();
                        while (available.hasNext())
                        {
                            FeatureDescriptor candidate = available.next();
                            if (candidate.name() == null
                                || candidate.name().equals(fd.name()))
                            {
                                available.remove();
                            }
                        }
                    }
                }
            }
        }
        for (FeatureDescriptor plugin : availablePlugins)
        {
            if (plugin.getProperty("batchEntity") != null)
            {
                log.info("Installing new plug-in: \"" + plugin.name() + "\"");
                String msg = installOrUpdate(admin, plugin, false, null);
                if (msg != null)
                {
                    log.error("Error installing new plug-in \""
                        + plugin.name() + "\": " + msg);
                }
                ec.saveChanges();
            }
        }
    }


    //~ Static/instance variables .............................................

    static private String pluginRoot = null;
    static private String pluginDataRoot = null;

    private BatchPluginDescriptor descriptor;
}
