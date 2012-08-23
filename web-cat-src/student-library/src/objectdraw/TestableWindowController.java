package objectdraw;
import objectdraw.*;
import junit.framework.Assert;
import java.awt.Component;

import java.awt.event.*;

import java.util.*;
import javax.swing.JFrame;
import java.awt.Graphics;

// -------------------------------------------------------------------------
/**
 *  TestableWindowController is an FrameWindowController with various methods
 *  to assert things about the state of the Window.
 *  @author  Roy Patrick Tan
 *  @version June 5, 2006
 */


public class TestableWindowController extends objectdraw.FrameWindowController
{
    private VTWindowControllerListener listener;
    //private volatile InitLock init_lock = new InitLock();

    private volatile boolean init_done;

    /**
     * This constructor forces TestableWindowController to wait for the initial window to
     * draw itself before exiting the constructor.
     */
    public TestableWindowController() {
        //System.out.println("TestableWindowController constructor waiting for init...");
        waitForInit();
        //System.out.println("TestableWindowController constructor done.");
    }

/*
    private boolean isLocked() {
        return init_lock.isLocked();
    }
*/
    /**
     * Get the top-level JFrame component.  You will not use this function for developing
     * your test cases.
     */
    public JFrame getFrame() {
        java.awt.Container c = this.getParent();
        while (c != null && ! (c instanceof JFrame)) {
            c = c.getParent();
        }
        return (JFrame) c;
    }

    /**
     * Get the listener for this TestableWindowController.  You will not use this function
     * for developing your test cases.
     */
    VTWindowControllerListener getListener() {
        if(listener == null)
        {
            MouseListener[] listeners = ((Component) canvas).getMouseListeners();
            for(int i = 0; i < listeners.length; i++) {
                if(listeners[i] instanceof VTWindowControllerListener) {
                    listener = (VTWindowControllerListener) listeners[i];
                    return listener;
                }
            }
        }
        return listener;
    }

    /**
    * This method forces the program to wait on a thread to release the init lock.
    * you will not use this function for developing your test cases.
    */
    public void waitForInit() {

        for( int i = 0; i < 10 && !init_done; i++ ) {
            try{
                //System.out.println("waiting...");
                Thread.sleep(200);
            } catch (Exception e) {
                System.out.println("thread interrupted.");
            }
        }
        if( ! init_done ) {
            System.out.println("Init did not occur, trying to force init...");
            repaint();
            waitForInit();
        }
    }

    /**
     * This init registers our own Listener to the WindowController. You will
     * not use this function for developing your test cases.
     */
    protected void helpinit() {

        //System.out.println("begin helpinit");
        super.helpinit();

        //next line: somehow BlueJ balks at calling getMouseListeners() directly.
        Component c = (Component) canvas;
        MouseListener[] listeners = c.getMouseListeners();
        for(int i = 0; i < listeners.length; i++) {
            c.removeMouseListener(listeners[i]);
            c.removeMouseMotionListener( (MouseMotionListener) listeners[i]);
        }
        listener = new VTWindowControllerListener(this, canvas);
        c.addMouseListener(listener);
        c.addMouseMotionListener(listener);

        System.out.println("setting init_done to true...");
        init_done = true;
        //System.out.println("end HelpInit");
    }

    /**
     * Paint gets called by the system whenever the application needs to be repainted.
     */
    public void paint(Graphics g) {
        //System.out.println("begin paint.");
        //waitForInit();
        super.paint(g);
        //waitForInit();
        //System.out.println("End paint.");
    }

    //~ Methods ...............................................................
    /**
     * has2dObject determines existence of a 2D object with the specified
     * parameters. Every parameter can be passed null to mean "any".
     *
     * @param shape     the shape of the object. null for any shape.
     * @param loc       the location of the object on the canvas, null for any
     *                  location.
     * @param width     the width of the object, null for any width.
     * @param height    the height of the object, null for any height.
     * @param color     the color of the object, null for any color.
     * @param visible   whether the object is visible on the canvas or not.
     *                  pass null, if either.
     *
     * @return true if it has the 2D object specified, false if not.
     */
    public boolean has2DObject(
        Class<? extends Drawable2DInterface> shape,
        Location loc,
        Double width,
        Double height,
        java.awt.Color color,
        Boolean visible)
    {
        DrawableIterator i = canvas.getDrawableIterator();

        while (i.hasNext())
        {
            DrawableInterface obj = i.next();
            if (obj instanceof Drawable2DInterface)
            {
                Drawable2DInterface obj2d = (Drawable2DInterface) obj;
                if (shape != null && !shape.isAssignableFrom(obj2d.getClass()))
                {
                    //if the shape is not the same as specified
                    continue;
                }

                if (loc != null && ! loc.equals(obj2d.getLocation()))
                {
                    continue;
                }

                if (width != null && width.doubleValue() != obj2d.getWidth())
                {
                    continue;
                }

                if (height != null
                    && height.doubleValue() != obj2d.getHeight())
                {
                    continue;
                }

                if (color != null && ! color.equals(obj2d.getColor()))
                {
                    continue;
                }

                if (visible != null
                    && visible.booleanValue() == obj2d.isHidden())
                {
                    continue;
                }
                return true; // all criteria passed
            } // else it's not a 2d interface so move on.

        } // end of while
        return false;
    }

    /**
     * get2dObject returns an arbitrary shape that meets the specified
     * parameters.  That arbitrary shape is the first one that appears in
     * the DrawableIterator for the canvas.  A null parameter can mean "any".
     *
     * @param shape     the shape of the object. null for any shape.
     * @param loc       the location of the object on the canvas, null for any
     *                  location.
     * @param width     the width of the object, null for any width.
     * @param height    the height of the object, null for any height.
     * @param color     the color of the object, null for any color.
     * @param visible   whether the object is visible on the canvas or not.
     *                  pass null, if either.
     *
     * @return an arbitrary shape that fits the parameters passed, or null
     * if no such element exists.
     */
    public Drawable2DInterface get2DObject(
        Class<? extends Drawable2DInterface> shape,
        Location loc,
        Double width,
        Double height,
        java.awt.Color color,
        Boolean visible)
    {
        Drawable2DInterface element = null;
        boolean found = false;
        DrawableIterator i = canvas.getDrawableIterator();

        while(i.hasNext() && !found)
        {
            DrawableInterface obj = i.next();
            if (obj instanceof Drawable2DInterface)
            {
                Drawable2DInterface obj2d = (Drawable2DInterface) obj;
                if (shape != null && !shape.isAssignableFrom(obj2d.getClass()))
                {
                    //if the shape is not the same as specified
                    continue;
                }

                if (loc != null && ! loc.equals(obj2d.getLocation()))
                {
                    continue;
                }

                if (width != null && width.doubleValue() != obj2d.getWidth())
                {
                    continue;
                }

                if (height != null
                    && height.doubleValue() != obj2d.getHeight())
                {
                    continue;
                }

                if (color != null && ! color.equals(obj2d.getColor()))
                {
                    continue;
                }

                if (visible != null
                    && visible.booleanValue() == obj2d.isHidden())
                {
                    continue;
                }
                element = obj2d; // all criteria passed
                found = true;
            } // else it's not a 2d interface so move on.

        } // end of while
        return element;
    }

    /**
     * getLine returns an arbitrary Line object that fits the parameters that
     * are passed to the function.  A null parameter means "any."
     *
     * @param start     the starting point of the line, null for any starting
     *                  location
     * @param end       the end location of the line, null for any end location
     * @param color     the color of the line, null for any color.
     * @param visible   whether the line is visible on the canvas or not.
     *                  pass null, if either.
     *
     * @return  an arbitrary Line that fits the parameters passed, or null,
     * if no such line exists.
     */
    public Line getLine(
        Location start, Location end, java.awt.Color color, Boolean visible)
    {

        Line element = null;

        DrawableIterator i = canvas.getDrawableIterator();

        while (i.hasNext())
        {
            DrawableInterface obj = i.next();
            if(obj instanceof Line) {
                Line line = (Line) obj;
                if(start != null && ! start.equals(line.getStart())) {
                    //if the shape is not the same as specified
                    continue;
                }

                if(end != null && ! end.equals(line.getEnd())) {
                    //if the shape is not the same as specified
                    continue;
                }

                if(color != null && ! color.equals(line.getColor())) {
                    continue;
                }

                if(visible != null && visible.booleanValue() == line.isHidden()) {
                    continue;
                }
                element = line; //all criteria passed
            } //else it's not a line so move on.

        } //end of while
        return element;
    }

    /**
     * get2dObject gets a list of 2D object with the specified
     * parameters. Every parameter can be passed null to mean "any".
     *
     * @param shape     the shape of the object. null for any shape.
     * @param loc       the location of the object on the canvas, null for any
     *                  location.
     * @param width     the width of the object, null for any width.
     * @param height    the height of the object, null for any height.
     * @param color     the color of the object, null for any color.
     * @param visible   whether the object is visible on the canvas or not.
     *                  pass null, if either.
     *
     * @return a List of the 2D objects specified, the list may be empty
     */
    public List<Drawable2DInterface> get2DObjects(Class<?> shape,
                      Location loc, Double width, Double height,
                      java.awt.Color color, Boolean visible)
    {
        List<Drawable2DInterface> list = new ArrayList<Drawable2DInterface>();

        DrawableIterator i = canvas.getDrawableIterator();

        while(i.hasNext()) {
            DrawableInterface obj = i.next();
            if(obj instanceof Drawable2DInterface) {
                Drawable2DInterface obj2d = (Drawable2DInterface) obj;
                if(shape != null && !shape.isAssignableFrom(obj2d.getClass())) {
                    //if the shape is not the same as specified
                    continue;
                }
                if(loc != null && ! loc.equals(obj2d.getLocation())) {
                    continue;
                }

                if(width != null && width.doubleValue() != obj2d.getWidth()) {
                    continue;
                }

                if(height != null && height.doubleValue() != obj2d.getHeight()) {
                    continue;
                }

                if(color != null && ! color.equals(obj2d.getColor())) {
                    continue;
                }

                if(visible != null && visible.booleanValue() == obj2d.isHidden()) {
                    continue;
                }
                list.add(obj2d); //all criteria passed
            } //else it's not a 2d interface so move on.

        } //end of while
        return list;
    }



    /**
     * Accessor function to get the DrawingCanvas from the frame. This is for
     * testing purposes.
     */
    public DrawingCanvas getCanvas() {
        return canvas;
    }

    /**
     * assert version of has2DObject
     *
     * @param shape     the shape of the object. null for any shape.
     * @param loc       the location of the object on the canvas, null for any
     *                  location.
     * @param width     the width of the object, null for any width.
     * @param height    the height of the object, null for any height.
     * @param color     the color of the object, null for any color.
     * @param visible   whether the object is visible on the canvas or not.
     *                  pass null, if either.
     */
    public void assertHas2DObject(
        Class<? extends Drawable2DInterface> shape,
        Location loc,
        Double width,
        Double height,
        java.awt.Color color,
        Boolean visible)
    {
        if (!has2DObject(shape, loc, width, height, color, visible))
        {
            Assert.fail("Did not find 2d object of type: "
                + shape.getClass().toString()
                + " location: " + loc
                + " width: " + width
                + " height: " + height
                + " color: " + color
                + " visibility: " + visible);
        }
    }

    /**
     * Inverse of assertHas2DObject(), this method will fail if an object
     * specified by the parameters is found, succeed otherwise.
     *
     * @param shape     the shape of the object. null for any shape.
     * @param loc       the location of the object on the canvas, null for any
     *                  location.
     * @param width     the width of the object, null for any width.
     * @param height    the height of the object, null for any height.
     * @param color     the color of the object, null for any color.
     * @param visible   whether the object is visible on the canvas or not.
     *                  pass null, if either.
     *
     */
    public void assertNo2DObject(
        Class<? extends Drawable2DInterface> shape,
        Location loc,
        Double width,
        Double height,
        java.awt.Color color,
        Boolean visible)
    {
        if (has2DObject(shape, loc, width, height, color, visible) )
        {
            Assert.fail("Found a 2d object of type: "
                + shape.getClass().toString()
                + " location: " + loc
                + " width: " + width
                + " height: " + height
                + " color: " + color
                + " visibility: " + visible);
        }
    }

    /**
     * hasLine returns determines the existence of a line with the specified parameters.
     * Any of the parameters can be null to mean "dont care".
     *
     * @param start     the starting point of the line, null for any starting location
     * @param end       the end location of the line, null for any end location
     * @param color     the color of the line, null for any color.
     * @param visible   whether the line is visible on the canvas or not.
     *                  pass null, if either.
     *
     * @return  true if the line exists.
     */
    public boolean hasLine(Location start, Location end, java.awt.Color color, Boolean visible) {

        DrawableIterator i = canvas.getDrawableIterator();

        while(i.hasNext()) {
            DrawableInterface obj = i.next();
            if(obj instanceof Line) {
                Line line = (Line) obj;
                if(start != null && ! start.equals(line.getStart())) {
                    //if the shape is not the same as specified
                    continue;
                }

                if(end != null && ! end.equals(line.getEnd())) {
                    //if the shape is not the same as specified
                    continue;
                }

                if(color != null && ! color.equals(line.getColor())) {
                    continue;
                }

                if(visible != null && visible.booleanValue() == line.isHidden()) {
                    continue;
                }
                return true; //all criteria passed
            } //else it's not a line so move on.

        } //end of while
        return false;
    }

    /**
     * getLines gets a list of lines with the specified parameters.
     * Any of the parameters can be null to mean "dont care".
     *
     * @param start     the starting point of the line, null for any starting location
     * @param end       the end location of the line, null for any end location
     * @param color     the color of the line, null for any color.
     * @param visible   whether the line is visible on the canvas or not.
     *                  pass null, if either.
     *
     * @return  a possibly empty list of lines that satisfy the scriteria specified by the
     *          parameters.
     */
    public List<Line> getLines(Location start, Location end, java.awt.Color color, Boolean visible) {

        List<Line> list = new ArrayList<Line>();

        DrawableIterator i = canvas.getDrawableIterator();

        while(i.hasNext()) {
            DrawableInterface obj = i.next();
            if(obj instanceof Line) {
                Line line = (Line) obj;
                if(start != null && ! start.equals(line.getStart())) {
                    //if the shape is not the same as specified
                    continue;
                }

                if(end != null && ! end.equals(line.getEnd())) {
                    //if the shape is not the same as specified
                    continue;
                }

                if(color != null && ! color.equals(line.getColor())) {
                    continue;
                }

                if(visible != null && visible.booleanValue() == line.isHidden()) {
                    continue;
                }
                list.add(line); //all criteria passed
            } //else it's not a line so move on.

        } //end of while
        return list;
    }

    /**
     * assertHasLine asserts the existence of a line with the specified parameters.
     * This method fails if no such line exists.
     *
     * @param start     the starting point of the line, null for any starting location
     * @param end       the end location of the line, null for any end location
     * @param color     the color of the line, null for any color.
     * @param visible   whether the line is visible on the canvas or not.
     *                  pass null, if either.
     *
     */
    public void assertHasLine(Location start, Location end, java.awt.Color color, Boolean visible) {
        if (! hasLine(start, end, color, visible)) {
            Assert.fail();
        }
    }

    /**
     * assertNoLine asserts the non-existence of a line with the specified parameters.
     * This method fails if such line exists.
     *
     * @param start     the starting point of the line, null for any starting location
     * @param end       the end location of the line, null for any end location
     * @param color     the color of the line, null for any color.
     * @param visible   whether the line is visible on the canvas or not.
     *                  pass null, if either.
     *
     */
    public void assertNoLine(Location start, Location end, java.awt.Color color, Boolean visible) {
        if (hasLine(start, end, color, visible)) {
            Assert.fail();
        }
    }

    /**
     * assert version of has2DObject that has a specific hint to the user.
     *
     * @param hint      error message that should be displayed for a failed assertion
     * @param shape     the shape of the object. null for any shape.
     * @param loc       the location of the object on the canvas, null for any
     *                  location.
     * @param width     the width of the object, null for any width.
     * @param height    the height of the object, null for any height.
     * @param color     the color of the object, null for any color.
     * @param visible   whether the object is visible on the canvas or not.
     *                  pass null, if either.
     */
    public void assertHas2DObject(
        String hint,
        Class<? extends Drawable2DInterface> shape,
        Location loc,
        Double width,
        Double height,
        java.awt.Color color,
        Boolean visible)
    {
        if (!has2DObject(shape, loc, width, height, color, visible))
        {
            Assert.fail(hint);
        }
    }

    /**
     * inverse of assertHas2DObject, this method will fail if an object specified
     * by the parameters is found, suceed otherwise.  Prints a custom message in
     * the event of failure.
     *
     * @param hint      error message that should be displayed for a failed assertion
     * @param shape     the shape of the object. null for any shape.
     * @param loc       the location of the object on the canvas, null for any
     *                  location.
     * @param width     the width of the object, null for any width.
     * @param height    the height of the object, null for any height.
     * @param color     the color of the object, null for any color.
     * @param visible   whether the object is visible on the canvas or not.
     *                  pass null, if either.
     *
     */
    public void assertNo2DObject(
        String hint,
        Class<? extends Drawable2DInterface> shape,
        Location loc,
        Double width,
        Double height,
        java.awt.Color color,
        Boolean visible)
    {
        if (has2DObject(shape, loc, width, height, color, visible) )
        {
            Assert.fail(hint);
        }
    }

    /**
     * assertHasLine asserts the existence of a line with the specified parameters.
     * This method fails if no such line exists.  Prints a custom error message in the event
     * of failure.
     *
     * @param hint      error message that should be displayed for a failed assertion
     * @param start     the starting point of the line, null for any starting location
     * @param end       the end location of the line, null for any end location
     * @param color     the color of the line, null for any color.
     * @param visible   whether the line is visible on the canvas or not.
     *                  pass null, if either.
     *
     */
    public void assertHasLine(String hint, Location start, Location end, java.awt.Color color, Boolean visible) {
        if (! hasLine(start, end, color, visible)) {
            Assert.fail(hint);
        }
    }


    /**
     * assertNoLine iasserts the non-existence of a line with the specified parameters.
     * This method fails if such line exists.  Displays a custom error message in the event
     * of failure.
     *
     * @param hint      error message that should be displayed for a failed assertion.
     * @param start     the starting point of the line, null for any starting location
     * @param end       the end location of the line, null for any end location
     * @param color     the color of the line, null for any color.
     * @param visible   whether the line is visible on the canvas or not.
     *                  pass null, if either.
     *
     */
    public void assertNoLine(String hint, Location start, Location end, java.awt.Color color, Boolean visible) {
        if (hasLine(start, end, color, visible)) {
            Assert.fail(hint);
        }
    }


}
