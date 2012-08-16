package objectdraw;


import java.awt.Font;
import java.awt.Image;
import junit.framework.Assert;


// -------------------------------------------------------------------------
/**
 *
 * This is an extension to the TestableWindowController that includes
 * "wrapper"-like assertions that keep Students from having to make use of
 * the more complicated but more generic assertion statements in
 * {@link TestableWindowController}.
 * <p>
 * For most of your programming assignments, you will be extending an
 * instance of the StudentTestableWindowController class.
 * For Example:</p>
 * <pre>
 * public class Cube
 *     extends StudentTestableWindowController
 * {
 *      ...
 * }
 * </pre>
 * <p>
 * Therefore, all instantiations of the class that you create can make use
 * of the assert statements presented below.  Unlike the typical JUnit
 * assert statements, these must called as instance methods on an object:</p>
 * <pre>
 * Cube x = new Cube();
 *
 * x.assertCanvasEmpty();
 * </pre>
 * <p>
 * The {@link TestableWindowController} class that
 * StudentTestableWindowController class inherits from contains additional
 * assert statements that can be referenced the same way. These methods
 * are far more generic than the more specific functions in
 * StudentTestableWindowController.  The StudentTestableWindowController
 * will be useful for a lot of your purposes, but a more generic
 * function may be useful, from time to time.</p>
 *
 *  @author  Matthew Thornton
 *  @version .2 July 31, 2006
 */
public class StudentTestableWindowController
    extends TestableWindowController
{
    //~ Methods ...............................................................

    //Existential assertions

    // ----------------------------------------------------------
    /**
     * Asserts there are no visible shape objects on the canvas.
     */
    public void assertCanvasEmpty()
    {
        DrawableIterator i = canvas.getDrawableIterator();

        while(i.hasNext())
        {
            DrawableInterface d = i.next();
            if( ! d.isHidden()) {
                Assert.fail("The canvas should not have visible elements.");
            }
        }

    }


    // ----------------------------------------------------------
    /*
     * Asserts there are no shape objects on the canvas.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint error message that should be displayed in the event of
     * a failed assertion.
    public void assertCanvasEmpty(String hint)
    {
        DrawableIterator i = canvas.getDrawableIterator();
        if(i.hasNext())
        {
            Assert.fail(hint);
        }
    }
    */


    // ----------------------------------------------------------
    /**
     * Asserts there is at least one visible element on the canvas.
     */
    public void assertNotCanvasEmpty()
    {
        DrawableIterator i = canvas.getDrawableIterator();
        while(i.hasNext())
        {
            DrawableInterface d = i.next();
            if(!d.isHidden()) {
                return;
            }
        }
        Assert.fail("Canvas should have visible elements.");
    }


    // ----------------------------------------------------------
    /*
     * assertNotCanvasEmpty asserts that there is at least one element on the canvas.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint error message that should be displayed in the event of a failed assertion.

    public void assertNotCanvasEmpty(String hint)
    {
        DrawableIterator i = canvas.getDrawableIterator();
        if(!i.hasNext())
        {
            Assert.fail(hint);
        }
    }
    */


    // ----------------------------------------------------------
    /**
     * assertExistFramedRect asserts that there exists a framed rectangle at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertExistFramedRect(double x, double y)
    {
        assertHas2DObject("Canvas should have a framed rectangle at (" + x + "," + y + ").", FramedRect.class, new Location(x, y), null, null, null, null);
    }


    // ----------------------------------------------------------
    /*
     * assertExistFramedRect asserts that there exists a framed rectangle at the specified (x, y) position.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x     the x coordinate
     * @param y     the y coordinate
    public void assertExistFramedRect(String hint, double x, double y)
    {
        assertHas2DObject(hint, FramedRect.class, new Location(x, y), null, null, null, null);
    }
    */


    // ----------------------------------------------------------
    /**
     * assertExistFilledRect asserts that there exists a filled rectangle at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertExistFilledRect(double x, double y)
    {
        assertHas2DObject("Canvas should have a filled rectangle at (" + x + "," + y + ").",FilledRect.class, new Location(x, y), null, null, null, null);
    }


    // ----------------------------------------------------------
    /*
     * assertExistFilledRect asserts that there exists a filled rectangle at the specified (x, y) position.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x     the x coordinate
     * @param y     the y coordinate
    public void assertExistFilledRect(String hint, double x, double y)
    {
        assertHas2DObject(hint, FilledRect.class, new Location(x, y), null, null, null, null);
    }
    */


    // ----------------------------------------------------------
    /**
     * assertExistAngLine asserts that there exists an angle line at the given starting location.
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertExistAngLine(double x, double y)
    {
        assertHasLine("Canvas should have a angLine at (" + x + "," + y + ").", new Location(x, y), null, null, null);
    }


    // ----------------------------------------------------------
    /*
     * assertExistAngLine asserts that there exists an angle line at the given starting location.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x     the x coordinate
     * @param y     the y coordinate
    public void assertExistAngLine(String hint, double x, double y)
    {
        assertHasLine(hint, new Location(x, y), null, null, null);
    }
    */


    // ----------------------------------------------------------
    /**
     * assertExistFilledArc asserts that there exists a filled arc at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertExistFilledArc(double x, double y)
    {
        assertHas2DObject("Canvas should have a filled arc at (" + x + "," + y + ").", FilledArc.class, new Location(x, y), null, null, null, null);
    }


    // ----------------------------------------------------------
    /*
     * assertExistFilledArc asserts that there exists a filled arc at the specified (x, y) position.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x     the x coordinate
     * @param y     the y coordinate
    public void assertExistFilledArc(String hint, double x, double y)
    {
        assertHas2DObject(hint, FilledArc.class, new Location(x, y), null, null, null, null);
    }
    */


    // ----------------------------------------------------------
    /**
     * assertExistFilledOval asserts that there exists a filled oval at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertExistFilledOval(double x, double y)
    {
        assertHas2DObject("Canvas should have a filled oval at (" + x + "," + y + ").", FilledOval.class, new Location(x, y), null, null, null, null);
    }

    /*
     * assertExistFilledOval asserts that there exists a filled oval at the specified (x, y) position.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x     the x coordinate
     * @param y     the y coordinate
    public void assertExistFilledOval(String hint, double x, double y)
    {
        assertHas2DObject(hint, FilledOval.class, new Location(x, y), null, null, null, null);
    }
    */


    // ----------------------------------------------------------
    /**
     * assertExistFramedArc asserts that there exists a framed arc at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertExistFramedArc(double x, double y)
    {
        assertHas2DObject("Canvas should have a framed arc at (" + x + "," + y + ").", FramedArc.class, new Location(x, y), null, null, null, null);
    }

    /*
     * assertExistFramedArc asserts that there exists a framed arc at the specified (x, y) position.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x     the x coordinate
     * @param y     the y coordinate

    public void assertExistFramedArc(String hint, double x, double y)
    {
        assertHas2DObject(hint, FramedArc.class, new Location(x, y), null, null, null, null);
    }
    */


    // ----------------------------------------------------------
    /**
     * assertExistFramedOval asserts that there exists a framed oval at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertExistFramedOval(double x, double y)
    {
        assertHas2DObject("Canvas should have a framed oval at (" + x + "," + y + ").",FramedOval.class, new Location(x, y), null, null, null, null);
    }


    // ----------------------------------------------------------
    /*
     * assertExistFramedOval asserts that there exists a framed oval at the specified (x, y) position.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x     the x coordinate
     * @param y     the y coordinate

    public void assertExistFramedOval(String hint, double x, double y)
    {
        assertHas2DObject(hint, FramedOval.class, new Location(x, y), null, null, null, null);
    }
    */


    // ----------------------------------------------------------
    /**
     * assertExistFramedRect asserts that there exists a framed, rounded rectangle at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertExistFramedRoundedRect(double x, double y)
    {
        assertHas2DObject("Canvas should have a framed rounded rectangle at (" + x + "," + y + ").", FramedRoundedRect.class, new Location(x, y), null, null, null, null);
    }

    /*
     * assertExistFramedRect asserts that there exists a framed, rounded rectangle at the specified (x, y) position.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x     the x coordinate
     * @param y     the y coordinate
    public void assertExistFramedRoundedRect(String hint, double x, double y)
    {
        assertHas2DObject(hint, FramedRoundedRect.class, new Location(x, y), null, null, null, null);
    }
    */

    /**
     * assertExistFilledRoundedRect asserts that there exists a filled, rounded rectangle at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertExistFilledRoundedRect(double x, double y)
    {
        assertHas2DObject("Canvas should have a filled rounded rectangle at (" + x + "," + y + ").", FilledRoundedRect.class, new Location(x, y), null, null, null, null);
    }

    /*
     * assertExistFilledRect asserts that there exists a filled, rounded rectangle at the specified (x, y) position.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x     the x coordinate
     * @param y     the y coordinate

    public void assertExistFilledRoundedRect(String hint, double x, double y)
    {
        assertHas2DObject(hint, FilledRoundedRect.class, new Location(x, y), null, null, null, null);
    }
    */

    /**
     * assertExistLine asserts that there exists a line starting at the specified (x, y) position
     *
     * @param startx     the x coordinate where the line begins
     * @param starty     the y coordinate where the line begins
     */
    public void assertExistLine(double startx, double starty)
    {
        assertHasLine("Canvas should have a line starting at (" +startx + "," + starty + ").", new Location(startx, starty), null, null, null);
    }

    /*
     * assertExistLine asserts that there exists a line starting at the specified (x, y) position.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param startx     the x coordinate where the line begins
     * @param starty     the y coordinate where the line begins

    public void assertExistLine(String hint, double startx, double starty)
    {
        assertHasLine(hint, new Location(startx, starty), null, null, null);
    }
    */

    /**
     * assertExistLine asserts that there exists a line connection the start and end points.
     *
     * @param startx        the x coordinate where the line begins.
     * @param starty        the y coordinate where the line begins.
     * @param endx      the x coordinate where the line ends.
     * @param endy      the y coordinate where the line ends.
     */
    public void assertExistLine(double startx, double starty, double endx, double endy)
    {
            assertHasLine("Canvas should have a line starting at ("+startx+","+starty+") and ending at ("+endx+","+endy+").", new Location(startx, starty), new Location(endx, endy), null, null);
    }

    /*
     * assertExistLine asserts that there exists a line connection the start and end points.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param startx        the x coordinate where the line begins.
     * @param starty        the y coordinate where the line begins.
     * @param endx      the x coordinate where the line ends.
     * @param endy      the y coordinate where the line ends.
    public void assertExistLine(String hint, double startx, double starty, double endx, double endy)
    {
            assertHasLine(hint, new Location(startx, starty), new Location(endx, endy), null, null);
    }
    */

    /**
     * assertExistText asserts that there exists a textbox at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertExistText(double x, double y)
    {
        assertHas2DObject("Canvas should have a Text object at (" + x + "," + y + ").", Text.class, new Location(x, y), null, null, null, null);
    }

    /*
     * assertExistText asserts that there exists a textbox at the specified (x, y) position.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x     the x coordinate
     * @param y     the y coordinate

    public void assertExistText(String hint, double x, double y)
    {
        assertHas2DObject(hint, Text.class, new Location(x, y), null, null, null, null);
    }
    */

    /**
     * assertExistVisibleImage asserts that there exists a VisibleImage at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertExistVisibleImage(double x, double y)
    {
        assertHas2DObject("Canvas should have a VisibleImage at (" + x + "," + y + ").", VisibleImage.class, new Location(x, y), null, null, null, null);
    }

    /*
     * assertExistLine asserts that there exists a VisibleImage at the specified (x, y) position.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x     the x coordinate
     * @param y     the y coordinate
     *
    public void assertExistVisibleImage(String hint, double x, double y)
    {
        assertHas2DObject(hint, VisibleImage.class, new Location(x, y), null, null, null, null);
    }
    */



    /**
     * assertColor asserts that the shape being evaluated has the desired color.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param color     the color that is desired.
     */
    public void assertColor(double x, double y, java.awt.Color color)
    {

        if(get2DObject(null, new Location(x, y), null, null, null, null)!=null)
        {
            if(!(get2DObject(null, new Location(x, y), null, null, null, null).getColor()).equals(color))
            {
                Assert.fail("The 2D shape at (" + x + "," + y + ") has the wrong color.  Expected "+color+ "but was "
                            +get2DObject(null, new Location(x, y), null, null, null, null).getColor());
            }
        }
        else
        {
            Assert.fail("There is no 2D shape at ("+x+","+y+")." );
        }
    }

    /*
     * assertColor asserts that the shape being evaluated has the desired color.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param color     the color that is desired.
     *
    public void assertColor(String hint, double x, double y, java.awt.Color color)
    {
       if(!(get2DObject(null, new Location(x, y), null, null, null, null).getColor()).equals(color))
       {
           Assert.fail(hint);
       }
    }
    */

    /**
     * assertVisible asserts that the shape being evaluated is visible
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     */
    public void assertVisible(double x, double y)
    {
        if(get2DObject(null, new Location(x, y), null, null, null, null)!=null)
        {
            if(get2DObject(null, new Location(x, y), null, null, null, null).isHidden())
            {
                Assert.fail("The 2D shape at (" + x + "," + y + ") is not visible.");
            }
        }
        else
        {
            Assert.fail("There is no 2D shape at ("+x+","+y+").");
        }
    }

    /*
     * assertVisible asserts that the shape being evaluated is visible
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param shape     the shape being evaluated
    public void assertVisible(String hint, double x, double y)
    {
       if(get2DObject(null, new Location(x, y), null, null, null, null).isHidden())
       {
           Assert.fail(hint);
       }
    }
    */

    /**
     * assertInvisible asserts that the shape being evaluated is invisible to the canvas.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     */
    public void assertInvisible(double x, double y)
    {
        if(get2DObject(null, new Location(x, y), null, null, null, null)!=null)
        {
            if(!(get2DObject(null, new Location(x, y), null, null, null, null).isHidden()))
            {
                Assert.fail("The 2D shape at (" + x + "," + y + ") is not invisible.");
            }
        }
        else
        {
            Assert.fail("There is no 2D shape at ("+x+","+y+").");
        }
    }

    /*
     * assertInvisible asserts that the shape being evaluated is invisible to the canvas.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     *
    public void assertInvisible(String hint, double x, double y)
    {
       if(!(get2DObject(null, new Location(x, y), null, null, null, null).isHidden()))
       {
           Assert.fail(hint);
       }
    }
    */

    /**
     * assertWidth asserts that the shape being evaluated is a desired width.
     *
     * @param x      the x coordinats of the shape being evaluated.
     * @param y      the y coordinate of the shape being evaluated.
     * @param width     the desired width of shape
     */
    public void assertWidth(double x, double y, double width)
    {
        if(get2DObject(null, new Location(x, y), null, null, null, null)!=null)
        {
            if(get2DObject(null, new Location(x, y), null, null, null, null).getWidth()!=width)
            {
                Assert.fail("The 2D shape at (" + x + "," + y + ") has the wrong width.  Expected "+width+ "but was "
                            +get2DObject(null, new Location(x, y), null, null, null, null).getWidth());
            }
        }
        else
        {
            Assert.fail("There is no 2D shape at ("+x+","+y+").");
        }
    }

    /*
     * assertWidth asserts that the shape being evaluated is a desired width.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param height        the desired width of shape.
     *
    public void assertWidth(String hint, double x, double y, double width)
    {
       if(!(get2DObject(null, new Location(x, y), null, null, null, null).getWidth()==width))
       {
           Assert.fail(hint);
       }
    }
    */

    /**
     * assertHeight asserts that the shape being evaluated is a desired height.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param height        the desired height of shape.
     */
    public void assertHeight(double x, double y, double height)
    {
        if(get2DObject(null, new Location(x, y), null, null, null, null)!=null)
        {
            if(get2DObject(null, new Location(x, y), null, null, null, null).getHeight()!=height)
            {
                Assert.fail("The 2D shape at (" + x + "," + y + ") has the wrong height.  Expected "+height+ "but was "
                            +get2DObject(null, new Location(x, y), null, null, null, null).getHeight());
            }
        }
        else
        {
            Assert.fail("There is no 2D shape at ("+x+","+y+").");
        }
    }

    /*
     * assertHeight asserts that the shape being evaluated is a desired height.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param height        the desired height of shape
    public void assertHeight(String hint, double x, double y, double height)
    {
       if(!(get2DObject(null, new Location(x, y), null, null, null, null).getHeight()==height))
       {
           Assert.fail(hint);
       }
    }
    */

    /**
     * assertLocationInShape asserts that the shape being evaluated includes the specified (x,y) coordinate.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param xLoc     the x coordinate of the location to be tested
     * @param yLoc     the y coordinate of the location to be tested
     */
    public void assertLocationInShape(double x, double y, double xLoc, double yLoc)
    {
        if(get2DObject(null, new Location(x, y), null, null, null, null)!=null)
        {
            if(!(get2DObject(null, new Location(x, y), null, null, null, null).contains(new Location(xLoc, yLoc))))
            {
                Assert.fail("The 2D shape at (" + x + "," + y + ") does not contain the point ("+xLoc+","+yLoc+").");
            }
        }
        else
        {
            Assert.fail("There is no 2D shape at ("+x+","+y+").");
        }
    }

    /*
     * assertLocationInShape asserts that the shape being evaluated includes the specified (x, y) coordinate
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param xLoc     the x coordinate of the location to be tested.
     * @param yLoc     the y coordinate of the location to be tested.
     *
    public void assertLocationInShape(String hint, double x, double y, double xLoc, double yLoc)
    {
       if(!(get2DObject(null, new Location(x, y), null, null, null, null).contains(new Location(x,y))))
       {
           Assert.fail(hint);
       }
    }
    */

    /**
     * assertCorrectStartAngle asserts that the frame arc being evaluated has the correct starting angle.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param angle     angle that is to be evaluated.
     */
    public void assertCorrectFramedArcStartAngle(double x, double y, double angle)
    {
        FramedArc shape = (FramedArc)get2DObject(null, new Location(x, y), null, null, null, null);
        if(shape!=null)
        {
            if(shape.getStartAngle()!=angle)
            {
                Assert.fail("The FramedArc at (" + x + "," + y + ") has the wrong start angle.  Expected "+angle+ "but was "
                            +shape.getStartAngle());
            }
        }
        else
        {
            Assert.fail("There is no FramedArc at ("+x+","+y+").");
        }

    }

    /*
     * assertCorrectStartAngle asserts that the shape being evaluated has the correct starting angle
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param angle     angle that is to be evaluated.

    public void assertCorrectFramedArcStartAngle(String hint, double x, double y, double angle)
    {
       if(!(((FramedArc)get2DObject(null, new Location(x, y), null, null, null, null)).getStartAngle()==angle))
       {
           Assert.fail(hint);
       }
    }
    */

    /**
     * assertCorrectStartAngle asserts that the shape being evaluated has the correct starting angle.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param angle     angle that is to be evaluated.
     */
    public void assertCorrectFilledArcStartAngle(double x, double y, double angle)
    {
        FilledArc shape = (FilledArc)get2DObject(null, new Location(x, y), null, null, null, null);
        if(shape!=null)
        {
            if(shape.getStartAngle()!=angle)
            {
                Assert.fail("The FilledArc at (" + x + "," + y + ") has the wrong start angle.  Expected "+angle+ "but was "
                            +shape.getStartAngle());
            }
        }
        else
        {
            Assert.fail("There is no FilledArc at ("+x+","+y+").");
        }
    }

    /**
     * assertCorrectStartAngle asserts that the shape being evaluated  has the correct starting angle.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param angle     angle that is to be evaluated.

    public void assertCorrectFilledArcStartAngle(String hint, double x, double y, double angle)
    {
       if(!(((FilledArc)get2DObject(null, new Location(x, y), null, null, null, null)).getStartAngle()==angle))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertCorrectArcAngle asserts that the shape being evaluated spans the correct angle.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param angle     angle that is to be evaluated.
     */
    public void assertCorrectFramedArcAngle(double x, double y, double angle)
    {
        FramedArc shape = (FramedArc)get2DObject(null, new Location(x, y), null, null, null, null);
        if(shape!=null)
        {
            if(shape.getArcAngle()!=angle)
            {
                Assert.fail("The FramedArc at (" + x + "," + y + ") has the wrong arc angle.  Expected "+angle+ "but was "
                            +shape.getArcAngle());
            }
        }
        else
        {
            Assert.fail("There is no FramedArc at ("+x+","+y+").");
        }
    }

    /**
     * assertCorrectArcAngle asserts that the shape being evaluated spans the correct angle.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param angle     angle that is to be evaluated.

    public void assertCorrectFramedArcAngle(String hint, double x, double y, double angle)
    {
       if(!(((FramedArc)get2DObject(null, new Location(x, y), null, null, null, null)).getArcAngle()==angle))
       {
           Assert.fail(hint);
       }
    }
    */

    /**
     * assertCorrectArcAngle asserts that the shape being evaluated spans the correct angle.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param angle     angle that is to be evaluated.
     */
    public void assertCorrectFilledArcAngle(double x, double y, double angle)
    {
        FilledArc shape = (FilledArc)get2DObject(null, new Location(x, y), null, null, null, null);
        if(shape!=null)
        {
            if(shape.getArcAngle()!=angle)
            {
                Assert.fail("The FilledArc at (" + x + "," + y + ") has the wrong angle.  Expected "+angle+ "but was "
                            +shape.getArcAngle());
            }
        }
        else
        {
            Assert.fail("There is no FilledArc at ("+x+","+y+").");
        }
    }

    /**
     * assertCorrectArcAngle asserts that the shape being evaluated spans the correct angle.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param angle     angle that is to be evaluated.

    public void assertCorrectFilledArcAngle(String hint, double x, double y, double angle)
    {
       if(!(((FilledArc)get2DObject(null, new Location(x, y), null, null, null, null)).getArcAngle()==angle))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertCorrectArcWidth asserts that the shape being evaluated has a correct corner arc width.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param width     the appropriate width for shape.
     */
    public void assertCorrectFramedRoundedRectArcWidth(double x, double y, double width)
    {
        FramedRoundedRect shape = (FramedRoundedRect)get2DObject(null, new Location(x, y), null, null, null, null);
        if(shape!=null)
        {
            if(shape.getArcWidth()!=width)
            {
                Assert.fail("The FramedRoundedRect at (" + x + "," + y + ") has the wrong arc width.  Expected "+width+ "but was "
                            +shape.getArcWidth());
            }
        }
        else
        {
            Assert.fail("There is no FramedRoundedRect at ("+x+","+y+").");
        }

    }

    /**
     * assertCorrectArcWidth asserts that the shape being evaluated hs a correct corner arc width.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param width     the appropriate arc width for shape.

    public void assertCorrectFramedRoundedRectArcWidth(String hint, double x, double y, double width)
    {
       if(!(((FramedRoundedRect)get2DObject(null, new Location(x, y), null, null, null, null)).getArcWidth()==width))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertCorrectArcHeight asserts that the shape being evaluated has a correct corner arc height.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param height     the appropriate height for shape.
     */
    public void assertCorrectFramedRoundedRectArcHeight(double x, double y, double height)
    {
        FramedRoundedRect shape = (FramedRoundedRect)get2DObject(null, new Location(x, y), null, null, null, null);
        if(shape!=null)
        {
            if(shape.getArcHeight()!=height)
            {
                Assert.fail("The FramedRoundedRect at (" + x + "," + y + ") has the wrong arc height.  Expected "+height+ "but was "
                            +shape.getArcHeight());
            }
        }
        else
        {
            Assert.fail("There is no FramedRoundedRect at ("+x+","+y+").");
        }
    }

    /**
     * assertCorrectArcHeight asserts that the shape being evaluated hs a correct corner arc height.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param height     the appropriate arc width for shape.

    public void assertCorrectFramedRoundedRectArcHeight(String hint, double x, double y, double height)
    {
       if(!(((FramedRoundedRect)get2DObject(null, new Location(x, y), null, null, null, null)).getArcHeight()==height))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertCorrectArcWidth asserts that the shape being evaluated has a correct corner arc width.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param width     the appropriate width for shape.
     */
    public void assertCorrectFilledRoundedRectArcWidth(double x, double y, double width)
    {
        FilledRoundedRect shape = (FilledRoundedRect)get2DObject(null, new Location(x, y), null, null, null, null);
        if(shape!=null)
        {
            if(shape.getArcWidth()!=width)
            {
                Assert.fail("The FilledRoundedRect at (" + x + "," + y + ") has the wrong arc width.  Expected "+width+ "but was "
                            +shape.getArcWidth());
            }
        }
        else
        {
            Assert.fail("There is no FramedArc at ("+x+","+y+").");
        }
    }

    /**
     * assertCorrectArcWidth asserts that the shape being evaluated hs a correct corner arc width.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param width     the appropriate arc width for shape.

    public void assertCorrectFilledRoundedRectArcWidth(String hint, double x, double y, double width)
    {
       if(!(((FilledRoundedRect)get2DObject(null, new Location(x, y), null, null, null, null)).getArcWidth()==width))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertCorrectArcHeight asserts that the shape being evaluated has a correct corner arc height.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param height     the appropriate height for shape.
     */
    public void assertCorrectFilledRoundedRectArcHeight(double x, double y, double height)
    {
        FilledRoundedRect shape = (FilledRoundedRect)get2DObject(null, new Location(x, y), null, null, null, null);
        if(shape!=null)
        {
            if(shape.getArcHeight()!=height)
            {
                Assert.fail("The FilledRoundedRect at (" + x + "," + y + ") has the wrong arc height.  Expected "+height+ "but was "
                            +shape.getArcHeight());
            }
        }
        else
        {
            Assert.fail("There is no FilledRoundedRect at ("+x+","+y+").");
        }
    }

    /**
     * assertCorrectArcHeight asserts that the shape being evaluated hs a correct corner arc height.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param height     the appropriate arc width for shape.

    public void assertCorrectFilledRoundedRectArcHeight(String hint, double x, double y, double height)
    {
       if(!(((FilledRoundedRect)get2DObject(null, new Location(x, y), null, null, null, null)).getArcHeight()==height))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertCorrectText asserts that the textbox being evaluated is displaying the correct text.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param text      desired text
     */
    public void assertCorrectText(double x, double y, String text)
    {
        Text shape = (Text)get2DObject(null, new Location(x, y), null, null, null, null);
        if(shape!=null)
        {
            if(!(shape.getText().equals(text)))
            {
                Assert.fail("The Text at (" + x + "," + y + ") has the wrong text.  Expected "+text+ "but was "
                            +shape.getText());
            }
        }
        else
        {
            Assert.fail("There is no Text at ("+x+","+y+").");
        }
    }

    /**
     * assertCorrectText asserts that the textbox being evaluated is displaying the correct text.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param text      desired text.

    public void assertCorrectText(String hint, double x, double y, String text)
    {
       if(!((((Text)get2DObject(null, new Location(x, y), null, null, null, null)).getText()).equals(text)))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertCorrectFont asserts that the textbox being evaluated is displaying the correct font.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param font      desired font
     */
    public void assertCorrectFont(double x, double y, Font font)
    {
        Text shape = (Text)get2DObject(null, new Location(x, y), null, null, null, null);
        if(shape!=null)
        {
            if(!(shape.getFont().equals(font)))
            {
                Assert.fail("The Text at (" + x + "," + y + ") has the wrong font.  Expected "+font.toString()+ "but was "
                            +shape.getFont().toString());
            }
        }
        else
        {
            Assert.fail("There is no Text at ("+x+","+y+").");
        }
    }

    /**
     * assertCorrectFont asserts that the textbox being evaluated is displaying the correct font.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param font      desired font.

    public void assertCorrectFont(String hint, double x, double y, Font font)
    {
       if(!((((Text)get2DObject(null, new Location(x, y), null, null, null, null)).getFont()).equals(font)))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertIsAnEndpoint asserts that a line being tested has an endpoint at the given position.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param epx     x coordinate of point
     * @param epy     y coordinate of point
     */
    public void assertIsAnEndpoint(double x, double y, double epx, double epy)
    {

        Line shape = getLine(new Location(x, y), null, null, null);
        if(shape!=null)
        {
            if(!(((shape.getStart()).equals(new Location(epx, epy)))||((shape.getEnd()).equals(new Location(epx, epy)))))
            {
                Assert.fail("The Line at (" + x + "," + y + ") does not have an endpoint at (" + epx + "," + epy + ").");
            }
        }
        else
        {
            Assert.fail("There is no Line at ("+x+","+y+").");
        }
    }

    /**
     * assertIsAnEndpoint asserts that a line being tested has an endpoint at the given position.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param epx     x coordinate of point
     * @param epy     y coordinate of point

    public void assertCorrectStartPoint(String hint, double x, double y, double epx, double epy)
    {
       if(!(((getLine(new Location(x, y), null, null, null).getStart()).equals(new Location(x, y))) || ((getLine(new Location(x, y), null, null, null).getEnd()).equals(new Location(x,y)))))
       {
           Assert.fail(hint);
       }
    }
     */

   /**
     * assertCorrectImage asserts that the VisibleImage displayed is the correct image.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param image     the correct image to be displayed in VisibleImage.
     */
    public void assertCorrectImage(double x, double y, Image image)
    {
        VisibleImage shape = (VisibleImage)get2DObject(null, new Location(x, y), null, null, null, null);
        if(shape!=null)
        {
            if(!(shape.getImage().equals(image)))
            {
                Assert.fail("The Image at (" + x + "," + y + ") has the wrong image.");
            }
        }
        else
        {
            Assert.fail("There is no VisibleImage at ("+x+","+y+").");
        }
    }

    /**
     * assertCorrectImage asserts that the VisibleImage displayed is the correct image.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param x        the x coordinate of the shape being evaluated.
     * @param y        the y coordinate of the shape being evaluated.
     * @param image     the correct image to be displayed in VisibleImage.

    public void assertCorrectImage(String hint, double x, double y, Image image)
    {
       if(!((((VisibleImage)get2DObject(null, new Location(x, y), null, null, null, null)).getImage()).equals(image)))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertColor asserts that the shape being evaluated has the desired color.
     *
     * @param shape     the shape being evaluated.
     * @param color     the color that is desired.
     */
    public void assertColor(DrawableInterface shape, java.awt.Color color)
    {
        if(shape!=null)
        {
            if(!(shape.getColor()).equals(color))
            {
                Assert.fail("The 2D shape has the wrong color.  Expected "+color+ "but was "
                            +shape.getColor());
            }
        }
        else
        {
            Assert.fail("There is no 2D shape as a parameter.");
        }
    }

    /**
     * assertColor asserts that the shape being evaluated has the desired color.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param shape     the shape being evaluated.
     * @param color     the color that is desired.

    public void assertColor(String hint, DrawableInterface shape, java.awt.Color color)
    {
       if(!(shape.getColor()).equals(color))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertVisible asserts that the shape being evaluated is visible
     *
     * @param shape     the shape being evaluated.
     */
    public void assertVisible(DrawableInterface shape)
    {
        if(shape!=null)
        {
            if(shape.isHidden())
            {
                Assert.fail("The 2D shape is not visible.");
            }
        }
        else
        {
            Assert.fail("There is no 2D shape.");
        }
    }

    /**
     * assertVisible asserts that the shape being evaluated is visible
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param shape     the shape being evaluated

    public void assertVisible(String hint, DrawableInterface shape)
    {
       if(shape.isHidden())
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertInvisible asserts that the shape being evaluated is invisible to the canvas.
     *
     * @param shape     the shape being evaluated.
     */
    public void assertInvisible(DrawableInterface shape)
    {
        if(shape!=null)
        {
            if(!(shape.isHidden()))
            {
                Assert.fail("The 2D shape is not invisible.");
            }
        }
        else
        {
            Assert.fail("There is no 2D shape.");
        }
    }

    /**
     * assertInvisible asserts that the shape being evaluated is invisible to the canvas.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param shape     the shape being evaluated.

    public void assertInvisible(String hint, DrawableInterface shape)
    {
       if(!(shape.isHidden()))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertWidth asserts that the shape being evaluated is a desired width.
     *
     * @param shape     the shape being evaluated
     * @param width     the desired width of shape
     */
    public void assertWidth(Drawable2DInterface shape, double width)
    {
        if(shape!=null)
        {
            if(shape.getWidth()!=width)
            {
                Assert.fail("The 2D shape has the wrong width.  Expected "+width+ "but was "
                            +shape.getWidth());
            }
        }
        else
        {
            Assert.fail("There is no 2D shape.");
        }
    }

    /**
     * assertWidth asserts that the shape being evaluated is a desired width.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param shape     the shape being evaluated.
     * @param height        the desired width of shape.

    public void assertWidth(String hint, Drawable2DInterface shape, double width)
    {
       if(!(shape.getWidth()==width))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertHeight asserts that the shape being evaluated is a desired height.
     *
     * @param shape     the shape being evaluated.
     * @param height        the desired height of shape.
     */
    public void assertHeight(Drawable2DInterface shape, double height)
    {
        if(shape!=null)
        {
            if(shape.getHeight()!=height)
            {
                Assert.fail("The 2D shape has the wrong height.  Expected "+height+ "but was "
                            +shape.getHeight());
            }
        }
        else
        {
            Assert.fail("There is no 2D shape.");
        }
    }

    /**
     * assertHeight asserts that the shape being evaluated is a desired height.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param shape     the shape being evaluated.
     * @param height        the desired height of shape

    public void assertHeight(String hint, Drawable2DInterface shape, double height)
    {
       if(!(shape.getHeight()==height))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertLocationInShape asserts that the shape being evaluated includes the specified (x,y) coordinate.
     *
     * @param shape     the shape being evaluated.
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertLocationInShape(Drawable2DInterface shape, double x, double y)
    {
        if(shape!=null)
        {
            if(!shape.contains(new Location(x, y)))
            {
                Assert.fail("The 2D shape does not include the desired point.");
            }
        }
        else
        {
            Assert.fail("There is no 2D shape.");
        }
    }

    /**
     * assertLocationInShape asserts that the shape being evaluated includes the specified (x, y) coordinate
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param shape     the shape being evaluated.
     * @param x     the x coordinate.
     * @param y     the y coordinate.

    public void assertLocationInShape(String hint, Drawable2DInterface shape, double x, double y)
    {
       if(!(shape.contains(new Location(x,y))))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertCorrectStartAngle asserts that the shape being evaluated has the correct starting angle.
     *
     * @param shape     the shape being evaluated.
     * @param angle     angle that is to be evaluated.
     */
    public void assertCorrectStartAngle(FramedArc shape, double angle)
    {
        if(shape!=null)
        {
            if(shape.getStartAngle()!=angle)
            {
                Assert.fail("The FramedArc has the wrong start angle.  Expected "+angle+ "but was "
                            +shape.getStartAngle());
            }
        }
        else
        {
            Assert.fail("There is no FramedArc.");
        }
    }

    /**
     * assertCorrectStartAngle asserts that the shape being evaluated has the correct starting angle
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param shape     the shape being evaluated.
     * @param angle     angle that is to be evaluated.

    public void assertCorrectStartAngle(String hint, FramedArc shape, double angle)
    {
       if(!(shape.getStartAngle()==angle))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertCorrectStartAngle asserts that the shape being evaluated has the correct starting angle.
     *
     * @param shape     the shape being evaluated.
     * @param angle     angle that is to be evaluated.
     */
    public void assertCorrectStartAngle(FilledArc shape, double angle)
    {
        if(shape!=null)
        {
            if(shape.getStartAngle()!=angle)
            {
                Assert.fail("The FilledArc has the wrong start angle.  Expected "+angle+ "but was "
                            +shape.getStartAngle());
            }
        }
        else
        {
            Assert.fail("There is no FilledArc.");
        }
    }

    /**
     * assertCorrectStartAngle asserts that the shape being evaluated  has the correct starting angle.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param shape     the shape being evaluated.
     * @param angle     angle that is to be evaluated.

    public void assertCorrectStartAngle(String hint, FilledArc shape, double angle)
    {
       if(!(shape.getStartAngle()==angle))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertCorrectArcAngle asserts that the shape being evaluated spans the correct angle.
     *
     * @param shape     the shape being evaluated.
     * @param angle     angle that is to be evaluated.
     */
    public void assertCorrectArcAngle(FramedArc shape, double angle)
    {
        if(shape!=null)
        {
            if(shape.getArcAngle()!=angle)
            {
                Assert.fail("The FramedArc has the wrong arc angle.  Expected "+angle+ "but was "
                            +shape.getArcAngle());
            }
        }
        else
        {
            Assert.fail("There is no FramedArc.");
        }
    }

    /**
     * assertCorrectArcAngle asserts that the shape being evaluated spans the correct angle.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param shape     the shape being evaluated.
     * @param angle     angle that is to be evaluated.

    public void assertCorrectArcAngle(String hint, FramedArc shape, double angle)
    {
       if(!(shape.getArcAngle()==angle))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertCorrectArcAngle asserts that the shape being evaluated spans the correct angle.
     *
     * @param shape     the shape being evaluated.
     * @param angle     angle that is to be evaluated.
     */
    public void assertCorrectArcAngle(FilledArc shape, double angle)
    {
        if(shape!=null)
        {
            if(shape.getStartAngle()!=angle)
            {
                Assert.fail("The FilledArc has the wrong arc angle.  Expected "+angle+ " but was "
                            +shape.getArcAngle());
            }
        }
        else
        {
            Assert.fail("There is no FilledArc.");
        }
    }

    /**
     * assertCorrectArcAngle asserts that the shape being evaluated spans the correct angle.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param shape     the shape being evaluated.
     * @param angle     angle that is to be evaluated.

    public void assertCorrectArcAngle(String hint, FilledArc shape, double angle)
    {
       if(!(shape.getArcAngle()==angle))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertCorrectArcWidth asserts that the shape being evaluated has a correct corner arc width.
     *
     * @param shape     the shape being evaluated.
     * @param width     the appropriate width for shape.
     */
    public void assertCorrectArcWidth(FramedRoundedRect shape, double width)
    {
        if(shape!=null)
        {
            if(shape.getArcWidth()!=width)
            {
                Assert.fail("The FramedRoundedRect has the wrong arc width.  Expected "+width+ " but was "
                            +shape.getArcWidth());
            }
        }
        else
        {
            Assert.fail("There is no FramedRoundedRect.");
        }
    }

    /**
     * assertCorrectArcWidth asserts that the shape being evaluated hs a correct corner arc width.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param shape     the shape being evaluated.
     * @param width     the appropriate arc width for shape.

    public void assertCorrectArcWidth(String hint, FramedRoundedRect shape, double width)
    {
       if(!(shape.getArcWidth()==width))
       {
           Assert.fail(hint);
       }
    }
     */


    /**
     * assertCorrectArcHeight asserts that the shape being evaluated has a correct corner arc height.
     *
     * @param shape     the shape being evaluated.
     * @param height     the appropriate height for shape.
     */
    public void assertCorrectArcHeight(FramedRoundedRect shape, double height)
    {
        if(shape!=null)
        {
            if(shape.getArcHeight()!=height)
            {
                Assert.fail("The FramedRoundedRect has the wrong arc height.  Expected "+height+ " but was "
                            +shape.getArcHeight());
            }
        }
        else
        {
            Assert.fail("There is no FramedRoundedRect.");
        }
    }

    /**
     * assertCorrectArcHeight asserts that the shape being evaluated hs a correct corner arc height.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param shape     the shape being evaluated.
     * @param height     the appropriate arc width for shape.

    public void assertCorrectArcHeight(String hint, FramedRoundedRect shape, double height)
    {
       if(!(shape.getArcHeight()==height))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertCorrectArcWidth asserts that the shape being evaluated has a correct corner arc width.
     *
     * @param shape     the shape being evaluated.
     * @param width     the appropriate width for shape.
     */
    public void assertCorrectArcWidth(FilledRoundedRect shape, double width)
    {
        if(shape!=null)
        {
            if(shape.getArcWidth()!=width)
            {
                Assert.fail("The FilledRoundedRect has the wrong arc width.  Expected "+width+ " but was "
                            +shape.getArcWidth());
            }
        }
        else
        {
            Assert.fail("There is no FilledRoundedRect.");
        }
    }

    /**
     * assertCorrectArcWidth asserts that the shape being evaluated hs a correct corner arc width.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param shape     the shape being evaluated.
     * @param width     the appropriate arc width for shape.

    public void assertCorrectArcWidth(String hint, FilledRoundedRect shape, double width)
    {
       if(!(shape.getArcWidth()==width))
       {
           Assert.fail(hint);
       }
    }
     */


    /**
     * assertCorrectArcHeight asserts that the shape being evaluated has a correct corner arc height.
     *
     * @param shape     the shape being evaluated.
     * @param height     the appropriate height for shape.
     */
    public void assertCorrectArcHeight(FilledRoundedRect shape, double height)
    {
        if(shape!=null)
        {
            if(shape.getArcHeight()!=height)
            {
                Assert.fail("The FilledRoundedRect has the wrong arc height.  Expected "+height+ " but was "
                            +shape.getArcHeight());
            }
        }
        else
        {
            Assert.fail("There is no FilledRoundedRect.");
        }
    }

    /**
     * assertCorrectArcHeight asserts that the shape being evaluated hs a correct corner arc height.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param shape     the shape being evaluated.
     * @param height     the appropriate arc width for shape.

    public void assertCorrectArcHeight(String hint, FilledRoundedRect shape, double height)
    {
       if(!(shape.getArcHeight()==height))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertCorrectText asserts that the textbox being evaluated is displaying the correct text.
     *
     * @param shape     the shape being evaluated.
     * @param text      desired text
     */
    public void assertCorrectText(Text shape, String text)
    {
        if(shape!=null)
        {
            if(!(shape.getText().equals(text)))
            {
                Assert.fail("The Text has the wrong text.  Expected "+text+ "but was "
                            +shape.getText());
            }
        }
        else
        {
            Assert.fail("There is no Text at.");
        }
    }

    /**
     * assertCorrectText asserts that the textbox being evaluated is displaying the correct text.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param shape     the shape being evaluated.
     * @param text      desired text.

    public void assertCorrectText(String hint, Text shape, String text)
    {
       if(!((shape.getText()).equals(text)))
       {
           Assert.fail(hint);
       }
    }
     */


    /**
     * assertCorrectFont asserts that the textbox being evaluated is displaying the correct font.
     *
     * @param shape     the shape being evaluated.
     * @param font      desired font
     */
    public void assertCorrectFont(Text shape, Font font)
    {
        if(shape!=null)
        {
            if(!(shape.getFont().equals(font)))
            {
                Assert.fail("The Text at has the wrong font.  Expected "+font.toString()+ " but was "
                            +shape.getFont().toString());
            }
        }
        else
        {
            Assert.fail("There is no Text at.");
        }
    }

    /**
     * assertCorrectFont asserts that the textbox being evaluated is displaying the correct font.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param shape     the shape being evaluated.
     * @param font      desired font.

    public void assertCorrectFont(String hint, Text shape, Font font)
    {
       if(!((shape.getFont()).equals(font)))
       {
           Assert.fail(hint);
       }
    }
     */

    /**
     * assertIsAnEndpoint asserts that a line being tested has an endpoint at the given position.
     *
     * @param shape     the shape being evaluated.
     * @param x     x coordinate of point
     * @param y     y coordinate of point
     */
    public void assertIsAnEndpoint(Line shape, double x, double y)
    {
        if(shape!=null)
        {
            if(!(((shape.getStart()).equals(new Location(x,y)))||((shape.getEnd()).equals(new Location(x,y)))))
            {
                Assert.fail("The Line does not have an endpoint at (" + x + "," + y + ").");
            }
        }
        else
        {
            Assert.fail("There is no Line.");
        }
    }

    /**
     * assertIsAnEndpoint asserts that a line being tested has an endpoint at the given position.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param shape     the shape being evaluated.
     * @param x     x coordinate of point
     * @param y     y coordinate of point
     *
    public void assertCorrectStartPoint(String hint, Line shape, double x, double y)
    {
       if(!(((shape.getStart()).equals(new Location(x, y))) || ((shape.getEnd()).equals(new Location(x,y)))))
       {
           Assert.fail(hint);
       }
    }
     */

   /**
     * assertCorrectImage asserts that the VisibleImage displayed is the correct image.
     *
     * @param shape     the shape being evaluated.
     * @param image     the correct image to be displayed in VisibleImage.
     */
    public void assertCorrectImage(VisibleImage shape, Image image)
    {
        if(shape!=null)
        {
            if(!(shape.getImage().equals(image)))
            {
                Assert.fail("The Image has the wrong image.");
            }
        }
        else
        {
            Assert.fail("There is no VisibleImage.");
        }
    }

    /*
     * assertCorrectImage asserts that the VisibleImage displayed is the correct image.
     * Prints a custom error message in the event of an assertion failure.
     *
     * @param shape     the shape being evaluated.
     * @param image     the correct image to be displayed in VisibleImage.

    public void assertCorrectImage(String hint, VisibleImage shape, Image image)
    {
       if(!((shape.getImage()).equals(image)))
       {
           Assert.fail(hint);
       }
    }
    */
}
