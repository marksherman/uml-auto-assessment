/*
 * ==========================================================================*\
 * | $Id: ObjectFieldExtractor.java,v 1.6 2011/06/09 15:44:06 stedwar2 Exp $
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

import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.core.JVM;
import java.util.Map;
import java.util.TreeMap;


// -------------------------------------------------------------------------
/**
 * Allows an object to be converted to a map of field name/value pairs and vice
 * versa. Also provides helper methods for comparing and merging such maps.
 *
 * @author Stephen Edwards
 * @author Last changed by $Author: stedwar2 $
 * @version $Revision: 1.6 $, $Date: 2011/06/09 15:44:06 $
 */
public class ObjectFieldExtractor
{
    // ~ Instance/static variables .............................................

    private transient ReflectionProvider reflectionProvider;

    private transient ReflectionProvider pjReflectionProvider = new PureJavaReflectionProvider();


    // ~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new object.
     */
    public ObjectFieldExtractor(ReflectionProvider rp)
    {
        reflectionProvider = rp;
    }


    // ~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Convert an object to a map of field name/value pairs.
     *
     * @param object
     *            The object to convert
     * @return The object's field values in map form
     */
    public Map<String, Object> objectToFieldMap( Object object )
    {
        final TreeMap<String, Object> result = new TreeMap<String, Object>();

        reflectionProvider.visitSerializableFields( object,
            new ReflectionProvider.Visitor()
            {
                @SuppressWarnings("rawtypes")
                public void visit(
                    String fieldName,
                    Class type,
                    Class definedIn,
                    Object value )
                {
                    result.put( fieldName, value );
                }
            } );

        return result;
    }


    // ----------------------------------------------------------
    /**
     * Convert an object to a map of field name/value pairs.
     *
     * @param object
     *            The object to convert
     * @return The object's field values in map form
     */
    @SuppressWarnings("unchecked")
    public <T> T fieldMapToObject(
        Class<T> t, final Map<String, Object> fields)
    {
        if ( fields == null )
        {
            return null;
        }
        Object result = null;
        result = reflectionProvider.newInstance( t );
        if ( !restoreObjectFromFieldMap( result, fields )
            && !( reflectionProvider instanceof PureJavaReflectionProvider ) )
        {
            // If some fields weren't initialized, then try to create an
            // object using a default constructor, if possible
            try
            {
                Object newResult = pjReflectionProvider.newInstance( t );
                restoreObjectFromFieldMap( newResult, fields );
                result = newResult;
            }
            catch ( Exception e )
            {
                // if we can't create it, there's no default constructor
            }
        }
        return (T)result;
    }


    private static class BooleanWrapper
    {
        boolean value;
    }


    // ----------------------------------------------------------
    /**
     * Convert an object to a map of field name/value pairs.
     *
     * @param object
     *            The object to convert
     * @param fields
     *            The field values to restore from
     * @return True if all fields in the object were present in the field set
     */
    @SuppressWarnings("unchecked")
    public boolean restoreObjectFromFieldMap(
        Object object,
        final Map<String, Object> fields )
    {
        final Object result = object;
        final BooleanWrapper allFound = new BooleanWrapper();
        allFound.value = true;
        reflectionProvider.visitSerializableFields( result,
            new ReflectionProvider.Visitor()
            {
                @SuppressWarnings("rawtypes")
                public void visit(
                    String fieldName,
                    Class type,
                    Class definedIn,
                    Object value )
                {
                    if ( fields.containsKey( fieldName ) )
                    {
                        reflectionProvider.writeField( result,
                            fieldName,
                            fields.get( fieldName ),
                            definedIn );
                    }
                    else
                    {
                        allFound.value = false;
                    }
                }
            } );
        return allFound.value;
    }


    // ----------------------------------------------------------
    /**
     * Compute the difference between two field sets. This is not the same as
     * "set difference". Instead, it is really the "changes" map minus any
     * entries that duplicate those in the "original". In other words, what
     * entries in "changes" map to different values than the same entries in
     * "original"?
     *
     * @param original
     *            The base field set to compare against
     * @param changes
     *            The second (modified) field set
     * @return A field set map that contains the values defined in changes that
     *         are either not present in or are different than in the original.
     */
    public Map<String, Object> difference(
        Map<String, Object> original,
        Map<String, Object> changes )
    {
        Map<String, Object> differences = new TreeMap<String, Object>();
        for ( String key : changes.keySet() )
        {
            Object value = changes.get( key );
            if ( ( value == null && original.get( key ) != null )
                || ( value != null && !value.equals( original.get( key ) ) ) )
            {
                differences.put( key, value );
            }
        }
        return differences;
    }
}
