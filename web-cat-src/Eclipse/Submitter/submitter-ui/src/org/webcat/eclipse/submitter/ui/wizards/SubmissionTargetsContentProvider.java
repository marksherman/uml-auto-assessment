/*==========================================================================*\
 |  $Id: SubmissionTargetsContentProvider.java,v 1.2 2010/12/06 21:08:41 aallowat Exp $
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

package org.webcat.eclipse.submitter.ui.wizards;

import java.util.ArrayList;


import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.webcat.eclipse.submitter.ui.dialogs.SubmissionParserErrorDialog;
import org.webcat.submitter.targets.ImportGroupTarget;
import org.webcat.submitter.targets.SubmissionTarget;

//--------------------------------------------------------------------------
/**
 * The content provider for the tree that displays the submission targets in
 * the wizard.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/12/06 21:08:41 $
 */
public class SubmissionTargetsContentProvider implements ITreeContentProvider
{
	//~ Methods ...............................................................

	// ----------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object parentElement)
	{
		SubmissionTarget obj = (SubmissionTarget)parentElement;

		ArrayList<SubmissionTarget> children =
			new ArrayList<SubmissionTarget>();
		computeChildren(obj, children);
		return children.toArray();
	}


	// ----------------------------------------------------------
	/**
	 * Computes the visible children of the specified node, displaying a message
	 * to the user if any errors occur.
	 */
	private void computeChildren(SubmissionTarget obj,
			                     ArrayList<SubmissionTarget> list)
	{
		try
		{
			SubmissionTarget[] children = obj.getLogicalChildren();
			for (int i = 0; i < children.length; i++)
			{
				SubmissionTarget child = children[i];

				if (!child.isHidden())
				{
					if (child.isContainer() && !child.isNested())
						computeChildren(child, list);
					else
						list.add(child);
				}
			}
		}
		catch (Throwable e)
		{
			SubmissionParserErrorDialog dlg = new SubmissionParserErrorDialog(
			        null, e);
			dlg.open();

			list.clear();
		}
	}


	// ----------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element)
	{
		return ((SubmissionTarget) element).parent();
	}


	// ----------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element)
	{
		if (element instanceof ImportGroupTarget)
		{
			// If it's an imported group, it might have children.
			// Chances are it does. We want expand logic here.

			return true;
		}
		else
		{
			ArrayList<SubmissionTarget> children =
				new ArrayList<SubmissionTarget>();
			computeChildren((SubmissionTarget)element, children);
			return children.size() > 0;
		}
	}


	// ----------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement)
	{
		return getChildren(root);
	}


	// ----------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose()
	{
		// Do nothing.
	}


	// ----------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
	{
		root = (SubmissionTarget) newInput;
	}

	
	//~ Static/instance variables .............................................

	/* The root of the submission target tree. */
	private SubmissionTarget root;
}
