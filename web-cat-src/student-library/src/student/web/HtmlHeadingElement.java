/*==========================================================================*\
 |  $Id: HtmlHeadingElement.java,v 1.2 2010/02/23 17:06:36 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2007-2010 Virginia Tech
 |
 |  This file is part of the Student-Library.
 |
 |  The Student-Library is free software; you can redistribute it and/or
 |  modify it under the terms of the GNU Lesser General Public License as
 |  published by the Free Software Foundation; either version 3 of the
 |  License, or (at your option) any later version.
 |
 |  The Student-Library is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU Lesser General Public License for more details.
 |
 |  You should have received a copy of the GNU Lesser General Public License
 |  along with the Student-Library; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package student.web;

//-------------------------------------------------------------------------
/**
 *  This interface represents a specialized {@link HtmlElement} that
 *  represents an HTML heading tag--e.g., H1, H2, H3, H4, H5, or H6.
 *  It adds a method to get the heading level as an integer value.
 *
 *  @author  Stephen Edwards
 *  @author Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/02/23 17:06:36 $
 */
public interface HtmlHeadingElement
    extends HtmlElement
{
    // ----------------------------------------------------------
    /**
     * Get the heading level (1-6) of this element.
     * @return The heading's level.
     */
    int getHeadingLevel();
}
