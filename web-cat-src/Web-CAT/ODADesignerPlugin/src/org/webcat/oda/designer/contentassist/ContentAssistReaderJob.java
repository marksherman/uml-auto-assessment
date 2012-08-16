/*==========================================================================*\
 |  $Id: ContentAssistReaderJob.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
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

package org.webcat.oda.designer.contentassist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Map;
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
 * A background job that retrieves content assist information from the server.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: ContentAssistReaderJob.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class ContentAssistReaderJob extends Job
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    public ContentAssistReaderJob(ContentAssistManager manager, boolean force)
    {
        super(Messages.CONTENTASSIST_JOB_NAME);

        this.manager = manager;
        this.forceEvenIfVersionsIdentical = force;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    @Override
    protected IStatus run(IProgressMonitor monitor)
    {
        IStatus finalStatus = Status.OK_STATUS;
        IStatus errorStatus = new Status(IStatus.ERROR, DesignerActivator.PLUGIN_ID,
                Messages.CONTENTASSIST_CONNECTION_ERROR);

        monitor.beginTask(Messages.CONTENTASSIST_PROGRESS_DESCRIPTION, 3);

        Hashtable<String, String> params = new Hashtable<String, String>();
        params.put("username", manager.username); //$NON-NLS-1$
        params.put("password", manager.password); //$NON-NLS-1$
        params.put("designerVersion", Integer.toString(
                DesignerActivator.getDefault().getVersionAsInteger()));

        WOActionDispatcher dispatcher = new WOActionDispatcher(
                manager.serverUrl);
        WOActionResponse response;

        boolean needsUpdate = forceEvenIfVersionsIdentical;

        if (!needsUpdate)
        {
            response = dispatcher.send(
                    "contentAssist/subsystemVersionCheck", null, params); //$NON-NLS-1$

            if (response.status().isOK())
            {
                try
                {
                    InputStream stream = response.stream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(stream));

                    Map<String, String> currentVersions = new Hashtable<String, String>();

                    ContentAssistIOUtils.readSubsystemVersions(currentVersions,
                            reader);

                    for (String subsystem : currentVersions.keySet())
                    {
                        if (!manager.subsystemVersions.containsKey(subsystem)
                                || isVersionHigher(currentVersions
                                        .get(subsystem),
                                        manager.subsystemVersions
                                                .get(subsystem)))
                        {
                            needsUpdate = true;
                            break;
                        }
                    }
                }
                catch (IOException e)
                {
                    // Ignore exception.
                }

                response.close();
            }
        }

        monitor.worked(1);

        if (needsUpdate)
        {
            response = dispatcher.send(
                    "contentAssist/entityDescriptions", null, params); //$NON-NLS-1$

            if (response.status().isOK())
            {
                try
                {
                    InputStream stream = response.stream();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(stream));

                    ContentAssistIOUtils.readEntityDescriptions(
                            manager.subsystemVersions,
                            manager.entityDescriptions, reader);
                }
                catch (IOException e)
                {
                    // Ignore exception.
                }

                response.close();
            }
            else
            {
                finalStatus = errorStatus;
            }
        }

        monitor.worked(1);

        response = dispatcher.send(
                "contentAssist/objectDescriptions", null, params); //$NON-NLS-1$

        if (response.status().isOK())
        {
            try
            {
                InputStream stream = response.stream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(stream));

                ContentAssistIOUtils.readObjectDescriptions(
                        manager.objectDescriptions, reader);
            }
            catch (IOException e)
            {
                // Ignore exception.
            }

            response.close();
        }

        // Don't report an error if this stage fails -- Instead, we'll just use
        // whatever the most recently cached object descriptions are.

        manager.writeToState();
        monitor.done();

        return finalStatus;
    }


    // ----------------------------------------------------------
    private boolean isVersionHigher(String thisVersion, String otherVersion)
    {
        try
        {
            String[] thisVersionParts = thisVersion.split("\\."); //$NON-NLS-1$
            String[] otherVersionParts = otherVersion.split("\\."); //$NON-NLS-1$

            int thisMajor = Integer.parseInt(thisVersionParts[0]);
            int thisMinor = Integer.parseInt(thisVersionParts[1]);
            int thisRevision = Integer.parseInt(thisVersionParts[2]);

            int otherMajor = Integer.parseInt(otherVersionParts[0]);
            int otherMinor = Integer.parseInt(otherVersionParts[1]);
            int otherRevision = Integer.parseInt(otherVersionParts[2]);

            return (otherMajor < thisMajor)
                    || (otherMajor == thisMajor && otherMinor < thisMinor)
                    || (otherMajor == thisMajor && otherMinor == thisMinor && otherRevision < thisRevision);
        }
        catch (NumberFormatException e)
        {
            // In the strange event that we can't parse the version number,
            // just force an update to be safe.

            return true;
        }
    }


    //~ Static/instance variables .............................................

    private ContentAssistManager manager;
    private boolean forceEvenIfVersionsIdentical;
}
