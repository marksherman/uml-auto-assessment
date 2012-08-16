/*==========================================================================*\
 |  $Id: SummaryContext.java,v 1.4 2009/09/13 12:59:29 aallowat Exp $
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

package net.sf.webcat.eclipse.cxxtest.xml.memstats;

import net.sf.webcat.eclipse.cxxtest.internal.model.DerefereeSummary;
import net.sf.webcat.eclipse.cxxtest.xml.ElementContext;

import org.xml.sax.Attributes;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.4 $ $Date: 2009/09/13 12:59:29 $
 */
public class SummaryContext extends ElementContext
{
	public SummaryContext(DerefereeContext parent, Attributes attributes)
	{
		int totalBytes = getAttrInt(attributes, ATTR_TOTAL_BYTES_ALLOCATED);
		int maxBytes = getAttrInt(attributes, ATTR_MAX_BYTES_IN_USE);

		int callsNew = getAttrInt(attributes, ATTR_CALLS_TO_NEW);
		int callsDelete = getAttrInt(attributes, ATTR_CALLS_TO_DELETE);
		int callsArrayNew = getAttrInt(attributes, ATTR_CALLS_TO_ARRAY_NEW);
		int callsArrayDelete = getAttrInt(attributes, ATTR_CALLS_TO_ARRAY_DELETE);
		int callsDeleteNull = getAttrInt(attributes, ATTR_CALLS_TO_DELETE_NULL);
		
		DerefereeSummary info = new DerefereeSummary(totalBytes, maxBytes,
				callsNew, callsDelete, callsArrayNew,
				callsArrayDelete, callsDeleteNull);
		parent.setSummary(info);
	}
	
	private int getAttrInt(Attributes attributes, String name)
	{
		return Integer.parseInt(attributes.getValue(name));
	}
	

	private static final String ATTR_TOTAL_BYTES_ALLOCATED = "total-bytes-allocated"; //$NON-NLS-1$
	private static final String ATTR_MAX_BYTES_IN_USE = "max-bytes-in-use"; //$NON-NLS-1$
	private static final String ATTR_CALLS_TO_NEW = "calls-to-new"; //$NON-NLS-1$
	private static final String ATTR_CALLS_TO_DELETE = "calls-to-delete"; //$NON-NLS-1$
	private static final String ATTR_CALLS_TO_ARRAY_NEW = "calls-to-array-new"; //$NON-NLS-1$
	private static final String ATTR_CALLS_TO_ARRAY_DELETE = "calls-to-array-delete"; //$NON-NLS-1$
	private static final String ATTR_CALLS_TO_DELETE_NULL = "calls-to-delete-null"; //$NON-NLS-1$
}
