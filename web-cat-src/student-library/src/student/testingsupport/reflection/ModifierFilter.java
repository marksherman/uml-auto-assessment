/*==========================================================================*\
 |  $Id: ModifierFilter.java,v 1.1 2011/06/09 15:31:24 stedwar2 Exp $
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

import java.lang.reflect.Modifier;

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
 *  @version $Revision: 1.1 $, $Date: 2011/06/09 15:31:24 $
 */
public abstract class ModifierFilter<ConcreteFilterType, FilteredObjectType>
    extends Filter<ConcreteFilterType, FilteredObjectType>
{
    //~ Fields ................................................................

    private Integer requiredModifiers;
    private Integer mustNotHaveModifiers;


    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create a new VisibilityFilter object.
     * @param previous The previous filter in the chain of filters.
     * @param descriptionOfConstraint A description of the constraint imposed
     * by this filter (just one step in the chain).
     */
    protected ModifierFilter(
        ModifierFilter<ConcreteFilterType, FilteredObjectType> previous,
        String descriptionOfConstraint)
    {
        super(previous, descriptionOfConstraint);
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations that have "public"
     * visibility.
     * @return The restricted filter.
     */
    public ConcreteFilterType declaredPublic()
    {
        return withModifiers(Modifier.PUBLIC, "declared public");
    }


    // ----------------------------------------------------------
    /**
     * Determine whether this object is declared "public".
     * @return True if this is declared public.
     */
    public boolean isPublic()
    {
        return hasModifiers(Modifier.PUBLIC);
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations that have "protected"
     * visibility.
     * @return The restricted filter.
     */
    public ConcreteFilterType declaredProtected()
    {
        return withModifiers(Modifier.PROTECTED, "declared protected");
    }


    // ----------------------------------------------------------
    /**
     * Determine whether this object is declared "protected".
     * @return True if this is declared protected.
     */
    public boolean isProtected()
    {
        return hasModifiers(Modifier.PROTECTED);
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations that have "private"
     * visibility.
     * @return The restricted filter.
     */
    public ConcreteFilterType declaredPrivate()
    {
        return withModifiers(Modifier.PRIVATE, "declared private");
    }


    // ----------------------------------------------------------
    /**
     * Determine whether this object is declared "private".
     * @return True if this is declared private.
     */
    public boolean isPrivate()
    {
        return hasModifiers(Modifier.PRIVATE);
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations that have "default"
     * visibility (package-level visibility).
     * @return The restricted filter.
     */
    public ConcreteFilterType declaredPackageVisible()
    {
        return withoutModifiers(
            Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE,
            "declared package-visible");
    }


    // ----------------------------------------------------------
    /**
     * Determine whether this object is declared to have "default" visibility
     * (package-level visibility).
     * @return True if this is declared package-level.
     */
    public boolean isPackageVisible()
    {
        return doesNotHaveModifiers(
            Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE);
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations that are "static".
     * @return The restricted filter.
     */
    public ConcreteFilterType declaredStatic()
    {
        return withModifiers(Modifier.STATIC, "declared static");
    }


    // ----------------------------------------------------------
    /**
     * Determine whether this object is declared "static".
     * @return True if this is declared static.
     */
    public boolean isStatic()
    {
        return hasModifiers(Modifier.STATIC);
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations that are "final".
     * @return The restricted filter.
     */
    public ConcreteFilterType declaredFinal()
    {
        return withModifiers(Modifier.FINAL, "declared final");
    }


    // ----------------------------------------------------------
    /**
     * Determine whether this object is declared "final".
     * @return True if this is declared final.
     */
    public boolean isFinal()
    {
        return hasModifiers(Modifier.FINAL);
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations that have all of the
     * given modifiers.  The parameter is an integer bit mask in the
     * same style of {@link Modifier}.
     * @param modifiers The modifiers required by this filter.
     * @return The restricted filter.
     */
    public ConcreteFilterType withModifiers(int modifiers)
    {
        return withModifiers(modifiers, "with modifiers <" + modifiers + ">");
    }


    // ----------------------------------------------------------
    /**
     * Determine whether the objects matching this filter have all of the
     * given modifiers.
     * @param modifiers The modifiers to check.
     * @return True if all given modifiers are present.
     */
    public boolean hasModifiers(final int modifiers)
    {
        if (exists())
        {
            return quantify.evaluate(new Predicate<FilteredObjectType>()
            {
                public boolean isSatisfiedBy(FilteredObjectType object)
                {
                    return (modifiersFor(object) & modifiers) == modifiers;
                }
            });
        }
        else
        {
            int modifiersPresent = requiredModifiers() & modifiers;
            int remaining = modifiers - modifiersPresent;
            if (remaining == 0)
            {
                return true;
            }
            else if (previousFilter() != null
                && previousFilter() instanceof ModifierFilter)
            {
                @SuppressWarnings("rawtypes")
                ModifierFilter filter = (ModifierFilter)previousFilter();
                return filter.hasModifiers(remaining);
            }
            else
            {
                return false;
            }
        }
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations that do not have the
     * any of the given modifiers.  The parameter is an integer bit mask in
     * the same style of {@link Modifier}.
     * @param modifiers The modifiers denied by this filter.
     * @return The restricted filter.
     */
    public ConcreteFilterType withoutModifiers(int modifiers)
    {
        return withoutModifiers(
            modifiers, "without modifiers <" + modifiers + ">");
    }


    // ----------------------------------------------------------
    /**
     * Determine whether the objects matching this filter omit all of the
     * given modifiers.
     * @param modifiers The modifiers to check.
     * @return True if all given modifiers are absent.
     */
    public boolean doesNotHaveModifiers(final int modifiers)
    {
        if (exists())
        {
            return quantify.evaluate(new Predicate<FilteredObjectType>()
            {
                public boolean isSatisfiedBy(FilteredObjectType object)
                {
                    return (modifiersFor(object) & modifiers) == 0;
                }
            });
        }
        else
        {
            int modifiersAbsent = mustNotHaveModifiers() & modifiers;
            int remaining = modifiers - modifiersAbsent;
            if (remaining == 0)
            {
                return true;
            }
            else if (previousFilter() != null
                && previousFilter() instanceof ModifierFilter)
            {
                @SuppressWarnings("rawtypes")
                ModifierFilter filter = (ModifierFilter)previousFilter();
                return filter.doesNotHaveModifiers(remaining);
            }
            else
            {
                return false;
            }
        }
    }


    //~ Protected Methods .....................................................

    // ----------------------------------------------------------
    /**
     * Extract the modifiers from the specified object.
     * @param object The object to retrieve modifiers from
     * @return A single value representing all the modifiers on the object.
     */
    protected abstract int modifiersFor(FilteredObjectType object);


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param object TODO: describe
     * @return TODO: describe
     */
    protected boolean thisFilterAccepts(FilteredObjectType object)
    {
        int required = requiredModifiers();
        int mustNotHave = mustNotHaveModifiers();
        return ((modifiersFor(object) & required) == required)
            && ((modifiersFor(object) & mustNotHave) == 0);
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @return TODO: describe
     */
    protected int requiredModifiers()
    {
        return (requiredModifiers == null)
            ? defaultRequiredModifiers()
            : requiredModifiers;
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @return TODO: describe
     */
    protected int defaultRequiredModifiers()
    {
        if (previousFilter() != null
            && previousFilter() instanceof ModifierFilter)
        {
            @SuppressWarnings("rawtypes")
            ModifierFilter filter = (ModifierFilter)previousFilter();
            return filter.defaultRequiredModifiers();
        }
        return Modifier.PUBLIC;
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @return TODO: describe
     */
    protected int mustNotHaveModifiers()
    {
        return (mustNotHaveModifiers == null)
            ? defaultMustNotHaveModifiers()
            : mustNotHaveModifiers;
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @return TODO: describe
     */
    protected int defaultMustNotHaveModifiers()
    {
        if (previousFilter() != null
            && previousFilter() instanceof ModifierFilter)
        {
            @SuppressWarnings("rawtypes")
            ModifierFilter filter = (ModifierFilter)previousFilter();
            return filter.defaultMustNotHaveModifiers();
        }
        return Modifier.STATIC;
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations that have all of the
     * given modifiers.  The parameter is an integer bit mask in the
     * same style of {@link Modifier}.
     * @param modifiers The modifiers required by this filter.
     * @param description The description of this constraint.
     * @return The restricted filter.
     */
    @SuppressWarnings("unchecked")
    protected ConcreteFilterType withModifiers(
        int modifiers, String description)
    {
        ConcreteFilterType result =
            createFreshFilter((ConcreteFilterType)this, description);

        ModifierFilter<ConcreteFilterType, FilteredObjectType> filter =
            (ModifierFilter<ConcreteFilterType, FilteredObjectType>)result;
        filter.requiredModifiers = modifiers;

        return result;
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations that do not have the
     * any of the given modifiers.  The parameter is an integer bit mask in
     * the same style of {@link Modifier}.
     * @param modifiers The modifiers denied by this filter.
     * @param description The description of this constraint.
     * @return The restricted filter.
     */
    @SuppressWarnings("unchecked")
    protected ConcreteFilterType withoutModifiers(
        int modifiers, String description)
    {
        ConcreteFilterType result =
            createFreshFilter((ConcreteFilterType)this, description);
        ModifierFilter<ConcreteFilterType, FilteredObjectType> filter =
            (ModifierFilter<ConcreteFilterType, FilteredObjectType>)result;
        filter.mustNotHaveModifiers = modifiers;

        return result;
    }


}
