/*==========================================================================*\
 |  $Id: BrowserEditor.java,v 1.3 2010/12/06 21:08:41 aallowat Exp $
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


import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.webcat.eclipse.submitter.ui.i18n.Messages;

//--------------------------------------------------------------------------
/**
 * Implements an Eclipse editor that hosts the SWT Browser widget, so an HTML
 * page can be displayed in the workbench editor window.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.3 $ $Date: 2010/12/06 21:08:41 $
 */
public class BrowserEditor extends EditorPart
{
	//~ Methods ...............................................................

	// ----------------------------------------------------------
	/**
	 * Called when the editor is initialized.
	 */
	public void init(IEditorSite site, IEditorInput input)
	        throws PartInitException
	{
		if (!(input instanceof BrowserEditorInput))
		{
			throw new PartInitException(
			        Messages.BROWSEREDITOR_INVALID_INPUT);
		}

		setSite(site);
		setInput(input);

		editorInput = (BrowserEditorInput)input;

		setPartName(MessageFormat.format(Messages.BROWSEREDITOR_TITLE,
		        editorInput.getProject().getName()));
	}


	// ----------------------------------------------------------
	/**
	 * Creates the embedded browser widget.
	 */
	public void createPartControl(Composite parent)
	{
		parent.setLayout(new FillLayout());
		browser = new Browser(parent, SWT.NONE);

		String html = editorInput.getHtml();
		
		if(html != null)
		{
			browser.setText(html);
		}
	}


	// ----------------------------------------------------------
	/**
	 * Saving is not implemented.
	 */
	public void doSave(IProgressMonitor monitor)
	{
		// Do nothing.
	}


	// ----------------------------------------------------------
	/**
	 * Saving is not implemented.
	 */
	public void doSaveAs()
	{
		// Do nothing.
	}


	// ----------------------------------------------------------
	/**
	 * Saving is not implemented, so the editor is never dirty.
	 */
	public boolean isDirty()
	{
		return false;
	}


	// ----------------------------------------------------------
	/**
	 * Saving is not implemented.
	 */
	public boolean isSaveAsAllowed()
	{
		return false;
	}


	// ----------------------------------------------------------
	/**
	 * Sets the focus to the embedded browser control when the editor is
	 * activated.
	 */
	public void setFocus()
	{
		if(browser != null)
		{
			browser.setFocus();
		}
	}


	//~ Static/instance variables .............................................

	/** The extension identifier for this editor. */
	public static final String ID =
		"net.sf.webcat.eclipse.submitter.ui.editors.BrowserEditor"; //$NON-NLS-1$
	
	/* The embedded browser widget. */
	private Browser browser;

	/* The editor input used to initialize this editor. */
	private BrowserEditorInput editorInput;
}
