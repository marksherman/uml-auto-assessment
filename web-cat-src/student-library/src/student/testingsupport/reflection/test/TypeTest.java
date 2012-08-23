/*==========================================================================*\
 |  $Id: TypeTest.java,v 1.1 2011/03/07 22:57:33 stedwar2 Exp $
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

package student.testingsupport.reflection.test;

import student.TestCase;
import student.testingsupport.reflection.*;
import static student.testingsupport.Reflection.*;

//-------------------------------------------------------------------------
/**
 *  Tests for the {@link Type} class.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.1 $, $Date: 2011/03/07 22:57:33 $
 */
public class TypeTest
    extends TestCase
{
    // ----------------------------------------------------------
    /**
     * Some simple getName() and toString() tests.
     */
    public void testNames()
    {
        Type<?>[] types = {
            type,
            type("foobar"),
            type.inAnyPackage(),
            type.inPackage("student"),
            type("foobar").inAnyPackage(),
            type("foobar").inPackage("student")
        };

        String[] expectedNames = {
            "<any-name>",
            "foobar",
            "<any-package>.<any-name>",
            "student.<any-name>",
            "<any-package>.foobar",
            "student.foobar"
        };

        String[] expectedDescriptions = {
            "type",
            "type with name foobar",
            "type in any package",
            "type in package student",
            "type with name foobar in any package",
            "type with name foobar in package student"
        };

        for (int i = 0; i < types.length; i++)
        {
            assertEquals(expectedNames[i], types[i].getName());
            assertEquals(expectedDescriptions[i], types[i].description());
        }
    }

    // ----------------------------------------------------------
    /**
     * Test basic Type operations.
     */
    public void testType1()
    {
        Type<?> t = type("java.lang.String");
        assertTrue(t.exists());
        assertEquals(t, String.class);

        Type<String> t2 = t.as(String.class);
        assertTrue(t2.exists());
        assertEquals(t2, String.class);
    }


    // ----------------------------------------------------------
    /**
     * Test basic Type operations.
     */
    public void testType2()
    {
        Type<?> person = type("student.testingsupport.reflection.test.Person");
        assertTrue(person.exists());
        assertEquals(person, Person.class);
        assertEquals(
            "student.testingsupport.reflection.test.Person", person.getName());
        assertEquals(
            "student.testingsupport.reflection.test", person.getPackageName());
        assertEquals(
            "type with name Person in package "
            + "student.testingsupport.reflection.test",
            person.description());
        assertEquals(Person.class.toString(), person.toString());

        Type<?> employee =
            type("student.testingsupport.reflection.test.Employee");
        assertTrue(employee.exists());
        assertEquals(employee, Employee.class);

        assertTrue(employee.extendsClass(person));
        assertTrue(person.isAssignableFrom(employee));
        assertTrue(person.isAssignableFrom(person));
        assertTrue(employee.isAssignableFrom(employee));
        assertFalse(employee.isAssignableFrom(person));
        assertTrue(person.isPublic());
        assertFalse(person.isProtected());
        assertFalse(person.isPrivate());

        Type<?> myMarker =
            type("student.testingsupport.reflection.test.MyMarker");
        assertTrue(employee.implementsInterface(myMarker));
        assertFalse(person.implementsInterface(myMarker));
        assertFalse(person.isInterface());
        assertFalse(person.isEnum());
        assertTrue(person.isClass());
        assertTrue(myMarker.isInterface());
        assertFalse(myMarker.isEnum());
        assertFalse(myMarker.isClass());
        assertFalse(person.isAbstract());

        Type<Person> employee2 = employee.as(Person.class);
        assertEquals(employee, employee2);
    }


    private static final Class<?>[] classes = {
        String.class,
        int.class,
        void.class,
        int[].class,
        String[].class,
        int[][][][][][][].class
    };


    // ----------------------------------------------------------
    /**
     * Just a temporary method to try out various things at compile time.
     * Ignore me.
     */
    @SuppressWarnings("unused")
    public void dontTestMe()
    {
        for (Class<?> c : classes)
        {
            System.out.println("c.getName() = " + c.getName());
            System.out.println(
                "c.getCanonicalName() = " + c.getCanonicalName());
        }

        Class<?> c1 = Integer.class;
        Class<?> c2 = int.class;
        System.out.println("c1 = " + c1.getCanonicalName());
        System.out.println("c2 = " + c2.getCanonicalName());
        System.out.println("c1.equals(c2) = " + c1.equals(c2));
        System.out.println("c1 assignable from c2: " + c1.isAssignableFrom(c2));
        System.out.println("c2 assignable from c1: " + c2.isAssignableFrom(c1));

        byte b = 1;
        short s = 10;
        int i = 100;
        long l = 1000;
        double d = 20.0;
        float f = 20.0f;
        boolean y = true;
        char c = 'c';

        s = b;
        i = s;
        l = i;
        f = i;
        d = l;
        f = l;
    }


    // ----------------------------------------------------------
    /**
     * Test search restrictions.
     */
    public void testRestrictions()
    {
        Type.restrictSearchesTo(
            "/Users/edwards/Documents/zkworkspace/student-library/bin:"
            + "/Users/edwards/Documents/zkworkspace/student-library/lib/"
            + "xpp3_min-1.1.4c.jar");
        System.out.println(type.allMatches());
        System.out.println(type.inAnyPackage().allMatches());
        System.out.println(type.inPackage("student.web").allMatches());
    }
}
