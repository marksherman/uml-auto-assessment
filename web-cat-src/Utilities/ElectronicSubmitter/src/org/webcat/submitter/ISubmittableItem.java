/*==========================================================================*\
 |  $Id: ISubmittableItem.java,v 1.2 2010/05/14 14:45:43 aallowat Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
 |
 |  This file is part of Web-CAT Electronic Submitter.
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

package org.webcat.submitter;

import java.io.IOException;
import java.io.InputStream;

//--------------------------------------------------------------------------
/**
 * A submittable item is a member of a submittable collection and represents
 * any single piece of that collection that can be submitted, such as a
 * project, source file, or folder.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.2 $ $Date: 2010/05/14 14:45:43 $
 */
public interface ISubmittableItem
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Gets the filename of this submittable item, relative in terms of its
     * location in the package that it will be stored in.
     *
     * @return the filename of the submittable item, relative to the root of
     *     the destination package
     */
    String getFilename();


    // ----------------------------------------------------------
    /**
     * Gets the kind of item represented by this object. A submittable item can
     * represent either a folder for containing other files or a file itself.
     *
     * @return the kind of item represented by this object
     */
    SubmittableItemKind getKind();


    // ----------------------------------------------------------
    /**
     * Gets an input stream from which to read the contents of this item, if
     * {@link #getKind()} is equal to {@link SubmittableItemKind#FILE}.
     *
     * @return the input stream from which to read the contents of this item
     * @throws IOException if an I/O exception occurred
     */
    InputStream getStream() throws IOException;


    // ----------------------------------------------------------
    /**
     * Gets the children of this submittable item. If an item does not have any
     * children, it should return an empty array, not null.
     *
     * @return the children of this submittable item
     */
    ISubmittableItem[] getChildren();
}
