/*==========================================================================*\
 |  $Id: GraphicTestCase.java,v 1.3 2010/06/01 12:23:39 fr3lm0 Exp $
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

import acm.graphics.GCanvas;
import acm.graphics.GObject;
import acm.program.GraphicsProgram;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import junit.framework.Assert;
import student.testingsupport.GObjectFilter;

//-------------------------------------------------------------------------
/**
 *  This class provides enhancements to {@link GUITestCase} to support
 *  testing of ACM graphics programs that use {@link GObject} instances.
 *
 *  @author  Stephen Edwards
 *  @author Last changed by $Author: fr3lm0 $
 *  @version $Revision: 1.3 $, $Date: 2010/06/01 12:23:39 $
 */
public class GraphicTestCase
    extends GUITestCase
{
    //~ Constants .............................................................

    /**
     * This field re-exports the <code>where</code> operator from
     * {@link student.testingsupport.GObjectFilter} so that it is available
     * in test methods without requiring a static import.
     */
    public final GObjectFilter.Operator where =
        GObjectFilter.ClientImports.where;


    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new GraphicTestCase object.
     */
    public GraphicTestCase()
    {
        super();
    }


    // ----------------------------------------------------------
    /**
     * Creates a new GraphicTestCase object.
     * @param name The name of this test case.
     */
    public GraphicTestCase(String name)
    {
        super(name);
    }


    //~ Public Methods ........................................................


    // ----------------------------------------------------------
	/**
     * Look up a {@link GObject} on the canvas of the {@link GraphicsProgram}
     * being tested by specifying its class.  This method expects the given
     * class to identify a unique GObject, meaning that there should only
     * be one instance of the given class on the canvas.
     * If no matching GObject exists, the test case will fail with an
     * appropriate message.  If more than one matching GObject exists,
     * the test case will fail with an appropriate message.
     *
     * @param <T>  This method is a template method, and the type T used for
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the GObject you wish to retrieve, and
     *             also the way you specify the return type of this method.
     * @return The single instance of the desired type that was found
     *         (otherwise, a test case failure results).
     * @see #getFirstGObjectMatching(Class)
     * @see #getAllGObjectsMatching(Class)
     */
	public <T extends GObject> T getGObject(Class<T> type)
    {
        @SuppressWarnings("unchecked")
        T result = (T)getGObject(where.typeIs(type));
        return result;
    }


    // ----------------------------------------------------------
	/**
     * Look up a {@link GObject} on the canvas of the {@link GraphicsProgram}
     * being tested by specifying its class and a {@link GObjectFilter}.
     * This method expects exactly one GObject to match your criteria.
     * If no matching GObjecty exists, the test case will fail with an
     * appropriate message.  If more than one matching GObject exists,
     * the test case will fail with an appropriate message.
     *
     * @param <T>  This method is a template method, and the type T used for
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the GObject you wish to retrieve, and
     *             also the way you specify the return type of this method.
     * @param filter The search criteria.
     * @return The single GObject matching the criteria specified
     *         (otherwise, a test case failure results).
     * @see #getFirstGObjectMatching(Class, GObjectFilter)
     * @see #getAllGObjectsMatching(Class, GObjectFilter)
     */
	public <T extends GObject> T getGObject(Class<T> type, GObjectFilter filter)
    {
        @SuppressWarnings("unchecked")
        T result = (T)getGObject(filter.and.typeIs(type));
        return result;
    }


    // ----------------------------------------------------------
	/**
     * Look up a {@link GObject} on the canvas of the {@link GraphicsProgram}
     * being tested, using a filter to specify which GObject you want.  This
     * method is more general than {@link #getGObject(Class, GObjectFilter)},
     * since no class needs to be specified, but that also means the return
     * type is less specific (it is always <code>GObject</code>).
     * This method expects the given filter to identify a unique GObject.  If
     * no matching GObject exists, the test case will fail with an appropriate
     * message.  If more than one matching GObject exists, the test case will
     * fail with an appropriate message.
     *
     * @param filter The search criteria.
     * @return The single GObject matching the provided filter (otherwise, a
     *         test case failure results).
     * @see #getFirstGObjectMatching(GObjectFilter)
     * @see #getAllGObjectsMatching(GObjectFilter)
     */
	public GObject getGObject(GObjectFilter filter)
    {
        List<GObject> gobjs = getAllGObjectsMatching(filter);

        if (gobjs.size() == 0)
        {
            Assert.fail("Cannot find GObject matching: " + filter);
        }
        else if (gobjs.size() > 1)
        {
            Assert.fail(
                "Found " + gobjs.size() + " GObjects matching: " + filter);
        }
        return gobjs.get(0);
    }


    // ----------------------------------------------------------
	/**
     * Look up a {@link GObject} on the canvas of the {@link GraphicsProgram}
     * being tested by specifying its class.
     * This method expects the given class to identify at least one such
     * GObject.  If no matching GObject exists, the test case will fail
     * with an appropriate message.  If more than one matching GObject
     * exists, the first one found will be returned (although client code
     * should not expect a specific search order).
     *
     * @param <T>  This method is a template method, and the type T used for
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the GObject you wish to retrieve, and
     *             also the way you specify the return type of this method.
     * @return The first GObject of the desired type that was found
     *         (a test case failure results if there are none).
     * @see #getGObject(Class)
     * @see #getAllGObjectsMatching(Class)
     */
	public <T extends GObject> T getFirstGObjectMatching(Class<T> type)
    {
        @SuppressWarnings("unchecked")
        T result = (T)getFirstGObjectMatching(where.typeIs(type));
        return result;
    }


    // ----------------------------------------------------------
	 /**
     * Look up a {@link GObject} on the canvas of the {@link GraphicsProgram}
     * being tested by specifying its class and a {@link GObjectFilter}.
     * This method expects the given criteria to identify at least one such
     * GObject.  If no matching GObject exists, the test case will fail
     * with an appropriate message.  If more than one matching GObject
     * exists, the first one found will be returned (although client code
     * should not expect a specific search order).
     *
     * @param <T>  This method is a template method, and the type T used for
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the GObject you wish to retrieve, and
     *             also the way you specify the return type of this method.
     * @param filter The search criteria.
     * @return The first GObject that was found matching the criteria
     *         specified (a test case failure results if there are none).
     * @see #getGObject(Class, GObjectFilter)
     * @see #getAllGObjectsMatching(Class, GObjectFilter)
     */
	public <T extends GObject> T getFirstGObjectMatching(
	    Class<T> type, GObjectFilter filter)
    {
        @SuppressWarnings("unchecked")
        T result = (T)getFirstGObjectMatching(filter.and.typeIs(type));
        return result;
    }


    // ----------------------------------------------------------
	/**
     * Look up a {@link GObject} on the canvas of the {@link GraphicsProgram}
     * being tested by specifying a {@link GObjectFilter}.
     * This method is more general
     * than {@link #getFirstGObjectMatching(Class, GObjectFilter)}, since no
     * class needs to be specified, but that also means the return type
     * is less specific (it is always <code>GObject</code>).
     * This method expects the given criteria to identify at least one such
     * GObject.  If no matching GObject exists, the test case will fail
     * with an appropriate message.  If more than one matching GObject
     * exists, the first one found will be returned (although client code
     * should not expect a specific search order).
     *
     * @param filter The search criteria.
     * @return The first GObject that was found matching the criteria
     *         specified (a test case failure results if there are none).
     * @see #getGObject(GObjectFilter)
     * @see #getAllGObjectsMatching(GObjectFilter)
     */
    public GObject getFirstGObjectMatching(GObjectFilter filter)
    {
        Iterator<GObject> iter = getCanvas().iterator();
        while (iter.hasNext())
        {
            GObject gobj = iter.next();
            if (filter.test(gobj))
            {
                return gobj;
            }
        }
        fail("Cannot find GObject matching: " + filter);
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Look up all {@link GObject}s on the canvas of the
     * {@link GraphicsProgram} being tested by specifying their
     * class.  All matching objects are returned in a list.
     *
     * @param <T>  This method is a template method, and the type T used as
     *             the <code>List</code> element type in
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the GObjects you wish to retrieve,
     *             and also the way you specify the type of elements in
     *             the list returned by this method.
     * @return A list of all GObjects of the desired type that were found.
     *         This will be an empty list (not null) if no matching GObjects
     *         are found.
     * @see #getGObject(Class)
     * @see #getFirstGObjectMatching(Class)
     */
    public <T extends GObject> List<T> getAllGObjectsMatching(Class<T> type)
    {
        @SuppressWarnings("unchecked")
        List<T> result =
            (List<T>)getAllGObjectsMatching(where.typeIs(type));
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Look up all {@link GObject}s on the canvas of the
     * {@link GraphicsProgram} being tested by specifying their
     * class and a {@link GObjectFilter}.  All matching objects are returned in
     * a list.
     * @param <T>  This method is a template method, and the type T used as
     *             the <code>List</code> element type in
     *             the return value is implicitly deduced from the provided
     *             argument <code>type</code>.
     * @param type The type (class) of the GObjects you wish to retrieve,
     *             and also the way you specify the type of elements in
     *             the list returned by this method.
     * @param filter The search criteria.
     * @return A list of all GObjects found matching the criteria specified.
     *         This will be an empty list (not null) if no matching GObjects
     *         are found.
     * @see #getGObject(Class,GObjectFilter)
     * @see #getAllGObjectsMatching(Class, GObjectFilter)
     */
    public <T extends GObject> List<T> getAllGObjectsMatching(
        Class<T> type, GObjectFilter filter)
    {
        @SuppressWarnings("unchecked")
        List<T> result =
            (List<T>)getAllGObjectsMatching(filter.and.typeIs(type));
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Look up all {@link GObject}s on the canvas of the
     * {@link GraphicsProgram} being tested by specifying
     * a {@link GObjectFilter}.
     * All matching objects are returned in a list.
     * This method is more general than
     * {@link #getAllGObjectsMatching(Class, GObjectFilter)}, since no
     * class needs to be specified, but that also means the return type
     * is less specific (it is always <code>List&lt;GObject&gt;</code>).
     *
     * @param filter The search criteria.
     * @return A list of all GObjects found matching the criteria specified.
     *         This will be an empty list (not null) if no matching GObjects
     *         are found.
     * @see #getGObject(GObjectFilter)
     * @see #getAllGObjectsMatching(GObjectFilter)
     */
    public List<GObject> getAllGObjectsMatching(GObjectFilter filter)
    {
        ArrayList<GObject> matches = new ArrayList<GObject>();
        Iterator<GObject> iter = getCanvas().iterator();
        while (iter.hasNext())
        {
            GObject gobj = iter.next();
            if (filter.test(gobj))
            {
                matches.add(gobj);
            }
        }
        return matches;
    }


    //~ Protected Methods/Declarations ........................................

    // ----------------------------------------------------------
    /**
     * Retrieve the {@link GCanvas} object associated
     * with the {@link GraphicsProgram} being tested.
     *
     * @return The GCanvas containing all of the GraphicsProgram's GObjects.
     */
    protected GCanvas getCanvas()
    {
        return getComponent(GCanvas.class);
    }
}
