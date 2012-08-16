/*==========================================================================*\
 |  $Id: TargetParsingTests.java,v 1.2 2010/05/14 14:45:43 aallowat Exp $
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

package org.webcat.submitter.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import java.io.InputStream;
import org.junit.BeforeClass;
import org.junit.Test;
import org.webcat.submitter.ProtocolRegistry;
import org.webcat.submitter.Submitter;
import org.webcat.submitter.targets.AssignmentGroupTarget;
import org.webcat.submitter.targets.AssignmentTarget;
import org.webcat.submitter.targets.RootTarget;
import org.webcat.submitter.targets.SubmissionTarget;
import org.webcat.submitter.tests.utility.MockProtocol;

//--------------------------------------------------------------------------
/**
 * Tests the submitter parsing the target definitions.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/05/14 14:45:43 $
 */
public class TargetParsingTests
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Initialization done once before all of the test cases in this class run.
     *
     * @throws Exception if an exception occurs
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        // Register the mock protocol with the global protocol registry.

        ProtocolRegistry.getInstance().add("mock", MockProtocol.class);

        // Create a new instance of the submitter and read the test target
        // definitions; this will be shared by all tests.

        submitter = new Submitter();

        InputStream stream = TargetParsingTests.class.getResourceAsStream(
                "test-targets.xml");
        submitter.readSubmissionTargets(stream);
        stream.close();
    }


    // ----------------------------------------------------------
    @Test
    public void targetRoot() throws Exception
    {
        RootTarget root = submitter.getRoot();
        assertNotNull("root should not be null", root);
        assertNull("name of root should be null", root.getName());
    }


    // ----------------------------------------------------------
    @Test
    public void targetAssignmentGroup() throws Exception
    {
        RootTarget root = submitter.getRoot();

        SubmissionTarget[] children = root.getChildren();
        assertEquals("root should have one child", 1, children.length);

        SubmissionTarget child = children[0];
        assertTrue("child of root should be ITargetAssignmentGroup",
                child instanceof AssignmentGroupTarget);

        AssignmentGroupTarget group = (AssignmentGroupTarget) child;
        assertEquals("group name should be \"Test Group\"",
                group.getName(), "Test Group");
    }


    // ----------------------------------------------------------
    @Test
    public void targetAssignment() throws Exception
    {
        RootTarget root = submitter.getRoot();
        AssignmentGroupTarget group =
            (AssignmentGroupTarget) root.getChildren()[0];

        SubmissionTarget[] children = group.getChildren();
        assertEquals("assignment group should have two children",
                2, children.length);

        SubmissionTarget child = children[0];
        assertTrue("child of assignment group should be ITargetAssignment",
                child instanceof AssignmentTarget);

        AssignmentTarget assignment = (AssignmentTarget) child;
        assertEquals("assignment name should be \"Test Assignment\"",
                assignment.getName(), "Test Assignment");

        assertEquals("assignment transport URI should be \"mock:mock\"",
                assignment.getTransport(), "mock:mock");

        assertEquals("assignment packager URI should be " +
                "\"org.webcat.submitter.packagers.zip\"",
                assignment.getPackager(),
                "org.webcat.submitter.packagers.zip");
    }


    // ----------------------------------------------------------
    @Test
    public void targetByPath() throws Exception
    {
        SubmissionTarget target =
            submitter.getTarget("Test Group/Test Assignment");

        assertNotNull("target should not be null", target);
        assertTrue("target should be an assignment",
                target instanceof AssignmentTarget);
    }


    //~ Static/instance variables .............................................

    private static Submitter submitter;
}
