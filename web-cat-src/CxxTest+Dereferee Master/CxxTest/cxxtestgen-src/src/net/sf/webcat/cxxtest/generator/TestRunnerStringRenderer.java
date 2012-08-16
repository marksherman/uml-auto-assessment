/*==========================================================================*\
 |  $Id: TestRunnerStringRenderer.java,v 1.1 2009/10/10 17:05:40 aallowat Exp $
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

import org.antlr.stringtemplate.AttributeRenderer;

//--------------------------------------------------------------------------
/**
 * An attribute renderer used in the test runner template to output path
 * strings as a relative path or as an escaped C-style string literal.
 * 
 * @author Tony ALlevato
 * @version $Id: TestRunnerStringRenderer.java,v 1.1 2009/10/10 17:05:40 aallowat Exp $
 */
public class TestRunnerStringRenderer implements AttributeRenderer
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new string renderer using the specified path as the basis for
     * relativization.
     * 
     * @param runnerPath the path of the source file being generated
     */
    public TestRunnerStringRenderer(String runnerPath)
    {
        this.runnerPath = runnerPath;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * The default string conversion method for rendering without a format. The
     * object is rendered simply by calling the object's toString() method.
     * 
     * @param o the object to render as a string
     * 
     * @return the string value of the object
     */
    public String toString(Object o)
    {
        return o.toString();
    }


    // ----------------------------------------------------------
    /**
     * The formatted version of the string renderer. This method supports the
     * following rendering formats:
     * 
     * <dl>
     * <dt>asCString</dt>
     * <dd>Renders the string as a C-style string literal, escaping any double
     * quotes and backslashes that might be present, and surrounding it with
     * double-quotes.</dd>
     * <dt>runnerRelativePath</dt>
     * <dd>Assumes that the string is a path to a file or directory and renders
     * it as a relative path, using the renderer's runner path as the
     * basis.</dd>
     * <dt>runnerRelativePathAsCString</dt>
     * <dd>A combination of the above two formats.</dd>
     * </dl>
     * 
     * @param o the object to render as a string
     * @param formatName the format in which to render the string, one of the
     *     choices described above
     * 
     * @return the string value of the object, rendered in the specified format
     */
    public String toString(Object o, String formatName)
    {
        if (formatName.equals("asCString"))
        {
            return toCString(o.toString());
        }
        else if (formatName.equals("runnerRelativePath"))
        {
            return PathUtils.relativizePath(runnerPath, o.toString());
        }
        else if (formatName.equals("runnerRelativePathAsCString"))
        {
            return toCString(PathUtils.relativizePath(
                    runnerPath, o.toString()));
        }
        else
        {
            return o.toString();
        }
    }


    // ----------------------------------------------------------
    /**
     * Converts the specified string to a double-quoted C-style string literal,
     * escaping any double-quotes or backslashes that might be in it.
     * 
     * @param str the string to convert
     * 
     * @return the C-style string result
     */
    private String toCString(String str)
    {
        StringBuilder res = new StringBuilder();
        res.append('"');

        for (int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);

            if (ch == '\\')
            {
                res.append("\\\\");
            }
            else
            {
                res.append(ch);
            }
        }

        res.append('"');

        return res.toString();
    }

    
    //~ Static/instance variables .............................................

    /** The path to the runner source file that is being generated. */
    private String runnerPath;
}
