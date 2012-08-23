/*==========================================================================*\
 |  $Id: BrowserEditorInput.java,v 1.3 2010/12/06 21:08:41 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
 |
 |  This file is part of Web-CAT Eclipse Plugins.
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

package org.webcat.eclipse.submitter.ui.editors;

import java.text.MessageFormat;


import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.webcat.eclipse.submitter.ui.i18n.Messages;

//--------------------------------------------------------------------------
/**
 * An editor input that represents HTML code from an HTTP response, which will
 * be displayed by the browser editor.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2010/12/06 21:08:41 $
 */
public class BrowserEditorInput implements IEditorInput
{
	//~ Constructors ..........................................................

	// ----------------------------------------------------------
	/**
	 * Creates a new instance of the BrowserEditorInput class.
	 *
	 * @param project the project whose submission created the response that
	 *     will be displayed
	 * @param html the HTML content of the response that will be displayed
	 */
	public BrowserEditorInput(IProject project, String html)
	{
		this.project = project;
		this.html = html;
	}


	//~ Methods ...............................................................

	// ----------------------------------------------------------
	/**
	 * Returns false to indicate that the editor content doesn't actually
	 * "exist" -- that is, it is not displayed in the most recently used list.
	 */
	public boolean exists()
	{
		return false;
	}


	// ----------------------------------------------------------
	/**
	 * Returns the HTML content generated in the response.
	 * 
	 * @return the HTML content
	 */
	public String getHtml()
	{
		return html;
	}


	// ----------------------------------------------------------
	/**
	 * Returns the project whose submission generated the response.
	 * 
	 * @return the project
	 */
	public IProject getProject()
	{
		return project;
	}


	// ----------------------------------------------------------
	public ImageDescriptor getImageDescriptor()
	{
		return null;
	}


	// ----------------------------------------------------------
	public String getName()
	{
		return MessageFormat.format(Messages.BROWSEREDITOR_TITLE, project
		        .getName());
	}


	// ----------------------------------------------------------
	public IPersistableElement getPersistable()
	{
		return null;
	}


	// ----------------------------------------------------------
	public String getToolTipText()
	{
		return getName();
	}


	// ----------------------------------------------------------
	@SuppressWarnings("rawtypes")
	public Object getAdapter(Class adapter)
	{
		return null;
	}


	//~ Static/instance variables .............................................

	/* The HTML content that should be displayed in the browser. */
	private String html;

	/* The project whose submission created the response that will be
	   displayed in the browser. */
	private IProject project;
}
