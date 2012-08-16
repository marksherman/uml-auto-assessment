/*==========================================================================*\
 |  $Id: RssFeed.java,v 1.4 2011/03/07 14:08:04 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2007-2011 Virginia Tech
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

import static student.testingsupport.SystemIOUtilities.isOnServer;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedInput;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//-------------------------------------------------------------------------
/**
 *  This class represents a syndication feed that contains a list of
 *  {@link RssEntry} objects.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.4 $, $Date: 2011/03/07 14:08:04 $
 */
public class RssFeed
    implements RssEntity, Serializable, Cloneable
{
    //~ Instance/static variables .............................................

    private static final long serialVersionUID = -660537505655423375L;

    private URL loadedFrom;

    /** The raw ROME feed. */
    protected SyndFeed nativeFeed;

    /** The list of entries. */
    protected List<RssEntry> entries;

    /** The feed's link URL (set lazily in {@link #getLink()}. */
    protected URL feedLink;

    /** The feed's image URL (set lazily in {@link #getImageLink()}. */
    protected URL imageLink;

    /** The feed site's favicon URL (set lazily in {@link #getIconLink()}. */
    protected URL iconLink;


    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new RssFeed object that is completely empty.
     */
    public RssFeed()
    {
        this(new SyndFeedImpl());
    }


    // ----------------------------------------------------------
    /**
     * Creates a new RssFeed object by reading from the given URL.
     * @param url The URL to read from, as a string
     */
    public RssFeed(String url)
    {
        this(readFeed(url));
        try
        {
            loadedFrom = new URL(url);
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }


    // ----------------------------------------------------------
    /**
     * Creates a new RssFeed object by reading from the given URL.
     * @param url The URL to read from, as a string
     */
    public RssFeed(URL url)
    {
        this(readFeed(url));
        loadedFrom = url;
    }


    // ----------------------------------------------------------
    /**
     * Creates a new RssFeed object by copying from an existing one.
     * Subclasses should override this constructor if they add their
     * own data, so that their internal subclass data gets initialized
     * properly.
     * @param existingFeed The feed to copy
     */
    public RssFeed(RssFeed existingFeed)
    {
        this(existingFeed.nativeFeed);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new RssFeed object from an existing ROME feed.
     * @param feed The ROME feed to wrap
     */
    RssFeed(SyndFeed feed)
    {
        nativeFeed = feed;
        if (feed == null)
        {
            nativeFeed = new SyndFeedImpl();
        }
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     *  Get this feed's author.  If there are multiple authors,
     *  this method returns the first one.
     *  @return This feed's author
     */
    public String getAuthor()
    {
        return nativeFeed.getAuthor();
    }


    // ----------------------------------------------------------
    /**
     *  Set this feed's author.
     *  @param author This feed's author
     */
    public void setAuthor(String author)
    {
        nativeFeed.setAuthor(author);
    }


    // ----------------------------------------------------------
    /**
     *  Get this feed's authors.
     *  @return This feed's authors
     */
    @SuppressWarnings("unchecked")
    public List<String> getAuthors()
    {
        return nativeFeed.getAuthors();
    }


    // ----------------------------------------------------------
    /**
     *  Set this feed's authors.
     *  @param authors A list of this feed's authors
     */
    public void setAuthors(List<String> authors)
    {
        nativeFeed.setAuthors(authors);
    }


    // ----------------------------------------------------------
    /**
     *  Get this feed's publication date.
     *  @return This feed's date
     */
    public Date getDate()
    {
        return nativeFeed.getPublishedDate();
    }


    // ----------------------------------------------------------
    /**
     *  Set this feed's publication date.
     *  @param date This feed's date
     */
    public void setDate(Date date)
    {
        nativeFeed.setPublishedDate(date);
    }


    // ----------------------------------------------------------
    /**
     *  Get this feed's description.
     *  @return This feed's description
     */
    public String getDescription()
    {
        return nativeFeed.getDescription();
    }


    // ----------------------------------------------------------
    /**
     *  Set this feed's description.
     *  @param description The new description
     */
    public void setDescription(String description)
    {
        nativeFeed.setDescription(description);
    }


    // ----------------------------------------------------------
    /**
     *  Get this feed's entries.
     *  @return This feed's entries
     */
    public List<RssEntry> getEntries()
    {
        if (entries == null)
        {
            convertEntryList();
        }
        return entries;
    }


    // ----------------------------------------------------------
    /**
     *  Add a new entry to this feed.
     *  @param entry The new entry to add
     */
    @SuppressWarnings("unchecked")
    public void addEntry(RssEntry entry)
    {
        List<RssEntry> myEntries = getEntries();
        if (myEntries == null)
        {
            myEntries = new ArrayList<RssEntry>();
            myEntries.add(entry);
            setEntries(myEntries);
        }
        else
        {
            myEntries.add(entry);
            nativeFeed.getEntries().add(entry.nativeEntry);
        }
    }


    // ----------------------------------------------------------
    /**
     *  Set this feed's entries.
     *  @param entries This feed's entries
     */
    @SuppressWarnings("unchecked")
    public void setEntries(List<RssEntry> entries)
    {
        this.entries = entries;
        if (nativeFeed.getEntries() == null)
        {
            nativeFeed.setEntries(new ArrayList<SyndEntry>(entries.size()));
        }
        List<SyndEntry> nativeEntries = nativeFeed.getEntries();
        for (RssEntry entry : entries)
        {
            nativeEntries.add(entry.nativeEntry);
        }
    }


    // ----------------------------------------------------------
    /**
     *  Get the link for the site icon belonging to this feed's home site.
     *  This icon is often used in browsers and so on to identify the
     *  feed's site.
     *  @return The feed's icon URL, which points to the home site's
     *  "favicon.ico" icon (which may not even exist, of course)
     */
    public URL getIconLink()
    {
        if (iconLink == null)
        {
            URL link = getLink();
            if (link != null)
            {
                try
                {
                    iconLink = new URL(link, "/favicon.ico");
                }
                catch (MalformedURLException e)
                {
                    if (!isOnServer())
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return iconLink;
    }


    // ----------------------------------------------------------
    /**
     *  Set the link for the site icon belonging to this feed's home site.
     *  @param link The new URL to use
     */
    public void setIconLink(URL link)
    {
        iconLink = link;
    }


    // ----------------------------------------------------------
    /**
     *  Get the link for this feed's image as a URL.
     *  @return The feed's image URL, or null if there is none
     */
    public URL getImageLink()
    {
        if (imageLink == null)
        {
            String link = nativeFeed.getImage().getUrl();
            if (link != null)
            {
                try
                {
                    imageLink = new URL(link);
                }
                catch (MalformedURLException e)
                {
                    if (!isOnServer())
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return imageLink;
    }


    // ----------------------------------------------------------
    /**
     *  Set this link for this feed's image.
     *  @param link The new URL to use
     */
    public void setImageLink(URL link)
    {
        imageLink = link;
        nativeFeed.getImage().setUrl(link == null ? null : link.toString());
    }


    // ----------------------------------------------------------
    /**
     *  Get the link as a URL where this feed was loaded from.
     *  @return The link's URL, or null if there is none
     */
    public URL getLoadedFromLink()
    {
        return loadedFrom;
    }


    // ----------------------------------------------------------
    /**
     *  Set the value this feed believes it was loaded from (normally
     *  set by the constructor).
     *  @param link The new URL to use
     */
    public void setLoadedFromLink(URL link)
    {
        loadedFrom = link;
    }


    // ----------------------------------------------------------
    /**
     *  Get the link (as a URL) where this feed is officially published.
     *  @return The link's URL, or null if there is none
     */
    public URL getLink()
    {
        if (feedLink == null)
        {
            String link = nativeFeed.getLink();
            if (link != null)
            {
                try
                {
                    feedLink = new URL(link);
                }
                catch (MalformedURLException e)
                {
                    if (!isOnServer())
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return feedLink;
    }


    // ----------------------------------------------------------
    /**
     *  Set this feed's publication link.
     *  @param link The new URL to use
     */
    public void setLink(URL link)
    {
        feedLink = link;
        nativeFeed.setLink(link == null ? null : link.toString());
    }


    // ----------------------------------------------------------
    /**
     *  Get this feed's title.
     *  @return The feed's title
     */
    public String getTitle()
    {
        return nativeFeed.getTitle();
    }


    // ----------------------------------------------------------
    /**
     *  Set this feed's title.
     *  @param title The feed's title
     */
    public void setTitle(String title)
    {
        nativeFeed.setTitle(title);
    }


    // ----------------------------------------------------------
    /**
     *  Clone this object.
     *  @return a copy of this object
     */
    public Object clone()
    {
        try
        {
            return new RssFeed((SyndFeed)nativeFeed.clone());
        }
        catch (CloneNotSupportedException e)
        {
            throw new RuntimeException(e);
        }
    }


    // ----------------------------------------------------------
    /**
     *  Generate a printable version of this feed.
     *  @return the string representation of this object
     */
    public String toString()
    {
        return nativeFeed.toString();
    }


    //~ Protected Methods .....................................................

    // ----------------------------------------------------------
    /**
     *  Convert the native ROME feed's list of entities into a proper
     *  list of wrapped {@link RssEntry} objects, and store the result
     *  in the {@link #entries} field.  If the native feed
     *  is null, this method leaves the entries field null as well.
     */
    protected void convertEntryList()
    {
        List<RssEntry> result = null;
        List<?> nativeEntries = nativeFeed.getEntries();
        if (nativeEntries != null)
        {
            result = new ArrayList<RssEntry>(nativeEntries.size());
            for (Object entry : nativeEntries)
            {
                result.add(new RssEntry((SyndEntry)entry));
            }
        }
        entries = result;
    }


    // ----------------------------------------------------------
    /**
     *  Create a ROME feed by reading from the given URL.
     *  @param url The feed URL to read from
     *  @return the corresponding ROME feed
     */
    protected static SyndFeed readFeed(String url)
    {
        try
        {
            return readFeed(new URL(url));
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
    }


    // ----------------------------------------------------------
    /**
     *  Create a ROME feed by reading from the given URL.
     *  @param url The feed URL to read from
     *  @return the corresponding ROME feed
     */
    protected static SyndFeed readFeed(URL url)
    {
        SyndFeed feed = null;
        try
        {
            SyndFeedInput input = new SyndFeedInput();
//            URLConnection connection = url.openConnection();
//            connection.setRequestProperty("User-Agent", USER_AGENT);
//            feed = input.build(new XmlReader(connection));
            feed = input.build(new java.io.StringReader(
                student.web.internal.WebContent.get(url)));
        }
        catch (IllegalArgumentException e)
        {
            if (!student.testingsupport.SystemIOUtilities.isOnServer())
            {
                System.out.println("RSS feed type unrecognized: "
                    + e.getMessage());
                e.printStackTrace();
            }
        }
//        catch (IOException e)
//        {
//            if (!net.sf.webcat.SystemIOUtilities.isOnServer())
//            {
//                System.out.println("Failed to connect to RSS site: "
//                    + e.getMessage());
//                e.printStackTrace();
//            }
//        }
        catch (Exception e)
        {
            if (!student.testingsupport.SystemIOUtilities.isOnServer())
            {
                System.out.println("Failed readding RSS: " + e.getMessage());
                e.printStackTrace();
            }
        }
        if (feed == null)
        {
            feed = new SyndFeedImpl();
        }
        return feed;
    }
}
