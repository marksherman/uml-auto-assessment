/*==========================================================================*\
 |  $Id: Annotation.java,v 1.1 2011/06/09 15:31:24 stedwar2 Exp $
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
 *  @version $Revision: 1.1 $, $Date: 2011/06/09 15:31:24 $
 */
public class Annotation<ClassType>
    extends NameFilter<Annotation<ClassType>, Class<ClassType>>
{
    //~ Fields ................................................................

    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Create a new Type object that represents a type filter.
     * @param previous The previous filter in the chain of filters.
     * @param descriptionOfThisStage A description of this stage in the
     * filter chain.
     */
    protected Annotation(
        Annotation<ClassType> previous, String descriptionOfThisStage)
    {
        super(previous, descriptionOfThisStage);
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * A starting point for type filters where the name is unspecified.
     */
    public static final Annotation<?> annotation =
        new Annotation<Object>(null, null).typeResolver();


    // ----------------------------------------------------------
    /**
     * Create a new Type object.
     * @param name The name of the type (class).
     * @return a new Type filter object representing the named field.
     */
    public static Annotation<?> annotation(String name)
    {
        return annotation.withName(name);
    }


    // ----------------------------------------------------------
    /**
     * Create a new Type object from a {@link Class} object.
     * @param aClass The class to represent.
     * @param <T> This type is deduced from aClass.
     * @return a new Type object representing the given class.
     */
    public static <T> Annotation<T> annotation(Class<T> aClass)
    {
        if (aClass == null)
        {
            @SuppressWarnings("unchecked")
            Annotation<T> result = (Annotation<T>)annotation;
            return result;
        }
        @SuppressWarnings("unchecked")
        Annotation<T> result = ((Annotation<T>)annotation)
            .createFreshFilter(aClass);
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
        Type.restrictSearchesTo(searchPath);
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
        Type.searchEverywhere();
    }

    // ----------------------------------------------------------
    /**
     * Restrict this filter to only admit declarations with the specified
     * name.  If the name is a simple name (that is,
     * @param name The name required by the resulting filter.
     * @return A new filter with the given restriction.
     */
    public Annotation<ClassType> withName(String name)
    {
        if (name == null)
        {
            return this;
        }
        int pos = name.lastIndexOf('.');
        if (pos > 0)
        {
            final String simpleName = name.substring(pos + 1);
            return (new Annotation<ClassType>(this, "with name " + simpleName)
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
            return (new Annotation<ClassType>(this, "with name " + simpleName)
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
     * Specify the class loader that should be used to look up (load) the
     * class this type represents.
     * @param loader The class loader to use to resolve this type.
     * @return The current Type object, for method chaining.
     */
    public Annotation<ClassType> fromClassLoader(final ClassLoader loader)
    {
        if (loader == null)
        {
            return this;
        }
        return (new Annotation<ClassType>(this, "loaded from " + loader)
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
     * Restrict this filter to only types declared in the specified package.
     * @param packageName The package where this type must be found.  The
     *                    empty string ("") represents the default package,
     *                    while null represents any possible package.
     * @return The restricted filter.
     */
    public Annotation<ClassType> inPackage(final String packageName)
    {
        String constraint = "in any package";
        if (packageName != null)
        {
            constraint = packageName.isEmpty()
                ? "in default package"
                : ("in package " + packageName);
        }
        return (new Annotation<ClassType>(this, constraint)
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
    public Annotation<ClassType> inAnyPackage()
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
     * @param annotation TODO: document.
     * @return TODO: document.
     */
    public boolean isInSamePackageAs(Annotation<?> annotation)
    {
        return isInPackage(annotation.getPackageName());
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


    //~ Protected Methods .....................................................

    // ----------------------------------------------------------
    @Override
    protected Annotation<ClassType> createFreshFilter(
        Annotation<ClassType> previous, String descriptionOfThisStage)
    {
        return new Annotation<ClassType>(previous, descriptionOfThisStage);
    }


    // ----------------------------------------------------------
    protected Annotation<ClassType> createFreshFilter(
        final Class<ClassType> rawClass)
    {
        return new Annotation<ClassType>(null, describe(rawClass))
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
        return "annotation";
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
            && previousFilter() instanceof Annotation)
        {
            return ((Annotation<ClassType>)previousFilter()).getTargetLoader();
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
            && previousFilter() instanceof Annotation)
        {
            return ((Annotation<ClassType>)previousFilter()).getTargetPackage();
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
            && previousFilter() instanceof Annotation)
        {
            return ((Annotation<ClassType>)previousFilter()).getTargetSimpleName();
        }
        else
        {
            return null;
        }
    }


    // ----------------------------------------------------------
    /**
     * This creates a "filter" that simply performs type resolution.  It
     * deeply looks through the chain for package constraints, name
     * constraints, and class loader constraints, and then uses them to
     * perform the search.
     * @return A new filter that performs type resolution when necessary.
     */
    protected Annotation<ClassType> typeResolver()
    {
        return new Annotation<ClassType>(this, null)
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
