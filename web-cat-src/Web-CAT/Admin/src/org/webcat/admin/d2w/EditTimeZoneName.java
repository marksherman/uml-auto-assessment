/*==========================================================================*\
 |  $Id: EditTimeZoneName.java,v 1.2 2010/09/26 23:35:42 stedwar2 Exp $
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

import org.webcat.core.*;
import com.webobjects.appserver.*;
import com.webobjects.foundation.*;

//-------------------------------------------------------------------------
/**
 * A customized edit component for selecting time zone names.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/09/26 23:35:42 $
 */
public class EditTimeZoneName
    extends er.directtoweb.components.ERDCustomEditComponent
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     *
     * @param context The context to use
     */
    public EditTimeZoneName(WOContext context)
    {
        super(context);
    }


    //~ KVC Attributes (must be public) .......................................

    public AuthenticationDomain.TimeZoneDescriptor zone;


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public AuthenticationDomain.TimeZoneDescriptor selectedZone()
    {
        if (selectedZone == null)
        {
            NSArray<AuthenticationDomain.TimeZoneDescriptor> zones =
                AuthenticationDomain.availableTimeZones();
            String name = (String)objectPropertyValue();
            for (AuthenticationDomain.TimeZoneDescriptor descriptor : zones)
            {
                System.out.println("checking " + name + " against "
                    + descriptor.id);
                if (descriptor.id.equals(name))
                {
                    selectedZone = descriptor;
                    break;
                }
            }
        }
        return selectedZone;
    }


    // ----------------------------------------------------------
    public void setSelectedZone(AuthenticationDomain.TimeZoneDescriptor aZone)
    {
        selectedZone = aZone;
        setObjectPropertyValue(aZone.id);
    }


    //~ Instance/static variables .............................................

    private AuthenticationDomain.TimeZoneDescriptor selectedZone;
}
