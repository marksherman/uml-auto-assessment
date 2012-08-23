/*==========================================================================*\
 |  $Id: MockSubmittableItem.java,v 1.1 2010/03/02 18:38:53 aallowat Exp $
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

package org.webcat.submitter.tests.utility;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.webcat.submitter.ISubmittableItem;
import org.webcat.submitter.SubmittableItemKind;

//--------------------------------------------------------------------------
/**
 * A mock submittable item for unit testing. A forest of these objects can be
 * constructed to mimic a set of directories and files on a file system.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author: aallowat $
 * @version $Revision: 1.1 $ $Date: 2010/03/02 18:38:53 $
 */
public class MockSubmittableItem implements ISubmittableItem
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a new mock submittable item with the specified name and
     * kind.
     *
     * @param name the name of the mock item, relative to the destination
     *     package
     * @param kind the kind of the mock item
     */
    public MockSubmittableItem(String name, SubmittableItemKind kind)
    {
        this.name = name;
        this.kind = kind;
        children = new ArrayList<ISubmittableItem>();
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Adds the specified mock item as a child of this item.
     *
     * @param child the mock item to add as a child
     */
    public void addChild(MockSubmittableItem child)
    {
        children.add(child);
    }


    // ----------------------------------------------------------
    /**
     * Sets the content of the mock item. If this mock item is mimicking a
     * file, this value acts as the content of that file.
     *
     * @param value the content of the mock item
     */
    public void setContent(String value)
    {
        content = value;
    }


    // ----------------------------------------------------------
    /**
     * @see ISubmittableItem#getChildren()
     */
    public ISubmittableItem[] getChildren()
    {
        ISubmittableItem[] items = new ISubmittableItem[children.size()];
        return children.toArray(items);
    }


    // ----------------------------------------------------------
    /**
     * @see ISubmittableItem#getFilename()
     */
    public String getFilename()
    {
        return name;
    }


    // ----------------------------------------------------------
    /**
     * @see ISubmittableItem#getKind()
     */
    public SubmittableItemKind getKind()
    {
        return kind;
    }


    // ----------------------------------------------------------
    /**
     * @see ISubmittableItem#getStream()
     */
    public InputStream getStream() throws IOException
    {
        byte[] bytes = content.getBytes();
        return new ByteArrayInputStream(bytes);
    }


    //~ Static/instance variables .............................................

    /* The name of the item. */
    private String name;

    /* The kind of the item -- a file or a folder. */
    private SubmittableItemKind kind;

    /* Text content for the item (mimics file contents). */
    private String content;

    /* The item's children. */
    private List<ISubmittableItem> children;
}
