/*
 * ==========================================================================*\
 * | $Id: PersistentMap.java,v 1.2 2011/02/20 21:02:28 mwoodsvt Exp $
 * |*-------------------------------------------------------------------------*|
 * | Copyright (C) 2011 Virginia Tech | | This file is part of the
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

package student.web;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


// -------------------------------------------------------------------------
/**
 * This interface represents a {@link Map} that is <b>persistent</b>, meaning
 * that its contents will continue to be available from one program run to the
 * next. Basically, it is a container where you can store information
 * semi-permanently, so that your program(s) can access it again later.
 * <p>
 * A <code>Map</code> is an object that maps keys to values. In a
 * <code>PersistentMap</code>, the keys are Strings. You can think of the keys
 * in a persistent map as being string identifiers that you can use to identify
 * objects that you save now, and then recall again in later programs (or
 * separate executions of the same program). A map cannot have any duplicate
 * keys, and each unique key maps to at most one value.
 * </p>
 * <p>
 * Other than the fact that a persistent map keeps its values from one execution
 * of a program to the next, you use it just like any other {@link Map}. The
 * most basic operations are illustrated below.
 * </p>
 * 
 * <pre>
 *  PersistentMap&lt;UserProfile&gt; map = ...;
 * 
 *  // Use put(key, value) to save a value under a given key
 *  map.put("stedwar2", someObject);
 * 
 *  // Use get(key) to look up the object saved for that key
 *  UserProfile profile = map.get("stedwar2");
 * </pre>
 * <p>
 * Note that if you have saved an object under a given key, and later modify
 * that object in some way, <b>you need to call <code>put()</code> again in
 * order for those new changes to be recorded in the map.</b>
 * </p>
 * 
 * @param <T>
 *            The type of objects to store in the map
 * 
 * @author Mike Woods
 * @author Last changed by $Author: mwoodsvt $
 * @version $Revision: 1.2 $, $Date: 2011/02/20 21:02:28 $
 */
public interface PersistentMap<T> extends java.util.Map<String, T>
{
    // ----------------------------------------------------------
    /**
     * Remove all of the key/value associations from this map. The map will be
     * empty after this call returns.
     */
    public void clear();


    // ----------------------------------------------------------
    /**
     * Returns true if this map contains a mapping for the specified key.
     * 
     * @param key
     *            The key whose presence in this map is to be tested, which must
     *            be a non-null, non-empty string.
     * 
     * @return True if this map contains a mapping for the specified key.
     */
    public boolean containsKey( Object key );


    // ----------------------------------------------------------
    /**
     * Returns true if this map maps one or more keys to the specified value.
     * This operation will probably require time linear in the map size.
     * 
     * @param value
     *            The value whose presence in the map is being tested.
     * 
     * @return True if the map contains a mapping from one or more keys to the
     *         specified value.
     */
    public boolean containsValue( Object value );


    // ----------------------------------------------------------
    /**
     * Returns a set of all entries (that is, key/value pairs) stored in this
     * map. An {@link Map.Entry Entry} object represents a key together with its
     * value, and provides <code>getKey()</code> and <code>getValue()</code>
     * methods to access them.
     * 
     * @return The set of entries within the map.
     */
    public Set<Entry<String, T>> entrySet();


    // ----------------------------------------------------------
    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key. If a mapping exists but it is not of
     * type <code>T</code>, then null will be returned. Use
     * {@link #containsKey(Object) containsKey} to determine if a mapping
     * exists.
     * 
     * @param key
     *            The key whose associated value is to be returned. This key
     *            must be a non-null, non-empty string.
     * 
     * @return The value to which the specified key is mapped, or null if this
     *         map contains no mapping for the key (or the mapped value is not
     *         an instance of the generic type parameter T of this map).
     */
    public T get( Object key );


    // ----------------------------------------------------------
    /**
     * Returns <code>true</code> if this map contains no key-value mappings.
     * 
     * @return True if this map contains no key-value mappings.
     */
    public boolean isEmpty();


    // ----------------------------------------------------------
    /**
     * Returns the set of all keys currently in use in this map--that is, all
     * keys that are mapped to values. The resulting set should not be modified.
     * Also, the map should not be modified while code is looping over the
     * contents of the set returned by this method (that is, do not call
     * <code>put()</code> or <code>remove()</code> on the map while looping over
     * the set of keys).
     * 
     * @return The set of all keys currently associated with values in this map.
     */
    public Set<String> keySet();


    // ----------------------------------------------------------
    /**
     * Stores a given object in the map by associating it with the given key. If
     * the map previously contained a mapping for the key, the old value is
     * replaced by the specified value.
     * 
     * @param key
     *            The key with which the specified value is to be associated,
     *            which must be a non-null, non-empty string.
     * @param value
     *            The object to be associated with the key in the map.
     * 
     * @return The previous value associated with the key, or null if no
     *         previous association existed.
     */
    public T put( String key, T value );


    // ----------------------------------------------------------
    /**
     * Copies all of the mappings from the specified map into this map. The
     * effect of this call is equivalent to that of calling
     * {@link #put(Object, Object) put(k, v)} on this map once for each mapping
     * from key <code>k</code> to value <code>v</code> in the other map. The
     * behavior of this operation is undefined if the other map is modified
     * while the operation is in progress.
     * 
     * @param externalMap
     *            The external map of keys to values that will be added to this
     *            map.
     */
    public void putAll( Map<? extends String, ? extends T> externalMap );


    // ----------------------------------------------------------
    /**
     * Remove an object from this map by specifying its key. This method removes
     * the mapping for a key from this map if one is present. It returns the
     * value to which this map previously associated the given key, or null if
     * the map contained no mapping for the key. The map will not contain a
     * mapping for the specified key once the call returns.
     * 
     * @param key
     *            The key associated with the value to remove. The key must be a
     *            non-null, non-empty string.
     * 
     * @return Returns the removed object, if one is associated with the key, or
     *         null if no previous association existed.
     */
    public T remove( Object key );


    // ----------------------------------------------------------
    /**
     * Returns the number of key-value mappings in this map.
     * 
     * @return The number of key-value mappings in this map.
     */
    public int size();


    // ----------------------------------------------------------
    /**
     * Returns a collection representing all of the values stored in this map.
     * 
     * @return A collection representing all of values stored in the map.
     */
    public Collection<T> values();
}
