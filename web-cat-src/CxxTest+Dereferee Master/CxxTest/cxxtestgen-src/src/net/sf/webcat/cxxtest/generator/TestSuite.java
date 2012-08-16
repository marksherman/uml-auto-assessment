/*==========================================================================*\
 |  $Id: TestSuite.java,v 1.1 2009/10/10 17:05:40 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2009 Virginia Tech
 |
 |  This file is part of the Web-CAT CxxTest Distribution.
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

package net.sf.webcat.cxxtest.generator;

import java.util.ArrayList;
import java.util.List;

//--------------------------------------------------------------------------
/**
 * Represents a test suite (a class containing test case methods).
 * 
 * @author Tony ALlevato
 * @version $Id: TestSuite.java,v 1.1 2009/10/10 17:05:40 aallowat Exp $
 */
public class TestSuite
{
    //~ Constructors ..........................................................

    // ------------------------------------------------------
    /**
     * Creates a new test suite object.
     * 
     * @param name the name of the test suite class
     * @param fullPath the absolute path to the source file containing this
     *     test suite
     * @param lineNumber the line number in the source file where the test
     *     suite is defined
     */
    public TestSuite(String name, String fullPath, int lineNumber)
    {
        this.name = name;
        this.fullPath = fullPath;
        this.lineNumber = lineNumber;

        testCases = new ArrayList<TestCase>();

        createLineNumber = 0;
        destroyLineNumber = 0;
    }


    //~ Methods ...............................................................

    // ------------------------------------------------------
    /**
     * Gets the name of the test suite class.
     */
    public String getName()
    {
        return name;
    }


    // ------------------------------------------------------
    /**
     * Gets the absolute path to the source file that contains this test suite.
     */
    public String getFullPath()
    {
        return fullPath;
    }


    // ------------------------------------------------------
    /**
     * Gets the line number in the source code at which this test suite
     * class starts.
     */
    public int getLineNumber()
    {
        return lineNumber;
    }


    // ------------------------------------------------------
    /**
     * Gets the name of the C++ object that will be generated to represent this
     * test suite.
     */
    public String getObjectName()
    {
        return "suite_" + getName();
    }


    // ------------------------------------------------------
    /**
     * Gets the name of the C++ object that will be generated to represent this
     * test suite description.
     */
    public String getDescriptionObjectName()
    {
        return "suiteDescription_" + getName();
    }


    // ------------------------------------------------------
    /**
     * Gets the name of the C++ object that will be generated to represent the
     * list of test cases found in this test suite.
     */
    public String getTestListName()
    {
        return "Tests_" + getName();
    }


    // ------------------------------------------------------
    /**
     * Gets the list of test cases in this test suite.
     */
    public List<TestCase> getTestCases()
    {
        return testCases;
    }


    // ------------------------------------------------------
    /**
     * Gets the line number that contains a createSuite static method for this
     * test suite, or 0 if there is none.
     */
    public int getCreateLineNumber()
    {
        return createLineNumber;
    }


    // ------------------------------------------------------
    /**
     * Sets the line number that contains a createSuite static method for this
     * test suite, or 0 if there is none.
     */
    public void setCreateLineNumber(int value)
    {
        createLineNumber = value;
    }


    // ------------------------------------------------------
    /**
     * Gets the line number that contains a destroySuite static method for this
     * test suite, or 0 if there is none.
     */
    public int getDestroyLineNumber()
    {
        return destroyLineNumber;
    }


    // ------------------------------------------------------
    /**
     * Sets the line number that contains a destroySuite static method for this
     * test suite, or 0 if there is none.
     */
    public void setDestroyLineNumber(int value)
    {
        destroyLineNumber = value;
    }


    // ------------------------------------------------------
    /**
     * Gets a value indicating whether the test suite is dynamic or not (that
     * is, it contains a createSuite static method).
     */
    public boolean isDynamic()
    {
        return (createLineNumber != 0);
    }


    // ------------------------------------------------------
    /**
     * Adds a test case method to this test suite.
     * 
     * @param testCase the test case to add
     */
    public void addTestCase(TestCase testCase)
    {
        testCases.add(testCase);
    }


    //~ Instance variables ...............................................

    // The name of the test suite.
    private String name;
    
    // The absolute path to the source file containing the test suite.
    private String fullPath;
    
    // The line number in the source file at which the test suite is defined.
    private int lineNumber;

    // The list of test case methods contained in this test suite.
    private List<TestCase> testCases;

    // The line number of the createSuite method, or 0 if there is none.
    private int createLineNumber;

    // The line number of the destroySuite method, or 0 if there is none.
    private int destroyLineNumber;
}
