package objectdraw;

import junit.extensions.abbot.*;

// -------------------------------------------------------------------------
/**
 *  Test cases for TestableWindowController
 *
 *  @author  Roy Patrick Tan
 *  @version June 5, 2006
 */
public class TestableWindowControllerTest
    extends ComponentTestFixture
{

    private VTControllerTester tester;
    private TestableWindowController window;

    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new TestableWindowControllerTest test object.
     */
    public TestableWindowControllerTest()
    {
        // nothing to do
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Sets up the test fixture.
     * Called before every test case method.
     */
    protected void setUp()
    {
        window = new TestableWindowController();
        tester = new VTControllerTester(window);
    }


    // ----------------------------------------------------------
    /**
     * Tears down the test fixture.
     * Called after every test case method.
     */
    protected void tearDown()
    {
        // nothing to do
    }


    // ----------------------------------------------------------
    /**
     * Test empty canvas.
     */
    public void testEmptyCanvas()
    {
           assertFalse(window.has2DObject(null, null, null, null, null, null));
           assertFalse(window.hasLine(null, null, null, null));
    }


    // ----------------------------------------------------------
    /**
     * Test rectangle.
     */
    public void testRectangle()
    {
        FilledRect rect = new FilledRect(10, 20, 100, 200, window.getCanvas());
        rect.setColor(java.awt.Color.RED);

        //existence statements
        //there is some object
        window.assertHas2DObject(null, null, null, null, null, null);
        //there is a FilledRect
        window.assertHas2DObject(
            FilledRect.class, null, null, null, null, null);
        //there is a FilledRect at (10,20)
        window.assertHas2DObject(
            FilledRect.class, new Location(10,20), null, null, null, null);
        //there is a FilledRect at (10,20) with width 100
        window.assertHas2DObject(
            FilledRect.class, new Location(10,20), 100.0, null, null, null);
        //etc.
        window.assertHas2DObject(
            FilledRect.class, new Location(10,20), 100.0, 200.0, null, null);
        window.assertHas2DObject(
            FilledRect.class, new Location(10,20), 100.0, 200.0,
            java.awt.Color.RED, null);
        window.assertHas2DObject(
            FilledRect.class, new Location(10,20), 100.0, 200.0,
            java.awt.Color.RED, true);

        //nonexistence statements
        //there is no framedrect
        window.assertNo2DObject(FramedRect.class, null, null, null, null, null);
        //there are no invisible objects
        window.assertNo2DObject(null, null, null, null, null, false);
    }


    // ----------------------------------------------------------
    /**
     * Test line.
     */
    public void testLine()
    {
        @SuppressWarnings("unused")
        Line l = new Line(20, 10, 100, 200, window.getCanvas());
        @SuppressWarnings("unused")
        Line m = new Line(20, 10, 100, 10, window.getCanvas());

        //existence statements
        //there is some Line
        window.assertHasLine(null, null, null, null);
        //there is a starting at 20,10
        window.assertHasLine(new Location(20,10), null, null, null);
        assertEquals(
            window.getLines(new Location(20,10), null, null, null).size(), 2);

        //there is no invisible line
        window.assertNoLine(null, null, null, false);
    }


    // ----------------------------------------------------------
    /**
     * Test mouse move.
     */
    public void testMouseMove()
    {
        tester.actionMouseMove( new Location(100,100));
    }
}
