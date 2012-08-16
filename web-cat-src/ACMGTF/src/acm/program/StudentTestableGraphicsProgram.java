package acm.program;


import java.awt.*;
import junit.framework.Assert;
import java.util.Iterator;
import acm.graphics.*;
import abbot.finder.*;


/**
 * @author Jason
 *
 */
public class StudentTestableGraphicsProgram extends TestableGraphicsProgram
{
    

    // New Style Methods
    /**
     * assertFilledGArcLocatedAt asserts that there exists a filled GArc at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertFilledGArcLocatedAt(double x, double y)
    {
        String hint = "Canvas should have a filled GRect at (" + x + ", " + y + ")";
        assertHasGObject(hint, GraphicFilter.type(GArc.class).and(GraphicFilter.locatedAt(x, y)).and(GraphicFilter.filled));
    }
    
    /**
     * assertFilledGOvalLocatedAt asserts that there exists a filled GOval at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertFilledGOvalLocatedAt(double x, double y)
    {
        String hint = "Canvas should have a filled GRect at (" + x + ", " + y + ")";
        assertHasGObject(hint, GraphicFilter.type(GOval.class).and(GraphicFilter.locatedAt(x, y)).and(GraphicFilter.filled));
    }
    
    /**
     * assertFilledGRectLocatedAt asserts that there exists a filled Rect at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertFilledGRectLocatedAt(double x, double y)
    {
        String hint = "Canvas should have a filled GRect at (" + x + ", " + y + ")";
        assertHasGObject(hint, GraphicFilter.type(GRect.class).and(GraphicFilter.locatedAt(x, y)).and(GraphicFilter.filled));
    }
    
    /**
     * assertFilledGRoundRectLocatedAt asserts that there exists a filled GRoundRect at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertFilledGRoundRectLocatedAt(double x, double y)
    {
        String hint = "Canvas should have a filled GRect at (" + x + ", " + y + ")";
        assertHasGObject(hint, GraphicFilter.type(GRoundRect.class).and(GraphicFilter.locatedAt(x, y)).and(GraphicFilter.filled));
    }
    
    /**
     * assertG3DRectLocatedAt asserts that there exists a G3DRect at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertG3DRectLocatedAt(double x, double y)
    {
        String hint = "Canvas should have a G3DRect at (" + x + ", " + y + ")";
        assertHasGObject(hint, GraphicFilter.type(G3DRect.class).and(GraphicFilter.locatedAt(x, y)));
    }
    
    /**
     * assertGArcLocatedAt asserts that there exists a GArc at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertGArcLocatedAt(double x, double y)
    {
        String hint = "Canvas should have a GArc at (" + x + ", " + y + ")";
        assertHasGObject(hint, GraphicFilter.type(GArc.class).and(GraphicFilter.locatedAt(x, y)));
    }
    
    /**
     * assertGCompoundLocatedAt asserts that there exists a GCompound at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertGCompoundLocatedAt(double x, double y)
    {
        String hint = "Canvas should have a GCompound at (" + x + ", " + y + ")";
        assertHasGObject(hint, GraphicFilter.type(GCompound.class).and(GraphicFilter.locatedAt(x, y)));
    }
    
    /**
     * assertGImageLocatedAt asserts that there exists a GImage at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertGImageLocatedAt(double x, double y)
    {
        String hint = "Canvas should have a filled GImage at (" + x + ", " + y + ")";
        assertHasGObject(hint, GraphicFilter.type(GImage.class).and(GraphicFilter.locatedAt(x, y)));
    }
    
    /**
     * assertGLabelLocatedAt asserts that there exists a GLabel at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertGLabelLocatedAt(double x, double y)
    {
        String hint = "Canvas should have a GLabel at (" + x + ", " + y + ")";
        assertHasGObject(hint, GraphicFilter.type(GLabel.class).and(GraphicFilter.locatedAt(x, y)));
    }
    
    /**
     * assertGLineLocatedAt asserts that there exists a GLine at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertGLineLocatedAt(double x, double y)
    {
        String hint = "Canvas should have a GLine at (" + x + ", " + y + ")";
        assertHasGObject(hint, GraphicFilter.type(GLine.class).and(GraphicFilter.locatedAt(x, y)));
    }
    
    /**
     * assertGOvalLocatedAt asserts that there exists a GOval at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertGOvalLocatedAt(double x, double y)
    {
        String hint = "Canvas should have a GOval at (" + x + ", " + y + ")";
        assertHasGObject(hint, GraphicFilter.type(GOval.class).and(GraphicFilter.locatedAt(x, y)));
    }
    
    /**
     * assertGPolygonLocatedAt asserts that there exists a GPolygon at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertGPolygonLocatedAt(double x, double y)
    {
        String hint = "Canvas should have a GPolygon at (" + x + ", " + y + ")";
        assertHasGObject(hint, GraphicFilter.type(GPolygon.class).and(GraphicFilter.locatedAt(x, y)));
    }
    
    /**
     * assertGRectLocatedAt asserts that there exists a GRect at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertGRectLocatedAt(double x, double y)
    {
        String hint = "Canvas should have a GRect at (" + x + ", " + y + ")";
        assertHasGObject(hint, GraphicFilter.type(GRect.class).and(GraphicFilter.locatedAt(x, y)));
    }
    
    /**
     * assertGRoundRectLocatedAt asserts that there exists a GRoundRect at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertGRoundRectLocatedAt(double x, double y)
    {
        String hint = "Canvas should have a GRoundRect at (" + x + ", " + y + ")";
        assertHasGObject(hint, GraphicFilter.type(GRoundRect.class).and(GraphicFilter.locatedAt(x, y)));
    }
    
    /**
     * assertGTurtleLocatedAt asserts that there exists a GTurtle at the specified (x, y) position
     *
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    public void assertGTurtleLocatedAt(double x, double y)
    {
        String hint = "Canvas should have a GTurtle at (" + x + ", " + y + ")";
        assertHasGObject(hint, GraphicFilter.type(GTurtle.class).and(GraphicFilter.locatedAt(x, y)));
    }
    
    public void assertHasGObject(GraphicFilter filter)
    {
        String hint = "No objects matching filter found.";
        assertHasGObject(hint, filter);
    }
    
    // Old Style Methods
    /**
     * assertCanvasEmpty asserts there are no visible shape objects on the canvas.
     */
    public void assertCanvasEmpty()
    {
        Iterator i = iterator();
        do
        {
            if(!i.hasNext())
                break;
            GObject d = (GObject) i.next();
            if(d.isVisible())
                Assert.fail("The canvas should not have visible elements.");
        } while(true);
    }
    
    /**
     * assertNotCanvasEmpty asserts there is at least one visible element on the canvas.
     */
    public void assertCanvasNotEmpty()
    {
        for(Iterator i = iterator(); i.hasNext();)
        {
            GObject d = (GObject)i.next();
            if(d.isVisible())
                return;
        }

        Assert.fail("Canvas should have visible elements.");
    }
    
    
     
    
}
