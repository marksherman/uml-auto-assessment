/*==========================================================================*\
 |  $Id: WebCATDataException.java,v 1.1 2010/05/11 15:52:50 aallowat Exp $
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

package org.webcat.oda.commons;

//-------------------------------------------------------------------------
/**
 * Wraps other BIRT exceptions in a common type so that they can be handled
 * easily in a report preview request or an actual report generation request
 * on Web-CAT.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 * @version $Id: WebCATDataException.java,v 1.1 2010/05/11 15:52:50 aallowat Exp $
 */
public class WebCATDataException extends Exception
{
    //~ Constructors ..........................................................

	// ----------------------------------------------------------
    /**
     * Initializes this exception with the specified cause.
     *
     * @param cause the exception to wrap
     */
    public WebCATDataException(Throwable cause)
    {
        super(cause);
    }


    //~ Static/instance variables .............................................

    /** Serialization version ID. */
	private static final long serialVersionUID = 4405991602545557825L;

}
