/*==========================================================================*\
 |  $Id: EddieSyndFeed.java,v 1.2 2010/02/23 17:06:36 stedwar2 Exp $
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

package student.web.internal;

import com.sun.syndication.feed.WireFeed;
import com.sun.syndication.feed.module.Module;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndImage;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import uk.org.catnip.eddie.FeedData;

//-------------------------------------------------------------------------
/**
 *  Just fooling around with a replacement for {@link student.web.RssFeed}
 *  that uses the more forgiving Eddie library to parse feeds--that way,
 *  it won't throw errors.
 *  <p>
 *  This work is not yet complete!
 *  </p>
 *
 *  @author  Stephen Edwards
 *  @author Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2010/02/23 17:06:36 $
 */
public class EddieSyndFeed
    implements SyndFeed, Serializable, Cloneable
{
    // ----------------------------------------------------------
    public EddieSyndFeed(FeedData original)
    {
        innerFeed = original;
    }


    // ----------------------------------------------------------
    @Override
    public Object clone()
    {
        return new EddieSyndFeed(null
            // TODO: (FeedData)innerFeed.clone()
            );
    }


    // ----------------------------------------------------------
    public WireFeed createWireFeed()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public WireFeed createWireFeed(String arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public String getAuthor()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public List<String> getAuthors()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public List<String> getCategories()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public List<String> getContributors()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public String getCopyright()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public String getDescription()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public SyndContent getDescriptionEx()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public String getEncoding()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public List<SyndEntry> getEntries()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public String getFeedType()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public Object getForeignMarkup()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public SyndImage getImage()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public String getLanguage()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public String getLink()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public List<String> getLinks()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public Module getModule(String arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public List<Module> getModules()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public Date getPublishedDate()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public List<String> getSupportedFeedTypes()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public String getTitle()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public SyndContent getTitleEx()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public String getUri()
    {
        // TODO Auto-generated method stub
        return null;
    }


    // ----------------------------------------------------------
    public void setAuthor(String arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void setAuthors(List arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void setCategories(List arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void setContributors(List arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    public void setCopyright(String arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    public void setDescription(String arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    public void setDescriptionEx(SyndContent arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    public void setEncoding(String arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void setEntries(List arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    public void setFeedType(String arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    public void setForeignMarkup(Object arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    public void setImage(SyndImage arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    public void setLanguage(String arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    public void setLink(String arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void setLinks(List arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    @SuppressWarnings("unchecked")
    public void setModules(List arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    public void setPublishedDate(Date arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    public void setTitle(String arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    public void setTitleEx(SyndContent arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    public void setUri(String arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    public void copyFrom(Object arg0)
    {
        // TODO Auto-generated method stub

    }


    // ----------------------------------------------------------
    public Class<?> getInterface()
    {
        // TODO Auto-generated method stub
        return null;
    }


    //~ Instance/static variables .............................................

    private FeedData innerFeed;

    private static final long serialVersionUID = -3966083283645021482L;
}
