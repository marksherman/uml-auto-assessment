/*==========================================================================*\
 |  $Id: RssEntity.java,v 1.2 2010/02/23 17:06:36 stedwar2 Exp $
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

import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.util.List;

//-------------------------------------------------------------------------
/**
 *  This interface defines the common features of RSS entities like
 *  {@link RssEntry} objects and {@link RssFeed} objects.
 *
 *  @version 2007.09.01
 *  @author Stephen Edwards
 */
public interface RssEntity
    extends Serializable, Cloneable
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     *  Get this entity's author.  If there are multiple authors,
     *  this method returns the first one.
     *  @return This entity's author
     */
    String getAuthor();


    // ----------------------------------------------------------
    /**
     *  Set this entity's author.
     *  @param author This entity's author
     */
    void setAuthor(String author);


    // ----------------------------------------------------------
    /**
     *  Get this entity's authors.
     *  @return This entity's authors
     */
    List<String> getAuthors();


    // ----------------------------------------------------------
    /**
     *  Set this entity's authors.
     *  @param authors A list of this entity's authors
     */
    void setAuthors(List<String> authors);


    // ----------------------------------------------------------
    /**
     *  Get this entity's publication date.
     *  @return This entity's date
     */
    Date getDate();


    // ----------------------------------------------------------
    /**
     *  Set this entity's publication date.
     *  @param date This entity's date
     */
    void setDate(Date date);


    // ----------------------------------------------------------
    /**
     *  Get this entity's description.
     *  @return This entity's description
     */
    String getDescription();


    // ----------------------------------------------------------
    /**
     *  Set this entity's description.
     *  @param description The new description
     */
    void setDescription(String description);


    // ----------------------------------------------------------
    /**
     *  Get this entity's link as a URL.
     *  @return The link's URL, or null if there is none
     */
    URL getLink();


    // ----------------------------------------------------------
    /**
     *  Set this entity's link.
     *  @param link The new URL to use
     */
    void setLink(URL link);


    // ----------------------------------------------------------
    /**
     *  Get this entity's title.
     *  @return The entity's title
     */
    String getTitle();


    // ----------------------------------------------------------
    /**
     *  Set this entity's title.
     *  @param title The entity's title
     */
    void setTitle(String title);


}
