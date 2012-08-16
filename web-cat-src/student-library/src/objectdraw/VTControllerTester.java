package objectdraw;

import junit.extensions.abbot.*;
import abbot.tester.*;
import java.awt.event.MouseEvent;

// -------------------------------------------------------------------------
/**
 *  
 * 
 *  @author  Roy Patrick Tan
 *  @version June 9, 2006
 */
public class VTControllerTester
{
    //~ Instance/static variables .............................................
    private ComponentTester tester;
    private TestableWindowController component;
    private javax.swing.JFrame frame = null;
    private VTWindowControllerListener listener = null;
    private DrawingCanvas canvas = null;
    private FrameTester frameTester = null;
    
    int x;
    int y;
    
    //~ Constructor ...........................................................
    
    /**
     * Create a UI testing robot, given a TestableWindowController.
     * @param controller    the component to be tested.
     */
    public VTControllerTester(TestableWindowController controller) {
        System.setProperty("abbot.robot.verify","false");
        tester = new ComponentTester();
        frameTester = new FrameTester();
        
        this.component = controller;
        component.waitForInit();
        
        while(frame == null)
            frame = component.getFrame();
        while(canvas == null)
            canvas = component.getCanvas();
        while(listener == null)
            listener = component.getListener();
    }

    //~ Methods ...............................................................
    
    /**
     * Simulates a click on loc.
     * @param loc the Location in the canvas where the mouse is clicked.
     */
    public void actionClick(Location loc) {
        listener.lockMouseClick();
        listener.getWindowFocus();
        tester.actionClick(component, (int) loc.getX(), (int) loc.getY() );
        listener.waitUnlock();
    }

    /*
    public void actionMousePress(Location loc) {
        System.out.println("new action click");
        listener.lockMouseClick();
        MouseEvent e = new MouseEvent(
                component, 
                MouseEvent.MOUSE_PRESSED,
                (new java.util.Date()).getTime(), 
                MouseEvent.BUTTON1_MASK, 
                (int) loc.getX(), 
                (int) loc.getY(), 
                1, 
                false);
                
        listener.mousePressed(e);
        listener.waitUnlock();
    }
    */
    /**
     * Simulates the mouse moving towards loc.
     * To get an onMouseDrag event, call actionMouseMove after
     * actionMousePress.
     * @param loc the location the move is moving to.
     */
    public void actionMouseMove(Location loc) {
        listener.lockMouseMove(loc);
        //System.out.println(loc + ":" + loc.toPoint());
        //component.toFront();
        //frameTester.actionClick(frame, 1,1);
        listener.getWindowFocus();
        int x = 1;
        int y = 1;
        do {
            //tester.waitForIdle();
            tester.actionMouseMove(component, new ComponentLocation( loc.toPoint()) );
            tester.delay(100);
            x = x * -1;
            y = y * -1;
            loc.translate(x,y);
        } while( listener.isLocked());
        
        listener.waitUnlock();
    }
    
    /**
     * Simulates a mouse press on the app.
     * @param loc the location where the mouse is pressed.
     */
    public void actionMousePress(Location loc) {
        //System.out.println("action mouse press called...");
        //System.out.println("locking...");
        listener.lockMousePress();
        listener.getWindowFocus();
        
        //System.out.println("pressing mouse button...");
        tester.actionMousePress(
            component, new ComponentLocation( loc.toPoint() ));
        //System.out.println("waiting for unlock...");
        listener.waitUnlock();
        System.out.println("done.");
    }
    
    /**
     * Simulates the mouse being released.
     * This assumes that the mouse is released wherever it is located
     * at the moment actionMouseRelease is called.
     */
    public void actionMouseRelease() {
        listener.lockMouseRelease();
        listener.getWindowFocus();
        tester.actionMouseRelease();
        listener.waitUnlock();
    }
}
