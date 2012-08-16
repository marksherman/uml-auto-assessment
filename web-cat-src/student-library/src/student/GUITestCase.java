/*==========================================================================*\
 |  $Id: GUITestCase.java,v 1.15 2011/06/09 15:29:58 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2007-2010 Virginia Tech
 |
 |  This file is part of the Student-Library.
 |
 |  The Student-Library is free software; you can redistribute it and/or
 |  modify it under the terms of the GNU Lesser General Public License as
 |  published by the Free Software Foundation; either version 3 of the
 |  License, or (at your option) any later version.
 |
 |  The Student-Library is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU Lesser General Public License for more details.
 |
 |  You should have received a copy of the GNU Lesser General Public License
 |  along with the Student-Library; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package student;

import static student.testingsupport.ReflectionSupport.invoke;
import abbot.finder.BasicFinder;
import abbot.finder.ComponentFinder;
import abbot.finder.ComponentNotFoundException;
import abbot.finder.Hierarchy;
import abbot.finder.Matcher;
import abbot.finder.MultiMatcher;
import abbot.finder.MultipleComponentsFoundException;
import abbot.finder.TestHierarchy;
import abbot.tester.ComponentLocation;
import abbot.tester.ComponentTester;
import abbot.tester.JComboBoxTester;
import abbot.tester.JListTester;
import abbot.tester.JMenuItemTester;
import abbot.tester.JTextComponentTester;
import abbot.tester.Robot;
import abbot.tester.WindowTracker;
import abbot.util.AWTFixtureHelper;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import student.testingsupport.GUIFilter;

//-------------------------------------------------------------------------
/**
 *  This class provides enhancements to {@link student.TestCase} to support
 *  testing of custom Swing components, panels, and main programs
 *  with graphical user interfaces (GUIs).  GUI testing support is based
 *  on abbot's testing infrastructure, and works equally well for
 *  Swing and/or AWT components.
 *
 *  @author  Stephen Edwards
 *  @author Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.15 $, $Date: 2011/06/09 15:29:58 $
 */
public class GUITestCase
    extends TestCase
{
    //~ Constants .............................................................

    /**
     * This field re-exports the <code>where</code> operator from
     * {@link student.testingsupport.GUIFilter} so that it is available
     * in test methods without requiring a static import.
     */
    public final GUIFilter.Operator where = GUIFilter.ClientImports.where;


    //~ Instance/static variables .............................................

    /**
     * Any member data derived from these classes will be automatically set
     * to <code>null</code> after the test has run.  This enables GC of said
     * classes without GC of the test itself (the default JUnit runners never
     * release their references to the tests) or requiring explicit
     * <code>null</code>-setting in the {@link TestCase#tearDown()} method.
     */
    protected static final Class<?>[] DISPOSE_CLASSES = {
        Component.class,
        ComponentTester.class
    };

    private AWTFixtureHelper fixtureHelper;
    private Throwable edtException;
    private long edtExceptionTime;
    private ComponentFinder finder;
    private Hierarchy hierarchy;


    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new GUITestCase object.
     */
    public GUITestCase()
    {
        super();
    }


    // ----------------------------------------------------------
    /**
     * Creates a new GUITestCase object.
     * @param name The name of this test case.
     */
    public GUITestCase(String name)
    {
        super(name);
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Displays a specific component that you wish to test, such as a panel,
     * a custom text field, or some other component, wrapping it in a
     * {@link Frame}.  The frame's size will be its preferred size.  This
     * method will return when the enclosing {@link Frame} is showing and
     * ready for input.
     * @param component The component to display.
     * @return The newly created, visible frame containing the component.
     */
    public Frame showInFrame(Component component)
    {
        return showInFrame(component, null);
    }


    // ----------------------------------------------------------
    /**
     * Displays a specific component that you wish to test, such as a panel,
     * a custom text field, or some other component, wrapping it in a
     * {@link Frame} with the size you specify.  This method will return when
     * the enclosing {@link Frame} is showing and ready for input.
     * @param component The component to display.
     * @param size The desired size of the enclosing frame, or
     *        <code>null</code> to use the component's preferred size.
     * @return The newly created, visible frame containing the component
     */
    public Frame showInFrame(Component component, Dimension size)
    {
        return fixtureHelper.showFrame(component, size, getName());
    }


    // ----------------------------------------------------------
    /**
     * Safely display a window with proper EDT synchronization.   This method
     * blocks until the {@link Window} is showing and ready for input.
     * @param window The window to show.
     */
    public void showWindow(Window window)
    {
        showWindow(window, null);
    }


    // ----------------------------------------------------------
    /**
     * Safely display a window with proper EDT synchronization.   This method
     * blocks until the {@link Window} is showing and ready for input.
     * @param window The window to show.
     * @param size The desired size of the enclosing frame, or
     *        <code>null</code> to use the component's preferred size.
     */
    public void showWindow(Window window, Dimension size)
    {
        fixtureHelper.showWindow(window, size, true);
    }


    // ----------------------------------------------------------
    /**
     * Set any key modifiers for future events.  This method's effects will be
     * automatically undone at the end of the test.
     * @param modifiers A mask indicating which modifier keys to use.
     * @param pressed Whether the modifiers should be in the pressed state.
     */
    public void setModifiers(int modifiers, boolean pressed)
    {
        fixtureHelper.setModifiers(modifiers, pressed);
    }


    // ----------------------------------------------------------
    /**
     * Hook into the custom test execution machinery provided by MixRunner
     * to ensure proper test harness setup and tear down that won't
     * likely be accidentally overridden by a derived class.
     * <p>
     * If any exceptions are thrown on the event dispatch thread, they count
     * as errors.  They will not, however supersede any failures/errors
     * thrown by the test itself unless thrown prior to the main test
     * failure.
     *
     * @param statement The (JUnit4-style) test method to be executed.
     *
     * @throws Throwable If any exception occurs in the test case or on the
     *         event dispatch thread.
     */
    protected void runTestMethod(org.junit.runners.model.Statement statement)
        throws Throwable
    {
        if (Boolean.getBoolean("abbot.skip_ui_tests"))
        {
            return;
        }

        Throwable exception = null;
        long exceptionTime = -1;
        try
        {
            try
            {
                fixtureSetUp();
                statement.evaluate();
            }
            catch (Throwable e)
            {
                exception = e;
            }
            finally
            {
                try
                {
                    fixtureTearDown();
                }
                catch (Throwable tearingDown)
                {
                    if (exception == null)
                    {
                        exception = tearingDown;
                    }
                }
            }
            if (exception != null)
            {
                throw exception;
            }
        }
        catch (Throwable e)
        {
            exceptionTime = System.currentTimeMillis();
            exception = e;
        }
        finally
        {
            // Cf. StepRunner.runStep()
            // Any EDT exception which occurred *prior* to when the
            // exception on the main thread was thrown should be used
            // instead.
            if (edtException != null
                && (exception == null
                    || edtExceptionTime < exceptionTime))
            {
                exception = new EventDispatchException(edtException);
            }
        }
        if (exception != null) {
            throw exception;
        }
    }


    public void runBare()
        throws Throwable
    {
        if (Boolean.getBoolean("abbot.skip_ui_tests"))
        {
            return;
        }

        Throwable exception = null;
        long exceptionTime = -1;
        try
        {
            try
            {
                fixtureSetUp();
                super.runBare();
            }
            catch (Throwable e)
            {
                exception = e;
            }
            finally
            {
                try
                {
                    fixtureTearDown();
                }
                catch (Throwable tearingDown)
                {
                    if (exception == null)
                    {
                        exception = tearingDown;
                    }
                }
            }
            if (exception != null)
            {
                throw exception;
            }
        }
        catch (Throwable e)
        {
            exceptionTime = System.currentTimeMillis();
            exception = e;
        }
        finally
        {
            // Cf. StepRunner.runStep()
            // Any EDT exception which occurred *prior* to when the
            // exception on the main thread was thrown should be used
            // instead.
            if (edtException != null
                && (exception == null
                    || edtExceptionTime < exceptionTime))
            {
                exception = new EventDispatchException(edtException);
            }
        }
        if (exception != null) {
            throw exception;
        }
    }


    // ----------------------------------------------------------
    /**
     * The "not" operator for negating an existing filter, when the not
     * operation is at the very beginning of the expression, re-exported
     * from {@link student.testingsupport.GUIFilter} so that it is
     * available in test methods without requiring a static import.  This
     * method is designed to be used in expressions like
     * <code>not(where.enabledIs(true).or.hasFocusIs(true))</code>.
     *
     * @param otherFilter The filter to negate
     * @return A new filter that represents a combination of the left
     *         filter with "NOT otherFilter".
     */
    public static GUIFilter not(final GUIFilter otherFilter)
    {
        return GUIFilter.ClientImports.not(otherFilter);
    }


    // ----------------------------------------------------------
    /**
     * Look up a component in the GUI being tested by specifying its class.
     * This method expects the given class to identify a unique component,
     * meaning that there should only be one instance of the given class
     * in the entire GUI.  This can be useful if, for example, you are
     * trying to retrieve a custom panel object created from a class you
     * only instantiate once.
     * <p>
     * If no matching component exists, the test case will fail with an
     * appropriate message.  If more than one matching component exists,
     * the test case will fail with an appropriate message.
     * test case failure results).
     * @param <T>  This method is a template method, and the type T used for
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the component you wish to retrieve, and
     *             also the way you specify the return type of this method.
     * @return The single component of the desired type that was found
     *         (otherwise, a test case failure results).
     * @see #getFirstComponentMatching(Class)
     * @see #getAllComponentsMatching(Class)
     */
    public <T extends Component> T getComponent(Class<T> type)
    {
        @SuppressWarnings("unchecked")
        T result = (T)getComponent(where.typeIs(type));
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Look up a component in the GUI being tested by specifying its class
     * and its name (as returned by the component's
     * {@link Component#getName() getName()} method).
     * This method expects the given class and name together to identify a
     * unique component, meaning that there should only be one instance of
     * the given class with the given name in the entire GUI.  Normally,
     * that will always be the case, since names are used as unique
     * identifiers for testing purposes.
     * <p>
     * If no matching component exists, the test case will fail with an
     * appropriate message.  If more than one matching component exists,
     * the test case will fail with an appropriate message.
     * test case failure results).
     * @param <T>  This method is a template method, and the type T used for
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the component you wish to retrieve, and
     *             also the way you specify the return type of this method.
     * @param name The name of the desired component
     * @return The single component matching the criteria specified
     *         (otherwise, a test case failure results).
     */
    public <T extends Component> T getComponent(Class<T> type, String name)
    {
        @SuppressWarnings("unchecked")
        GUIFilter nameFilter = where.typeIs(type).and.nameIs(name);
        T result = null;
        List<Component> results = getAllComponentsMatching(nameFilter);
        if(results.size() == 0)
        {
            GUIFilter textFilter = where.typeIs(type).and.textIs(name);
            results = getAllComponentsMatching(textFilter);
        }
        if(results.size() == 1)
        {
            result = (T)results.get(0);
        }
        else if(results.size() > 1)
        {
            fail("Found " + results.size() + " components matching: "
                + nameFilter);
        }
        else
            fail("Cannot find component matching: " + nameFilter);

        return result;
    }


    // ----------------------------------------------------------
    /**
     * Look up a component in the GUI being tested by specifying its class
     * and a {@link GUIFilter}.
     * This method expects exactly one component to match your criteria.
     * If no matching component exists, the test case will fail with an
     * appropriate message.  If more than one matching component exists,
     * the test case will fail with an appropriate message.
     * test case failure results).
     * @param <T>  This method is a template method, and the type T used for
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the component you wish to retrieve, and
     *             also the way you specify the return type of this method.
     * @param filter The search criteria.
     * @return The single component matching the criteria specified
     *         (otherwise, a test case failure results).
     * @see #getFirstComponentMatching(Class,GUIFilter)
     * @see #getAllComponentsMatching(Class,GUIFilter)
     */
    public <T extends Component> T getComponent(Class<T> type, GUIFilter filter)
    {
        @SuppressWarnings("unchecked")
        T result = (T)getComponent(where.typeIs(type).and(filter));
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Look up a component in the GUI being tested, using a filter to
     * specify which component you want.  This method is more general
     * than {@link #getComponent(Class,GUIFilter)}, since no class needs to be
     * specified, but that also means the return type is less specific
     * (it is always <code>Component</code>).
     * This method expects the given filter
     * to identify a unique component.  If no matching component exists,
     * the test case will fail with an appropriate message.  If more than
     * one matching component exists, the test case will fail with an
     * appropriate message.
     * @param filter The search criteria.
     * @return The single component matching the provided filter (otherwise, a
     *         test case failure results).
     * @see #getFirstComponentMatching(GUIFilter)
     * @see #getAllComponentsMatching(GUIFilter)
     */
    public Component getComponent(GUIFilter filter)
    {
        Component result = null;
        try
        {
            result = getFinder().find(filter2matcher(filter));
        }
        catch (ComponentNotFoundException e)
        {
            fail("Cannot find component matching: " + filter);
        }
        catch (MultipleComponentsFoundException e)
        {
            Component[] comps = e.getComponents();
            int visibleCount = 0;
            for (Component c: comps)
            {
                if(c.isVisible())
                {
                    visibleCount++;
                    result = c;
                }

            }
            if (visibleCount != 1)
            {
                fail("Found " + e.getComponents().length + " components matching: "
                    + filter);
            }
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Look up a component in the GUI being tested by specifying its class.
     * This method expects the given class to identify at least one such
     * component.  If no matching component exists, the test case will fail
     * with an appropriate message.  If more than one matching component
     * exists, the first one found will be returned (although client code
     * should not expect a specific search order).
     * @param <T>  This method is a template method, and the type T used for
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the component you wish to retrieve, and
     *             also the way you specify the return type of this method.
     * @return The first component of the desired type that was found
     *         (a test case failure results if there are none).
     * @see #getComponent(Class)
     * @see #getAllComponentsMatching(Class)
     */
    public <T extends Component> T getFirstComponentMatching(Class<T> type)
    {
        @SuppressWarnings("unchecked")
        T result = (T)getFirstComponentMatching(where.typeIs(type));
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Look up a component in the GUI being tested by specifying its class
     * and a {@link GUIFilter}.
     * This method expects the given criteria to identify at least one such
     * component.  If no matching component exists, the test case will fail
     * with an appropriate message.  If more than one matching component
     * exists, the first one found will be returned (although client code
     * should not expect a specific search order).
     * @param <T>  This method is a template method, and the type T used for
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the component you wish to retrieve, and
     *             also the way you specify the return type of this method.
     * @param filter The search criteria.
     * @return The first component that was found matching the criteria
     *         specified (a test case failure results if there are none).
     * @see #getComponent(Class,GUIFilter)
     * @see #getAllComponentsMatching(Class,GUIFilter)
     */
    public <T extends Component> T getFirstComponentMatching(
        Class<T> type, GUIFilter filter)
    {
        @SuppressWarnings("unchecked")
        T result =
            (T)getFirstComponentMatching(where.typeIs(type).and(filter));
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Look up a component in the GUI being tested by specifying
     * a {@link GUIFilter}.  This method is more general
     * than {@link #getFirstComponentMatching(Class,GUIFilter)}, since no
     * class needs to be specified, but that also means the return type
     * is less specific (it is always <code>Component</code>).
     * This method expects the given criteria to identify at least one such
     * component.  If no matching component exists, the test case will fail
     * with an appropriate message.  If more than one matching component
     * exists, the first one found will be returned (although client code
     * should not expect a specific search order).
     * @param filter The search criteria.
     * @return The first component that was found matching the criteria
     *         specified (a test case failure results if there are none).
     * @see #getComponent(GUIFilter)
     * @see #getAllComponentsMatching(GUIFilter)
     */
    public Component getFirstComponentMatching(GUIFilter filter)
    {
        Component result = null;
        try
        {
            result = getFinder().find(filter2matcher(filter));
        }
        catch (ComponentNotFoundException e)
        {
            fail("Cannot find component matching: " + filter);
        }
        catch (MultipleComponentsFoundException e)
        {
            result = e.getComponents()[0];
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Look up all components in the GUI being tested by specifying their
     * class.  All matching objects are returned in a list.
     * @param <T>  This method is a template method, and the type T used as
     *             the <code>List</code> element type in
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the components you wish to retrieve,
     *             and also the way you specify the type of elements in
     *             the list returned by this method.
     * @return A list of all components of the desired type that were found.
     *         This will be an empty list (not null) if no matching components
     *         are found.
     * @see #getComponent(Class)
     * @see #getFirstComponentMatching(Class)
     */
    public <T extends Component> List<T> getAllComponentsMatching(
        Class<T> type)
    {
        @SuppressWarnings("unchecked")
        List<T> result = (List<T>)getAllComponentsMatching(where.typeIs(type));
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Look up all components in the GUI being tested by specifying their
     * class and a {@link GUIFilter}.  All matching objects are returned in
     * a list.
     * @param <T>  This method is a template method, and the type T used as
     *             the <code>List</code> element type in
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the components you wish to retrieve,
     *             and also the way you specify the type of elements in
     *             the list returned by this method.
     * @param filter The search criteria.
     * @return A list of all components found matching the criteria specified.
     *         This will be an empty list (not null) if no matching components
     *         are found.
     * @see #getComponent(Class,GUIFilter)
     * @see #getAllComponentsMatching(Class,GUIFilter)
     */
    public <T extends Component> List<T> getAllComponentsMatching(
        Class<T> type, GUIFilter filter)
    {
        @SuppressWarnings("unchecked")
        List<T> result = (List<T>)getAllComponentsMatching(
            where.typeIs(type).and(filter));
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Look up all components in the GUI being tested by specifying
     * a {@link GUIFilter}.
     * All matching objects are returned in a list.
     * This method is more general than
     * {@link #getAllComponentsMatching(Class,GUIFilter)}, since no
     * class needs to be specified, but that also means the return type
     * is less specific (it is always <code>List&lt;Component&gt;</code>).
     * @param filter The search criteria.
     * @return A list of all components found matching the criteria specified.
     *         This will be an empty list (not null) if no matching components
     *         are found.
     * @see #getComponent(GUIFilter)
     * @see #getAllComponentsMatching(GUIFilter)
     */
    public List<Component> getAllComponentsMatching(GUIFilter filter)
    {
        List<Component> result = null;
        try
        {
            Component single = getFinder().find(filter2matcher(filter));
            result = new ArrayList<Component>(1);
            result.add(single);
        }
        catch (ComponentNotFoundException e)
        {
            result = new ArrayList<Component>();
        }
        catch (MultipleComponentsFoundException e)
        {
            result = Arrays.asList(e.getComponents());
        }
        return result;
    }


    //~ Basic Component Action Methods ........................................

    // ----------------------------------------------------------
    /**
     * Left-click on the center of the component (mouse button 1 press and
     * release).
     * @param component The component to click on.
     */
    public void click(Component component)
    {
        if(switchFocus)
            focus(component);
        ComponentTester.getTester(component).actionClick(component);
    }


    // ----------------------------------------------------------
    /**
     * Left-click at the given location on a component (mouse button 1
     * press and release).
     * Coordinates are specified relative to the component being clicked.
     * @param component The component to click on.
     * @param x    The x-coordinate of the click location.
     * @param y    The y-coordinate of the click location.
     */
    public void click(Component component, int x, int y)
    {
        if(switchFocus)
            focus(component);
        ComponentTester.getTester(component).actionClick(component, x, y);
    }


    // ----------------------------------------------------------
    /**
     * Left-double-click on the center of the component (mouse button 1
     * press and release twice).
     * @param component The component to click on.
     */
    public void doubleClick(Component component)
    {
        if(switchFocus)
            focus(component);
        ComponentTester.getTester(component).actionClick(
            component, new ComponentLocation(), InputEvent.BUTTON1_MASK, 2);
    }


    // ----------------------------------------------------------
    /**
     * Left-double-click at the given location on a component (mouse button 1
     * press and release twice).
     * Coordinates are specified relative to the component being clicked.
     * @param component The component to click on.
     * @param x    The x-coordinate of the click location.
     * @param y    The y-coordinate of the click location.
     */
    public void doubleClick(Component component, int x, int y)
    {
        if(switchFocus)
            focus(component);
        click(component, x, y, InputEvent.BUTTON1_MASK, 2);
    }


    // ----------------------------------------------------------
    /**
     * Right-click on the center of the component (mouse button 3 press
     * and release).
     * @param component The component to click on.
     */
    public void rightClick(Component component)
    {
        if(switchFocus)
            focus(component);
        ComponentTester.getTester(component).actionClick(
            component, new ComponentLocation(), InputEvent.BUTTON3_MASK);
    }


    // ----------------------------------------------------------
    /**
     * Right-click at the given location on a component (mouse button 3
     * press and release).
     * Coordinates are specified relative to the component being clicked.
     * @param component The component to click on.
     * @param x    The x-coordinate of the click location.
     * @param y    The y-coordinate of the click location.
     */
    public void rightClick(Component component, int x, int y)
    {
        if(switchFocus)
            focus(component);
        click(component, x, y, InputEvent.BUTTON3_MASK, 1);
    }


    // ----------------------------------------------------------
    /**
     * Click on a component, specifying the location, specific mouse buttons,
     * and click count.
     * Coordinates are specified relative to the component being clicked.
     * @param component The component to click on.
     * @param x    The x-coordinate of the click location.
     * @param y    The y-coordinate of the click location.
     * @param buttons The button mask indicating which button(s) are being
     *                clicked.  Use {@link InputEvent} constants (just like
     *                in a mouse listener) to identify which button(s) to
     *                click simultaneously.
     * @param count   The number of clicks for this event
     *                (single, double, more ...).
     */
    public void click(Component component, int x, int y, int buttons, int count)
    {
        if(switchFocus)
            focus(component);
        ComponentTester.getTester(component).actionClick(
            component, x, y, buttons, count);
    }


    // ----------------------------------------------------------
    /**
     * Press the left mouse button at the given location on a component
     * (mouse button 1).
     * Coordinates are specified relative to the component being clicked.
     * @param component The component to press the mouse button on.
     * @param x    The x-coordinate of the click location.
     * @param y    The y-coordinate of the click location.
     */
    public void mousePress(Component component, int x, int y)
    {
        ComponentTester.getTester(component).actionMousePress(
            component, new ComponentLocation(new Point(x, y)));
    }


    // ----------------------------------------------------------
    /**
     * Press one or more mouse buttons at the given location on a component.
     * Coordinates are specified relative to the component being clicked.
     * @param component The component to press the mouse button on.
     * @param x    The x-coordinate of the click location.
     * @param y    The y-coordinate of the click location.
     * @param buttons The button mask indicating which button(s) are being
     *                clicked.  Use {@link InputEvent} constants (just like
     *                in a mouse listener) to identify which button(s) to
     *                click simultaneously.
     */
    public void mousePress(Component component, int x, int y, int buttons)
    {
        ComponentTester.getTester(component).actionMousePress(
            component, new ComponentLocation(new Point(x, y)), buttons);
    }


    // ----------------------------------------------------------
    /**
     * Release any currently held mouse buttons.
     */
    public void mouseRelease()
    {
        ComponentTester.getTester(Component.class).actionMouseRelease();
    }


    // ----------------------------------------------------------
    /**
     * Move the mouse pointer to the given location on the specified
     * component.  Note that a <code>mouseEntered</code> event will be
     * automatically generated as well, if the mouse was not already
     * over the specified component before the move.  Further, if the mouse
     * is already inside the component and the coordinates specified lie
     * outside it, a <code>mouseExited</code> event will be generated
     * automatically.
     * @param component The component to move the mouse button on.
     * @param x    The x-coordinate of the destination.
     * @param y    The y-coordinate of the destination.
     */
    public void mouseMove(Component component, int x, int y)
    {
        ComponentTester.getTester(component).actionMouseMove(
            component, new ComponentLocation(new Point(x, y)));
    }


    // ----------------------------------------------------------
    /**
     * Move the mouse pointer out of the given component.  This is achieved
     * by moving the mouse to (width + 1, height + 1) relative to the
     * component, forcing a <code>mouseExited</code> event to be generated.
     * This might also cause a <code>mouseEntered</code> event in a surrounding
     * or neighboring component--use <code>mouseMove()</code> with other
     * specific coordinates if you want a different effect.
     * @param component The component to exit from.
     */
    public void mouseExit(Component component)
    {
        mouseMove(
            component, component.getWidth() + 1, component.getHeight() + 1);
    }


    // ----------------------------------------------------------
    /**
     * Initiate a drag action at a specific location on a component.
     * @param component The component to begin the drag from
     * @param x         The x-coordinate for the mouse press that initiates
     *                  the drag, which is specified relative to the
     *                  <code>component</code>.
     * @param y         The y-coordinate for the mouse press that initiates
     *                  the drag, which is specified relative to the
     *                  <code>component</code>.
     */
    public void mouseDragFrom(Component component, int x, int y)
    {
        ComponentTester.getTester(component).actionDrag(
            component, new ComponentLocation(new Point(x, y)));
    }


    // ----------------------------------------------------------
    /**
     * Initiate a drag action at a specific location on a component, with
     * a specified mouse button.
     * @param component The component to begin the drag from
     * @param x         The x-coordinate for the mouse press that initiates
     *                  the drag, which is specified relative to the
     *                  <code>component</code>.
     * @param y         The y-coordinate for the mouse press that initiates
     *                  the drag, which is specified relative to the
     *                  <code>component</code>.
     * @param buttons The button mask indicating which button(s) are
     *                pressed to initiate this mouse drag action.  Use
     *                {@link InputEvent} constants (just like
     *                in a mouse listener) to identify which button(s) to
     *                press simultaneously.
     */
    public void mouseDragFrom(Component component, int x, int y, int buttons)
    {
        ComponentTester.getTester(component).actionDrag(
            component, new ComponentLocation(new Point(x, y)), buttons);
    }


    // ----------------------------------------------------------
    /**
     * Drag the currently dragged object over the given location on
     * the specified component.  Use this method to simulate intermediate
     * parts of a drag--that is, movements after the drag has started
     * (via <code>startDragFrom()</code>) and before the "drop" (mouse
     * release).
     * @param component The component to drag over.
     * @param x         The x-coordinate of the location to drag over,
     *                  relative to the given <code>component</code>.
     * @param y         The y-coordinate of the location to drag over,
     *                  relative to the given <code>component</code>.
     */
    public void mouseDragOver(Component component, int x, int y)
    {
        ComponentTester.getTester(component).actionDragOver(
            component, new ComponentLocation(new Point(x, y)));
    }


    // ----------------------------------------------------------
    /**
     * End a drag operation currently in progress by "dropping" at the
     * in the center of the specified component.  This operation presumes
     * you have already started dragging using <code>startDragFrom()</code>.
     * Calling this method implicitly causes a <code>mouseMoved</code>
     * event to get to the center of the given component, followed by a
     * mouse release of whatever mouse buttons are currently pressed as
     * part of the drag operation that was in progress.
     * @param destination The component to drop on.
     */
    public void mouseDropOn(Component destination)
    {
        ComponentTester.getTester(destination).actionDrop(destination);
    }


    // ----------------------------------------------------------
    /**
     * End a drag operation currently in progress by "dropping" at the
     * specified coordinates on the given component.  This operation presumes
     * you have already started dragging using <code>startDragFrom()</code>.
     * Calling this method implicitly causes a <code>mouseMoved</code>
     * event to get to the specified coordinates, followed by a
     * mouse release of whatever mouse buttons are currently pressed as
     * part of the drag operation that was in progress.
     * @param destination The component to drop on.
     * @param x           The x-coordinate of the drop location,
     *                    relative to the given <code>destination</code>.
     * @param y           The y-coordinate of the drop location,
     *                    relative to the given <code>destination</code>.
     */
    public void mouseDropOn(Component destination, int x, int y)
    {
        ComponentTester.getTester(destination).actionDrop(destination, x, y);
    }


    // ----------------------------------------------------------
    /**
     * Give the focus to a specific component.
     * @param component The component to receive focus.
     */
    public void focus(Component component)
    {
        ComponentTester.getTester(component).actionFocus(component);
    }


    // ----------------------------------------------------------
    /**
     * Type the given text into the given component, replacing any
     * existing text already there.  If the empty string or
     * <code>null</code> is given, then this method
     * simply removes all existing text.
     * @param component The component to enter text on.
     * @param text      The text to enter on the component.
     */
    public void enterText(JTextComponent component, String text)
    {
        JTextComponentTester tester =
            (JTextComponentTester)ComponentTester.getTester(component);
        tester.actionEnterText(component, text);
    }


    // ----------------------------------------------------------
    /**
     * Send a single keystroke to a component.  A keystroke consists of
     * a key press/key release, with no modifiers.
     * @param component The component that will receive the keystroke--focus
     *                  will be automatically given to this component first,
     *                  before the keystroke.
     * @param keyCode   The keystroke to send.  Use the
     *                  {@link KeyEvent} <code>VK_*</code> constants.
     */
    public void keyStroke(Component component, int keyCode)
    {
        ComponentTester.getTester(component)
            .actionKeyStroke(component, keyCode);
    }


    // ----------------------------------------------------------
    /**
     * Send a single keystroke to a component.  A keystroke consists of
     * a key press/key release, with the specified modifiers.
     * @param component The component that will receive the keystroke--focus
     *                  will be automatically given to this component first,
     *                  before the keystroke.
     * @param keyCode   The keystroke to send.  Use the
     *                  {@link KeyEvent} <code>VK_*</code> constants.
     * @param modifiers The keyboard modifier keys to press simultaneously
     *                  (e.g., shift, control, etc.).  For modifiers,
     *                  use the {@link InputEvent} modifier mask constants.
     */
    public void keyStroke(Component component, int keyCode, int modifiers)
    {
        ComponentTester.getTester(component)
            .actionKeyStroke(component, keyCode, modifiers);
    }


    // ----------------------------------------------------------
    /**
     * Send a single key press (key down action) to a component.
     * @param component The component that will receive the key press--focus
     *                  will be automatically given to this component first,
     *                  before the key is pressed.
     * @param keyCode   The key to press.  Use the
     *                  {@link KeyEvent} <code>VK_*</code> constants.
     */
    public void keyPress(Component component, int keyCode)
    {
        ComponentTester.getTester(component)
            .actionKeyPress(component, keyCode);
    }


    // ----------------------------------------------------------
    /**
     * Send a single key release (key up action) to a component.
     * @param component The component that will receive the key release--focus
     *                  will be automatically given to this component first,
     *                  before the key is released.
     * @param keyCode   The key to release.  Use the
     *                  {@link KeyEvent} <code>VK_*</code> constants.
     */
    public void keyRelease(Component component, int keyCode)
    {
        ComponentTester.getTester(component)
            .actionKeyRelease(component, keyCode);
    }


    // ----------------------------------------------------------
    /**
     * Type a sequence of characters on the given component, where
     * the contents are specified as a string.  This method
     * will send the events required to generate the given string on
     * the given component.
     * @param component The component to type on (or to).  It will receive
     *                  focus first, if necessary.
     * @param sequence  The content to type on the component.
     */
    public void keyString(Component component, String sequence)
    {
        ComponentTester.getTester(component)
            .actionKeyString(component, sequence);
    }


    // ----------------------------------------------------------
    /**
     * Select a specific item in a combo box or selection list.
     * @param component The combo box to select from.
     * @param item      The item to select.
     */
    public void selectItem(JComboBox component, String item)
    {
        JComboBoxTester tester =
            (JComboBoxTester)ComponentTester.getTester(component);
        tester.actionSelectItem(component, item);
    }


    // ----------------------------------------------------------
    /**
     * Select a specific item in a selection list.
     * @param component The list to select from.
     * @param item      The item to select.
     */
    public void selectItem(JList component, String item)
    {
        JListTester tester =
            (JListTester)ComponentTester.getTester(component);
        tester.actionSelectItem(component, item);
    }


    // ----------------------------------------------------------
    /**
     * Get the contents of a combo box, in the form of a list of strings.
     * @param component The combo box.
     * @return The component's contents as a list (never null).
     */
    public List<String> getContents(JComboBox component)
    {
        JComboBoxTester tester =
            (JComboBoxTester)ComponentTester.getTester(component);
        String[] result = tester.getContents(component);
        if (result == null)
        {
            return new ArrayList<String>();
        }
        else
        {
            return Arrays.asList(result);
        }
    }


    // ----------------------------------------------------------
    /**
     * Get the contents of a list control, in the form of a list of strings.
     * @param component The list.
     * @return The component's contents as a list (never null).
     */
    public List<String> getContents(JList component)
    {
        JListTester tester =
            (JListTester)ComponentTester.getTester(component);
        String[] result = tester.getContents(component);
        if (result == null)
        {
            return new ArrayList<String>();
        }
        else
        {
            return Arrays.asList(result);
        }
    }


    // ----------------------------------------------------------
    /**
     * Select a given menu item.
     * @param item The menu item to select.
     */
    public void selectMenuItem(JMenuItem item)
    {
        JMenuItemTester tester =
            (JMenuItemTester)ComponentTester.getTester(item);
        tester.actionSelectMenuItem(item);
    }


    // ----------------------------------------------------------
    /**
     * Get the hierarchical path to the given component, starting from the
     * outermost component in which it is contained,
     *  e.g., "JFrame:JRootPane:JPanel:JButton".
     *  @param component The component
     *  @return A string representation of the given component's location
     *          in the component hierarchy.
     */
    public String getPathFor(Component component)
    {
        return Robot.toHierarchyPath(component);
    }


    // ----------------------------------------------------------
    /**
     * A shorter, simpler way to get a human-readable representation of a
     * component.  The <code>toString()</code> method on {@link Component}
     * prints a huge amount of information, and ironically often makes it
     * difficult to understand exactly which component you've printed.
     * This version of <code>toString()</code> prints only the component's
     * class, name, and hash code.
     * @param c The component
     * @return A string representation of the component of the form
     * <code>JButton[button1, 6546787]</code>.
     */
    public String toString(Component c)
    {
        return c.getClass().getSimpleName()
            + "[" + c.getName() + ", " + c.hashCode() + "]";
    }


    // ----------------------------------------------------------
    /**
     * Get a human-reable representation of an array of components using
     * {@link #toString(Component)}.
     * @param components The array
     * @return A string representation of the array of the form
     * <code>(JButton[button1, 6546787], JTextField[input, 6881863])</code>.
     */
    public String toString(Component[] components)
    {
        String result = "(";
        for (int i = 0; i < components.length; i++)
        {
            if (i != 0)
            {
                result += ", ";
            }
            result += toString(components[i]);
        }
        return result + ")";
    }


    // ----------------------------------------------------------
    /**
     * Get a human-readable representation of a List of components using
     * {@link #toString(Component)}.
     * @param <T>  This method is a template method, and the type T is
     *             implicitly deduced from the type of elements in the
     *             provided <code>List</code> argument.  It represents the
     *             specific subtype of <code>Component</code> in the
     *             <code>List</code>.
     * @param components The list
     * @return A string representation of the list of the form
     * <code>(JButton[button1, 6546787], JTextField[input, 6881863])</code>.
     */
    public <T extends Component> String toString(List<T> components)
    {
        String result = "(";
        for (int i = 0; i < components.size(); i++)
        {
            if (i != 0)
            {
                result += ", ";
            }
            result += toString(components.get(i));
        }
        return result + ")";
    }


    // ----------------------------------------------------------
    /**
     * Call a method that involves window-based I/O, so that the
     * method is executed on the GUI event thread.  This version is
     * intended for calling void methods that do not return any results.
     * @param reciever The object to which the method belongs.
     * @param methodName The name of the method to call.
     * @param params A list of zero or more parameters to pass to the method.
     */
    public void callGUIIOMethod(
        final Object reciever,
        final String methodName,
        final Object ... params)
    {
        callGUIIOMethod(new Runnable()
            {
                public void run()
                {
                    invoke(reciever, methodName, params);
                }
            });
    }


    // ----------------------------------------------------------
    /**
     * Call a method that involves window-based I/O, so that the
     * method is executed on the GUI event thread, and return its value.
     * This version is intended for calling methods that have a non-void
     * return type.
     * @param <T>  This method is a template method, and the type T is
     *             implicitly deduced from the <code>returnType</code>
     *             argument.
     * @param receiver The object to which the method belongs.
     * @param returnType The expected type of the method's return value.
     * @param methodName The name of the method to call.
     * @param params A list of zero or more parameters to pass to the method.
     * @return The return value of the method.
     */
    public <T> T callGUIIOMethod(
        final Object   receiver,
        final Class<T> returnType,
        final String   methodName,
        final Object ... params)
    {
        class RunnableWithResult implements Runnable
        {
            private T result;

            public void run()
            {
                result = invoke(receiver, returnType, methodName, params);
            }

            public T getResult()
            {
                return result;
            }
        }

        RunnableWithResult runnable = new RunnableWithResult();
        callGUIIOMethod(runnable);
        return runnable.getResult();
    }


    // ----------------------------------------------------------
    /**
     * A more primitive version of <code>callGUIIOMethod()</code> that
     * takes a {@link Runnable} instead of a receiver, method name, and
     * parameters.  Students should use one of the other versions instead.
     * @param r The runnable to invoke.
     */
    public void callGUIIOMethod(Runnable r)
    {
        SwingUtilities.invokeLater(r);
        getRobot().waitForIdle();
    }


    // ----------------------------------------------------------
    /**
     * Assuming that a JFileChoosers is currently open, selects the given
     * file and closes the JFileChooser
     * @param fileName the name of the file to choose
     */
    public void selectFileInChooser(String fileName)
    {
        final JFileChooser chooser = getComponent(JFileChooser.class);
        final File file = new File(fileName);
        callGUIIOMethod(new Runnable()
            {
                public void run()
                {
                    chooser.setSelectedFile(file);
                    chooser.approveSelection();
                }
            });
    }
    /**
     * Assuming that a JFileChoosers is currently open, selects the given
     * files and closes the JFileChooser
     * @param fileName the name of the file to choose
     */
    public void selectFilesInChooser(String ... files)
    {
        final JFileChooser chooser = getComponent(JFileChooser.class);
        final File[] chooserFiles = new File[files.length];
        for(int i = 0; i < files.length; i++)
            chooserFiles[i] = new File(files[i]);

        callGUIIOMethod(new Runnable()
        {
            public void run()
            {
                chooser.setSelectedFiles(chooserFiles);
                chooser.approveSelection();
            }
        });

    }


    // ----------------------------------------------------------
    /**
     * Assuming that a JColorChooser is currently open, chooses the given
     * color and closes the JColorChooser
     * @param color the color to choose in the JColorChooser
     */
    public void selectColorInChooser(final Color color)
    {
        // get the JColorChooser itself
        final JColorChooser chooser = getComponent(JColorChooser.class);
        // the button panel is actually a sibling component of the color
        // chooser, so first get the color chooser's parent
        JPanel chooserParent = (JPanel)chooser.getParent();
        // then find the button panel by using chooserParent
        JPanel buttonPanel =
            getComponent(JPanel.class, where.parentIs(chooserParent));
        // finally, find the button labeled "OK" on the buttonPanel
        final JButton okButton = getComponent(JButton.class,
            where.textIs("OK").and.parentIs(buttonPanel));
        callGUIIOMethod(new Runnable()
            {
                public void run()
                {
                    chooser.setColor(color);
                    okButton.doClick();
                }
            });
    }

    /**
     * Assuming that a Dialog of some sort is currently
     * open, this method clicks the specified button.
     * @param buttonText The text on the button to click.
     */
    public void clickDialogButton(final String buttonText)
    {
        try
        {
            JDialog d = (JDialog)getFinder().find(filter2matcher(where.typeIs(JDialog.class)));
            final JButton b = getComponent(JButton.class, where.textIs(buttonText).and.ancestorIs(d));
            callGUIIOMethod(new Runnable()
            {
                public void run()
                {
                    b.doClick();
                }
            });
        }
        catch ( ComponentNotFoundException e )
        {
            JPanel panel = getComponent(JPanel.class, where.nameIs("OptionPane.buttonArea"));
            final JButton b = getComponent(JButton.class, where.textIs(buttonText).and.ancestorIs(panel));
            callGUIIOMethod(new Runnable()
            {
                public void run()
                {
                    b.doClick();
                }
            });
        }
        catch ( MultipleComponentsFoundException e )
        {
            fail("Found " + e.getComponents().length + " components matching search criteria.");
        }

    }


    // ----------------------------------------------------------
    /**
     * Assuming a {@link JOptionPane} is currently open and waiting for
     * input, this method enters the given text into the pane's text
     * field and then closes the JOptionPane.
     * @param text The text to enter into the JOptionPane.
     */
    public void setInputDialogText(final String text)
    {
        // grab the text field on the JOptionPane by name
        JTextField f = getComponent(
            JTextField.class, where.nameIs("OptionPane.textField"));
        f.setText(text);

        // grab the panel that holds the buttons by name
        // we need this so we can use the parentIs() filter to find the button
        // in case there are other buttons with text "OK"
        JPanel panel = getComponent(
            JPanel.class, where.nameIs("OptionPane.buttonArea"));

        click(getComponent(
            JButton.class, where.textIs("OK").and.parentIs(panel)));
    }

    public void setSwitchFocus(boolean switchFocus)
    {
        this.switchFocus = switchFocus;
    }


    //~ Protected Methods/Declarations ........................................

    // ----------------------------------------------------------
    /**
     * Return an Abbot {@link abbot.tester.Robot} for basic event
     * generation.
     * @return the robot used for this test
     */
    protected Robot getRobot()
    {
        return fixtureHelper.getRobot();
    }


    // ----------------------------------------------------------
    /**
     *  Return a WindowTracker instance.
     *  @return the window tracker for this test
     */
    protected WindowTracker getWindowTracker()
    {
        return fixtureHelper.getWindowTracker();
    }


    // ----------------------------------------------------------
    /**
     * Convenience method to sleep for a UI interval
     * (same as getRobot().sleep()).
     */
    protected void sleep()
    {
        getRobot().sleep();
    }


    // ----------------------------------------------------------
    /**
     * Ensure proper test harness setup that won't
     * be inadvertently overridden by a derived class.
     */
    protected void fixtureSetUp()
    {
        hierarchy = createHierarchy();

        finder = new BasicFinder(hierarchy);

        fixtureHelper = new AWTFixtureHelper(hierarchy)
        {
            // ----------------------------------------------------------
            /**
             * Dispose of all extant windows.
             */
            protected void disposeAll()
            {
                java.util.Iterator<?> iter = hierarchy.getRoots().iterator();
                while (iter.hasNext())
                {
                    hierarchy.dispose((Window)iter.next());
                }
            }
        };
    }


    // ----------------------------------------------------------
    /**
     * Handles restoration of system state.  Automatically disposes of any
     * Components used in the test.
    */
    protected void fixtureTearDown()
    {
        edtExceptionTime = fixtureHelper.getEventDispatchErrorTime();
        edtException = fixtureHelper.getEventDispatchError();
        fixtureHelper.dispose();
        fixtureHelper = null;
        clearTestFields();
        ComponentTester.clearTesterCache();
        // Explicitly set these null, since the test fixture instance may
        // be kept around by the test runner
        hierarchy = null;
        finder = null;
    }


    // ----------------------------------------------------------
    /**
     * Obtain a component finder to look up components.
     * @return a component finder for this test class.
     */
    protected ComponentFinder getFinder()
    {
        return finder;
    }


    // ----------------------------------------------------------
    /**
     * Create the component hierarchy object that will be used in this
     * test class.  This method allows derived classes to provide their
     * own {@link Hierarchy} if necessary.
     * @return A new hierarchy object for use in this test class.
     */
    protected Hierarchy createHierarchy()
    {
        return new TestHierarchy();
    }


    // ----------------------------------------------------------
    /**
     * Get the hierarchy used by finders in this test class.
     * @return The hierarchy currently in use
     */
    protected Hierarchy getHierarchy()
    {
        return hierarchy;
    }


    // ----------------------------------------------------------
    /**
     * Represents an exception that occurred on the event dispatch thread.
     */
    protected static class EventDispatchException
        extends InvocationTargetException
    {
        private static final long serialVersionUID = 8199841262670518931L;


        // ----------------------------------------------------------
        /**
         * Create a new exception object.
         * @param t The throwable that was thrown in the event dispatch thread
         */
        public EventDispatchException(Throwable t)
        {
            super(t, "An exception was thrown on the event dispatch thread: "
                  + t.toString());
        }


        // ----------------------------------------------------------
        public void printStackTrace()
        {
            getTargetException().printStackTrace();
        }


        // ----------------------------------------------------------
        public void printStackTrace(PrintStream p)
        {
            getTargetException().printStackTrace(p);
        }


        // ----------------------------------------------------------
        public void printStackTrace(PrintWriter p)
        {
            getTargetException().printStackTrace(p);
        }
    }


    //~ Private Methods/Declarations ..........................................

    // ----------------------------------------------------------
    /**
     * Clears all non-static {@link TestCase} fields which are instances of
     * any class found in {@link #DISPOSE_CLASSES}.
     */
    private void clearTestFields()
    {
        Field fieldForException = null;
        try
        {
            for (Field field : getClass().getDeclaredFields())
            {
                fieldForException = field;
                if (!Modifier.isStatic(field.getModifiers()))
                {
                    field.setAccessible(true);
                    for (Class<?> cls : DISPOSE_CLASSES)
                    {
                        if (cls.isAssignableFrom(field.getType())) {
                            field.set(this, null);
                        }
                    }
                }
            }
        }
        catch(Exception e) {
            if (fieldForException != null)
            {
                System.err.println("Unable to automatically clear field "
                    + fieldForException + " during fixture tearDown()");
            }
            e.printStackTrace(System.err);
        }
    }


    // ----------------------------------------------------------
    private Matcher filter2matcher(final GUIFilter filter)
    {
        return new MultiMatcher()
        {
            public boolean matches(Component comp)
            {
                return filter.test(comp);
            }

            public Component bestMatch(Component[] components)
                throws MultipleComponentsFoundException
            {
                throw new MultipleComponentsFoundException(components);
            }
        };
    }


    // Force AWT mode, if no preference has been set in the environment
    static
    {
        boolean forceAWT = AccessController.doPrivileged(
            new PrivilegedAction<Boolean>() {
                public Boolean run()
                {
                    return System.getProperty("abbot.robot.mode") != null;
                }
            });

        if (forceAWT)
        {
            Robot.setEventMode(Robot.EM_AWT);
        }
    }


    private static boolean switchFocus;
}
