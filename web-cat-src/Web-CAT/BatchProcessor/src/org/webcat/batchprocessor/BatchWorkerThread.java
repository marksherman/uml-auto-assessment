/*==========================================================================*\
 |  $Id: BatchWorkerThread.java,v 1.7 2012/05/09 16:34:04 stedwar2 Exp $
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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import org.apache.log4j.Logger;
import org.webcat.core.Application;
import org.webcat.core.EOBase;
import org.webcat.core.FileUtilities;
import org.webcat.core.MutableDictionary;
import org.webcat.core.WCProperties;
import org.webcat.jobqueue.WorkerThread;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSTimestamp;
import er.extensions.eof.ERXFetchSpecificationBatchIterator;

//-------------------------------------------------------------------------
/**
 * A job queue worker thread for processing BatchJobs.
 *
 * @author  Tony Allevato
 * @author  Last changed by: $Author: stedwar2 $
 * @version $Revision: 1.7 $, $Date: 2012/05/09 16:34:04 $
 */
public class BatchWorkerThread extends WorkerThread<BatchJob>
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a new instance of the BatchWorkerThread class.
     *
     * @param queueEntity the queue entity
     */
    public BatchWorkerThread()
    {
        super(BatchJob.ENTITY_NAME);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Implements the logic of the job.
     */
    @Override
    protected void processJob() throws IOException
    {
        lastSuspensionInfo = null;

        JobInfo info = prepareJob();
        info.job.setProgress(0.0);

        while (!info.jobShouldDie
                && !BatchJob.STATE_END.equals(info.currentState))
        {
            if (isCancelling() || info.job.isDeletedEO())
            {
                info.jobShouldDie = true;
                break;
            }

            // TODO: implement throttling

            if (info.currentState != null && info.currentState.contains(":"))
            {
                handleNewIteration(info);
            }
            else if (info.job.isInIteration())
            {
                handleStateInsideIteration(info);
            }
            else
            {
                handleRegularState(info);
            }

            rewriteBatchProperties(info);
            info.job.setCurrentState(info.currentState);
            localContext().saveChanges();
        }

        if (!info.jobShouldDie)
        {
            // Collect any reports that were generated and flag the job as
            // completed.

            processSavedProperties(info);
            collectReports(info);
            completeJob(info);

            localContext().saveChanges();
        }

        // Wait a short amount of time for the plug-in process to complete
        // on it's own. We don't give it long; it should terminate itself
        // when the "end" state is reached or whenever it sends a "die"
        // response. If it is still running, we kill it manually before the
        // job ends.

        try
        {
            Thread.sleep(3000);
        }
        catch (InterruptedException e)
        {
            // Do nothing.
        }

        info.pluginProcess.destroy();

        // We do the clean-up here instead of overriding cancelJob because
        // we want to wait for the plug-in process to terminate before
        // deleting the result directory (it may still have open files in
        // there that would prevent us from wiping it).

        if (isCancelling())
        {
            EOEditingContext ec = localContext();

            // Delete the batch result that was generated for the job.

            ec.deleteObject(currentJob().batchResult());
            ec.saveChanges();
        }
    }


    // ----------------------------------------------------------
    @Override
    protected void resetJob()
    {
        captureAdditionalSuspensionInfo(currentJob());
        currentJob().setCurrentState(BatchJob.STATE_START);
    }


    // ----------------------------------------------------------
    private void captureAdditionalSuspensionInfo(BatchJob job)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("State job was in when suspended: ");
        buffer.append(job.currentState());
        buffer.append("\n");

        lastSuspensionInfo = buffer.toString();
    }


    // ----------------------------------------------------------
    @Override
    protected String additionalSuspensionInfo()
    {
        return lastSuspensionInfo;
    }


    // ----------------------------------------------------------
    private JobInfo prepareJob() throws IOException
    {
        JobInfo info = new JobInfo();

        BatchJob job = currentJob();
        BatchResult result = job.batchResult();

        info.job = job;
        info.result = result;
        info.currentState = job.currentState();

        // Create the working directories for the batch job.

        prepareWorkingDirectory(job);
        prepareResultDirectoryIfNecessary(job);

        // Set up the properties to pass to the plug-in script.

        info.batchProperties = new WCProperties();
        info.batchPropertiesFile = new File(result.resultDirName(),
                BatchResult.propertiesFileName());

        if (BatchJob.STATE_START.equals(info.job.currentState())
            || !info.batchPropertiesFile.exists())
        {
            initializeBatchProperties(info);
            rewriteBatchProperties(info);
        }
        else
        {
            reloadBatchProperties(info);
        }

        // Start the process.

        File workingDir = new File(job.workingDirName());
        info.batchHandler =
            BatchHandlerManager.getInstance().createHandler(
                    job.batchPlugin().batchEntity(), localContext(),
                    info.batchProperties, workingDir);

        info.pluginProcess = startPluginProcess(
                job, info.batchPropertiesFile.getPath(), workingDir);

        if (info.pluginProcess == null)
        {
            throw new NullPointerException("external process failed to run; "
                    + "was null");
        }

        info.pluginStdin = new BufferedWriter(
                new OutputStreamWriter(info.pluginProcess.getOutputStream()));
        info.pluginStdout = new BufferedReader(
                new InputStreamReader(info.pluginProcess.getInputStream()));

        // Get an iterator that points to the current item in the batch.

        info.qualifier = EOBase.accessibleBy(job.user()).and(
            job.objectQuery().qualifier());
        info.objectCount = job.objectQuery().upperBoundOfObjectCount();
        info.iterator = job.iteratorForRemainingItems(localContext());

        return info;
    }


    // ----------------------------------------------------------
    private void handleNewIteration(JobInfo info)
    {
        if (info.job.isInIteration())
        {
            suspendJob(info,
                    "Received a new iteration state transition \""
                    + info.currentState + "\" while already iterating over "
                    + "the batch");
        }

        // Set the job to begin iteration.

        String[] stateParts = info.currentState.split(":");
        info.currentState = stateParts[0];
        String stateAfterIteration = stateParts[1];

        info.job.prepareForIteration(stateAfterIteration);
    }


    // ----------------------------------------------------------
    private void handleStateInsideIteration(JobInfo info) throws IOException
    {
        String action = null;
        EOEnterpriseObject nextObject = null;

        if (info.iterator != null)
        {
            while (info.iterator.hasNext() && nextObject == null)
            {
                EOEnterpriseObject object =
                    (EOEnterpriseObject) info.iterator.next();

                info.job.incrementIndexOfNextObject();

                if (info.qualifier.evaluateWithObject(object)
                        && info.batchHandler.shouldProcessItem(object))
                {
                    nextObject = object;
                }
            }
        }

        if (nextObject == null)
        {
            info.currentState = info.job.endIteration();
        }
        else
        {
            // Ask the batch handler to set up the item, and save any
            // changes to the properties file. Then signal the script and
            // wait for a response.

            info.batchHandler.setUpItem(nextObject);
            action = signalPlugin(info, info.currentState);
            info.batchHandler.tearDownItem(nextObject);

            if ("continue".equals(action))
            {
                if (info.iterator == null || !info.iterator.hasNext())
                {
                    // If there are no more objects to process, transition
                    // to the post-iteration state.

                    info.currentState = info.job.endIteration();
                }
            }
            else if ("break".equals(action))
            {
                info.currentState = info.job.endIteration();
            }
            else if ("die".equals(action))
            {
                info.jobShouldDie = true;
            }
            else
            {
                suspendJob(info,
                        "Received invalid response \""
                        + action + "\" from plug-in during iteration; "
                        + "valid responses are \"continue\", \"break\", and "
                        + "\"die\"");
            }
        }
    }


    // ----------------------------------------------------------
    private void handleRegularState(JobInfo info) throws IOException
    {
        info.currentState = signalPlugin(info, info.currentState);
    }


    // ----------------------------------------------------------
    private void completeJob(JobInfo info)
    {
        info.job.setBatchResultRelationship(null);

        info.result.setCompletedTime(new NSTimestamp());
        info.result.setIsComplete(true);
    }


    // ----------------------------------------------------------
    private void prepareWorkingDirectory(BatchJob job)
    {
        // Create the working directory for the user.
        File workingDir = new File(job.workingDirName());
        if (workingDir.exists())
        {
            FileUtilities.deleteDirectory(workingDir);
        }

        workingDir.mkdirs();
    }


    // ----------------------------------------------------------
    private void prepareResultDirectoryIfNecessary(BatchJob job)
    {
        // Only wipe and create the result directory if the job is starting
        // fresh.

        if (BatchJob.STATE_START.equals(job.currentState()))
        {
            File resultDir = new File(job.batchResult().resultDirName());
            if (resultDir.exists())
            {
                FileUtilities.deleteDirectory(resultDir);
            }

            resultDir.mkdirs();
        }
    }


    // ----------------------------------------------------------
    @SuppressWarnings("unchecked")
    private void initializeBatchProperties(JobInfo info)
    {
        BatchJob job = info.job;
        WCProperties properties = info.batchProperties;

        BatchPlugin batchPlugin = job.batchPlugin();

        // Re-write the properties file
        properties.addPropertiesFromDictionaryIfNotDefined(
            Application.wcApplication().subsystemManager().pluginProperties());

        properties.addPropertiesFromDictionaryIfNotDefined(
                batchPlugin.globalConfigSettings());
        properties.addPropertiesFromDictionaryIfNotDefined(
                batchPlugin.defaultConfigSettings());
        properties.addPropertiesFromDictionary(
                job.configSettings());

        properties.setProperty("userName", job.user().userName());
        properties.setProperty("workingDir", job.workingDirName());
        properties.setProperty("resultDir", job.batchResult().resultDirName());
        properties.setProperty("scriptHome", batchPlugin.dirName());
        properties.setProperty("pluginHome", batchPlugin.dirName());
        properties.setProperty("scriptData", BatchPlugin.pluginDataRoot());
        properties.setProperty("pluginData", BatchPlugin.pluginDataRoot());
        properties.setProperty("frameworksBaseURL",
            Application.wcApplication().frameworksBaseURL());
        if (log.isDebugEnabled())
        {
            log.debug("initializeBatchProperties():\n--------------------");
            StringWriter out = new StringWriter();
            try
            {
                info.batchProperties.store(out, "Properties contents:");
            }
            catch (Exception e)
            {
                log.warn("Exception writing properties file", e);
            }
            log.debug(out);
            log.debug("--------------------\n");
        }
    }


    // ----------------------------------------------------------
    private void reloadBatchProperties(JobInfo info)
    {
        info.batchProperties.clear();
        info.batchProperties.load(info.batchPropertiesFile.getAbsolutePath());
        if (log.isDebugEnabled())
        {
            log.debug("reloadBatchProperties():\n--------------------");
            StringWriter out = new StringWriter();
            try
            {
                info.batchProperties.store(out, "Properties contents:");
            }
            catch (Exception e)
            {
                log.warn("Exception writing properties file", e);
            }
            log.debug(out);
            log.debug("--------------------\n");
        }
    }


    // ----------------------------------------------------------
    private void rewriteBatchProperties(JobInfo info)
        throws IOException
    {
        if (log.isDebugEnabled())
        {
            log.debug("rewriteBatchProperties():\n--------------------");
            StringWriter out = new StringWriter();
            try
            {
                info.batchProperties.store(out, "Properties contents:");
            }
            catch (Exception e)
            {
                log.warn("Exception writing properties file", e);
            }
            log.debug(out);
            log.debug("--------------------\n");
        }
        BufferedOutputStream out = new BufferedOutputStream(
                new FileOutputStream(info.batchPropertiesFile));
        info.batchProperties.store(out,
                "Web-CAT batch plug-in configuration properties");
        out.close();
    }


    // ----------------------------------------------------------
    /**
     * Create result property objects from properties in the batch properties
     * file.
     */
    private void processSavedProperties(JobInfo info)
    {
        // Pull any properties that are prefixed with "saved." into
        // ResultOutcome objects
        final String SAVED_PROPERTY_PREFIX = "saved.";

        for (Object propertyAsObj : info.batchProperties.keySet())
        {
            String property = (String) propertyAsObj;

            if (property.startsWith(SAVED_PROPERTY_PREFIX))
            {
                String actualName = property.substring(
                        SAVED_PROPERTY_PREFIX.length());
                Object value = info.batchProperties.valueForKey(property);

                if (value != null)
                {
                    if (value instanceof NSArray)
                    {
                        NSArray<?> array = (NSArray<?>) value;
                        int index = 0;

                        for (Object elem : array)
                        {
                            createResultProperty(info, index, actualName, elem);
                            index++;
                        }
                    }
                    else
                    {
                        createResultProperty(info, null, actualName, value);
                    }
                }
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Creates a single result outcome from the value of a property in the
     * grading properties file. If the value is a dictionary, then it is
     * stored in the outcome directly; if it is a scalar value, then it is
     * stored in the outcome contents as a one-element dictionary with the key
     * named "value".
     *
     * @param job
     * @param submissionResult
     * @param index
     * @param tag
     * @param value
     */
    private void createResultProperty(JobInfo info,
                                      Integer index,
                                      String tag,
                                      Object value)
    {
        NSDictionary<String, Object> contents;

        if (!(value instanceof NSDictionary))
        {
            contents = new NSDictionary<String, Object>(value, "value");
        }
        else
        {
            @SuppressWarnings("unchecked")
            NSDictionary<String, Object> theContents =
                (NSDictionary<String, Object>) value;
            contents = theContents;
        }

        BatchResultProperty resultProp = BatchResultProperty.create(
                localContext(), false);
        resultProp.setTag(tag);
        resultProp.setContents(new MutableDictionary(contents));

        if (index != null)
        {
            resultProp.setIndex(index);
        }

        resultProp.setBatchResultRelationship(info.result);
    }


    // ----------------------------------------------------------
    private void collectReports(JobInfo info)
    {
        WCProperties properties = info.batchProperties;

        int numReports = properties.intForKey("numReports");
        for (int i = 1; i <= numReports; i++)
        {
            String attributeBase = "report" + i + ".";
            String fileName = properties.getProperty(attributeBase + "file");
            String title = properties.getProperty(attributeBase + "title");
            String mimeType = properties.getProperty(
                    attributeBase + "mimeType");

            boolean inline =
                ((properties.getProperty(attributeBase + "inline") == null)
                  ? true : properties.booleanForKey(attributeBase + "inline"));

            boolean collapsed =
                ((properties.getProperty(attributeBase + "collapsed") == null)
                  ? false : properties.booleanForKey(attributeBase + "collapsed"));

            String to = properties.getProperty(attributeBase + "to");
            BatchFeedbackRecipient recipient =
                BatchFeedbackRecipient.recipientFromPropertyValue(to);

            String loc = properties.getProperty(attributeBase + "location");
            BatchFeedbackLocation location =
                BatchFeedbackLocation.locationFromPropertyValue(loc);

            BatchFeedbackSection section = BatchFeedbackSection.create(
                    localContext(), collapsed, !inline);
            section.setBatchResultRelationship(info.result);
            section.setFileName(fileName);
            section.setLocation(location);
            section.setMimeType(mimeType);
            section.setOrder(i);
            section.setRecipients(recipient);
            section.setTitle(title);
        }
    }


    // ----------------------------------------------------------
    private Process startPluginProcess(BatchJob job, String args, File cwd)
    throws IOException
    {
        BatchPlugin plugin = job.batchPlugin();
        plugin.reinitializeConfigAttributesIfNecessary();

        File stderr = new File(job.batchResult().resultDirName(), "stderr.txt");

        args = args + " 2> " + stderr.getPath();

        return plugin.execute(args, cwd);
    }


    // ----------------------------------------------------------
    private String signalPlugin(JobInfo info, String newState)
    throws IOException
    {
        info.currentState = newState;

        // Update certain properties that are used on each state transition.

        double iterationProgress = (info.objectCount > 0)
            ? info.job.indexOfNextObject()  / (double)info.objectCount
            : 1.0;
        info.batchProperties.setProperty("batch.iterationProgress",
            Double.toString(iterationProgress));

        rewriteBatchProperties(info);

        // Write the next state to the plug-in's input stream and then wait for
        // it's response on the output stream.

        info.pluginStdin.write(newState);
        info.pluginStdin.write('\n');
        info.pluginStdin.flush();

        String response = info.pluginStdout.readLine();

        // Update the progress of the job using the information provided by the
        // plug-in.

        reloadBatchProperties(info);

        boolean needToSave = false;

        if (info.batchProperties.containsKey("batch.jobProgress"))
        {
            double progress =
                info.batchProperties.doubleForKey("batch.jobProgress");
            info.job.setProgress(progress);

            needToSave = true;
        }
        else
        {
            info.job.setProgress(iterationProgress);
        }

        if (info.batchProperties.containsKey("batch.jobProgressMessage"))
        {
            String message =
                info.batchProperties.stringForKey("batch.jobProgressMessage");
            info.job.setProgressMessage(message);

            needToSave = true;
        }

        if (needToSave)
        {
            localContext().saveChanges();
        }

        return response;
    }


    // ----------------------------------------------------------
    private void suspendJob(JobInfo info, String reason)
    {
        // TODO send a notification message
        log.error(reason);

        captureAdditionalSuspensionInfo(info.job);

        info.job.setSuspensionReason(reason);
        info.job.setIsReady(false);
        info.job.setCurrentState(BatchJob.STATE_START);
        localContext().saveChanges();
        info.jobShouldDie = true;
    }


    //~ Private classes .......................................................

    // ----------------------------------------------------------
    private class JobInfo
    {
        BatchJob          job;
        BatchResult       result;
        WCProperties      batchProperties;
        File              batchPropertiesFile;
        Process           pluginProcess;
        BufferedWriter    pluginStdin;
        BufferedReader    pluginStdout;
        BatchHandlerProxy batchHandler;

        EOQualifier       qualifier;
        int               objectCount;
        ERXFetchSpecificationBatchIterator iterator;

        String            currentState;
        boolean           jobShouldDie;
    }


    //~ Static/instance variables .............................................

    private String lastSuspensionInfo;

    private static final Logger log = Logger.getLogger(BatchWorkerThread.class);
}
