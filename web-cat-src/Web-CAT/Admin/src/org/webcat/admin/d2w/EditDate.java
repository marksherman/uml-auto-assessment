/*==========================================================================*\
 |  $Id: EditDate.java,v 1.2 2010/09/26 23:35:42 stedwar2 Exp $
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

import java.util.TimeZone;
import com.webobjects.appserver.*;

//-------------------------------------------------------------------------
/**
 * A customized version of
 * {@link er.directtoweb.components.ERDEditDateJavascript}
 * that uses the user's specified date formatting and selected time zone,
 * and also uses Web-CAT's preferred date picker widget.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/09/26 23:35:42 $
 */
public class EditDate
    extends er.directtoweb.components.ERDCustomEditComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     *
     * @param context The context to use
     */
    public EditDate(WOContext context)
    {
        super(context);
    }


    // ----------------------------------------------------------
    public TimeZone timeZone()
    {
        return TimeZone.getTimeZone(
                (String) session().valueForKeyPath("user.timeZoneName"));
    }
}
