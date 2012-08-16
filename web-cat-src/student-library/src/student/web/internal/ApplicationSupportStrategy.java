/*
 * ==========================================================================*\
 * | $Id: ApplicationSupportStrategy.java,v 1.3 2011/02/18 20:40:07 stedwar2 Exp
 * $
 * |*-------------------------------------------------------------------------*|
 * | Copyright (C) 2009-2010 Virginia Tech | | This file is part of the
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

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;


// -------------------------------------------------------------------------
/**
 * Defines the support methods needed to implement server-specific storage
 * features for a {@link student.web.PersistentMap}.
 * 
 * @author Stephen Edwards
 * @author Last changed by $Author: mwoodsvt $
 * @version $Revision: 1.6 $, $Date: 2011/04/26 00:54:07 $
 */
public interface ApplicationSupportStrategy
{
    // ----------------------------------------------------------
    /**
     * Cause the web application to show a different web page in the user's web
     * browser.
     * 
     * @param url
     *            The new web page to show in the user's browser
     */
    void showWebPage( String url );


    // ----------------------------------------------------------
    /**
     * Retrieve the name of the current ZHTML file, such as "index.zhtml" or
     * "lab02.zhtml".
     * 
     * @return The name of the current ZHTML file, without any directory
     *         component, or "" if there is none.
     */
    String getCurrentPageName();


    // ----------------------------------------------------------
    /**
     * Retrieve the relative path name of the current ZHTML file, such as
     * "/Fall09/mypid/index.zhtml" or "/Fall09/mypid/lab02/lab02.zhtml".
     * 
     * @return The name path to the current ZHTML file, or "" if there is none.
     */
    String getCurrentPagePath();


    // ----------------------------------------------------------
    /**
     * Get a parameter passed to this page in the query part of the URL.
     * 
     * @param name
     *            The name of the parameter to retrieve
     * @return The parameter's value on the current page, or null if there is
     *         none.
     */
    String getPageParameter( String name );


    // ----------------------------------------------------------
    /**
     * Get a parameter stored in the current session.
     * 
     * @param name
     *            The name of the parameter to retrieve
     * @return The parameter's value in the current session, or null if there is
     *         none.
     */
    Object getSessionParameter( String name );


    // ----------------------------------------------------------
    /**
     * Store a value in the current session. If a value already exists for the
     * given name, it is replaced.
     * 
     * @param name
     *            The name of the parameter to store
     * @param value
     *            The value to store
     * @return The previous value in the current session associated with the
     *         given name, if there is one, or null otherwise.
     */
    Object setSessionParameter( String name, Object value );


    // ----------------------------------------------------------
    /**
     * Remove a parameter stored in the current session, if it exists.
     * 
     * @param name
     *            The name of the parameter to remove
     * @return The removed value, if the parameter existed, or null if there is
     *         no value to remove.
     */
    Object removeSessionParameter( String name );


    /**
     * Get the cache for a given persistence id. This cache stores key to
     * StoredObject Mappings.
     * 
     * @param cacheId
     *            the id of the cache to retrieve
     * @return the cache for StoredObjects
     */
    Map<String, PersistentStorageManager.StoredObject> getPersistentCache(
        String cacheId );


    /**
     * Create a new cache for a given cacheId. This will remove the old cache
     * and initialize a new empty cache.
     * 
     * @param cacheId
     *            the cache id to wipe and re initialize
     * @return a brand new cache for stored objects
     */
    Map<String, PersistentStorageManager.StoredObject> initPersistentCache(
        String cacheId );


    /**
     * Attempt to get an alias for a given object. If the object is unaliasable,
     * the original object is returned.
     * 
     * @param value
     *            the value to attempt to retrieve an alias for.
     * @return the alias object representing the object.
     */
    Object getAlias( Object value );


    /**
     * Look at an object and determine if it is an alias. If so, resolve the
     * alias and return the aliased object. Otherwise return null.
     * 
     * @param alias
     *            the object that is a potential alias
     * @return the original object or null if it is not an alias.
     */
    Object resolveAlias( Object alias );


    /**
     * Get a reflection provider for this application type.
     * 
     * @return The reflection provider for this application type.
     */
    ReflectionProvider getReflectionProvider();


    /**
     * Get an input stream for a given File. This inputstream will contain the
     * xml to reconstitute an object from.
     * 
     * @param src
     *            the file to treat as a key
     * @return InputStream containing Object XML
     */
    InputStream getObjectSource( File src );


    /**
     * Get an output stream to write an object to. The file is treated as a key.
     * 
     * @param dest
     *            the key to lookup the object output
     * @return an OutputStream to write the object to.
     */
    OutputStream getObjectOutput( File dest );


    /**
     * Get the File representing the Base Directory of the Persistence Store.
     * 
     * @return the base directoyr fo the persistence store.
     */
    File getPersistentBase();


    /**
     * Get a file out of the Persistent Store.
     * 
     * @param dir
     *            the directory in the persistent store.
     * @return a persisted file.
     */
    File getPersistentFile( String dir );


    /**
     * Get a persisted file using a parent directory.
     * 
     * @param baseDir
     *            the base directory
     * @param dir
     *            the sub directory
     * @return a file in the persistent store
     */
    File getPersistentFile( File baseDir, String dir );


    /**
     * Get the Session Persistent Map for this application
     * 
     * @return Session persistent map.
     */
    Map<String, Object> getSessionPersistentMap();

}
