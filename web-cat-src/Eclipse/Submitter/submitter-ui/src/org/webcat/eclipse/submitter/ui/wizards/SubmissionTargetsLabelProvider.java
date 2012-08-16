/*==========================================================================*\
 |  $Id: SubmissionTargetsLabelProvider.java,v 1.2 2010/12/06 21:08:41 aallowat Exp $
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

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.webcat.eclipse.submitter.ui.SubmitterUIPlugin;
import org.webcat.submitter.targets.SubmissionTarget;

//--------------------------------------------------------------------------
/**
 * The label provider for the submission target tree in the wizard.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/12/06 21:08:41 $
 */
public class SubmissionTargetsLabelProvider extends LabelProvider
{
	//~ Constructors ..........................................................
	
	// ----------------------------------------------------------
	/**
	 * Creates a new instance of the label provider.
	 */
	public SubmissionTargetsLabelProvider()
	{
		folderImage = SubmitterUIPlugin
		        .getImageDescriptor("folder.gif").createImage(); //$NON-NLS-1$
		fileImage = SubmitterUIPlugin
		        .getImageDescriptor("file.gif").createImage(); //$NON-NLS-1$
	}


	//~ Methods ...............................................................

	// ----------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose()
	{
		folderImage.dispose();
		fileImage.dispose();

		super.dispose();
	}


	// ----------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element)
	{
		SubmissionTarget object = (SubmissionTarget)element;
		
		if(object.isContainer())
		{
			return folderImage;
		}
		else
		{
			return fileImage;
		}
	}


	// ----------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element)
	{
		SubmissionTarget object = (SubmissionTarget)element;

		if (object.getName() != null)
		{
			return object.getName();
		}
		else
		{
			return super.getText(element);
		}
	}


	//~ Static/instance variables .............................................

	/* The image used for assignment groups and imported groups. */
	private Image folderImage;

	/* The image used for assignment targets. */
	private Image fileImage;
}
