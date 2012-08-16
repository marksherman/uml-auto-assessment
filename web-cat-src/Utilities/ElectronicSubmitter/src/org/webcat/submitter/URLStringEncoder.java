/*==========================================================================*\
 |  $Id: URLStringEncoder.java,v 1.1 2010/09/14 18:13:30 aallowat Exp $
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

//--------------------------------------------------------------------------
/**
 * An implementation of {@see IStringEncoder} that performs URL encoding for
 * HTTP protocols.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/09/14 18:13:30 $
 */
public class URLStringEncoder implements IStringEncoder
{
	//~ Methods ...............................................................
	
	// ----------------------------------------------------------
	/**
	 * Encodes a string in URL format.
	 * 
	 * @param string the string to encode
	 * @return the encoded string
	 */
	public String encodeString(String string)
	{
		try
		{
			return URLEncoder.encode(string, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			return string;
		}
	}
}
