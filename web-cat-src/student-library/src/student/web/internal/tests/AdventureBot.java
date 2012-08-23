package student.web.internal.tests;

import student.web.*;
import java.net.*;
import student.web.WebBot;

// -------------------------------------------------------------------------
/**
 * This Bot goes on Dr. Edward's adventure.
 *
 *  @author  alanps mchao8
 *  @version 2009.02.06
 */
public class AdventureBot
    extends WebBot
{
    //~ Instance/static variables .............................................

    /**
     * This keeps getCorrectLink's while loop searching for the right link.
     */
    public boolean searchForLink;

    /**
     * Allows for conversion of URI and searching for "correct" in URI.
     */
    public String correctLink;

    /**
     * This keeps us adventuring lol.
     */
    public boolean weAdventure;

    /**
     * This jumps us to page to page searching for links.
     */
    public URI nextLink;

    /**
     * This sets the current link we wish to examine.
     */
    public String currentLink;
    /**
     * This gets the initial url - allowing us to initialize goAdventure();.
     */
    public String urlToInitialize;

    //~ Constructor ...........................................................

    //-------------------------------------------------------------------------
    /**
     * Create a new AdventureBot, starting on the given page.
     *
     * @param url The web page to start on
     */
    public AdventureBot(String url)
    {
        //This line just passes the url up to the superclass constructor
        super(url); //alternatively, WebBot(url);
        urlToInitialize = url;
        currentLink = url;
    }

    //~ Methods ...............................................................

    /**
     * This method will test to see if we have won the game.
     *
     * @return Returns a true of false to determine if we've won.
     */
    public boolean hasWon()
    {
        jumpToPage(currentLink);
        advanceToNextHeading();
        return getCurrentElementText().equals("You won!");
    }

    /**
     * This method will search our website for a URI containing "correct".
     *
     * @return Returns the URI of the correct link
     */
    public URI getCorrectLink()
    {
        jumpToPage(currentLink);
        searchForLink = true;
        while (searchForLink)
        {
            advanceToNextLink();
            if (isLookingAtLink()) {
                correctLink = getLinkURI().toString();
                if (correctLink.contains("correct"))
                {
                    searchForLink = false;
                }
            }
            if (isLookingAtEndOfPage()) {
                searchForLink = false;
            }
        }
        if (isLookingAtLink()) {
            out.println(getCurrentElementText());
            return getLinkURI();
        }
        return null;
    }

    /**
     * This method will get us to the winning location! Foiling Dr. Edwards plan
     * of ruining my good day with tricky java code.
     */
    public int goAdventuring()
    {
        jumpToPage(urlToInitialize);
        weAdventure = !hasWon();
        out.println("Here are the instructions to beat this silly game: ");
        while (weAdventure)
        {
            nextLink = getCorrectLink();
            currentLink = nextLink.toString();
            jumpToPage(nextLink);
            weAdventure = !hasWon();
        }
        out.println("Ha Dr.Edwards - I win!");
        return 1;
    }


    public static void main(String[] args)
    {
        AdventureBot bot = new AdventureBot(
            "http://courses.cs.vt.edu/~cs1705/practice/adventure/index.html");
        bot.goAdventuring();
    }
}
