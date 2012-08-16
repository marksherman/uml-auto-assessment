/*==========================================================================*\
 |  $Id: Fields.java,v 1.1 2011/03/07 14:05:58 stedwar2 Exp $
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

package student.testingsupport.reflection.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import student.web.internal.MRUMap;

//-------------------------------------------------------------------------
/**
 *  A set of static utility methods to look up lists of {@link Field}
 *  objects associated with a {@link Class}, where the results are backed
 *  by an internal cache.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.1 $, $Date: 2011/03/07 14:05:58 $
 */
public class Fields
{
    //~ Fields ................................................................

    private static MRUMap<Class<?>, List<Field>> declaredFields =
        new MRUMap<Class<?>, List<Field>>(100, 0);
    private static MRUMap<Class<?>, List<Field>> publicOrProtectedFields =
        new MRUMap<Class<?>, List<Field>>(100, 0);
    private static MRUMap<Class<?>, List<Field>> packageFields =
        new MRUMap<Class<?>, List<Field>>(100, 0);
    private static MRUMap<Class<?>, List<Field>> allFields =
        new MRUMap<Class<?>, List<Field>>(100, 0);


    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * This class contains only static helper methods, and so it should
     * never be instantiated.
     */
    private Fields()
    {
        // never called
    }


    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Determine whether a specified field has "default" visibility (that is,
     * package-level visibility).
     * @param field The field to check.
     * @return True if the field has default visibility (that is, it is not
     * private, protected, or public).
     */
    public static boolean isPackageVisible(Field field)
    {
        return Types.isPackageVisible(field.getModifiers());
    }


    // ----------------------------------------------------------
    /**
     * Get a list of all the fields in the specified class, both local and
     * inherited.  This list includes everything, whether or not it is
     * directly visible to methods in this class, and whether or not it is
     * inherited from an ancestor class or an ancestor interface.
     * @param clazz The class to get fields for.
     * @return A list of every field in clazz or one of its
     * ancestors/interfaces.
     */
    public static List<Field> fieldsIn(Class<?> clazz)
    {
        synchronized(allFields)
        {
            List<Field> result = allFields.get(clazz);
            if (result == null)
            {
                result = new ArrayList<Field>();

                // Get all local fields, using cache if possible
                result.addAll(fieldsDeclaredIn(clazz));

                if (clazz.getSuperclass() != null)
                {
                    result.addAll(fieldsIn(clazz.getSuperclass()));
                }
                for (Class<?> superClass : clazz.getInterfaces())
                {
                    result.addAll(fieldsIn(superClass));
                }

                allFields.put(clazz, result);
            }
            return result;
        }
    }


    // ----------------------------------------------------------
    /**
     * Get a list of all the fields declared in the specified class.
     * @param clazz The class to get fields for.
     * @return A list of every field declared directly in clazz.
     */
    public static List<Field> fieldsDeclaredIn(Class<?> clazz)
    {
        synchronized(declaredFields)
        {
            List<Field> result = declaredFields.get(clazz);
            if (result == null)
            {
                result = Arrays.asList(clazz.getDeclaredFields());
                declaredFields.put(clazz, result);
            }
            return result;
        }
    }


    // ----------------------------------------------------------
    /**
     * Get a list of all the fields visible inside the specified class, both
     * local and inherited.  This list includes everything that can be
     * referenced by methods inside the class (without qualification),
     * whether or not it is inherited from an ancestor class or an ancestor
     * interface.  This method is similar to {@link #fieldsIn(Class)}, except
     * that it excludes ancestor fields that are private, as well as ancestor
     * fields of default visibility that are from ancestors in different
     * packages than the specified clazz.
     * @param clazz The class to get fields for.
     * @return A list of every field visible in clazz.
     */
    public static List<Field> fieldsVisibleIn(Class<?> clazz)
    {
        List<Field> result = new ArrayList<Field>(fieldsDeclaredIn(clazz));

        if (clazz.getSuperclass() != null)
        {
            result.addAll(publicOrProtectedFieldsIn(clazz.getSuperclass()));
            for (Field field : packageFieldsIn(clazz.getSuperclass()))
            {
                if (Types.areInSamePackage(clazz, field.getDeclaringClass()))
                {
                    result.add(field);
                }
            }
        }
        for (Class<?> superClass : clazz.getInterfaces())
        {
            for (Field field : packageFieldsIn(superClass.getSuperclass()))
            {
                if (Types.areInSamePackage(
                        superClass, field.getDeclaringClass()))
                {
                    result.add(field);
                }
            }
        }

        return result;
    }


    //~ Private Methods .......................................................

    // ----------------------------------------------------------
    /**
     * Get a list of all the public and protected fields in the specified
     * class, both local and inherited.
     * @param clazz The class to get fields for.
     * @return A list of every public and protected field in clazz or one of
     * its ancestors/interfaces.
     */
    private static List<Field> publicOrProtectedFieldsIn(Class<?> clazz)
    {
        synchronized(publicOrProtectedFields)
        {
            List<Field> result = publicOrProtectedFields.get(clazz);
            if (result == null)
            {
                result = new ArrayList<Field>();

                // Get all non-private local fields, using cache if possible
                for (Field field : fieldsDeclaredIn(clazz))
                {
                    if (Modifier.isPublic(field.getModifiers())
                        || Modifier.isProtected(field.getModifiers()))
                    {
                        result.add(field);
                    }
                }

                if (clazz.getSuperclass() != null)
                {
                    result.addAll(publicOrProtectedFieldsIn(
                        clazz.getSuperclass()));
                }
                for (Class<?> superClass : clazz.getInterfaces())
                {
                    result.addAll(publicOrProtectedFieldsIn(superClass));
                }

                publicOrProtectedFields.put(clazz, result);
            }
            return result;
        }
    }


    // ----------------------------------------------------------
    /**
     * Get a list of all the non-private fields in the specified class, both
     * local and inherited.  Non-private means public, protected, or
     * package-level.  This list includes everything, whether or not it is
     * directly visible to methods in this class, and whether or not it is
     * inherited from an ancestor class or an ancestor interface.
     * @param clazz The class to get fields for.
     * @return A list of every non-private field in clazz or one of its
     * ancestors/interfaces.
     */
    private static List<Field> packageFieldsIn(Class<?> clazz)
    {
        synchronized(packageFields)
        {
            List<Field> result = packageFields.get(clazz);
            if (result == null)
            {
                result = new ArrayList<Field>();

                // Get all non-private local fields, using cache if possible
                for (Field field : fieldsDeclaredIn(clazz))
                {
                    if (!Types.isPackageVisible(field.getModifiers()))
                    {
                        result.add(field);
                    }
                }

                if (clazz.getSuperclass() != null)
                {
                    result.addAll(packageFieldsIn(clazz.getSuperclass()));
                }
                for (Class<?> superClass : clazz.getInterfaces())
                {
                    result.addAll(packageFieldsIn(superClass));
                }

                packageFields.put(clazz, result);
            }
            return result;
        }
    }
}
