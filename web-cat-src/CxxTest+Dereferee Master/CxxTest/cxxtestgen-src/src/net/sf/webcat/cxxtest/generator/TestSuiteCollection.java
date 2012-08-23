/*==========================================================================*\
 |  $Id: TestSuiteCollection.java,v 1.1 2009/10/10 17:05:40 aallowat Exp $
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
 * Represents the collection of test suites that will populate the runner.
 * 
 * @author Tony ALlevato
 * @version $Id: TestSuiteCollection.java,v 1.1 2009/10/10 17:05:40 aallowat Exp $
 */
public class TestSuiteCollection
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    public TestSuiteCollection()
    {
        this.suites = new ArrayList<TestSuite>();
        this.possibleTestFiles = new ArrayList<String>();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public List<TestSuite> getSuites()
    {
        return suites;
    }


    // ----------------------------------------------------------
    public List<String> getPossibleTestFiles()
    {
        return possibleTestFiles;
    }


    // ----------------------------------------------------------
    public boolean doesMainFunctionExist()
    {
        return mainExists;
    }

    
    // ----------------------------------------------------------
    public void addSuite(TestSuite suite)
    {
        suites.add(suite);
    }
    
    
    // ----------------------------------------------------------
    public TestSuite getSuite(String name)
    {
        for (TestSuite suite : suites)
        {
            if (name.equals(suite.getName()))
                return suite;
        }
        
        return null;
    }


    //~ Static/instance variables .............................................

    private List<TestSuite> suites;
    private List<String> possibleTestFiles;
    private boolean mainExists;
}
