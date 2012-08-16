/*==========================================================================*\
 |  $Id: Reflection.java,v 1.2 2011/06/09 15:31:24 stedwar2 Exp $
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

package student.testingsupport;

import student.testingsupport.reflection.*;

//-------------------------------------------------------------------------
/**
 *  TODO: document.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2011/06/09 15:31:24 $
 */
public class Reflection
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * This class only provides static methods, to the constructor is
     * private.
     */
    private Reflection()
    {
        // Nothing to do
    }


    //~ Constants .............................................................

    /**
     * This constant represents an argument list containing one argument
     * whose value is null.  It can be passed into invoke() or create()
     * methods whenever the corresponding operation takes a single argument,
     * but the call should pass null as that single argument.  If you try
     * to use <code>null</code> directly in such a situation, you may get
     * a compiler warning, because the compiler cannot distinguish between
     * <code>null</code> meaning no arguments at all (i.e., a null argument
     * list) or <code>null</code> meaning an argument list of length one
     * whose first element is <code>null</code>.  This constant is always
     * interpreted as the second one (if you meant the first, you wouldn't
     * have provided any argument at all!).
     */
    public static final Object[] NULL = new Object[] { null };


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * A starting point for type filters where the name is unspecified.
     */
    public static final Type<?> type = Type.type;


    // ----------------------------------------------------------
    /**
     * Get the type corresponding to a name.
     * @param name The name of the type.
     * @return A Type object representing the type.
     */
    public static Type<?> type(String name)
    {
        return Type.type(name);
    }


    // ----------------------------------------------------------
    /**
     * Get the type corresponding to a {@link Class}.
     * @param aClass The class to represent.
     * @param <T> This parameter is deduced from aClass.
     * @return A Type object representing the class.
     */
    public static <T> Type<T> type(Class<T> aClass)
    {
        return Type.type(aClass);
    }


    // ----------------------------------------------------------
    /**
     * A starting point for field filters where the name is unspecified.
     */
    public static final Field<Object> field = Field.field;


    // ----------------------------------------------------------
    /**
     * Get the field corresponding to a name.
     * @param name The name of the field.
     * @return A Field object representing the field.
     */
    public static Field<Object> field(String name)
    {
        return Field.field(name);
    }


    // ----------------------------------------------------------
    /**
     * Get the field corresponding to a {@link java.lang.reflect.Field}.
     * @param field The field to represent.
     * @return A Field object representing the field.
     */
    public static Field<Object> field(java.lang.reflect.Field field)
    {
        return Field.field(field);
    }


    // ----------------------------------------------------------
    // constructor("name")


    // ----------------------------------------------------------
    // method("name")


    // ----------------------------------------------------------
    // innerClass("name")

    // ----------------------------------------------------------
    // staticField("name")


    // ----------------------------------------------------------
    // staticMethod("name")


    // ----------------------------------------------------------
    // staticInnerClass("name")


    // ----------------------------------------------------------
    /**
     * Change the quantification behavior of a filter so that boolean
     * predicates are evaluated over all matches for the filter and the
     * result is "or"ed together.
     * @param filter The filter to alter.
     * @param <ConcreteFilterType> This type is deduced from the filter.
     * @return A new filter that behaves like the different one, but uses
     * the desired quantification strategy.
     */
    public static <ConcreteFilterType extends Filter<ConcreteFilterType, ?>>
        ConcreteFilterType atLeastOne(ConcreteFilterType filter)
    {
        return Filter.atLeastOne(filter);
    }


    // ----------------------------------------------------------
    /**
     * Change the quantification behavior of a filter so that boolean
     * predicates are evaluated over all matches for the filter and the
     * result is "xor"ed together.
     * @param filter The filter to alter.
     * @param <ConcreteFilterType> This type is deduced from the filter.
     * @return A new filter that behaves like the different one, but uses
     * the desired quantification strategy.
     */
    public static <ConcreteFilterType extends Filter<ConcreteFilterType, ?>>
        ConcreteFilterType onlyOne(ConcreteFilterType filter)
    {
        return Filter.onlyOne(filter);
    }


    // ----------------------------------------------------------
    /**
     * Change the quantification behavior of a filter so that boolean
     * predicates are evaluated over all matches for the filter and the
     * result is "and"ed together.
     * @param filter The filter to alter.
     * @param <ConcreteFilterType> This type is deduced from the filter.
     * @return A new filter that behaves like the different one, but uses
     * the desired quantification strategy.
     */
    public static <ConcreteFilterType extends Filter<ConcreteFilterType, ?>>
        ConcreteFilterType every(ConcreteFilterType filter)
    {
        return Filter.every(filter);
    }


    // ----------------------------------------------------------
    /**
     * Dynamically look up and invoke a class constructor for the target
     * class, with appropriate hints if any failures happen along the way.
     * @param className The type of object to create.
     * @param params The parameters to pass to the constructor.
     * @return The newly created object.
     */
    public static Object create(String className, Object ... params)
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Dynamically look up and invoke a class constructor for the target
     * class, with appropriate hints if any failures happen along the way.
     * @param returnType The type of object to create.
     * @param params The parameters to pass to the constructor.
     * @param <T> The generic parameter T is deduced from the returnType.
     * @return The newly created object.
     */
    public static <T> T create(Type<T> returnType, Object ... params)
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Dynamically look up and invoke a class constructor for the target
     * class, with appropriate hints if any failures happen along the way.
     * @param returnType The type of object to create.
     * @param params The parameters to pass to the constructor.
     * @param <T> The generic parameter T is deduced from the returnType.
     * @return The newly created object.
     */
    public static <T> T create(Class<T> returnType, Object ... params)
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link java.lang.reflect.Constructor#newInstance(Object...)},
     * but converts any thrown exceptions into RuntimeExceptions.
     * @param constructor The constructor to invoke.
     * @param params The parameters to pass to the constructor.
     * @param <T> The generic parameter T is deduced from the constructor.
     * @return The newly created object.
     */
    public static <T> T create(Constructor<T> constructor, Object ... params)
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link java.lang.reflect.Constructor#newInstance(Object...)},
     * but converts any thrown exceptions into RuntimeExceptions.
     * @param constructor The constructor to invoke.
     * @param params The parameters to pass to the constructor.
     * @param <T> The generic parameter T is deduced from the constructor.
     * @return The newly created object.
     */
    public static <T> T create(
        java.lang.reflect.Constructor<T> constructor, Object ... params)
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link #create(String, Object...)}, but unwraps
     * any InvocationTargetExceptions and throws the true cause.  This
     * version is provided when you want to write test cases where you
     * are intending to check for Exceptions as expected results.
     * @param className The type of object to create.
     * @param params The parameters to pass to the constructor.
     * @return The newly created object.
     * @throws Exception if the underlying method throws one.
     */
    public static Object createEx(String className, Object ... params)
        throws Exception
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link #create(Type, Object...)}, but unwraps
     * any InvocationTargetExceptions and throws the true cause.  This
     * version is provided when you want to write test cases where you
     * are intending to check for Exceptions as expected results.
     * @param returnType The type of object to create.
     * @param params The parameters to pass to the constructor.
     * @param <T> The generic parameter T is deduced from the returnType.
     * @return The newly created object.
     * @throws Exception if the underlying method throws one.
     */
    public static <T> T createEx(Type<T> returnType, Object ... params)
        throws Exception
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link #create(Class, Object...)}, but unwraps
     * any InvocationTargetExceptions and throws the true cause.  This
     * version is provided when you want to write test cases where you
     * are intending to check for Exceptions as expected results.
     * @param returnType The type of object to create.
     * @param params The parameters to pass to the constructor.
     * @param <T> The generic parameter T is deduced from the returnType.
     * @return The newly created object.
     * @throws Exception if the underlying method throws one.
     */
    public static <T> T createEx(Class<T> returnType, Object ... params)
        throws Exception
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link #create(java.lang.reflect.Constructor, Object...)},
     * but unwraps any InvocationTargetExceptions and throws the true cause.
     * This version is provided when you want to write test cases where you
     * are intending to check for Exceptions as expected results.
     * @param constructor The constructor to invoke.
     * @param params The parameters to pass to the constructor.
     * @param <T> The generic parameter T is deduced from the constructor.
     * @return The newly created object.
     * @throws Exception if the underlying method throws one.
     */
    public static <T> T createEx(
        Constructor<T> constructor, Object ... params)
        throws Exception
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link #create(java.lang.reflect.Constructor, Object...)},
     * but unwraps any InvocationTargetExceptions and throws the true cause.
     * This version is provided when you want to write test cases where you
     * are intending to check for Exceptions as expected results.
     * @param constructor The constructor to invoke.
     * @param params The parameters to pass to the constructor.
     * @param <T> The generic parameter T is deduced from the constructor.
     * @return The newly created object.
     * @throws Exception if the underlying method throws one.
     */
    public static <T> T createEx(
        java.lang.reflect.Constructor<T> constructor, Object ... params)
        throws Exception
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Dynamically look up and invoke a method on a target object, with
     * appropriate hints if any failures happen along the way.  This
     * version is intended for calling "void" methods that have no
     * return value.
     * @param receiver The object to invoke the method on.
     * @param methodName The name of the method to invoke.
     * @param params The parameters to pass to the method.
     */
    public static void invoke(
        Object receiver,
        String methodName,
        Object ... params)
    {
        invoke(receiver, void.class, methodName, params);
    }


    // ----------------------------------------------------------
    /**
     * Dynamically look up and invoke a method on a target object, with
     * appropriate hints if any failures happen along the way.
     * @param receiver The object to invoke the method on.
     * @param returnType The expected type of the method's return value.
     *     Use null (or <code>void.class</code>) if the method that is
     *     looked up is a void method.
     * @param methodName The name of the method to invoke.
     * @param params The parameters to pass to the method.
     * @param <T> The generic parameter T is deduced from the returnType.
     * @return The results from invoking the given method.
     */
    public static <T> T invoke(
        Object receiver,
        Class<T> returnType,
        String methodName,
        Object ... params)
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Dynamically look up and invoke a method on a target object, with
     * appropriate hints if any failures happen along the way.
     * @param receiver The object to invoke the method on.
     * @param returnType The expected type of the method's return value.
     *     Use null (or <code>void.class</code>) if the method that is
     *     looked up is a void method.
     * @param methodName The name of the method to invoke.
     * @param params The parameters to pass to the method.
     * @param <T> The generic parameter T is deduced from the returnType.
     * @return The results from invoking the given method.
     */
    public static <T> T invoke(
        Object receiver,
        Type<T> returnType,
        String methodName,
        Object ... params)
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link java.lang.reflect.Method#invoke(Object, Object...)},
     * but converts any thrown exceptions into RuntimeExceptions.
     * @param receiver The object to invoke the method on.
     * @param method The method to invoke.
     * @param params The parameters to pass to the method.
     * @param <T> The generic parameter T is deduced from the method.
     * @return The result from the method.
     */
    public static <T> T invoke(
        Object receiver, Method<T> method, Object ... params)
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link java.lang.reflect.Method#invoke(Object, Object...)},
     * but converts any thrown exceptions into RuntimeExceptions.
     * @param receiver The object to invoke the method on.
     * @param method The method to invoke.
     * @param params The parameters to pass to the method.
     * @return The result from the method.
     */
    public static Object invoke(
        Object receiver, java.lang.reflect.Method method, Object ... params)
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link #invoke(Object, String, Object...)}, but unwraps
     * any InvocationTargetExceptions and throws the true cause.  This
     * version is provided when you want to write test cases where you
     * are intending to check for Exceptions as expected results.
     * @param receiver The object to invoke the method on.
     * @param methodName The name of the method to invoke.
     * @param params The parameters to pass to the method.
     * @throws Exception if the underlying method throws one.
     */
    public static void invokeEx(
        Object receiver,
        String methodName,
        Object ... params)
        throws Exception
    {
        invokeEx(receiver, void.class, methodName, params);
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link #invoke(Object, Class, String, Object...)}, but unwraps
     * any InvocationTargetExceptions and throws the true cause.  This
     * version is provided when you want to write test cases where you
     * are intending to check for Exceptions as expected results.
     * @param receiver The object to invoke the method on.
     * @param returnType The expected type of the method's return value.
     *     Use null (or <code>void.class</code>) if the method that is
     *     looked up is a void method.
     * @param methodName The name of the method to invoke.
     * @param params The parameters to pass to the method.
     * @param <T> The generic parameter T is deduced from the returnType.
     * @return The results from invoking the given method.
     * @throws Exception if the underlying method throws one.
     */
    public static <T> T invokeEx(
        Object receiver,
        Class<T> returnType,
        String methodName,
        Object ... params)
        throws Exception
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link #invoke(Object, Class, String, Object...)}, but unwraps
     * any InvocationTargetExceptions and throws the true cause.  This
     * version is provided when you want to write test cases where you
     * are intending to check for Exceptions as expected results.
     * @param receiver The object to invoke the method on.
     * @param returnType The expected type of the method's return value.
     *     Use null (or <code>void.class</code>) if the method that is
     *     looked up is a void method.
     * @param methodName The name of the method to invoke.
     * @param params The parameters to pass to the method.
     * @param <T> The generic parameter T is deduced from the returnType.
     * @return The results from invoking the given method.
     * @throws Exception if the underlying method throws one.
     */
    public static <T> T invokeEx(
        Object receiver,
        Type<T> returnType,
        String methodName,
        Object ... params)
        throws Exception
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link #invoke(Object, Method, Object...)}, but unwraps
     * any InvocationTargetExceptions and throws the true cause.  This
     * version is provided when you want to write test cases where you
     * are intending to check for Exceptions as expected results.
     * @param receiver The object to invoke the method on.
     * @param method The method to invoke.
     * @param params The parameters to pass to the method.
     * @param <T> The generic parameter T is deduced from the method.
     * @return The result from the method.
     * @throws Exception if the underlying method throws one.
     */
    public static <T> T invokeEx(
        Object receiver, Method<T> method, Object ... params)
        throws Exception
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link #invoke(Object, java.lang.reflect.Method, Object...)},
     * but unwraps any InvocationTargetExceptions and throws the true cause.
     * This version is provided when you want to write test cases where you
     * are intending to check for Exceptions as expected results.
     * @param receiver The object to invoke the method on.
     * @param method The method to invoke.
     * @param params The parameters to pass to the method.
     * @return The result from the method.
     * @throws Exception if the underlying method throws one.
     */
    public static Object invokeEx(
        Object receiver, java.lang.reflect.Method method, Object ... params)
        throws Exception
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Get the value of a field.
     * @param receiver The object to retrieve the value from.
     * @param field The field to read.
     * @param <T> The generic parameter T is deduced from the field.
     * @return The value of the given field in the specified object.
     */
    public <T> T get(Object receiver, Field<T> field)
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Get the value of a field, without checking the type of the field.
     * @param receiver The object to retrieve the value from.
     * @param field The field to read.
     * @return The value of the given field in the specified object.
     */
    public Object get(Object receiver, String field)
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Get the value of a field.
     * @param receiver The object to retrieve the value from.
     * @param field The field to read.
     * @param type The type of the field.  The field's value must be assignable
     *             to this type, or a ReflectionError will be thrown.
     * @param <T> The generic parameter T is deduced from the type.
     * @return The value of the given field in the specified object.
     */
    public <T> T get(Object receiver, Type<T> type, String field)
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Get the value of a field.
     * @param receiver The object to retrieve the value from.
     * @param field The field to read.
     * @param type The type of the field.  The field's value must be assignable
     *             to this type, or a ReflectionError will be thrown.
     * @param <T> The generic parameter T is deduced from the type.
     * @return The value of the given field in the specified object.
     */
    public <T> T get(Object receiver, Class<T> type, String field)
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Set the value of a field.  The type of the value must be assignable
     * to the field.
     * @param receiver The object to set the value in.
     * @param field The field to store.
     * @param value The value to store in the field.
     * @param <T> The generic parameter T is deduced from the field.
     */
    public <T> void set(Object receiver, Field<T> field, T value)
    {
        throw new UnsupportedOperationException("not yet implemented");
    }


    // ----------------------------------------------------------
    /**
     * Set the value of a field.  The type of the value must be assignable
     * to the field.
     * @param receiver The object to set the value in.
     * @param field The field to store.
     * @param value The value to store in the field.
     */
    public void set(Object receiver, String field, Object value)
    {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
