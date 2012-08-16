package objectdraw;

import java.awt.event.*;

// -------------------------------------------------------------------------
/**
 * 
 * This is our listener that delegates mouse events to the component, to
 * simplify handling. This listener is similar to objectdraw.WindowControllerListener,
 * but adds functionality to wait for an event to happen.
 * 
 *  @author  Roy Tan
 *  @version 7/5/2006
 */
public class VTWindowControllerListener
    implements MouseListener, MouseMotionListener
{
    private boolean mousePressLock = false;
    private boolean mouseReleaseLock = false;
    private boolean mouseClickLock = false;
    private boolean mouseMoveLock = false;
    private Location mouseMoveDestination = null;
    
    
    public void lockMouseClick() {
        mouseClickLock = true;
        //System.out.println("locked for mouse click");
    }
    
    public void lockMouseMove(Location dest) {
        mouseMoveLock = true;
        mouseMoveDestination = dest;
        //System.out.println("locked for mouse move");
    }
    
    public void lockMousePress() {
        mousePressLock = true;
        //System.out.println("locked for mouse click");
    }
    
    public void lockMouseRelease() {
        mouseReleaseLock = true;
        //System.out.println("locked for mouse click");
    }
    
    public boolean isLocked() {
        return  (mousePressLock || 
                mouseReleaseLock ||
                mouseClickLock ||
                mouseMoveLock);
    }
    
    public void waitUnlock() {
        int timeout = 100;
        while(  mousePressLock || 
                mouseReleaseLock ||
                mouseClickLock ||
                mouseMoveLock)
        {
            timeout --;
            try {
                //System.out.println("sleeping");
                Thread.sleep(100);
            } catch(Exception e) {
                //System.out.println("Sleep interrupted");   
            }
            if(timeout == 0) {
                /* 
                 System.out.println( "mousePressLock:" + mousePressLock + "\n" +
                                    "mouseReleaseLock:" + mouseReleaseLock + "\n" +
                                    "mouseClickLock:" + mouseClickLock + "\n" +
                                    "mouseMoveLock:" + mouseMoveLock + "\n");
                                    */
            }
        }
    }

    public VTWindowControllerListener(TestableWindowController controller, DrawingCanvas canvas)
    {
        this.controller = controller;
        this.canvas = canvas;
    }

    public void mouseClicked(MouseEvent e)
    {
        try
        {
            canvas.requestFocusInWindow();
        }
        catch(AbstractMethodError err)
        {
            canvas.requestFocus();
        }
        
        //System.out.println("Calling onMouseClick " + e.getPoint());
        controller.onMouseClick(new Location(e.getPoint()));
        
        mouseClickLock = false;
    }

    public void mousePressed(MouseEvent e)
    {
        //System.out.println("Mouse pressed.");
        controller.onMousePress(new Location(e.getPoint()));
        mousePressLock = false;
    }

    public void mouseReleased(MouseEvent e)
    {
        //System.out.println("Mouse released.");
        controller.onMouseRelease(new Location(e.getPoint()));
        mouseReleaseLock = false;
    }

    public void mouseEntered(MouseEvent e)
    {
        controller.onMouseEnter(new Location(e.getPoint()));
    }

    public void mouseExited(MouseEvent e)
    {
        Location l = new Location(e.getPoint());
        //System.out.println("Calling onMouseExit " + l);
        controller.onMouseExit(new Location(e.getPoint()));
        if(l.equals(mouseMoveDestination)) {
            mouseMoveLock = false;
        }
    }

    public void mouseDragged(MouseEvent e)
    {
        Location l = new Location(e.getPoint());
        //System.out.println("Calling onMouseDrag " + l);
        controller.onMouseDrag(l);
        if(l.equals(mouseMoveDestination)) {
            mouseMoveLock = false;
        }
    }

    public void mouseMoved(MouseEvent e)
    {
        Location l = new Location(e.getPoint());
        //System.out.println("Calling onMouseMove " + l);
        controller.onMouseMove(l);
        if(l.equals(mouseMoveDestination)) {
            mouseMoveLock = false;
        }
    }

    public void getWindowFocus() {
        javax.swing.JFrame frame = controller.getFrame();
        int move = 10;
        while(! (frame.isActive() && frame.isFocused()) ) {
            
            //---
            // On Mac OS X, these will make both frame.isActive and frame.isFocused true,
            // but the component will still *not* receive the mouse event.
            //frame.toFront();
            //frame.requestFocus();
            
            frame.setLocation((int) frame.getLocation().getX() + move, (int) frame.getLocation().getY() + move);
            move *= -1;
            
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                //interrupted... now what?   
            }
        }
    }
    
    private TestableWindowController controller;
    private DrawingCanvas canvas;
}
