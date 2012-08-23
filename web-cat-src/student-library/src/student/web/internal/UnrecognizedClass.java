/*
 * ==========================================================================*\
 * | $Id: UnrecognizedClass.java,v 1.4 2011/02/20 21:02:28 mwoodsvt Exp $
 * |*-------------------------------------------------------------------------*|
 * | Copyright (C) 2007-2010 Virginia Tech | | This file is part of the
 * Student-Library. | | The Student-Library is free software; you can
 * redistribute it and/or | modify it under the terms of the GNU Lesser General
 * Public License as | published by the Free Software Foundation; either version
 * 3 of the | License, or (at your option) any later version. | | The
 * Student-Library is distributed in the hope that it will be useful, | but
 * WITHOUT ANY WARRANTY; without even the implied warranty of | MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the | GNU Lesser General Public
 * License for more details. | | You should have received a copy of the GNU
 * Lesser General Public License | along with the Student-Library; if not, see
 * <http://www.gnu.org/licenses/>.
 * \*==========================================================================
 */

package student.web.internal;

import student.web.internal.converters.FlexibleFieldSetConverter;


// -------------------------------------------------------------------------
/**
 * Used by the {@link FlexibleMapper} and {@link FlexibleFieldSetConverter} to
 * represent unknown classes in an object being reconstructed--typically because
 * classes that were present when the object was originally stored are not
 * accessible in the current class loader during reconstruction.
 * <p>
 * This is an singleton class, so use {@link #getInstance()}.
 * </p>
 * 
 * @author Stephen Edwards
 * @author Last changed by $Author: mwoodsvt $
 * @version $Revision: 1.4 $, $Date: 2011/02/20 21:02:28 $
 */
public class UnrecognizedClass
{
    // ----------------------------------------------------------
    /**
     * This is a singleton class, so the constructor is private.
     */
    private UnrecognizedClass()
    {
        // provided just to hide the constructor
    }


    // ----------------------------------------------------------
    /**
     * Get the single instance of this class that exists at runtime.
     * 
     * @return The shared instance of this class.
     */
    public static UnrecognizedClass getInstance()
    {
        return INSTANCE;
    }

    private static final UnrecognizedClass INSTANCE = new UnrecognizedClass();
}
