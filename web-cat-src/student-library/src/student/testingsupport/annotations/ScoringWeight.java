/*==========================================================================*\
 |  $Id: ScoringWeight.java,v 1.2 2011/06/09 15:31:45 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2011 Virginia Tech
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

package student.testingsupport.annotations;

import java.lang.annotation.*;

//-------------------------------------------------------------------------
/**
 *  Annotation used to provide a weight for a single test case method, or
 *  a default weight for all test case methods in a suite class.  Without
 *  using this annotation, the default weight is 1.0.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.2 $, $Date: 2011/06/09 15:31:45 $
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ScoringWeight
{
    /**
     * The weight for this test case method (if applied to a method), or
     * the cumulative weight for all test methods in this class (if applied
     * to a test class).
     * @return The weight.
     */
    double value();

    /**
     * The default scoring weight to use on test methods in a test class.
     * This is only applicable when using this annotation on a test class,
     * and will be ignored if specified on an individual method.
     * @return The default weight.
     */
    double defaultMethodWeight() default 1.0;
}
