/*==========================================================================*\
 |  $Id: ImportGroupTarget.java,v 1.2 2010/12/06 21:06:48 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
 |
 |  This file is part of Web-CAT Electronic Submitter.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU General Public License as published by
 |  the Free Software Foundation; either version 2 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU General Public License along
 |  with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.submitter.targets;

import java.io.PrintWriter;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;
import org.webcat.submitter.AmbiguityResolutionPolicy;
import org.webcat.submitter.ILongRunningTask;
import org.webcat.submitter.ILongRunningTaskManager;
import org.webcat.submitter.SubmissionTargetException;
import org.webcat.submitter.Submitter;
import org.webcat.submitter.internal.Xml;

//--------------------------------------------------------------------------
/**
 * Represents an imported group in the submission target tree. An imported group
 * refers to an external XML submission target file that will be merged with the
 * tree at the location of the import group node. The imported group must have a
 * name and a valid URL to the external file.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/12/06 21:06:48 $
 */
public class ImportGroupTarget extends SubmissionTarget
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new imported group node with the specified parent.
     *
     * @param parent
     *            The node that will be assigned the parent of the new node.
     */
    public ImportGroupTarget(SubmissionTarget parent)
    {
        super(parent);

        loaded = false;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#isContainer()
     */
    @Override
    public boolean isContainer()
    {
        return true;
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#isNested()
     */
    @Override
    public boolean isNested()
    {
        return true;
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#isLoaded()
     */
    @Override
    public boolean isLoaded()
    {
        return loaded;
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#isActionable()
     */
    @Override
    public boolean isActionable()
    {
        return false;
    }


    // ----------------------------------------------------------
    /**
     * Gets the URL from which this import group should load its children.
     *
     * @return the URL from which this import group should load its children
     */
    public String getHref()
    {
        return href;
    }


    // ----------------------------------------------------------
    /**
     * Sets the URL from which this import group should load its children.
     *
     * @param url the URL from which this import group should load its children
     */
    public void setHref(String url)
    {
        href = url;
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#getAmbiguityResolution()
     */
    @Override
    public AmbiguityResolutionPolicy getAmbiguityResolution()
    throws SubmissionTargetException
    {
        if (!loaded)
        {
            loadImportedDefinitions();
        }

        return super.getAmbiguityResolution();
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#getIncludedFiles()
     */
    @Override
    public String[] getIncludedFiles() throws SubmissionTargetException
    {
        if (!loaded)
        {
            loadImportedDefinitions();
        }

        return super.getIncludedFiles();
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#getExcludedFiles()
     */
    @Override
    public String[] getExcludedFiles() throws SubmissionTargetException
    {
        if (!loaded)
        {
            loadImportedDefinitions();
        }

        return super.getExcludedFiles();
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#getRequiredFiles()
     */
    @Override
    public String[] getRequiredFiles() throws SubmissionTargetException
    {
        if (!loaded)
        {
            loadImportedDefinitions();
        }

        return super.getRequiredFiles();
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#getLocalAttribute(String)
     */
    @Override
    public String getLocalAttribute(String attribute)
    throws SubmissionTargetException
    {
        if (!loaded)
        {
            loadImportedDefinitions();
        }

        return super.getLocalAttribute(attribute);
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#getLocalTransport()
     */
    @Override
    public String getLocalTransport() throws SubmissionTargetException
    {
        if (!loaded)
        {
            loadImportedDefinitions();
        }

        return super.getLocalTransport();
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#getLocalTransportParameters()
     */
    @Override
    public Map<String, String> getLocalTransportParameters()
    throws SubmissionTargetException
    {
        if (!loaded)
        {
            loadImportedDefinitions();
        }

        return super.getLocalTransportParameters();
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#getLocalPackager()
     */
    @Override
    public String getLocalPackager() throws SubmissionTargetException
    {
        if (!loaded)
        {
            loadImportedDefinitions();
        }

        return super.getLocalPackager();
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#getLocalPackagerParameters()
     */
    @Override
    public Map<String, String> getLocalPackagerParameters()
    throws SubmissionTargetException
    {
        if (!loaded)
        {
            loadImportedDefinitions();
        }

        return super.getLocalPackagerParameters();
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#getChildren()
     */
    @Override
    public SubmissionTarget[] getChildren()
    throws SubmissionTargetException
    {
        if (!loaded)
        {
            loadImportedDefinitions();
        }

        return super.getChildren();
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#getIgnoredAttributes()
     */
    protected Set<String> getIgnoredAttributes()
    {
    	Set<String> ignored = super.getIgnoredAttributes();
    	ignored.add("href");

    	return ignored;
    }

    
    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#parse(Node, ILongRunningTask)
     */
    @Override
    public void parse(Node parentNode, ILongRunningTask task)
    throws SubmissionTargetException
    {
        parseCommonAttributes(parentNode, task);

    	Node hrefNode = parentNode.getAttributes().getNamedItem(
                Xml.Attributes.HREF);

        if (hrefNode != null)
        {
            href = hrefNode.getNodeValue();
        }

        task.doWork(1);
    }


    // ----------------------------------------------------------
    /**
     * Loads this group's submission definitions from the external URL
     * specified in the href attribute.
     *
     * @throws SubmissionTargetException if an error occurs during loading
     */
    private void loadImportedDefinitions() throws SubmissionTargetException
    {
        ILongRunningTaskManager taskManager =
            getRoot().getLongRunningTaskManager();

        // Use a temporary instance of the submitter to perform the loading.

        Submitter submitter = new Submitter();
        submitter.setLongRunningTaskManager(taskManager);

        try
        {
            submitter.readSubmissionTargets(new URL(href));
            SubmissionTarget root = submitter.getRoot();

            copyFrom(root);

            loaded = true;
        }
        catch (SubmissionTargetException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new SubmissionTargetException(e);
        }
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#writeToXML(PrintWriter, int)
     */
    @Override
    public void writeToXML(PrintWriter writer, int indentLevel)
    {
        // Write opening tag.
        padToIndent(indentLevel, writer);
        writer.print("<import-group name=\"");
        writeXMLString(getName(), writer);
        writer.print("\"");

        if(isHidden())
            writer.print(" hidden=\"true\"");

        writer.print(" href=\"");
        writeXMLString(href.toString(), writer);
        writer.println("\"/>");
    }


    //~ Static/instance variables .............................................

    /* The URL to the external submission target file for this group. */
    private String href;

    /* Indicates whether the group has been loaded from the external file. */
    private boolean loaded;
}
