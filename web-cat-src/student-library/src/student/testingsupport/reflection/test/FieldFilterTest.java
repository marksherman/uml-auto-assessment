/*==========================================================================*\
 |  $Id: FieldFilterTest.java,v 1.1 2011/03/07 22:57:33 stedwar2 Exp $
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
 *  Tests for the {@link Field} class.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.1 $, $Date: 2011/03/07 22:57:33 $
 */
public class FieldFilterTest
    extends TestCase
{
    // ----------------------------------------------------------
    private void dumpFieldFilter(Field<?> filter)
    {
        System.out.println(filter + " matches: " + filter.allMatches());
    }

    // ----------------------------------------------------------
    /**
     * Test basic Field operations.
     */
    public void testField1()
    {
        Field<Double> field1 = field("salary").ofType(double.class)
            .in(Employee.class);
        dumpFieldFilter(field1);

        Employee stan = new Employee();
        dumpFieldFilter(field("salary").visibleIn(stan));

        dumpFieldFilter(field.declaredStatic().in(Employee.class));

        dumpFieldFilter(field);

        dumpFieldFilter(field.withName("msg").in(stan));

        dumpFieldFilter(field.visibleIn(stan));
    }

    // ----------------------------------------------------------
    /**
     * Test basic Field operations.
     */
    public void testField2()
    {
        Employee stan = new Employee();
        assertTrue(
            field("personProtected").in(Person.class).isVisibleIn(stan));
        assertFalse(
            field("firstName").declaredIn(Person.class)
                .isVisibleIn(type(Employee.class)));
    }
}
