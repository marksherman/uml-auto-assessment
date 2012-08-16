/*==========================================================================*\
 |  $Id: ObjectdrawTestCase.java,v 1.4 2010/06/01 12:29:24 fr3lm0 Exp $
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

import acm.graphics.GObject;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import objectdraw.DrawableInterface;
import objectdraw.DrawableIterator;
import objectdraw.DrawingCanvas;
import objectdraw.JDrawingCanvas;
import objectdraw.WindowController;
import student.testingsupport.ObjectdrawFilter;

//-------------------------------------------------------------------------
/**
 *  This class provides enhancements to {@link GUITestCase} to support
 *  testing of objectdraw graphics programs that use {@link DrawableInterface}
 *  instances.
 *
 *  @author  Stephen Edwards
 *  @author Last changed by $Author: fr3lm0 $
 *  @version $Revision: 1.4 $, $Date: 2010/06/01 12:29:24 $
 */
public class ObjectdrawTestCase
    extends GUITestCase
{
    //~ Constants .............................................................

    /**
     * This field re-exports the <code>where</code> operator from
     * {@link student.testingsupport.ObjectdrawFilter} so that it is available
     * in test methods without requiring a static import.
     */
    public final ObjectdrawFilter.Operator where =
        ObjectdrawFilter.ClientImports.where;


    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new ObjectdrawTestCase object.
     */
    public ObjectdrawTestCase()
    {
        super();
    }


    // ----------------------------------------------------------
    /**
     * Creates a new ObjectdrawTestCase object.
     * @param name The name of this test case.
     */
    public ObjectdrawTestCase(String name)
    {
        super(name);
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Look up a {@link DrawableInterface} object on the canvas of the
     * {@link WindowController} being tested by specifying
     * its class.  This method expects the given class to identify a unique
     * DrawableInterface object, meaning that there should only be one
     * instance of the given class on the canvas.
     * If no matching object exists, the test case will
     * fail with an appropriate message.  If more than one matching
     * object exists, the test case will fail with an appropriate message.
     *
     * @param <T>  This method is a template method, and the type T used for
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the object you wish to retrieve, and
     *             also the way you specify the return type of this method.
     * @return The single instance of the desired type that was found
     *         (otherwise, a test case failure results).
     * @see #getFirstDrawableMatching(Class)
     * @see #getAllDrawablesMatching(Class)
     */
    public <T extends DrawableInterface> T getDrawable(Class<T> type)
    {
        @SuppressWarnings("unchecked")
        T result = (T)getDrawable(where.typeIs(type));
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Look up a {@link DrawableInterface} object on the canvas of the
     * {@link WindowController} being tested by specifying its class
     * and an {@link ObjectdrawFilter}.
     * This method expects exactly one DrawableInterface object to match your
     * criteria.  If no matching object exists, the test
     * case will fail with an appropriate message.  If more than one matching
     * object exists, the test case will fail with an appropriate message.
     *
     * @param <T>  This method is a template method, and the type T used for
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the object you wish to retrieve, and
     *             also the way you specify the return type of this method.
     * @param filter The search criteria.
     * @return The single DrawableInterface object matching the criteria
     *         specified (otherwise, a test case failure results).
     * @see #getFirstDrawableMatching(Class, ObjectdrawFilter)
     * @see #getAllDrawablesMatching(Class, ObjectdrawFilter)
     */
    public <T extends DrawableInterface> T getDrawable(
        Class<T> type, ObjectdrawFilter filter)
    {
        @SuppressWarnings("unchecked")
        T result = (T)getDrawable(filter.and.typeIs(type));
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Look up a {@link DrawableInterface} object on the canvas of the
     * {@link WindowController} being tested, using a filter to
     * specify which object you want.  This method is more general
     * than {@link #getDrawable(Class, ObjectdrawFilter)}, since no class
     * needs to be specified, but that also means the return type is less
     * specific (it is always <code>DrawableInterface</code>).
     * This method expects the given filter
     * to identify a unique DrawableInterface object.  If no matching
     * object exists, the test case will fail with an appropriate message.
     * If more than one matching object exists, the test case will fail
     * with an appropriate message.
     *
     * @param filter The search criteria.
     * @return The single DrawableInterface object matching the provided
     *         filter (otherwise, a test case failure results).
     *
     * @see #getFirstDrawableMatching(ObjectdrawFilter)
     * @see #getAllDrawablesMatching(ObjectdrawFilter)
     */
    public DrawableInterface getDrawable(ObjectdrawFilter filter)
    {
        List<DrawableInterface> dis = getAllDrawablesMatching(filter);

        if (dis.size() == 0)
        {
            Assert.fail("Cannot find objectdraw shape matching: " + filter);
        }
        else if (dis.size() > 1)
        {
            Assert.fail("Found " + dis.size()
                + " objectdraw shapes matching: " + filter);
        }
        return dis.get(0);
    }


    // ----------------------------------------------------------
    /**
     * Look up a {@link DrawableInterface} object on the canvas of the
     * {@link WindowController} being tested by specifying its class.
     * This method expects the given class to identify at least one such
     * DrawableInterface object.  If no matching object
     * exists, the test case will fail with an appropriate message.  If more
     * than one matching object exists, the first one found will be returned
     * (although client code should not expect a specific search order).
     *
     * @param <T>  This method is a template method, and the type T used for
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the object you wish to retrieve, and
     *             also the way you specify the return type of this method.
     * @return The first object of the desired type that was found
     *         (a test case failure results if there are none).
     * @see #getDrawable(Class)
     * @see #getAllDrawablesMatching(Class)
     */
    public <T extends DrawableInterface> T getFirstDrawableMatching(
        Class<T> type)
    {
        @SuppressWarnings("unchecked")
        T result = (T)getFirstDrawableMatching(where.typeIs(type));
        return result;
    }


    // ----------------------------------------------------------
     /**
     * Look up a {@link DrawableInterface} object on the canvas of the
     * {@link WindowController} being tested by specifying its class
     * and an {@link ObjectdrawFilter}.
     * This method expects the given criteria to identify at least one such
     * DrawableInterface object.  If no matching object exists, the test case
     * will fail with an appropriate message.  If more than one matching
     * object exists, the first one found will be returned (although client
     * code should not expect a specific search order).
     *
     * @param <T>  This method is a template method, and the type T used for
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the object you wish to retrieve, and
     *             also the way you specify the return type of this method.
     * @param filter The search criteria.
     * @return The first DrawableInterface object that was found matching the
     *         criteria specified (a test case failure results if there are
     *         none).
     * @see #getDrawable(Class,ObjectdrawFilter)
     * @see #getAllDrawablesMatching(Class, ObjectdrawFilter)
     */
    public <T extends DrawableInterface> T getFirstDrawableMatching(
        Class<T> type, ObjectdrawFilter filter)
    {
        @SuppressWarnings("unchecked")
        T result = (T)getFirstDrawableMatching(filter.and.typeIs(type));
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Look up a {@link DrawableInterface} object on the canvas of the
     * {@link WindowController} being tested by specifying
     * an {@link ObjectdrawFilter}.  This method is more general
     * than {@link #getFirstDrawableMatching(Class, ObjectdrawFilter)}, since
     * no class needs to be specified, but that also means the return type
     * is less specific (it is always <code>DrawableInterface</code>).
     * This method expects the given criteria to identify at least one such
     * DrawableInterface object.  If no matching object exists, the test case
     * will fail with an appropriate message.  If more than one matching object
     * exists, the first one found will be returned (although client code
     * should not expect a specific search order).
     *
     * @param filter The search criteria.
     * @return The first object that was found matching the criteria
     *         specified (a test case failure results if there are none).
     * @see #getDrawable(ObjectdrawFilter)
     * @see #getAllDrawablesMatching(ObjectdrawFilter)
     */
    public DrawableInterface getFirstDrawableMatching(ObjectdrawFilter filter)
    {
        DrawableIterator iter = getCanvas().getDrawableIterator();
        while (iter.hasNext())
        {
            DrawableInterface di = iter.next();
            if (filter.test(di))
            {
                return di;
            }
        }
        fail("Cannot find objectdraw shape matching: " + filter);
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Look up all {@link DrawableInterface} objects on the canvas of the
     * {@link WindowController} being tested by specifying their
     * class.  All matching objects are returned in a list.
     * @param <T>  This method is a template method, and the type T used as
     *             the <code>List</code> element type in
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the objects you wish to retrieve,
     *             and also the way you specify the type of elements in
     *             the list returned by this method.
     * @return A list of all objects of the desired type that were found.
     *         This will be an empty list (not null) if no matching objects
     *         are found.
     * @see #getDrawable(Class)
     * @see #getFirstDrawableMatching(Class)
     */
    public <T extends DrawableInterface> List<T> getAllDrawablesMatching(
        Class<T> type)
    {
        @SuppressWarnings("unchecked")
        List<T> result =
            (List<T>)getAllDrawablesMatching(where.typeIs(type));
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Look up all {@link DrawableInterface} objects on the canvas of the
     * {@link WindowController} being tested by specifying their
     * class and an {@link ObjectdrawFilter}.  All matching objects are
     * returned in a list.
     *
     * @param <T>  This method is a template method, and the type T used as
     *             the <code>List</code> element type in
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the objects you wish to retrieve,
     *             and also the way you specify the type of elements in
     *             the list returned by this method.
     * @param filter The search criteria.
     * @return A list of all objects found matching the criteria specified.
     *         This will be an empty list (not null) if no matching objects
     *         are found.
     * @see #getDrawable(Class,ObjectdrawFilter)
     * @see #getAllDrawablesMatching(Class,ObjectdrawFilter)
     */
    public <T extends DrawableInterface> List<T> getAllDrawablesMatching(
        Class<T> type, ObjectdrawFilter filter)
    {
        @SuppressWarnings("unchecked")
        List<T> result =
            (List<T>)getAllDrawablesMatching(filter.and.typeIs(type));
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Look up all {@link DrawableInterface} objects on the canvas of the
     * {@link WindowController} being tested by specifying
     * an {@link ObjectdrawFilter}.  All matching objects are returned in a
     * list.  This method is more general than
     * {@link #getAllDrawablesMatching(Class, ObjectdrawFilter)}, since no
     * class needs to be specified, but that also means the return type
     * is less specific (it is always <code>DrawableInterface</code>).
     * @param filter The search criteria.
     * @return A list of all DrawableInterface objects found matching the
     *         criteria specified.  This will be an empty list (not null) if
     *         no matching objects are found.
     * @see #getDrawable(ObjectdrawFilter)
     * @see #getAllDrawablesMatching(ObjectdrawFilter)
     */
    public List<DrawableInterface> getAllDrawablesMatching(
        ObjectdrawFilter filter)
    {
        ArrayList<DrawableInterface> matches =
            new ArrayList<DrawableInterface>();
        DrawableIterator iter = getCanvas().getDrawableIterator();
        while (iter.hasNext())
        {
            DrawableInterface di = iter.next();
            if (filter.test(di))
            {
                matches.add(di);
            }
        }
        return matches;
    }


    //~ Protected Methods/Declarations ........................................

    // ----------------------------------------------------------
    /**
     * Retrieve the {@link JDrawingCanvas} object associated
     * with the {@link WindowController} being tested.
     *
     * @return The JDrawingCanvas containing all of the WindowController's
     * drawable objects.
     */
    protected DrawingCanvas getCanvas()
    {
        return getComponent(JDrawingCanvas.class);
    }
}
