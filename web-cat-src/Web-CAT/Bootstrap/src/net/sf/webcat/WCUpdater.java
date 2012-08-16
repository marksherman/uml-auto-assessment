/*==========================================================================*\
 |  $Id: WCUpdater.java,v 1.2 2011/05/27 15:30:56 stedwar2 Exp $
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

package net.sf.webcat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

//-------------------------------------------------------------------------
/**
 * This class runs and handles the update creation for webcat. It runs
 * a background process which will continually check for updates.
 *
 * @author Travis Bale
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2011/05/27 15:30:56 $
 */
public class WCUpdater
{
    //~ Instance/static variables .............................................

    private static WCUpdater instance = null;

	private File downloadDir;
	private File stagingDir;
    private File updateDir;
    private File frameworkDir;
    private File mainBundle;

    private Map<File, SubsystemUpdater>   subsystems  =
        new HashMap<File, SubsystemUpdater>();
    private Map<String, SubsystemUpdater> subsystemsByName =
        new HashMap<String, SubsystemUpdater>();
    private Map<String, Condition>	updateFileConditions =
    	new HashMap<String, Condition>();

    private static final String FRAMEWORK_SUBDIR1 =
        "/Contents/Frameworks/Library/Frameworks";
    private static final String FRAMEWORK_SUBDIR2 =
        "/Contents/Library/Frameworks";

    private static final String DOWNLOAD_SUBDIR = "pending-downloads";
    private static final String STAGING_SUBDIR = "complete-downloads";
    private static final String UPDATE_SUBDIR = "pending-updates";


    //~ Public Constants ......................................................

    /**
     * The possible states for an update that is to be downloaded.
     */
    public enum Condition {
        DOWNLOAD_PENDING,
        DOWNLOAD_COMPLETE,
        UPDATE_PENDING,
        UP_TO_DATE,
        UPDATE_IS_AVAILABLE,
        UNAVAILABLE
    }


    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Constructor.
     *
     * Cannot be called directly. Must use getInstance().
     */
    private WCUpdater()
    {
    	//Exists to prevent instantiation
    }


    // ----------------------------------------------------------
    /**
     * Gets an instance of this updater.
     * @return An instance of the updater
     */
    public static WCUpdater getInstance()
    {
    	if(instance == null)
    	{
    		instance = new WCUpdater();
    	}

    	return instance;
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Accessor for the download directory
     * @return the download directory
     */
    public File getDownloadDir()
    {
    	return downloadDir;
    }


    // ----------------------------------------------------------
    /**
     * Accessor for the staging directory
     * @return the staging directory
     */
    public File getStagingDir()
    {
    	return stagingDir;
    }


    // ----------------------------------------------------------
    /**
     * Accessor for the update directory
     * @return the update directory
     */
    public File getUpdateDir()
    {
    	return updateDir;
    }


    // ----------------------------------------------------------
    /**
     * Accessor for the framework directory
     * @return the framework directory
     */
    public File getFrameworkDir()
    {
    	return frameworkDir;
    }


    // ----------------------------------------------------------
    /**
     * Accessor for the main bundle
     * @return the main bundle
     */
    public File getMainBundle()
    {
    	return mainBundle;
    }


    // ----------------------------------------------------------
    /**
     * Schedules and starts the update.
     */
    public void startBackgroundUpdaterThread(long delay, long period)
    {
		TimerTask updateTask = new TimerTask()
		{
			public void run()
			{
				getInstance().createUpdate();
				FeatureProvider.refreshProviderRegistry();
				getInstance().refreshSubsystemUpdaters();
			}
		};

		Timer timer = new Timer();
		timer.schedule(updateTask, delay, period);
    }


    // ----------------------------------------------------------
    /**
     * Sets up the update directories and sets the current file conditions.
     * @param webInfDir The WEB-INF directory file
     */
    public void setup(File webInfDir)
    {
    	//Create the Directories needed for the Updater
    	downloadDir = new File(webInfDir, DOWNLOAD_SUBDIR);
		stagingDir = new File(webInfDir, STAGING_SUBDIR);
        updateDir = new File(webInfDir, UPDATE_SUBDIR);

        if (webInfDir.isDirectory())
        {
            for (File bundleSearchDir : webInfDir.listFiles())
            {
                if (bundleSearchDir.isDirectory()
                    && bundleSearchDir.getName().endsWith(".woa"))
                {
                    mainBundle = new File(bundleSearchDir, "Contents");
                    frameworkDir = new File(
                        bundleSearchDir.getAbsolutePath()
                        + FRAMEWORK_SUBDIR1);
                    if (!frameworkDir.exists())
                    {
                        frameworkDir = new File(
                            bundleSearchDir.getAbsolutePath()
                            + FRAMEWORK_SUBDIR2);
                    }
                    break;
                }
            }
        }
    	if (!downloadDir.exists())
    	{
    		downloadDir.mkdirs();
    	}

        if(!stagingDir.exists())
        {
        	stagingDir.mkdirs();
        }

        if (!updateDir.exists())
        {
            updateDir.mkdirs();
        }

        //Load the Conditions
        for(File jar : downloadDir.listFiles())
        {
        	updateFileConditions.put(
        	    jar.getName(), Condition.DOWNLOAD_PENDING);
        }

        for(File jar : stagingDir.listFiles())
        {
        	updateFileConditions.put(
        	    jar.getName(), Condition.DOWNLOAD_COMPLETE);
        }

        for(File jar : updateDir.listFiles())
        {
        	updateFileConditions.put(
        	    jar.getName(), Condition.UPDATE_PENDING);
        }
    }


    // ----------------------------------------------------------
    /**
     * Access the collection of subsystems in this application.
     * @return a collection of {@link SubsystemUpdater} objects representing
     *     the available subsystems
     */
    public Collection<SubsystemUpdater> subsystems()
    {
    	return subsystems.values();
    }


    // ----------------------------------------------------------
    /**
     * Get the {@link SubsystemUpdater} for the specified subsystem location.
     * Creates a new updater on demand, if necessary.
     * @param dir the subsystem location to look up
     * @return the corresponding updater
     */
    public SubsystemUpdater getUpdaterFor(File dir)
    {
        SubsystemUpdater updater = null;
        if (dir != null)
        {
            updater = subsystems.get(dir);
            if (updater == null)
            {
                updater = new SubsystemUpdater(dir);
                subsystems.put(dir, updater);
                if (updater.name() != null)
                {
                    subsystemsByName.put(updater.name(), updater);
                }
            }
        }
        return updater;
    }


    // ----------------------------------------------------------
    /**
     * Returns the condition of the specified filename.
     *
     * @param filename The update file's name
     * @return The condition of the specified file.
     */
    public Condition getFileConditionFor(String filename)
    {
        return updateFileConditions.get(filename);
    }


    // ----------------------------------------------------------
    /**
     * Log an informational message. This implementation sends output
     * to {@link System#out}.
     * @param msg the message to log.
     */
    public static void logInfo(String msg)
    {
        System.out.println( msg );
    }


    // ----------------------------------------------------------
    /**
     * Log an error message.  This implementation sends output
     * to {@link System#out}, but provides a hook so that subclasses
     * can use Log4J (we don't use that here, so that the Log4J library
     * can be dynamically updatable through subsystems).
     * @param msg the message to log
     */
    public static void logError(Class<?> reference, String msg)
    {
        String className = reference.getName();
        int pos = className.lastIndexOf('.');
        if (pos >= 0)
        {
            className = className.substring(pos + 1);
        }
        System.out.println(className + ": ERROR: " + msg);
    }


    // ----------------------------------------------------------
    /**
     * Log an error message.  This implementation sends output
     * to {@link System#out}, but provides a hook so that subclasses
     * can use Log4J (we don't use that here, so that the Log4J library
     * can be dynamically updatable through subsystems).
     * @param msg the message to log
     * @param exception an optional exception that goes with the message
     */
    public static void logError(
        Class<?> reference, String msg, Throwable exception)
    {
        logError(reference, msg);
        System.out.println(exception);
    }


    //~ Private Methods .......................................................

    // ----------------------------------------------------------
	/**
     * Creates an update by downloading the newest versions of the
     * subsystems and preparing the update directory with an update
     * that can be applied.
     */
    private void createUpdate()
    {
    	logInfo("Download all updates");

    	// Download the updates
        boolean downloadSuccessful = downloadNewUpdates();
        FileUtilities.deleteOlderFiles(stagingDir);

        logInfo("\nMoving updates that can be applied to pending-updates "
            + "directory.");

        // Prepare the update
        if(downloadSuccessful)
        {
           prepareFullUpdate();
        }
        else
        {
           preparePartialUpdate();
        }

        FileUtilities.deleteOlderFiles(updateDir);
    }


    // ----------------------------------------------------------
	/**
     * If automatic updates are turned on, scan all current subsystems and
     * download any new versions of update files that are available.
     * @param aFrameworkDir The directory where all subsystems are located
     * @param mainBundle The main bundle location
     * @return true if no errors occurred while downloading
     */
    private boolean downloadNewUpdates()
    {
    	boolean successfulDownload = true;

        for (File subdir : frameworkDir.listFiles())
        {
            if(!downloadUpdateIfNecessary(getUpdaterFor(subdir)))
            {
            	successfulDownload = false;
            }
        }

        // Now handle the application update, if available
        if(!downloadUpdateIfNecessary( getUpdaterFor(mainBundle) ))
        {
        	successfulDownload = false;
        }

        // Now check through existing subsystems and check for any required
        // subsystems that are not yet installed
        for (SubsystemUpdater thisUpdater : subsystems.values())
        {
            String requires = thisUpdater.getProperty("requires");

            if (thisUpdater.providerVersion() != null)
            {
                requires =
                    thisUpdater.providerVersion().getProperty("requires");
            }
            else
            {
            	logError(getClass(), "Unable to read from provider.");
            }

            if (requires != null)
            {
                for (String requiredSubsystem : requires.split( ",\\s*" ))
                {
                    if (!subsystemsByName.containsKey(requiredSubsystem))
                    {
                        // A required subsystem is not present, so find it
                        // and download it
                        logInfo( "Installed subsystem "
                            + thisUpdater.name() + " requires subsystem "
                            + requiredSubsystem
                            + ", which is not installed.");
                        // First, look in the subsystem's provider
                        FeatureDescriptor newSubsystem;
						try
						{
							newSubsystem = thisUpdater.provider()
							    .subsystemDescriptor(requiredSubsystem);
						}
						catch (IOException e)
						{
							newSubsystem = null;
						}
                        if (newSubsystem == null)
                        {
                            // OK, look in all providers for it
                            for (FeatureProvider fp :
                                FeatureProvider.providers())
                            {
                                newSubsystem = fp.subsystemDescriptor(
                                    requiredSubsystem);
                                if (newSubsystem != null)
                                {
                                    break;
                                }
                            }
                        }
                        if (newSubsystem == null)
                        {
                            logInfo("Cannot identify provider for subsystem "
                                + requiredSubsystem);

                            successfulDownload = false;
                        }
                        else
                        {
                            try
                            {
                            	String filename = newSubsystem.name + "_"
                            	    + newSubsystem.currentVersion() + ".jar";

                            	Condition fileCondition =
                            	    updateFileConditions.get(filename);

                            	if (fileCondition !=
                            	        Condition.DOWNLOAD_COMPLETE
                            	    && fileCondition !=
                            	        Condition.UPDATE_PENDING)
                        		{
                        			updateFileConditions.put(
                        			    filename, Condition.DOWNLOAD_PENDING);
                        			newSubsystem.downloadTo(
                        			    downloadDir, stagingDir);
                        			updateFileConditions.put(
                        			    filename, Condition.DOWNLOAD_COMPLETE);
                        		}
                        		else
                        		{
                        			logInfo(newSubsystem.name
                        			    + " is already downloaded.");
                        		}
                            }
                            catch (IOException c)
                            {
                            	successfulDownload = false;
                            }
                        }
                    }
                }
            }
        }

        return successfulDownload;
    }


    // ----------------------------------------------------------
    /**
     * Check for any updates for the given subsystem, and download them.
     * @param updater The {@link SubsystemUpdater} to download for
     * @return true if download is successful
     */
    private boolean downloadUpdateIfNecessary(SubsystemUpdater updater)
    {
        try
        {
        	FeatureDescriptor latest = updater.providerVersion();
        	if (latest != null)
        	{
        		String filename =
        		    latest.name + "_" + latest.currentVersion() + ".jar";
        		Condition fileCondition = updateFileConditions.get(filename);

        		if (fileCondition != Condition.DOWNLOAD_COMPLETE
        		    && fileCondition != Condition.UPDATE_PENDING)
        		{
        			updateFileConditions.put(
        			    filename, Condition.DOWNLOAD_PENDING);
        			if (updater.downloadUpdateIfNecessary(
        			    downloadDir, stagingDir))
        			{
        				updateFileConditions.put(
        				    filename, Condition.DOWNLOAD_COMPLETE);
        			}
        			else
        			{
        				updateFileConditions.put(
        				    filename, Condition.UP_TO_DATE);
        			}
        		}
        		else
        		{
        			logInfo(latest.name + " is already downloaded.");
        		}
        	}
		}
        catch (IOException e)
        {
			logError(getClass(), "Error occured during download." , e);
			return false;
		}

        return true;
    }


    // ----------------------------------------------------------
    /**
     * Prepares for the update by moving all files from the staging
     * directory to the update directory.
     */
    private void prepareFullUpdate()
    {
    	if (stagingDir.exists() && stagingDir.isDirectory())
        {
    		for (File jar : stagingDir.listFiles())
            {
                for (String extension :
                    SubsystemUpdater.JAVA_ARCHIVE_EXTENSIONS)
                {
                    if (jar.getName().endsWith(extension))
                    {
                    	if(!jar.renameTo(new File(updateDir, jar.getName())))
                		{
                    		logError(getClass(), "Unable to move "
                    		    + jar.getName()
                    		    + " from "
                    		    + stagingDir.getAbsolutePath()
                    		    + " to "
                    		    + updateDir.getAbsolutePath());
                		}
                		else
                		{
                			updateFileConditions.put(
                			    jar.getName(), Condition.UPDATE_PENDING);
                			logInfo("Moving "
                			    + jar.getName()
                			    + " from "
                			    + stagingDir.getAbsolutePath()
                			    + " to "
                			    + updateDir.getAbsolutePath());
                		}
                    }
                }
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Prepares for the update by moving all files from the staging
     * directory to the update directory. It only moves files that do
     * not have requirements that have not been downloaded.
     */
    private void preparePartialUpdate()
    {
    	if (stagingDir.exists() && stagingDir.isDirectory())
        {
    		File[] stagingFiles = stagingDir.listFiles();
    		for (int i = 0; i < stagingFiles.length; i++)
            {
    			File jar = stagingFiles[i];

    			if (jar != null)
    			{
    				for (String extension :
    					SubsystemUpdater.JAVA_ARCHIVE_EXTENSIONS)
    				{
    					if (jar.getName().endsWith(extension))
    					{
    						String subsystemName = jar.getName();
    						subsystemName = subsystemName.substring(
    						    0, subsystemName.indexOf("_"));
    			    		ArrayList<String> requires =
    			    		    getRequirements(subsystemName);
    			    		ArrayList<Integer> positions =
    			    		    new ArrayList<Integer>();

    			    		if (requires == null)
    			    		{
    			    			continue;
    			    		}

    			    		boolean canAddUpdate = true;
    			    		for (String requirement : requires)
    			    		{
    			    			Condition fileCondition =
    			    				updateFileConditions.get(requirement);

    			    			if (fileCondition ==
    			    			    Condition.DOWNLOAD_COMPLETE)
    			    			{
    			    				for(int j = i + 1; j < stagingFiles.length;
    			    				    j++)
        			    			{
    			    					String filename =
    			    					    stagingFiles[j].getName();

    			    					if (requirement.equals(filename))
        			    				{
    			    						positions.add(j);
        			    				}
        			    			}
    			    			}
    			    			else if (
    			    			    fileCondition != Condition.UPDATE_PENDING
    			    			    && fileCondition != Condition.UP_TO_DATE)
    			    			{
    			    				canAddUpdate = false;
    			    				break;
    			    			}
    			    		}

    			    		//Move file and requirements to updateDir
    			    		if (canAddUpdate)
    			    		{
    			    			if (!jar.renameTo(new File(
    			    			    updateDir, jar.getName())))
    	                		{
    	                    		logError(getClass(), "Unable to move "
    	                    		    + jar.getName()
    	                    		    + " from "
    	                    		    + stagingDir.getAbsolutePath()
    	                    		    + " to "
    	                    		    + updateDir.getAbsolutePath());
    	                		}
    	                		else
    	                		{
    	                			updateFileConditions.put(
    	                			    jar.getName(),
    	                			    Condition.UPDATE_PENDING);
    	                			logInfo("Moving "
    	                			    + jar.getName()
    	                			    + " from "
    	                			    + stagingDir.getAbsolutePath()
    	                			    + " to "
    	                			    + updateDir.getAbsolutePath());
    	                		}

    			    			stagingFiles[i] = null;
    			    			for (int pos : positions)
    			    			{
    			    				if (stagingFiles[pos].renameTo(
    			    				    new File(updateDir,
    			    				        stagingFiles[pos].getName())))
    		                		{
    		                    		logError(getClass(), "Unable to move "
    		                    		    + stagingFiles[pos].getName()
    		                    		    + " from "
    		                    		    + stagingDir.getAbsolutePath()
    		                    		    + " to "
    		                    		    + updateDir.getAbsolutePath());
    		                		}
    		                		else
    		                		{
    		                			updateFileConditions.put(
    		                			    stagingFiles[pos].getName(),
    		                			    Condition.UPDATE_PENDING);
    		                			logInfo("Moving "
    		                			    + stagingFiles[pos].getName()
    		                			    + " from "
    		                			    + stagingDir.getAbsolutePath()
    		                			    + " to "
    		                			    + updateDir.getAbsolutePath());
    		                		}

    			    				stagingFiles[pos] = null;
    			    			}
    			    		}
    					}
    				}
    			}
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Refresh the subsystems collection so that it reflects the new
     * updates (intended to be called after downloading/applying pending
     * updates).
     * @param aFrameworkDir The directory where all subsystems are located
     * @param mainBundle The main bundle location
     */
    private void refreshSubsystemUpdaters()
    {
        // Clear out old values
        subsystems = new HashMap<File, SubsystemUpdater>();
        subsystemsByName = new HashMap<String, SubsystemUpdater>();
        updateFileConditions = new HashMap<String, Condition>();

        // Look up the updater for each framework
        for (File dir : frameworkDir.listFiles())
        {
            getUpdaterFor(dir);
        }

        // Now create the updater for the main bundle
        getUpdaterFor(mainBundle);

        // Load file conditions
        for (File jar : downloadDir.listFiles())
        {
        	updateFileConditions.put(
        	    jar.getName(), Condition.DOWNLOAD_PENDING);
        }

        for (File jar : stagingDir.listFiles())
        {
        	updateFileConditions.put(
        	    jar.getName(), Condition.DOWNLOAD_COMPLETE);
        }

        for (File jar : updateDir.listFiles())
        {
        	updateFileConditions.put(jar.getName(), Condition.UPDATE_PENDING);
        }
    }


    // ----------------------------------------------------------
    /**
     * Gets a list of all required subsystems need by the specified subsystem.
     *
     * @param subsystem The name of the subsystem who's requirements we
     *                  are checking.
     * @return A list of a required subsystems. Returns null if no
     *         connection can be made with the provider.
     */
    private ArrayList<String> getRequirements(String subsystem)
    {
    	SubsystemUpdater updater = subsystemsByName.get(subsystem);
    	ArrayList<String> requires = new ArrayList<String>();

    	if (updater != null)
    	{
    		String required;
            if (updater.providerVersion() != null)
            {
				required = updater.providerVersion().getProperty("requires");
			}
            else
			{
				return null;
			}

    		if (required != null)
    		{
    			for (String requiredSubsystem : required.split( ",\\s*" ))
    			{
    				ArrayList<String> temp =
    				    getRequirements(requiredSubsystem);

    				if (temp == null)
    				{
    					return null;
    				}

    				SubsystemUpdater requiredUpdater =
    				    subsystemsByName.get(requiredSubsystem);
    				if (requiredUpdater == null)
    				{
    					return null;
    				}
    				requiredSubsystem +=
    				    "_" + requiredUpdater.currentVersion() + ".jar";

    				requires.addAll(temp);

    				if (!requires.contains(requiredSubsystem))
    				{
    					requires.add(requiredSubsystem);
    				}
    			}
    		}
    	}
    	else
    	{
    		return null;
    	}

    	return requires;
    }
}
