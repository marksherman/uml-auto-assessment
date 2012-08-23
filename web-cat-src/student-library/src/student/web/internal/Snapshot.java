package student.web.internal;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.BidiMap;


/**
 * This snapshot class stores all of the context information about an object
 * that has been retrieved from the persistence store. When an object is
 * unmarshled, the information retrieved is stored in this class. Then when the
 * object is later marshaled, this information is used to compute which fields
 * within the original object have been changed. All changed fields are then
 * loaded into the newest object retrieved from the store and persisted. This
 * snapshot also allows the developer to store context relating to collections,
 * maps, and arrays. The context for these are different then normal field sets.
 * For these objects, we must remember the old ordering of objects so we can
 * merge changes from the persistence store.
 *
 * @author mjw87
 *
 */
public class Snapshot
{
    
    // This snapshot records all of the information about he object currently in
    // use by the application
    private static Snapshot local = null;

    // This snapshot records all of the information about the newest version of
    // the object that has been retrieved from the store.
    private static Snapshot newest = null;


    /**
     * Clear the snapshot of the current local version of the object.
     */
    public static void clearLocal()
    {
        local = null;
    }


    /**
     * Clear the statically stored Newest snapshot.
     */
    public static void clearNewest()
    {
        newest = null;
    }


    /**
     * Get the snapshot for the object in use in the application.
     *
     * @return The snapshot.
     */
    public static Snapshot getLocal()
    {
        return local;
    }


    /**
     * Get the newest snapshot found for the current object being persisted
     * or retrieved.
     *
     * @return The newest snapshot.
     */
    public static Snapshot getNewest()
    {
        return newest;
    }


    /**
     * This looks up the id for the source object using local context from
     * when the object was retrieved or a snapshot tagged as the new version
     * of an object.
     *
     * @param source The object to find the id for.
     * @param generate Indicates whether to generate a new id for the object.
     * @return The ID.
     */
    public static UUID lookupId( Object source, boolean generate )
    {
        UUID id = local.findId( source );
        if ( id != null )
        {
            return id;
        }
        if ( newest != null )
            id = newest.findId( source );
        if ( generate )
        {
            id = UUID.randomUUID();
        }
        return id;
    }


    /**
     * Set the local snapshot that contains all of the information about the
     * object currently in use by the application.
     *
     * @param snapshot The snapshot to remember as the local snapshot.
     */
    public static void setLocal( Snapshot snapshot )
    {
        local = snapshot;
    }


    /**
     * Set the newest snapshot found for the current object being analyzed by
     * the persistence store.
     *
     * @param snapshot   The snapshot to remember as the newest snapshot.
     */
    public static void setNewest( Snapshot snapshot )
    {
        newest = snapshot;
    }

    Map<Object, Map<String, Object>> objToFieldSet =
        new IdentityHashMap<Object, Map<String, Object>>();

    Map<Object, Collection<Object>> objToOrigObj =
        new IdentityHashMap<Object, Collection<Object>>();

    // <Object,UUID>
    BidiMap objToUuid = new DualBidiIdentityHashMap<Object, UUID>();


    /**
     * Using a provided object, find the UUID that identifies it or null if
     * there is none.
     *
     * @param source
     *            the object to lookup a UUID for
     * @return the UUID that identifies this object in the persistence store.
     */
    public UUID findId( Object source )
    {
        return (UUID)objToUuid.get( source );
    }


    /**
     * Find the object that is identified by a UUID. This is limited to the
     * scope of the Snapshot.
     *
     * @param collectionId
     *            The UUID to lookup an object for
     * @return The object corresponding to this id
     */
    public Object findObject( UUID collectionId )
    {
        return this.objToUuid.getKey( collectionId );
    }


    /**
     * Get the Base object associated with a specified UUID. This will only be
     * non null if the UUID identifies a collection, map, or array. Otherwise,
     * there is no base object. This method is provided to obtain original
     * collections to identify a different ordering in a list.
     *
     * @param sourceId
     *            The UUID to lookup
     * @return the list with the original ordering as seen when the object was
     *         originally retrieved from the store.
     */
    public Object getBaseObject( UUID sourceId )
    {
        Object orig = this.objToUuid.getKey( sourceId );
        if ( orig == null )
            return null;
        return objToOrigObj.get( orig );
    }


    /**
     * Using an Id, lookup the corresponding fieldset as seen when the object
     * was retrieved from the store.
     *
     * @param id
     *            the identifier for the object.
     * @return the field set corresponding to the id.
     */
    public Map<String, Object> getFieldSetFromId( UUID id )
    {
        if ( id == null )
            return null;
        Object source = objToUuid.getKey( id );
        return objToFieldSet.get( source );
    }


    /**
     * Using an object reference, lookup the corresponding fieldset from the
     * store.
     *
     * @param source The object to look up.
     * @return The fieldset for the source object.
     */
    public Map<String, Object> getFieldSetFromObject( Object source )
    {
        return objToFieldSet.get( source );
    }


    /**
     * Resolve a Collection or array to this snapshot. This will track the
     * object for eventual persistence back to the store.
     *
     * @param id
     *            the UUID to associate with this object
     * @param result
     *            the object that has been retrieved from the store.
     * @param baseCollection
     *            The collection ordering as it was seen when the object was
     *            retrieved.
     */
    public void resolveObject(
        UUID id,
        Object result,
        Collection<Object> baseCollection )
    {
        resolveObject0( id, result, null );
        objToOrigObj.put( result, baseCollection );
    }


    /**
     * Resolve a non collection or array object in this snapshot.
     *
     * @param id
     *            the UUID to associate with this object
     * @param result
     *            the result of retrieving the object from the store.
     * @param fields
     *            The fieldset that was present when the object was last
     *            retrieved.
     */
    public void resolveObject(
        UUID id,
        Object result,
        Map<String, Object> fields )
    {
        resolveObject0( id, result, fields );
    }


    private void resolveObject0(
        UUID id,
        Object result,
        Map<String, Object> fields )
    {
        objToUuid.put( result, id );
        objToFieldSet.put( result, fields );
    }
}
