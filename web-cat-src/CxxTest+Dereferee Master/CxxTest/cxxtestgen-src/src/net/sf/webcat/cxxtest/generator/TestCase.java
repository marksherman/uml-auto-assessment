/*==========================================================================*\
 |  $Id: TestCase.java,v 1.1 2009/10/10 17:05:40 aallowat Exp $
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

//--------------------------------------------------------------------------
/**
 * Represents a test case method that will have code generated to execute it.
 * 
 * @author Tony Allevato
 * @version $Id: TestCase.java,v 1.1 2009/10/10 17:05:40 aallowat Exp $
 */
public class TestCase
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new test case at the specified line in a source file.
     */
    public TestCase(String name, int lineNumber)
    {
        this.name = name;
        this.lineNumber = lineNumber;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the name of the test case method.
     */
    public String getName()
    {
        return name;
    }


    // ----------------------------------------------------------
    /**
     * Gets the line number at which the test case method appears in the
     * source.
     */
    public int getLineNumber()
    {
        return lineNumber;
    }


    //~ Static/instance variables .............................................

    private String name;
    private int lineNumber;
}
