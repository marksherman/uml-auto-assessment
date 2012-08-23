/*==========================================================================*\
 |  $Id: PixelConverter.java,v 1.2 2010/12/06 21:08:41 aallowat Exp $
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

package org.webcat.eclipse.submitter.ui;

import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Control;

import org.eclipse.jface.dialogs.Dialog;

//--------------------------------------------------------------------------
/**
 * A helper class to convert dialog units to pixels; used to create
 * appropriately sized buttons in dialogs and wizards. This code is adapted
 * from code in the SWT source.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/12/06 21:08:41 $
 */
public class PixelConverter
{
	//~ Constructors ..........................................................

	// ----------------------------------------------------------
	/**
	 * Initializes a new instance of the PixelConverter class for the
	 * specified control.
	 * 
	 * @param control the control for which the PixelConverter is being
	 *     created
	 */
	public PixelConverter(Control control)
	{
		GC gc = new GC(control);
		gc.setFont(control.getFont());
		fontMetrics = gc.getFontMetrics();
		gc.dispose();
	}


	//~ Methods ...............................................................

	// ----------------------------------------------------------
	/**
	 * Gets the number of pixels corresponding to the height of the given
	 * number of characters.
	 * 
	 * @param chars the number of characters
	 * @return the number of pixels
	 */
	public int convertHeightInCharsToPixels(int chars)
	{
		return Dialog.convertHeightInCharsToPixels(fontMetrics, chars);
	}


	// ----------------------------------------------------------
	/**
	 * Gets the number of pixels corresponding to the given number of
	 * horizontal dialog units.
	 * 
	 * @param dlus the number of horizontal dialog units
	 * @return the number of pixels
	 */
	public int convertHorizontalDLUsToPixels(int dlus)
	{
		return Dialog.convertHorizontalDLUsToPixels(fontMetrics, dlus);
	}


	// ----------------------------------------------------------
	/**
	 * Gets the number of pixels corresponding to the given number of vertical
	 * dialog units.
	 * 
	 * @param dlus the number of vertical dialog units
	 * @return the number of pixels
	 */
	public int convertVerticalDLUsToPixels(int dlus)
	{
		return Dialog.convertVerticalDLUsToPixels(fontMetrics, dlus);
	}


	// ----------------------------------------------------------
	/**
	 * Gets the number of pixels corresponding to the width of the given
	 * number of characters.
	 * 
	 * @param chars the number of characters
	 * @return the number of pixels
	 */
	public int convertWidthInCharsToPixels(int chars)
	{
		return Dialog.convertWidthInCharsToPixels(fontMetrics, chars);
	}


	//~ Static/instance variables .............................................

	/* A FontMetrics instance derived from the font used by the control that
	   instantiated this PixelConverter. */
	private FontMetrics fontMetrics;
}
