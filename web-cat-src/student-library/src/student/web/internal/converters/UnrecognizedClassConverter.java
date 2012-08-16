/*
 * ==========================================================================*\
 * | $Id: UnrecognizedClassConverter.java,v 1.1 2011/02/18 20:42:29 stedwar2 Exp
 * $
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

package student.web.internal.converters;

import java.util.Map;
import java.util.UUID;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.TreeMapConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

import student.web.internal.Snapshot;
import student.web.internal.UnrecognizedClass;


// -------------------------------------------------------------------------
/**
 * An XStream {@link Converter} for handling {@link UnrecognizedClass} values.
 * All <code>UnrecognizedClass</code> values are converted to <code>null</code>.
 *
 * @author Stephen Edwards
 * @author Last changed by $Author: stedwar2 $
 * @version $Revision: 1.4 $, $Date: 2011/06/09 15:44:06 $
 */
public class UnrecognizedClassConverter implements Converter
{
    private TreeMapConverter mapConverter;
    // ----------------------------------------------------------
    /**
     * Default constructor.
     */
    public UnrecognizedClassConverter(Mapper mapper)
    {
        // nothing to do
        mapConverter = new TreeMapConverter( mapper );
    }


    // ----------------------------------------------------------
    /**
     * Store an <code>UnrecognizedClass</code> value, which should never happen.
     * This method does nothing, so such values are never actually stored.
     *
     * @param source
     *            The value to store.
     * @param writer
     *            The writer to store it on.
     * @param context
     *            The conversion context.
     */
    public void marshal(
        Object source,
        HierarchicalStreamWriter writer,
        MarshallingContext context )
    {
        //Do Nothing
    }


    // ----------------------------------------------------------
    /**
     * Reconstitute an <code>UnrecognizedClass</code> value, which is always
     * reconstructed as <code>null</code>.
     *
     * @param reader
     *            The input source to read from.
     * @param context
     *            The conversion context.
     * @return Always <code>null</code>.
     */
    public Object unmarshal(
        HierarchicalStreamReader reader,
        UnmarshallingContext context )
    {
//        String name = reader.getNodeName();
        if ( reader.getAttribute( XMLConstants.FIELDSET_ATTRIBUTE ) != null )
        {
            String objId = reader.getAttribute( XMLConstants.ID_ATTRIBUTE );
            UUID id = UUID.fromString( objId );
            @SuppressWarnings("unchecked")
            Map<String, Object> realFields = (Map<String, Object>)mapConverter.unmarshal( reader,
                context );
            Snapshot.getLocal().resolveObject( id, new Object(), realFields );
            return new NullableClass(reader.getNodeName(),id);
        }
        // Treat instances if this class as if they were null
        return null;
    }


    // ----------------------------------------------------------
    /**
     * Return true for any class that this converter can handle.
     *
     * @param type
     *            The class to check.
     * @return True if <code>type</code> is <code>UnrecognizedClass</code>, and
     *         false otherwise.
     */
    @SuppressWarnings("rawtypes")
    public boolean canConvert( Class type )
    {
        return UnrecognizedClass.class.equals( type );
    }

}
