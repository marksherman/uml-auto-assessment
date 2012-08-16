package student.web.internal.tests;

import student.TestCase;
import student.*;

// -------------------------------------------------------------------------
/**
 *
 *  This webbot test to make sure the website is the one it should be.
 *
 *  @author  Catalina Astengo(castengo), CJ Norris (cjn)
 *  @version (2009.01.30)
 */
public class PageOutlinerTest
    extends TestCase
{
    private PageOutliner pageOutl1;
    private student.web.WebBot webBot1;

    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new PageOutlinerTest test object.
     */
    public PageOutlinerTest()
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
        pageOutl1 = new PageOutliner();
        pageOutl1.task();
        webBot1 = pageOutl1.getRobot();
    }


    // ----------------------------------------------------------


    /**
     * tests the page title.
     */
    public void testcompletion()
    {
        assertEquals("Some Cool CS Quotes", webBot1.getPageTitle());
    }
    /**
     * tests the size of the first heading.
     */
    public void testheading()
    {
        assertEquals(2, webBot1.getHeadingLevel());
    }


    /**
     * checks to see if webbot is at end of page.
     */
    public void testpageend()
    {
        assertEquals(false, webBot1.isLookingAtEndOfPage());
    }
    /**
     * tests to see if the bot has visited any previous pages.
     */
    public void testprevious()
    {
        assertEquals(0, webBot1.numberOfPreviousPages());
    }
}





