/*==========================================================================*\
 |  $Id: ReflectionSupport.java,v 1.8 2012/02/29 03:57:51 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2007-2011 Virginia Tech
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;

//-------------------------------------------------------------------------
/**
 *  This class provides static helper methods to use reflection to check
 *  for and invoke methods on objects.  It is intended for use in test
 *  cases, and makes it easier to write test cases that compile successfully
 *  but fail at run-time if the class under test fails to provide
 *  required methods (or provides them with the wrong signature).  For
 *  Web-CAT users, this makes it possible to get partial scores, even when
 *  some methods are not even provided in the class under test.
 *  <p>
 *  Consider a situation where you are writing a test case that invokes the
 *  <code>doIt()</code> method on a given object.  You might do it like
 *  this:
 *  </p>
 *  <pre>
 *  MyType result = receiver.doIt(param1, param2);  // returns a value
 *  // ...
 *  receiver.doSomethingElse();  // a void method with no parameters
 *  </pre>
 *  <p>The first line passes two parameters to <code>doIt()</code> and stores
 *  the return value in a local variable called <code>result</code>.  All
 *  this would be fine if the class under test actually provides a method
 *  called <code>doIt()</code> with the appropriate signature and return
 *  type.  Otherwise, you would get a compile-time error.  But on Web-CAT
 *  a compilation error in a test suite would give a resulting score of
 *  zero--no successful compilation, no partial credit.  The story is similar
 *  with the second line, which simply calls a void method on the class
 *  under test.
 *  </p>
 *  <p>What if, instead, you wanted to write test cases that <em>checked
 *  for</em> specific methods, and either succeeded or failed depending on
 *  whether the method was present?  You can use the methods in this
 *  class to write such test cases.  So instead of writing the call
 *  above, you could do this:</p>
 *  <pre>
 *  // At the top of your test class
 *  import static net.sf.webcat.ReflectionSupport.*;
 *
 *  // ...
 *
 *  // For a method that returns a result:
 *  MyType result = invoke(receiver, MyType.class, "doIt", param1, param2);
 *
 *  // Or for a void method:
 *  invoke(receiver, "doSomethingElse");
 *  </pre>
 *  <p>
 *  The syntax is simple and straight forward.  The overloaded invoke
 *  method can be used on functions (methods that return a value) or
 *  procedures (void methods that return nothing).  For methods that
 *  return a value, you specify the type of the return value as the
 *  second parameter.  If you omit the return type, then it is assumed to
 *  be "void".  You specify the name of the method as a string, and then
 *  a variable length set of arguments.
 *  </p>
 *  <p>
 *  If you are calling a method that is returning a primitive type, be
 *  sure to use the corresponding wrapper class as the expected return
 *  value:
 *  </p>
 *  <pre>
 *  // Instead of boolean answer = receiver.equals(anotherObject);
 *  Boolean answer = invoke(receiver, Boolean.class, "equals", anotherObject);

 *  // Or in an assert, using auto-unboxing:
 *  assertTrue(invoke(receiver, Boolean.class, "equals", anotherObject));
 *  </pre>
 *  <p>
 *  Any errors that occur during reflection, such as failing to find the
 *  required method, failing to find a method with the required signature,
 *  finding a method that is not public, etc., are converted into
 *  appropriate test case errors with meaningful diagnostic hints for the
 *  student.  Any exceptions are wrapped in a RuntimeException and need not
 *  be explicitly caught by the caller (they will turn into test case
 *  failures as well).
 *  </p>
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.8 $, $Date: 2012/02/29 03:57:51 $
 */
public class ReflectionSupport
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * This class only provides static methods, to the constructor is
     * private.
     */
    private ReflectionSupport()
    {
        // Nothing to do
    }


    //~ Public classes used inside this class .................................

    // ----------------------------------------------------------
    /**
     * A custom error class that represents any error conditions that
     * arise in the reflection-based methods provided by this class.
     */
    public static class ReflectionError
        extends AssertionError
    {
        private static final long serialVersionUID = 4456797064805957471L;

        ReflectionError(String message)
        {
            super(message);
        }
    }


    // ----------------------------------------------------------
    /**
     * An enumeration that represents a set of constants for specifying
     * constraints on the visibility of a declaration.
     */
    public static enum VisibilityConstraint
    {
        /** Declared with private visibility (only). */
        DECLARED_PRIVATE(Modifier.PRIVATE),
        /** Declared with package-level (default) visibility (only). */
        DECLARED_PACKAGE(
            Modifier.PRIVATE | Modifier.PROTECTED | Modifier.PUBLIC, true),
        /** Declared with protected visibility (only). */
        DECLARED_PROTECTED(Modifier.PROTECTED),
        /** Declared with public visibility (only). */
        DECLARED_PUBLIC(Modifier.PUBLIC),
        /** Declared with protected or public visibility. */
        AT_LEAST_PROTECTED(Modifier.PROTECTED | Modifier.PUBLIC),
        /** Declared with package-level (default), protected, or public
         * visibility.
         */
        AT_LEAST_PACKAGE(Modifier.PRIVATE, true),
        /** Declared with any visibility. */
        ANY_VISIBILITY(0)
        {
            public boolean accepts(int modifiers) { return true; }
        };


        // ----------------------------------------------------------
        private VisibilityConstraint(int mask)
        {
            this(mask, false);
        }


        // ----------------------------------------------------------
        private VisibilityConstraint(int mask, boolean reverseMask)
        {
            this.mask = mask;
            this.reverseMask = reverseMask;
        }


        // ----------------------------------------------------------
        /**
         * Determine if a given set of modifiers, expressed as an integer
         * mask, meets this visibility constraint.
         * @param modifiers The modifiers to check.
         * @return True if the modifiers are consistent with this constraint.
         */
        public boolean accepts(int modifiers)
        {
            boolean result = reverseMask;
            if ((mask & modifiers) != 0)
            {
                result = !result;
            }
            return result;
        }


        // ----------------------------------------------------------
        /**
         * Determine if a given member meets this visibility constraint.
         * @param member The member to check.
         * @return True if the member's visibility is consistent with this
         * constraint.
         */
        public boolean accepts(java.lang.reflect.Member member)
        {
            return accepts(member.getModifiers());
        }


        //~ Fields ............................................................

        private int mask = 0;
        private boolean reverseMask = false;
    };


    //~ Object Creation Methods ...............................................

    // ----------------------------------------------------------
    /**
     * Dynamically look up a class by name, with appropriate hints if the
     * class cannot be found.
     * @param className The type of object to create
     * @return The corresponding Class object
     */
    public static Class<?> getClassForName(String className)
    {
        for (ClassLoader loader : getCandidateLoaders())
        {
            try
            {
                return loader.loadClass(className);
            }
            catch (ClassNotFoundException e)
            {
                // Ignore it and try the next loader
            }
        }

        // Class wasn't found in any candidate loader
        fail("cannot find class " + className);

        // Unreachable, so just to make the compiler happy:
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Look up a constructor by parameter profile, finding the
     * constructor that will accept the given list of parameters (not
     * requiring an exact match on parameter types).  It turns any
     * errors into test case failures with appropriate hint messages.
     * Assumes the intended constructor should be public, and fails with an
     * appropriate hint if it is not.
     * Note that this method <b>does not handle variable argument lists</b>
     * in the target constructor for which it is searching.
     * @param c The type of object to create
     * @param params The constructor's parameter profile
     * @return The corresponding Constructor object
     */
    public static Constructor<?> getMatchingConstructor(
        Class<?> c, Class<?> ... params)
    {
        Constructor<?> result = null;
        Constructor<?> ctorWithSameParamCount = null;
        if (params == null) { params = new Class[0]; }
        for (Constructor<?> m : c.getConstructors())
        {
            Class<?>[] paramTypes = m.getParameterTypes();
            if (params.length == paramTypes.length)
            {
                ctorWithSameParamCount = m;
                result = m; // maybe ... we'll clear it if wrong
                for (int i = 0; i < params.length; i++)
                {
                    if (params[i] != null)
                    {
                        // If the actual is non-null, check to see if
                        // it can be assigned to the formal correctly.
                        if (!actualMatchesFormal(params[i], paramTypes[i]))
                        {
                            result = null;
                            break;
                        }
                    }
                    else if (paramTypes[i].isPrimitive())
                    {
                        // If actual is null, then the formal can't
                        // be a primitive
                        result = null;
                        break;
                    }
                }
                if (result != null)
                {
                    // If we found a match that can accept all the
                    // parameters  ...
                    break;
                }
            }
        }
        if (result == null)
        {
            String message = null;
            if (ctorWithSameParamCount != null)
            {
                message = "constructor cannot be called with argument"
                    + ((params.length == 1) ? "" : "s")
                    + " of type "
                    + simpleArgumentList(params)
                    + ": incorrect parameter type(s)";
            }
            else
            {
                message = "" + c + " is missing public constructor "
                    + simpleMethodName(simpleClassName(c), params);
            }
            fail(message);
        }
        else if (!Modifier.isPublic(result.getModifiers()))
        {
            fail("constructor " + simpleMethodName(simpleClassName(c), params)
                + " should be public");
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link Constructor#newInstance(Object...)}, but converts
     * any thrown exceptions into RuntimeExceptions.
     * @param constructor The constructor to invoke
     * @param params The parameters to pass to the constructor
     * @param <T> The generic parameter T is deduced from the provided
     *            constructor
     * @return The newly created object
     */
    public static <T> T create(Constructor<T> constructor, Object ... params)
    {
        T result = null;
        try
        {
            result = constructor.newInstance(params);
        }
        catch (InvocationTargetException e)
        {
            Throwable cause = e;
            while (cause.getCause() != null)
            {
                cause = cause.getCause();
            }
            throw new RuntimeException(cause);
        }
        catch (InstantiationException e)
        {
            Throwable cause = e;
            while (cause.getCause() != null)
            {
                cause = cause.getCause();
            }
            throw new RuntimeException(cause);
        }
        catch (IllegalAccessException e)
        {
            // This should never happen, since getMethod() has already
            // done the appropriate checks.
            throw new RuntimeException(e);
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Dynamically look up and invoke a class constructor for the target
     * class, with appropriate hints if any failures happen along the way.
     * @param returnType The type of object to create.
     * @param params The parameters to pass to the constructor
     * @param <T> The generic parameter T is deduced from the returnType
     * @return The newly created object
     */
    @SuppressWarnings("unchecked")
    public static <T> T create(
        Class<T> returnType,
        Object ... params)
    {
        Object result = null;
        Class<?>[] paramProfile = null;
        if (params != null)
        {
            paramProfile = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++)
            {
                if ( params[i] == null)
                {
                    // A null indicates we'll try to pass null as an
                    // actual in the getMatchingMethod() search
                    paramProfile[i] = null;
                }
                else
                {
                    paramProfile[i] = params[i].getClass();
                }
            }
        }
        Constructor<?> c = getMatchingConstructor(returnType, paramProfile);

        result = create(c, params);

        if (result != null)
        {
            assertTrue("constructor "
                + simpleMethodName(simpleClassName(returnType), paramProfile)
                + " did not produce result of type "
                + simpleClassName(returnType),
                returnType.isAssignableFrom(result.getClass()));
        }
        // The cast below is technically unsafe, according to the compiler,
        // but will never be violated, due to the assertion above.
        return (T)result;
    }


    // ----------------------------------------------------------
    /**
     * Dynamically look up and invoke a class constructor for the target
     * class, with appropriate hints if any failures happen along the way.
     * @param className The type of object to create
     * @param params The parameters to pass to the constructor
     * @return The newly created object
     */
    public static Object create(String className, Object ... params)
    {
        return create(getClassForName(className), params);
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link #create(Constructor, Object...)}, but unwraps
     * any InvocationTargetExceptions and throws the true cause.  This
     * version is provided when you want to write test cases where you
     * are intending to check for Exceptions as expected results.
     * @param constructor The constructor to invoke
     * @param params The parameters to pass to the constructor
     * @return The newly created object
     * @throws Exception if the underlying method throws one
     */
    public static Object createEx(Constructor<?> constructor, Object ... params)
        throws Exception
    {
        Object result = null;
        try
        {
            result = constructor.newInstance(params);
        }
        catch (InvocationTargetException e)
        {
            Throwable cause = e;
            Exception ex = null;
            if (cause instanceof Exception)
            {
                ex = (Exception)cause;
            }
            while (cause.getCause() != null)
            {
                cause = cause.getCause();
                if (cause instanceof Exception)
                {
                    ex = (Exception)cause;
                }
            }
            if (ex != null)
            {
                throw ex;
            }
            else
            {
                // the cause is a raw Throwable of some kind, rather than
                // an Exception, so it needs to be wrapped anyway
                throw new RuntimeException(cause);
            }
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link #create(Class, Object...)}, but unwraps
     * any InvocationTargetExceptions and throws the true cause.  This
     * version is provided when you want to write test cases where you
     * are intending to check for Exceptions as expected results.
     * @param returnType The type of object to create.
     * @param params The parameters to pass to the constructor
     * @param <T> The generic parameter T is deduced from the returnType
     * @return The newly created object
     * @throws Exception if the underlying method throws one
     */
    @SuppressWarnings("unchecked")
    public static <T> T createEx(
        Class<T> returnType,
        Object ... params)
        throws Exception
    {
        Object result = null;
        Class<?>[] paramProfile = null;
        if (params != null)
        {
            paramProfile = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++)
            {
                if ( params[i] == null)
                {
                    // A null indicates we'll try to pass null as an
                    // actual in the getMatchingMethod() search
                    paramProfile[i] = null;
                }
                else
                {
                    paramProfile[i] = params[i].getClass();
                }
            }
        }
        Constructor<?> c = getMatchingConstructor(returnType, paramProfile);

        result = createEx(c, params);

        if (result != null)
        {
            assertTrue("constructor "
                + simpleMethodName(simpleClassName(returnType), paramProfile)
                + " did not produce result of type "
                + simpleClassName(returnType),
                returnType.isAssignableFrom(result.getClass()));
        }
        // The cast below is technically unsafe, according to the compiler,
        // but will never be violated, due to the assertion above.
        return (T)result;
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link #create(String, Object...)}, but unwraps
     * any InvocationTargetExceptions and throws the true cause.  This
     * version is provided when you want to write test cases where you
     * are intending to check for Exceptions as expected results.
     * @param className The type of object to create
     * @param params The parameters to pass to the constructor
     * @return The newly created object
     * @throws Exception if the underlying method throws one
     */
    public static Object createEx(String className, Object ... params)
        throws Exception
    {
        return createEx(getClassForName(className), params);
    }


    //~ Method Invocation Methods .............................................

    // ----------------------------------------------------------
    /**
     * Look up a method by name and parameter profile, turning any
     * errors into test case failures with appropriate hint messages.
     * Only looks up methods that are declared in the specified class,
     * not inherited methods.  Assumes the intended method should be
     * public, and fails with an appropriate hint if it is not.
     * @param c The type of the receiver
     * @param name The method name
     * @param params The method's parameter profile
     * @return The corresponding Method object
     */
    public static Method getMethod(
        Class<?> c, String name, Class<?> ... params)
    {
        Method m = null;
        try
        {
            m = c.getDeclaredMethod(name, params);
        }
        catch (NoSuchMethodException e)
        {
            String message = c + " is missing method "
                + simpleMethodName(name, params);
            fail(message);
        }
        catch (SecurityException e)
        {
            String message = "method " + simpleMethodName(name, params)
                + " in " + c + " cannot be accessed (should be public)";
            fail(message);
        }
        if (m != null && !Modifier.isPublic(m.getModifiers()))
        {
            fail(simpleMethodName(m) + " should be public");
        }
        return m;
    }


    // ----------------------------------------------------------
    /**
     * Look up a method by name and parameter profile, finding the
     * method that will accept the given list of parameters (not requiring
     * an exact match on parameter types).  It turns any
     * errors into test case failures with appropriate hint messages.
     * Only looks up methods that are declared in the specified class,
     * not inherited methods.  Assumes the intended method should be
     * public, and fails with an appropriate hint if it is not.
     * Note that this method <b>does not handle variable argument lists</b>
     * in the target method for which it is searching.
     * @param c The type of the receiver
     * @param name The method name
     * @param params The method's parameter profile
     * @return The corresponding Method object
     */
    public static Method getMatchingMethod(
        Class<?> c, String name, Class<?> ... params)
    {
        Method result = null;
        Method methodWithSameName = null;
        Method methodWithSameParamCount = null;
        if (params == null) { params = new Class[0]; }
        for (Method m : c.getMethods())
        {
            if (m.getName().equals(name))
            {
                methodWithSameName = m;
                Class<?>[] paramTypes = m.getParameterTypes();
                if (params.length == paramTypes.length)
                {
                    methodWithSameParamCount = m;
                    result = m; // maybe ... we'll clear it if wrong
                    for (int i = 0; i < params.length; i++)
                    {
                        if (params[i] != null)
                        {
                            // If the actual is non-null, check to see if
                            // it can be assigned to the formal correctly.
                            if (!actualMatchesFormal(params[i], paramTypes[i]))
                            {
                                result = null;
                                break;
                            }
                        }
                        else if (paramTypes[i].isPrimitive())
                        {
                            // If actual is null, then the formal can't
                            // be a primitive
                            result = null;
                            break;
                        }
                    }
                    if (result != null)
                    {
                        // If we found a match that can accept all the
                        // parameters  ...
                        break;
                    }
                }
            }
        }
        if (result == null)
        {
            String message = null;
            if (methodWithSameParamCount != null)
            {
                message = simpleMethodName(methodWithSameParamCount)
                    + " cannot be called with argument"
                    + ((params.length == 1) ? "" : "s")
                    + " of type "
                    + simpleArgumentList(params)
                    + ": incorrect parameter type(s)";
            }
            else if (methodWithSameName != null)
            {
                message = simpleMethodName(methodWithSameName);
                if (params.length == 0)
                {
                    message += " cannot be called with no arguments";
                }
                else
                {
                    message += " cannot be called with argument"
                        + ((params.length == 1) ? "" : "s")
                        + " of type "
                        + simpleArgumentList(params);
                }
                message += ": incorrect number of parameters";
            }
            else
            {
                message = "" + c + " is missing public method "
                    + simpleMethodName(name, params);
            }
            fail(message);
        }
        else if (!Modifier.isPublic(result.getModifiers()))
        {
            fail(simpleMethodName(result) + " should be public");
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Dynamically look up and invoke a method on a target object, with
     * appropriate hints if any failures happen along the way.
     * @param receiver The object to invoke the method on
     * @param returnType The expected type of the method's return value.
     *     Use null (or <code>void.class</code>) if the method that is
     *     looked up is a void method.
     * @param methodName The name of the method to invoke
     * @param params The parameters to pass to the method
     * @param <T> The generic parameter T is deduced from the returnType
     * @return The results from invoking the given method
     */
    @SuppressWarnings("unchecked")
    public static <T> T invoke(
        Object receiver,
        Class<T> returnType,
        String methodName,
        Object ... params)
    {
        Object result = null;
        Class<?> targetClass = receiver.getClass();
        Class<?>[] paramProfile = null;
        if (params != null)
        {
            paramProfile = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++)
            {
                if ( params[i] == null)
                {
                    // A null indicates we'll try to pass null as an
                    // actual in the getMatchingMethod() search
                    paramProfile[i] = null;
                }
                else
                {
                    paramProfile[i] = params[i].getClass();
                }
            }
        }
        Method m = getMatchingMethod(targetClass, methodName, paramProfile);

        if (returnType == null || returnType == void.class)
        {
            Class<?> declaredReturnType = m.getReturnType();
            assertTrue("method " + simpleMethodName(m)
                + " should be a void method",
                declaredReturnType == void.class ||
                declaredReturnType == null);
        }
        else
        {
            Class<?> declaredReturnType = m.getReturnType();
            assertTrue("method " + simpleMethodName(m)
                + " should be declared with a return type of "
                + simpleClassNameUsingPrimitives(returnType),
                declaredReturnType != void.class &&
                declaredReturnType != null &&
                (actualMatchesFormal(declaredReturnType, returnType)
                    // Had to add this second part in for legacy compatibility,
                    // where tests written with Integer.class need to
                    // work, even though they should have been written
                    // with int.class
                 || canAutoBoxFromActualToFormal(
                     returnType, declaredReturnType)));
        }

        result = invoke(receiver, m, params);

        if (result != null)
        {
            if (returnType != null)
            {
                assertTrue("method " + simpleMethodName(m)
                    + " should be a void method",
                    returnType != void.class);
                assertTrue("method " + simpleMethodName(m)
                    + " did not produce result of type "
                    + simpleClassName(returnType),
                    actualMatchesFormal(result.getClass(), returnType));
            }
            else
            {
                fail("method " + simpleMethodName(m)
                    + " should be a void method");
            }
        }
        // The cast below is technically unsafe, according to the compiler,
        // but will never be violated, due to the assertion above.
        return (T)result;
    }


    // ----------------------------------------------------------
    /**
     * Dynamically look up and invoke a method on a target object, with
     * appropriate hints if any failures happen along the way.  This
     * version is intended for calling "void" methods that have no
     * return value.
     * @param receiver The object to invoke the method on
     * @param methodName The name of the method to invoke
     * @param params The parameters to pass to the method
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
     * Just like {@link Method#invoke(Object, Object...)}, but converts
     * any thrown exceptions into RuntimeExceptions.
     * @param receiver The object to invoke the method on
     * @param method The method to invoke
     * @param params The parameters to pass to the method
     * @return The result from the method
     */
    public static Object invoke(
        Object receiver, Method method, Object ... params)
    {
        Object result = null;
        try
        {
            result = method.invoke(receiver, params);
        }
        catch (InvocationTargetException e)
        {
            Throwable cause = e;
            while (cause.getCause() != null)
            {
                cause = cause.getCause();
            }

            if (cause instanceof Error)
            {
                throw (Error)cause;
            }
            else if (cause instanceof RuntimeException)
            {
                throw (RuntimeException)cause;
            }
            else
            {
                throw new RuntimeException(cause);
            }
        }
        catch (IllegalAccessException e)
        {
            // This should never happen, since getMethod() has already
            // done the appropriate checks.
            throw new RuntimeException(e);
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link #invoke(Object, Class, String, Object...)}, but unwraps
     * any InvocationTargetExceptions and throws the true cause.  This
     * version is provided when you want to write test cases where you
     * are intending to check for Exceptions as expected results.
     * @param receiver The object to invoke the method on
     * @param returnType The expected type of the method's return value.
     *     Use null (or <code>void.class</code>) if the method that is
     *     looked up is a void method.
     * @param methodName The name of the method to invoke
     * @param params The parameters to pass to the method
     * @param <T> The generic parameter T is deduced from the returnType
     * @return The results from invoking the given method
     * @throws Exception if the underlying method throws one
     */
    @SuppressWarnings("unchecked")
    public static <T> T invokeEx(
        Object receiver,
        Class<T> returnType,
        String methodName,
        Object ... params)
        throws Exception
    {
        Object result = null;
        Class<?> targetClass = receiver.getClass();
        Class<?>[] paramProfile = null;
        if (params != null)
        {
            paramProfile = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++)
            {
                if ( params[i] == null)
                {
                    // A null indicates we'll try to pass null as an
                    // actual in the getMatchingMethod() search
                    paramProfile[i] = null;
                }
                else
                {
                    paramProfile[i] = params[i].getClass();
                }
            }
        }
        Method m = getMatchingMethod(targetClass, methodName, paramProfile);

        if (returnType == null || returnType == void.class)
        {
            Class<?> declaredReturnType = m.getReturnType();
            assertTrue("method " + simpleMethodName(m)
                + " should be a void method",
                declaredReturnType == void.class ||
                declaredReturnType == null);
        }
        else
        {
            Class<?> declaredReturnType = m.getReturnType();
            assertTrue("method " + simpleMethodName(m)
                + " should be declared with a return type of "
                + simpleClassNameUsingPrimitives(returnType),
                declaredReturnType != void.class &&
                declaredReturnType != null &&
                (actualMatchesFormal(declaredReturnType, returnType)
                    // Had to add this second part in for legacy compatibility,
                    // where tests written with Integer.class need to
                    // work, even though they should have been written
                    // with int.class
                 || canAutoBoxFromActualToFormal(
                     returnType, declaredReturnType)));
        }

        result = invokeEx(receiver, m, params);

        if (result != null)
        {
            if (returnType != null)
            {
                assertTrue("method " + simpleMethodName(m)
                    + " should be a void method",
                    returnType != void.class);
                assertTrue("method " + simpleMethodName(m)
                    + " did not produce result of type "
                    + simpleClassName(returnType),
                    returnType.isAssignableFrom(result.getClass()));
            }
            else
            {
                fail("method " + simpleMethodName(m)
                    + " should be a void method");
            }
        }
        // The cast below is technically unsafe, according to the compiler,
        // but will never be violated, due to the assertion above.
        return (T)result;
    }


    // ----------------------------------------------------------
    /**
     * Just like {@link #invoke(Object, String, Object...)}, but unwraps
     * any InvocationTargetExceptions and throws the true cause.  This
     * version is provided when you want to write test cases where you
     * are intending to check for Exceptions as expected results.
     * @param receiver The object to invoke the method on
     * @param methodName The name of the method to invoke
     * @param params The parameters to pass to the method
     * @throws Exception if the underlying method throws one
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
     * Just like {@link Method#invoke(Object, Object...)}, but unwraps
     * any InvocationTargetExceptions and throws the true cause.  This
     * version is provided when you want to write test cases where you
     * are intending to check for Exceptions as expected results.
     * @param receiver The object to invoke the method on
     * @param method The method to invoke
     * @param params The parameters to pass to the method
     * @return The result from the method
     * @throws Exception if the underlying method throws one
     */
    public static Object invokeEx(
        Object receiver, Method method, Object ... params)
        throws Exception
    {
        Object result = null;
        try
        {
            result = method.invoke(receiver, params);
        }
        catch (InvocationTargetException e)
        {
            Throwable cause = e;
            Exception ex = null;
            Error     error = null;
            if (cause instanceof Exception)
            {
                ex = (Exception)cause;
            }
            else if (cause instanceof Error)
            {
                error = (Error)cause;
            }
            while (cause.getCause() != null)
            {
                cause = cause.getCause();
                if (cause instanceof Exception)
                {
                    ex = (Exception)cause;
                }
                else if (cause instanceof Error)
                {
                    error = (Error)cause;
                }
            }
            if (error != null)
            {
                throw error;
            }
            else if (ex != null)
            {
                throw ex;
            }
            else
            {
                // the cause is a raw Throwable of some kind, rather than
                // an Exception, so it needs to be wrapped anyway
                throw new RuntimeException(cause);
            }
        }
        return result;
    }


    //~ Field Manipulation Methods ............................................

    // ----------------------------------------------------------
    /**
     * Look up a field by name, receiver class, and type, finding the
     * field that will accept the given type (not requiring
     * an exact match on type).  Any errors are thrown as instances of
     * {@link ReflectionError}.
     * It looks up fields that are declared in the specified class,
     * as well as inherited classes.  It sets up accessibility if the field
     * is not public.
     * @param receiverClass The class of the receiver
     * @param type The type of this field
     * @param fieldName The name of the field
     * @return The corresponding Field
     */
    public static Field getField(
        Class<?> receiverClass, Class<?> type, String fieldName)
    {
        Field field = null;
        Class<?> declaringClass = receiverClass;
        // TODO: This approach will not find public fields declared in
        //       implemented interfaces.
        while (field == null && declaringClass != null)
        {
            try
            {
                field = declaringClass.getDeclaredField(fieldName);
            }
            catch (NoSuchFieldException e)
            {
               // check parent class
               declaringClass = declaringClass.getSuperclass();
            }
        }

        if (field == null)
        {
            fail("Cannot find field " + fieldName
                + " in " + simpleClassName(receiverClass));
        }
        if (!actualMatchesFormal(field.getType(), type))
        {
            String msg = "Field " + fieldName + " in class ";
            if (declaringClass == receiverClass)
            {
                msg += simpleClassName(declaringClass);
            }
            else
            {
                msg += simpleClassName(receiverClass) + " (inherited from "
                    + simpleClassName(declaringClass) + ")";
            }
            fail(msg + " is declared of type "
                + simpleClassName(field.getType())
                + ", not "
                + simpleClassName(type)
                + ".");
        }

        //check and set access permission
        if (!field.isAccessible())
        {
            final Field theField = field;
            AccessController.doPrivileged( new PrivilegedAction<Void>()
                {
                    public Void run()
                    {
                        theField.setAccessible(true);
                        return null;
                    }
                });
        }
        return field;
    }


    //------------------------------------------------------------------
    /**
     * Get the value of a field. The field is looked up a field by name,
     * receiver object and type, finding the field that will accept the
     * given type (not requiring an exact match on type).  It turns any
     * errors into ReflectionErrors with appropriate hint messages.
     * @param receiver The object containing the field
     * @param type The type of this field
     * @param fieldName The name of the field
     * @param <T> The generic parameter T is deduced from the returnType
     * @return The value corresponding Field
     */
    public static <T> T get(Object receiver, Class<T> type, String fieldName )
    {
        assert fieldName != null    : "fieldName cannot be null";
        assert !fieldName.isEmpty() : "fieldName cannot be empty";
        assert receiver != null     : "Receiver object cannot be empty";

        Field field = getField(receiver.getClass(), type, fieldName);

        Object fieldValue = null;
        if (field != null)
        {
            try
            {
                fieldValue = field.get(receiver);
            }
            catch (IllegalArgumentException e)
            {
                fail(field.getName() + " in "
                    + simpleClassName(receiver.getClass())
                    + " cannot be retrieved.");
            }
            catch (IllegalAccessException e)
            {
                // Shouldn't happen, since setAccessible() was called,
                // but the compiler requires a handler.
                fail(e.getMessage());
            }

        }
        @SuppressWarnings("unchecked")
        T value = (T)fieldValue;
        return value;
    }


    //------------------------------------------------------------------
    /**
     * Get the value of a field. The field is looked up a field by name,
     * receiver object and type, finding the field that will accept the
     * given type (not requiring an exact match on type).  It turns any
     * errors into ReflectionErrors with appropriate hint messages.
     * Note that the field must be a static field.
     * @param receiverClass The class of the receiver
     * @param type The type of this field
     * @param fieldName The name of the field
     * @param <T> The generic parameter T is deduced from the returnType
     * @return The value corresponding Field
     */
    public static <T> T get(
        Class<?> receiverClass, Class<T> type, String fieldName)
    {
        assert fieldName != null     : "fieldName cannot be null";
        assert !fieldName.isEmpty()  : "fieldName cannot be empty";
        assert receiverClass != null : "Receiver object cannot be empty";

        Field field = getField(receiverClass, type, fieldName);

        Object fieldValue = null;

        if (field != null)
        {
            try
            {
                fieldValue = field.get(null);
            }
            catch (IllegalArgumentException e)
            {
                fail(field.getName() + " in "
                    + simpleClassName(receiverClass)
                    + " cannot be retrieved.");
            }
            catch (IllegalAccessException e)
            {
                // Shouldn't happen, since setAccessible() was called,
                // but the compiler requires a handler.
                fail(e.getMessage());
            }
        }
        @SuppressWarnings("unchecked")
        T value = (T)fieldValue;
        return value;
    }


    //------------------------------------------------------------------
    /**
     * Sets value of a field.
     * @param receiver The object of the receiver
     * @param fieldName The name of the field
     * @param value The value to set in the field
     */
    public static void set(Object receiver, String fieldName, Object value)
    {
        assert fieldName != null    : "fieldName cannot be null";
        assert !fieldName.isEmpty() : "fieldName cannot be empty";
        assert receiver != null     : "Receiver object cannot be empty";

        Field field =
            getField(receiver.getClass(), value.getClass(), fieldName);
        if (field != null)
        {
            try
            {
                field.set(receiver, value);
            }
            catch (IllegalArgumentException e)
            {
                fail(field.getName() +" of type "
                    + simpleClassName(field.getType())
                    +" cannot be assigned a value of "
                    + value
                    + ((value == null)
                        ? ""
                        : "(of type "
                            + simpleClassName(value.getClass())
                            + ")")
                    + ".");
            }
            catch (IllegalAccessException e)
            {
                // Shouldn't happen, since setAccessible() was called,
                // but the compiler requires a handler.
                fail(e.getMessage());
            }
        }
    }


    //------------------------------------------------------------------
    /**
     * Sets value of a field.
     * @param receiverClass The class of the receiver
     * @param fieldName The name of the field
     * @param value The value will be set in the field
     */
    public static void set(
        Class<?> receiverClass, String fieldName,  Object value)
    {
        assert fieldName != null     : "fieldName cannot be null";
        assert !fieldName.isEmpty()  : "fieldName cannot be empty";
        assert receiverClass != null : "Receiver object cannot be empty";

        Field field = getField(receiverClass, value.getClass(), fieldName);
        if (field != null)
        {
            try
            {
                field.set(null, value);
            }
            catch (IllegalArgumentException e)
            {
                fail(field.getName() +" of type "
                    + simpleClassName(field.getType())
                    +" cannot be assigned a value of "
                    + value
                    + ((value == null)
                        ? ""
                        : "(of type "
                            + simpleClassName(value.getClass())
                            + ")")
                    + ".");
            }
            catch (IllegalAccessException e)
            {
                // Shouldn't happen, since setAccessible() was called,
                // but the compiler requires a handler.
                fail(e.getMessage());
            }
        }
    }


    //~ Public Utility Methods ................................................

    //  simple printing methods ---------------------------------

    // ----------------------------------------------------------
    /**
     * Returns the name of the given class without any package prefix.
     * If the argument is an array type, square brackets are added to
     * the name as appropriate.  This method isuseful in generating
     * diagnostic messages or feedback.
     * @param aClass The class to generate a name for
     * @return The class' name, without the package part, e.g., "String"
     *     instead of "java.lang.String"
     */
    public static String simpleClassName(Class<?> aClass)
    {
        if (aClass == null) return "null";
        String result = aClass.getName();


        // If it is an array, add appropriate number of brackets
        try
        {
            Class<?> cl = aClass;
            while (cl.isArray())
            {
                result += "[]";
                cl = cl.getComponentType();
            }
        }
        catch (Throwable e)
        {
            // Swallow it and stick with the bare class name
        }

        int pos = result.lastIndexOf('.');
        if (pos >= 0)
        {
            result = result.substring(pos + 1);
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Returns the name of the given class without any package prefix.
     * If the argument is an array type, square brackets are added to
     * the name as appropriate.  This method is useful in generating
     * diagnostic messages or feedback.
     * @param aClass The class to generate a name for
     * @return The class' name, without the package part, e.g., "String"
     *     instead of "java.lang.String"
     */
    public static String simpleClassNameUsingPrimitives(Class<?> aClass)
    {
        if (aClass == Boolean.class)
        {
            aClass = Boolean.TYPE;
        }
        else if (aClass == Byte.class)
        {
            aClass = Byte.TYPE;
        }
        else if (aClass == Character.class)
        {
            aClass = Character.TYPE;
        }
        else if (aClass == Short.class)
        {
            aClass = Short.TYPE;
        }
        else if (aClass == Integer.class)
        {
            aClass = Integer.TYPE;
        }
        else if (aClass == Long.class)
        {
            aClass = Long.TYPE;
        }
        else if (aClass == Float.class)
        {
            aClass = Float.TYPE;
        }
        else if (aClass == Double.class)
        {
            aClass = Double.TYPE;
        }
        return simpleClassName(aClass);
    }


    // ----------------------------------------------------------
    /**
     * Constructs a printable version of a method's name, given
     * the method name and its parameter type(s), if any.
     * Useful in generating diagnostic messages or feedback.
     * @param name   The method name
     * @param params The method's parameter type(s), in order
     * @return A printable version of the method name, like
     *     "myMethod()" or "yourMethod(String, int)"
     */
    public static String simpleMethodName(String name, Class<?> ... params)
    {
        return name + simpleArgumentList(params);
    }


    // ----------------------------------------------------------
    /**
     * Constructs a printable version of a method's argument list, including
     * the parentheses, given the method's parameter type(s), if any.
     * @param params The method's parameter type(s), in order
     * @return A printable version of the argument list built using
     *     {@link #simpleClassName(Class)}, like "(String, int)"
     */
    public static String simpleArgumentList(Class<?> ... params)
    {
        String result = "(";
        boolean needsComma = false;
        for (Class<?> c : params)
        {
            if (needsComma)
            {
                result += ", ";
            }
            if (c == null)
            {
                result += "null";
            }
            else
            {
                result += simpleClassName(c);
            }
            needsComma = true;
        }
        result += ")";
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Constructs a printable version of a method's name.  Unlike
     * {@link Method#toString()}, this one uses {@link #simpleClassName(Class)}
     * so package info is eliminated from the types in the resulting
     * string.  It also omits exception information, unlike Method.toString().
     * @param method The method to print
     * @return A printable version of the method name, like
     *     "public void MyClass.myMethod()" or
     *     "public String YourClass.yourMethod(String, int)"
     */
    public static String simpleMethodName(Method method)
    {
        StringBuffer sb = new StringBuffer();
        int mod = method.getModifiers();
        if (mod != 0)
        {
            sb.append(Modifier.toString(mod) + " ");
        }
        sb.append(simpleClassName(method.getReturnType()) + " ");
        sb.append(method.getName());
        sb.append(simpleArgumentList(method.getParameterTypes()));
        sb.append(" in class ");
        sb.append(simpleClassName(method.getDeclaringClass()));
        return sb.toString();
    }


    // argument matching methods --------------------------------

    // ----------------------------------------------------------
    /**
     * Determine whether an actual argument type matches a formal argument
     * type.  This uses {@link Class#isAssignableFrom(Class)}, but gives
     * the correct results for primitive types vs. wrapper types.
     * @param actual The type of the actual parameter
     * @param formal The type of the formal parameter
     * @return True if the actual value can be passed into a parameter
     *    declared using the formal type
     */
    public static boolean actualMatchesFormal(Class<?> actual, Class<?> formal)
    {
        boolean result = formal.isAssignableFrom(actual);
        if (!result)
        {
            result = canAutoBoxFromActualToFormal(actual, formal);
        }
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Determine whether it is appropriate to attempt to "auto-box" an
     * actual argument of type <code>actual</code> into a formal parameter
     * type <code>formal</code>.
     * @param actual The type of the actual value.
     * @param formal The type of the formal parameter.
     * @return True if it is appropriate to auto-box the actual into the
     * type of the formal.
     */
    public static boolean canAutoBoxFromActualToFormal(
        Class<?> actual, Class<?> formal)
    {
        return
            ( (    formal.equals(byte.class)
                || formal.equals(short.class)
                || formal.equals(int.class)
                || formal.equals(long.class)
                || formal.equals(float.class)
                || formal.equals(double.class) )
              && Number.class.isAssignableFrom(actual) )
            || ( formal.equals(boolean.class)
                 && actual.equals(Boolean.class) )
            || ( formal.equals(char.class)
                 && actual.equals(Character.class) );
    }


    //~ Private Utility Methods ...............................................

    //-----------------------------------------------------------------
    /**
     * Throws a ReflectionError with the given message if the given
     * condition is not true.
     * @param message The message will be used to create ReflectionError
     * @param condition The condition to check
     */
    private static void assertTrue(String message, boolean condition)
    {
        if (!condition)
        {
            fail(message, 2);
        }
    }


    //-----------------------------------------------------------------
    /**
     * Throws a ReflectionError with the given message.
     * @param message The message will be used to create ReflectionError
     */
    private static void fail(String message)
    {
        fail(message, 2);
    }


    //-----------------------------------------------------------------
    /**
     * Throws a ReflectionError with the given message.
     * @param message The message will be used to create ReflectionError
     * @param stackFramesToStrip The number of levels to strip from the
     * top of the stack frame (e.g., 1 will strip the call to fail() itself).
     */
    private static void fail(String message, int stackFramesToStrip)
    {
        ReflectionError error = new ReflectionError(message);
        StackTraceElement[] trace = error.getStackTrace();
        // remove the call to fail() from the stack trace
        error.setStackTrace(java.util.Arrays.copyOfRange(
            trace, stackFramesToStrip, trace.length));
        throw error;
    }


    //-----------------------------------------------------------------
    /**
     * Return an array of potential classloaders to use to look up classes.
     * This array contains three loaders, including the loader
     * used to load ReflectionSupport itself, the current thread's
     * context classloader, and the classloader used to load the nearest
     * non-library class in the method calling sequence.  These loaders
     * are arranged in order from most-specific to least-specific,
     * in terms of delegation (i.e., if any ancestor/descendant delegation
     * relationships exist among the loaders, the descendant appears before
     * the ancestor in the returned array).
     */
    private static ClassLoader[] getCandidateLoaders()
    {
        ClassLoader[] result = new ClassLoader[] {
            RESOLVER.getNonlibraryCallerClass().getClassLoader(),
            Thread.currentThread().getContextClassLoader(),
            ReflectionSupport.class.getClassLoader()
        };

        // Sort them in most-specific-to-least-specific order
        // using a simple bubble sort (augh!!)
        boolean moved = false;
        if (hasAsChild(result[1], result[2]))
        {
            ClassLoader temp = result[2];
            result[2] = result[1];
            result[1] = temp;
            moved = true;
        }
        if (hasAsChild(result[0], result[1]))
        {
            ClassLoader temp = result[1];
            result[1] = result[0];
            result[0] = temp;
            moved = true;
        }
        if (moved)
        {
            if (hasAsChild(result[1], result[2]))
            {
                ClassLoader temp = result[2];
                result[2] = result[1];
                result[1] = temp;
            }
        }
        else
        {
            if (hasAsChild(result[0], result[2]))
            {
                ClassLoader temp = result[2];
                result[2] = result[0];
                result[0] = temp;
            }
        }
        return result;
    }


    //-----------------------------------------------------------------
    /**
     * Returns true if 'loader2' is a delegation child of 'loader1' or if
     * 'loader1' == 'loader2'. Of course, this works only for classloaders that
     * set their parent pointers correctly. 'null' is interpreted as the
     * primordial loader (i.e., everybody's parent).
     */
    private static boolean hasAsChild(ClassLoader loader1, ClassLoader loader2)
    {
        if (loader1 == loader2)
        {
            return true;
        }
        if (loader2 == null)
        {
            return false;
        }
        if (loader1 == null)
        {
            return true;
        }

        for ( ; loader2 != null; loader2 = loader2.getParent())
        {
            if (loader2 == loader1)
            {
                return true;
            }
        }

        return false;
    }


    //-----------------------------------------------------------------
    /**
     * This interface represents a strategy for finding the nearest
     * non-library class in the calling sequence.
     */
    private static interface CallerResolver
    {
        Class<?> getNonlibraryCallerClass();
    }


    //-----------------------------------------------------------------
    /**
     * A stock implementation of CallerResolver that uses features from
     * the SecurityManager class to get access to the declaring classes of
     * methods on the call stack.  Note that this class need NOT be installed
     * as the current security manager at all (and shouldn't be).  It
     * simply uses inherited features to implement the CallerResolver
     * interface.
     */
    private static class SecurityManagerCallerResolver
        extends SecurityManager
        implements CallerResolver
    {
        public Class<?> getNonlibraryCallerClass()
        {
            Class<?>[] stack = getClassContext();
            for (Class<?> c : getClassContext())
            {
                boolean isClientClass = true;
                String name = c.getName();
                for (String prefix : STACK_FILTERS)
                {
                    if (name.startsWith(prefix))
                    {
                        isClientClass = false;
                    }
                    break;
                }
                if (isClientClass)
                {
                    return c;
                }
            }
            if (stack.length > 0)
            {
                // If no non-library classes were found, return the bottom
                // of stack
                return stack[stack.length - 1];
            }
            else
            {
                // If there's no stack (!), just return this class
                return ReflectionSupport.class;
            }
        }
    }


    //~ Private Fields ........................................................

    private static /*final*/ CallerResolver RESOLVER;

    static
    {
        try
        {
            // this can fail if the current SecurityManager does not allow
            // RuntimePermission ("createSecurityManager"):

            AccessController.doPrivileged( new PrivilegedAction<Void>()
                {
                    public Void run()
                    {
                        RESOLVER = new SecurityManagerCallerResolver();
                        return null;
                    }
                });
        }
        catch (SecurityException e)
        {
            System.out.println("Warning: " + ReflectionSupport.class
                + " could not create CallerResolver:");
            e.printStackTrace();
            RESOLVER = new CallerResolver()
            {
                public java.lang.Class<?> getNonlibraryCallerClass()
                {
                    return ReflectionSupport.class;
                };
            };
        }
    }


    private static final String[] STACK_FILTERS = {
        "student.",
        "cs1705.",
        // JUnit 4 support:
        "org.junit.",
        // JUnit 3 support:
        "junit.",
        "java.",
        "sun.",
        "org.apache.tools.ant.",
        // Web-CAT infrastructure
        "net.sf.webcat."
    };

}
