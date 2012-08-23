/*==========================================================================*\
 |  $Id: NameFilter.java,v 1.2 2011/06/09 15:31:24 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2011 Virginia Tech
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

package student.testingsupport.reflection;

import java.util.regex.Pattern;

//-------------------------------------------------------------------------
/**
 *  TODO: document.
 *
 *  @param <ConcreteFilterType> A parameter indicating the concrete subclass
 *  of this class, for use in providing more specialized return types on
 *  some methods.
 *  @param <FilteredObjectType> A parameter indicating the kind of object
 *  this filter accepts.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2011/06/09 15:31:24 $
 */
public abstract class NameFilter<ConcreteFilterType, FilteredObjectType>
    extends AnnotationFilter<ConcreteFilterType, FilteredObjectType>
{
    //~ Fields ................................................................

    private String requiredName;
    private Pattern matchPattern;
    private Pattern containsPattern;


    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create a new NameFilter object.
     * @param previous The previous filter in the chain of filters.
     * @param descriptionOfConstraint A description of the constraint imposed
     * by this filter (just one step in the chain).
     */
    protected NameFilter(
        NameFilter<ConcreteFilterType, FilteredObjectType> previous,
        String descriptionOfConstraint)
    {
        super(previous, descriptionOfConstraint);
        requiredName = null;
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations with the specified
     * name.
     * @param name The name required by the resulting filter.
     * @return A new filter with the given restriction.
     */
    @SuppressWarnings("unchecked")
    public ConcreteFilterType withName(String name)
    {
        if (name == null)
        {
            ConcreteFilterType result = (ConcreteFilterType)this;
            return result;
        }
        ConcreteFilterType result = createFreshFilter(
            (ConcreteFilterType)this, "with name \"" + name + '"');

        NameFilter<ConcreteFilterType, FilteredObjectType> filter =
            (NameFilter<ConcreteFilterType, FilteredObjectType>)result;
        filter.requiredName = name;

        return result;
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations with a
     * name matching the specified pattern (regular expression).
     * @param pattern The pattern required by the resulting filter
     * @return A new filter with the given restriction.
     */
    @SuppressWarnings("unchecked")
    public ConcreteFilterType withNameMatching(Pattern pattern)
    {
        if (pattern == null)
        {
            ConcreteFilterType result = (ConcreteFilterType)this;
            return result;
        }
        ConcreteFilterType result = createFreshFilter(
            (ConcreteFilterType)this, "with name matching \"" + pattern + '"');

        NameFilter<ConcreteFilterType, FilteredObjectType> filter =
            (NameFilter<ConcreteFilterType, FilteredObjectType>)result;
        filter.matchPattern = pattern;

        return result;
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations with a
     * name matching the specified pattern, interpreted as a regular
     * expression.
     * @param pattern The pattern required by the resulting filter
     * @return A new filter with the given restriction.
     */
    public ConcreteFilterType withNameMatching(String pattern)
    {
        if (pattern == null)
        {
            @SuppressWarnings("unchecked")
            ConcreteFilterType result = (ConcreteFilterType)this;
            return result;
        }
        return withNameMatching(Pattern.compile(pattern));
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations with a
     * name containing the specified pattern (a regular expression).
     * @param pattern The pattern required by the resulting filter
     * @return A new filter with the given restriction.
     */
    @SuppressWarnings("unchecked")
    public ConcreteFilterType withNameContaining(Pattern pattern)
    {
        if (pattern == null)
        {
            ConcreteFilterType result = (ConcreteFilterType)this;
            return result;
        }
        ConcreteFilterType result = createFreshFilter(
            (ConcreteFilterType)this, "with name matching \"" + pattern + '"');

        NameFilter<ConcreteFilterType, FilteredObjectType> filter =
            (NameFilter<ConcreteFilterType, FilteredObjectType>)result;
        filter.containsPattern = pattern;

        return result;
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations with a
     * name containing the specified pattern, interpreted as a
     * regular expression.
     * @param pattern The pattern required by the resulting filter
     * @return A new filter with the given restriction.
     */
    public ConcreteFilterType withNameContaining(String pattern)
    {
        if (pattern == null)
        {
            @SuppressWarnings("unchecked")
            ConcreteFilterType result = (ConcreteFilterType)this;
            return result;
        }
        return withNameContaining(Pattern.compile(pattern));
    }


    // ----------------------------------------------------------
    /**
     * Retrieve the name of the object matching this filter.
     * @return The object's name.
     */
    public String getName()
    {
        if (exists())
        {
            return nameOf(uniqueMatch());
        }
        else if (requiredName != null)
        {
            return requiredName;
        }
        else if (previousFilter() != null
            && previousFilter() instanceof NameFilter)
        {
            NameFilter<ConcreteFilterType, FilteredObjectType> filter =
                (NameFilter<ConcreteFilterType, FilteredObjectType>)
                previousFilter();
            return filter.getName();
        }
        else
        {
            return null;
        }
    }


    //~ Protected Methods .....................................................

    // ----------------------------------------------------------
    /**
     * A helper for diagnostic messages that should be used in place
     * of {@link #getName()} when you want a printable (i.e., non-null)
     * name.
     * @return The name, if it exists, or the string "unknown" if
     * the name is null.
     */
    protected String getNameOrUnknown()
    {
        String name = getName();
        return (name == null) ? "unknown" : name;
    }


    // ----------------------------------------------------------
    /**
     * Extract the name from the specified object.
     * @param object The object to retrieve modifiers from
     * @return The name of the object.
     */
    protected abstract String nameOf(FilteredObjectType object);


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param object TODO: describe
     * @return TODO: describe
     */
    protected boolean thisFilterAccepts(FilteredObjectType object)
    {
        boolean result = true;
        String name = nameOf(object);
        if (requiredName != null)
        {
            result = requiredName.equals(nameOf(object));
        }
        if (result && matchPattern != null)
        {
            result = matchPattern.matcher(name).matches();
        }
        if (result && containsPattern != null)
        {
            result = containsPattern.matcher(name).find();
        }
        return result && super.thisFilterAccepts(object);
    }
}
