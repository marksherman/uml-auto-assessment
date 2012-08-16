/*==========================================================================*\
 |  $Id: Type.java,v 1.5 2011/06/09 15:31:24 stedwar2 Exp $
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import student.testingsupport.reflection.internal.Types;

//-------------------------------------------------------------------------
/**
 *  TODO: document.
 *
 *  TODO: add annotation support.
 *
 *  @param <ClassType> If present, this is a constraint on the type that
 *  this object represents.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.5 $, $Date: 2011/06/09 15:31:24 $
 */
public class Type<ClassType>
    extends NameFilter<Type<ClassType>, Class<ClassType>>
{
    //~ Fields ................................................................

    private static final List<Class<?>> PRIMITIVE_TYPES = Arrays.asList(
        new Class<?>[] {
            boolean.class,
            byte.class,
            short.class,
            char.class,
            int.class,
            long.class,
            float.class,
            double.class
        });


    private static final List<Class<?>> WRAPPER_TYPES = Arrays.asList(
        new Class<?>[] {
            Boolean.class,
            Byte.class,
            Short.class,
            Character.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class
        });


    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create a new Type object that represents a type filter.
     * @param previous The previous filter in the chain of filters.
     * @param descriptionOfThisStage A description of this stage in the
     * filter chain.
     */
    protected Type(Type<ClassType> previous, String descriptionOfThisStage)
    {
        super(previous, descriptionOfThisStage);
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * A starting point for type filters where the name is unspecified.
     */
    public static final Type<?> type =
        new Type<Object>(null, null).typeResolver();


    // ----------------------------------------------------------
    /**
     * Create a new Type object.
     * @param name The name of the type (class).
     * @return a new Type filter object representing the named field.
     */
    public static Type<?> type(String name)
    {
        return type.withName(name);
    }


    // ----------------------------------------------------------
    /**
     * Create a new Type object from a {@link Class} object.
     * @param aClass The class to represent.
     * @param <T> This type is deduced from aClass.
     * @return a new Type object representing the given class.
     */
    public static <T> Type<T> type(Class<T> aClass)
    {
        if (aClass == null)
        {
            @SuppressWarnings("unchecked")
            Type<T> result = (Type<T>)type;
            return result;
        }
        @SuppressWarnings("unchecked")
        Type<T> result = ((Type<T>)type).createFreshFilter(aClass);
        return result;
    }


    // ----------------------------------------------------------
    /**
     * Restrict type searches that look for all classes in a package, or
     * all classes visible to a class loader, to the specified list of
     * search locations (the default is the search path in the system
     * property
     * <code>student.testingsupport.reflection.internal.Types.searchPath</code>,
     * or the set of directories visible from the class loader where
     * this class was loaded).
     * This restriction affects all type searches when a fully qualified
     * class name is not provided.
     * @param searchPath The search path to use.  A classpath-like string
     * denoting the classpath locations where searching should be performed.
     */
    public static void restrictSearchesTo(String searchPath)
    {
        type.flush();
        Types.restrictSearchesTo(searchPath);
    }

    // ----------------------------------------------------------
    /**
     * Force type searches to search all possible locations (i.e., remove
     * the default initial restrictions, which limit searches either to just
     * directories (not jar files) or to the locations specified in the
     * system property
     * <code>student.testingsupport.reflection.internal.Types.searchPath</code>).
     * Note that in some instances this may cause some type filters to
     * exhaust available PermGen space, requiring programs to be run
     * with an additional JVM command line argument to increase the max
     * PermGen size.
     */
    public static void searchEverywhere()
    {
        Types.restrictSearchesTo(null);
    }

    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations with the specified
     * name.  If the name is a simple name (that is,
     * @param name The name required by the resulting filter.
     * @return A new filter with the given restriction.
     */
    public Type<ClassType> withName(String name)
    {
        if (name == null)
        {
            return this;
        }
        int pos = name.lastIndexOf('.');
        if (pos > 0)
        {
            final String simpleName = name.substring(pos + 1);
            return (new Type<ClassType>(this, "with name " + simpleName)
            {
                @Override
                protected String getTargetSimpleName()
                {
                    return simpleName;
                }
            }).inPackage(name.substring(0, pos));
        }
        else
        {
            final String simpleName = name;
            return (new Type<ClassType>(this, "with name " + simpleName)
            {
                @Override
                protected String getTargetSimpleName()
                {
                    return simpleName;
                }
            }).typeResolver();
        }
    }


    // ----------------------------------------------------------
    /**
     * Get a compile-time-constrained version of this Type that is limited
     * to only types that are compatible with the specified class.
     * @param superClass The constraint to add to this type.
     * @param <T> The generic parameter T is deduced from the superClass.
     * @return A constrained version of this type that represents a
     *         subclass of the given superClass.
     */
    @SuppressWarnings("unchecked")
    public <T> Type<T> as(final Class<T> superClass)
    {
        if (superClass == null)
        {
            Type<T> result = (Type<T>)type;
            return result;
        }
        return new Type<T>(
            (Type<T>)this, "as " + superClass.getCanonicalName())
        {
            private Type<T> clazz = type(superClass);
            @Override
            protected boolean thisFilterAccepts(Class<T> object)
            {
                return clazz.isAssignableFrom(object);
            }
        };
    }


    // ----------------------------------------------------------
    /**
     * Get a compile-time-constrained version of this Type that is limited
     * to only types that are compatible with the second specified type.
     * @param superClass The constraint to add to this type.
     * @param <T> The generic parameter T is deduced from the superClass.
     * @return A constrained version of this type that represents a
     *         subclass of the given superClass.
     */
    @SuppressWarnings("unchecked")
    public <T> Type<T> as(final Type<T> superClass)
    {
        if (superClass == null)
        {
            Type<T> result = (Type<T>)type;
            return result;
        }
        return new Type<T>(
            (Type<T>)this, "as " + superClass.getNameOrUnknown())
        {
            @Override
            protected boolean thisFilterAccepts(Class<T> object)
            {
                return superClass.isAssignableFrom(object);
            }
        };
    }


    // ----------------------------------------------------------
    /**
     * Specify the class loader that should be used to look up (load) the
     * class this type represents.
     * @param loader The class loader to use to resolve this type.
     * @return The current Type object, for method chaining.
     */
    public Type<ClassType> fromClassLoader(final ClassLoader loader)
    {
        if (loader == null)
        {
            return this;
        }
        return (new Type<ClassType>(this, "loaded from " + loader)
        {
            @Override
            protected ClassLoader getTargetLoader()
            {
                return loader;
            }
        }).typeResolver();
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only match types (classes) that extend a
     * given super class.  Unlike {@link #isAssignableFrom(Type)}, this
     * method does <b>not</b> consider a class to "extend" itself, so
     * this class and the given superClass must be distinct in order for
     * the result to be true.
     * @param superClass The class to test against.
     * @param <T> This parameter is deduced from the superClass.
     * @return The restricted filter.
     */
    @SuppressWarnings("unchecked")
    public <T> Type<T> extendingClass(final Type<T> superClass)
    {
        if (superClass == null)
        {
            Type<T> result = (Type<T>)type;
            return result;
        }
        return new Type<T>(
            (Type<T>)this, "extending class " + superClass.getNameOrUnknown())
        {
            @Override
            protected boolean thisFilterAccepts(Class<T> object)
            {
                return classExtendsType(object, superClass);
            }
        };
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only match types (classes) that extend a
     * given super class.  Unlike {@link #isAssignableFrom(Type)}, this
     * method does <b>not</b> consider a class to "extend" itself, so
     * this class and the given superClass must be distinct in order for
     * the result to be true.
     * @param superClass The class to test against.
     * @param <T> This parameter is deduced from the superClass.
     * @return The restricted filter.
     */
    public <T> Type<T> extendingClass(Class<T> superClass)
    {
        if (superClass == null)
        {
            @SuppressWarnings("unchecked")
            Type<T> result = (Type<T>)type;
            return result;
        }
        return extendingClass(type(superClass));
    }


    // ----------------------------------------------------------
    /**
     * Determine whether this type (class) extends a given super class.
     * Unlike {@link #isAssignableFrom(Type)}, this method does <b>not</b>
     * consider a class to "extend" itself, so this class and the given
     * superClass must be distinct in order for the result to be true.
     * @param superClass The class to test against.
     * @return True if this type represents a class that "extends" the given
     * superClass (the two must be distinct, and both must be classes, not
     * enums or interfaces).
     */
    public boolean extendsClass(final Type<?> superClass)
    {
        if (superClass == null)
        {
            return false;
        }
        return quantify.evaluate(new Predicate<Class<ClassType>>()
        {
            public boolean isSatisfiedBy(Class<ClassType> object)
            {
                return classExtendsType(object, superClass);
            }
        });
    }


    // ----------------------------------------------------------
    /**
     * Determine whether this type (class) extends a given super class.
     * Unlike {@link #isAssignableFrom(Type)}, this method does <b>not</b>
     * consider a class to "extend" itself, so this class and the given
     * superClass must be distinct in order for the result to be true.
     * @param superClass The class to test against.
     * @return True if this type represents a class that "extends" the given
     * superClass (the two must be distinct, and both must be classes, not
     * enums or interfaces).
     */
    public boolean extendsClass(Class<?> superClass)
    {
        if (superClass == null)
        {
            return false;
        }
        return extendsClass(type(superClass));
    }


    // ----------------------------------------------------------
    /**
     * Restrict this type (class or interface) to only match types that
     * implement a given interface.  Unlike {@link #isAssignableFrom(Type)},
     * this method does <b>not</b> consider an interface to"implement" itself,
     * so this class and the given interface must be distinct in order for
     * the result to be true.
     * @param anInterface The interface to test against.
     * @param <T> This parameter is deduced from anInterface.
     * @return The restricted type filter.
     */
    @SuppressWarnings("unchecked")
    public <T> Type<T> implementingInterface(final Type<T> anInterface)
    {
        if (anInterface == null)
        {
            Type<T> result = (Type<T>)type;
            return result;
        }
        return new Type<T>(
            (Type<T>)this,
            "implementing interface " + anInterface.getNameOrUnknown())
        {
            @Override
            protected boolean thisFilterAccepts(Class<T> object)
            {
                return classImplementsInterface(object, anInterface);
            }
        };
    }


    // ----------------------------------------------------------
    /**
     * Restrict this type (class or interface) to only match types that
     * implement a given interface.  Unlike {@link #isAssignableFrom(Type)},
     * this method does <b>not</b> consider an interface to"implement" itself,
     * so this class and the given interface must be distinct in order for
     * the result to be true.
     * @param anInterface The interface to test against.
     * @param <T> This parameter is deduced from anInterface.
     * @return The restricted type filter.
     */
    public <T> Type<T> implementingInterface(final Class<T> anInterface)
    {
        if (anInterface == null)
        {
            @SuppressWarnings("unchecked")
            Type<T> result = (Type<T>)type;
            return result;
        }
        return implementingInterface(type(anInterface));
    }


    // ----------------------------------------------------------
    /**
     * Determine whether this type (class or interface) implements a given
     * interface.  Unlike {@link #isAssignableFrom(Type)}, this method does
     * <b>not</b> consider an interface to"implement" itself, so this class
     * and the given interface must be distinct in order for the result to
     * be true.
     * @param anInterface The interface to test against.
     * @return True if this type represents a class or interface that
     * "implements" anInterface (the two must be distinct, and anInterface
     * must be an interface, not a class).
     */
    public boolean implementsInterface(final Type<?> anInterface)
    {
        if (anInterface == null)
        {
            return false;
        }
        return quantify.evaluate(new Predicate<Class<ClassType>>()
        {
            public boolean isSatisfiedBy(Class<ClassType> object)
            {
                return classImplementsInterface(object, anInterface);
            }
        });
    }


    // ----------------------------------------------------------
    /**
     * Determine whether this type (class or interface) implements a given
     * interface.  Unlike {@link #isAssignableFrom(Type)}, this method does
     * <b>not</b> consider an interface to"implement" itself, so this class
     * and the given interface must be distinct in order for the result to
     * be true.
     * @param anInterface The interface to test against.
     * @return True if this type represents a class or interface that
     * "implements" anInterface (the two must be distinct, and anInterface
     * must be an interface, not a class).
     */
    public boolean implementsInterface(Class<?> anInterface)
    {
        if (anInterface == null)
        {
            return false;
        }
        return implementsInterface(type(anInterface));
    }


    // ----------------------------------------------------------
    /**
     * Determine whether a given object is an instance of this type.  Mirrors
     * {@link Class#isInstance(Object)}.
     * @param object The object to test.
     * @return True if the given object is an instance of this type.
     */
    public boolean isInstance(final Object object)
    {
        if (object == null)
        {
            return false;
        }
        return quantify.evaluate(new Predicate<Class<ClassType>>()
        {
            public boolean isSatisfiedBy(Class<ClassType> clazz)
            {
                return clazz.isInstance(object);
            }
        });
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations of interfaces.
     * @return The restricted filter.
     */
    public Type<ClassType> declaredAsInterface()
    {
        return withModifiers(Modifier.INTERFACE, "declared as interface");
    }


    // ----------------------------------------------------------
    /**
     * Determine whether this type is an interface.
     * @return True if this is an interface.
     */
    public boolean isInterface()
    {
        return hasModifiers(Modifier.INTERFACE);
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations of interfaces.
     * @return The restricted filter.
     */
    public Type<ClassType> declaredAsEnum()
    {
        return new Type<ClassType>(this, "delcared as enum")
        {
            @Override
            protected boolean thisFilterAccepts(Class<ClassType> object)
            {
                return object.isEnum();
            }
        };
    }


    // ----------------------------------------------------------
    /**
     * Determine whether this type is an enum.
     * @return True if this is an enum.
     */
    public boolean isEnum()
    {
        return quantify.evaluate(new Predicate<Class<ClassType>>()
        {
            public boolean isSatisfiedBy(Class<ClassType> object)
            {
                return object.isEnum();
            }
        });
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations of abstract classes.
     * @return The restricted filter.
     */
    public Type<ClassType> declaredAbstract()
    {
        return withModifiers(Modifier.ABSTRACT, "declared abstract");
    }


    // ----------------------------------------------------------
    /**
     * Determine whether this type is an abstract class.
     * @return True if this is an abstract class.
     */
    public boolean isAbstract()
    {
        return hasModifiers(Modifier.ABSTRACT);
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations of abstract classes.
     * @return The restricted filter.
     */
    @SuppressWarnings("unchecked")
    public Type<ClassType> declaredAsAnnotation()
    {
        return (Type<ClassType>)implementingInterface(
            java.lang.annotation.Annotation.class);
    }


    // ----------------------------------------------------------
    /**
     * Determine whether this type is an abstract class.
     * @return True if this is an abstract class.
     */
    public boolean isAnnotation()
    {
        return implementsInterface(java.lang.annotation.Annotation.class);
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations of classes, not
     * interfaces, enums, or primitive types.
     * @return The restricted filter.
     */
    public Type<ClassType> declaredAsClass()
    {
        return new Type<ClassType>(this, "declared as a class")
        {
            @Override
            protected boolean thisFilterAccepts(Class<ClassType> object)
            {
                return isClass(object);
            }
        };
    }


    // ----------------------------------------------------------
    /**
     * Determine whether this type is a regular class, that is, if it is not
     * an interface or an enum.  Strictly speaking, enums are actually
     * classes, but this method only returns true for "regular" (or abstract)
     * classes.  Use {@link #isEnum()} to find out if it is an enum instead.
     * @return True if this is a class.
     */
    public boolean isClass()
    {
        return quantify.evaluate(new Predicate<Class<ClassType>>()
        {
            public boolean isSatisfiedBy(Class<ClassType> object)
            {
                return isClass(object);
            }
        });
    }


    // ----------------------------------------------------------
    /**
     * Determine whether this type represents one of the eight primitive
     * Java types.
     * @return True if this is a primitive type.
     */
    public boolean isPrimitive()
    {
        return quantify.evaluate(new Predicate<Class<ClassType>>()
        {
            public boolean isSatisfiedBy(Class<ClassType> object)
            {
                return isPrimitive(object);
            }
        });
    }


    // ----------------------------------------------------------
    /**
     * Restrict this filter to only types declared in the specified package.
     * @param packageName The package where this type must be found.  The
     *                    empty string ("") represents the default package,
     *                    while null represents any possible package.
     * @return The restricted filter.
     */
    public Type<ClassType> inPackage(final String packageName)
    {
        String constraint = "in any package";
        if (packageName != null)
        {
            constraint = packageName.isEmpty()
                ? "in default package"
                : ("in package " + packageName);
        }
        return (new Type<ClassType>(this, constraint)
        {
            @Override
            protected boolean thisFilterAccepts(Class<ClassType> object)
            {
                return packageName == null
                    || Types.isInPackage(object, packageName);
            }

            @Override
            protected String getTargetPackage()
            {
                return packageName;
            }
        }).typeResolver();
    }


    // ----------------------------------------------------------
    /**
     * All this filter to match a corresponding type in any package.
     * @return The (less) restricted filtered.
     */
    public Type<ClassType> inAnyPackage()
    {
        return inPackage(null);
    }


    // ----------------------------------------------------------
    /**
     * Determine whether the match(es) of this filter are located in a
     * specified package.  This predicate is quantifiable.
     * @param packageName The package to test against.
     * @return True if the types matching this filter are found in the
     * specified package, according to the current quantifier constraints.
     */
    public boolean isInPackage(final String packageName)
    {
        return quantify.evaluate(new Predicate<Class<ClassType>>()
        {
            public boolean isSatisfiedBy(Class<ClassType> object)
            {
                return Types.isInPackage(object, packageName);
            }
        });
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param type TODO: document.
     * @return TODO: document.
     */
    public boolean isInSamePackageAs(Type<?> type)
    {
        return isInPackage(type.getPackageName());
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @param type TODO: document.
     * @return TODO: document.
     */
    public boolean isInSamePackageAs(Class<?> type)
    {
        return isInSamePackageAs(Type.type(type));
    }


    // ----------------------------------------------------------
    /**
     * TODO: document.
     * @return TODO: document.
     */
    public String getPackageName()
    {
        Package pkg = raw().getPackage();
        return (pkg == null) ? null : pkg.getName();
    }


    // ----------------------------------------------------------
    /**
     * TODO: document
     * @param source TODO: document
     * @return TODO: document
     */
    public boolean isAssignableFrom(Type<?> source)
    {
        return isAssignableFrom(source.raw());
    }


    // ----------------------------------------------------------
    /**
     * TODO: document
     * @param source TODO: document
     * @return TODO: document
     */
    public boolean isAssignableFrom(final Class<?> source)
    {
        if (exists())
        {
            return quantify.evaluate(new Predicate<Class<ClassType>>()
            {
                public boolean isSatisfiedBy(Class<ClassType> object)
                {
                    return isAssignableFrom(object, source);
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
     * TODO: document
     * @param value TODO: document
     * @return TODO: document
     */
    public boolean isAssignableFrom(final Object value)
    {
        if (exists())
        {
            return quantify.evaluate(new Predicate<Class<ClassType>>()
            {
                public boolean isSatisfiedBy(Class<ClassType> object)
                {
                    if (value == null)
                    {
                        return !isPrimitive(object);
                    }
                    else
                    {
                        return isAssignableFrom(object, value.getClass());
                    }
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
     * TODO: document
     * @param value The value to cast
     * @return The cast value
     */
    public ClassType cast(Object value)
    {
        Class<?> target = raw();
        if (value == null)
        {
            if (isPrimitive(target))
            {
                throw new NullPointerException(
                    "Cannot cast null to type " + raw());
            }
            return null;
        }
        if (!isAssignableFrom(target, value.getClass()))
        {
            throw new ClassCastException(
                value.getClass() + " cannot be cast to " + target);
        }

        @SuppressWarnings("unchecked")
        ClassType result = (ClassType)value;
        return result;
    }


    //~ Protected Methods .....................................................

    // ----------------------------------------------------------
    @Override
    protected Type<ClassType> createFreshFilter(
        Type<ClassType> previous, String descriptionOfThisStage)
    {
        return new Type<ClassType>(previous, descriptionOfThisStage);
    }


    // ----------------------------------------------------------
    protected Type<ClassType> createFreshFilter(
        final Class<ClassType> rawClass)
    {
        return new Type<ClassType>(null, describe(rawClass))
        {
            private List<Class<ClassType>> result =
                new ArrayList<Class<ClassType>>();
            {
                result.add(rawClass);
            }

            // ----------------------------------------------------------
            @Override
            protected List<Class<ClassType>> candidatesFromThisFilter()
            {
                return result;
            }
        };
    }


    // ----------------------------------------------------------
    @Override
    protected String filteredObjectDescription()
    {
        return "type";
    }


    // ----------------------------------------------------------
    @Override
    protected String nameOf(Class<ClassType> object)
    {
        return object.getCanonicalName();
    }


    // ----------------------------------------------------------
    @Override
    protected int modifiersFor(Class<ClassType> object)
    {
        return object.getModifiers();
    }


    // ----------------------------------------------------------
    /**
     * Look up the class loader that should be used to resolve this type.
     * A value of null implies using the default class loader.
     * @return The class loader to use, or null if the default should be used.
     */
    protected ClassLoader getTargetLoader()
    {
        if (previousFilter() != null
            && previousFilter() instanceof Type)
        {
            return ((Type<ClassType>)previousFilter()).getTargetLoader();
        }
        else
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    /**
     * Look up the package name that should be used to resolve this type.
     * A value of "" implies using the default package.  A value of null
     * implies searching in <b>every</b> package.  The default here is
     * to see if anything earlier in the chain has a constraint, and if
     * not, default to "".
     * @return The package to search in.
     */
    protected String getTargetPackage()
    {
        if (previousFilter() != null
            && previousFilter() instanceof Type)
        {
            return ((Type<ClassType>)previousFilter()).getTargetPackage();
        }
        else
        {
            return "";
        }
    }


    // ----------------------------------------------------------
    /**
     * Look up the simple (package-less) class name that should be used to
     * resolve this type.
     * @return The simple (package-less) name to search for.
     */
    protected String getTargetSimpleName()
    {
        if (previousFilter() != null
            && previousFilter() instanceof Type)
        {
            return ((Type<ClassType>)previousFilter()).getTargetSimpleName();
        }
        else
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    private boolean isPrimitive(Class<?> clazz)
    {
        return PRIMITIVE_TYPES.contains(clazz);
    }


    // ----------------------------------------------------------
    /**
     * Determine whether the specified type is a class, or is instead an
     * interface or enum.
     * @param clazz The type to inspect.
     * @return True if clazz is not an interface or enum, i.e., it is a
     * regular class.
     */
    protected boolean isClass(Class<?> clazz)
    {
        return !clazz.isInterface() && ! clazz.isEnum() && !isPrimitive(clazz);
    }


    // ----------------------------------------------------------
    private boolean classExtendsType(Class<?> clazz, Type<?> type)
    {
        return !clazz.isInterface()
            && !isPrimitive(clazz)
            && !type.equals(clazz)
            && !type.isInterface()
            && !type.isPrimitive()
            && type.isAssignableFrom(clazz);
    }


    // ----------------------------------------------------------
    private boolean classImplementsInterface(Class<?> clazz, Type<?> type)
    {
        return !isPrimitive(clazz)
            && !type.equals(clazz)
            && type.isInterface()
            && type.isAssignableFrom(clazz);
    }


    // ----------------------------------------------------------
    /**
     * TODO: document
     * @param source TODO: document
     * @return TODO: document
     */
    private boolean isAssignableFrom(Class<?> destination, Class<?> source)
    {
        int primitiveDestIdx = PRIMITIVE_TYPES.indexOf(destination);
        int primitiveSrcIdx = PRIMITIVE_TYPES.indexOf(source);
        int wrapperSrcIdx = WRAPPER_TYPES.indexOf(source);

        if (primitiveDestIdx >= 0)
        {
            // First, try to unbox
            if (wrapperSrcIdx >= 0)
            {
                source = PRIMITIVE_TYPES.get(wrapperSrcIdx);
                primitiveSrcIdx = wrapperSrcIdx;
            }

            if (primitiveSrcIdx < 0)
            {
                // No conversion possible
                return false;
            }

            // No widening conversions *to* these types are performed
            // automatically in Java
            if (boolean.class.equals(destination)
                || char.class.equals(destination))
            {
                return primitiveDestIdx == primitiveSrcIdx;
            }
            else
            {
                // Now check for a widening conversion
                return primitiveDestIdx >= primitiveSrcIdx;
            }
        }
        else if (primitiveSrcIdx >= 0)
        {
            // If auto-boxing is possible, then try it, since the destination
            // is not a primitive type
            source = WRAPPER_TYPES.get(primitiveSrcIdx);
            wrapperSrcIdx = primitiveSrcIdx;
        }
        return destination.isAssignableFrom(source);
    }


    // ----------------------------------------------------------
    /**
     * This creates a "filter" that simply performs type resolution.  It
     * deeply looks through the chain for package constraints, name
     * constraints, and class loader constraints, and then uses them to
     * perform the search.
     * @return A new filter that performs type resolution when necessary.
     */
    protected Type<ClassType> typeResolver()
    {
        return new Type<ClassType>(this, null)
        {
            @Override
            public String getName()
            {
                String pkg = getTargetPackage();
                String name = getTargetSimpleName();
                if (name == null)
                {
                    name = "<any-name>";
                }
                if (pkg == null)
                {
                    pkg = "<any-package>";
                }
                if (!pkg.isEmpty())
                {
                    name = pkg + "." + name;
                }
                return name;
            }


            @Override
            protected boolean guaranteesMultipleMatches()
            {
                return getTargetPackage() == null
                    && getTargetSimpleName() == null;
            }


            @Override
            protected List<Class<ClassType>> candidatesFromThisFilter()
            {
                String pkg = getTargetPackage();
                String name = getTargetSimpleName();
                ClassLoader loader = getTargetLoader();
                List<Class<?>> raw = null;
                if (name != null)
                {
                    if (pkg == null)
                    {
                        // Searching for all classes with this name!
                        raw = Types.allClassesWithSimpleName(name, loader);
                    }
                    else
                    {
                        if (!pkg.isEmpty())
                        {
                            name = pkg + "." + name;
                        }
                        Class<?> c = Types.classForName(name, loader);
                        if (c != null)
                        {
                            raw = new ArrayList<Class<?>>();
                            raw.add(c);
                        }
                    }
                }
                else if (pkg != null)
                {
                    raw = Types.classesInPackage(pkg, loader);
                }
                else
                {
                    raw = Types.allClasses(loader);
                }
                @SuppressWarnings({ "unchecked", "rawtypes" })
                List<Class<ClassType>> found = (List)raw;
                return found;
            }
        };
    }
}
