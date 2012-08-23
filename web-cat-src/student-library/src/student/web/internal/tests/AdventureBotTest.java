package student.web.internal.tests;

import student.*;
import java.net.*;
import student.TestCase;

// -------------------------------------------------------------------------
/**
 *  We need to test our AdventureBot to make sure it behaves properly.
 *
 *  @author  alanps mchao8
 *  @version 2009.02.06
 *
 *  Author's Notes: Creating all these tests are absolutely uncessessary and
 *  rediculous. What you should be teaching us is how to actually build java
 *  applications - not stupid bluej projects. So far we can apply very little
 *  that you've taught us else where in my life - and its extremely frustrating.
 *
 *  Not to mention that building java apps for the web is a complete waste of
 *  time because the server software, Apache Tomcat, is absoultely reidiculous
 *  to maintain and get working. So why not rather have us focus on building
 *  desktop applications that suit java much more - being an OS independent
 *  langauge. Our two cents.
 *
 */
public class AdventureBotTest
    extends TestCase
{
    //~ Instance/static variables .............................................
    /**
     * This variable initializes our bot for testing.
     */
    private AdventureBot bot;

    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new AdventureBotTest test object.
     */
    public AdventureBotTest()
    {
        // The constructor is usually empty in unit tests, since it runs
        // once for the whole class, not once for each test method.
        // Per-test initialization should be placed in setUp() instead.
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Sets up the test fixture.
     * Called before every test case method.
     */
    public void setUp()
    {
        /*# Insert your own setup code here */
    }


    // ----------------------------------------------------------
    /*# Insert your own test methods here */

   /**
    * Test the AdeventureBot's constructor to see if it points the WebBot to the
    * correct page.
    */
    public void testConstructor()
    {
        bot = new AdventureBot("http://courses.cs.vt.edu/~cs1705/" +
                                         "practice/adventure/index.html");
        assertTrue(bot.getPageTitle().equals("A Creepy Old House"));
    }

    /**
     * This will test hasWon returning true to keep the while loop going.
     */
    public void testMethodhasWonFalse()
    {
        bot = new AdventureBot("http://courses.cs.vt.edu/~cs1705/" +
                                        "practice/adventure/index.html");
        assertEquals(false, bot.hasWon());
    }
    /**
     * This will test hasWon returning false to end the while loop.
     */
    public void testMethodhasWonTrue()
    {
        bot = new AdventureBot("http://courses.cs.vt.edu/~cs1705/practice/" +
                                         "adventure/marshcorrect.html");
        assertEquals(true, bot.hasWon());
    }

    /**
     * This will test getCorrectLink so make sure it gets the first correctlink.
     */

    public void testMethodgetCorrectLinkGetsLink()
    {
        bot = new AdventureBot("http://courses.cs.vt.edu/~cs1705/practice/" +
            "adventure/index.html");
        assertEquals("http://courses.cs.vt.edu/~cs1705/practice/adventure/" +
            "trailcorrect.html", bot.getCorrectLink().toString());
    }
    /**
     * This tests to make sure that goAdventuring runs properly.
     */
	public void testMethodgoAdventuring()
	{
		bot = new AdventureBot("http://courses.cs.vt.edu/~cs1705/practice/" +
		        "adventure/index.html");
		assertEquals(1, bot.goAdventuring());
	}

	/**
	 * This will test the winning page - when getCorrectLink fails because there
	 * are no link.
	 */

	public void testMethodgetCorrectLinkFalse()
	{
		bot = new AdventureBot("http://courses.cs.vt.edu/~cs1705/practice/" +
		        "adventure/marshcorrect.html");
		assertEquals(null, bot.getCorrectLink());
	}
}





