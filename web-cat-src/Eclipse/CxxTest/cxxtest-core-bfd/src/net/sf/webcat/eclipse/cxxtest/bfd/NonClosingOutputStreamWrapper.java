/*==========================================================================*\
 |  $Id: NonClosingOutputStreamWrapper.java,v 1.1 2009/09/13 19:31:54 aallowat Exp $
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

package net.sf.webcat.eclipse.cxxtest.bfd;

import java.io.IOException;
import java.io.OutputStream;

//------------------------------------------------------------------------
/**
 * A wrapper for an output stream that implements the close() method as a
 * no-op. This is useful when passing a stream such as System.out to a method
 * that closes it when it's done (like ProcessClosure), but you want to avoid
 * this behavior.
 *  
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2009/09/13 19:31:54 $
 */
public class NonClosingOutputStreamWrapper extends OutputStream
{
	//~ Constructors .........................................................

	// ----------------------------------------------------------
	/**
	 * Creates a new instance of the NonClosingOutputStreamWrapper class that
	 * wraps the specified stream.
	 * 
	 * @param stream the stream to be wrapped
	 */
	public NonClosingOutputStreamWrapper(OutputStream stream)
	{
		this.stream = stream;
	}


	//~ Methods ..............................................................

	// ----------------------------------------------------------
	/**
	 * Overridden to perform no action.
	 */
	@Override
	public void close()
	{
		// Do nothing.
	}


	// ----------------------------------------------------------
	/**
	 * Writes a byte to the output stream.
	 * 
	 * @param b the byte to write
	 * @throws IOException if an I/O exception occurs
	 */
	@Override
	public void write(int b) throws IOException
	{
		stream.write(b);
	}

	
	//~ Static/instance variables ............................................
	
	/* The stream being wrapped. */
	private OutputStream stream;
}
