/*==========================================================================*\
 |  $Id: ContextualSAXHandler.java,v 1.3 2009/09/13 12:59:29 aallowat Exp $
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

package net.sf.webcat.eclipse.cxxtest.xml;

import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Stack;

import net.sf.webcat.eclipse.cxxtest.i18n.Messages;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A SAX content handler that keeps a stack of "contexts", where each context
 * is pushed upon entering a new tag and popped upon exiting the tag. Users
 * of this class should subclass an ElementContext for each tag type in their
 * document and put appropriate attributes/children/content handling in that
 * context class.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2009/09/13 12:59:29 $
 */
public class ContextualSAXHandler extends DefaultHandler
{
	private class ContextEntry
	{
		private ElementContext context;
		private String localName;
		
		public ContextEntry(ElementContext context, String localName)
		{
			this.context = context;
			this.localName = localName;
		}
		
		public ElementContext getContext()
		{
			return context;
		}
		
		public String getLocalName()
		{
			return localName;
		}
	}

	private Stack<ContextEntry> contextStack;
	
	private Locator locator;
	
	private ElementContext initialContext;
	
	public ContextualSAXHandler(ElementContext initialContext)
	{
		this.initialContext = initialContext;

		contextStack = new Stack<ContextEntry>();
	}

	public void setDocumentLocator(Locator locator)
	{
		this.locator = locator;
	}

	public void startDocument()
	{
		ContextEntry entry = new ContextEntry(initialContext, ""); //$NON-NLS-1$
		contextStack.push(entry);
	}

	public void endDocument() throws SAXException
	{
		if(contextStack.size() != 1)
		{
			String ctxPath = getCurrentContextPath();

			String msg = MessageFormat.format(
					Messages.ContextualSAXHandler_StackNotEmptyAtEnd,
					new Object[] { ctxPath });

			throw new SAXParseException(msg, locator);
		}
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{
		ContextEntry entry = contextStack.peek();
		ElementContext newContext = null;
		
		try
		{
			newContext = entry.getContext().startElement(
					uri, localName, qName, attributes);
		}
		catch(SAXException e)
		{
			throw new SAXParseException(null, locator, e);
		}

		if(newContext == null)
		{
			String ctxPath = getCurrentContextPath();
			
			String msg = MessageFormat.format(
					Messages.ContextualSAXHandler_UnrecognizedTag,
					new Object[] { localName, ctxPath });

			throw new SAXParseException(msg, locator);
		}

		ContextEntry newEntry = new ContextEntry(newContext, localName);
		contextStack.push(newEntry);
	}

	private String getCurrentContextPath()
	{
		StringBuffer buf = new StringBuffer();

		boolean first = true;

		Enumeration<ContextEntry> e = contextStack.elements();
		while(e.hasMoreElements())
		{
			ContextEntry entry = e.nextElement();
			
			if(first)
				first = false;
			else
				buf.append('/');

			buf.append(entry.getLocalName());
		}
		
		return buf.toString();
	}

	public void endElement(String uri, String localName, String qName)
	throws SAXException
	{
		if(contextStack.size() == 1)
		{
			throw new SAXParseException(
					Messages.ContextualSAXHandler_StackPrematurelyEmpty,
					locator);
		}

		ContextEntry entry = contextStack.pop();
		
		try
		{
			entry.getContext().endElement(uri, localName, qName);
		}
		catch(SAXException e)
		{
			throw new SAXParseException(null, locator, e);
		}
	}
	
	public void characters(char[] chars, int start, int length)
	throws SAXException
	{
		ContextEntry entry = contextStack.peek();
		
		try
		{
			entry.getContext().characters(chars, start, length);
		}
		catch(SAXException e)
		{
			throw new SAXParseException(null, locator, e);
		}
	}
}
