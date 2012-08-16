/*==========================================================================*\
 |  $Id: OgnlBuilderTemplates.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2008 Virginia Tech
 |
 |  This file is part of Web-CAT.
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

package org.webcat.oda.designer.ognl;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.graphics.Point;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: OgnlBuilderTemplates.java,v 1.1 2010/05/11 15:52:46 aallowat Exp $
 */
public class OgnlBuilderTemplates
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    private OgnlBuilderTemplates()
    {
        // Static class; prevent instantiation.
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public static void insertTemplate(SourceViewer editor, String template)
    {
        Point range = editor.getSelectedRange();

        int selStart = template.indexOf("%%"); //$NON-NLS-1$
        template = template.replaceFirst("%%", ""); //$NON-NLS-1$ //$NON-NLS-2$
        int selLength = template.indexOf("%%") - selStart; //$NON-NLS-1$
        template = template.replaceFirst("%%", ""); //$NON-NLS-1$ //$NON-NLS-2$

        try
        {
            editor.getDocument().replace(range.x, range.y, template);
            editor.setSelectedRange(range.x + selStart, selLength);
        }
        catch (BadLocationException e1)
        {
            // Ignore exception.
        }
    }


    //~ Static/instance variables .............................................

    // The %% markers denote the beginning and end of what should be the
    // selected range of text once the template is inserted.

    public static final String TEMPLATE_MIN = "@min(%%arg1, arg2%%)"; //$NON-NLS-1$
    public static final String TEMPLATE_MAX = "@max(%%arg1, arg2%%)"; //$NON-NLS-1$
    public static final String TEMPLATE_FLOOR = "@floor(%%arg%%)"; //$NON-NLS-1$
    public static final String TEMPLATE_CEIL = "@ceil(%%arg%%)"; //$NON-NLS-1$
    public static final String TEMPLATE_ROUND = "@round(%%arg%%)"; //$NON-NLS-1$
    public static final String TEMPLATE_ABS = "@abs(%%arg%%)"; //$NON-NLS-1$
    public static final String TEMPLATE_SIGNUM = "@signum(%%arg%%)"; //$NON-NLS-1$
    public static final String TEMPLATE_CONDITIONAL = "%%<condition>%% ? <if true> : <if false>"; //$NON-NLS-1$
    public static final String TEMPLATE_INSTANCEOF = " instanceof %%<class>%%"; //$NON-NLS-1$
    public static final String TEMPLATE_LIST = "{ %%item1, item2, ...%% }"; //$NON-NLS-1$
    public static final String TEMPLATE_MAP = "#{ %%key1 : value1, key2 : value2, ...%% }"; //$NON-NLS-1$
    public static final String TEMPLATE_CHAINED = ".( %%<expression>%% )"; //$NON-NLS-1$
    public static final String TEMPLATE_PROJECTION = ".{ %%<expression>%% }"; //$NON-NLS-1$
    public static final String TEMPLATE_SELECT_ALL = ".{? %%<condition>%% }"; //$NON-NLS-1$
    public static final String TEMPLATE_SELECT_FIRST = ".{^ %%<condition>%% }"; //$NON-NLS-1$
    public static final String TEMPLATE_SELECT_LAST = ".{$ %%<condition>%% }"; //$NON-NLS-1$
    public static final String TEMPLATE_LAMBDA = ":[ %%<expression>%% ]"; //$NON-NLS-1$
}
