package student.web.internal.tests;

import student.*;

// -------------------------------------------------------------------------
/**
 *  This class tests ScrapTheScreen to ensure proper operation.
 *
 *  @author  dboynton
 *  @version 2009.02.24
 */
public class ScrapeTheScreenTest
    extends TestCase
{
	ScrapeTheScreen testScraper;
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new ScrapeTheScreenTest test object.
     */
    public ScrapeTheScreenTest()
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
        testScraper = new ScrapeTheScreen();
    }


    // ----------------------------------------------------------
    /**
	 * This method makes sure that the task labels are printed.
	 * The task should have the same labels, regardless of what the bot
	 * returns from the web page.
	 */
	public void testTheTask() {
		testScraper.task();
		assertTrue(testScraper.getRobot().out().getHistory().contains(
			"Most Active"));
		assertTrue(testScraper.getRobot().out().getHistory().contains(
			"------------"));
		assertTrue(testScraper.getRobot().out().getHistory().contains(
			"Most Popular"));
	}

	/**
	 * Test the creation of the robot.
	 * When performing a task, it should not be null.
	 */
	public void testGetRobot() {
		testScraper.task();
		assertNotNull(testScraper.getRobot());
	}

}
