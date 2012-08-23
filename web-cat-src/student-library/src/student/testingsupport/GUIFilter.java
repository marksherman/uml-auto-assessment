/*==========================================================================*\
 |  $Id: GUIFilter.java,v 1.5 2010/07/26 13:59:37 stedwar2 Exp $
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

import java.awt.Component;
import java.awt.Rectangle;
import java.lang.reflect.Method;

//-------------------------------------------------------------------------
/**
 *  This class Represents a filter or query that can be used to describe
 *  a {@link Component} when searching.  Note that the methods and fields
 *  in this class are designed specifically to support a natural, readable,
 *  boolean expression "mini-language" for use in describing a single
 *  {@link Component} (or group of {@link Component}s) by its (or their)
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
 *  import static student.testingsupport.GUIFilter.ClientImports.*;
 *  </pre>
 *  <p>
 *  Note that the {@link student.GUITestCase} class already re-exports the
 *  items defined in the {@link ClientImports} nested class, so GUI test
 *  cases should <em>not</em> include the static import.
 *  </p>
 *  <p>
 *  The expressions that you can create with this class are designed to
 *  represent "filters" or boolean predicates that can be applied to a
 *  Component, returning true if the component "matches" the filter or false
 *  if the component does not match.
 *  </p>
 *  <p>
 *  Often, a filter object is created solely for the purpose of passing
 *  the filter into some other operation, such as a search operation.  For
 *  example, the student.GUITestCase class provides a
 *  {@link student.GUITestCase#getComponent(Class,GUIFilter) getComponent()}
 *  method that takes a filter as a parameter.  For the examples below, we
 *  will use <code>getComponent()</code> as the context, specifying each
 *  filter as an argument value in a call to that method.
 *  </p>
 *  <p>
 *  The basic principles for using this class are as follows:
 *  </p>
 *  <ul>
 *  <li><p>Never try to create a GUIFilter object directly.  Instead, always
 *         write something that looks like a boolean expression, and that
 *         starts with the operator <code>where</code>:</p>
 *  <pre>
 *  JButton button = getComponent(JButton.class, where.nameIs("okButton"));
 *  </pre></li>
 *  <li><p>The basic properties you can check with filters include:
 *         <code>nameIs()</code>, <code>textIs()</code>,
 *         <code>enabledIs()</code>, <code>hasFocusIs()</code>,
 *         <code>hasFocusIs()</code>, and <code>typeIs()</code>.  They are
 *         all used the same way:</p>
 *  <pre>
 *  JButton done = getComponent(JButton.class, where.textIs("Done"));
 *  JLabel name = getComponent(JLabel.class, where.textIs("Name:"));
 *  </pre></li>
 *  <li><p>You can combine filters using logical "and" as necessary:</p>
 *  <pre>
 *  JButton done = getComponent(JButton.class,
 *      where.nameIs("done").and.enabledIs(true).and.hasFocusIs(true));
 *  </pre></li>
 *  <li><p>You can also use "or":</p>
 *  <pre>
 *  JButton done = getComponent(JButton.class,
 *      where.nameIs("done").or.enabledIs(true).or.hasFocusIs(true));
 *  </pre></li>
 *  <li><p>Operators like "and" and "or" are interpreted strictly left to
 *  right.  There is <b>no precedence</b>, because of the way Java interprets
 *  dot notation.</p>
 *  <pre>
 *  JButton done = getComponent(JButton.class,
 *      where.nameIs("done").or.enabledIs(true).and.hasFocusIs(true));
 *      // means ((name = "done" or enabled = true) and focus = true)
 *      // note that the left operator is always evaluated first!
 *  </pre>
 *  <p>If you want to force a different order of evaluation
 *  than strictly left-to-right, then use parentheses by writing the
 *  appropriate operator as <code>and()</code> or <code>or()</code>.  Just
 *  be sure to start the new expression inside the parentheses with
 *  <code>where</code>:</p>
 *  <pre>
 *  JButton done = getComponent(JButton.class,
 *      where.nameIs("done").or(where.enabledIs(true).and.hasFocusIs(true)));
 *      // now means (name = "done" or (enabled = true and focus = true))
 *      // because of the extra parentheses used
 *  </pre></li>
 *  <li><p>Finally, you can even use "not" (logical negation), but it is
 *  called like a method, so parentheses (and thus a leading <code>where)
 *  are <em>always required</em> to make the intended extent of the negation
 *  clear:</p>
 *  <pre>
 *  JButton done = getComponent(JButton.class,
 *      where.nameIs("done").and.not(where.enabledIs(true).or.hasFocusIs(true)));
 *  </pre></li>
 *  </ul>
 *
 *  @author  Stephen Edwards
 *  @author Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.5 $, $Date: 2010/07/26 13:59:37 $
 */
public abstract class GUIFilter
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
    protected GUIFilter(String description)
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
     * the right argument, see {@link #and(GUIFilter)} instead.
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
     * expressions like <code>where.nameIs("abc").or.nameIs("def")</code>.
     * This operator is implemented as a public field so that the simple
     * <code>.or.</code> notation can be used as a connective between
     * filters.  If you want to use parentheses for grouping to define
     * the right argument, see {@link #or(GUIFilter)} instead.
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
     *  query operations supported for all {@link Component} objects,
     *  each of which can be combined using any operator.
     */
    public static abstract class Operator
    {
        // ----------------------------------------------------------
        /**
         * Create a filter that compares the name of a component against a
         * given value.
         * @param name  The name to look for
         * @return A new filter that succeeds only on components with the
         *         given name
         */
        public GUIFilter nameIs(final String name)
        {
            return applySelfTo(GUIFilter.nameIs(name));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks the text of a component by calling the
         * component's <code>getText()</code> method.
         * @param text The text to look for
         * @return A new filter that succeeds only on components where
         *         <code>getText()</code> returns the specified text.
         */
        public GUIFilter textIs(final String text)
        {
            return applySelfTo(GUIFilter.textIs(text));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that succeeds if a component has focus.
         * @param value True when searching for a component with focus, or
         *              false when searching for one without.
         * @return A new filter that succeeds only on components that currently
         *         has focus
         */
        public GUIFilter hasFocusIs(final boolean value)
        {
            return applySelfTo(GUIFilter.hasFocusIs(value));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that succeeds if a component is enabled.
         * @param value True when searching for an enabled component, or false
         *              when searching for a disabled component.
         * @return A new filter that succeeds only on components that currently
         *         are enabled
         */
        public GUIFilter enabledIs(final boolean value)
        {
            return applySelfTo(GUIFilter.enabledIs(value));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks the class of a component.
         * @param aClass The required class to check for (any subclass will
         *               also match).
         * @return A new filter that only succeeds on instances of the
         *         given class.
         */
        public GUIFilter typeIs(final Class<? extends Component> aClass)
        {
            return applySelfTo(GUIFilter.typeIs(aClass));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a component's width.
         * @param value The width to look for.
         * @return A new filter that succeeds only on components that have
         *         the given width.
         */
        public GUIFilter widthIs(final int value)
        {
            return applySelfTo(GUIFilter.widthIs(value));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a component's height.
         * @param value The height to look for.
         * @return A new filter that succeeds only on components that have
         *         the given height.
         */
        public GUIFilter heightIs(final int value)
        {
            return applySelfTo(GUIFilter.heightIs(value));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a component's size.
         * @param width The required width.
         * @param height The required height.
         * @return A new filter that succeeds only on components that have
         *         the given size.
         */
        public GUIFilter sizeIs(final int width, final int height)
        {
            return applySelfTo(GUIFilter.sizeIs(width, height));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a component's size.
         * @param maxWidth The required width.
         * @param maxHeight The required height.
         * @return A new filter that succeeds only on components that have
         *         the given size.
         */
        public GUIFilter sizeIsWithin(final int maxWidth, final int maxHeight)
        {
            return applySelfTo(GUIFilter.sizeIsWithin(maxWidth, maxHeight));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a component's x-coordinate.
         * @param x The required x-coordinate, relative to the
         *          component's parent.
         * @return A new filter that succeeds only on components that have
         *         the given x-coordinate.
         */
        public GUIFilter xLocationIs(final int x)
        {
            return applySelfTo(GUIFilter.xLocationIs(x));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a component's y-coordinate.
         * @param y The required y-coordinate, relative to the
         *          component's parent.
         * @return A new filter that succeeds only on components that have
         *         the given y-coordinate.
         */
        public GUIFilter yLocationIs(final int y)
        {
            return applySelfTo(GUIFilter.yLocationIs(y));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks a component's location relative to
         * its parent.
         * @param x The required x-coordinate, relative to the
         *          component's parent.
         * @param y The required y-coordinate, relative to the
         *          component's parent.
         * @return A new filter that succeeds only on components that have
         *         the given location.
         */
        public GUIFilter locationIs(final int x, final int y)
        {
            return applySelfTo(GUIFilter.locationIs(x, y));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks whether a component's location (its
         * top left corner) lies within a specific rectangle.
         * @param region A rectangle defining a region in the component's
         *               parent.
         * @return A new filter that succeeds only on components that have
         *         a location within the given region.
         */
        public GUIFilter isLocatedWithin(final Rectangle region)
        {
            return applySelfTo(GUIFilter.isLocatedWithin(region));
        }


        // ----------------------------------------------------------
        /**
         * Create a filter that checks whether a component's bounding box
         * (as returned by {@link Component#getBounds()}) lies within a
         * specific rectangle--that is, whether the entire component's area,
         * rather than just its top left corner, lies within the specified
         * region.
         * @param region A rectangle defining a region in the component's
         *               parent.
         * @return A new filter that succeeds only on components that
         *         lie entirely within the given region, as determined by
         *         {@link Rectangle#contains(Rectangle)}.
         */
        public GUIFilter isContainedWithin(final Rectangle region)
        {
            return applySelfTo(GUIFilter.isContainedWithin(region));
        }

        /**
        * Create a filter that checks a component's parent
        * @param parent the parent component of the component being checked
        * @return A new filter that succeeds only on components that
        *         are children of parent
        *
        */
        public GUIFilter parentIs(final Component parent)
        {
            return applySelfTo(GUIFilter.parentIs(parent));
        }

        /**
         * Create a filter that checks a component's ancestor
         * @param ancestor one of the ancestors of the component being checked
         * @return A new filter that succeeds only on components that
         *         are descendants of ancestor
         */
        public GUIFilter ancestorIs(final Component ancestor)
        {
            return applySelfTo(GUIFilter.ancestorIs(ancestor));
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
        protected abstract GUIFilter applySelfTo(final GUIFilter otherFilter);

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
        public GUIFilter not(final GUIFilter otherFilter)
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
        protected GUIFilter applySelfTo(final GUIFilter otherFilter)
        {
            return new GUIFilter(description(
                GUIFilter.this.toString(), otherFilter.toString()))
            {
                public boolean test(Component component)
                {
                    return combine(GUIFilter.this.test(component),
                        otherFilter.test(component));
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
     * <code>where.nameIs("abc").and(enabledIs(true).or.hasFocusIs(true))</code>.
     * If you wish to use the <code>.and.</code> notation instead, leaving
     * off the parentheses, see {@link BinaryOperator#and(GUIFilter)}.
     *
     * @param otherFilter  The second argument to "and".
     * @return A new filter object that represents "this AND otherFilter".
     */
    public final GUIFilter and(final GUIFilter otherFilter)
    {
        final GUIFilter self = this;
        GUIFilter gf =  new GUIFilter("(" + this + " AND " + otherFilter + ")")
        {
            public boolean test(Component component)
            {
               return self.test(component) && otherFilter.test(component);
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    /**
     * The "or" operator for combining filters, when you want to use
     * parentheses to group its righthand argument.  This method is designed
     * to be used in expressions like
     * <code>where.nameIs("abc").or(enabledIs(true).and.hasFocusIs(true))</code>.
     * If you wish to use the <code>.or.</code> notation instead, leaving
     * off the parentheses, see {@link BinaryOperator#or(GUIFilter)}.
     *
     * @param otherFilter  The second argument to "or".
     * @return A new filter object that represents "this OR otherFilter".
     */
    public final GUIFilter or(final GUIFilter otherFilter)
    {
        final GUIFilter self = this;
        GUIFilter gf = new GUIFilter("(" + this + " OR " + otherFilter + ")")
        {
            public boolean test(Component component)
            {
                return self.test(component) || otherFilter.test(component);
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    /**
     * Evaluate whether a component matches this filter.  This operation is
     * intended to be overridden by each subclass to implement the actual
     * check that a specific kind of filter performs.
     * @param component The component to check
     * @return true if the component matches this filter
     */
    public abstract boolean test(Component component);


    // ----------------------------------------------------------
    /**
     * This class represents the "where" operator that is used to begin
     * a filter expression.  Client classes that wish to support filter
     * syntax should declare a final field (static or instance) like
     * this:
     * <pre>
     * public static final GUIFilter.WhereOperator where =
     *     new GUIFilter.WhereOperator();
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
            protected GUIFilter applySelfTo(GUIFilter filter)
            {
                return filter;
            }
        };


        // ----------------------------------------------------------
        /**
         * The "not" operator for negating an existing filter, when the not
         * operation is at the very beginning of the expression.  This
         * method is designed to be used in expressions like
         * <code>not(where.enabledIs(true).or.hasFocusIs(true))</code>.
         *
         * @param otherFilter The filter to negate
         * @return A new filter that represents a combination of the left
         *         filter with "NOT otherFilter".
         */
        public static GUIFilter not(final GUIFilter otherFilter)
        {
            return primitiveNot(otherFilter);
        }
    }


    //~ Private Methods/Declarations ..........................................

    // ----------------------------------------------------------
    private static GUIFilter nameIs(final String name)
    {
        GUIFilter gf = new GUIFilter("name = \"" + name + "\"")
        {
            public boolean test(Component component)
            {
                if (component.getName() == null)
                {
                    return name == null;
                }
                else
                {
                    return component.getName().equals(name);
                }
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    private static GUIFilter textIs(final String text)
    {
        GUIFilter gf = new GUIFilter("text = \"" + text + "\"")
        {
            public boolean test(Component component)
            {
                Method m = null;
                try
                {
                    m = component.getClass().getMethod("getText");
                    return ((String)m.invoke(component)).equals(text);
                }
                catch (Exception e)
                {
                    return false;
                }
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    private static GUIFilter hasFocusIs(final boolean value)
    {
        GUIFilter gf = new GUIFilter("hasFocus = " + value)
        {
            public boolean test(Component component)
            {
                return component.isFocusOwner() == value;
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    private static final GUIFilter enabledIs(final boolean value)
    {
        GUIFilter gf = new GUIFilter("enabled = " + value)
        {
            public boolean test(Component component)
            {
                return component.isEnabled() == value;
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    private static final GUIFilter widthIs(final int value)
    {
        GUIFilter gf = new GUIFilter("width = " + value)
        {
            public boolean test(Component component)
            {
                return component.getWidth() == value;
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    private static final GUIFilter heightIs(final int value)
    {
        GUIFilter gf = new GUIFilter("height = " + value)
        {
            public boolean test(Component component)
            {
                return component.getHeight() == value;
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    private static final GUIFilter sizeIs(final int width, final int height)
    {
        GUIFilter gf = new GUIFilter("size = (" + width + ", " + height + ")")
        {
            public boolean test(Component component)
            {
                return component.getWidth() == width
                    && component.getHeight() == height;
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    private static final GUIFilter sizeIsWithin(
        final int maxWidth, final int maxHeight)
    {
        GUIFilter gf = new GUIFilter(
            "sizeIsWithin(" + maxWidth + ", " + maxHeight + ")")
        {
            public boolean test(Component component)
            {
                return component.getWidth() <= maxWidth
                    && component.getHeight() <= maxHeight;
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    private static final GUIFilter xLocationIs(final int value)
    {
        GUIFilter gf = new GUIFilter("xLocation = " + value)
        {
            public boolean test(Component component)
            {
                return component.getX() == value;
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    private static final GUIFilter yLocationIs(final int value)
    {
        GUIFilter gf = new GUIFilter("yLocation = " + value)
        {
            public boolean test(Component component)
            {
                return component.getY() == value;
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    private static final GUIFilter locationIs(final int x, final int y)
    {
        GUIFilter gf = new GUIFilter("location = (" + x + ", " + y + ")")
        {
            public boolean test(Component component)
            {
                return component.getX() == x && component.getY() == y;
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    private static final GUIFilter isLocatedWithin(final Rectangle region)
    {
        GUIFilter gf = new GUIFilter("isLocatedWithin(" + region + ")")
        {
            public boolean test(Component component)
            {
                return region.contains(component.getLocation());
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    private static final GUIFilter isContainedWithin(final Rectangle region)
    {
        GUIFilter gf = new GUIFilter("isContainedWithin(" + region + ")")
        {
            public boolean test(Component component)
            {
                return region.contains(component.getBounds());
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    private static GUIFilter typeIs(final Class<? extends Component> aClass)
    {
        GUIFilter gf = new GUIFilter("type = " + aClass.getSimpleName())
        {
            public boolean test(Component component)
            {
                return aClass.isAssignableFrom(component.getClass());
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    private static GUIFilter parentIs(final Component parent)
    {
        GUIFilter gf = new GUIFilter("parent is " + parent)
        {
            public boolean test(Component component)
            {
                return component.getParent() == parent;
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    private static GUIFilter ancestorIs(final Component ancestor)
    {
        GUIFilter gf = new GUIFilter("ancestor is " + ancestor)
        {
            public boolean test(Component component)
            {
                Component c = component;
                while (c != null && c != ancestor)
                {
                    c = c.getParent();
                }
                return c != null;
            }
        };
        return gf;
    }


    // ----------------------------------------------------------
    private static final GUIFilter primitiveNot(final GUIFilter otherFilter)
    {
        return new GUIFilter("(NOT " + otherFilter + ")")
        {
            public boolean test(Component component)
            {
                return !otherFilter.test(component);
            }
        };
    }

}
