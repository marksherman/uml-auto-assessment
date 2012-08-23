/*==========================================================================*\
 |  $Id: DisplayClassNameWithoutPackage.java,v 1.2 2010/09/26 23:35:42 stedwar2 Exp $
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

//-------------------------------------------------------------------------
/**
 * A customized version of
 * {@link er.directtoweb.components.strings.ERD2WDisplayString}
 * for displaying class names--it strips off the leading package qualification.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/09/26 23:35:42 $
 */
public class DisplayClassNameWithoutPackage
    extends er.directtoweb.components.strings.ERD2WDisplayString
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     *
     * @param context The context to use
     */
    public DisplayClassNameWithoutPackage(WOContext context)
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
            int pos = str.lastIndexOf('.');
            if (pos >= 0 && pos < str.length())
            {
                str = str.substring(pos + 1);
                value = str;
            }
        }
        return value;
    }
}
