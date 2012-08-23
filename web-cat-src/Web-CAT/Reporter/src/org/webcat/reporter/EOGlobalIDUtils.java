/*==========================================================================*\
 |  $Id: EOGlobalIDUtils.java,v 1.2 2011/05/27 15:36:46 stedwar2 Exp $
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2011 Virginia Tech
 |
 |  This file is part of Web-CAT.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU Affero General Public License as published
 |  by the Free Software Foundation; either version 3 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU Affero General Public License
 |  along with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package org.webcat.reporter;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//-------------------------------------------------------------------------
/**
 * Provides static utility methods for converting arrays or dictionaries of EO
 * IDs to the corresponding EOs and vice versa. The conversions provided are
 * "deep"; that is, nested arrays and dictionaries are traversed as well.
 *
 * Any elements of an array or dictionary that are not a global ID or enterprise
 * object are skipped and returned unmodified.
 *
 * @author Tony Allevato
 * @author  Last changed by $Author: stedwar2 $
 * @version $Revision: 1.2 $, $Date: 2011/05/27 15:36:46 $
 */
public class EOGlobalIDUtils
{
    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * This class provides only static utility methods, so no instances should
     * ever be created.
     */
    private EOGlobalIDUtils()
    {
        // Nothing to do
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Converts a list of EO IDs to a list of the objects that they represent.
     *
     * @param array
     *            the list of {@link EOGlobalID}s to convert
     * @param ec
     *            the editing context
     *
     * @return an {@link NSArray} containing the corresponding objects
     */
    public static NSArray<EOEnterpriseObject> enterpriseObjectsForIdArray(
        List<?> array, EOEditingContext ec)
    {
        NSMutableArray<EOEnterpriseObject> newArray =
            new NSMutableArray<EOEnterpriseObject>();

        for (Object value : array)
        {
            Object newValue = tryEnterpriseObjectForId(value, ec);
            newArray.addObject(newValue);
        }

        return newArray;
    }


    // ----------------------------------------------------------
    /**
     * Converts a dictionary containing EO IDs for values into a dictionary with
     * the corresponding enterprise objects as values.
     *
     * @param dictionary
     *            the dictionary of {@link EOGlobalID}s to convert
     * @param ec
     *            the editing context
     *
     * @return an {@link NSDictionary} containing the corresponding objects
     */
    public static NSDictionary<?, ?> enterpriseObjectsForIdDictionary(
        Map<?, ?> dictionary, EOEditingContext ec)
    {
        NSMutableDictionary<?, ?> newDictionary =
            new NSMutableDictionary<Object, Object>();

        if (dictionary instanceof NSDictionary)
        {
            Enumeration<?> e =
                ((NSDictionary<?, ?>) dictionary).keyEnumerator();
            while (e.hasMoreElements())
            {
                Object key = e.nextElement();
                Object value =
                    ((NSDictionary<?, ?>) dictionary).objectForKey(key);

                Object newValue = tryEnterpriseObjectForId(value, ec);
                newDictionary.setObjectForKey(newValue, key);
            }
        }
        else
        {
            Iterator<?> it = dictionary.keySet().iterator();
            while (it.hasNext())
            {
                Object key = it.next();
                Object value = dictionary.get(key);

                Object newValue = tryEnterpriseObjectForId(value, ec);
                newDictionary.setObjectForKey(newValue, key);
            }
        }

        return newDictionary;
    }


    // ----------------------------------------------------------
    /**
     * Tries to convert an object that might be an enterprise object into a
     * global ID.
     *
     * @param value
     *            the object to try to convert
     * @param ec
     *            the editing context
     *
     * @return if the object was a global ID, the corresponding enterprise
     *         object is returned. If the object was a list or dictionary, a
     *         deep conversion is returned. Otherwise, the original object is
     *         returned.
     */
    public static Object tryEnterpriseObjectForId(Object value,
            EOEditingContext ec)
    {
        if (value instanceof List)
        {
            return enterpriseObjectsForIdArray((List<?>) value, ec);
        }
        else if (value instanceof Map)
        {
            return enterpriseObjectsForIdDictionary((Map<?, ?>) value, ec);
        }
        else if (value instanceof EOGlobalID)
        {
            return ec.faultForGlobalID((EOGlobalID) value, ec);
        }
        else
        {
            return value;
        }
    }


    // ----------------------------------------------------------
    /**
     * Converts a list of enterprise objects to a list of global IDs.
     *
     * @param array
     *            the list of enterprise objects to convert
     * @param ec
     *            the editing context
     *
     * @return an {@link NSArray} containing the corresponding global IDs
     */
    public static NSArray<?> idsForEnterpriseObjectArray(
        List<?> array, EOEditingContext ec)
    {
        NSMutableArray<?> newArray = new NSMutableArray<Object>();

        if (array instanceof NSArray)
        {
            Enumeration<?> e = ((NSArray<?>) array).objectEnumerator();
            while (e.hasMoreElements())
            {
                Object value = e.nextElement();

                Object newValue = tryIdForEnterpriseObject(value, ec);
                newArray.addObject(newValue);
            }
        }
        else
        {
            Iterator<?> it = array.iterator();
            while (it.hasNext())
            {
                Object value = it.next();

                Object newValue = tryIdForEnterpriseObject(value, ec);
                newArray.addObject(newValue);
            }
        }

        return newArray;
    }


    // ----------------------------------------------------------
    /**
     * Converts a dictionary containing enterprise objects for values into a
     * dictionary with the corresponding EO global IDs as values.
     *
     * @param dictionary
     *            the dictionary of enterprise objects to convert
     * @param ec
     *            the editing context
     *
     * @return an {@link NSDictionary} containing the corresponding global IDs
     */
    public static NSDictionary<?, ?> idsForEnterpriseObjectDictionary(
        Map<?, ?> dictionary, EOEditingContext ec)
    {
        NSMutableDictionary<?, ?> newDictionary =
            new NSMutableDictionary<Object, Object>();

        if (dictionary instanceof NSDictionary)
        {
            Enumeration<?> e =
                ((NSDictionary<?, ?>) dictionary).keyEnumerator();
            while (e.hasMoreElements())
            {
                Object key = e.nextElement();
                Object value =
                    ((NSDictionary<?, ?>) dictionary).objectForKey(key);

                Object newValue = tryIdForEnterpriseObject(value, ec);
                newDictionary.setObjectForKey(newValue, key);
            }
        }
        else
        {
            Iterator<?> it = dictionary.keySet().iterator();
            while (it.hasNext())
            {
                Object key = it.next();
                Object value = dictionary.get(key);

                Object newValue = tryIdForEnterpriseObject(value, ec);
                newDictionary.setObjectForKey(newValue, key);
            }
        }

        return newDictionary;
    }


    // ----------------------------------------------------------
    /**
     * Tries to convert an object that might be an enterprise object into a
     * global ID. If the object is a list or a dictionary, it is deeply
     * converted.
     *
     * @param value
     *            the object try to convert
     * @param ec
     *            the editing context
     *
     * @return if the object was an enterprise object, its global ID is
     *         returned. If it was a list or dictionary, then a deep conversion
     *         is returned. Otherwise, the original object is returned.
     */
    public static Object tryIdForEnterpriseObject(Object value,
            EOEditingContext ec)
    {
        if (value instanceof List)
        {
            return idsForEnterpriseObjectArray((List<?>) value, ec);
        }
        else if (value instanceof Map)
        {
            return idsForEnterpriseObjectDictionary((Map<?, ?>) value, ec);
        }
        else if (value instanceof EOEnterpriseObject)
        {
            return ec.globalIDForObject((EOEnterpriseObject) value);
        }
        else
        {
            return value;
        }
    }
}
