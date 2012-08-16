/*==========================================================================*\
 |  $Id: TestCase.java,v 1.3 2009/09/13 12:59:29 aallowat Exp $
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

//--------------------------------------------------------------------------
/**
 * Represents a test case method that will have code generated to execute it.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2009/09/13 12:59:29 $
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
