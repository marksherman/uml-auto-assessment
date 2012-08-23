/*==========================================================================*\
 |  $Id: DerefereeContext.java,v 1.2 2009/09/13 12:59:29 aallowat Exp $
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
import net.sf.webcat.eclipse.cxxtest.model.IDerefereeLeak;
import net.sf.webcat.eclipse.cxxtest.xml.ElementContext;

import org.xml.sax.Attributes;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2009/09/13 12:59:29 $
 */
public class DerefereeContext extends ElementContext
{
	private DocumentContext document;

	public DerefereeContext(DocumentContext document, Attributes attributes)
	{
		this.document = document;
		
		document.setActualLeakCount(getAttrInt(attributes, ATTR_ACTUAL_LEAK_COUNT));
	}

	public ElementContext startElement(String uri, String localName,
			String qName, Attributes attributes)
	{
		if(TAG_SUMMARY.equals(localName))
			return new SummaryContext(this, attributes);
		else if(TAG_LEAK.equals(localName))
			return new LeakContext(this, attributes);
		else
			return null;
	}
	
	public void addLeak(IDerefereeLeak leak)
	{
		document.addLeak(leak);
	}
	
	public void setSummary(DerefereeSummary info)
	{
		document.setSummary(info);
	}
	
	private int getAttrInt(Attributes attributes, String name)
	{
		return Integer.parseInt(attributes.getValue(name));
	}
	
	
	private static final String TAG_SUMMARY = "summary"; //$NON-NLS-1$

	private static final String TAG_LEAK = "leak"; //$NON-NLS-1$

	private static final String ATTR_ACTUAL_LEAK_COUNT = "actual-leak-count"; //$NON-NLS-1$
}
