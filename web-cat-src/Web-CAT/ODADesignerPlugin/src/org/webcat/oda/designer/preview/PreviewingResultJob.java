/*==========================================================================*\
 |  $Id: PreviewingResultJob.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

package org.webcat.oda.designer.preview;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.webcat.oda.designer.DesignerActivator;
import org.webcat.oda.designer.i18n.Messages;
import org.webcat.oda.designer.util.WOActionDispatcher;
import org.webcat.oda.designer.util.WOActionResponse;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: PreviewingResultJob.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class PreviewingResultJob extends Job
{
    // ------------------------------------------------------------------------
    /**
     *
     * @param jobName
     * @param url
     * @param username
     * @param password
     * @param maxRecords
     * @param timeout
     * @param dataSetUuid
     * @param entityType
     * @param expressions
     */
    public PreviewingResultJob(String jobName, String url, String username,
            String password, int maxRecords, int timeout, String dataSetUuid,
            String entityType, String[] expressions)
    {
        super(jobName);

        this.serverUrl = url;
        this.username = username;
        this.password = password;
        this.maxRecords = maxRecords;
        this.timeout = timeout;
        this.dataSetUuid = dataSetUuid;
        this.entityType = entityType;
        this.expressions = expressions;

        StringBuilder builder = new StringBuilder();

        builder.append(expressions[0]);

        for (int i = 1; i < expressions.length; i++)
        {
            builder.append("%===%");
            builder.append(expressions[i]);
        }

        this.expressionParameterString = builder.toString();

        currentBatchRetrieved = false;
        currentBatchLock = new Object();
    }


    // ------------------------------------------------------------------------
    /**
     *
     */
    @Override
    protected IStatus run(IProgressMonitor monitor)
    {
        monitor.beginTask(Messages.PREVIEW_JOB_OBTAINING_DATA,
                maxRecords);

        IStatus status = performRetrieval(monitor);

        if (status.isOK() && retrievedRows != null)
        {
            DesignerActivator.getDefault().getPreviewCache().movePendingToCached(
                    dataSetUuid, retrievedRows);
        }
        else
        {
            DesignerActivator.getDefault().getPreviewCache().cancelPending(
                    dataSetUuid);
        }

        monitor.done();
        return status;
    }


    private IStatus performRetrieval(IProgressMonitor monitor)
    {
        IStatus status = Status.OK_STATUS;

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (timeout * 1000);

        // Open a connection and initialize the retrieval with a query.
        status = startRetrieval(monitor);
        if (!status.isOK())
        {
            return status;
        }

        if (endTime != startTime && System.currentTimeMillis() > endTime)
        {
            return Status.OK_STATUS;
        }

        retrievedRows = new ArrayList<Object[]>();

        boolean hasNext = (lastBatchCount > 0);

        while (hasNext)
        {
            if (monitor.isCanceled())
            {
                cancelRetrieval();

                return Status.CANCEL_STATUS;
            }

            Object[] row = new Object[expressions.length];
            retrievedRows.add(row);

            for (int j = 0; j < expressions.length; j++)
            {
                row[j] = currentBatchValues.get(currentBatchIndex)[j];
            }

            monitor.worked(1);

            if (endTime != startTime && System.currentTimeMillis() > endTime)
            {
                return Status.OK_STATUS;
            }

            hasNext = (retrievedRows.size() < maxRecords)
                    && moveToNextRow(monitor);
        }

        if (endTime != startTime && System.currentTimeMillis() > endTime)
        {
            return Status.OK_STATUS;
        }

        return status;
    }


    private IStatus startRetrieval(IProgressMonitor monitor)
    {
        Hashtable<String, String> params = new Hashtable<String, String>();
        params.put("entityType", entityType); //$NON-NLS-1$
        params.put("expressions", expressionParameterString); //$NON-NLS-1$
        params.put("username", username); //$NON-NLS-1$
        params.put("password", password); //$NON-NLS-1$
        params.put("timeout", Integer.toString(timeout)); //$NON-NLS-1$

        try
        {
            StringWriter stringWriter = new StringWriter();
            BufferedWriter writer = new BufferedWriter(stringWriter);
            DesignerActivator.getDefault().getPreviewQueryManager().writeQuery(
                    dataSetUuid, writer);
            writer.flush();
            params.put("query", stringWriter.toString()); //$NON-NLS-1$
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        WOActionDispatcher dispatcher = new WOActionDispatcher(serverUrl);
        WOActionResponse response = dispatcher.send(
                "designerPreview/startRetrieval", null, params); //$NON-NLS-1$

        try
        {
            if (response.status().isOK())
            {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(response.stream()));

                previewSessionId = reader.readLine();

                if (previewSessionId.equals("!!! ERROR")) //$NON-NLS-1$
                {
                    return getStatusFromReader(reader);
                }
            }
        }
        catch (IOException e)
        {
            return new Status(IStatus.ERROR, DesignerActivator.PLUGIN_ID, e
                    .getMessage(), e);
        }
        finally
        {
            response.close();
        }

        IStatus status = cancelableGetNextBatch(monitor);
        if (!status.isOK())
        {
            return status;
        }

        return Status.OK_STATUS;
    }


    private IStatus getStatusFromReader(BufferedReader reader)
    {
        StringBuilder message = new StringBuilder();
        String line;

        try
        {
            while ((line = reader.readLine()) != null)
                message.append(line);
        }
        catch (IOException e)
        {
            message
                    .append(Messages.PREVIEW_JOB_BAD_RESPONSE);
        }

        return new Status(IStatus.ERROR, DesignerActivator.PLUGIN_ID, message
                .toString());
    }


    private IStatus cancelRetrieval()
    {
        WOActionDispatcher dispatcher = new WOActionDispatcher(serverUrl);
        WOActionResponse response = dispatcher.send(
                "designerPreview/cancelRetrieval", previewSessionId, null); //$NON-NLS-1$
        response.close();

        return Status.CANCEL_STATUS;
    }


    private IStatus cancelableGetNextBatch(IProgressMonitor monitor)
    {
        IStatus status = Status.OK_STATUS;

        synchronized (currentBatchLock)
        {
            currentBatchRetrieved = false;
        }

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                getNextBatch();
            }
        };

        thread.start();
        boolean alreadyCanceled = false;

        // TODO needs to be fixed -- result set cancellation NOT the same as
        // job cancellation

        while (!isCurrentBatchRetrieved())
        {
            try
            {
                Thread.sleep(500);
            }
            catch (InterruptedException e)
            {
                // Ignore exception.
            }

            if (!alreadyCanceled && monitor.isCanceled())
            {
                status = cancelRetrieval();
            }
        }

        if (!alreadyCanceled)
            status = currentBatchRetrievalStatus;

        return status;
    }


    private boolean isCurrentBatchRetrieved()
    {
        synchronized (currentBatchLock)
        {
            return currentBatchRetrieved;
        }
    }


    private void getNextBatch()
    {
        WOActionDispatcher dispatcher = new WOActionDispatcher(serverUrl);
        WOActionResponse response = dispatcher.send(
                "designerPreview/retrieveNextBatch", previewSessionId, null); //$NON-NLS-1$

        lastBatchCount = 0;
        currentBatchIndex = 0;
        currentBatchValues = new ArrayList<Object[]>();

        try
        {
            if (response.status().isOK())
            {
                ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
                pipeStreams(response.stream(), bufferStream);

                String bufferString = bufferStream.toString();

                BufferedReader reader = new BufferedReader(new StringReader(
                        bufferString));
                String line = reader.readLine();

                if (line.equals("!!! ERROR")) //$NON-NLS-1$
                {
                    synchronized (currentBatchLock)
                    {
                        currentBatchRetrieved = true;
                        currentBatchRetrievalStatus = getStatusFromReader(reader);
                        return;
                    }
                }

                ObjectInputStream input = new ObjectInputStream(
                        new ByteArrayInputStream(bufferStream.toByteArray()));

                boolean hasNext = input.readBoolean();
                while (hasNext)
                {
                    Object[] currentRow = new Object[expressions.length];

                    for (int j = 0; j < expressions.length; j++)
                        currentRow[j] = input.readObject();

                    currentBatchValues.add(currentRow);
                    hasNext = input.readBoolean();
                }

                lastBatchCount = currentBatchValues.size();
                input.close();
            }
        }
        catch (Exception e)
        {
            synchronized (currentBatchLock)
            {
                currentBatchRetrieved = true;
                currentBatchRetrievalStatus = new Status(IStatus.ERROR,
                        DesignerActivator.PLUGIN_ID, e.getMessage(), e);
                return;
            }
        }
        finally
        {
            response.close();
        }

        synchronized (currentBatchLock)
        {
            currentBatchRetrieved = true;
            currentBatchRetrievalStatus = response.status();
        }
    }


    private void pipeStreams(InputStream inputStream, OutputStream outputStream)
            throws IOException
    {
        byte[] buffer = new byte[65536];

        int bytesRead = inputStream.read(buffer, 0, 65536);

        while (bytesRead > 0)
        {
            outputStream.write(buffer, 0, bytesRead);
            bytesRead = inputStream.read(buffer, 0, 65536);
        }
    }


    private boolean moveToNextRow(IProgressMonitor monitor)
    {
        if (lastBatchCount == 0)
            return false;

        currentBatchIndex++;
        currentRow++;

        if (currentBatchIndex == currentBatchValues.size())
        {
            IStatus status = cancelableGetNextBatch(monitor);

            if (!status.isOK() || lastBatchCount == 0)
                return false;
        }

        return true;
    }


    // ==== Fields ============================================================

    private String serverUrl;

    private String username;

    private String password;

    private int maxRecords;

    private int timeout;

    private ArrayList<Object[]> retrievedRows;

    private int currentRow;

    private int lastBatchCount;

    private String dataSetUuid;

    private String entityType;

    private String[] expressions;

    private String expressionParameterString;

    private String previewSessionId;

    private ArrayList<Object[]> currentBatchValues;

    private int currentBatchIndex;

    private boolean currentBatchRetrieved;

    private Object currentBatchLock;

    private IStatus currentBatchRetrievalStatus;
}
