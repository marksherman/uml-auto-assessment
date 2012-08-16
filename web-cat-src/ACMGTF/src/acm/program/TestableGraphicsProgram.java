package acm.program;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import junit.framework.Assert;

import acm.graphics.GFillable;
import acm.graphics.GLabel;
import acm.graphics.GLine;
import acm.graphics.GObject;
import acm.graphics.GPoint;
import acm.program.Filter;
public class TestableGraphicsProgram extends GraphicsProgram
{
    /**
     * getGObject returns an arbitrary shape that meets the specified parameters.
     * That arbitrary shape is the first one that appears in the DrawableIterator
     * for the canvas.  
     *
     * @param shape     the shape of the object. null for any shape.
     * @param filter    a Filter object that specifies which GObject to return
     *
     * @return an arbitrary shape that fits the parameters passed, or null
     * if no such element exists.
     */
    public GObject getGObject(GraphicFilter filter)
    {
        Iterator iter = iterator();
        while(iter.hasNext())
        {    
            GObject gobj = (GObject)iter.next();
            if(filter.test(gobj))
                return gobj;
        }
        return null;
    }
    
    public <T extends GObject> T getGObject(Class<T> type, GraphicFilter filter)
    {
        return (T)getGObject(filter.and(GraphicFilter.type(type)));
    }
    
    public GObject getExactlyOneGObject(GraphicFilter filter)
    {
        IllegalStateException ise;
        
        GObject[] gobjs = getAllGObjects(filter);
        
        if(gobjs.length == 0)
        {
            ise = new IllegalStateException("No GObjects matching filter found.");
            StackTraceElement[] stack = ise.getStackTrace();
            for(StackTraceElement ste : stack)
            {
                if(ste.getClassName().startsWith("org.junit.") || ste.getClassName().startsWith("junit."))
                {
                    Assert.fail(ise.getMessage());
                }
            }
            throw ise;
        }
        else if(gobjs.length > 1)
        {
            ise = new IllegalStateException("Multiple GObjects matching filter found.");
            StackTraceElement[] stack = ise.getStackTrace();
            for(StackTraceElement ste : stack)
            {
                if(ste.getClassName().startsWith("org.junit.") || ste.getClassName().startsWith("junit."))
                {
                    Assert.fail(ise.getMessage());
                }
            }
            throw ise;
        }
        return gobjs[0];               
    }
    
    public <T extends GObject> T getExactlyOneGObject(Class<T> type, GraphicFilter filter)
    {
        return (T)getExactlyOneGObject(filter.and(GraphicFilter.type(type)));
    }
    
    /**
     * getAllGObjects returns all GObjects on the canvas that meets the specified parameters.
     *
     * @param shape     the shape of the object. null for any shape.
     * @param filter    a Filter object that specifies which GObjects to return
     *
     * @return an array of all of the GObjects that match the filter criteria
     */
    public GObject[] getAllGObjects(GraphicFilter filter)
    {
        ArrayList<GObject> matches = new ArrayList<GObject>();
        Iterator iter = iterator();
        while(iter.hasNext())
        {
            GObject gobj = (GObject)iter.next();
            if(filter.test(gobj))
                matches.add(gobj);
        }
        GObject[] gobjs = new GObject[matches.size()];
        int i = 0;
        for(GObject gobj : matches)
        {
            gobjs[i] = gobj;
            i++;
        }
        return gobjs;
    }
    
    @SuppressWarnings("unchecked")
    public <T extends GObject> T[] getAllGObjects(Class<T> type, GraphicFilter filter)
    {
        GObject[] gobjs = getAllGObjects(filter.and(GraphicFilter.type(type)));

        T[] tobjs = (T[]) Array.newInstance(type, gobjs.length);
        for (int i = 0; i < gobjs.length; i++)
        {
            tobjs[i] = (T) gobjs[i];
        }

        return tobjs;
    }
    
    /**
     * hasGObject determines existence of a graphic object that matches the
     * specified filter
     *
     * @param shape     the shape of the object. null for any shape.
     * @param filter    a Filter object that specifies which GObjects to search for
     *
     * @return true if it has the 2D object specified, false if not.
     */
    public boolean hasGObject(GraphicFilter filter)
    {
        return getGObject(filter) != null;
    }
    
    /**
     * assert version of hasGObject that has a specific hint to the user.
     *
     * @param hint      error message that should be displayed for a failed assertion
     * @param filters   array of filters to use describing the object to be found
     */
    public void assertHasGObject(String hint, GraphicFilter filter)
    {
        if(!hasGObject(filter))
            Assert.fail(hint);
    }
    
    /**
     * inverse of assertHasGObject, this method will fail if an object specified
     * by the filters is found, succeed otherwise.  Prints a custom message in
     * the event of failure.
     *
     * @param hint      error message that should be displayed for a failed assertion
     * @param filters   array of filters to use describing the object to be found
     *
     */
    public void assertNoGObject(String hint, GraphicFilter filter)
    {
        if(hasGObject(filter))
            Assert.fail(hint);
    }

    public final void mouseClicked(double x, double y)
    {
        eventListener.mouseClicked(new MouseEvent(this.getContentPane(),
                                         MouseEvent.BUTTON1,
                                         System.currentTimeMillis(),
                                         MouseEvent.BUTTON1_DOWN_MASK,
                                         (int) x,
                                         (int)y,
                                         1,
                                         false));
    }

    public final void mousePressed(double x, double y)
    {
        
        eventListener.mousePressed(new MouseEvent(this.getContentPane(),
                 MouseEvent.BUTTON1,
                 System.currentTimeMillis(),
                 MouseEvent.BUTTON1_DOWN_MASK,
                 (int) x,
                 (int)y,
                 1,
                 false));
    }
   
    public final void mouseDragged(double x, double y)
    {
        eventListener.mouseDragged(new MouseEvent(this.getContentPane(),
                 MouseEvent.BUTTON1,
                 System.currentTimeMillis(),
                 MouseEvent.BUTTON1_DOWN_MASK,
                 (int) x,
                 (int)y,
                 1,
                 false));
    }

    public final void mouseReleased(double x, double y)
    {
        eventListener.mouseReleased(new MouseEvent(this.getContentPane(),
                 MouseEvent.BUTTON1,
                 System.currentTimeMillis(),
                 MouseEvent.BUTTON1_DOWN_MASK,
                 (int) x,
                 (int)y,
                 1,
                 false));
    }

    public final void mouseMoved(double x, double y)
    {
        eventListener.mouseMoved(new MouseEvent(this.getContentPane(),
                 MouseEvent.BUTTON1,
                 System.currentTimeMillis(),
                 MouseEvent.BUTTON1_DOWN_MASK,
                 (int) x,
                 (int)y,
                 1,
                 false));
    }
    
    public final void mouseEntered()
    {
        eventListener.mouseEntered(new MouseEvent(this.getContentPane(),
                MouseEvent.MOUSE_ENTERED,
                System.currentTimeMillis(),
                MouseEvent.BUTTON1,
                1,
                1,
                1,
                false));
    }
    public final void mouseExited()
    {
        eventListener.mouseExited(new MouseEvent(this.getContentPane(),
                MouseEvent.MOUSE_EXITED,
                System.currentTimeMillis(),
                MouseEvent.BUTTON1,
                1,
                1,
                1,
                false));
    }
    
    public final void mouseEntered(double x, double y)
    {
        eventListener.mouseEntered(new MouseEvent(this.getContentPane(),
                MouseEvent.MOUSE_ENTERED,
                System.currentTimeMillis(),
                MouseEvent.BUTTON1,
                (int) x,
                (int) y,
                1,
                false));
    }
    public final void mouseExited(double x, double y)
    {
        eventListener.mouseExited(new MouseEvent(this.getContentPane(),
                MouseEvent.MOUSE_EXITED,
                System.currentTimeMillis(),
                MouseEvent.BUTTON1,
                (int) x,
                (int) y,
                1,
                false));
    }
    
    public static abstract class GraphicFilter extends Filter
    {
        public final GraphicFilter and(final GraphicFilter otherFilter) 
        {
            final GraphicFilter self = this;
            GraphicFilter f =  new GraphicFilter(){
                public boolean test(GObject gobj)  
                {
                   return self.test(gobj) && otherFilter.test(gobj);    
                }
            };
            f.description = "(" + this.description + " AND " + otherFilter.description + ")";
            return f;
        }
        
        public final GraphicFilter or(final GraphicFilter otherFilter)
        {
            final GraphicFilter self = this;
            GraphicFilter f = new GraphicFilter(){
                public boolean test(GObject gobj)
                {
                    return self.test(gobj) || otherFilter.test(gobj);
                }
            };
            f.description = "(" + this.description + " OR " + otherFilter.description + ")";
            return f;
        }
        
        public static final GraphicFilter not(final GraphicFilter otherFilter)
        {
            GraphicFilter f = new GraphicFilter(){
                public boolean test(GObject gobj)
                {
                    return !otherFilter.test(gobj);
                }
            };
            f.description = " NOT " + otherFilter.description;
            return f;
        }
        
        public static final GraphicFilter contains(final GPoint point)
        {
            GraphicFilter f = new GraphicFilter()
            {
                public boolean test(GObject gobj)
                {
                    return (gobj.contains(point));
                }
            };
            f.description = "contains ("+ point.getX() + ", " + point.getY() + ")";
            return f;
        }
        
        public static final GraphicFilter contains(double x, double y)
        {
            return contains(new GPoint(x, y));
        }
        
        public static final GraphicFilter near(final GPoint point)
        {
            GraphicFilter f = new GraphicFilter(){
              public boolean test(GObject gobj)
              {
                  double distance = Math.sqrt(Math.pow(point.getX() - gobj.getX(), 2) + 
                      Math.pow(point.getX() - gobj.getX(), 2));
                  return distance < 50;
              }
            };
            f.description = "near (" + point.getX() + ", " + point.getY() + ")";
            return f;
        }
        
        public static final GraphicFilter near(double x, double y)
        {
            return near(new GPoint(x,y));
        }
        
        public static final GraphicFilter near(final GPoint point, final double distance)
        {
            GraphicFilter f = new GraphicFilter(){
                public boolean test(GObject gobj)
                {
                    double d = Math.sqrt(Math.pow(point.getX() - gobj.getX(), 2) + 
                        Math.pow(point.getX() - gobj.getX(), 2));
                    return d < distance;
                }
              };
              f.description = "within " + distance + " pixels of (" + point.getX() + ", " + point.getY() + ")";
              return f;
        }
        
        public static final GraphicFilter near(double x, double y, double distance)
        {
            return near(new GPoint(x, y), distance);
        }
        
        public static final GraphicFilter lineStartingAt(final GPoint point)
        {
            GraphicFilter f =  new GraphicFilter(){
                public boolean test(GObject gobj)
                {
                    if(!(gobj instanceof GLine))
                        return false;
                    return ((GLine)gobj).getStartPoint().equals(point);
                }
            };
            f.description = "line starting at (" + point.getX() + ", " + point.getY() + ")";
            return f;
        }
        
        public static final GraphicFilter lineStartingAt(double x, double y)
        {
            return lineStartingAt(new GPoint(x, y));
        }
        
        public static final GraphicFilter lineEndingAt(final GPoint point)
        {
            GraphicFilter f = new GraphicFilter(){
                public boolean test(GObject gobj)
                {
                    if(!(gobj instanceof GLine))
                        return false;
                    return ((GLine)gobj).getEndPoint().equals(point);
                }
            };
            f.description = "line ending at (" + point.getX() + ", " + point.getY() + ")";
            return f;
        }
        
        public static final GraphicFilter lineEndingAt(double x, double y)
        {
            return lineEndingAt(new GPoint(x, y));
        }
        
        public static final GraphicFilter locatedAt(final GPoint point)
        {
            GraphicFilter f =  new GraphicFilter(){
                public boolean test(GObject gobj)
                {
                    if(gobj.getLocation().equals(point))
                        return true;
                    return false;
                }
            };
            f.description = "located at (" + point.getX() + ", " + point.getY() + ")";
            return f;
        }
        
        public static final GraphicFilter locatedAt(double x, double y)
        {
            return locatedAt(new GPoint(x, y));  
        }
        
        public static final GraphicFilter withText(final String text)
        {
            GraphicFilter f = new GraphicFilter(){
                public boolean test(GObject gobj)
                {
                    return gobj instanceof GLabel ? ((GLabel)gobj).getLabel().equals(text) : false;
                }
            };
            f.description = "with text: \"" + text + "\"";
            return f;
        }
        
        public static final GraphicFilter width(final int width)
        {
            GraphicFilter f = new GraphicFilter(){
                public boolean test( GObject gobj )
                {
                    if(gobj.getWidth() == width)
                        return true;
                    return false;
                } 
            };
            f.description = "width: " + width;
            return f;
        }
        
        public static final GraphicFilter height(final int height)
        {
            GraphicFilter f = new GraphicFilter(){
                public boolean test( GObject gobj )
                {
                    if(gobj.getHeight() == height)
                        return true;
                    return false;
                }    
            };
            f.description = "height: " + height;
            return f;
        }
        
        public static final GraphicFilter color(final Color color)
        {
            GraphicFilter f = new GraphicFilter(){
                public boolean test( GObject gobj )
                {
                    if(gobj.getColor().equals(color))
                        return true;
                    return false;
                }
            };
            f.description = "color: " + color;
            return f;
        }
        
        public static final GraphicFilter type(final Class c)
        {
            GraphicFilter f = new GraphicFilter(){
                public boolean test(GObject gobj)
                {
                    return c.isAssignableFrom(gobj.getClass());
                }
            };
            f.description = "class: " + c.getSimpleName();
            return f;
        }
        
        public static final GraphicFilter filled = new GraphicFilter() {
            { this.setDescription("filled"); }
            public boolean test(GObject gobj)
            {
               return GFillable.class.isAssignableFrom(gobj.getClass()) && ((GFillable)gobj).isFilled();
            }
        };
        
        public static final GraphicFilter not_filled = new GraphicFilter() {
            { this.setDescription("not filled"); }
            public boolean test(GObject gobj)
            {
                return (GFillable.class.isAssignableFrom(gobj.getClass()) && !((GFillable)gobj).isFilled());
            }
        };

        public static final GraphicFilter visible = new GraphicFilter() {
            { this.setDescription("visible"); }
            public boolean test(GObject gobj)
            {
                return gobj.isVisible();
            }
        };
        
        public static final GraphicFilter not_visible = new GraphicFilter() {
            { this.setDescription("not visible"); }
            public boolean test(GObject gobj)
            {
                return !gobj.isVisible();
            }
        };
        
        public static final GraphicFilter any = new GraphicFilter() {
            { this.setDescription("any"); }
            
            public boolean test(GObject gobj)
            {
                return true;
            }
        };
                
        
        
        public abstract boolean test(GObject gobj);
        
    }
}

    
