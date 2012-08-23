/*==========================================================================*\
 |  $Id: Messages.java,v 1.1 2009/09/13 12:59:13 aallowat Exp $
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

package net.sf.webcat.eclipse.cxxtest.bfd.i18n;

import org.eclipse.osgi.util.NLS;

//------------------------------------------------------------------------
/**
 * TODO: real description
 *  
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2009/09/13 12:59:13 $
 */
public class Messages extends NLS
{
	private static final String BUNDLE_NAME = "net.sf.webcat.eclipse.cxxtest.bfd.i18n.messages"; //$NON-NLS-1$

	public static String StaticLibraryManager_CheckingLibraryReqs;
	public static String StaticLibraryManager_LookingForBfd;
	public static String StaticLibraryManager_LookingForIbertyBuiltIn;
	public static String StaticLibraryManager_LookingForIbertySeparate;
	public static String StaticLibraryManager_LookingForIntlBuiltIn;
	public static String StaticLibraryManager_LookingForIntlSeparate;
	public static String StaticLibraryManager_MissingLibrariesMsgStart;

	static
	{
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages()
	{
	}
}
