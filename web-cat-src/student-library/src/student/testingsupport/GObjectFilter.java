/*==========================================================================*\
 |  $Id: GObjectFilter.java,v 1.4 2011/06/05 22:47:01 aallowat Exp $
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

package student.testingsupport;

import acm.graphics.GFillable;
import acm.graphics.GLabel;
import acm.graphics.GLine;
import acm.graphics.GObject;
import acm.graphics.GPoint;
import java.awt.Color;
import java.awt.Rectangle;

//-------------------------------------------------------------------------
/**
 *  This class Represents a filter or query that can be used to describe
 *  a {@link GObject} when searching.  Note that the methods and fields
 *  in this class are designed specifically to support a natural, readable,
 *  boolean expression "mini-language" for use in describing a single
 *  {@link GObject} (or group of {@link GObject}s) by its (or their)
 *  properties.  As a result, it does violate some conventions regarding
 *  the use of public fields (although note that all here are immutable)
 *  and occasionally even the naming conventions for constants (e.g.,
 *  <code>where</code>).  However, breaking these conventions is necessary
 *  in this class in order to support the more natural syntax for filter
 *  expressions, and so was deemed a better design choice.
 *  <p>
 *  Client classes that wish to use these filters should add the
 *  following static import directive:
 *  </p>
 *  <pre>
 *  import static student.testingsupport.GObjectFilter.ClientImports.*;
 *  </pre>
 *  <p>
 *  Note that the {@link student.GraphicTestCase} class already re-exports the
 *  items defined in the {@link ClientImports} nested class, so GUI test
 *  cases should <em>not</em> include the static import.
 *  </p>
 *  <p>
 *  The expressions that you can create with this class are designed to
 *  represent "filters" or boolean predicates that can be applied to a
 *  GObject, returning true if the GObject "matches" the filter or false
 *  if the component does not match.
 *  </p>
 *  <p>
 *  Often, a filter object is created solely for the purpose of passing
 *  the filter into some other operation, such as a search operation.  For
 *  example, the student.GraphicTestCase class provides a
 *  {@link student.GraphicTestCase#getGObject(Class,GObjectFilter) getGObject()}
 *  method that takes a filter as a parameter.  For the examples below, we
 *  will use <code>getGObject()</code> as the context, specifying each
 *  filter as an argument value in a call to that method.
 *  </p>
 *  <p>
 *  The basic principles for using this class are as follows:
 *  </p>
 *  <ul>
 *  <li><p>Never try to create a GObjectFilter object directly.  Instead,
 *         always write something that looks like a boolean expression, and
 *         that starts with the operator <code>where</code>:</p>
 *  <pre>
 *  GRect rect = getGObject(GRect.class, where.locationIs(25, 25));
 *  </pre></li>
 *  <li><p>The basic properties you can check with filters include:
 *         <code>locationIs()</code>, <code>textIs()</code>,
 *         <code>visibilityIs()</code>, <code>filledIs()</code>,
 *         <code>widthIs()</code>, and <code>heightIs()</code>.  They are
 *         all used the same way:</p>
 *  <pre>
 *  GLabel name = getGObject(GLabel.class, where.textIs("name"));
 *  GRect rect = getGObject(JLabel.class, where.locationIs(25, 25));
 *  </pre></li>
 *  <li><p>You can combine filters using logical "and" as necessary:</p>
 *  <pre>
 *  GRect rect = getGObject(GRect,
 *      where.locationIs(25, 25).and.visibilityIs(true).and.filledIs(true));
 *  </pre></li>
 *  <li><p>You can also use "or":</p>
 *  <pre>
 *  GRect rect = getGObject(GRect,
 *      where.locationIs(25, 25).or.visibilityIs(true).or.filledIs(true));
 *  </pre></li>
 *  <li><p>Operators like "and" and "or" are interpreted strictly left to
 *  right.  There is <b>no precedence</b>, because of the way Java interprets
 *  dot notation.</p>
 *  <pre>
 *  GRect rect = getGObject(GRect,
 *      where.locationIs(25, 25).or.visibilityIs(true).and.filledIs(true));
 *      // means ((location = (25, 25) or visibility = true) and filled = true)
 *      // note that the left operator is always evaluated first!
 *  </pre>
 *  <p>If you want to force a different order of evaluation
 *  than strictly left-to-right, then use parentheses by writing the
 *  appropriate operator as <code>and()</code> or <code>or()</code>.  Just
 *  be sure to start the new expression inside the parentheses with
 *  <code>where</code>:</p>
 *  <pre>
 *  GRect rect = getGObject(GRect.class,
 *      where.locationIs(25, 25).or(where.visibilityIs(true).and.filledIs(true)));
 *      // now means (location = (25, 25) or (visibility = true and filled = true))
 *      // because of the extra parentheses used
 *  </pre></li>
 *  <li><p>Finally, you can even use "not" (logical negation), but it is
 *  called like a method, so parentheses (and thus a leading <code>where)
 *  are <em>always required</em> to make the intended extent of the negation
 *  clear:</p>
 *  <pre>
 *  GRect rect = getGObject(GRect.class,
 *      where.locationIs(25, 25).and.not(where.visibilityIs(true).or.filledIs(true)));
 *  </pre></li>
 *  </ul>
 *
 *  @author  Stephen Edwards
 *  @author Last changed by $Author: aallowat $
 *  @version $Revision: 1.4 $, $Date: 2011/06/05 22:47:01 $
 */
public abstract class GObjectFilter
{
    //~ Instance/static variables .............................................

    private String description;


    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new filter object.  This constructor is not public, since
     * all filters are expected to be created using operators rather than
     * by calling new.
     * @param description A string description of this filter, used in
     *                    {@link #toString()}.
     */
    protected GObjectFilter (String description)
    {
        this.description = description;
    }


    //~ Public Fields .........................................................

    // These fields are public to afford a more natural syntax, although they
    // can never be manipulated since they are final and have no mutators.
    // They are instance fields instead of static fields, because that is
    // necessary for their semantics.

    // ----------------------------------------------------------
    /**
     * The "and" operator for combining filters, designed to be used in
     * expressions like <code>where.nameIs("...").and.enabledIs(true)</code>.
     * This operator is implemented as a public field so that the simple
     * <code>.and.</code> notation can be used as a connective between
     * filters.  If you want to use parentheses for grouping to define
     * the right argument, see {@link #and(GObjectFilter)} instead.
     */
    public final BinaryOperator and = new BinaryOperator() {
        // ----------------------------------------------------------
        @Override
        protected boolean combine(boolean leftResult, boolean rightResult)
        {
            return leftResult && rightResult;
        }

        // ----------------------------------------------------------
        @Override
        protected String description(
            String leftDescription, String rightDescription)
        {
            return "(" + leftDescription + " AND " + rightDescription + ")";
        }

    };


    // ----------------------------------------------------------
    /**
     * The "or" operator for combining filters, designed to be used in
     * expressions like <code>where.locationIs(25, 25).or.textIs("def")</code>.
     * This operator is implemented as a public field so that the simple
     * <code>.or.</code> notation can be used as a connective between
     * filters.  If you want to use parentheses for grouping to define
     * the right argument, see {@link #or(GObjectFilter)} instead.
     */
    public final BinaryOperator or = new BinaryOperator() {
        // ----------------------------------------------------------
        @Override
        protected boolean combine(boolean leftResult, boolean rightResult)
        {
            return leftResult || rightResult;
        }

        // ----------------------------------------------------------
        @Override
        protected String description(
            String leftDescription, String rightDescription)
        {
            return "(" + leftDescription + " OR " + rightDescription + ")";
        }

    };


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     *  This base class represents an operator used to create a query.
     *  As the base class for all operators, it defines the primitive
     *  query operations supported for all {@link GObject} objects,
     *  each of which can be combined using any operator.
     */
    public static abstract class Operator
    {
        // ----------------------------------------------------------
        /**
         * Create a filter that checks that a GObject contains
         * the given point.
         * @param point the required point
         * @return A new filter that succeeds only on GObjects that
         * contain the point.
         */
        public GObjectFilter cointainsPoint(GPoint point)
        {
            return applySelfTo(GObjectFilter.containsPoint(point));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks that a GObject contains
         * the given point.
         * @param x The required x coordinate, relative to the
         * GObject's parent.
         * @param y The required y coordinate, relative to the
         * GObject's parent.
         * @return A new filter that succeeds only on GObjects that
         * contain the point (x, y).
         */
        public GObjectFilter cointainsPoint(double x, double y)
        {
            return applySelfTo(GObjectFilter.containsPoint(new GPoint(x, y)));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks that a GObject is within 50
         * pixels of the given point.
         * @param point The required point.
         * @return A new filter that succeeds only on GObjects that are
         * located with 50 pixels of point.
         */
        public GObjectFilter isNear(GPoint point)
        {
            return applySelfTo(GObjectFilter.isNear(point));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks that a GObject is within 50
         * pixels of the given point.
         * @param x The required x coordinate, relative to the
         * GObject's parent.
         * @param y The required y coordinate, relative to the
         * GObject's parent.
         * @return A new filter that succeeds only on GObjects that are
         * located within 50 pixels of the point (x, y).
         */
        public GObjectFilter isNear(double x, double y)
        {
            return applySelfTo(GObjectFilter.isNear(new GPoint(x, y)));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks that a GObject is within
         * distance pixels of the given point.
         * @param point The required point.
         * @param distance The distance used to judge whether the
         * GObject is near the given point
         * @return A new filter that succeeds only on GObjects that are
         * within distance pixels of the given point.
         */
        public GObjectFilter isNear(GPoint point, double distance)
        {
            return applySelfTo(GObjectFilter.isNear(point, distance));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks that a GObject is within
         * distance pixels of the given point.
         * @param x The required x coordinate, relative to the
         * GObject's parent.
         * @param y The required y coordinate, relative to the
         * GObject's parent
         * @param distance The distance used to judge whether the
         * GObject is near the given point
         * @return A new filter that succeeds only on GObjects that are
         * within distance pixels of the point (x, y).
         */
        public GObjectFilter isNear(double x, double y, double distance)
        {
            return applySelfTo(
                GObjectFilter.isNear(new GPoint(x, y), distance));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks that a GOBject is a line
         * that starts at point.
         * @param point The required point.
         * @return A new filter that succeeds only on GObjects
         * that are lines starting at point.
         */
        public GObjectFilter lineStartPointIs(GPoint point)
        {
            return applySelfTo(GObjectFilter.lineStartPointIs(point));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks that a GPoint is a line
         * that starts at the point (x, y).
         * @param x The required x coordinate, relative to the
         * GObject's parent.
         * @param y The required y coordinate, relative to the
         * GObject's parent
         * @return A new filter that succeeds only on GObjects
         * that are lines starting at the point (x, y).
         */
        public GObjectFilter lineStartPointIs(double x, double y)
        {
            return applySelfTo(
                GObjectFilter.lineStartPointIs(new GPoint(x, y)));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks that a GOBject is a line
         * that ends at point.
         * @param point The required point.
         * @return A new filter that succeeds only on GObjects
         * that are lines ending at point.
         */
        public GObjectFilter lineEndPointIs(GPoint point)
        {
            return applySelfTo(GObjectFilter.lineEndPointIs(point));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks that a GPoint is a line
         * that ends at the point (x, y).
         * @param x The required x coordinate, relative to the
         * GObject's parent.
         * @param y The required y coordinate, relative to the
         * GObject's parent
         * @return A new filter that succeeds only on GObjects
         * that are lines endin at the point (x, y).
         */
        public GObjectFilter lineEndPointIs(double x, double y)
        {
            return applySelfTo(GObjectFilter.lineEndPointIs(new GPoint(x, y)));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a GObject's size.
         * @param width The required width.
         * @param height The required height.
         * @return A new filter that succeeds only on GObjects that have
         *         the given size.
         */
        public GObjectFilter sizeIs(final double width, final double height)
        {
            return applySelfTo(GObjectFilter.sizeIs(width, height));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a GObject's size.
         * @param maxWidth The required width.
         * @param maxHeight The required height.
         * @return A new filter that succeeds only on GObjects that are
         *         eqal to or smaller than the given size.
         */
        public GObjectFilter sizeIsWithin(
            final double maxWidth, final double maxHeight)
        {
            return applySelfTo(
                GObjectFilter.sizeIsWithin(maxWidth, maxHeight));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a GObject's location relative to
         * its parent.
         * @param x The required x-coordinate, relative to the
         *          GObject's parent.
         * @param y The required y-coordinate, relative to the
         *          GObject's parent.
         * @return A new filter that succeeds only on GObjects that have
         *         the given location.
         */
        public GObjectFilter locationIs(final double x, final double y)
        {
            return locationIs(new GPoint(x, y));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a GObject's location relative to
         * its parent.
         * @param point The required point, relative to the
         *          GObject's parent.
         *
         * @return A new filter that succeeds only on GObjects that have
         *         the given location.
         */
        public GObjectFilter locationIs(final GPoint point)
        {
            return applySelfTo(GObjectFilter.locationIs(point));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks whether a GObject's location (its
         * top left corner) lies within a specific rectangle.
         * @param region A rectangle defining a region in the GObject's
         *               parent.
         * @return A new filter that succeeds only on GObjects that have
         *         a location within the given region.
         */
        public GObjectFilter isLocatedWithin(final Rectangle region)
        {
            return applySelfTo(GObjectFilter.isLocatedWithin(region));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks whether a GObject's bounding box
         * (as returned by {@link GObject#getBounds()}) lies within a
         * specific rectangle--that is, whether the entire GObject's area,
         * rather than just its top left corner, lies within the specified
         * region.
         * @param region A rectangle defining a region in the GObject's
         *               parent.
         * @return A new filter that succeeds only on GObjects that
         *         lie entirely within the given region, as determined by
         *         {@link Rectangle#contains(Rectangle)}.
         */
        public GObjectFilter isContainedWithin(final Rectangle region)
        {
            return applySelfTo(GObjectFilter.isContainedWithin(region));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a GObject's x-coordinate.
         * @param x The required x-coordinate, relative to the
         *          GObject's parent.
         * @return A new filter that succeeds only on GObjects that have
         *         the given x-coordinate.
         */
        public GObjectFilter xLocationIs(final double x)
        {
            return applySelfTo(GObjectFilter.xLocationIs(x));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a GObject's y-coordinate.
         * @param y The required y-coordinate, relative to the
         *          GObject's parent.
         * @return A new filter that succeeds only on GObjects that have
         *         the given y-coordinate.
         */
        public GObjectFilter yLocationIs(final double y)
        {
            return applySelfTo(GObjectFilter.yLocationIs(y));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks the text of a GObject by calling the
         * GObject's <code>getText()</code> method.
         * @param text The text to look for
         * @return A new filter that succeeds only on GObjects where
         *         <code>getText()</code> returns the specified text.
         */
        public GObjectFilter textIs(final String text)
        {
            return applySelfTo(GObjectFilter.textIs(text));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks the class of a GObject.
         * @param aClass The required class to check for (any subclass will
         *               also match).
         * @return A new filter that only succeeds on instances of the
         *         given class.
         */
        public GObjectFilter typeIs(final Class<? extends GObject> aClass)
        {
            return applySelfTo(GObjectFilter.typeIs(aClass));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a GObject's width.
         * @param value The width to look for.
         * @return A new filter that succeeds only on GObjects that have
         *         the given width.
         */
        public GObjectFilter widthIs(final double value)
        {
            return applySelfTo(GObjectFilter.widthIs(value));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a GObject's height.
         * @param value The height to look for.
         * @return A new filter that succeeds only on GObjects that have
         *         the given height.
         */
        public GObjectFilter heightIs(final double value)
        {
            return applySelfTo(GObjectFilter.heightIs(value));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a GObject's color.
         * @param color The required Color.
         * @return A new filter that succeeds only on GObjects that are
         * Color color.
         */
        public GObjectFilter colorIs(final Color color)
        {
            return applySelfTo(GObjectFilter.colorIs(color));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a GObject's fill color.
         * @param color The required Color.
         * @return A new filter that succeeds only on GObjects that are
         * filled with Color color.
         */
        public GObjectFilter fillColorIs(final Color color)
        {
            return applySelfTo(GObjectFilter.fillColorIs(color));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks whether a GObject is filled.
         * @param filled The required boolean value.
         * @return A new filter that succeeds only on GObjects whose
         * filled value is equal to filled.
         */
        public GObjectFilter filledIs(final boolean filled)
        {
            return applySelfTo(GObjectFilter.filledIs(filled));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a GObject's visibilty.
         * @param visibility The required boolean value.
         * @return A new filter that succeeds only on GObjects whos
         * visibility value is equal to visibility.
         */
        public GObjectFilter visibilityIs(final boolean visibility)
        {
            return applySelfTo(GObjectFilter.visibilityIs(visibility));
        }


        // ----------------------------------------------------------
        /**
         * Concrete subclasses must override this to implement an
         * operation on the filter being passed in to transform it into
         * another filter.
         * @param otherFilter The argument to transform (second argument,
         *               for binary operators)
         * @return A new compound filter that includes the given argument
         *         as one subfilter, after applying this operator to it.
         */
        protected abstract GObjectFilter applySelfTo(
            final GObjectFilter otherFilter);
    }


    // ----------------------------------------------------------
    /**
     * A non-static subclass for binary operators that implicitly
     * captures the outer filter to which it belongs, using it as
     * the first/left argument to the operator.
     */
    public abstract class BinaryOperator
        extends Operator
    {
        // ----------------------------------------------------------
        /**
         * The "not" operator for negating an existing filter, when you
         * want to use parentheses to group its righthand argument.  This
         * method is designed to be used in expressions like
         * <code>where.nameIs("abc").and.not(enabledIs(true).or.hasFocusIs(true))</code>.
         * If you wish to use the <code>.not.</code> notation instead, leaving
         * off the parentheses, see {@link BinaryOperator#not}.
         *
         * @param otherFilter The filter to negate
         * @return A new filter that represents a combination of the left
         *         filter with "NOT otherFilter".
         */
        public GObjectFilter not(final GObjectFilter otherFilter)
        {
            return applySelfTo(primitiveNot(otherFilter));
        }


        // ----------------------------------------------------------
        /**
         * Implements a composite filter based on a binary operation,
         * where the "left"/"first" filter is the parent from which this
         * class was created, and the "right"/"second" filter is the
         * argument supplied to this operation.
         * @param otherFilter The argument to transform (second argument,
         *               for binary operators)
         * @return A new compound filter that represents a combination
         *         of the first and second filters.
         */
        @Override
        protected GObjectFilter applySelfTo(final GObjectFilter otherFilter)
        {
            return new GObjectFilter(description(
                GObjectFilter.this.toString(), otherFilter.toString()))
            {
                public boolean test(GObject gobj)
                {
                    return combine(GObjectFilter.this.test(gobj),
                        otherFilter.test(gobj));
                }
            };
        }


        // ----------------------------------------------------------
        /**
         * Concrete subclasses must override this to implement the
         * appropriate logic for combining the results of the two filters
         * being combined.
         * @param leftResult The boolean result of the left filter
         * @param rightResult The boolean result of the right filter
         * @return The result of this combined filter.
         */
        protected abstract boolean combine(
            boolean leftResult, boolean rightResult);


        // ----------------------------------------------------------
        /**
         * Concrete subclasses must override this to implement the
         * appropriate logic for building a description of this filter
         * based on the descriptions of the two filters
         * being combined.
         * @param leftDescription The description of the left filter
         * @param rightDescription The description of the right filter
         * @return The description of this combined filter.
         */
        protected abstract String description(
            String leftDescription, String rightDescription);
    }


    // ----------------------------------------------------------
    /**
     * Get a string representation of this filter.
     * @return A string representation of this filter.
     */
    public String toString()
    {
        return description;
    }


    // ----------------------------------------------------------
    /**
     * The "and" operator for combining filters, when you want to use
     * parentheses to group its righthand argument.  This method is designed
     * to be used in expressions like
     * <code>where.textIs("abc").and(visibility(true).or.filledIs(true))</code>.
     * If you wish to use the <code>.and.</code> notation instead, leaving
     * off the parentheses, see {@link BinaryOperator#and(GObjectFilter)}.
     *
     * @param otherFilter  The second argument to "and".
     * @return A new filter object that represents "this AND otherFilter".
     */
    public final GObjectFilter and(final GObjectFilter otherFilter)
    {
        final GObjectFilter self = this;
        GObjectFilter f =
            new GObjectFilter("(" + this + " AND " + otherFilter + ")")
            {
                public boolean test(GObject gobj)
                {
                    return self.test(gobj) && otherFilter.test(gobj);
                }
            };
        return f;
    }


    // ----------------------------------------------------------
    /**
     * The "or" operator for combining filters, when you want to use
     * parentheses to group its righthand argument.  This method is designed
     * to be used in expressions like
     * <code>where.textIs("abc").or(visibilityIs(true).and.filledIs(true))</code>.
     * If you wish to use the <code>.or.</code> notation instead, leaving
     * off the parentheses, see {@link BinaryOperator#or(GObjectFilter)}.
     *
     * @param otherFilter  The second argument to "or".
     * @return A new filter object that represents "this OR otherFilter".
     */
    public final GObjectFilter or(final GObjectFilter otherFilter)
    {
        final GObjectFilter self = this;
        GObjectFilter f =
            new GObjectFilter("(" + this + " OR " + otherFilter + ")")
            {
                public boolean test(GObject gobj)
                {
                    return self.test(gobj) || otherFilter.test(gobj);
                }
            };
        return f;
    }


    // ----------------------------------------------------------
    /**
     * Evaluate whether a GObject matches this filter.  This operation is
     * intended to be overridden by each subclass to implement the actual
     * check that a specific kind of filter performs.
     * @param gobj The GObject to check
     * @return true if the GObject matches this filter
     */
    public abstract boolean test(GObject gobj);


    // ----------------------------------------------------------
    /**
     * This class represents the "where" operator that is used to begin
     * a filter expression.  Client classes that wish to support filter
     * syntax should declare a final field (static or instance) like
     * this:
     * <pre>
     * public static final GObjectFilter.WhereOperator where =
     *     new GObjectFilter.WhereOperator();
     * </pre>
     */
    public static class ClientImports
    {
        // ----------------------------------------------------------
        /**
         * This object represents the "where" operator that is used to begin
         * a filter expression.
         */
        public static final Operator where = new Operator() {
            // ----------------------------------------------------------
            @Override
            protected GObjectFilter applySelfTo(GObjectFilter filter)
            {
                return filter;
            }
        };


        // ----------------------------------------------------------
        /**
         * The "not" operator for negating an existing filter, when the not
         * operation is at the very beginning of the expression.  This
         * method is designed to be used in expressions like
         * <code>not(where.visibilityIs(true).or.filledIs(true))</code>.
         *
         * @param otherFilter The filter to negate
         * @return A new filter that represents a combination of the left
         *         filter with "NOT otherFilter".
         */
        public static GObjectFilter not(final GObjectFilter otherFilter)
        {
            return primitiveNot(otherFilter);
        }
    }


    //~ Private Methods/Declarations ..........................................

    // ----------------------------------------------------------
    private static final GObjectFilter containsPoint(final GPoint point)
    {
        GObjectFilter f = new GObjectFilter(
            "containsPoint (" + point.getX() + ", " + point.getY() + " )")
        {
            public boolean test(GObject gobj)
            {
                return (gobj.contains(point));
            }
        };
        return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter isNear(final GPoint point)
    {
        GObjectFilter f = new GObjectFilter(
            "isNear (" + point.getX() + ", " + point.getY() + ")")
        {
          public boolean test(GObject gobj)
          {
              double distance =
                  Math.sqrt(Math.pow(point.getX() - gobj.getX(), 2)
                  + Math.pow(point.getX() - gobj.getX(), 2));
              return distance < 50;
          }
        };
        return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter isNear(
        final GPoint point, final double distance)
    {
        GObjectFilter f = new GObjectFilter("within " + distance
            + " pixels of (" + point.getX() + ", " + point.getY() + ")")
        {
            public boolean test(GObject gobj)
            {
                double d =
                    Math.sqrt(Math.pow(point.getX() - gobj.getX(), 2)
                    + Math.pow(point.getX() - gobj.getX(), 2));
                return d < distance;
            }
          };
          return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter lineStartPointIs(final GPoint point)
    {
        GObjectFilter f =  new GObjectFilter(
            "line starts at (" + point.getX() + ", " + point.getY() + ")")
        {
            public boolean test(GObject gobj)
            {
                return (gobj instanceof GLine)
                    && ((GLine)gobj).getStartPoint().equals(point);
            }
        };
        return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter lineEndPointIs(final GPoint point)
    {
        GObjectFilter f = new GObjectFilter(
            "line ending at (" + point.getX() + ", " + point.getY() + ")")
        {
            public boolean test(GObject gobj)
            {
                return (gobj instanceof GLine)
                    && ((GLine)gobj).getEndPoint().equals(point);
            }
        };
        return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter sizeIs(
        final double width, final double height)
    {
        GObjectFilter f = new GObjectFilter(
            "size = (" + width + ", " + height + ")")
        {
            public boolean test(GObject gobj)
            {
                return gobj.getWidth() == width && gobj.getHeight() == height;
            }
        };
        return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter sizeIsWithin(
        final double maxWidth, final double maxHeight)
    {
        GObjectFilter f = new GObjectFilter(
            "sizeIsWithin(" + maxWidth + ", " + maxHeight + ")")
        {
            public boolean test(GObject gobj)
            {
                return gobj.getWidth() <= maxWidth
                    && gobj.getHeight() <= maxHeight;
            }
        };
        return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter locationIs(final GPoint point)
    {
        GObjectFilter f =  new GObjectFilter(
            "location = (" + point.getX() + ", " + point.getY() + ")")
        {
            public boolean test(GObject gobj)
            {
                GPoint loc = gobj.getLocation();
                return loc.getX() == point.getX()
                    && loc.getY() == point.getY();
            }
        };
        return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter isLocatedWithin(final Rectangle region)
    {
        GObjectFilter f = new GObjectFilter("isLocatedWithin(" + region + ")")
        {
            @Override
            public boolean test(GObject gobj)
            {
                return region.contains(gobj.getLocation().toPoint());
            }
        };
        return f;
    }

    // ----------------------------------------------------------
    private static final GObjectFilter isContainedWithin(
        final Rectangle region)
    {
        GObjectFilter f = new GObjectFilter("isLocatedWithin(" + region + ")")
        {
            @Override
            public boolean test(GObject gobj)
            {
                return region.contains(gobj.getBounds().toRectangle());
            }
        };
        return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter xLocationIs(final double x)
    {
        GObjectFilter f = new GObjectFilter("xLocation = " + x)
        {
            @Override
            public boolean test(GObject gobj)
            {
                return gobj.getX() == x;
            }
        };
        return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter yLocationIs(final double y)
    {
        GObjectFilter f = new GObjectFilter("yLocation = " + y)
        {
            @Override
            public boolean test(GObject gobj)
            {
                return gobj.getX() == y;
            }
        };
        return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter textIs(final String text)
    {
        GObjectFilter f = new GObjectFilter("text = \"" + text + "\"")
        {
            public boolean test(GObject gobj)
            {
                if (gobj instanceof GLabel)
                {
                    return (text == null)
                        ? text == ((GLabel)gobj).getLabel()
                        : text.equals(((GLabel)gobj).getLabel());
                }
                else
                {
                    return false;
                }
            }
        };
        return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter widthIs(final double width)
    {
        GObjectFilter f = new GObjectFilter("width = " + width)
        {
            public boolean test( GObject gobj )
            {
                return gobj.getWidth() == width;
            }
        };
        return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter heightIs(final double height)
    {
        GObjectFilter f = new GObjectFilter("height = " + height)
        {
            public boolean test( GObject gobj )
            {
                return gobj.getHeight() == height;
            }
        };
        return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter colorIs(final Color color)
    {
        GObjectFilter f = new GObjectFilter("color = " + color)
        {
            public boolean test( GObject gobj )
            {
                return gobj.getColor().equals(color);
            }
        };
        return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter fillColorIs(final Color color)
    {
        GObjectFilter f = new GObjectFilter("fillColor = " + color)
        {
            public boolean test( GObject gobj )
            {
                return gobj instanceof GFillable
                    && ((GFillable)gobj).getFillColor().equals(color);
            }
        };
        return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter typeIs(final Class<? extends GObject> c)
    {
        GObjectFilter f = new GObjectFilter("type = " + c.getSimpleName())
        {
            public boolean test(GObject gobj)
            {
                return c.isAssignableFrom(gobj.getClass());
            }
        };
        return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter filledIs(final boolean filled)
    {
        GObjectFilter f = new GObjectFilter("filled = " + filled)
        {
            public boolean test(GObject gobj)
            {
                return gobj instanceof GFillable
                    && ((GFillable)gobj).isFilled() == filled;
            }
        };
        return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter visibilityIs(final boolean visibility)
    {
        GObjectFilter f = new GObjectFilter("visibility = " + visibility)
        {
            public boolean test(GObject gobj)
            {
                return gobj.isVisible();
            }
        };
    return f;
    }


    // ----------------------------------------------------------
    private static final GObjectFilter primitiveNot(
        final GObjectFilter otherFilter)
    {
        return new GObjectFilter("(NOT " + otherFilter + ")")
        {
            public boolean test(GObject gobj)
            {
                return !otherFilter.test(gobj);
            }
        };
    }

}
