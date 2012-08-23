/*==========================================================================*\
 |  $Id: TestRunnerStringRenderer.java,v 1.3 2009/09/13 12:59:29 aallowat Exp $
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

import org.antlr.stringtemplate.AttributeRenderer;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2009/09/13 12:59:29 $
 */
public class TestRunnerStringRenderer implements AttributeRenderer
{
    public TestRunnerStringRenderer(String runnerPath)
    {
        this.runnerPath = runnerPath;
    }

    public String toString(Object o)
    {
        return o.toString();
    }

    public String toString(Object o, String formatName)
    {
    	if (formatName.equals("asCString")) //$NON-NLS-1$
            return toCString(o.toString());
        else if (formatName.equals("runnerRelativePath")) //$NON-NLS-1$
            return PathUtils.relativizePath(runnerPath, o.toString());
        else if (formatName.equals("runnerRelativePathAsCString")) //$NON-NLS-1$
            return toCString(PathUtils.relativizePath(runnerPath, o.toString()));
        else
            return o.toString();
    }

    private String toCString(String str)
    {
        String res = ""; //$NON-NLS-1$
        for (int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);

            if (ch == '\\')
                res += "\\\\"; //$NON-NLS-1$
            else
                res += ch;
        }

        return "\"" + res + "\""; //$NON-NLS-1$ //$NON-NLS-2$
    }

    private String runnerPath;
}
