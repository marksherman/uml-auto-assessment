/*==========================================================================*\
 |  $Id: NoStackTraces.java,v 1.1 2011/03/07 14:05:04 stedwar2 Exp $
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
 *  Marker annotation to indicate that test case failures within a single
 *  method or an entire class should not include abbreviated stack trace
 *  information in the hint message for test case failures due to unexpected
 *  exceptions thrown from the code under test.
 *
 *  @author  Stephen Edwards
 *  @author  Last changed by $Author: stedwar2 $
 *  @version $Revision: 1.1 $, $Date: 2011/03/07 14:05:04 $
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface NoStackTraces
{
    /**
     * Indicates whether stack traces should be suppressed even for
     * failures of user-provided assertions in the code under test.  When
     * true, stack traces are provided for internal assert failures, but
     * omitted for all other unexpected exceptions.  When false, stack
     * traces are omitted for everything, including internal assert failures.
     */
    boolean value() default true;
}
