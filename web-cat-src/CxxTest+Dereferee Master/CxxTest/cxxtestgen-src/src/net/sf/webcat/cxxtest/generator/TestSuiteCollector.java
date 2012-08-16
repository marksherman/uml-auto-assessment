/*==========================================================================*\
 |  $Id: TestSuiteCollector.java,v 1.1 2009/10/10 17:05:40 aallowat Exp $
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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//--------------------------------------------------------------------------
/**
 * Collects information about the test suites and test cases in a set of C++
 * source files.
 * 
 * @author Tony ALlevato
 * @version $Id: TestSuiteCollector.java,v 1.1 2009/10/10 17:05:40 aallowat Exp $
 */
public class TestSuiteCollector
{
    //~ Constructors ..........................................................
    
    // ----------------------------------------------------------
    /**
     * Creates a new instance of the TestSuiteCollector class using the
     * specified list of C++ source files.
     */
    public TestSuiteCollector(List<String> testFiles)
    {
        suites = new TestSuiteCollection();

        for (String file : testFiles)
        {
            parseTestFile(file);
        }
    }
    

    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the collection of test suites that were collected from the C++
     * source files.
     */
    public TestSuiteCollection getSuites()
    {
        return suites;
    }
    

    // ----------------------------------------------------------
    /**
     * Parses the C++ file at the specified path for test suites.
     */
    private void parseTestFile(String path)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            int lineNum = 1;

            while ((line = reader.readLine()) != null)
            {
                if (SUITE_START_REGEX.matcher(line).matches())
                {
                    line = line.trim();
                    line += reader.readLine();
                    lineNum++;
                }

                scanLineForSuiteStart(path, line, lineNum);
                
                if (suite != null)
                {
                    scanLineForTest(line, lineNum);
                    scanLineForCreate(line, lineNum);
                    scanLineForDestroy(line, lineNum);
                }

                lineNum++;
            }
            
            reader.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    

    // ----------------------------------------------------------
    /**
     * Scans the current line for a test suite class definition.
     */
    private void scanLineForSuiteStart(String path, String line, int lineNum)
    {
        Matcher matcher = SUITE_FULL_REGEX.matcher(line);
        if (matcher.matches())
        {
            suite = new TestSuite(matcher.group(1), path, lineNum);
            suites.addSuite(suite);
        }
    }


    // ----------------------------------------------------------
    /**
     * Scans the current line for a test case method definition.
     */
    private void scanLineForTest(String line, int lineNum)
    {
        Matcher matcher = TEST_REGEX.matcher(line);
        if (matcher.matches())
        {
            suite.addTestCase(new TestCase(matcher.group(1), lineNum));
        }
    }


    // ----------------------------------------------------------
    /**
     * Scans the current line for a createSuite method definition.
     */
    private void scanLineForCreate(String line, int lineNum)
    {
        Matcher matcher = CREATE_REGEX.matcher(line);
        if (matcher.matches())
        {
            suite.setCreateLineNumber(lineNum);
        }
    }


    // ----------------------------------------------------------
    /**
     * Scans the current line for a destroySuite method definition.
     */
    private void scanLineForDestroy(String line, int lineNum)
    {
        Matcher matcher = DESTROY_REGEX.matcher(line);
        if (matcher.matches())
        {
            suite.setCreateLineNumber(lineNum);
        }
    }

    
    //~ Static/instance variables .............................................

    private static final Pattern SUITE_START_REGEX =
        Pattern.compile("\\bclass\\s+(\\w+)\\s*:\\s*(//.*)?$");

    private static final Pattern SUITE_FULL_REGEX =
        Pattern.compile("\\bclass\\s+(\\w+)\\s*:\\s*public\\s+((::)?\\s*"
                + "CxxTest\\s*::\\s*)?TestSuite\\b.*");
    
    private static final Pattern TEST_REGEX =
        Pattern.compile("^\\s*\\bvoid\\s+([Tt]est\\w+)\\s*\\(\\s*"
                + "(void)?\\s*\\).*");

    private static final Pattern CREATE_REGEX =
        Pattern.compile("\\bstatic\\s+\\w+\\s*\\*\\s*createSuite\\s*\\("
                + "\\s*(void)?\\s*\\).*");
    
    private static final Pattern DESTROY_REGEX =
        Pattern.compile("\\bstatic\\s+void\\s+destroySuite\\s*\\("
                + "\\s*\\w+\\s*\\*\\s*\\w*\\s*\\).*");

    private TestSuite suite;
    private TestSuiteCollection suites;
}
