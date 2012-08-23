/*==========================================================================*\
 |  $Id: HtmlElement.java,v 1.3 2010/02/23 17:06:36 stedwar2 Exp $
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
 *  This interface represents an instance of some HTML element (or "tag"),
 *  usually obtained by reading from a web page using a {@link WebBot}.  As
 *  an example, consider this HTML fragment:
 *  <pre>
 *  &lt;img src="logo.jpg" width="100" height="50"/&gt;
 *  </pre>
 *  <p>
 *  When parsed by a {@link WebBot}, this HTML would be converted to a single
 *  HtmlElement object.  That object's "type" would be "img".  It would
 *  have three attributes: "src", "width", and "height".  Each of these
 *  attributes could be retrieved using {@link #getAttributeValue(String)}.
 *  In this case, there would be no text value associated with the tag,
 *  since it has no children (in a DOM sense).  As another example,
 *  consider this second HTML fragment:
 *  </p>
 *  <pre>
 *  &lt;a href="http://www.vt.edu/"&gt;Virginia Tech&lt;/a&gt;
 *  </pre>
 *  <p>
 *  When parsed by a {@link WebBot}, this HTML would be converted to a single
 *  HtmlElement object as well.  That object's type would be "a".  It would
 *  have one attribute: "href".  If you called its
 *  {@link #getText()} method, you would get "Virginia Tech".
 *  </p>
 *
 *  @author  Stephen Edwards
 *  @author Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.3 $, $Date: 2010/02/23 17:06:36 $
 */
public interface HtmlElement
{
    // ----------------------------------------------------------
    /**
     * Get the "type" of this element--that is, the HTML name of the tag,
     * such as "a", "h1", "img", etc.  Note that the element type is
     * case-sensitive in XHTML or XML documents.
     * @return The type of this tag
     */
    String getType();


    // ----------------------------------------------------------
    /**
     * Get the text contained by this element, without any embedded
     * HTML tags.
     * @return The text surrounded by this element in the original
     * document, or null if none
     */
    String getText();


    // ----------------------------------------------------------
    /**
     * Get the entire nested HTML content surrounded by this element,
     * including any nested HTML elements with full markup.
     * @return The nested HTML surrounded by this element in the original
     * document, or null if none
     */
    String getInnerHTML();


    // ----------------------------------------------------------
    /**
     * Check to see if this element has a specific attribute.
     * @param attributeName The attribute to check for
     * @return True if this element has the specified attribute
     */
    boolean hasAttribute(String attributeName);


    // ----------------------------------------------------------
    /**
     * Look up an attribute's value on this element.
     * @param attributeName The attribute to look up
     * @return The attribute's value for this element, or null if there is none
     */
    String getAttributeValue(String attributeName);


    // ----------------------------------------------------------
    /**
     * Get an Iterable that will allow you to cycle through the complete set
     * of attributes on this element.
     * @return An iterator suitable for use in a for-each loop
     */
    Iterable<String> getAttributes();


    // ----------------------------------------------------------
    /**
     * Generate a complete HTML rendering of this element, all its attributes,
     * and any text that it contains.
     * @return A printable version of this element
     */
    String toString();
}
