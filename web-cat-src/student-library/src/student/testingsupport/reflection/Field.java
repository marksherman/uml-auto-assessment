/*==========================================================================*\
 |  $Id: Field.java,v 1.2 2011/06/09 15:31:24 stedwar2 Exp $
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

import java.util.ArrayList;
import java.util.List;
import student.testingsupport.reflection.internal.Fields;

//-------------------------------------------------------------------------
/**
 *  A "filter" that represents a set of {@link java.lang.reflect.Field}
 *  objects meeting specified constraints.  This class provides a fluent
 *  interface for specifying constraints on the set of fields, as well as
 *  common predicates useful for testing the properties of the set of fields.
 *  A getter and setter method is also provided.  This class is simpler to
 *  use than {@link java.lang.reflect.Field}, without requiring any explicit
 *  exception handling.
 *
 *  TODO: add full set of usage examples
 *
 *  @param <FieldType> The type used to declare the corresponding field.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2011/06/09 15:31:24 $
 */
public class Field<FieldType>
    extends NameFilter<Field<FieldType>, java.lang.reflect.Field>
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create a new Field object that represents a field filter.
     * @param previous The previous filter in the chain of filters.
     * @param descriptionOfThisStage A description of this stage in the
     * filter chain.
     */
    protected Field(Field<FieldType> previous, String descriptionOfThisStage)
    {
        super(previous, descriptionOfThisStage);
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * A starting point for field filters where the name is unspecified.
     */
    public static final Field<Object> field = new Field<Object>(null, null);


    // ----------------------------------------------------------
    /**
     * Create a new Field object.
     * @param name The name of the field.
     * @return a new Field filter object representing the named field.
     */
    public static Field<Object> field(String name)
    {
        return field.withName(name);
    }


    // ----------------------------------------------------------
    /**
     * Create a new Field object from a {@link java.lang.reflect.Field}
     * object.
     * @param aField The field to represent.
     * @return a new Field filter object representing the given field.
     */
    public static Field<Object> field(java.lang.reflect.Field aField)
    {
        if (aField == null)
        {
            return field;
        }
        return field.createFreshFilter(aField);
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param type TODO: describe
     * @param <T> TODO: describe
     * @return TODO: describe
     */
    @SuppressWarnings("unchecked")
    public <T> Field<T> ofType(final Class<T> type)
    {
        if (type == null)
        {
            Field<T> result = (Field<T>)this;
            return result;
        }
        return new Field<T>(
            (Field<T>)this, "of type " + type.getCanonicalName())
        {
            // ----------------------------------------------------------
            @Override
            protected boolean thisFilterAccepts(java.lang.reflect.Field object)
            {
                return type.equals(object.getType());
            }
        };
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param type TODO: describe
     * @param <T> TODO: describe
     * @return TODO: describe
     */
    public <T> Field<T> ofType(final Type<T> type)
    {
        if (type == null)
        {
            @SuppressWarnings("unchecked")
            Field<T> result = (Field<T>)this;
            return result;
        }

        // Note: This only works if the type has a single, unique match.
        // If the type represents multiple possibilities, the NxM filtering
        // is not performed.

        // TODO: fix this to use the atLeastOne() quantifier
        return ofType(type.raw());
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param type TODO: document.
     * @return TODO: describe
     */
    public boolean isOfType(final Type<?> type)
    {
        if (type == null)
        {
            return false;
        }
        else if (exists())
        {
            // TODO: fix this to use the atLeastOne() quantifier
            return quantify.evaluate(new Predicate<java.lang.reflect.Field>()
                {
                    public boolean isSatisfiedBy(java.lang.reflect.Field object)
                    {
                        return type.equals(object.getType());
                    }
                });
        }
        else
        {
            return false;
        }
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @return TODO: describe
     */
    public Type<FieldType> getType()
    {
        @SuppressWarnings("unchecked")
        Type<FieldType> result =
            Type.type((Class<FieldType>)uniqueMatch().getType());
        return result;
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param type TODO: describe
     * @return TODO: describe
     */
    public Field<FieldType> in(final Type<?> type)
    {
        if (type == null)
        {
            return this;
        }
        // TODO: needs to work for a type that represents a set of classes!
        return in(type.raw());
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param type TODO: describe
     * @return TODO: describe
     */
    public Field<FieldType> in(final Class<?> type)
    {
        if (type == null)
        {
            return this;
        }
        return new Field<FieldType>(
            this, "in " + type.getCanonicalName())
        {
            // ----------------------------------------------------------
            @Override
            protected List<java.lang.reflect.Field> candidatesFromThisFilter()
            {
                return Fields.fieldsIn(type);
            }
        };
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param receiver TODO: describe
     * @return TODO: describe
     */
    public Field<FieldType> in(final Object receiver)
    {
        if (receiver == null)
        {
            return this;
        }
        return new Field<FieldType>(in(receiver.getClass()),
            "on receiver " + receiver)
        {
            // ----------------------------------------------------------
            protected Object receiver()
            {
                return receiver;
            }
        };
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param type TODO: document.
     * @return TODO: describe
     */
    public boolean isIn(final Type<?> type)
    {
        if (type == null)
        {
            return false;
        }
        else if (exists())
        {
            return quantify.evaluate(new Predicate<java.lang.reflect.Field>()
                {
                    public boolean isSatisfiedBy(java.lang.reflect.Field object)
                    {
                        return Type.type(object.getDeclaringClass())
                            .isAssignableFrom(type);
                    }
                });
        }
        else
        {
            return false;
        }
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param type TODO: document.
     * @return TODO: describe
     */
    public boolean isIn(Class<?> type)
    {
        if (type == null)
        {
            return false;
        }
        return isIn(Type.type(type));
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param receiver TODO: document.
     * @return TODO: describe
     */
    public boolean isIn(Object receiver)
    {
        if (receiver == null)
        {
            return false;
        }
        return isIn(Type.type(receiver.getClass()));
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param type TODO: describe
     * @return TODO: describe
     */
    public Field<FieldType> declaredIn(final Type<?> type)
    {
        if (type == null)
        {
            return this;
        }
        return declaredIn(type.raw());
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param type TODO: describe
     * @return TODO: describe
     */
    public Field<FieldType> declaredIn(final Class<?> type)
    {
        if (type == null)
        {
            return this;
        }
        return new Field<FieldType>(
            this, "declared in " + type.getCanonicalName())
        {
            // ----------------------------------------------------------
            @Override
            protected List<java.lang.reflect.Field> candidatesFromThisFilter()
            {
                return Fields.fieldsDeclaredIn(type);
            }
        };
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param receiver TODO: describe
     * @return TODO: describe
     */
    public Field<FieldType> declaredIn(final Object receiver)
    {
        if (receiver == null)
        {
            return this;
        }
        return new Field<FieldType>(
            declaredIn(receiver.getClass()),
            "on receiver <" + receiver + ">")
        {
            // ----------------------------------------------------------
            protected Object receiver()
            {
                return receiver;
            }
        };
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param type TODO: document.
     * @return TODO: describe
     */
    public boolean isDeclaredIn(final Type<?> type)
    {
        if (type == null)
        {
            return false;
        }
        else if (exists())
        {
            return quantify.evaluate(new Predicate<java.lang.reflect.Field>()
                {
                    public boolean isSatisfiedBy(java.lang.reflect.Field object)
                    {
                        return Type.type(object.getDeclaringClass())
                            .equals(type);
                    }
                });
        }
        else
        {
            return false;
        }
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param type TODO: document.
     * @return TODO: describe
     */
    public boolean isDeclaredIn(Class<?> type)
    {
        if (type == null)
        {
            return false;
        }
        return isDeclaredIn(Type.type(type));
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param receiver TODO: document.
     * @return TODO: describe
     */
    public boolean isDeclaredIn(Object receiver)
    {
        if (receiver == null)
        {
            return false;
        }
        return isDeclaredIn(Type.type(receiver.getClass()));
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param type TODO: describe
     * @return TODO: describe
     */
    public Field<FieldType> visibleIn(final Type<?> type)
    {
        if (type == null)
        {
            return this;
        }
        return visibleIn(type.raw());
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param type TODO: describe
     * @return TODO: describe
     */
    public Field<FieldType> visibleIn(final Class<?> type)
    {
        if (type == null)
        {
            return this;
        }
        return new Field<FieldType>(
            this, "visible in " + type.getCanonicalName())
        {
            // ----------------------------------------------------------
            @Override
            protected List<java.lang.reflect.Field> candidatesFromThisFilter()
            {
                return Fields.fieldsVisibleIn(type);
            }
        };
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param receiver TODO: describe
     * @return TODO: describe
     */
    public Field<FieldType> visibleIn(final Object receiver)
    {
        if (receiver == null)
        {
            return this;
        }
        return new Field<FieldType>(
            visibleIn(receiver.getClass()),
            "on receiver <" + receiver + ">")
        {
            // ----------------------------------------------------------
            protected Object receiver()
            {
                return receiver;
            }
        };
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param type TODO: document.
     * @return TODO: describe
     */
    public boolean isVisibleIn(final Type<?> type)
    {
        if (type == null)
        {
            return false;
        }
        else if (exists())
        {
            return quantify.evaluate(new Predicate<java.lang.reflect.Field>()
                {
                    public boolean isSatisfiedBy(java.lang.reflect.Field object)
                    {
                        return Type.type(object.getDeclaringClass())
                            .equals(type)
                            || (Fields.isPackageVisible(object)
                                && type.isInSamePackageAs(
                                    object.getDeclaringClass()));
                    }
                });
        }
        else
        {
            return false;
        }
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param type TODO: document.
     * @return TODO: describe
     */
    public boolean isVisibleIn(Class<?> type)
    {
        if (type == null)
        {
            return false;
        }
        return isVisibleIn(Type.type(type));
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param receiver TODO: document.
     * @return TODO: describe
     */
    public boolean isVisibleIn(Object receiver)
    {
        if (receiver == null)
        {
            return false;
        }
        return isVisibleIn(Type.type(receiver.getClass()));
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param sourceType TODO: describe
     * @return TODO: describe
     */
    public Field<FieldType> assignableFrom(final Type<?> sourceType)
    {
        if (sourceType == null)
        {
            return this;
        }
        return new Field<FieldType>(
            this, "assignable from " + sourceType.getName())
        {
            // ----------------------------------------------------------
            @Override
            protected boolean thisFilterAccepts(java.lang.reflect.Field object)
            {
                return Type.type(object.getType())
                    .isAssignableFrom(sourceType);
            }
        };
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param sourceType TODO: describe
     * @return TODO: describe
     */
    public boolean isAssignableFrom(final Type<?> sourceType)
    {
        if (sourceType == null)
        {
            return false;
        }
        return quantify.evaluate(new Predicate<java.lang.reflect.Field>()
            {
                public boolean isSatisfiedBy(java.lang.reflect.Field object)
                {
                    return Type.type(object.getType())
                        .isAssignableFrom(sourceType);
                };
            });
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param sourceType TODO: describe
     * @return TODO: describe
     */
    public Field<FieldType> assignableFrom(final Class<?> sourceType)
    {
        if (sourceType == null)
        {
            return this;
        }
        return assignableFrom(Type.type(sourceType));
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param sourceType TODO: describe
     * @return TODO: describe
     */
    public boolean isAssignableFrom(final Class<?> sourceType)
    {
        if (sourceType == null)
        {
            return false;
        }
        return quantify.evaluate(new Predicate<java.lang.reflect.Field>()
            {
                private Type<?> source = Type.type(sourceType);
                public boolean isSatisfiedBy(java.lang.reflect.Field object)
                {
                    return Type.type(object.getType())
                        .isAssignableFrom(source);
                };
            });
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @return TODO: document.
     */
    public FieldType get()
    {
        java.lang.reflect.Field field = raw();
        field.setAccessible(true);
        Object receiver = receiver();

        if (receiver == null && !isStatic())
        {
            throw new ReflectionError(
                "no receiver object provided for get() on non-static field "
                + field + ".");
        }
        try
        {
            @SuppressWarnings("unchecked")
            FieldType result = (FieldType)field.get(receiver);
            return result;
        }
        catch (IllegalAccessException e)
        {
            throw new ReflectionError("get() on field " + field
                + " produced an IllegalAccessException on receiver <"
                + receiver + ">: " + e.getMessage());
        }
        catch (IllegalArgumentException e)
        {
            throw new ReflectionError("get() on field " + field
                + " is not applicable to receiver <" + receiver
                + ">: " + e.getMessage());
        }
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param value TODO: document.
     */
    public void set(FieldType value)
    {
        java.lang.reflect.Field field = raw();
        field.setAccessible(true);
        Object receiver = receiver();

        if (receiver == null && !isStatic())
        {
            throw new ReflectionError(
                "no receiver object provided for set() on non-static field "
                + field + ".");
        }
        try
        {
            field.set(receiver, value);
        }
        catch (IllegalAccessException e)
        {
            throw new ReflectionError("set() on field " + field
                + " produced an IllegalAccessException on receiver <"
                + receiver + ">: " + e.getMessage());
        }
        catch (IllegalArgumentException e)
        {
            throw new ReflectionError("set() on field " + field
                + " is not applicable to receiver <" + receiver
                + ">: " + e.getMessage());
        }
    }


    //~ Protected Methods .....................................................

    // ----------------------------------------------------------
    @Override
    protected Field<FieldType> createFreshFilter(
        Field<FieldType> previous, String descriptionOfThisStage)
    {
        return new Field<FieldType>(previous, descriptionOfThisStage);
    }


    // ----------------------------------------------------------
    protected Field<FieldType> createFreshFilter(
        final java.lang.reflect.Field rawField)
    {
        return new Field<FieldType>(null, describe(rawField))
        {
            private List<java.lang.reflect.Field> result =
                new ArrayList<java.lang.reflect.Field>();
            {
                result.add(rawField);
            }

            // ----------------------------------------------------------
            @Override
            protected List<java.lang.reflect.Field> candidatesFromThisFilter()
            {
                return result;
            }
        };
    }


    // ----------------------------------------------------------
    @Override
    protected String filteredObjectDescription()
    {
        return "field";
    }


    // ----------------------------------------------------------
    @Override
    protected String nameOf(java.lang.reflect.Field object)
    {
        return object.getName();
    }


    // ----------------------------------------------------------
    @Override
    protected int modifiersFor(java.lang.reflect.Field object)
    {
        return object.getModifiers();
    }


    // ----------------------------------------------------------
    @Override
    protected int defaultRequiredModifiers()
    {
        return 0;
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @return TODO: describe
     */
    protected Object receiver()
    {
        return null;
    }
}
