/*==========================================================================*\
 |  $Id: SubmissionTests.java,v 1.2 2010/05/14 14:45:43 aallowat Exp $
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

import static org.junit.Assert.assertEquals;
import java.io.InputStream;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.webcat.submitter.ISubmittableItem;
import org.webcat.submitter.NoItemsToSubmitException;
import org.webcat.submitter.ProtocolRegistry;
import org.webcat.submitter.RequiredItemsMissingException;
import org.webcat.submitter.SubmissionManifest;
import org.webcat.submitter.SubmittableItemKind;
import org.webcat.submitter.Submitter;
import org.webcat.submitter.targets.AssignmentTarget;
import org.webcat.submitter.tests.utility.MockProtocol;
import org.webcat.submitter.tests.utility.MockSubmittableItem;
import org.webcat.submitter.tests.utility.ZipProcessor;

//--------------------------------------------------------------------------
/**
 * Tests the submission process.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/05/14 14:45:43 $
 */
public class SubmissionTests
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

        // Initializes the mock submittable items.

        singleItem = new MockSubmittableItem("single.txt",
                SubmittableItemKind.FILE);
        singleItem.setContent("single item");

        emptyDirectory = new MockSubmittableItem("empty",
                SubmittableItemKind.FOLDER);

        directoryWithOneFile = new MockSubmittableItem("a",
                SubmittableItemKind.FOLDER);
        MockSubmittableItem a_b = new MockSubmittableItem("a/b",
                SubmittableItemKind.FILE);
        directoryWithOneFile.addChild(a_b);
        a_b.setContent("b, child of a");


        multipleTopLevel1 = new MockSubmittableItem("a",
                SubmittableItemKind.FILE);
        multipleTopLevel1.setContent("file a");

        multipleTopLevel2 = new MockSubmittableItem("b",
                SubmittableItemKind.FOLDER);
        MockSubmittableItem b_c = new MockSubmittableItem("b/c",
                SubmittableItemKind.FILE);
        multipleTopLevel2.addChild(b_c);
        b_c.setContent("c, child of b");


        nestedDirectoriesRoot = new MockSubmittableItem("a",
                SubmittableItemKind.FOLDER);
        a_b = new MockSubmittableItem("a/b", SubmittableItemKind.FOLDER);
        nestedDirectoriesRoot.addChild(a_b);
        MockSubmittableItem a_b_c = new MockSubmittableItem("a/b/c",
                SubmittableItemKind.FILE);
        a_b.addChild(a_b_c);
        a_b_c.setContent("a, child of b/c");

        requiredJava = new MockSubmittableItem("required.java",
                SubmittableItemKind.FILE);
        requiredJava.setContent("required Java file");

        requiredTxt = new MockSubmittableItem("required.txt",
                SubmittableItemKind.FILE);
        requiredTxt.setContent("required text file");
    }


    // ----------------------------------------------------------
    /**
     * Initialization done before each test case is executed.
     *
     * @throws Exception if an exception occurs
     */
    @Before
    public void setUp() throws Exception
    {
        // Create a new instance of the submitter and read the test target
        // definitions; this will be shared by all tests.

        submitter = new Submitter();

        InputStream stream = SubmissionTests.class.getResourceAsStream(
                "test-targets.xml");
        submitter.readSubmissionTargets(stream);
        stream.close();

        assignment = (AssignmentTarget) submitter.getTarget(
                "Test Group/Test Assignment");
        requiresAssignment = (AssignmentTarget) submitter.getTarget(
                "Test Group/Test Requires");
    }


    // ----------------------------------------------------------
    /**
     * Cleanup done after each test case is executed.
     */
    @After
    public void tearDown()
    {
        submitter = null;
    }


    // ----------------------------------------------------------
    /**
     * Tests an empty submission. This should cause
     * {@link NoItemsToSubmitException} to be thrown.
     *
     * @throws Exception if an exception occurs
     */
    @Test(expected = NoItemsToSubmitException.class)
    public void submitEmpty() throws Exception
    {
        submit(assignment);
    }


    // ----------------------------------------------------------
    /**
     * Tests a submission of a single top-level file.
     *
     * @throws Exception if an exception occurs
     */
    @Test
    public void submitSingleFile() throws Exception
    {
        Map<String, String> entries = submit(assignment, singleItem);

        assertEquals("package should have 1 entry", 1, entries.size());
        assertEquals("single item", entries.get("single.txt"));
    }


    // ----------------------------------------------------------
    /**
     * Tests a submission of a single, empty, top-level folder.
     *
     * @throws Exception if an exception occurs
     */
    @Test
    public void submitEmptyDirectory() throws Exception
    {
        Map<String, String> entries = submit(assignment, emptyDirectory);

        assertEquals("package should have 1 entry", 1, entries.size());
        assertEquals(null, entries.get("empty"));
    }


    // ----------------------------------------------------------
    /**
     * Tests a submission of a top-level folder containing a file.
     *
     * @throws Exception if an exception occurs
     */
    @Test
    public void submitDirectoryWithOneFile() throws Exception
    {
        Map<String, String> entries = submit(assignment, directoryWithOneFile);

        assertEquals("package should have 2 entries", 2, entries.size());
        assertEquals(null, entries.get("a"));
        assertEquals("b, child of a", entries.get("a/b"));
    }


    // ----------------------------------------------------------
    /**
     * Tests a submission of multiple top-level items.
     *
     * @throws Exception if an exception occurs
     */
    @Test
    public void submitMultipleTopLevelItems() throws Exception
    {
        Map<String, String> entries = submit(assignment,
                multipleTopLevel1, multipleTopLevel2);

        assertEquals("package should have 3 entries", 3, entries.size());
        assertEquals("file a", entries.get("a"));
        assertEquals(null, entries.get("b"));
        assertEquals("c, child of b", entries.get("b/c"));
    }


    // ----------------------------------------------------------
    /**
     * Tests a submission with nested folders.
     *
     * @throws Exception if an exception occurs
     */
    @Test
    public void submitNestedDirectories() throws Exception
    {
        Map<String, String> entries = submit(assignment,
                nestedDirectoriesRoot);

        assertEquals("package should have 3 entries", 3, entries.size());
        assertEquals(null, entries.get("a"));
        assertEquals(null, entries.get("a/b"));
        assertEquals("a, child of b/c", entries.get("a/b/c"));
    }


    // ----------------------------------------------------------
    /**
     * Tests a submission missing a required file.
     *
     * @throws Exception if an exception occurs
     */
    @Test(expected = RequiredItemsMissingException.class)
    public void submitMissingRequiredFile() throws Exception
    {
        submit(requiresAssignment, requiredTxt);
    }


    // ----------------------------------------------------------
    /**
     * Tests a submission that has a required file.
     *
     * @throws Exception if an exception occurs
     */
    @Test
    public void submitHasRequiredFile() throws Exception
    {
        Map<String, String> entries = submit(requiresAssignment,
                requiredTxt, requiredJava);

        assertEquals("package should have 2 entries", 2, entries.size());
        assertEquals("required text file", entries.get("required.txt"));
        assertEquals("required Java file", entries.get("required.java"));
    }


    // ----------------------------------------------------------
    /**
     * A helper method to perform the actual submission to a ZIP archive and
     * then return a map containing its entries, where the keys are the names
     * of the entries and the values are strings representing the file content
     * (or null for folders).
     *
     * @param items the items to submit
     */
    private Map<String, String> submit(AssignmentTarget asmt,
            ISubmittableItem... items) throws Exception
    {
        SubmissionManifest manifest = new SubmissionManifest();
        manifest.setAssignment(asmt);
        manifest.setSubmittableItems(items);

        submitter.submit(manifest);

        ZipProcessor zp = new ZipProcessor(MockProtocol.getTransmittedBytes());
        return zp.entries();
    }


    //~ Static/instance variables .............................................

    private Submitter submitter;
    private AssignmentTarget assignment;
    private AssignmentTarget requiresAssignment;

    /* Mock items for testing. */
    private static MockSubmittableItem singleItem;
    private static MockSubmittableItem emptyDirectory;
    private static MockSubmittableItem directoryWithOneFile;
    private static MockSubmittableItem multipleTopLevel1;
    private static MockSubmittableItem multipleTopLevel2;
    private static MockSubmittableItem nestedDirectoriesRoot;
    private static MockSubmittableItem requiredJava;
    private static MockSubmittableItem requiredTxt;
}
