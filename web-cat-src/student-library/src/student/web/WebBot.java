/*==========================================================================*\
 |  $Id: WebBot.java,v 1.5 2010/02/23 17:06:36 stedwar2 Exp $
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

import static student.testingsupport.SystemIOUtilities.isOnServer;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.ccil.cowan.tagsoup.Parser;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import student.IOHelper;
import student.testingsupport.PrintWriterWithHistory;
import student.web.internal.MutableNamespaceContext;

// -------------------------------------------------------------------------
/**
 *  This class represents a robot that knows how to walk through a web
 *  page and identify headings and links.  It will automatically transform
 *  "messy" real-world html into conforming XHTML as it visits pages, so
 *  all tag matching and other support should presume XHTML conventions.
 *
 *  @author  Stephen Edwards
 *  @author Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.5 $, $Date: 2010/02/23 17:06:36 $
 */
public class WebBot
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new WebBot that is not yet viewing any web page.
     */
    public WebBot()
    {
        out = isOnServer()
            ? new PrintWriterWithHistory()
            : new PrintWriterWithHistory(System.out, true);

        // Create a mutable namespace context. This should really be provided
        // by the JDK, but the default implementation does not allow new
        // entries to be added.
        nc = new MutableNamespaceContext();

        // Set the prefix "html" to correspond to the xhtml namespace.
        // This can be called multiple times with different prefixes.
        addXpathNamespace("html", "http://www.w3.org/1999/xhtml");
//            nc.setNamespace("", "http://www.w3.org/1999/xhtml");
        xpath.setNamespaceContext(nc);

        try
        {
//                parser.setFeature(
//                    "http://xml.org/sax/features/namespace-prefixes",true);
            xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        }
        catch (Exception e)
        {
            e.printStackTrace(out);
        }
    }


    // ----------------------------------------------------------
    /**
     * Creates a new WebBot for a given URL.
     * @param url The web page where the robot will start.
     */
    public WebBot(String url)
    {
        this();
        jumpToPage(url);
    }


    //~ Public methods ........................................................

    // ----------------------------------------------------------
    /**
     * Is the robot currently viewing a real web page with readable contents?
     * Normally, this would be true, but may be false if the bot has not been
     * given a web page to start on, or if it has been given a malformed or
     * nonexistent URL address, or even if the server for the targeted page
     * is not available.
     * @return True if the robot is currently viewing a real web page with
     * readable contents
     */
    public boolean isViewingWebPage()
    {
        return pages.size() > 0;
    }


    // ----------------------------------------------------------
    /**
     * Has the robot advanced through all the contents (headings and links)
     * on the current page?  Will also return true if
     * {@link #isViewingWebPage()} returns false.
     * @return True if the robot has advanced over all the headings and links
     * in the current document, or false if there are more headings and/or
     * links to visit.
     */
    public boolean isLookingAtEndOfPage()
    {
        return !isViewingWebPage()
            || pages.peek().pos >= pages.peek().len();
    }


    // ----------------------------------------------------------
    /**
     * Moves the robot back to the start of the current page.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     */
    public void returnToStartOfPage()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        pages.peek().pos = -1;
    }


    // ----------------------------------------------------------
    /**
     * Get the title the current web page.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @return The page's title, or null if the page has no title.
     */
    public String getPageTitle()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        return pages.peek().page.getTitle();
    }


    // ----------------------------------------------------------
    /**
     * Echo the current web page title to the robot's default output channel.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     */
    public void echoPageTitle()
    {
        getOutputChannel().print(getPageTitle());
        getOutputChannel().flush();
    }


    // ----------------------------------------------------------
    /**
     * Get the URL for the current web page.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @return The page's URL, if it exists.
     */
    public URL getPageURL()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        return pages.peek().page.url;
    }


    // ----------------------------------------------------------
    /**
     * Get a printable summary of this robot.
     *
     * @return The page's content
     */
    public String toString()
    {
        String result = getClass().getName();
        if (isViewingWebPage())
        {
            result += "[" + getPageURL();
            String title = getPageTitle();
            if (title != null)
            {
                result += " => " + title;
            }
            result += "]";
        }
        else
        {
            result += "[no page]";
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Get the HTML element of interest that the robot is currently standing
     * on.
     *
     * <b>Requires</b> the bot to be looking at an element on the
     * current web page.
     *
     * @return The heading's title.
     */
    public HtmlElement getCurrentElement()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        assert !isLookingAtEndOfPage()
            : "Already passed all content on this web page";
        PageLocation loc = pages.peek();
        assert loc.hasCurrentElt() : "Not looking at any element";
        return loc.currentElt();
    }


    // ----------------------------------------------------------
    /**
     * Is the robot looking at (or standing on) an HTML heading element on
     * the current page?
     * @return True if the robot is positioned at a heading, or false
     * otherwise.
     */
    public boolean isLookingAtHeading()
    {
        return isViewingWebPage()
            && isHeading(pages.peek().currentElt());
    }


    // ----------------------------------------------------------
    /**
     * Advance the robot forward in the current document until it is looking
     * at (or standing on) the next HTML heading element it can find.  If
     * there are no more headings in the document, it will end up looking
     * at the end of the page.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     */
    public void advanceToNextHeading()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        PageLocation loc = pages.peek();
        if (loc.pos >= loc.len())
        {
            // at end of page
            return;
        }
        loc.pos++;
        while ( loc.hasCurrentElt() && !isHeading(loc.currentElt()))
        {
            loc.pos++;
        }
    }


    // ----------------------------------------------------------
    /**
     * Get an iterator over all headings in the current document.  This
     * method is designed to make it easy to write foreach-style loops
     * over page headings.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @return an iterator of {@link HtmlHeadingElement} objects describing the
     * headings in the page.
     */
    public List<HtmlHeadingElement> getHeadings()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        return pages.peek().page.getHeadings(6);
    }


    // ----------------------------------------------------------
    /**
     * Get an iterator over all headings in the current document with a level
     * less than or equal to the value specified.  This method is designed to
     * make it easy to write foreach-style loops over page headings.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @param level Only include headings at this level or above (i.e.,
     * numerically less than or equal to this number)
     * @return an iterator of {@link HtmlHeadingElement} objects describing the
     * headings in the page with levels less than or equal to the
     * specified level.
     */
    public List<HtmlHeadingElement> getHeadingsToLevel( int level )
    {
        assert isViewingWebPage() : "Not viewing a web page";
        return pages.peek().page.getHeadings(level);
    }


    // ----------------------------------------------------------
    /**
     * Echo the text of the current HTML element (heading, link, etc.) to the
     * robot's default output channel.
     *
     * <b>Requires</b> the bot to be viewing an existing HTML element on the
     * current web page.
     */
    public void echoCurrentElementText()
    {
        getOutputChannel().print(getCurrentElementText());
        getOutputChannel().flush();
    }


    // ----------------------------------------------------------
    /**
     * Get the text of the current HTML element on this web page--i.e., the
     * title of a heading or the text associated with a link.
     *
     * <b>Requires</b> the bot to be looking at an element on the
     * current web page.
     *
     * @return The text contained by this element on the web page.
     */
    public String getCurrentElementText()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        assert !isLookingAtEndOfPage()
        : "Already passed all content on this web page";
        PageLocation loc = pages.peek();
        assert loc.hasCurrentElt() : "Not looking at any element";
        return loc.currentElt().getText();
    }


    // ----------------------------------------------------------
    /**
     * Get the heading level (1-6) of the current heading on this web page.
     *
     * <b>Requires</b> the bot to be looking at a heading element on the
     * current web page.
     *
     * @return The heading's level.
     */
    public int getHeadingLevel()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        assert isLookingAtHeading() : "Not looking at a heading";
        return levelOf(pages.peek().currentElt());
    }


    // ----------------------------------------------------------
    /**
     * Is the robot looking at (or standing on) an HTML anchor containing
     * an href attribute (that is, a link to another web page) on
     * the current page?
     * @return True if the robot is positioned at a link, or false
     * otherwise.
     */
    public boolean isLookingAtLink()
    {
        return isViewingWebPage()
            && isLink(pages.peek().currentElt());
    }


    // ----------------------------------------------------------
    /**
     * Advance the robot forward in the current document until it is looking
     * at (or standing on) the next HTML anchor containing an href attribute
     * that it can find.  If there are no more headings in the document, it
     * will end up looking at the end of the page.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     */
    public void advanceToNextLink()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        PageLocation loc = pages.peek();
        if (loc.pos >= loc.len())
        {
            // at end of page
            return;
        }
        loc.pos++;
        while ( loc.hasCurrentElt() && !isLink(loc.currentElt()))
        {
            loc.pos++;
        }
    }


    // ----------------------------------------------------------
    /**
     * Get the URI of the current link on this web page.
     *
     * <b>Requires</b> the bot to be looking at a link (anchor) element on
     * the current web page.
     *
     * @return The link's destination.
     */
    public URI getLinkURI()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        assert isLookingAtLink() : "Not looking at a link";
        PageLocation loc = pages.peek();
        return resolveURIFromPage(loc.currentElt().getAttributeValue("href"));
    }


    // ----------------------------------------------------------
    /**
     * Check whether the URL of the current link on this web page refers to
     * a different page, or just another location within the current page.
     *
     * <b>Requires</b> the bot to be looking at a link (anchor) element on
     * the current web page.
     *
     * @return True if the link refers to a different page
     */
    public boolean linkGoesToAnotherPage()
    {
        URI thisUri = getLinkURI();
        URI parent = pages.peek().page.uri;
        boolean result = false;
        if (thisUri != null)
        {
            if (parent == null)
            {
                result = true;
            }
            else
            {
                result = ( !thisUri.getHost().equals(parent.getHost())
                           || !thisUri.getPath().equals(parent.getPath()) );
            }
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Check whether the URL of the current link on this web page refers to
     * a page on a separate server, or simply another location on the same
     * server.
     *
     * <b>Requires</b> the bot to be looking at a link (anchor) element on
     * the current web page.
     *
     * @return True if the link refers to a page located on a different server
     */
    public boolean linkGoesToAnotherServer()
    {
        URI thisUri = getLinkURI();
        URI parent = pages.peek().page.uri;
        boolean result = false;
        if (thisUri != null)
        {
            if (parent == null)
            {
                result = true;
            }
            else
            {
                result = !thisUri.getHost().equals(parent.getHost());
            }
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Get an iterator over all links in the current document.  This method
     * is designed to make it easy to write foreach-style loops over links.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @return an iterator of {@link URI} objects describing the
     * links in the page.
     */
    public List<URI> getLinks()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        return pages.peek().page.getLinks(ALL_LINKS);
    }


    // ----------------------------------------------------------
    /**
     * Get an iterator over all links in the current document that refer to
     * other web pages.  This is a subset of those returned by
     * {@link #getLinks()}, with any links to other locations within the same
     * page filtered out.  This method is designed to make it easy to write
     * foreach-style loops over links.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @return an iterator of {@link URI} objects describing the
     * links in the page.
     */
    public List<URI> getLinksToOtherPages()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        return pages.peek().page.getLinks(OTHER_PAGE_LINKS);
    }


    // ----------------------------------------------------------
    /**
     * Get an iterator over all links in the current document that refer to
     * pages on other servers.  This is a subset of those returned by
     * {@link #getLinks()}, with any links to pages on the same server as the
     * current page filtered out.  This method is designed to make it easy
     * to write foreach-style loops over links.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @return an iterator of {@link URI} objects describing the
     * links in the page.
     */
    public List<URI> getLinksOffServer()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        return pages.peek().page.getLinks(OTHER_SITE_LINKS);
    }


    // ----------------------------------------------------------
    /**
     * Causes the bot to temporarily leave the current page and hop over to
     * the page at the end of the current link.  The bot will "remember" where
     * it came from, keeping track of past pages in a stack.  After working
     * with the other page, you can use {@link #returnToPreviousPage()} to
     * come back to the point where you left off.
     *
     * <b>Requires</b> the bot to be looking at a link (anchor) element on
     * the current web page.
     */
    public void jumpToLinkedPage()
    {
        jumpToNormalizedURI(getLinkURI());
    }


    // ----------------------------------------------------------
    /**
     * Causes the bot to leave the current page and return to the page it was
     * previously visiting, at the location where it left off.  The previous
     * page is the one that was most recently "remembered", or alternatively,
     * the one on top of the stack of previous pages that have been visited.
     * Use this method in conjunction with {@link #jumpToLinkedPage()} to
     * explore multiple pages.
     *
     * <b>Requires</b> the bot to have some previous page to return to.
     */
    public void returnToPreviousPage()
    {
        assert hasPreviousPage() : "No previous page available";
        pages.pop();
    }


    // ----------------------------------------------------------
    /**
     * Check to see if this bot previously visited a different page that it
     * can now return to.  Is the stack of previous pages empty or not?
     * @return True if there is at least one previous page on the stack of
     * previous visited pages, or false if there are none.
     */
    public boolean hasPreviousPage()
    {
        return pages.size() > 1;
    }


    // ----------------------------------------------------------
    /**
     * How deep is the stack of previous pages that this robot can return to?
     * Each time the robot jumps to a new page, it remembers its previous
     * page so you can {@link #returnToPreviousPage()}.  These previous pages
     * are remembered on a stack, and this method allows you to determine
     * how deep this stack is--that is, how many times you can repeatedly
     * call returnToPreviousPage() successfully.
     * @return The depth of the previous page stack.  This result is zero if
     * the robot is on a page, but has not yet jumped to any others, or -1
     * if there is no current page at all.
     */
    public int numberOfPreviousPages()
    {
        return pages.size() - 1;
    }


    // ----------------------------------------------------------
    /**
     * Causes the bot to temporarily leave the current page and hop over to
     * the page specified by the URL (as a string).  The bot will "remember"
     * where it came from, keeping track of past pages in a stack.  After
     * working with the other page, you can use {@link #returnToPreviousPage()}
     * to come back to the point where you left off.
     * @param url The new page to jump to
     */
    public void jumpToPage(String url)
    {
        jumpToPage(urlForString(url));
    }


    // ----------------------------------------------------------
    /**
     * Causes the bot to temporarily leave the current page and hop over to
     * the page specified by the URL.  The bot will "remember" where
     * it came from, keeping track of past pages in a stack.  After working
     * with the other page, you can use {@link #returnToPreviousPage()} to
     * come back to the point where you left off.
     * @param url The new page to jump to
     */
    public void jumpToPage(URL url)
    {
        assert url != null : "Specified url cannot be null";
        jumpToNormalizedURL(normalizeURL(url));
    }


    // ----------------------------------------------------------
    /**
     * Causes the bot to temporarily leave the current page and hop over to
     * the page specified by the URL.  The bot will "remember" where
     * it came from, keeping track of past pages in a stack.  After working
     * with the other page, you can use {@link #returnToPreviousPage()} to
     * come back to the point where you left off.
     * @param uri The new page to jump to
     */
    public void jumpToPage(URI uri)
    {
        assert uri != null : "Specified URI cannot be null";
        jumpToNormalizedURI(uri.normalize());
    }


    // ----------------------------------------------------------
    /**
     * Causes the bot to temporarily leave the current page and hop over to
     * a specific HTML string provided as a parameter.  Instead of reading
     * web content from the internet, the text you pass in will be used
     * instead.  The bot will "remember" where it was before, keeping track
     * of past pages in a stack.  After working with the provided HTML
     * content you pass in, you can use {@link #returnToPreviousPage()}
     * to come back to the point where you left off in the previous page.
     * @param html A string containing an HTML document to treat as if it
     * came from the web
     */
    public void jumpToThisHTML(String html)
    {
        Page newPage = new Page(html);
        if ( newPage.success )
        {
            jumpToPage(newPage);
        }
    }



    // ----------------------------------------------------------
    /**
     * Get a fully-resolved URI from a (possibly relative) string URI, such as
     * the value of an anchor's href or an img's src attribute.  If the
     * input parameter is a relative URI, it will be converted into an
     * appropriate absolute URI relative to the current page's web location.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @param uri The URI to convert to absolute form
     * @return The equivalent, fully-resolved URI, or null if
     *         there is none.
     */
    public URI resolveURIFromPage(String uri)
    {
        URI result = null;
        Page page = pages.peek().page;
        if (uri != null)
        {
            if (page.uri == null)
            {
                try
                {
                    result = new URI(uri);
                }
                catch (URISyntaxException e)
                {
                    try
                    {
                        URL resultAsUrl = new URL(uri);
                        result = new URI(
                            resultAsUrl.getProtocol(),
                            resultAsUrl.getUserInfo(),
                            resultAsUrl.getHost(),
                            resultAsUrl.getPort(),
                            resultAsUrl.getPath(),
                            resultAsUrl.getQuery(),
                            resultAsUrl.getRef()
                        );
                    }
                    catch (Exception ee)
                    {
                        ee.printStackTrace(out);
                    }
                }
            }
            else
            {
                try
                {
                    result = page.uri.resolve(uri);
                }
                catch (IllegalArgumentException e)
                {
                    try
                    {
                        URL resultAsUrl = page.url == null
                            ? new URL(page.url, uri)
                            : new URL(uri);
                        result = new URI(
                            resultAsUrl.getProtocol(),
                            resultAsUrl.getUserInfo(),
                            resultAsUrl.getHost(),
                            resultAsUrl.getPort(),
                            resultAsUrl.getPath(),
                            resultAsUrl.getQuery(),
                            resultAsUrl.getRef()
                        );
                    }
                    catch (Exception ee)
                    {
                        ee.printStackTrace(out);
                    }
                }
            }
        }
        if (result != null)
        {
            result = result.normalize();
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Check whether this robot has visited this page before.
     * @param uri The page to check
     * @return True if this robot has previously visited (or is currently on)
     * the given web page
     */
    public boolean hasVisitedPage(URI uri)
    {
        try
        {
            return hasVisitedPage(uri.normalize().toURL());
        }
        catch (MalformedURLException e)
        {
            return false;
        }
    }


    // ----------------------------------------------------------
    /**
     * Check whether this robot has visited this page before.
     * @param url The page to check
     * @return True if this robot has previously visited (or is currently on)
     * the given web page
     */
    public boolean hasVisitedPage(URL url)
    {
        return pageCache.containsKey(normalizeURL(url));
    }


    // ----------------------------------------------------------
    /**
     * Tell this bot where to send its output.  Whenever you tell the bot to
     * echo content or headings, they will go to this destination.  By
     * default, output goes to the standard output channel, but you can
     * change the destination here.
     * @param output The output channel to send messages to
     */
    public void setOutputChannel(PrintWriter output)
    {
        assert output != null : "output parameter cannot be null";
        if (output != trueChannel && output != out)
        {
            trueChannel = output;
            out = new PrintWriterWithHistory(trueChannel, true);
        }
    }



    // ----------------------------------------------------------
    /**
     * Get the output channel where this bot is sending its output.
     * @return The current output channel for this bot
     */
    public PrintWriterWithHistory getOutputChannel()
    {
        return out;
    }


    // ----------------------------------------------------------
    /**
     * Get the output channel where this bot is sending its output.
     * This is just a short convenience synonym for
     * {@link #getOutputChannel()}.
     * @return The current output channel for this bot
     */
    public PrintWriterWithHistory out()
    {
        return getOutputChannel();
    }


    // ----------------------------------------------------------
    /**
     * Check whether this robot's output should be treated as plain text,
     * or as HTML markup.  The default is false (treat as plain text).
     * @return True if the output should be treated as HTML markup
     */
    public boolean outputIsHtml()
    {
        return outputIsHtml;
    }


    // ----------------------------------------------------------
    /**
     * Set whether this robot's output should be treated as plain text,
     * or as HTML markup.
     * @param value True if the output should be treated as HTML markup, false
     * if it should be treated as plain text
     */
    public void setOutputIsHtml(boolean value)
    {
        outputIsHtml = value;
    }


    // ----------------------------------------------------------
    /**
     * Execute this robot's built-in sequence of steps.  The default sequence
     * is to do nothing, but subclasses can override this method to add
     * their own behaviors.  These behaviors will be automatically run
     * if the robot is attached to a {@link RobotViewer}.
     */
    public void run()
    {
        // The default does nothing
    }


    //~ Protected nested classes ..............................................

    // ----------------------------------------------------------
    /**
     * Represents a web page that can be visited by this bot.  This class is
     * not static, since it uses the output channel of the bot.
     */
    protected class Page
    {
        /** This page's URL. */
        public URL             url;

        /** This page's URL as a URI. */
        public URI             uri;

        /** This page's title. */
        private String          title;

        /** This page's entire content as a string. */
        private String         content;

        /** This page's entire content as a string. */
        private SoftReference<String> softContent;

        /** This page's entire content as a DOM tree. */
        private SoftReference<Node> doc;

        /** Was this page read and initialized successfully? */
        public boolean         success = false;


        // ----------------------------------------------------------
        /**
         * Create a new page by reading it from the web.
         * @param url the page's URL
         */
        public Page(URL url)
        {
            this.url = url;

            // Initialize the uri field
            if (url != null)
            {
                try
                {
                    uri = url.toURI();
                    success = true;
                }
                catch (URISyntaxException e)
                {
                    e.printStackTrace(out);
                }
            }
            initialize();
        }


        // ----------------------------------------------------------
        /**
         * Create a new page by reading it from a local file.
         * @param file The file to read from
         */
        public Page(File file)
        {
            try
            {
                FileReader in = new FileReader(file);
                content = readContentFrom(in);
                String name = file.getCanonicalPath();
                String fileSeparator = System.getProperty("file.separator");
                if (fileSeparator != null && fileSeparator != "/")
                {
                    name.replaceAll("\\Q" + fileSeparator + "\\E", "/");
                }
                if (!name.startsWith("/"))
                {
                    name = "/" + name;
                }
                name = name.replaceAll(" ","%20");
                url = new URL("file://" + name);
                success = true;
            }
            catch (IOException e)
            {
                e.printStackTrace(out);
            }
            initialize();
        }


        // ----------------------------------------------------------
        /**
         * Create a new page by reading it from a given HTML string.
         * @param htmlContent The content to use for this page
         */
        public Page(String htmlContent)
        {
            content = htmlContent;
            success = content != null;
            initialize();
        }


        // ----------------------------------------------------------
        /**
         * Get an iterator over the headings in this document.
         * @param level The level of headings to get, where 0 is all headings,
         * and 1-6 are only the headings <= the given number
         * @return an iterator over the requested set of headings
         */
        @SuppressWarnings("unchecked")
        public List<HtmlHeadingElement> getHeadings(int level)
        {
            return (List<HtmlHeadingElement>)(List)xPathFindAll(HTML_HEADING);
        }


        // ----------------------------------------------------------
        /**
         * Get an iterator over the links in this document.
         * @param kind One of the constants ALL_LINKS, OTHER_PAGE_LINKS,
         * or OTHER_SITE_LINKS, indicating which links to include in the
         * iterator.
         * @return an iterator over the requested set of links
         */
        public List<URI> getLinks(int kind)
        {
            List<HtmlElement> anchors = xPathFindAll(HTML_ANCHOR);
            List<URI> result = new ArrayList<URI>();
            for (HtmlElement anchor : anchors)
            {
                URI thisUri =
                    resolveURIFromPage(anchor.getAttributeValue("href"));
                if (thisUri != null)
                {
                    if (kind == ALL_LINKS)
                    {
                        result.add(thisUri);
                    }
                    else
                    {
                        String scheme = uri.getScheme().toLowerCase();
                        if (!uri.isOpaque()
                            || "http".equals(scheme)
                            || "https".equals(scheme)
                            || "file".equals(scheme))
                        {
                            if (kind == OTHER_PAGE_LINKS)
                            {
                                result.add(thisUri);
                            }
                            else if (thisUri.getHost() != null
                                && (!thisUri.getHost().equals(
                                    uri.getHost())))
                            {
                                // kind must be OTHER_SITE_LINKS at this point
                                result.add(thisUri);
                            }
                        }
                    }
                }
            }
            return result;
        }


        // ----------------------------------------------------------
        /**
         * Get this document's title a string.
         * @return The document title
         */
        public String getTitle()
        {
            if (title == null)
            {
                HtmlElement e =
                    xPathFindFirst("/html:html/html:head/html:title");
                if (e != null)
                {
                    title = e.getText();
                }
            }
            return title;
        }


        // ----------------------------------------------------------
        /**
         * Get this document's entire content as a string.
         * @return The document content
         */
        public String getContent()
        {
            if (content != null) return content;
            String result = (softContent == null)
                ? null
                : softContent.get();
            if (result == null)
            {
                result = student.web.internal.WebContent.get(url);
                softContent = new SoftReference<String>(result);
            }
            return result;
        }


        // ----------------------------------------------------------
        /**
         * Get this document's entire content as a DOM tree.
         * @return a DOM node
         */
        public Node getDoc()
        {
            Node result = (doc == null)
                ? null
                : doc.get();
            if (result == null)
            {
                String docContent = getContent();
                if (docContent != null)
                {
                    try
                    {
                        TransformerHandler th = stf.newTransformerHandler();

                        // This dom result will contain the results of the
                        // transformation
                        DOMResult dr = new DOMResult();
                        th.setResult(dr);
                        parser.setContentHandler(th);
                        parser.parse(
                            new InputSource(new StringReader(getContent())));
                        result = dr.getNode();

                        doc = new SoftReference<Node>(result);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace(out);
                        success = false;
                    }
                }
                else
                {
                    success = false;
                }
            }
            return result;
        }


        // ----------------------------------------------------------
        /**
         * @param xpathQuery An XPATH query to run against the DOM Tree
         * @return The first HTML element in the document that matches the
         *         query.
         */
        public HtmlElement xPathFindFirst(String xpathQuery)
        {
            NodeList nl = null;
            try
            {
                nl =  (NodeList)xpath.evaluate(
                    xpathQuery, getDoc(), XPathConstants.NODESET);
            }
            catch (Exception e)
            {
                e.printStackTrace(out);
            }
            return (nl == null || nl.getLength() == 0)
                ? null
                : tagForNode(nl.item(0));
        }


        // ----------------------------------------------------------
        /**
         * @param xpathQuery An XPATH query to run against the DOM Tree
         * @return A list of HTML elements that result from running the
         *         query against the document.
         */
        public List<HtmlElement> xPathFindAll(String xpathQuery)
        {
            NodeList nl = null;
            try
            {
                nl = (NodeList)xpath.evaluate(
                    xpathQuery, getDoc(), XPathConstants.NODESET);
            }
            catch (Exception e)
            {
                e.printStackTrace(out);
            }
            ArrayList<HtmlElement> result = new ArrayList<HtmlElement>();
            if (nl != null)
            {
                for (int i = 0; i < nl.getLength(); i++)
                {
                    result.add(tagForNode(nl.item(i)));
                }
            }
            return result;
        }


        // ----------------------------------------------------------
        /**
         * Get the number of times the {@link #targetPhrase} occurs in
         * this page.
         * @return The number of times the {@link #targetPhrase} occurred
         */
        public int getPatternCount()
        {
            if (   lastPattern == null
                || !lastPattern.equals(targetPhrase)
                || patternCount < 0)
            {
                if (targetPhrase == null
                    || content == null
                    || content.length() == 0)
                {
                    patternCount = 0;
                    patternFrequency = 0.0;
                }
                else
                {
                    Matcher matcher = targetPhrase.matcher(content);
                    patternCount = 0;
                    int chars = 0;
                    while (matcher.find())
                    {
                        patternCount++;
                        chars += matcher.end() - matcher.start();
                    }
                    if (chars == 0)
                    {
                        patternFrequency = 0.0;
                    }
                    else
                    {
                        patternFrequency =
                            (double)chars/(double)content.length();
                    }
                }
                lastPattern = targetPhrase;
            }
            return patternCount;
        }


        // ----------------------------------------------------------
        /**
         * Get the frequency of the {@link #targetPhrase}, which approximates
         * the size of all the occurrences of the target phrase in the document
         * divided by the document's total size.
         * @return The {@link #targetPhrase} frequency
         */
        public double getPatternFrequency()
        {
            // Force it to be calculated first by getting the count
            getPatternCount();
            // Now, just return the cached value
            return patternFrequency;
        }


        // ----------------------------------------------------------
        private HtmlElement tagForNode(Node node)
        {
            String tagName = node.getNodeName();
            if (tagName != null
                && tagName.length() == 2
                && (tagName.charAt(0) == 'h'
                    || tagName.charAt(0) == 'H')
                && tagName.charAt(1) > '0'
                && tagName.charAt(1) < '7')
            {
                return new HtmlHeadingNodeTag(node, xformer);
            }
            else
            {
                return new HtmlNodeTag(node, xformer);
            }

        }


        // ----------------------------------------------------------
        /**
         * Dump this page for diagnostic purposes.
         * @param outstream The output channel to dump on
         */
        private String readContentFrom(Reader in)
        {
            StringWriter writer = new StringWriter(8192);
            char buff[] = new char[8192];
            try
            {
                int len = in.read(buff);
                while (len > -1)
                {
                    writer.write(buff, 0, len);
                    len = in.read(buff);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace(out);
            }
            finally
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace(out);
                }
                try
                {
                    writer.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace(out);
                }
            }
            return writer.toString();
        }


        // ----------------------------------------------------------
        /**
         * Dump this page for diagnostic purposes.
         * @param outstream The output channel to dump on
         */
        public void dump(PrintStream outstream)
        {
            outstream.println("dumping doc: url = " + url);
            outstream.println("    success  = " + success);
            outstream.println("    uri      = " + uri);
            outstream.println("    title    = " + title);
            outstream.println("    begin-content");
            outstream.println(content);
            outstream.println("    end-content");

        }


        // ----------------------------------------------------------
        private void initialize()
        {
            // Force the document to be parsed
            getDoc();
        }


        // ----------------------------------------------------------
        private Pattern lastPattern;
        private int patternCount = -1;
        private double patternFrequency = -1.0;
    }


    // ----------------------------------------------------------
    /**
     * Represents a bot location on a specific web page.
     */
    protected static class PageLocation
    {
        /** The page containing this location. */
        public Page page;

        /** The position within the list of elements of interest. */
        public int pos = -1;
        private int len = -1;
        private String elementXpath = null;
        private SoftReference<List<HtmlElement>> elts;

        // ----------------------------------------------------------
        /**
         * Create a new page location with its own list of elements of
         * interest.
         * @param p The page
         */
        public PageLocation(Page p)
        {
            page = p;
        }


        // ----------------------------------------------------------
        /**
         * Set the xpath expression defining the elements of interest
         * on this page, which will reset the current position and the
         * current lis of elements of interest.
         * @param xpath The new xpath expression
         */
        public void setElementXpath(String xpath)
        {
            if (xpath == null)
            {
                if (elementXpath == null) return;
                pos = -1;
                len = -1;
                elts = null;
                elementXpath = xpath;
            }
            else if (!xpath.equals(elementXpath))
            {
                pos = -1;
                len = -1;
                elts = null;
                elementXpath = xpath;
            }
        }


        // ----------------------------------------------------------
        /**
         * Get the length of the current list of elements of interest.
         * Use this instead of elts().size() where possible.
         * @return The number of elements of interest on this page.
         */
        public int len()
        {
            if (len == -1)
            {
                len = elts().size();
            }
            return len;
        }


        // ----------------------------------------------------------
        /**
         * Get the current list of elements of interest.
         * @return The list of elements of interest
         */
        public List<HtmlElement> elts()
        {
            List<HtmlElement> result = (elts == null)
                ? null
                : elts.get();
            if (result == null)
            {
                String xpathQuery = elementXpath;
                if (xpathQuery == null)
                {
                    xpathQuery = HTML_HEADING_OR_ANCHOR;
                }
                result = page.xPathFindAll(xpathQuery);
                elts = new SoftReference<List<HtmlElement>>(result);
            }
            return result;
        }


        // ----------------------------------------------------------
        /**
         * Determine whether this position refers to a current element.
         * @return True if this position is standing on an element
         */
        public boolean hasCurrentElt()
        {
            return pos >= 0 && pos < len();
        }


        // ----------------------------------------------------------
        /**
         * Get the current element.
         * @return The current element, or null if none
         */
        public HtmlElement currentElt()
        {
            return hasCurrentElt() ? elts().get(pos) : null;
        }
    }


    // ----------------------------------------------------------
    private static class AttributeIterator
        implements Iterator<String>, Iterable<String>
    {
        // ----------------------------------------------------------
        public AttributeIterator(NamedNodeMap map)
        {
            inner = map;
            pos = 0;
        }


        // ----------------------------------------------------------
        public boolean hasNext()
        {
            return pos < inner.getLength();
        }


        // ----------------------------------------------------------
        public String next()
        {
            Attr attr = (Attr)inner.item(pos);
            pos++;
            return attr.getName();
        }


        // ----------------------------------------------------------
        public void remove()
        {
            throw new UnsupportedOperationException();
        }


        // ----------------------------------------------------------
        public Iterator<String> iterator()
        {
            return this;
        }


        //~ Instance/static variables .........................................
        private NamedNodeMap inner;
        private int pos;
    }


    // ----------------------------------------------------------
    private static class HtmlHeadingNodeTag
        extends HtmlNodeTag
        implements HtmlHeadingElement
    {
        // ----------------------------------------------------------
        public HtmlHeadingNodeTag(Node node, Transformer transformer)
        {
            super(node, transformer);
        }


        // ----------------------------------------------------------
        public int getHeadingLevel()
        {
            if (level == 0)
            {
                String name = getType();
                level = (int)(name.charAt(1) - '0');
            }
            return level;
        }


        //~ Instance/static variables .........................................
        private int level = 0;
    }


    // ----------------------------------------------------------
    private static class HtmlNodeTag
        implements HtmlElement
    {
        // ----------------------------------------------------------
        public HtmlNodeTag(Node node, Transformer transformer)
        {
            inner = node;
            xformer = transformer;
        }


        // ----------------------------------------------------------
        public String getType()
        {
            return inner.getNodeName();
        }


        // ----------------------------------------------------------
        public String getText()
        {
            String result = getInnerHTML();
            if (result != null)
            {
                Matcher m = INNER_TAG_TRIMMER.matcher(result);
                result = m.replaceAll("");
            }
            return result;
        }


        // ----------------------------------------------------------
        public String getInnerHTML()
        {
            if (nodeChildrenAsTextIsNull)
            {
                return null;
            }

            String result = nodeChildrenAsText == null
                ? null
                : nodeChildrenAsText.get();
            if (result == null)
            {
                result = toString();
                if (result != null)
                {
                    Matcher m = TAG_TRIMMER.matcher(result);
                    if (m.find())
                    {
                        result = m.group(1);
                        nodeChildrenAsText = new SoftReference<String>(result);
                    }
                    else
                    {
                        result = null;
                        nodeChildrenAsTextIsNull = true;
                    }
                }
            }
            return result;
        }


        // ----------------------------------------------------------
        public boolean hasAttribute(String attributeName)
        {
            return inner.getAttributes().getNamedItem(attributeName) != null;
        }


        // ----------------------------------------------------------
        public String getAttributeValue(String attributeName)
        {
            Attr attr =
                (Attr)inner.getAttributes().getNamedItem(attributeName);
            return attr == null
                ? null
                : attr.getNodeValue();
        }


        // ----------------------------------------------------------
        public Iterable<String> getAttributes()
        {
            return new AttributeIterator(inner.getAttributes());
        }


        // ----------------------------------------------------------
        public String toString()
        {
            String result = nodeAsText == null ? null : nodeAsText.get();
            if (result == null)
            {
                try
                {
                    result = dumpNode(inner);
                    nodeAsText = new SoftReference<String>(result);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            return result;
        }


        // ----------------------------------------------------------
        /**
         * @param node A node to be dumped to a string
         * @param omitDeclaration A boolean whether to omit the XML declaration
         * @return A string representation of the node.
         * @throws Exception If anything goes wrong. Error handling omitted.
         */
        private String dumpNode(Node node)
            throws Exception
        {
            StringWriter sw = new StringWriter();
            Result result = new StreamResult(sw);
            Source source = new DOMSource(node);
            xformer.transform(source, result);
            return sw.toString();
        }


        //~ Instance/static variables .........................................
        private Transformer xformer;
        private Node inner;
        private SoftReference<String> nodeAsText;
        private SoftReference<String> nodeChildrenAsText;
        private boolean nodeChildrenAsTextIsNull;
    }


    //~ Protected methods .....................................................

    // ----------------------------------------------------------
    /**
     * Get the current web page's entire content as a string.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @return The page's content
     */
    protected String getPageContent()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        return pages.peek().page.getContent();
    }


    // ----------------------------------------------------------
    /**
     * Bind a symbolic name to an XML namespace URL so that the symbolic name
     * can be used as a namespace prefix on identifiers in XPATH expressions.
     * This method is for <b>advanced users only</b>.  It is only necessary
     * if your WebBot is manipulating content that is not HTML/XHTML, and you
     * need to write XPATH expressions in some other XML namespace.  The
     * default namespace bindings are for the prefix "html" to be bound to
     * the namespace http://www.w3.org/1999/xhtml.  You can add as many
     * additional namespaces as you need in order to build your own XPATH
     * expressions.
     *
     * @param name The symbolic prefix to use for this namesapce
     * @param url  The URL identifying this XML namespace
     */
    protected void addXpathNamespace(String name, String url)
    {
        nc.setNamespace(name, url);
    }


    // ----------------------------------------------------------
    /**
     * Determine whether a given HTML element is an anchor tag with an HREF
     * attribute.
     * @param element The HTML element to test
     * @return True if it is a link
     */
    protected boolean isLink(HtmlElement element)
    {
        if (element == null) return false;
        String name = element.getType();
        return "a".equals(name) || "A".equals(name);
    }


    // ----------------------------------------------------------
    /**
     * Determine whether a given HTML element is a heading tag.
     * @param element The HTML element to test
     * @return True if it is a heading (any level)
     */
    protected boolean isHeading(HtmlElement element)
    {
        return element != null && element instanceof HtmlHeadingElement;
    }


    // ----------------------------------------------------------
    /**
     * Convert an HTML element representing a heading tag into its
     * corresponding level number.
     * @param element The HTML element to look up
     * @return The heading's level, 1-6, or 0 if this is not a heading
     */
    protected int levelOf(HtmlElement element)
    {
        return isHeading(element)
            ? ((HtmlHeadingElement)element).getHeadingLevel()
            : 0;
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the cached page for the given URL.  This method will
     * create the page and insert it in the cache if it does not yet exist.
     * Assumes the URL has been normalized and is absolute.
     * @param url The URL to look up
     * @return the page object for this URL
     */
    protected Page cachedPageFor(URL url)
    {
        Page result = pageCache.get(url);
        if (result == null)
        {
            result = new Page(url);
            pageCache.put(url, result);
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Performs cleanup once this bot has completed all its tasks.  Users
     * should never need to explicitly call this operation.
     */
    protected void releaseCachedResources()
    {
        pages.clear();
        pageCache.clear();
    }


    // ----------------------------------------------------------
    /**
     * Convert a string to a URL.
     * @param url The string to convert
     * @return the URL, if one exists, or null if a conversion error occurs.
     */
    protected URL urlForString(String url)
    {
        try
        {
            return new URI(url).normalize().toURL();
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace(out);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace(out);
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Normalize a URL.
     * @param url The url to normalize
     * @return the normalized version of the URL
     */
    protected URL normalizeURL(URL url)
    {
        try
        {
            if (url.toString().indexOf(' ') >= 0)
            {
                url = new URL(url.toString().replaceAll(" ","%20"));
            }
            return url.toURI().normalize().toURL();
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace(out);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace(out);
        }
        return null;
    }


    // ----------------------------------------------------------
    /**
     * The worker method for the various flavors of {@link #jumpToPage(URI)}.
     * This method assumes the given URI has been normalized.
     * @param uri The new page to jump to
     */
    protected void jumpToNormalizedURI(URI uri)
    {
        try
        {
            jumpToNormalizedURL(uri.toURL());
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace(out);
        }
    }


    // ----------------------------------------------------------
    /**
     * The worker method for the various flavors of {@link #jumpToPage(URL)}.
     * This method assumes the given URL has been normalized.
     * @param url The new page to jump to
     */
    protected void jumpToNormalizedURL(URL url)
    {
        if (url == null) return;
        Page newPage = cachedPageFor(url);
        if (newPage.success)
        {
            jumpToPage(newPage);
        }
    }


    // ----------------------------------------------------------
    /**
     * The worker method for the various flavors of {@link #jumpToPage(URL)}.
     * This method assumes the given URL has been normalized.
     * @param file The new page to jump to
     */
    protected void jumpToNormalizedURL(File file)
    {
        try
        {
            jumpToNormalizedURL(normalizeURL(makeFileAbsolute(file).toURI().toURL()));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace(out);
        }
    }


    // ----------------------------------------------------------
    /**
     * Adds this page to the history stack, enforcing required stack size
     * limit.
     * @param page The new page to add to the stack
     */
    protected void jumpToPage(Page page)
    {
        pages.push(new PageLocation(page));
    }


    // ----------------------------------------------------------
    /**
     * This is needed to get around issues with relative file names when
     * the current working directory is unknown or when running on a
     * server.
     * @param file The file to turn into an absolute path
     * @return An absolute version of the file, relative to the "logical"
     * current working directory from a student perspective, which may be
     * different than the JVM's true cwd.
     * @see IOHelper#getFile(File)
     */
    protected File makeFileAbsolute(File file)
    {
        File result = file;
        try
        {
            if (!file.isAbsolute())
            {
                result = IOHelper.getFile(file);
            }
        }
        catch (NoClassDefFoundError e)
        {
            if (isOnServer())
            {
                throw new RuntimeException(
                    "You must use cs1705.IOHelper.getFile() to create file "
                    + " objects on the server");
            }
        }
        return result;
    }

    //~ Instance/static variables .............................................

    /** The stack of pages in the current history trail, where the top of
     * the stack is the current page.
     */
    protected Stack<PageLocation> pages = new Stack<PageLocation>();

    /** The current output channel. */
    protected PrintWriter trueChannel;

    /** The current output channel. */
    protected PrintWriterWithHistory out;

    /** The target phrase to search for. */
    protected Pattern targetPhrase;

    /** Internal constant used to specify the set of links to get from a
     * page. */
    protected static final int ALL_LINKS        = 0;

    /** Internal constant used to specify the set of links to get from a
     * page. */
    protected static final int OTHER_PAGE_LINKS = 1;

    /** Internal constant used to specify the set of links to get from a
     * page. */
    protected static final int OTHER_SITE_LINKS = 2;

    /** Internal constant used as search + namespace prefix for xpath nodes.
     *  Its value is "//html:". */
    protected static final String HTML_NODE_PREFIX = "//html:";


    /** A cache of all pages visited so far. */
    private Map<URL, Page> pageCache = new HashMap<URL, Page>();

    /** Should the output stream generated by this bot be treated as HTML?. */
    private boolean outputIsHtml = false;

    private MutableNamespaceContext nc;
    private Parser parser = new Parser();
    private SAXTransformerFactory stf =
        (SAXTransformerFactory)TransformerFactory.newInstance();
    private XPathFactory xpf = XPathFactory.newInstance();
    private XPath xpath = xpf.newXPath();
    private Transformer xformer;
    private static final Pattern TAG_TRIMMER =
        Pattern.compile("^<[^>]*>(.*)</[^>]*>$", Pattern.DOTALL);
    private static final Pattern INNER_TAG_TRIMMER =
        Pattern.compile("<[^>]*>", Pattern.DOTALL);
    private static final String HTML_ANCHOR = HTML_NODE_PREFIX + "a";
    private static final String HTML_HEADING =
        HTML_NODE_PREFIX + "h1|"
        + HTML_NODE_PREFIX + "h2|"
        + HTML_NODE_PREFIX + "h3|"
        + HTML_NODE_PREFIX + "h4|"
        + HTML_NODE_PREFIX + "h5|"
        + HTML_NODE_PREFIX + "h6";
    private static final String HTML_HEADING_OR_ANCHOR =
        HTML_HEADING + "|" + HTML_ANCHOR;
}
