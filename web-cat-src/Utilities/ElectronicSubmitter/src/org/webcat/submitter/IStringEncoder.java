/*==========================================================================*\
 |  $Id: IStringEncoder.java,v 1.1 2010/09/14 18:13:30 aallowat Exp $
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

//--------------------------------------------------------------------------
/**
 * Protocols should implement this interface if they need to encode parameters
 * in a {@link SubmissionManifest} before passing them to the transport. As an
 * example, a {@link URLStringEncoder} is provided which is used by the
 * {@link HttpProtocol} and {@link HttpsProtocol} classes.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/09/14 18:13:30 $
 */
public interface IStringEncoder
{
	//~ Methods ...............................................................
	
	// ----------------------------------------------------------
	/**
	 * Encodes a string.
	 * 
	 * @param string the string to encode
	 * @return the encoded string
	 */
	String encodeString(String string);
}
