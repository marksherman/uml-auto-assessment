/*==========================================================================*\
 |  $Id: ChoiceOptionPredicate.java,v 1.2 2009/09/13 12:59:28 aallowat Exp $
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

package net.sf.webcat.eclipse.cxxtest.internal.options;


/**
 * An option predicate that matches values that are in an array of values.
 * 
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2009/09/13 12:59:28 $
 */
public class ChoiceOptionPredicate implements IOptionPredicate
{
	// === Methods ============================================================

	// ------------------------------------------------------------------------
	/**
	 * Creates a new ChoiceOptionPredicate with the specified array of choices.
	 * 
	 * @param values
	 *            the values that this predicate will match against.
	 */
	public ChoiceOptionPredicate(String[] values)
	{
		this.values = values;
	}


	// ------------------------------------------------------------------------
	/**
	 * Returns true if the value is among the values that this predicate was
	 * created with.
	 * 
	 * @param value
	 *            the String to test
	 * 
	 * @return true if the value matches a value in the array; otherwise,
	 *         false.
	 */
	public boolean accept(String value)
	{
		for(String choice : values)
			if(choice.equals(value))
				return true;
		
		return false;
	}


	// === Instance Variables =================================================

	/**
	 * The choices to match against.
	 */
	private String[] values;
}
