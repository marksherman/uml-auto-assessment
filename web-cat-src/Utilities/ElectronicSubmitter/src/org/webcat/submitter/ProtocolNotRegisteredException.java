/*==========================================================================*\
 |  $Id: ProtocolNotRegisteredException.java,v 1.1 2010/03/02 18:38:37 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
 |
 |  This file is part of Web-CAT Electronic Submitter.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU General Public License as published by
 |  the Free Software Foundation; either version 2 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU General Public License along
 |  with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.submitter;

import java.io.IOException;

//--------------------------------------------------------------------------
/**
 * Thrown when a submission is attempted using a transport protocol that is not
 * registered with the submitter.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:37 $
 */
public class ProtocolNotRegisteredException extends IOException
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new instance of the ProtocolNotRegisteredException class.
     *
     * @param scheme the scheme that was requested but not registered
     */
    public ProtocolNotRegisteredException(String scheme)
    {
        this.scheme = scheme;
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the protocol scheme that was requested but not registered.
     *
     * @return the scheme that was requested but not registered
     */
    public String getScheme()
    {
        return scheme;
    }


    // ----------------------------------------------------------
    /**
     * @see Throwable#getMessage()
     */
    @Override
    public String getMessage()
    {
        return "No protocol with the scheme \"" + scheme + "\" was registered";
    }


    //~ Static/instance variables .............................................

    private static final long serialVersionUID = 1L;

    /* The scheme that was requested but not registered. */
    private String scheme;
}
