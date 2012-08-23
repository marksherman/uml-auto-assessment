package student.web.internal.tests;

import student.*;
import java.util.*;

// -------------------------------------------------------------------------
/**
 *  Performs a series of tests to be sure the robot is behaving as expected.
 *
 *  @author  dboynton
 *  @version 2009.02.20
 */
public class SourceForgeScraperTest
    extends TestCase
{
	private SourceForgeScraper testScraper;
	private String sampleHTML;

    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new SourceForgeScraperTest test object.
     */
    public SourceForgeScraperTest()
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
	 *
	 * The sample HTML here is a simplified layout that mimics the style
	 * used on SourceForge.
     */
    public void setUp()
    {
        testScraper = new SourceForgeScraper();
		sampleHTML = "" +
			"<head><title=\"Sample HTML Page\"></title></head>" +
			"<body><div id=\"Test1\">Contents of Division 1</div>" +
			"<div id=\"Test2\">Begin Div2 Contents" +
				"<div id=\"Test3\">Contents of Division 3</div>" +
			"</div>" +
			"<div id=\"most_active\">" +
				"<a href=\"/projects/Number1/\">P1</a>" +
				"<a href=\"/projects/Number2/\">P2</a>" +
				"<a href=\"/projects/Number3/\">P3</a>" +
			"</div>" +
			"<div id=\"most_downloaded\">" +
				"<a href=\"/projects/Downlaod1/\">Download 1</a>" +
				"<a href=\"/falselink/Downlaod1/\">Download 1</a>" +
			"</div>" +
			"</body>";
    }


    // ----------------------------------------------------------
	/**
	 * Make sure that the Div tag locater is pulling out the
	 * correct html.
	 */
    public void testDivisionFinding() {
		String result = "zzzz";

		testScraper.jumpToThisHTML(sampleHTML);
		result = testScraper.getDivContents("Test 222");
		assertEquals(result, null);
		result = testScraper.getDivContents("Test3");
		assertEquals(result, "Contents of Division 3");
    }


	/**
	 * Make sure that the robot starts on the right page.
	 */
	public void testRightPage() {
		assertEquals(testScraper.getPageTitle(),
			"SourceForge.net: Software Map");
		assertEquals(testScraper.getPageURL().toString(),
			"http://sourceforge.net/softwaremap/");
	}


	/**
	 * Make sure that the getApplicationNames method works against test data.
	 * This will count the results from most_active, and verify the text
	 * returned for the first element of most_downloaded.
	 */
	public void testGetApplicationNames() {
		ArrayList<String> activeNames = new ArrayList<String>();
		ArrayList<String> popularNames = new ArrayList<String>();
		int counter = 0;

		testScraper.jumpToThisHTML(sampleHTML);
		activeNames = testScraper.getApplicationNames("most_active");
		popularNames = testScraper.getApplicationNames("most_downloaded");

		for (String appName : activeNames) {
			counter++;
		}

		assertEquals(counter, 3);
		assertEquals(popularNames.get(0), "Download 1");
	}


	/**
	 * This method will test the printing functionality of the bot.
	 */
	public void testPrinting() {
		testScraper.jumpToThisHTML(sampleHTML);
		testScraper.printMostActive();
		testScraper.printMostPopular();

		assertTrue(testScraper.out().getHistory().contains("P1"));
		assertTrue(testScraper.out().getHistory().contains("P2"));
		assertTrue(testScraper.out().getHistory().contains("P3"));
		assertTrue(testScraper.out().getHistory().contains("Download 1"));
	}
}
