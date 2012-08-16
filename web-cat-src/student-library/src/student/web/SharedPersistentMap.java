/*==========================================================================*\
 |  $Id: SharedPersistentMap.java,v 1.6 2011/06/21 14:57:28 mwoodsvt Exp $
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

package student.web;

import java.util.Map;
import student.web.internal.LocalApplicationSupportStrategy;
import student.web.internal.LocalityService;

//-------------------------------------------------------------------------
/**
 *  This class represents a {@link PersistentMap} whose contents are
 *  <b>shared</b> by all users/programs executing in the same context (i.e.,
 *  on the same CloudSpace server, or in the same current working directory
 *  on an end-user machine).  Its contents are <b>persistent</b>,
 *  meaning that they will continue to be available from one
 *  program run to the next.  Its contents are <b>shared</b> meaning that
 *  everyone using this class on the same CloudSpace server will see the
 *  same set of keys and values--objects stored in this map will be visible
 *  to everyone, as long as the same identifying key is used.  Basically, it
 *  is a shared container where you can store information semi-permanently, so
 *  that your program(s) or anyone else's can access it again later.
 *  <p>
 *  A <code>Map</code> is an object that maps keys to values.  In a
 *  <code>PersistentMap</code>, the keys are Strings.  You can think of
 *  the keys in a persistent map as being string identifiers that you
 *  can use to identify objects that you save now, and then recall again
 *  in later programs (or separate executions of the same program).
 *  A map cannot have any duplicate keys, and each unique key maps to at
 *  most one value.
 *  </p>
 *  <p>
 *  Other than the fact that a persistent map keeps its values from one
 *  execution of a program to the next, you use it just like any other
 *  {@link Map}.  The most basic operations are illustrated below.
 *  </p>
 *  <pre>
 *  SharedPersistentMap&lt;UserProfile&gt; map = ...;
 *
 *  // Use put(key, value) to save a value under a given key
 *  map.put("stedwar2", someObject);
 *
 *  // Use get(key) to look up the object saved for that key
 *  UserProfile profile = map.get("stedwar2");
 *  </pre>
 *  <p>
 *  Note that if you have saved an object under a given key, and later
 *  modify that object in some way, <b>you need to call <code>put()</code>
 *  again in order for those new changes to be recorded in the map.</b>
 *  </p>
 *
 *  @param <T> The type of objects to store in the map
 *
 *  @author  Mike Woods
 *  @author Last changed by $Author: mwoodsvt $
 *  @version $Revision: 1.6 $, $Date: 2011/06/21 14:57:28 $
 */
public class SharedPersistentMap<T>
    extends AbstractPersistentMap<T>
{
    // ~ Instance/static variables ............................................

    public static final String CONTEXT_OBJECT = "context-object";

    private static final String SHARED = "shared";


    // ~ Constructor ..........................................................

    // ----------------------------------------------------------
    /**
     * Create a new map that associates keys with values of the specified
     * class.  Initially, the map will contain all of the key/value
     * associations that have been previously stored in other
     * instances of this class, by <em>any</em> program running in the
     * same context (i.e., on the same CloudSpace server, or in the same
     * development project on a local machine).
     * <p>
     * Typical usage: if you want a shared persistent map that stores objects
     * of class <code>Widget</code>, then:
     * </p>
     * <pre>
     * SharedPersistentMap<Widget> map =
     *     new SharedPersistentMap<Widget>(Widget.class);
     * </pre>
     *
     * @param genericClass  The {@link Class} object that represents the
     *                      generic type <code>T</code>, the type of values
     *                      stored in this map.
     */
    public SharedPersistentMap( Class<T> genericClass )
    {
        super("",SHARED,genericClass );
//        typeAware = genericClass;
    }
    /**
     * NOT FOR STUDENT USE.  ONLY USE IF YOU KNOW WHAT YOU ARE DOING!
     * 
     * @param genericClass Class type
     * @param loader custom class loader to use to load classes
     */
    public SharedPersistentMap(Class<T> genericClass, ClassLoader loader)
    {
        super("",SHARED,genericClass, loader);
    }


    // ----------------------------------------------------------
    /**
     * Remove all of the key/value associations from this map.  The map
     * will be empty after this call returns.
     * <p>
     * Because the contents of this map might be shared among many
     * users, <b>you may not use this method in a program running in a
     * multi-user context</b> (i.e., it is forbidden on a CloudSpace server).
     * </p>
     */
    @Override
    public void clear()
    {
        if (LocalityService.getSupportStrategy()
            instanceof LocalApplicationSupportStrategy)
        {
            super.clear();
        }
        else
        {
            throw new UnsupportedOperationException(
                "clear() is not supported on a SharedPersistentMap whose "
                + "contents are shared among multiple users." );
        }
    }


    // ----------------------------------------------------------
    /**
     * Get the ID used to store the context cache in the ZK browser session.
     * @return The context cache ID.
     */
    @Override
    protected String getCacheId(String uniqueId)
    {
        return CONTEXT_OBJECT;
    }
}
