/*==========================================================================*\
 |  $Id: AssignmentTarget.java,v 1.2 2010/12/06 21:06:48 aallowat Exp $
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
import org.webcat.submitter.SubmissionTargetException;
import org.webcat.submitter.internal.Xml;

//--------------------------------------------------------------------------
/**
 * Represents a single assignment in the submission definition tree. An
 * assignment is an actionable object to which projects can be submitted.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/12/06 21:06:48 $
 */
public class AssignmentTarget extends SubmissionTarget
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new assignment node with the specified parent.
     *
     * @param parent The node that will be assigned the parent of the new
     *               node.
     */
    public AssignmentTarget(SubmissionTarget parent)
    {
        super(parent);
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#isContainer()
     */
    @Override
    public boolean isContainer()
    {
        return false;
    }


    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#isActionable()
     */
    @Override
    public boolean isActionable()
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
        return true;
    }

    // ----------------------------------------------------------
    /**
     * @see SubmissionTarget#parse(Node, ILongRunningTask)
     */
    @Override
    public void parse(Node parentNode, ILongRunningTask task)
    throws SubmissionTargetException
    {
        startSubtaskForChildNodes(parentNode, task);

        parseCommonAttributes(parentNode, task);

        Node node = parentNode.getFirstChild();

        List<String> includes = new ArrayList<String>();
        List<String> excludes = new ArrayList<String>();
        List<String> required = new ArrayList<String>();

        while(node != null)
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
            else
            {
                task.doWork(1);
            }

            node = node.getNextSibling();
        }

        setIncludedFiles(includes);
        setExcludedFiles(excludes);
        setRequiredFiles(required);

        task.finishSubtask();
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
        writer.print("<assignment name=\"");
        writeXMLString(getName(), writer);
        writer.print("\"");

        if(isHidden())
            writer.print(" hidden=\"true\"");

        writer.println(">");

        writeSharedProperties(indentLevel + 1, writer);

        // Write closing tag.
        padToIndent(indentLevel, writer);
        writer.println("</assignment>");
    }
}
