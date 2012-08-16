/*==========================================================================*\
 |  $Id: DocumentContext.java,v 1.5 2009/09/13 12:59:29 aallowat Exp $
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

import java.util.Vector;

import net.sf.webcat.eclipse.cxxtest.internal.model.DerefereeSummary;
import net.sf.webcat.eclipse.cxxtest.model.IDerefereeLeak;
import net.sf.webcat.eclipse.cxxtest.model.IDerefereeSummary;
import net.sf.webcat.eclipse.cxxtest.xml.ElementContext;

import org.xml.sax.Attributes;

/**
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.5 $ $Date: 2009/09/13 12:59:29 $
 */
public class DocumentContext extends ElementContext
{
	private Vector<IDerefereeLeak> leaks;
	
	private DerefereeSummary summary;

	private int actualLeakCount;
	
	public DocumentContext()
	{
		leaks = new Vector<IDerefereeLeak>();
	}

	public ElementContext startElement(String uri, String localName,
			String qName, Attributes attributes)
	{
		if(TAG_DEREFEREE.equals(localName))
			return new DerefereeContext(this, attributes);
		else
			return null;
	}

	public void addLeak(IDerefereeLeak leak)
	{
		leaks.add(leak);
	}

	public IDerefereeLeak[] getLeaks()
	{
		return leaks.toArray(new IDerefereeLeak[leaks.size()]);
	}

	public void setSummary(DerefereeSummary info)
	{
		summary = info;
	}
	
	public IDerefereeSummary getSummary()
	{
		summary.setLeaks(getLeaks());
		summary.setActualLeakCount(actualLeakCount);
		return summary;
	}
	
	public void setActualLeakCount(int value)
	{
		actualLeakCount = value;
	}


	private static final String TAG_DEREFEREE = "dereferee"; //$NON-NLS-1$
}
