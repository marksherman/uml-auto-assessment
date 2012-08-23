/*
 * ==========================================================================*\
 * | $Id: FlexibleMapper.java,v 1.4 2011/06/09 15:44:06 stedwar2 Exp $
 * |*-------------------------------------------------------------------------*|
 * | Copyright (C) 2010 Virginia Tech | | This file is part of the
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

import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

// -------------------------------------------------------------------------
/**
 * A custom XStream class mapper that handles non-existent classes by mapping
 * them to {@link UnrecognizedClass} rather than throwing an exception.
 *
 * @author Stephen Edwards
 * @author Last changed by $Author: stedwar2 $
 * @version $Revision: 1.4 $, $Date: 2011/06/09 15:44:06 $
 */
public class FlexibleMapper
    extends MapperWrapper
{
    // ----------------------------------------------------------
    /**
     * Create a new mapper, which delegates to another wrapper.
     *
     * @param wrapped
     *            The mapper to delegate all work to. The FlexibleMapper only
     *            handles cases that the wrapped mapper says are not found.
     */
    public FlexibleMapper(Mapper wrapped)
    {
        super(wrapped);
    }


    // ----------------------------------------------------------
    /**
     * Look up the class that corresponds to an XStream-encoded XML tag name
     * (which here is interpreted only as a Java class name).
     *
     * @param elementName
     *            The name of the Java class (that is, XML element name in an
     *            XStream-stored Java object) to look up.
     * @return The corresponding Class object.
     */
    @Override
    public Class<?> realClass(String elementName)
    {
        try
        {
            return super.realClass(elementName);
        }
        catch (CannotResolveClassException e)
        {
            return UnrecognizedClass.class;
        }
    }
}
