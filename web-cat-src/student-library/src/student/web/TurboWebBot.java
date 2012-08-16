/*==========================================================================*\
 |  $Id: TurboWebBot.java,v 1.3 2010/02/23 17:06:36 stedwar2 Exp $
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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

//-------------------------------------------------------------------------
/**
 *  This advanced WebBot provides additional methods useful for
 *  extracting content from web pages basdon tag type, tag id, CSS class,
 *  or other features.
 *
 *  @author  Stephen Edwards
 *  @author Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.3 $, $Date: 2010/02/23 17:06:36 $
 */
public class TurboWebBot
    extends WebBot
{
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new WebBot that is not yet viewing any web page.
     */
    public TurboWebBot()
    {
        super();
    }


    // ----------------------------------------------------------
    /**
     * Creates a new WebBot for a given URI.
     * @param uri The web page where the robot will start.
     */
    public TurboWebBot(URI uri)
    {
        this();
        jumpToPage(uri);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new WebBot for a given URL.
     * @param url The web page where the robot will start.
     */
    public TurboWebBot(URL url)
    {
        this();
        jumpToPage(url);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new WebBot for a given URL.
     * @param url The web page where the robot will start.
     */
    public TurboWebBot(String url)
    {
        super(url);
    }


    // ----------------------------------------------------------
    /**
     * Creates a new WebBot for a given file.
     * @param file The web page where the robot will start.
     */
    public TurboWebBot(File file)
    {
        this();
        jumpToPage(file);
    }


    //~ Public methods ........................................................

    // ----------------------------------------------------------
    /**
     * A key phrase of interest to look for in documents.  This
     * string will be interpreted as a case-insensitive
     * {@link Pattern regular expression}.
     * @param phrase a regular expression
     */
    public void setPhraseOfInterest(String phrase)
    {
        targetPhrase = Pattern.compile(phrase, Pattern.CASE_INSENSITIVE);
    }


    // ----------------------------------------------------------
    /**
     * Get a count of the number of times the set phrase of interest
     * occurs in the current page.
     *
     * <b>Requires</b> the bot to be viewing a web page, and that the
     * phrase of interest has been set.
     *
     * @return The number of occurrences of the phrase of interest in the
     * current web page
     */
    public int getPagePhraseCount()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        assert targetPhrase != null
            : "You must set the phrase of interest first";
        return pages.peek().page.getPatternCount();
    }


    // ----------------------------------------------------------
    /**
     * Get the frequency of the phrase of interest in the current page.
     * This is a number between 0 and 1 that approximates the fraction of
     * the page that is made up by the target phrase.  It is calculated by
     * taking the size of all the occurrences of the target phrase in the
     * document and dividing by the document's total size.
     *
     * <p>Note that this number tends to be small, since even interesting
     * phrases usually constitute only a small fraction of a page with any
     * interesting amount of information in it.  However, it does provide a
     * relative measure of how many times a phrase has been used, normalized
     * by the size of the document.</p>
     *
     * <b>Requires</b> the bot to be viewing a web page, and that the
     * phrase of interest has been set.
     *
     * @return The frequency of the phrase of interest in the
     * current web page
     */
    public double getPagePhraseFrequency()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        assert targetPhrase != null
            : "You must set the phrase of interest first";
        return pages.peek().page.getPatternFrequency();
    }


    // ----------------------------------------------------------
    /**
     * Advance the robot forward in the current document until it is looking
     * at (or standing on) the next HTML element of interest it can find.
     * Elements of interest can be controlled by calling
     * {@link #resetElementsOfInterest(String...)} (the default is all links
     * and all heading tags).  If there are no elements of interest in the
     * document, it will end up looking at the end of the page.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     */
    public void advanceToNextElement()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        PageLocation loc = pages.peek();
        if (loc.pos < loc.len())
        {
            loc.pos++;
        }
    }


    // ----------------------------------------------------------
    /**
     * Advance the robot forward in the current document until it is looking
     * at (or standing on) the next HTML element of the specified type that it
     * can find.  The specified element type must be one of the elements of
     * interest, as specified by calling
     * {@link #resetElementsOfInterest(String...)} (the default is all links
     * and all heading tags).  If there are no more elements of the desired
     * type in the document, or the desired type is not an element of interest,
     * the robot will end up looking at the end of the page.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @param tagType The type of element to look for (case-sensitive)
     */
    public void advanceToNextElement(String tagType)
    {
        assert isViewingWebPage() : "Not viewing a web page";
        assert tagType != null    : "You must provide a tagType";
        PageLocation loc = pages.peek();

        // move to first element if necessary
        if (loc.pos < 0)
        {
            loc.pos = 0;
        }
        while (loc.hasCurrentElt()
            && !tagType.equals(loc.currentElt().getType()))
        {
            loc.pos++;
        }
    }


    // ----------------------------------------------------------
    /**
     * Determine whether there are any more HTML elements of interest
     * further down the page from the robot's current position.  Elements of
     * interest can be controlled by calling
     * {@link #resetElementsOfInterest(String...)} (the default is all links
     * and all heading tags).
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @return True if there are any more elements of interest in the remainder
     *         of document
     */
    public boolean hasNextElement()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        PageLocation loc = pages.peek();
        return loc.pos < loc.len() - 1;
    }


    // ----------------------------------------------------------
    /**
     * Determine whether there are any more HTML elements of the specified type
     * further down the page from the robot's current position.    The
     * specified element type must be one of the elements of interest, as
     * specified by calling {@link #resetElementsOfInterest(String...)} (the
     * default is all links and all heading tags).
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @param tagType The type of element to look for
     * @return True if there are any more elements of the specified type in the
     *         remainder of document.  False if there are no more elements of
     *         that type, or if the specified tag type is not an element of
     *         interest.
     */
    public boolean hasNextElement(String tagType)
    {
        assert isViewingWebPage() : "Not viewing a web page";
        assert tagType != null    : "You must provide a tagType";
        PageLocation loc = pages.peek();
        int pos = loc.pos + 1;

        while (pos < loc.len()
            && !tagType.equals(loc.elts().get(pos).getType()))
        {
            pos++;
        }
        return pos < loc.len();
    }


    // ----------------------------------------------------------
    /**
     * Is the robot looking at (or standing on) an HTML element of interest on
     * the current page?  Elements of interest can be controlled by calling
     * {@link #resetElementsOfInterest(String...)} (the default is all links
     * and all heading tags).
     *
     * @return True if the robot is positioned at an element of interest,
     *         or false otherwise.
     */
    public boolean isLookingAtElement()
    {
        assert isViewingWebPage() : "Not viewing a web page";
        return pages.peek().hasCurrentElt();
    }


    // ----------------------------------------------------------
    /**
     * Is the robot looking at (or standing on) an HTML element of the
     * specified type on the current page?  The
     * specified element type must be one of the elements of interest, as
     * specified by calling {@link #resetElementsOfInterest(String...)} (the
     * default is all links and all heading tags).
     *
     * @return True if the robot is positioned at an element of the desired
     *         type, or false otherwise.  Also false if the specified tag
     *         type is not an element of interest.
     */
    public boolean isLookingAtElement(String tagType)
    {
        assert isViewingWebPage() : "Not viewing a web page";
        assert tagType != null    : "You must provide a tagType";
        PageLocation loc = pages.peek();
        return loc.hasCurrentElt()
            && tagType.equals(loc.currentElt().getType());
    }


    // ----------------------------------------------------------
    /**
     * Get the first HTML element of the specified type on this web page.
     * This method does not affect the robot's current position (the robot
     * will not move), and it does not depend on the elements of interest.
     * The specified tag type can be any HTML element, and the robot will
     * search for and find the first such element on the page, regardless of
     * where the robot is currently standing.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @param tagType The kind of element to search for.
     * @return The first matching element on the current web page, or null if
     *         none is found.
     * @see #getAllMatchingElements(String)
     */
    public HtmlElement getFirstMatchingElement(String tagType)
    {
        assert isViewingWebPage() : "Not viewing a web page";
        assert tagType != null    : "You must provide a tagType";
        return pages.peek().page.xPathFindFirst(HTML_NODE_PREFIX + tagType);
    }


    // ----------------------------------------------------------
    /**
     * Get the first HTML element of the specified type on this web page, based
     * on the context where the element appears.  For example, if you want the
     * first anchor in the first row of the first table on a page, you could
     * use this call:
     *
     * <pre>
     * HtmlElement result = myBot.getFirstMatchingElement("table", "tr", "a");
     * </pre>
     *
     * This method supports a variable number of arguments.  It will find the
     * first occurrence of the first element type listed.  Then,
     * <em>inside</em> that element, it will look for the first occurrence of
     * the second element type, and then search <em>inside</em> that one for
     * the first occurrence of the third element type, and so on.  It returns
     * the most deeply nested element in this series that it finds.
     *
     * This method does not affect the robot's current position (the robot
     * will not move), and it does not depend on the elements of interest.  The
     * specified tag type(s) can be any HTML element, and the robot will search
     * for and find the first matching element on the page, regardless of where
     * the robot is currently standing.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @param parentTag The first element to search for.
     * @param childTag Additional elements to find--each one will be searched
     *        for <em>within</em> the contents of the element immediately
     *        preceding it in the argument list.
     * @return The first matching element on the current web page, or null if
     *         none is found.
     * @see #getAllMatchingElements(String, String...)
     */
    public HtmlElement getFirstMatchingElement(
        String parentTag, String ... childTag)
    {
        assert isViewingWebPage() : "Not viewing a web page";
        assert parentTag != null  : "You must provide a parentTag";
        StringBuffer sb = new StringBuffer(10 + 10 * childTag.length);
        sb.append(HTML_NODE_PREFIX);
        sb.append(parentTag);
        for (String tag : childTag)
        {
            if (tag != null)
            {
                sb.append(HTML_NODE_PREFIX);
                sb.append(tag);
            }
        }
        return pages.peek().page.xPathFindFirst(sb.toString());
    }


    // ----------------------------------------------------------
    /**
     * Get all HTML elements of the specified type on this web page.
     * This method is just like {@link #getFirstMatchingElement(String)},
     * except that it returns all matches instead of just the first one.
     * This method does not affect the robot's current position (the robot
     * will not move), and it does not depend on the elements of interest.
     * The specified tag type can be any HTML element, and the robot will
     * search for and find all such elements on the page, regardless of where
     * the robot is currently standing.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @param tagType The kind of element to search for.
     * @return A list of all the matching elements.  The list will be empty if
     *         none are found.
     * @see #getFirstMatchingElement(String)
     */
    public List<HtmlElement> getAllMatchingElements(String tagType)
    {
        assert isViewingWebPage() : "Not viewing a web page";
        assert tagType != null    : "You must provide a tagType";
        return pages.peek().page.xPathFindAll(HTML_NODE_PREFIX + tagType);
    }


    // ----------------------------------------------------------
    /**
     * Get all HTML elements of the specified type on this web page, based
     * on the context where the elements appear. This method is just like
     * {@link #getFirstMatchingElement(String, String...)},
     * except that it returns all matches instead of just the first one.
     * This method does not affect the robot's current position (the robot
     * will not move), and it does not depend on the elements of interest.
     * The specified tag types can be any HTML element, and the robot will
     * search for and find all such elements on the page, regardless of where
     * the robot is currently standing.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @param parentTag The first element to search for.
     * @param childTag Additional elements to find--each one will be searched
     *        for <em>within</em> the contents of the element immediately
     *        preceding it in the argument list.
     * @return A list of all the matching elements.  The list will be empty if
     *         none are found.
     * @see #getFirstMatchingElement(String, String...)
     */
    public List<HtmlElement> getAllMatchingElements(
        String parentTag, String ... childTag)
    {
        assert isViewingWebPage() : "Not viewing a web page";
        assert parentTag != null  : "You must provide a parentTag";
        StringBuffer sb = new StringBuffer(10 + 10 * childTag.length);
        sb.append(HTML_NODE_PREFIX);
        sb.append(parentTag);
        for (String tag : childTag)
        {
            if (tag != null)
            {
                sb.append(HTML_NODE_PREFIX);
                sb.append(tag);
            }
        }
        return pages.peek().page.xPathFindAll(sb.toString());
    }


    // ----------------------------------------------------------
    /**
     * Get the first HTML element with the specified id on this web page,
     * using the HTML id="..." attribute on the element.
     * This method does not affect the robot's current position (the robot
     * will not move), and it does not depend on the elements of interest.
     * The robot will search for and find the first element with the given
     * id on the page, regardless of where the robot is currently standing.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @param id The id to search for.
     * @return The first (and usually only) element on the current web page
     *         with the given id, or null if none is found.
     */
    public HtmlElement getElementById(String id)
    {
        assert isViewingWebPage() : "Not viewing a web page";
        assert id != null         : "You must provide an id";
        return pages.peek().page.xPathFindFirst("//*[@id='" + id + "']");
    }


    // ----------------------------------------------------------
    /**
     * Get all the HTML elements with the specified CSS class on this web
     * page, using the HTML class="..." attribute on the elements.
     * This method does not affect the robot's current position (the robot
     * will not move), and it does not depend on the elements of interest.
     * The robot will search for and find all the elements with the given
     * CSS class on the page, regardless of where the robot is currently
     * standing.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @param cssClass The CSS class to search for.
     * @return A list of all elements on the current web page with
     *         the given CSS class.  The list will be empty if none are found.
     */
    public List<HtmlElement> getElementsByCssClass(String cssClass)
    {
        assert isViewingWebPage() : "Not viewing a web page";
        assert cssClass != null   : "You must provide a cssClass";
        return pages.peek().page.xPathFindAll("//*[@class='" + cssClass + "']");
    }


    // ----------------------------------------------------------
    /**
     * Move the WebBot back to the beginning of the page and reset the
     * set of elements that it can walk over to the given set of elements.  By
     * default, a WebBot is interested in links and headings (a,
     * h1, h2, h3, h4, h5, h6), but you can change the set of headings it
     * will step through to any group of HTML elements you like.  This method
     * supports a variable number of arguments, so you can provide as many
     * different element types as you like--if you provide no arguments, it
     * will reset back to the default of all links and headings.
     * <p>
     * For example, to ignore all elements (including links and headings)
     * except for image elements, use:</p>
     * <pre>
     * myBot.resetElementsOfInterest("img");
     * </pre>
     * <p>
     * If you want to look at links and at table cells:</p>
     * <pre>
     * myBot.resetElementsOfInterest("a", "td");
     * </pre>
     * <p>
     * <b>Requires</b> the bot to be viewing a web page.
     * </p>
     *
     * @param tagTypes a list of zero or more element types to look for.  If
     *        none are specified, the default of ("a", "h1", "h2", "h3", "h4",
     *        "h5", "h6") will be used instead
     */
    public void resetElementsOfInterest(String ... tagTypes)
    {
        assert isViewingWebPage() : "Not viewing a web page";
        if (tagTypes == null || tagTypes.length == 0)
        {
            pages.peek().setElementXpath(null);
        }
        else
        {
            StringBuffer sb = new StringBuffer(tagTypes.length * 10);
            for (String tag : tagTypes)
            {
                if (sb.length() > 0)
                {
                    sb.append('|');
                }
                sb.append(HTML_NODE_PREFIX);
                sb.append(tag);
            }
            pages.peek().setElementXpath(sb.toString());
        }
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
    public void addXpathNamespace(String name, String url)
    {
        super.addXpathNamespace(name, url);
    }


    // ----------------------------------------------------------
    /**
     * Find nodes within the current document using an XPATH expression.
     * This method is for <b>advanced users only</b>, and requires that you
     * understand XPATH.
     * This method does not affect the robot's current position (the robot
     * will not move), and it does not depend on the elements of interest.
     * The robot will search for and find all nodes on the page that match the
     * given XPATH expression, regardless of where the robot is currently
     * standing.
     *
     * Your XPATH expression must use namespaces for all element names.  The
     * default namespace bindings are for the prefix "html" to be bound to
     * the namespace http://www.w3.org/1999/xhtml.  You can add additional
     * namespace bindings yourself using
     * {@link #addXpathNamespace(String, String)} if you need more.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @param xpathExpression The XPATH expression to search for
     * @return A list of all matching nodes.  The list will be empty if
     *         no matches were found.
     */
    public List<HtmlElement> getAllElementsMatchingXpath(
        String xpathExpression)
    {
        assert isViewingWebPage() : "Not viewing a web page";
        assert xpathExpression != null : "You must provide an xpathExpression";
        return pages.peek().page.xPathFindAll(xpathExpression);
    }


    // ----------------------------------------------------------
    /**
     * Get the current web page's entire content as a string.
     *
     * <b>Requires</b> the bot to be viewing a web page.
     *
     * @return The page's content
     */
    public String getPageContent()
    {
        return super.getPageContent();
    }


    // ----------------------------------------------------------
    /**
     * Causes the bot to temporarily leave the current page and hop over to
     * the specified file.  The bot will "remember" where it came from,
     * keeping track of past pages in a stack.  After working with the other
     * page, you can use {@link #returnToPreviousPage()} to come back to the
     * point where you left off.
     * @param file The new page to jump to
     */
    public void jumpToPage(File file)
    {
        assert file != null : "Specified file cannot be null";
        assert file.exists() : "Specified file must exist in file system";
        jumpToNormalizedURL(file);
    }


    // ----------------------------------------------------------
    /**
     * Check whether this robot has visited this page before.
     * @param file The page to check
     * @return True if this robot has previously visited (or is currently on)
     * the given web page
     */
    public boolean hasVisitedPage(File file)
    {
        try
        {
            return hasVisitedPage(makeFileAbsolute(file).toURI().toURL());
        }
        catch (MalformedURLException e)
        {
            return false;
        }
    }
}
