/*==========================================================================*\
 |  $Id: RootTarget.java,v 1.2 2010/12/06 21:06:48 aallowat Exp $
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
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;
import org.webcat.submitter.ILongRunningTask;
import org.webcat.submitter.ILongRunningTaskManager;
import org.webcat.submitter.SubmissionTargetException;
import org.webcat.submitter.internal.Xml;

//--------------------------------------------------------------------------
/**
 * Represents the root of the submission definition tree. The root contains
 * settings that are inherited down throughout the entire tree.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/12/06 21:06:48 $
 */
public class RootTarget extends SubmissionTarget
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new root target.
     *
     * @param taskManager the long-running task manager that will be used to
     *     execute long-running tasks that may occur when parsing or loading
     *     submission targets
     */
    public RootTarget(ILongRunningTaskManager taskManager)
    {
        super(null);

        this.taskManager = taskManager;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the long-running task manager that will be used for this tree of
     * submission targets.
     *
     * @return the long-running task manager
     */
    public ILongRunningTaskManager getLongRunningTaskManager()
    {
        return taskManager;
    }


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
     * @see SubmissionTarget#isActionable()
     */
    @Override
    public boolean isActionable()
    {
        return false;
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#isNested()
     */
    @Override
    public boolean isNested()
    {
        return false;
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#isLoaded()
     */
    @Override
    public boolean isLoaded()
    {
        return true;
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#parse(Node, ILongRunningTask)
     */
    @Override
    public void parse(Node node, ILongRunningTask task)
    throws SubmissionTargetException
    {
        String nodeName = node.getLocalName();

        if (Xml.Elements.SUBMISSION_TARGETS.equals(nodeName))
        {
            parseSubmissionTargets(node, task);
        }
    }


    // ----------------------------------------------------------
    /**
     * Parses the children of a submission-targets root node.
     *
     * @param parentNode the node to parse
     * @param task the long-running task
     * @throws SubmissionTargetException if an exception occurred during
     *     parsing
     */
    private void parseSubmissionTargets(Node parentNode, ILongRunningTask task)
            throws SubmissionTargetException
    {
        startSubtaskForChildNodes(parentNode, task);

        parseCommonAttributes(parentNode, task);

        Node node = parentNode.getFirstChild();

        List<String> includes = new ArrayList<String>();
        List<String> excludes = new ArrayList<String>();
        List<String> required = new ArrayList<String>();
        List<SubmissionTarget> children = new ArrayList<SubmissionTarget>();

        while (node != null)
        {
            String nodeName = node.getLocalName();

            if (Xml.Elements.FILTER_AMBIGUITY.equals(nodeName))
            {
                parseFilterAmbiguity(node, task);
            }
            else if (Xml.Elements.INCLUDE.equals(nodeName))
            {
                includes.add(parseFilePattern(node, task));
            }
            else if (Xml.Elements.EXCLUDE.equals(nodeName))
            {
                excludes.add(parseFilePattern(node, task));
            }
            else if (Xml.Elements.REQUIRED.equals(nodeName))
            {
                required.add(parseFilePattern(node, task));
            }
            else if (Xml.Elements.TRANSPORT.equals(nodeName))
            {
                parseTransport(node, task);
            }
            else if (Xml.Elements.PACKAGER.equals(nodeName))
            {
                parsePackager(node, task);
            }
            else if (Xml.Elements.ASSIGNMENT_GROUP.equals(nodeName))
            {
                children.add(parseAssignmentGroup(node, task));
            }
            else if (Xml.Elements.IMPORT_GROUP.equals(nodeName))
            {
                children.add(parseImportGroup(node, task));
            }
            else if (Xml.Elements.ASSIGNMENT.equals(nodeName))
            {
                children.add(parseAssignment(node, task));
            }
            else
            {
                task.doWork(1);
            }

            node = node.getNextSibling();
        }

        setIncludedFiles(includes);
        setExcludedFiles(excludes);
        setRequiredFiles(required);
        setChildren(children);

        task.finishSubtask();
    }


    // ----------------------------------------------------------
    /**
     * Called if node represents an assignment group to create the new group
     * and parse it.
     *
     * @param node the assignment-group node to parse
     * @param task the long-running task
     * @return the new assignment group
     * @throws SubmissionTargetException if a parsing error occurred
     */
    private SubmissionTarget parseAssignmentGroup(Node node,
            ILongRunningTask task)
            throws SubmissionTargetException
    {
        AssignmentGroupTarget group = new AssignmentGroupTarget(this);
        group.parse(node, task);
        return group;
    }


    // ----------------------------------------------------------
    /**
     * Called if node represents an import group to create the new group and
     * parse it.
     *
     * @param node the import-group node to parse
     * @param task the long-running task
     * @return the new import group
     * @throws SubmissionTargetException if a parsing error occurred
     */
    private SubmissionTarget parseImportGroup(Node node,
            ILongRunningTask task)
            throws SubmissionTargetException
    {
        ImportGroupTarget group = new ImportGroupTarget(this);
        group.parse(node, task);
        return group;
    }


    // ----------------------------------------------------------
    /**
     * Called if node represents an assignment to create the new target and
     * parse it.
     *
     * @param node the assignment node to parse
     * @param task the long-running task
     * @return the new assignment
     * @throws SubmissionTargetException if a parsing error occurred
     */
    private SubmissionTarget parseAssignment(Node node,
            ILongRunningTask task) throws SubmissionTargetException
    {
        AssignmentTarget assignment = new AssignmentTarget(this);
        assignment.parse(node, task);
        return assignment;
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
        writer.print("<submission-targets ");
        writer.print("xmlns=\"http://web-cat.cs.vt.edu/submissionTargets\"");
        writer.println(">");

        writeSharedProperties(indentLevel + 1, writer);

        writeChildren(indentLevel + 1, writer);

        // Write closing tag.
        padToIndent(indentLevel, writer);
        writer.println("</submission-targets>");
    }


    //~ Static/instance variables .............................................

    /* The long-running task manager to use for operations on this tree. */
    private ILongRunningTaskManager taskManager;
}
