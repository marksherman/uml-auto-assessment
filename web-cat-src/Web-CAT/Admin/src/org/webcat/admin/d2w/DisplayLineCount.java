/*==========================================================================*\
 |  $Id: DisplayLineCount.java,v 1.2 2010/09/26 23:35:42 stedwar2 Exp $
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

package org.webcat.admin.d2w;

import com.webobjects.appserver.*;

import java.sql.*;
import java.util.regex.*;

//-------------------------------------------------------------------------
/**
 * A customized version of
 * {@link er.directtoweb.components.strings.ERD2WDisplayString}
 * for displaying long strings.  It simply shows a count of the number
 * of lines and characters.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/09/26 23:35:42 $
 */
public class DisplayLineCount
    extends er.directtoweb.components.strings.ERD2WDisplayString
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     *
     * @param context The context to use
     */
    public DisplayLineCount(WOContext context)
    {
        super(context);
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Creates all tables in their baseline configuration, as needed.
     * @throws SQLException on error
     */
    public Object objectPropertyValue()
    {
        Object value = super.objectPropertyValue();
        if (value != null)
        {
            String str = value.toString();
            int lineCount = 0;
            Matcher newLineMatcher =
                Pattern.compile("$", Pattern.MULTILINE).matcher(str);
            while (newLineMatcher.find())
            {
                lineCount++;
            }
            value = "" + lineCount + " lines, " + str.length() + " chars";
        }
        return value;
    }
}
