/*==========================================================================*\
 |  $Id: TestSuiteCollection.java,v 1.4 2009/09/13 12:59:29 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech 
 |
 |	This file is part of Web-CAT Eclipse Plugins.
 |
 |	Web-CAT is free software; you can redistribute it and/or modify
 |	it under the terms of the GNU General Public License as published by
 |	the Free Software Foundation; either version 2 of the License, or
 |	(at your option) any later version.
 |
 |	Web-CAT is distributed in the hope that it will be useful,
 |	but WITHOUT ANY WARRANTY; without even the implied warranty of
 |	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |	GNU General Public License for more details.
 |
 |	You should have received a copy of the GNU General Public License
 |	along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package net.sf.webcat.eclipse.cxxtest.internal.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//--------------------------------------------------------------------------
/**
 * Represents the collection of test suites that will populate the runner.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.4 $ $Date: 2009/09/13 12:59:29 $
 */
public class TestSuiteCollection
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    public TestSuiteCollection()
    {
        this.suites = new ArrayList<TestSuite>();
        this.possibleTestFiles = new HashSet<String>();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public List<TestSuite> getSuites()
    {
        return suites;
    }


    // ----------------------------------------------------------
    public String[] getPossibleTestFiles()
    {
        return possibleTestFiles.toArray(
        		new String[possibleTestFiles.size()]);
    }


    // ----------------------------------------------------------
    public boolean doesMainFunctionExist()
    {
        return mainExists;
    }

    
    // ----------------------------------------------------------
    public void setDoesMainFunctionExist(boolean value)
    {
    	mainExists = value;
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
    
    
    // ----------------------------------------------------------
    public void addPossibleTestFile(String name)
    {
    	possibleTestFiles.add(name);
    }
    
    
    // ----------------------------------------------------------
    public void removePossibleTestFile(String name)
    {
    	possibleTestFiles.remove(name);
    }


    //~ Static/instance variables .............................................

    private List<TestSuite> suites;
    private Set<String> possibleTestFiles;
    private boolean mainExists;
}
